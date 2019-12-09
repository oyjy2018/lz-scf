package com.zhjs.scfcloud.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.CommonAuditCommitDTO;
import com.zhjs.scfcloud.model.dto.FileDTO;
import com.zhjs.scfcloud.model.dto.credit.AuditFormDTO;
import com.zhjs.scfcloud.model.entity.*;
import com.zhjs.scfcloud.model.enums.OperatObjectEnum;
import com.zhjs.scfcloud.model.enums.OperatTypeEnum;
import com.zhjs.scfcloud.model.mapper.AddressMapper;
import com.zhjs.scfcloud.model.mapper.CreditApplyAuditMapper;
import com.zhjs.scfcloud.model.mapper.CreditTicketApplyMapper;
import com.zhjs.scfcloud.model.mapper.OperateLogMapper;
import com.zhjs.scfcloud.model.transfer.FileTransfer;
import com.zhjs.scfcloud.model.util.BusinessCfgUtil;
import com.zhjs.scfcloud.model.vo.business.BusinessAttrCfgVO;
import com.zhjs.scfcloud.system.service.CommentService;
import com.zhjs.scfcloud.system.service.AuditService;
import com.zhjs.scfcloud.system.service.FileService;
import com.zhjs.scfcloud.util.enums.CommonEnum;
import com.zhjs.scfcloud.util.util.DateUtil;
import com.zhjs.scfcloud.util.util.ListUtil;
import com.zhjs.scfcloud.util.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 审批相关
 */
@Service
public class AuditServiceImpl implements AuditService {

    private Logger logger = LoggerFactory.getLogger(AuditServiceImpl.class);
    @Resource
    private CommentService commentService;
    @Resource
    private CreditApplyAuditMapper creditApplyAuditMapper;
    @Resource
    private OperateLogMapper operateLogMapper;
    @Resource
    private AddressMapper addressMapper;
    @Resource
    private FileTransfer fileTransfer;
    @Resource
    private FileService fileService;

