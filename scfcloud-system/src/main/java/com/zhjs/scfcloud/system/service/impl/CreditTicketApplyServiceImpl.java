package com.zhjs.scfcloud.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.CommonAuditCommitDTO;
import com.zhjs.scfcloud.model.dto.FileDTO;
import com.zhjs.scfcloud.model.dto.credit.AuditFormDTO;
import com.zhjs.scfcloud.model.dto.credit.CreditUseApplyAuditFinishListQueryDTO;
import com.zhjs.scfcloud.model.dto.ticket.*;
import com.zhjs.scfcloud.model.entity.*;
import com.zhjs.scfcloud.model.mapper.*;
import com.zhjs.scfcloud.model.util.BusinessCfgUtil;
import com.zhjs.scfcloud.model.util.BusinessUtil;
import com.zhjs.scfcloud.model.vo.business.BusinessAttrCfgVO;
import com.zhjs.scfcloud.model.vo.business.BusinessWorkFlowExtendVO;
import com.zhjs.scfcloud.model.vo.credit.CreditApprovalCourseVO;
import com.zhjs.scfcloud.system.service.AuditService;
import com.zhjs.scfcloud.system.service.CreditTicketApplyService;
import com.zhjs.scfcloud.system.service.FileService;
import com.zhjs.scfcloud.util.enums.BusinessFlowEnum;
import com.zhjs.scfcloud.util.enums.CommonEnum;
import com.zhjs.scfcloud.util.util.ListUtil;
import com.zhjs.scfcloud.util.util.StringUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.lang.System;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: dailongting
 * @date:2019/6/28 16:46
 */
@Service
public class CreditTicketApplyServiceImpl implements CreditTicketApplyService {

    @Resource
    private CreditTicketApplyMapper creditTicketApplyMapper;
    @Resource
    private BusinessAttrMapper businessAttrMapper;
    @Resource
    private AuditService auditService;
    @Resource
    private FileMapper fileMapper;
    @Resource
    private CreditApplyAuditMapper creditApplyAuditMapper;
    @Resource
    private FileService fileService;
    @Resource
    private CreditRecordMapper creditRecordMapper;
    @Resource
    private CreditTicketAuditDataMapper creditTicketAuditDataMapper;

    @Override
    public Map<String, Object> findCreditTicketApplyDetails(Long creditTicketApplyId) {
        return creditTicketApplyMapper.findCreditTicketApplyDetails(creditTicketApplyId);
    }

