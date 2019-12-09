package com.zhjs.scfcloud.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.AuditLogListDTO;
import com.zhjs.scfcloud.model.dto.CommonAuditCommitDTO;
import com.zhjs.scfcloud.model.dto.CustomerDTO;
import com.zhjs.scfcloud.model.dto.credit.*;
import com.zhjs.scfcloud.model.entity.*;
import com.zhjs.scfcloud.model.enums.OperatObjectEnum;
import com.zhjs.scfcloud.model.enums.OperatTypeEnum;
import com.zhjs.scfcloud.model.mapper.*;
import com.zhjs.scfcloud.model.transfer.CustomerTransfer;
import com.zhjs.scfcloud.model.util.BusinessCfgUtil;
import com.zhjs.scfcloud.model.util.BusinessUtil;
import com.zhjs.scfcloud.model.vo.business.BusinessAttrCfgVO;
import com.zhjs.scfcloud.model.vo.business.BusinessWorkFlowExtendVO;
import com.zhjs.scfcloud.model.vo.credit.*;
import com.zhjs.scfcloud.system.service.*;
import com.zhjs.scfcloud.util.enums.BusinessFlowEnum;
import com.zhjs.scfcloud.util.enums.CommonEnum;
import com.zhjs.scfcloud.util.util.DateUtil;
import com.zhjs.scfcloud.util.util.JsonUtil;
import com.zhjs.scfcloud.util.util.ListUtil;
import com.zhjs.scfcloud.util.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 授信申请的业务逻辑接口实现类
 * <功能详细描述>
 * Version: 1.0.0, 2019-06-11 16:00
 *
 * @author liuchanghai
 * @create 2019-06-11 16:00
 * @since
 */