    /**
     * 通用审批处理 范围
     * 评论表
     * 审核数据表（支持多个）
     * 审核记录表
     * 操作日志表
     * @param dto
     * @return
     */
    @Override
    @Transactional
    public Result commonAuditCommit(CommonAuditCommitDTO dto) {

        //获取业务所有流程
        List<BusinessFlow> businessFlowList = BusinessCfgUtil.getBusinessFlowList(dto.getCompanyId(),dto.getBusinessTypeId(), CommonEnum.StatusEnnum.status1.getStatus());
        //当前流程
        BusinessFlow nowFlow = BusinessCfgUtil.getBusinessFlowByFlowCode(dto.getBeforeFlow(), businessFlowList);
        //审批后流程
        BusinessFlow afterFlow = BusinessCfgUtil.getBusinessFlowByFlowCode(dto.getAfterFlow(), businessFlowList);

        Date sysDate = new Date();

        logger.info("---->step:{},operation:{}",1,"保存文件");
        // 保存新上传的文件
        if (!ListUtil.isEmpty(dto.getFileList())){
            List<File> files = new ArrayList<>();
            for (FileDTO fileDTO: dto.getFileList()){
                if(fileDTO.getId() == null){
                    File file = fileTransfer.toFile(fileDTO);
                    file.setBusinessId(dto.getBusinessId())
                            .setCompanyId(dto.getCompanyId())
                            .setBusinessTypeId(dto.getBusinessTypeId())
                            .setStatus(1)
                            .setUpdateTime(sysDate)
                            .setCreateTime(sysDate);
                    files.add(file);
                }
            }
            // 保存文件
            if(files.size() > 0){
                fileService.saveBatch(files);
            }
        }

        logger.info("---->step:{},operation:{}",2,"保存评论");
        //如果有填写评论，则保存
        if(!StringUtil.isEmpty(dto.getCommentContent())){
            Comment comment = new Comment();
            comment.setCompanyId(dto.getCompanyId()) .setBusinessTypeId(dto.getBusinessTypeId())
                    .setBusinessId(dto.getBusinessId()).setContent(dto.getCommentContent())
                    .setFlowCode(dto.getBeforeFlow()).setStatusDesc("在状态["+nowFlow.getFlowName()+"]")
                    .setCreateBy(dto.getAuditPersonId()).setCreateTime(sysDate);
            commentService.save(comment);
        }

        logger.info("---->step:{},operation:{}",3,"保存审核数据");
        //将审核表单数据封装
        Map<String,List<AuditFormDTO>> formMap = dto.getFormDataList().stream().collect(Collectors.groupingBy(form -> {
            String tableName = "scf_"+StringUtil.toUnderlineName(form.getColumnName().split("_")[0]);
            String columnName = StringUtil.toUnderlineName(form.getColumnName().split("_")[1]);
            form.setColumnName(columnName);
            return tableName +" "+form.getProjectId(); //根据表名和主键分组
        }));

        //审核日志
        StringBuffer auditLog = new StringBuffer();

        //处理需要存储的字段，组装审核日志
        formMap.keySet().forEach(key -> { //每次处理单个表
            List<AuditFormDTO> auditFormList = formMap.get(key);
            String[] keys = key.split(" ");//将表名和主键拆分
            String table = keys[0];
            String projectId = null;
            if (keys.length > 1) projectId = keys[1];
            Map<String, Object> data = creditApplyAuditMapper.findAuditDataObj(table, dto.getCompanyId(), dto.getBusinessId(),dto.getBusinessIdColumnName(),projectId);
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("tableName", table);
            if (data == null) { //无纪录是新增
                StringBuffer columnNames = new StringBuffer("company_id,create_by");//公司id,创建人员必传
                columnNames.append(",").append(dto.getBusinessIdColumnName());//审批数据表中关联业务主表主键的字段名
                List columnValues = new ArrayList();
                //拼接字段值 ！！！！和字段的顺序一致
                columnValues.add(dto.getCompanyId());//公司id的值
                columnValues.add(dto.getAuditPersonId());//创建id的值
                columnValues.add(dto.getBusinessId());//审批数据表中关联业务主表主键的字段值
                for (AuditFormDTO auditFormDTO:auditFormList) {
                    columnNames.append(",").append(auditFormDTO.getColumnName());//拼接其他提交字段
                    columnValues.add(auditFormDTO.getColumnValue());//拼接其他提交字段的值
                }
                paramMap.put("columnNames", columnNames.toString());
                paramMap.put("columnValues", columnValues);
                //新增操作
                if (creditApplyAuditMapper.insertAuditData(paramMap) != 1){
                    new RuntimeException("新增审核数据失败！");
                }
                data = new HashMap<>();//避免拼接审核日志时报错
            }else { //有纪录时修改
                paramMap.put("columns", auditFormList);
                paramMap.put("companyId", dto.getCompanyId());
                paramMap.put("businessIdColumnName", dto.getBusinessIdColumnName());
                paramMap.put("businessId", dto.getBusinessId());
                paramMap.put("projectId", projectId);
                paramMap.put("updateBy", dto.getAuditPersonId());
                //更新操作
                if (creditApplyAuditMapper.updateAuditData(paramMap) != 1){
                    new RuntimeException("更新审核数据失败！");
                }
            }

            List<BusinessAttrCfgVO> businessAttrList = BusinessCfgUtil.getBusinessAttrCfgVOList(dto.getCompanyId(),dto.getBusinessTypeId(),null);
            //审核日志
            for (AuditFormDTO auditForm:auditFormList) {
                Object val = data.get(auditForm.getColumnName());
                if (StringUtil.isDigitEquals(val,auditForm.getColumnValue())) continue; //值未改变不做记录
                BusinessAttrCfgVO businessAttrCfgVO =  businessAttrList.stream().filter(vo -> {
                        String tableName = "scf_"+StringUtil.toUnderlineName(vo.getColumnName().split("\\_")[0]);
                        String column = StringUtil.toUnderlineName(vo.getColumnName().split("\\_")[1]);
                        return tableName.equals(table) && column.equals(auditForm.getColumnName());
                        }
                ).findAny().orElse(null);

                if (businessAttrCfgVO == null) continue;
                Object oldVal = val;
                String newVal = auditForm.getColumnValue();
                String columnType = businessAttrCfgVO.getColumnType();
                if (columnType.equals("select") || columnType.equals("radio")){
                   if (!StringUtil.isEmpty(val))
                       oldVal =BusinessCfgUtil.getBusinessAttrVal(Long.parseLong(val.toString()),businessAttrCfgVO.getCompanyId(),businessAttrCfgVO.getBusinessTypeId(),businessAttrCfgVO.getBusinessAttrId());
                   if (!StringUtil.isEmpty(auditForm.getColumnValue()))
                       newVal =BusinessCfgUtil.getBusinessAttrVal(Long.parseLong(auditForm.getColumnValue()),businessAttrCfgVO.getCompanyId(),businessAttrCfgVO.getBusinessTypeId(),businessAttrCfgVO.getBusinessAttrId());
                }else if (columnType.equals("select-province")||columnType.equals("select-city")||columnType.equals("select-region")) {
                    if (!StringUtil.isEmpty(val)) oldVal = addressMapper.selectById(Integer.parseInt(val.toString())).getName();
                    if (!StringUtil.isEmpty(auditForm.getColumnValue())) newVal =addressMapper.selectById(Integer.parseInt(auditForm.getColumnValue())).getName();
                }
                String unit = StringUtil.isEmpty(businessAttrCfgVO.getColumnUnit())?"":businessAttrCfgVO.getColumnUnit();
                String log = "【"+auditForm.getColumnCh()+"】：由【"+(oldVal == null ? "空值":oldVal+unit)+"】改为【"+(newVal == null ?"空值":newVal+unit)+"】";
                auditLog.append("/r/n").append(log);
            }
        });

        logger.info("---->step:{},operation:{}",4,"保存审核纪录");
        //创建审核记录
        // 获取最近的一条审核记录
        LambdaQueryWrapper<CreditApplyAudit> lambda = new QueryWrapper<CreditApplyAudit>().lambda();
        lambda.eq(CreditApplyAudit::getBusinessId,dto.getBusinessId()).eq(CreditApplyAudit::getBusinessTypeId,dto.getBusinessTypeId())
                .orderByDesc(CreditApplyAudit::getId).last("limit 1");
        CreditApplyAudit lastAudit = creditApplyAuditMapper.selectOne(lambda);
        //两次审核间隔时间
        String totalTimeConsuming = null;
        if (lastAudit != null && lastAudit.getAuditTime() != null) totalTimeConsuming = DateUtil.getDatePoor(lastAudit.getAuditTime(),sysDate);
        CreditApplyAudit audit = new CreditApplyAudit();
        audit.setCompanyId(dto.getCompanyId())
                .setBusinessTypeId(dto.getBusinessTypeId())
                .setBusinessId(dto.getBusinessId())
                .setAuditBeforeFlow(dto.getBeforeFlow())
                .setAuditAfterFlow(dto.getAfterFlow())
                .setAuditPersonId(dto.getAuditPersonId())
                .setAuditPerson(dto.getAuditPerson())
                .setAuditOpinion(StringUtil.isEmpty(auditLog.toString())?"无审批数据改变":auditLog.toString())
                .setAuditTime(sysDate)
                .setTotalTimeConsuming(totalTimeConsuming)
                .setCreateTime(sysDate)
                .setCreateBy(dto.getAuditPersonId())
        ;
        creditApplyAuditMapper.insert(audit);

        logger.info("---->step:{},operation:{}",5,"保存操作日志");
        //添加操作日志
        operateLogMapper.insert(
                new OperateLog().setCompanyId(dto.getCompanyId())
                        .setBusinessTypeId(dto.getBusinessTypeId())
                        .setBusinessId(dto.getBusinessId())
                        .setOperateContent("【"+nowFlow.getFlowName()+"】审核至【"+afterFlow.getFlowName()+"】")
                        .setOperatType(OperatTypeEnum.edit.getValue())
                        .setOperatObject(OperatObjectEnum.credit.getValue())
                        .setOperateTime(sysDate)
                        .setOperateUserId(dto.getAuditPersonId())
                        .setOperateUserName(dto.getAuditPerson())
        );

        return Result.success();
    }
}