    @Override
    @Transactional
    public Result saveDraft(CreditTicketDraftSaveDTO dto) {
        Date sysDate = new Date();

        BusinessType businessType = BusinessCfgUtil.getBusinessType(dto.getCompanyId(), "申请开商票");
        if(businessType == null){
            return Result.failure("无申请开商票业务配置数据");
        }
        //是否有业务ID，没有创建
        if(dto.getBusinessId() == null){
            CreditTicketApply apply = new CreditTicketApply();
            apply.setCompanyId(dto.getCompanyId())
                    .setBusinessTypeId(businessType.getId())
                    .setDisposeUserIds(dto.getUserId().toString())
                    .setFlowCode(dto.getFlowCode())
                    .setStatus(CommonEnum.StatusEnnum.status1.getStatus())
                    .setHasUse(CommonEnum.StatusEnnum.status0.getStatus())
                    .setCreateTime(sysDate)
                    .setCreateBy(dto.getUserId())
                    .setUpdateBy(dto.getUserId())
                    ;
            creditTicketApplyMapper.insert(apply);
            dto.setBusinessId(apply.getId());
        }

        //保存字段值
        Map<String, Object> columnMap = new HashMap<>();
        columnMap.put("userId",dto.getUserId());

        //根据逗号拆分成多个字段上传值（主要用来处理地址字段，小程序地址组件是一起填值的）
        String[] columns = dto.getColumnName().split(" ");
        String[] columnVals = dto.getValue().split(" ");
        List<Map<String, Object>> columnList = new ArrayList<>();
        Map<String, Object> columnObj = null;
        for(int i = 0;i<columns.length;i++){
            String[] temp = columns[i].split("_");
            //申请金额校验
            if ("applyMoney".equals(temp[1])) {
                CreditTicketApply apply = creditTicketApplyMapper.selectById(dto.getBusinessId());
                if (StringUtil.isEmpty(apply.getCreditId())) return Result.failure("请先选择授信项目");
                Result remainCreditAmountResult = getRemainCreditAmount(apply.getCreditId());
                if (remainCreditAmountResult.getCode() != Result.RESULT_CODE_SUCCESS) return remainCreditAmountResult;
                BigDecimal remainCreditAmount = ((Map<String,BigDecimal>)remainCreditAmountResult.getData()).get("remainCreditAmount");
                if (remainCreditAmount.compareTo(new BigDecimal(columnVals[i]))  == -1 ) {
                    return Result.failure("可申请额度不足");
                }
            }

            columnObj = new HashMap<>();
            columnObj.put("columnName",StringUtil.toUnderlineName(temp[1]));
            columnObj.put("columnVal",columnVals[i]);
            columnList.add(columnObj);

            if(!columnMap.containsKey("tableName")){
                columnMap.put("tableName", "scf_"+ StringUtil.toUnderlineName(temp[0]));
                if(dto.getPrimaryId() == null && "creditTicketApply".equals(temp[0])){
                    dto.setPrimaryId(dto.getBusinessId());
                }
            }
        }
        columnMap.put("columnList",columnList);

        //字段归属表数据主键ID为空，则新增
        if(dto.getPrimaryId() == null){
            businessAttrMapper.insertColumnByTableName(columnMap);
            dto.setPrimaryId((Long)columnMap.get("id"));
        }else{
            columnMap.put("primaryId",dto.getPrimaryId());
            businessAttrMapper.updateColumnByTableName(columnMap);
        }
        System.out.println(dto.getPrimaryId());

        Map<String, Object> data = new HashMap<>();
        data.put("businessId",dto.getBusinessId());
        data.put("primaryId",dto.getPrimaryId());
        return Result.success(data);
    }