@Service
public class CreditApplyServiceImpl implements CreditApplyService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    private CreditApplyMapper creditApplyMapper;
    @Resource
    private CreditItemMapper creditItemMapper;
    @Resource
    private CreditItemService creditItemService;
    @Resource
    private CreditRiskMapper creditRiskMapper;
    @Resource
    private CustomerTransfer customerTransfer;
    @Resource
    private CustomerService customerService;
    @Resource
    private FileMapper fileMapper;
    @Resource
    private CreditApplyAuditMapper creditApplyAuditMapper;
    @Resource
    private OperateLogMapper operateLogMapper;
    @Autowired
    private CreditRecordService creditRecordService;
    @Autowired
    private FileService fileService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private UserService userService;
    @Resource
    private CreditAuditDataMapper creditAuditDataMapper;
    @Resource
    private AuditService auditService;

    /**
     * 授信审核提交
     * @param dto 入参
     * @return
     */
    @Transactional
    @Override
    public Result<User> auditCommit(CommonAuditCommitDTO dto) {
        BusinessType businessType = BusinessCfgUtil.getBusinessType(dto.getCompanyId(), "申请授信");
        if(businessType == null){
            return Result.failure("无申请授信业务配置数据");
        }
        dto.setBusinessTypeId(businessType.getId());
        //调用通用审批方法
        dto.setBusinessIdColumnName("credit_apply_id"); //设置业务对应id

        /*通用处理以外的处理*/
        // 获取授信申请信息
        CreditApply creditApply = creditApplyMapper.selectById(dto.getBusinessId());
        // 获取流程list
        List<BusinessFlow> businessFlowList = BusinessCfgUtil.getBusinessFlowList(creditApply.getCompanyId(), creditApply.getBusinessTypeId(), null);
        if(businessFlowList == null || businessFlowList.isEmpty()){
            return Result.failure("无申请授信业务流程状态配置");
        }

        // 获取当前流程
        BusinessFlow beforeFlow = BusinessCfgUtil.getBusinessFlowByFlowCode(creditApply.getFlowCode(), businessFlowList);
        if(beforeFlow == null){
            return Result.failure("获取当前流程状态配置失败");
        }

        if(!beforeFlow.getFlowCode().equals(dto.getBeforeFlow())){
            return Result.failure("授信审核提交错误！申请单当前状态为：【"+beforeFlow.getFlowName()+"】");
        }

        // 获取下一流程
        BusinessFlow afterFlow = BusinessCfgUtil.getBusinessFlowByFlowCode(dto.getAfterFlow(), businessFlowList);
        if(afterFlow == null){
            return Result.failure("获取下一流程状态配置失败");
        }

        Result commonAuditResult = auditService.commonAuditCommit(dto);
        if (commonAuditResult.getCode() == Result.RESULT_CODE_FAILURE)
            return commonAuditResult;

        // 更新审核申请表的流程CODE
        creditApply.setDisposeUserIds(StringUtil.isEmpty(dto.getDisposeUserIds())?null:dto.getDisposeUserIds())
                    .setDisposeRoleIds(StringUtil.isEmpty(dto.getDisposeRoleIds())?null:dto.getDisposeRoleIds())
                    .setFlowCode(dto.getAfterFlow())
                    .setUpdateTime(new Date()).setUpdateBy(dto.getAuditPersonId());
        //如果是退回草稿状态  处理人改为创建人
        if(afterFlow.getFlowType() == 0){
            creditApply.setDisposeUserIds(creditApply.getCreateBy().toString())
                    .setDisposeRoleIds(null);
        }
        creditApplyMapper.updateById(creditApply);

        // 判断当前流程是否是授信完成
        if(afterFlow.getFlowType() == 10){
            //提取客户信息
            CustomerDTO customerDTO = customerTransfer.creditApply2CustomerDTO(creditApply);
            customerDTO.setUserId(dto.getAuditPersonId());
            //覆盖配置字段的值
            customerDTO.setGender(BusinessCfgUtil.getBusinessAttrVal(Long.parseLong(customerDTO.getGender()),dto.getCompanyId(),dto.getBusinessTypeId(),null));
            customerDTO.setMaritalStatus(BusinessCfgUtil.getBusinessAttrVal(Long.parseLong(customerDTO.getMaritalStatus()),dto.getCompanyId(),dto.getBusinessTypeId(),null));
            customerDTO.setHousingOwnership(BusinessCfgUtil.getBusinessAttrVal(Long.parseLong(customerDTO.getHousingOwnership()),dto.getCompanyId(),dto.getBusinessTypeId(),null));
            //保存客户
            Result result = customerService.updateOrSaveByIdCard(customerDTO);
            if (result.getCode() == Result.RESULT_CODE_FAILURE)
                throw new RuntimeException("保存客户信息失败");
            //获取申请user （授信创建人即申请人）
            User user = userService.getById(creditApply.getCreateBy());
            if (user == null)
                throw new RuntimeException("申请人不存在");
            //设置身份证号
            user.setIdCard(customerDTO.getIdCard()).setUpdateTime(LocalDateTime.now()).setUpdateById(dto.getAuditPersonId());
            //更新申请人对应用户
            if (!userService.updateById(user))
                throw new RuntimeException("更新申请用户失败");

            logger.info("生成授信记录:{} ", creditApply.toString());
            LambdaQueryWrapper<CreditItem> creditItemLambda = new QueryWrapper<CreditItem>().lambda();
            creditItemLambda.eq(CreditItem::getCreditApplyId,creditApply.getId());
            List<CreditItem> creditItems = creditItemMapper.selectList(creditItemLambda);
            if(creditItems.size() > 0){
                List<CreditRecord> creditRecords = new ArrayList<>();
                for (CreditItem item : creditItems){
                    CreditRecord record = new CreditRecord();
                    record.setIdCard(creditApply.getIdCard())
                        .setCompanyId(item.getCompanyId())
                        .setProjectId(item.getId())
                        .setProjectName(item.getItemName())
                        .setCreditApplyId(creditApply.getId())
                        .setIdCard(creditApply.getIdCard())
                        .setCustomerName(creditApply.getCustomerName())
                        .setCreditAmount(new BigDecimal(item.getAuditCreditMoney()))
                        .setIfImport(CommonEnum.WhetherEnum.NO.getStatus())
                        .setCreditStart(item.getCreditStartDate())
                        .setCreditEnd(item.getCreditEndDate())
                        .setDeleteStatus(CommonEnum.WhetherEnum.NO.getStatus())
                        .setCreateBy(dto.getAuditPersonId())
                        .setCreateTime(new Date());
                    creditRecords.add(record);
                }
                if(creditRecords.size() > 0){
                    // 保存授信记录
                    creditRecordService.saveBatch(creditRecords);
                    logger.info("保存授信记录:{}",creditRecords.toString());
                }
            }
            return Result.success(user);
        }
        return Result.success("审核成功");
    }

    /**
     * 更新表单字段的值并返回修改内容
     * @param businessId 业务ID
     * @param companyId 公司ID
     * @param businessTypeId 业务类型ID
     * @param columnName 字段名
     * @param columnValue 字段值
     * @return
     */
    public String updateCreditApplyFormData(Long businessId, Long companyId, Long businessTypeId,Long projectId,
                     String columnName, String columnValue) {
        String editContent = null;
        String[] strings = columnName.split("_");
        String tableName = "scf_" + StringUtil.toUnderlineName(strings[0]);
        columnName = StringUtil.toUnderlineName(strings[1]);
        String oldValue = creditApplyAuditMapper.findFieldOldValueBy(companyId, businessId, businessTypeId, projectId, tableName, columnName);
        int update = creditApplyAuditMapper.updateFieldNewValueBy(companyId, businessId, businessTypeId, projectId, tableName, columnName, columnValue);
        if(update != 0){
            if (StringUtil.isEmpty(oldValue)){
                editContent = "由空值改为" + columnValue;
            }else {
                editContent = "由" + oldValue +"改为" + columnValue;
            }
            return editContent;
        }
        return editContent;
    }

    /**
     * 查询授信审核日记
     * @param auditLogListDTO 入参 授信申请ID
     * @return
     */
    @Override
    public String findAuditLogList(AuditLogListDTO auditLogListDTO) {
        Map retMap = new HashMap();
        List<CreditAuditVO> creditAuditVOList = creditApplyAuditMapper.findAuditLogList(auditLogListDTO);
        retMap.put("total",auditLogListDTO.getTotal());
        retMap.put("list",creditAuditVOList);
        return Result.success(retMap).toSerializerJSON();
    }

    //授信申请保存文件
    @Override
    @Transactional
    public String saveFile(CreditApplyFileSaveDTO creditApplyFileSaveDTO) {
        Date sysDate = new Date();
        //是否有业务id 没有则新建
        if (creditApplyFileSaveDTO.getBusinessId() == null  ) {
            CreditApply creditApply = new CreditApply();
            creditApply.setCreateTime(sysDate);
            creditApplyMapper.insert(creditApply);
            creditApplyFileSaveDTO.setBusinessId(creditApply.getId());
        }

        List<File> fileList =  creditApplyFileSaveDTO.getFileList();

        //保存文件
        fileList.stream().forEach(file -> {
           //有id的不用保存
            if (file.getId() == null) {
                file.setBusinessId(creditApplyFileSaveDTO.getBusinessId())
                        .setBusinessTypeId(BusinessCfgUtil.getBusinessTypeId(creditApplyFileSaveDTO.getCompanyId(),"申请授信"))
                        .setStatus(CommonEnum.WhetherEnum.YES.getStatus())
                        .setUpdateTime(sysDate)
                        .setCreateTime(sysDate);
                if (fileMapper.insert(file) == 0)
                    throw new RuntimeException("文件保存失败");
            }
        });

        Map retMap = new HashMap();
        retMap.put("fileList",fileList);
        retMap.put("businessId",creditApplyFileSaveDTO.getBusinessId());

        return Result.success(retMap).toJSON();
    }

    //授信业务保存草稿
    @Override
    @Transactional
    public String saveDraft(CreditApplyDraftSaveDTO creditApplyDraftSaveDTO) {
        BusinessType businessType = BusinessCfgUtil.getBusinessType(creditApplyDraftSaveDTO.getCompanyId(), "申请授信");
        if(businessType == null){
            return Result.failure("无申请授信业务配置数据").toJSON();
        }
        //是否有业务id 没有则新建
        if (creditApplyDraftSaveDTO.getBusinessId() == null  ) {
            CreditApply creditApply = new CreditApply();
            creditApply.setCompanyId(creditApplyDraftSaveDTO.getCompanyId())
                    .setBusinessTypeId(businessType.getId())
                    .setDisposeUserIds(creditApplyDraftSaveDTO.getUserId().toString())   //处理人为自己
                    .setFlowCode("credit1").setCreateBy(creditApplyDraftSaveDTO.getUserId())
                    .setCreateTime(new Date());
            creditApplyMapper.insert(creditApply);
            creditApplyDraftSaveDTO.setBusinessId(creditApply.getId());
            //添加操作日志
            OperateLog operateLog = new OperateLog();
            operateLog.setBusinessId(creditApplyDraftSaveDTO.getBusinessId())
                    .setBusinessTypeId(businessType.getId())
                    .setCompanyId(creditApplyDraftSaveDTO.getCompanyId())
                    .setOperatType(OperatTypeEnum.add.getValue())
                    .setOperatObject(OperatObjectEnum.credit.getValue())
                    .setOperateContent("创建")
                    .setOperateTime(new Date())
                    .setOperateUserId(creditApplyDraftSaveDTO.getUserId())
                    .setOperateUserName(creditApplyDraftSaveDTO.getUserName());
            if ( operateLogMapper.insert(operateLog) != 1 )
                throw new RuntimeException("添加操作日志失败");
        }
        //保存的字段和值的map
        Map<String,Object> columnValueMap = new HashMap<>();
        String fieldName = creditApplyDraftSaveDTO.getTableName().split("\\_")[1];
        columnValueMap.put(fieldName, creditApplyDraftSaveDTO.getValue());
        //判断是否是省code
        if ("familyAddressProvinceCode".equals(fieldName)) {
            String[] value = creditApplyDraftSaveDTO.getValue().toString().split(" ");
            columnValueMap.put("familyAddressProvinceCode",value[0]);
            if (value.length >1) columnValueMap.put("familyAddressCityCode",value[1]);
            if (value.length >2) columnValueMap.put("familyAddressRegionCode",value[2]);
        }
        //根据表名 插入到不同的表
        switch (creditApplyDraftSaveDTO.getTableName().split("\\_")[0]) {
            case "creditApply":
                //将map的字段和值 赋予对象
                CreditApply creditApply = JsonUtil.mapToBean(columnValueMap,CreditApply.class);
                creditApply.setId(creditApplyDraftSaveDTO.getBusinessId()).setUpdateTime(new Date()).setUpdateBy(creditApplyDraftSaveDTO.getUserId());
                creditApplyMapper.updateByPrimaryKeySelective(creditApply);
                break;
            case "creditItem":
                if ("itemName".equals(fieldName)) {
                    List<CreditItem> creditItemList = creditItemMapper.selectList(new QueryWrapper<CreditItem>()
                            .eq("credit_apply_id", creditApplyDraftSaveDTO.getBusinessId())
                            .eq("item_name", creditApplyDraftSaveDTO.getValue().toString()));
                    if (!ListUtil.isEmpty(creditItemList)) {
                        CreditItem creditItemByName = creditItemList.get(0);
                        if (creditItemByName.getItemIndex().intValue() != creditApplyDraftSaveDTO.getIndex().intValue()){
                            return Result.failure("项目名称已被使用").toJSON();
                        }
                    }
                }
                CreditItem creditItem = creditItemMapper.selectOne(new QueryWrapper<CreditItem>()
                        .eq("credit_apply_id", creditApplyDraftSaveDTO.getBusinessId())
                        .eq("item_index", creditApplyDraftSaveDTO.getIndex()));
                if (creditItem == null) {
                    creditItem = JsonUtil.mapToBean(columnValueMap,CreditItem.class);
                    creditItem.setCreditApplyId(creditApplyDraftSaveDTO.getBusinessId())
                            .setCompanyId(creditApplyDraftSaveDTO.getCompanyId())
                            .setItemIndex(creditApplyDraftSaveDTO.getIndex())
                            .setCreateTime(new Date()).setCreateBy(creditApplyDraftSaveDTO.getUserId());
                    creditItemMapper.insert(creditItem);
                } else {
                    Map<String,Object> creditItemMap = JsonUtil.beanToMap(creditItem);
                    creditItemMap.putAll(columnValueMap);
                    creditItem = JsonUtil.mapToBean(creditItemMap,CreditItem.class);
                    creditItem.setUpdateTime(new Date()).setUpdateBy(creditApplyDraftSaveDTO.getUserId());
                    creditItemMapper.updateById(creditItem);
                }
                break;
            case "creditRisk":
                //通过授信业务id查询授信风险信用信息（两者一对一）
                CreditRisk creditRisk = creditRiskMapper.selectOne(new QueryWrapper<CreditRisk>().eq("credit_apply_id", creditApplyDraftSaveDTO.getBusinessId()));
                if (creditRisk == null) {
                    creditRisk = JsonUtil.mapToBean(columnValueMap,CreditRisk.class);
                    creditRisk.setCreditApplyId(creditApplyDraftSaveDTO.getBusinessId())
                            .setCompanyId(creditApplyDraftSaveDTO.getCompanyId())
                            .setCreateTime(new Date()).setCreateBy(creditApplyDraftSaveDTO.getUserId());
                    creditRiskMapper.insert(creditRisk);
                } else {
                    Map<String,Object> creditRiskMap = JsonUtil.beanToMap(creditRisk);
                    creditRiskMap.putAll(columnValueMap);
                    creditRisk = JsonUtil.mapToBean(creditRiskMap,CreditRisk.class);
                    creditRisk.setUpdateTime(new Date()).setUpdateBy(creditApplyDraftSaveDTO.getUserId());
                    creditRiskMapper.updateById(creditRisk);
                }
                break;
            default:return Result.failure("参数匹配失败").toJSON();
        }

        Map retMap = new HashMap();
        retMap.put("businessId",creditApplyDraftSaveDTO.getBusinessId());

        return Result.success(retMap).toJSON();
    }

    //授信申请提交
    @Override
    @Transactional
    public String commit(CreditApplyCommitDTO creditApplyCommitDTO) {
        BusinessType businessType = BusinessCfgUtil.getBusinessType(creditApplyCommitDTO.getCompanyId(), "申请授信");
        if(businessType == null){
            return Result.failure("无申请授信业务配置数据").toJSON();
        }
        //获取校验的参照集
        List<BusinessAttrCfgVO> businessAttrCfgVOList = BusinessCfgUtil.getBusinessAttrCfgVOList(
                creditApplyCommitDTO.getCompanyId(),
                businessType.getId()
                ,CommonEnum.WhetherEnum.YES.getStatus());
        businessAttrCfgVOList =
                businessAttrCfgVOList.stream()
                        .filter(attrCfg -> attrCfg.getHasCreateVisible() == 1)
                        .filter(attrCfg -> !"item".equals(attrCfg.getColumnGroup())).collect(Collectors.toList());

        Map<String, Object> details = creditApplyMapper.findCreditApplyDetails(creditApplyCommitDTO.getBusinessId());
        if (details == null) return Result.failure("授信申请信息为空").toJSON();

        //判断重复提交
        List<BusinessFlow> flowList = BusinessCfgUtil.getBusinessFlowList(businessType.getCompanyId(),businessType.getId(), CommonEnum.StatusEnnum.status1.getStatus());
        if(flowList == null || flowList.isEmpty()){
            return Result.failure("无申请授信业务流程状态配置").toJSON();
        }

        String flowCode = details.get("creditApply_flowCode").toString();
        BusinessFlow businessFlow =
                flowList.stream().filter(flow -> flow.getFlowCode().equals(flowCode)).findAny().orElse(null);
        if(businessFlow.getFlowType().intValue() != BusinessFlowEnum.FlowType.FLOW_TYPE0.getStatus().intValue()){
            return Result.failure("提交申请错误！申请单当前状态为：【"+businessFlow.getFlowName()+"】").toJSON();
        }

        Set<String> keys = details.keySet();
        List<AuditFormDTO> dataFormList = keys.stream().filter(key -> details.get(key) != null).map( key -> {
            AuditFormDTO form = new AuditFormDTO();
            form.setColumnName(key);
            form.setColumnValue(details.get(key).toString());
            return form;
        }).collect(Collectors.toList());

        //校验非项目信息字段
        Result validResult =  BusinessCfgUtil.validAttr(null, dataFormList, businessAttrCfgVOList, CommonEnum.QueryType.EDIT.getStatus());
        if (validResult.getCode() == Result.RESULT_CODE_FAILURE)
            return validResult.toJSON();

        //查询申请信息
//        CreditApply creditApply = creditApplyMapper.selectById(creditApplyCommitDTO.getBusinessId());
//        if (creditApply == null) return Result.failure("授信申请信息为空").toJSON();
//        if (!"credit1".equals(creditApply.getFlowCode())) return Result.failure("已提交").toJSON();
//        //校验creditApply
//        Result validResult =  BusinessCfgUtil.validBusinessAttr("creditApply"
//                ,creditApply,businessAttrCfgVOList,CommonEnum.QueryType.CREATE.getStatus());
//        if (validResult.getCode() == Result.RESULT_CODE_FAILURE)
//            return validResult.toJSON();

        //查询项目信息
        List<CreditItem> creditItemList = creditItemMapper.selectList(new QueryWrapper<CreditItem>().eq("credit_apply_id",creditApplyCommitDTO.getBusinessId()));
        if (creditItemList == null || creditItemList.isEmpty())
            return Result.failure("授信申请项目信息为空").toJSON();
        //校验creditItem
        for (CreditItem creditItem: creditItemList) {
            validResult =  BusinessCfgUtil.validBusinessAttr("creditItem"
                    ,creditItem,businessAttrCfgVOList,CommonEnum.QueryType.CREATE.getStatus());
            if (validResult.getCode() == Result.RESULT_CODE_FAILURE)
                return validResult.toJSON();
        }

        //查询风险信息
//        CreditRisk creditRisk = creditRiskMapper.selectOne(new QueryWrapper<CreditRisk>().eq("credit_apply_id",creditApplyCommitDTO.getBusinessId()));
//        if (creditRisk == null)
//            return Result.failure("授信申请风险信息为空").toJSON();
//        //校验creditRisk
//        validResult =  BusinessCfgUtil.validBusinessAttr("creditRisk"
//                , creditRisk,businessAttrCfgVOList,CommonEnum.QueryType.CREATE.getStatus());
//        if (validResult.getCode() == Result.RESULT_CODE_FAILURE)
//            return validResult.toJSON();
//迁移至审核通过后进行处理
//        //提取用户信息
//        CustomerDTO customerDTO = customerTransfer.creditApply2CustomerDTO(creditApply);
//        customerDTO.setUserId(creditApplyCommitDTO.getUserId());
//        //保存用户
//        Result result = customerService.updateOrSaveByIdCard(customerDTO);
//        if (result.getCode() == Result.RESULT_CODE_FAILURE)
//            return result.toJSON();

        //查询下一步处理人配置
        List<BusinessWorkFlowExtendVO> businessWorkFlowExtendVOList =BusinessCfgUtil.getBusinessWorkFlowExtendListByFlowCode("credit2",
                BusinessCfgUtil.getBusinessWorkFlowExtendList(creditApplyCommitDTO.getCompanyId(),businessType.getId()));
        if (businessWorkFlowExtendVOList == null || businessWorkFlowExtendVOList.isEmpty())
            throw new RuntimeException("无下一步处理人配置");
        String disposeRoleIds = StringUtil.stringDistinct(businessWorkFlowExtendVOList.stream().filter(vo -> !StringUtil.isEmpty(vo.getRoleIds())).map(vo -> vo.getRoleIds()).collect(Collectors.joining(",")), ",");
        String disposeUserIds = StringUtil.stringDistinct(businessWorkFlowExtendVOList.stream().filter(vo -> !StringUtil.isEmpty(vo.getUserIds())).map(vo -> vo.getUserIds()).collect(Collectors.joining(",")), ",");

        //更新授信申请表
        CreditApply creditApply = creditApplyMapper.selectById(creditApplyCommitDTO.getBusinessId());
        creditApply.setDisposeUserIds(StringUtil.isEmpty(disposeUserIds)?null:disposeUserIds)
                .setDisposeRoleIds(StringUtil.isEmpty(disposeRoleIds)?null:disposeRoleIds)
                .setFlowCode("credit2")
                .setApplyTime(new Date())
                .setUpdateTime(new Date())
                .setUpdateBy(creditApplyCommitDTO.getUserId());
        if (creditApplyMapper.updateByPrimaryKey(creditApply) != 1)
            throw new RuntimeException("更新授信申请表失败");

        //添加业务审批表此业务id的第一条记录
        CreditApplyAudit creditApplyAudit = new CreditApplyAudit();
        creditApplyAudit.setCompanyId(creditApplyCommitDTO.getCompanyId())
                .setBusinessTypeId(businessType.getId())
                .setBusinessId(creditApplyCommitDTO.getBusinessId())
                .setAuditBeforeFlow("credit1").setAuditAfterFlow("credit2")
                .setAuditTime(new Date())
                .setCreateTime(new Date()).setCreateBy(creditApplyCommitDTO.getUserId());
        if (creditApplyAuditMapper.insert(creditApplyAudit) != 1) {
            throw new RuntimeException("初始化进度信息失败");
        }

        //添加操作日志
        OperateLog operateLog = new OperateLog();
        operateLog.setBusinessId(creditApplyCommitDTO.getBusinessId())
                .setBusinessTypeId(businessType.getId())
                .setCompanyId(creditApplyCommitDTO.getCompanyId())
                .setOperatType(OperatTypeEnum.commit.getValue())
                .setOperatObject(OperatObjectEnum.credit.getValue())
                .setOperateContent("草稿->提交")
                .setOperateTime(new Date())
                .setOperateUserId(creditApplyCommitDTO.getUserId())
                .setOperateUserName(creditApplyCommitDTO.getUserName());
        if ( operateLogMapper.insert(operateLog) != 1 )
            throw new RuntimeException("添加操作日志失败");

        return Result.success().toJSON();
    }

    //授信申请我的申请
    @Override
    public String myApplyList(CreditApplyMyListQueryDTO creditApplyMyListQueryDTO) {
        Map retMap =  new HashMap();
        retMap.put("list",new ArrayList<>());
        retMap.put("total",0);

        //web端请求
        if ("web".equals(creditApplyMyListQueryDTO.getClient())) {
            //查询申请列表
            List<CreditApplyMyListWebVO> creditApplyMyListWebVOList = creditApplyMapper.selectMyApplyWebList(creditApplyMyListQueryDTO);
            if (creditApplyMyListWebVOList == null || creditApplyMyListWebVOList.isEmpty())
                return Result.success(retMap).toJSON();
            List<Long> idList = creditApplyMyListWebVOList.stream().map(creditApplyMyListWebVO -> creditApplyMyListWebVO.getId()).collect(Collectors.toList());
            //查询项目信息
            List<CreditItem> creditItemList = creditItemMapper.selectList(new QueryWrapper<CreditItem>().in("credit_apply_id",idList));
            Map<Long,List<CreditItem>> mapByApplyId = creditItemList.stream().collect(Collectors.groupingBy(creditItem -> creditItem.getCreditApplyId()));
            //将项目信息拼装到申请列表中
            creditApplyMyListWebVOList.stream().forEach(creditApplyMyListWebVO -> {
                Long applyId = creditApplyMyListWebVO.getId();
                List<CreditItem> itemList = mapByApplyId.get(applyId);
                if (itemList != null && !itemList.isEmpty()) {
                    creditApplyMyListWebVO.setAuditCreditBeginDate(itemList.stream().map(creditItem -> creditItem.getCreditStartDate()==null?"":DateUtil.format(creditItem.getCreditStartDate(),"yyyy-MM-dd")).collect(Collectors.joining("、")));
                    creditApplyMyListWebVO.setAuditCreditEndDate(itemList.stream().map(creditItem -> creditItem.getCreditEndDate()==null?"":DateUtil.format(creditItem.getCreditEndDate(),"yyyy-MM-dd")).collect(Collectors.joining("、")));
                    creditApplyMyListWebVO.setApplyCreditMoney(itemList.stream().map(creditItem -> creditItem.getApplyCreditMoney()==null?"":creditItem.getApplyCreditMoney().toString()).collect(Collectors.joining("、")));
                    creditApplyMyListWebVO.setAuditCreditMoney(itemList.stream().map(creditItem -> creditItem.getAuditCreditMoney()==null?"":creditItem.getAuditCreditMoney().toString()).collect(Collectors.joining("、")));
                    creditApplyMyListWebVO.setItemNames(itemList.stream().map(creditItem -> creditItem.getItemName()==null?"":creditItem.getItemName()).collect(Collectors.joining("、")));
                }
            });
            retMap.put("total",creditApplyMyListQueryDTO.getTotal());
            retMap.put("list",creditApplyMyListWebVOList);
            return Result.success(retMap).toSerializerJSON();
        }
        //app端请求
        else {
            //查询申请列表
            List<CreditApplyMyListAppVO> creditApplyMyListAppVOList = creditApplyMapper.selectMyApplyAppList(creditApplyMyListQueryDTO);
            if (creditApplyMyListAppVOList == null || creditApplyMyListAppVOList.isEmpty())
                return Result.success(retMap).toJSON();
            List<Long> idList = creditApplyMyListAppVOList.stream().filter(creditApplyMyListAppVO->{
                return creditApplyMyListAppVO.getBusinessTypeId().intValue() == 1; //筛选为授信申请业务的
            }).map(creditApplyMyListAppVO -> creditApplyMyListAppVO.getId()).collect(Collectors.toList());
            //存在授信申请时 查询授信项目信息
            if (idList != null && !idList.isEmpty()) {
                List<CreditItem> creditItemList = creditItemMapper.selectList(new QueryWrapper<CreditItem>().in("credit_apply_id", idList));
                Map<Long, List<CreditItem>> mapByApplyId = creditItemList.stream().collect(Collectors.groupingBy(creditItem -> creditItem.getCreditApplyId()));
                //将项目名称拼装到申请列表中
                creditApplyMyListAppVOList.stream().forEach(creditApplyMyListAppVO -> {
                    List<CreditItem> itemList = mapByApplyId.get(creditApplyMyListAppVO.getId());
                    if (itemList != null && !itemList.isEmpty()) {
                        creditApplyMyListAppVO.setItemNames(itemList.stream().map(creditItem -> creditItem.getItemName() == null ? "" : creditItem.getItemName()).collect(Collectors.joining("、")));
                    }
                });
            }
            retMap.put("total",creditApplyMyListQueryDTO.getTotal());
            retMap.put("list",creditApplyMyListAppVOList);
            return Result.success(retMap).toSerializerJSON();
        }
    }

    @Override
    public Result findCreditApplyDetails(CreditApplyQueryDTO dto) {
        CommonEnum.StatusEnnum status1 = CommonEnum.StatusEnnum.status1;

        //排除不可见的字段
        Map<String, Object> creditApplyDetails = creditApplyMapper.findCreditApplyDetails(dto.getCreditApplyId());
        if(creditApplyDetails == null) return Result.failure("授信申请信息丢失");
        Long companyId = Long.valueOf(creditApplyDetails.get("creditApply_companyId").toString());
        Long businessTypeId = Long.valueOf(creditApplyDetails.get("creditApply_businessTypeId").toString());

        BusinessType businessType = BusinessCfgUtil.getBusinessType(companyId, businessTypeId);
        if(businessType == null){
            return Result.failure("无申请授信业务配置数据");
        }
        //排除授信项目不可见字段
        List<Map<String, Object>> creditItemList = creditItemMapper.selectByCreditApplyId(dto.getCreditApplyId());

//        List<File> fileList = fileService.findList(dto.getCompanyId(),dto.getCreditApplyId(),businessType.getId());
//        creditApplyDetails.put("fileList",fileList);
        creditApplyDetails.put("item",creditItemList);

        return Result.success(creditApplyDetails);
    }

    @Override
    public Result findCreditApplyFile(CreditApplyQueryDTO dto) {
        //排除不可见的字段
        Map<String, Object> creditApplyDetails = creditApplyMapper.findCreditApplyDetails(dto.getCreditApplyId());
        if(creditApplyDetails == null) return Result.failure("授信申请信息丢失");
        Long companyId = Long.valueOf(creditApplyDetails.get("creditApply_companyId").toString());
        Long businessTypeId = Long.valueOf(creditApplyDetails.get("creditApply_businessTypeId").toString());
        BusinessType businessType = BusinessCfgUtil.getBusinessType(companyId, businessTypeId);
        if(businessType == null){
            return Result.failure("无申请授信业务配置数据");
        }
        return Result.success(fileService.findList(dto.getCompanyId(),dto.getCreditApplyId(),businessType.getId()));
    }


    //授信申请所有申请
    @Override
    public String allApplyList(CreditApplyAllListQueryDTO creditApplyAllListQueryDTO) {
        Map retMap =  new HashMap();
        retMap.put("list",new ArrayList<>());
        retMap.put("total",0);
        //查询申请列表
        List<CreditApplyAllListWebVO> creditApplyAllListWebVOList = creditApplyMapper.selectAllApplyList(creditApplyAllListQueryDTO);
        if (creditApplyAllListWebVOList == null || creditApplyAllListWebVOList.isEmpty())
            return Result.success(retMap).toJSON();

        List<Long> idList = creditApplyAllListWebVOList.stream().map(creditApplyAllListWebVO -> creditApplyAllListWebVO.getId()).collect(Collectors.toList());

        //查询项目信息
        List<CreditItem> creditItemList = creditItemMapper.selectList(new QueryWrapper<CreditItem>().in("credit_apply_id",idList));
        Map<Long,List<CreditItem>> mapByApplyId = creditItemList.stream().collect(Collectors.groupingBy(creditItem -> creditItem.getCreditApplyId()));
        //将项目信息拼装到申请列表中
        creditApplyAllListWebVOList.stream().forEach(creditApplyMyListWebVO -> {
            Long applyId = creditApplyMyListWebVO.getId();
            List<CreditItem> itemList = mapByApplyId.get(applyId);
            if (itemList != null && !itemList.isEmpty()) {
                creditApplyMyListWebVO.setItemNames(itemList.stream().map(creditItem -> creditItem.getItemName()==null?"":creditItem.getItemName()).collect(Collectors.joining("、")));
                creditApplyMyListWebVO.setAuditCreditEndDate(itemList.stream().map(creditItem -> creditItem.getCreditEndDate()==null?"":DateUtil.format(creditItem.getCreditEndDate(),"yyyy-MM-dd")).collect(Collectors.joining("、")));
                creditApplyMyListWebVO.setAuditCreditMoney(itemList.stream().map(creditItem -> creditItem.getAuditCreditMoney()==null?"":creditItem.getAuditCreditMoney().toString()).collect(Collectors.joining("、")));
                creditApplyMyListWebVO.setAuditCreditBeginDate(itemList.stream().map(creditItem -> creditItem.getCreditStartDate()==null?"":DateUtil.format(creditItem.getCreditStartDate(),"yyyy-MM-dd")).collect(Collectors.joining("、")));
                creditApplyMyListWebVO.setApplyCreditMoney(itemList.stream().map(creditItem -> creditItem.getApplyCreditMoney()==null?"":creditItem.getApplyCreditMoney().toString()).collect(Collectors.joining("、")));
            }
        });
        retMap.put("total",creditApplyAllListQueryDTO.getTotal());
        retMap.put("list",creditApplyAllListWebVOList);
        return Result.success(retMap).toSerializerJSON();
    }

    @Override
    public String auditList(CreditApplyAuditListQueryDTO creditApplyAuditListQueryDTO) {
        //查询申请列表
        Map retMap =  new HashMap();
        retMap.put("list",new ArrayList<>());
        retMap.put("total",0);

        List<CreditApplyAuditListWebVO> creditApplyAuditListWebVOList = creditApplyMapper.selectAuditList(creditApplyAuditListQueryDTO);
        if (creditApplyAuditListWebVOList == null || creditApplyAuditListWebVOList.isEmpty())
            return Result.success(retMap).toJSON();

        String applyIds = creditApplyAuditListWebVOList.stream().map(creditApplyAuditListWebVO -> creditApplyAuditListWebVO.getId().toString()).collect(Collectors.joining(","));

        //查询项目信息
        List<CreditItem> creditItemList = creditItemMapper.selectListByApplyIds(applyIds);
        Map<Long,List<CreditItem>> mapByApplyId = creditItemList.stream().collect(Collectors.groupingBy(creditItem -> creditItem.getCreditApplyId()));
        //将项目信息拼装到申请列表中
        creditApplyAuditListWebVOList.stream().forEach(creditApplyAuditListWebVO -> {
            Long applyId = creditApplyAuditListWebVO.getId();
            List<CreditItem> itemList = mapByApplyId.get(applyId);
            if (itemList != null && !itemList.isEmpty()) {
                creditApplyAuditListWebVO.setApplyCreditBusiness(itemList.stream().map(creditItem -> creditItem.getApplyCreditBusiness()==null?"":creditItem.getApplyCreditBusiness()).collect(Collectors.joining("、")));
                creditApplyAuditListWebVO.setAuditCreditBeginDate(itemList.stream().map(creditItem -> creditItem.getCreditStartDate()==null?"":DateUtil.format(creditItem.getCreditStartDate(),"yyyy-MM-dd")).collect(Collectors.joining("、")));
                creditApplyAuditListWebVO.setAuditCreditMoney(itemList.stream().map(creditItem -> creditItem.getAuditCreditMoney()==null?"":creditItem.getAuditCreditMoney().toString()).collect(Collectors.joining("、")));
                creditApplyAuditListWebVO.setAuditCreditEndDate(itemList.stream().map(creditItem -> creditItem.getCreditEndDate()==null?"":DateUtil.format(creditItem.getCreditEndDate(),"yyyy-MM-dd")).collect(Collectors.joining("、")));
                creditApplyAuditListWebVO.setItemNames(itemList.stream().map(creditItem -> creditItem.getItemName()==null?"":creditItem.getItemName()).collect(Collectors.joining("、")));
                creditApplyAuditListWebVO.setApplyCreditMoney(itemList.stream().map(creditItem -> creditItem.getApplyCreditMoney()==null?"":creditItem.getApplyCreditMoney().toString()).collect(Collectors.joining("、")));
            }
        });
        retMap.put("total", creditApplyAuditListQueryDTO.getTotal());
        retMap.put("list",creditApplyAuditListWebVOList);
        return Result.success(retMap).toSerializerJSON();
    }

    @Override
    @Transactional
    public Result appAuditCommit(com.zhjs.scfcloud.model.dto.credit.app.CreditAuditCommitDTO dto) {
        Date sysDate = new Date();
        //获取流程
        List<BusinessFlow> businessFlowList =
                BusinessCfgUtil.getBusinessFlowList(dto.getCompanyId(),dto.getBusinessTypeId(), CommonEnum.StatusEnnum.status1.getStatus());
        BusinessFlow beforeFlow = BusinessCfgUtil.getBusinessFlowByFlowCode(dto.getBeforeFlow(), businessFlowList);
        BusinessFlow afterFlow = BusinessCfgUtil.getBusinessFlowByFlowCode(dto.getAfterFlow(), businessFlowList);

        //如果有填写评论，则保存
        if(!StringUtil.isEmpty(dto.getCommentContent())){
            Comment comment = new Comment();
            comment.setCompanyId(dto.getCompanyId())
                    .setBusinessTypeId(dto.getBusinessTypeId())
                    .setBusinessId(dto.getCreditApplyId())
                    .setContent(dto.getCommentContent())
                    .setFlowCode(dto.getBeforeFlow())
                    .setStatusDesc("在状态["+beforeFlow.getFlowName()+"]")
                    .setCreateBy(dto.getAuditPersonId())
                    .setCreateTime(sysDate)
            ;
            commentService.save(comment);
        }

        //将审核表单数据封装
        Map<String,List<AuditFormDTO>> formMap = dto.getFormDataList().stream().collect(Collectors.groupingBy(form -> {
            String tableName = "scf_"+StringUtil.toUnderlineName(form.getColumnName().split("_")[0]);
            form.setColumnName(StringUtil.toUnderlineName(form.getColumnName().split("_")[1]));
            return tableName;
        }));

        //审核日志
        StringBuffer auditLog = new StringBuffer();
        Set<String> keys = formMap.keySet();
        List<Map<String, Object>> insertMapList = new ArrayList<>();
        //处理需要存储的字段，组装审核日志
        keys.forEach(key -> {
            List<AuditFormDTO> auditFormList = formMap.get(key);
            Map<String, Object> data = creditApplyMapper.findCreditAuditDataObj(key, dto.getCompanyId(), dto.getCreditApplyId());
            auditFormList.forEach(auditForm -> {
                Object val = data.get(auditForm.getColumnName());
                auditLog.append("/r/n").append("【"+auditForm.getColumnCh()+"】：由【"+(val == null ? "空值":val.toString())+"】改为【"+auditForm.getColumnValue()+"】");
            });
            Map<String, Object> insertMap = new HashMap<>();
            insertMap.put("tableName", key);
            insertMap.put("columns", auditFormList);
            insertMap.put("projectId", auditFormList.get(0).getProjectId());
            insertMapList.add(insertMap);
        });
        int c = creditApplyMapper.updateCreditAuditData(insertMapList,dto.getCompanyId(),dto.getCreditApplyId());
        //更新失败抛出异常进行回滚
        if(c == 0){
            new RuntimeException("记录审核数据失败！");
        }

        //创建审核记录
        // 获取最近的一条审核记录
        LambdaQueryWrapper<CreditApplyAudit> lambda = new QueryWrapper<CreditApplyAudit>().lambda();
        lambda.eq(CreditApplyAudit::getBusinessId,dto.getCreditApplyId()).eq(CreditApplyAudit::getBusinessTypeId,dto.getBusinessTypeId())
                .orderByDesc(CreditApplyAudit::getId).last("limit 1")
        ;
        CreditApplyAudit lastAudit = creditApplyAuditMapper.selectOne(lambda);

        String totalTimeConsuming = DateUtil.getDatePoor(lastAudit.getAuditTime(),sysDate);
        CreditApplyAudit audit = new CreditApplyAudit();
        audit.setCompanyId(dto.getCompanyId())
                .setBusinessId(dto.getCreditApplyId())
                .setBusinessTypeId(dto.getBusinessTypeId())
                .setAuditBeforeFlow(dto.getBeforeFlow())
                .setAuditAfterFlow(dto.getAfterFlow())
                .setAuditPersonId(dto.getAuditPersonId())
                .setAuditPerson(dto.getAuditPerson())
                .setAuditOpinion(auditLog.toString())
                .setAuditTime(sysDate)
                .setTotalTimeConsuming(totalTimeConsuming)
                .setCreateTime(sysDate)
                .setCreateBy(dto.getAuditPersonId())
        ;
        creditApplyAuditMapper.insert(audit);

        //更新授信申请
        creditApplyMapper.updateByPrimaryKeySelective(
                new CreditApply().setId(dto.getCreditApplyId())
                        .setFlowCode(dto.getAfterFlow())
                        .setDisposeUserIds(dto.getDisposeUserIds())
                        .setDisposeRoleIds(dto.getDisposeRoleIds())
                        .setUpdateBy(dto.getAuditPersonId())
        );

        //添加操作日志
        operateLogMapper.insert(
                new OperateLog().setCompanyId(dto.getCompanyId())
                        .setBusinessTypeId(dto.getBusinessTypeId())
                        .setBusinessId(dto.getCreditApplyId())
                        .setOperateContent("【"+beforeFlow.getFlowName()+"】审核至【"+afterFlow.getFlowName()+"】")
                        .setOperatType(OperatTypeEnum.edit.getValue())
                        .setOperatObject(OperatObjectEnum.credit.getValue())
                        .setOperateTime(sysDate)
                        .setOperateUserId(dto.getAuditPersonId())
                        .setOperateUserName(dto.getAuditPerson())
        );

        return Result.success("审批成功");
    }

    //删除授信项目
    @Override
    @Transactional
    public String deleteCreditItem(CreditItemDeleteDTO creditItemDeleteDTO) {
        //删除当前
        if (creditItemMapper.delete(new UpdateWrapper<CreditItem>()
                .eq("credit_apply_id",creditItemDeleteDTO.getCreditApplyId())
                .eq("item_index",creditItemDeleteDTO.getItemIndex())) != 1)
            return Result.failure("删除此项目失败").toJSON();
        //再查询剩余所有
        List<CreditItem> creditItemList = creditItemMapper.selectList(new QueryWrapper<CreditItem>().eq("credit_apply_id",creditItemDeleteDTO.getCreditApplyId()).orderByAsc("item_index"));

        if (ListUtil.isEmpty(creditItemList)) { //删除后无其他项目
            return Result.success().toJSON();
        }else {//还有其他项目
            for (int i = 0; i < creditItemList.size(); i++) {
                creditItemList.get(i).setItemIndex(i);//改变索引
            }
            if (!creditItemService.updateBatchById(creditItemList))
                return Result.failure("更新剩余项目的排序失败").toJSON();
        }
        return Result.success().toJSON();
    }

    @Override
    public Result findApprovalCourse(CreditApplyBaseDTO dto) {
        CreditApply creditApply = creditApplyMapper.selectById(dto.getCreditApplyId());
        if(creditApply == null){
            return Result.failure("授信申请数据丢失");
        }
        List<CreditApprovalCourseVO> applyAuditList = creditApplyAuditMapper.findApprovalCourse(dto.getCreditApplyId(), creditApply.getCompanyId(), creditApply.getBusinessTypeId());
        if(applyAuditList == null){
            applyAuditList = new ArrayList<>();
        }

        Map<String, Object> auditData = creditAuditDataMapper.findAuditDataById(dto.getCreditApplyId());
        // 因为审批数据结果要在表达式内使用，定义的变量只能赋值一次，否则会编译报错
        Map<String, Object> creditAuditData = auditData == null ? new HashMap<>():auditData;

        List<Map<String, Object>> creditItemList = creditItemMapper.selectByCreditApplyId(dto.getCreditApplyId());

        List<BusinessWorkFlowExtendVO> workFlowExtendVOList = BusinessCfgUtil.getBusinessWorkFlowExtendList(creditApply.getCompanyId(),creditApply.getBusinessTypeId());
        if(workFlowExtendVOList == null || workFlowExtendVOList.isEmpty()){
            return Result.failure("授信申请审批配置数据丢失");
        }

        Map<String, Object> businessAttrValMap = BusinessCfgUtil.getBusinessAttrValMap(creditApply.getCompanyId(),creditApply.getBusinessTypeId());
        if(businessAttrValMap == null){
            return Result.failure("授信申请字段属性值配置数据丢失");
        }

        applyAuditList.forEach(applyAudit -> {
            applyAudit.setApplyColumnList(new ArrayList<>());

            BusinessWorkFlowExtendVO workFlowExtendVO = BusinessCfgUtil.getBusinessWorkFlowExtend(applyAudit.getAuditBeforeFlow(),applyAudit.getAuditAfterFlow(),workFlowExtendVOList);
            if(workFlowExtendVO == null || workFlowExtendVO.getBusinessAttrCfgVOList() == null){
                return;
            }

            //封装非项目字段
            workFlowExtendVO.getBusinessAttrCfgVOList().stream().filter(vo -> !"item".equals(vo.getColumnGroup())).forEach(vo -> {
                Map<String, Object> applyColumn = new HashMap<>();
                BusinessUtil.setApplyColumn(vo, businessAttrValMap, creditAuditData, applyColumn);
                applyAudit.getApplyColumnList().add(applyColumn);
            });

            //判断是否有项目字段需要封装
            long count = workFlowExtendVO.getBusinessAttrCfgVOList().stream().filter(vo -> "item".equals(vo.getColumnGroup())).count();
            if(count > 0){
                //表达式不允许使用非常量变量，这里使用集合的size来计数
                List<Integer> temp = new ArrayList<>();
                //封装项目字段
                creditItemList.forEach(creditItem -> {
                    Map<String, Object> itemName = new HashMap<>();
                    itemName.put("columnCh", "授信项目名称");
                    itemName.put("columnVal", creditItem.get("creditItem_itemName"));
                    applyAudit.getApplyColumnList().add(itemName);
                    workFlowExtendVO.getBusinessAttrCfgVOList().stream().filter(vo -> "item".equals(vo.getColumnGroup())).forEach(vo -> {
                        if(itemName.get("sort") == null){
                            itemName.put("sort", (int)(vo.getSort() + (count * temp.size())));
                        }

                        Map<String, Object> applyColumn = new HashMap<>();
                        BusinessUtil.setApplyColumn(vo, businessAttrValMap, creditItem, applyColumn);
                        applyColumn.put("sort", (int)(vo.getSort() + (count * temp.size())));
                        applyColumn.put("itemId", creditItem.get("id"));
                        applyAudit.getApplyColumnList().add(applyColumn);
                        temp.add(1);
                    });
                });
            }

            //将字段排序
            applyAudit.setApplyColumnList(
                    applyAudit.getApplyColumnList().stream().sorted(Comparator.comparing(applyColumn -> (Integer)applyColumn.get("sort"))).collect(Collectors.toList())
            );
        });

        return Result.success(applyAuditList);
    }

    @Override
    public Result getDetails(QueryApplyDetailsDTO dto) {
        Map<String, Object> details = creditApplyMapper.findCreditApplyDetails(dto.getCreditApplyId());
        if(details == null){
            return Result.failure("授信申请数据丢失");
        }

        List<Map<String, Object>> creditItemList = creditItemMapper.selectByCreditApplyId(dto.getCreditApplyId());
//        if(creditItemList == null || creditItemList.isEmpty()){
//            return Result.failure("授信申请项目数据丢失");
//        }

        details.put("item", creditItemList);
        Long companyId = (Long)details.get("creditApply_companyId");
        Long businessTypeId = (Long)details.get("creditApply_businessTypeId");

        //获取详情表单属性配置
        List<BusinessAttrCfgVO> businessAttrList =
                BusinessCfgUtil.getBusinessAttrCfgVOList(companyId, businessTypeId, CommonEnum.StatusEnnum.status1.getStatus());
        if(businessAttrList == null || businessAttrList.isEmpty()) {
            return Result.failure("无授信申请业务表单配置数据");
        }

        Map<String, Object> businessAttrValMap = BusinessCfgUtil.getBusinessAttrValMap(companyId,businessTypeId);
        if(businessAttrValMap == null){
            return Result.failure("授信申请字段属性值配置数据丢失");
        }

        List<Map<String, Object>> applyAttrCfg = BusinessCfgUtil.packageApplyAttrCfgList(businessAttrList, businessAttrValMap, dto.getQueryType());

        BusinessCfgUtil.packageApplyDetailsCfg(applyAttrCfg, details);
        return Result.success(applyAttrCfg);
    }

}