    @Override
    @Transactional
    public Result auditCommit(CommonAuditCommitDTO dto) {
        //获取开票申请
        CreditTicketApply creditTicketApply = creditTicketApplyMapper.selectById(dto.getBusinessId());
        if (creditTicketApply == null) return Result.failure("申请数据不存在");

        // 获取流程list
        List<BusinessFlow> businessFlowList = BusinessCfgUtil.getBusinessFlowList(creditTicketApply.getCompanyId(), creditTicketApply.getBusinessTypeId(), null);
        if(businessFlowList == null || businessFlowList.isEmpty()){
            return Result.failure("无申请开商票业务流程状态配置");
        }
        // 获取当前流程
        BusinessFlow beforeFlow = BusinessCfgUtil.getBusinessFlowByFlowCode(creditTicketApply.getFlowCode(), businessFlowList);
        if(beforeFlow == null){
            return Result.failure("获取当前流程状态配置失败");
        }

        if(!beforeFlow.getFlowCode().equals(dto.getBeforeFlow())){
            return Result.failure("提交申请错误！申请单当前状态为：【"+beforeFlow.getFlowName()+"】");
        }

        // 获取下一流程状态
        BusinessFlow afterFlow = BusinessCfgUtil.getBusinessFlowByFlowCode(dto.getAfterFlow(), businessFlowList);
        if(afterFlow == null){
            return Result.failure("获取下一流程状态配置失败");
        }

        //如果是审批完成  判断审批金额
        if(afterFlow.getFlowType() == 10){
            //判断是否提交了审批金额
            AuditFormDTO auditCreditMoney = dto.getFormDataList().stream().filter(auditFormDTO -> "creditTicketAuditData_riskManagerHeadAuditMoney".equals(auditFormDTO.getColumnName())).findAny().orElseGet(null);
            if (auditCreditMoney != null) {
                BigDecimal auditMoney = new BigDecimal(StringUtil.isEmpty(auditCreditMoney.getColumnValue())?"0":auditCreditMoney.getColumnValue());
                //获取授信剩余额度
                CreditRecord record = creditRecordMapper.selectById(creditTicketApply.getCreditId());
                if (record == null) return Result.failure("授信数据不存在");
                //判断用信金额是否大于剩余额度
                BigDecimal remain = record.getCreditAmount().subtract(record.getUsedCreditAmount()==null?new BigDecimal(0):record.getUsedCreditAmount());
                if (remain.compareTo(auditMoney) == -1) {
                    return Result.failure("审批金额大于剩余额度");
                }
            }
        }

        //获取业务类型
        BusinessType businessType = BusinessCfgUtil.getBusinessType(dto.getCompanyId(), "申请开商票");
        if(businessType == null){
            return Result.failure("无申请开商票业务配置数据");
        }
        dto.setBusinessTypeId(businessType.getId());
        //调用通用审批方法
        dto.setBusinessIdColumnName("business_id"); //设置业务对应id
        Result commonAuditResult = auditService.commonAuditCommit(dto);
        if (commonAuditResult.getCode() == Result.RESULT_CODE_FAILURE)
            return commonAuditResult;

        /*通用处理以外的处理*/
        creditTicketApply .setFlowCode(dto.getAfterFlow())
                .setDisposeUserIds(StringUtil.isEmpty(dto.getDisposeUserIds())?null:dto.getDisposeUserIds())
                .setDisposeRoleIds(StringUtil.isEmpty(dto.getDisposeRoleIds())?null:dto.getDisposeRoleIds())
                .setUpdateBy(dto.getAuditPersonId()).setUpdateTime(new Date());

        //如果是退回草稿状态  处理人改为创建人
        if(afterFlow.getFlowType() == 0){
            creditTicketApply.setDisposeUserIds(creditTicketApply.getCreateBy().toString())
                    .setDisposeRoleIds(null);
        }
        //更新开商票申请
        creditTicketApplyMapper.updateById(creditTicketApply);

        return Result.success();
    }

    @Override
    @Transactional
    public Result saveFile(CreditTicketFileSaveDTO dto) {
        BusinessType businessType = BusinessCfgUtil.getBusinessType(dto.getCompanyId(), "申请开商票");
        if(businessType == null){
            return Result.failure("无申请开商票业务配置数据");
        }
        Date sysDate = new Date();
        //是否有业务id 没有则新建
        if (dto.getBusinessId() == null  ) {
            CreditTicketApply apply = new CreditTicketApply();
            apply.setCompanyId(dto.getCompanyId())
                    .setBusinessTypeId(businessType.getId())
                    .setFlowCode(dto.getFlowCode())
                    .setStatus(CommonEnum.StatusEnnum.status1.getStatus())
                    .setCreateBy(dto.getUserId())
                    .setCreateTime(sysDate)
                    .setUpdateBy(dto.getUserId())
            ;
            creditTicketApplyMapper.insert(apply);
            dto.setBusinessId(apply.getId());
        }

        File file = dto.getFile();
        file.setBusinessId(dto.getBusinessId())
                .setCompanyId(dto.getCompanyId())
                .setBusinessTypeId(businessType.getId())
                .setStatus(CommonEnum.WhetherEnum.YES.getStatus())
                .setUpdateTime(sysDate)
                .setCreateTime(sysDate);
        //保存文件
        if (fileMapper.insert(file) == 0)
            throw new RuntimeException("文件保存失败");

        Map retMap = new HashMap();
        retMap.put("file",file);
        retMap.put("businessId",dto.getBusinessId());

        return Result.success(retMap);
    }

    @Override
    @Transactional
    public Result applyCommit(CreditTicketApplyCommitDTO dto) {
        Date sysDate = new Date();
        CreditTicketApply apply = creditTicketApplyMapper.selectById(dto.getBusinessId());
        if (apply == null) return Result.failure("申请数据不存在");

        //判断重复提交
        List<BusinessFlow> flowList = BusinessCfgUtil.getBusinessFlowList(apply.getCompanyId(),apply.getBusinessTypeId(), CommonEnum.StatusEnnum.status1.getStatus());
        if(flowList == null || flowList.isEmpty()){
            return Result.failure("无申请开商票业务流程状态配置");
        }

        String flowCode = apply.getFlowCode();
        BusinessFlow businessFlow =
                flowList.stream().filter(flow -> flow.getFlowCode().equals(flowCode)).findAny().orElse(null);
        if(businessFlow.getFlowType().intValue() != BusinessFlowEnum.FlowType.FLOW_TYPE0.getStatus().intValue()){
            return Result.failure("提交申请错误！申请单当前状态为：【"+businessFlow.getFlowName()+"】");
        }

        List<BusinessAttrCfgVO> businessAttrCfgVOList =
                BusinessCfgUtil.getBusinessAttrCfgVOList(apply.getCompanyId(),apply.getBusinessTypeId(),CommonEnum.StatusEnnum.status1.getStatus());
        //筛选出草稿/创建页面需传字段
        List<BusinessAttrCfgVO> validAttrList =
                businessAttrCfgVOList.stream().filter(validAttr -> {
                    if(CommonEnum.WhetherEnum.NO.getStatus().intValue() == validAttr.getHasCreateNeed().intValue()){
                        return false;
                    }
                    return true;
                }).collect(Collectors.toList());

        //校验creditTicketApply
        Result validResult =  BusinessCfgUtil.validBusinessAttr("creditTicketApply"
                ,apply,validAttrList,CommonEnum.QueryType.AUDIT.getStatus());
        if (validResult.getCode() == Result.RESULT_CODE_FAILURE){
            return validResult;
        }

        //查询历史附件进行附件校验
        Result<List<File>> result =
                fileService.findSelective(
                        new FileDTO().setCompanyId(apply.getCompanyId())
                                .setBusinessTypeId(apply.getBusinessTypeId())
                                .setBusinessId(dto.getBusinessId())
                                .setStatus(CommonEnum.StatusEnnum.status1.getStatus())
                );
        if(result.getCode() != Result.RESULT_CODE_SUCCESS){
            return Result.failure("查询已提交文件失败");
        }

        //额度校验
        Result getRemainCreditAmountResult = getRemainCreditAmount(apply.getCreditId());
        if (getRemainCreditAmountResult.getCode() != Result.RESULT_CODE_SUCCESS) {
            return getRemainCreditAmountResult;
        }
        BigDecimal remainCreditAmount = ((Map<String,BigDecimal>)getRemainCreditAmountResult.getData()).get("remainCreditAmount");
        if (apply.getApplyMoney().compareTo(remainCreditAmount)  == 1 ) {
            return Result.failure("可申请额度不足");
        }

        //查询流程扭转对象
        BusinessWorkFlowExtendVO workFlowExtendVO =
                BusinessCfgUtil.getBusinessWorkFlowExtend(dto.getWorkFlowId(),apply.getCompanyId(),apply.getBusinessTypeId());
        if(workFlowExtendVO == null){
            return Result.failure("缺少流程配置");
        }

        //校验附件
        Result valid = BusinessCfgUtil.validFiles(result.getData(),workFlowExtendVO.getWorkFlowFileVOList());
        if(valid.getCode() != Result.RESULT_CODE_SUCCESS){
            return valid;
        }

        //更新业务流程与处理人
        apply.setFlowCode(workFlowExtendVO.getAfterFlow())
                .setApplyTime(sysDate)
                .setDisposeUserIds(StringUtil.isEmpty(workFlowExtendVO.getNextDisposeUserIds())?null:workFlowExtendVO.getNextDisposeUserIds())
                .setDisposeRoleIds(StringUtil.isEmpty(workFlowExtendVO.getNextDisposeRoleIds())?null:workFlowExtendVO.getNextDisposeRoleIds())
                .setUpdateBy(dto.getUserId())
        ;
        int count = creditTicketApplyMapper.updateById(apply);
        if(count == 0){
            return Result.failure("提交开票申请失败");
        }

        //添加审批记录
        CreditApplyAudit audit = new CreditApplyAudit();
        audit.setCompanyId(apply.getCompanyId())
                .setBusinessId(dto.getBusinessId())
                .setBusinessTypeId(apply.getBusinessTypeId())
                .setAuditBeforeFlow(workFlowExtendVO.getBeforeFlow())
                .setAuditAfterFlow(workFlowExtendVO.getAfterFlow())
                .setAuditTime(sysDate)
                .setAuditPersonId(dto.getUserId())
                .setAuditPerson(dto.getUserName())
                .setAuditOpinion("开票申请提交")
                .setCreateBy(dto.getUserId())
                .setCreateTime(sysDate)
        ;
        if(creditApplyAuditMapper.insert(audit) == 0){
            throw new RuntimeException("提交开票申请发生异常，请重试");
        }
        return Result.success();
    }

    //获取项目剩余额度
    @Override
    public Result getRemainCreditAmount(Long recordId) {
        CreditRecord record = creditRecordMapper.selectById(recordId);
        if (record == null) return Result.failure("授信记录不存在");
        return  getRemainCreditAmount(record);
    }

    @Override
    public Result getRemainCreditAmount(CreditRecord record) {
        //授信额度
        BigDecimal creditAmount = record.getCreditAmount();
        //已用额度
        BigDecimal usedCreditAmount = record.getUsedCreditAmount() == null?new BigDecimal(0):record.getUsedCreditAmount();
        //在途额度
        LambdaQueryWrapper<CreditTicketApply> queryWrapper = new QueryWrapper<CreditTicketApply>().lambda();
        queryWrapper.eq(CreditTicketApply::getHasUse,0);//未用信
        queryWrapper.eq(CreditTicketApply::getFlowCode,"ticket2");//待审批
        queryWrapper.eq(CreditTicketApply::getCreditId,record.getId());
        List<CreditTicketApply> creditTicketApplyList = creditTicketApplyMapper.selectList(queryWrapper);

        BigDecimal prepareCreditAmount = new BigDecimal(0);
        if (!ListUtil.isEmpty(creditTicketApplyList)) {
            for (CreditTicketApply creditTicketApply:creditTicketApplyList) {
                prepareCreditAmount = prepareCreditAmount.add(creditTicketApply.getApplyMoney());
            }
        }
        //审批额度
        List<CreditTicketAuditData> auditDataList = creditTicketAuditDataMapper.findUnUseAuditDataByRecordId(record.getId());
        BigDecimal auditAmount = new BigDecimal(0);
        if (!ListUtil.isEmpty(auditDataList)) {
            for (CreditTicketAuditData auditData:auditDataList) {
                if (!StringUtil.isEmpty(auditData.getRiskManagerHeadAuditMoney())){
                    auditAmount = auditAmount.add(auditData.getRiskManagerHeadAuditMoney());
                }
            }
        }

        //剩余额度
        BigDecimal remainCreditAmount = creditAmount.subtract(usedCreditAmount).subtract(prepareCreditAmount).subtract(auditAmount);

        Map retMap = new HashMap();
        retMap.put("remainCreditAmount",remainCreditAmount);
        return Result.success(retMap);
    }

    @Override
    public Result findApprovalCourse(CreditTicketApplyBaseDTO dto) {
        CreditTicketApply apply = creditTicketApplyMapper.selectById(dto.getCreditTicketId());
        if(apply == null){
            return Result.failure("开票申请数据丢失");
        }
        List<CreditApprovalCourseVO> applyAuditList = creditApplyAuditMapper.findApprovalCourse(apply.getId(),apply.getCompanyId(),apply.getBusinessTypeId());
        if(applyAuditList == null){
            applyAuditList = new ArrayList<>();
        }

        Map<String, Object> ticketAuditData = creditTicketAuditDataMapper.findAuditDataByBusinessId(apply.getId());
        // 因为审批数据结果要在表达式内使用，定义的变量只能赋值一次，否则会编译报错
        Map<String, Object> auditData = ticketAuditData == null ? new HashMap<>():ticketAuditData;

        List<BusinessWorkFlowExtendVO> workFlowExtendVOList = BusinessCfgUtil.getBusinessWorkFlowExtendList(apply.getCompanyId(),apply.getBusinessTypeId());
        if(workFlowExtendVOList == null || workFlowExtendVOList.isEmpty()){
            return Result.failure("开票申请审批配置数据丢失");
        }

        Map<String, Object> businessAttrValMap = BusinessCfgUtil.getBusinessAttrValMap(apply.getCompanyId(),apply.getBusinessTypeId());
        if(businessAttrValMap == null){
            return Result.failure("开票申请字段属性值配置数据丢失");
        }

        applyAuditList.forEach(applyAudit -> {
            applyAudit.setApplyColumnList(new ArrayList<>());

            BusinessWorkFlowExtendVO workFlowExtendVO = BusinessCfgUtil.getBusinessWorkFlowExtend(applyAudit.getAuditBeforeFlow(),applyAudit.getAuditAfterFlow(),workFlowExtendVOList);
            if(workFlowExtendVO == null || workFlowExtendVO.getBusinessAttrCfgVOList() == null){
                return;
            }

            //封装字段
            workFlowExtendVO.getBusinessAttrCfgVOList().forEach(vo -> {
                Map<String, Object> applyColumn = new HashMap<>();
                BusinessUtil.setApplyColumn(vo, businessAttrValMap, auditData, applyColumn);
                applyAudit.getApplyColumnList().add(applyColumn);
            });

            //将字段排序
            applyAudit.setApplyColumnList(
                    applyAudit.getApplyColumnList().stream().sorted(Comparator.comparing(applyColumn -> (Integer)applyColumn.get("sort"))).collect(Collectors.toList())
            );
        });

        return Result.success(applyAuditList);
    }

    @Override
    public Result getDetails(QueryApplyDetailsDTO dto) {
        Map<String, Object> details = creditTicketApplyMapper.findCreditTicketApplyDetails(dto.getCreditTicketId());
        if(details == null){
            return Result.failure("开票申请数据丢失");
        }

        Long companyId = (Long)details.get("companyId");
        Long businessTypeId = (Long)details.get("businessTypeId");

        //获取详情表单属性配置
        List<BusinessAttrCfgVO> businessAttrList =
                BusinessCfgUtil.getBusinessAttrCfgVOList(companyId, businessTypeId, CommonEnum.StatusEnnum.status1.getStatus());
        if(businessAttrList == null || businessAttrList.isEmpty()) {
            return Result.failure("无开票申请业务表单配置数据");
        }

        Map<String, Object> businessAttrValMap = BusinessCfgUtil.getBusinessAttrValMap(companyId,businessTypeId);
        if(businessAttrValMap == null){
            return Result.failure("开票申请字段属性值配置数据丢失");
        }

        List<Map<String, Object>> applyAttrCfg = BusinessCfgUtil.packageApplyAttrCfgList(businessAttrList, businessAttrValMap, dto.getQueryType());
        BusinessCfgUtil.packageApplyDetailsCfg(applyAttrCfg, details);

        Map<String, Object> applyDetails = new HashMap<>();
        applyDetails.put("creditTicketApplyId", details.get("creditTicketApplyId"));
        applyDetails.put("applyUserName", details.get("applyUserName"));
        applyDetails.put("applyUserIdCard", details.get("applyUserIdCard"));
        applyDetails.put("creditItemId", details.get("creditItemId"));
        applyDetails.put("creditItemName", details.get("creditItemName"));
        applyDetails.put("applyDetailsCfg", applyAttrCfg);
        return Result.success(applyDetails);
    }

    /**
     * 审批完成列表
     * @param dto
     * @return
     */
    @Override
    public Result getAuditFinishList(CreditUseApplyAuditFinishListQueryDTO dto) {
        //查询
        dto.setRecords(creditTicketApplyMapper.getAuditFinishList(dto));
        return Result.successPage(dto);
    }
}
