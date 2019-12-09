package com.zhjs.scfcloud.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.BaseIdDTO;
import com.zhjs.scfcloud.model.dto.BaseSingleUpdateDTO;
import com.zhjs.scfcloud.model.dto.credit.*;
import com.zhjs.scfcloud.model.entity.*;
import com.zhjs.scfcloud.model.enums.CreditUseEnum;
import com.zhjs.scfcloud.model.enums.OperatObjectEnum;
import com.zhjs.scfcloud.model.enums.OperatTypeEnum;
import com.zhjs.scfcloud.model.exception.ScfRuntimeException;
import com.zhjs.scfcloud.model.mapper.*;
import com.zhjs.scfcloud.model.transfer.CreditUseTransfer;
import com.zhjs.scfcloud.model.util.BusinessCfgUtil;
import com.zhjs.scfcloud.model.vo.CommentVO;
import com.zhjs.scfcloud.model.vo.credit.*;
import com.zhjs.scfcloud.system.service.CommentService;
import com.zhjs.scfcloud.system.service.CreditUseService;
import com.zhjs.scfcloud.system.service.FileService;
import com.zhjs.scfcloud.system.service.OperateLogService;
import com.zhjs.scfcloud.util.enums.CommonEnum;
import com.zhjs.scfcloud.util.util.ListUtil;
import com.zhjs.scfcloud.util.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.lang.System;
import java.math.BigDecimal;
import java.util.*;

/**
 * 用信管理业务逻辑接口 实现类
 * <功能详细描述>
 * Version: 1.0.0, 2019-06-27 11:22
 *
 * @author liuchanghai
 * @create 2019-06-27 11:22
 * @since
 */

@Service
public class CreditUseServiceImpl implements CreditUseService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private CreditTicketApplyMapper creditTicketApplyMapper;
    @Resource
    private CreditTicketMapper creditTicketMapper;
    @Autowired
    private CreditUseTransfer creditUseTransfer;
    @Autowired
    private FileService fileService;
    @Autowired
    private CommentService commentService;
    @Resource
    private CreditUseMapper creditUseMapper;
    @Autowired
    private OperateLogService operateLogService;
    @Resource
    private CreditTicketAuditDataMapper ticketAuditDataMapper;
    @Resource
    private CreditRecordMapper creditRecordMapper;
    @Resource
    private CreditItemMapper creditItemMapper;

    /**
     * 我的用信申请列表
     * @param dto 入参
     * @return
     */
    @Override
    public Result findCreditUseApplyMyList(CreditUseApplyMyListQueryDTO dto) {
        //查询
        dto.setRecords(creditTicketApplyMapper.findCreditUseApplyMyList(dto));
        return Result.successPage(dto,"records");
    }

    /**
     * 所有用信申请列表
     * @param dto 入参
     * @return
     */
    @Override
    public Result findCreditUseApplyAllList(CreditUseApplyAllListQueryDTO dto) {
        //查询
        dto.setRecords(creditTicketApplyMapper.findCreditUseApplyAllList(dto));
        return Result.successPage(dto);
    }

    /**
     * 用信审批列表
     * @param dto 入参
     * @return
     */
    @Override
    public Result findCreditUseAuditList(CreditUseAuditListQueryDTO dto) {
        //查询
        dto.setRecords(creditTicketApplyMapper.getAuditList(dto));
        return Result.successPage(dto);
    }

    /**
     * 录入用信记录
     * @param dto 入参
     * @return
     */
    @Override
    @Transactional
    public Result addCreditUseRecord(AddCreditUseRecordDTO dto) {
        String msg = "ok";

        //判断票据号码是否已经存在
        Integer ticketCount = creditTicketMapper.selectCount(new QueryWrapper<CreditTicket>().eq("ticket_no",dto.getTicketNo()).eq("status",1));
        if (ticketCount > 0) return Result.failure("此票据号已被使用");

        //用信实体
        CreditUse creditUse = new CreditUse();
        creditUse.setUseType(dto.getUseType())
                .setUseMoney(new BigDecimal(dto.getTicketMoney()))
                .setRefundStatus(0)
                .setUseBeginDate(dto.getTicketStart())
                .setShouldRefundDate(dto.getTicketEnd())
                .setCreateBy(dto.getUserId())
                .setUpdateBy(dto.getUserId())
                .setStatus(1)
                .setCreateTime(new Date())
                .setUpdateTime(new Date());
        //商票实体
        CreditTicket creditTicket = creditUseTransfer.creditUseRecordDTO2CreditTicket(dto);

        //授信记录实体
        CreditRecord record = null;

        if ("ticketApply".equals(dto.getEnter())){ //从开票申请处录入
            if (StringUtil.isEmpty(dto.getUseApplyId())) return Result.failure("开商票申请id为空");
            CreditTicketApply ticketApply = creditTicketApplyMapper.selectById(dto.getUseApplyId());
            if (ticketApply == null) return Result.failure("开商票申请数据不存在");
            //判断是否已经使用
            if (ticketApply.getHasUse().intValue() == CommonEnum.WhetherEnum.YES.getStatus().intValue()) {
                return Result.failure("用信申请已经被使用了");
            }
            //判断申请是否审批通过
            if (!"ticket99".equals(ticketApply.getFlowCode())) {
                return Result.failure("用信申请未审批通过");
            }
            //判断用信金额是否大于审批金额
            CreditTicketAuditData ticketAuditData = ticketAuditDataMapper.selectOne(new QueryWrapper<CreditTicketAuditData>().eq("business_id",dto.getUseApplyId()));
            if (ticketAuditData == null) return Result.failure("用信申请无审批数据");
            if (ticketAuditData.getRiskManagerHeadAuditMoney() == null) return Result.failure("无审批金额");
            if (ticketAuditData.getRiskManagerHeadAuditMoney().compareTo(new BigDecimal(dto.getTicketMoney())) != 0 ) {
                return Result.failure("用信金额必须等于审批金额");
            }
            //查询出授信记录
            record = creditRecordMapper.selectById(ticketApply.getCreditId());
            if (record == null) throw new ScfRuntimeException("授信记录不存在");
            //判断用信金额是否大于剩余额度
            BigDecimal remain = record.getCreditAmount().subtract(record.getUsedCreditAmount()==null?new BigDecimal(0):record.getUsedCreditAmount());
            if (remain.compareTo(new BigDecimal(dto.getTicketMoney())) == -1) {
                return Result.failure("用信金额大于剩余额度");
            }

            //补充用信信息
            creditUse.setCompanyId(ticketApply.getCompanyId())
                    .setCreditApplyId(ticketApply.getCreditApplyId())
                    .setCreditId(ticketApply.getCreditId())
                    .setItemName(ticketApply.getCreditItemName())
                    .setApplyUserIdCard(ticketApply.getApplyUserIdCard())
                    .setApplyUserName(ticketApply.getApplyUserName())
                    .setItemId(ticketApply.getCreditItemId())
                    .setUseApplyId(ticketApply.getId());

            //补充商票信息
            creditTicket.setCompanyId(ticketApply.getCompanyId());

            //更新申请记录为已使用
            ticketApply.setHasUse(CommonEnum.WhetherEnum.YES.getStatus()).setUpdateTime(new Date()).setUpdateBy(dto.getUserId());
            if (creditTicketApplyMapper.updateById(ticketApply) != 1){
                throw new ScfRuntimeException("更新申请的使用状态失败！");
            }

        }else {//从授信记录处录入
            if (StringUtil.isEmpty(dto.getCreditId())) return Result.failure("授信id为空");
            //查询出授信记录
            record = creditRecordMapper.selectById(dto.getCreditId());
            if (record == null)  Result.failure("授信记录不存在");
            //判断用信金额是否大于剩余额度
            BigDecimal remain = record.getCreditAmount().subtract(record.getUsedCreditAmount()==null?new BigDecimal(0):record.getUsedCreditAmount());
            if (remain.compareTo(new BigDecimal(dto.getTicketMoney())) == -1) {
                return Result.failure("用信金额大于剩余额度");
            }

            //补充用信信息
            creditUse.setCompanyId(record.getCompanyId())
                    .setCreditApplyId(record.getCreditApplyId())
                    .setCreditId(record.getId())
                    .setItemName(record.getProjectName())
                    .setApplyUserIdCard(record.getIdCard())
                    .setApplyUserName(record.getCustomerName());

            //补充商票信息
            creditTicket.setCompanyId(record.getCompanyId());
        }

        //添加用信
        if(creditUseMapper.insert(creditUse) == 0){
            throw new ScfRuntimeException("保存用信记录数据失败！");
        }

        creditTicket.setCreditUseId(creditUse.getId())
                .setUpdateBy(dto.getUserId())
                .setCreateBy(dto.getUserId())
                .setStatus(1)
                .setUpdateTime(new Date())
                .setCreateTime(new Date());
        int saveCreditTicket = creditTicketMapper.insert(creditTicket);
        //更新失败抛出异常进行回滚
        if(saveCreditTicket == 0){
           throw new ScfRuntimeException("保存商票数据失败！");
        }

        //更新授信记录已使用金额
        BigDecimal usedCreditAmount = (record.getUsedCreditAmount()==null?new BigDecimal(0):record.getUsedCreditAmount())
                .add(new BigDecimal(dto.getTicketMoney()));
        record.setUsedCreditAmount(usedCreditAmount);
        if (creditRecordMapper.updateById(record) != 1) {
            throw new ScfRuntimeException("更新已使用授信金额失败！");
        }

        // 设置操作日志
        OperateLog log = new OperateLog();
        log.setOperatObject(OperatObjectEnum.borrowing.getValue())
           .setBusinessId(creditTicket.getId())
           .setCompanyId(creditTicket.getCompanyId())
           .setOperatType(OperatTypeEnum.add.getValue())
           .setOperateContent("录入用信")
           .setOperateUserId(dto.getUserId())
           .setOperateUserName(dto.getUserName())
           .setOperateTime(new Date());
        operateLogService.save(log);

        return Result.success(msg);
    }

    /**
     * App端-查看用信申请详情
     * @param dto 入参
     * @return
     */
    @Override
    public Result findAppCreditUseApplyDetails(BaseIdDTO dto) {
        Map<String, Object> resMap = new HashMap<>();
//        CreditTicketApply ticketApply = creditTicketApplyMapper.selectById(dto.getId());
//        CreditUseApplyDetailsVO detailsVO = creditUseTransfer.po2CreditUseApplyDetailsVO(ticketApply);
        Map<String, Object> details = creditTicketApplyMapper.findCreditTicketApplyDetails(dto.getId());
        Long ticketApplyId = Long.parseLong(details.get("creditTicketApplyId").toString());
        Long companyId = Long.parseLong(details.get("companyId").toString());
        Long businessTypeId = Long.parseLong(details.get("businessTypeId").toString());
        List<CommentVO> commentVOList = commentService.findList(companyId,ticketApplyId,
                businessTypeId, null);
        details.put("commentList", commentVOList);
        List<File> fileList = fileService.findList(companyId, ticketApplyId,
                businessTypeId);
        details.put("fileList",fileList);
        // 查询表单配置
        Result<Map<String, Object>> detailsCfg = BusinessCfgUtil.getDetailsCfg(companyId,
                businessTypeId);
        resMap.put("formData",details);
        resMap.put("form",detailsCfg.getData());
        return Result.success(resMap);
    }

    /**
     * 查看用信申请详情
     * @param dto
     * @return
     */
    @Override
    public Result findCreditUseApplyDetails(BaseIdDTO dto) {
        Map<String, Object> resMap = new HashMap<>();
        // 获取申请信息
        Map<String, Object> details = creditTicketApplyMapper.findCreditTicketApplyDetails(dto.getId());
        Long ticketApplyId = Long.parseLong(details.get("creditTicketApplyId").toString());
        Long companyId = Long.parseLong(details.get("companyId").toString());
        Long businessTypeId = Long.parseLong(details.get("businessTypeId").toString());
        String flowCode = details.get("flowCode").toString();
        // 获取评论
        List<CommentVO> commentVOList = commentService.findList(companyId,ticketApplyId,
                businessTypeId, null);
        details.put("commentList", commentVOList);
        // 获取附件
        List<File> fileList = fileService.findList(companyId, ticketApplyId,
                businessTypeId);
        details.put("fileList",fileList);
        resMap.put("formData",details);
        // 获取当前流程扭转配置
        Result<Map<String, Object>> currentFlowCfg = BusinessCfgUtil.getCurrentFlowCfg(companyId,
                businessTypeId, flowCode);
        resMap.put("currentFlowCfg",currentFlowCfg.getData());
        // 获取表单配置
        Result<Map<String, Object>> detailsCfg = BusinessCfgUtil.getDetailsCfg(companyId,
                businessTypeId);
        resMap.put("form",detailsCfg.getData());
        return Result.success(resMap);
    }

    @Override
    public Result findCreditUseApplyDocuemnt(BaseIdDTO dto) {
        // 获取申请信息
        Map<String, Object> details = creditTicketApplyMapper.findCreditTicketApplyDetails(dto.getId());
        if(details == null) return Result.failure("无此申请记录");
        Long ticketApplyId = Long.parseLong(details.get("creditTicketApplyId").toString());
        Long companyId = Long.parseLong(details.get("companyId").toString());
        Long businessTypeId = Long.parseLong(details.get("businessTypeId").toString());
        if(details == null) return Result.failure("无此申请记录");
        return Result.success(fileService.findList(companyId, ticketApplyId, businessTypeId));
    }

    //app端我的用信列表
    @Override
    public String myAppList(CreditUseMyListAppDTO creditUseMyListAppDTO) {
        Map retMap = new HashMap();
        retMap.put("total",0);
        retMap.put("list",new ArrayList<>());
        List<CreditUseMyListAppVO> creditUseMyListAppVOList = creditUseMapper.getMyListAppVOList(creditUseMyListAppDTO);
        if (!ListUtil.isEmpty(creditUseMyListAppVOList)) {
            retMap.put("total",creditUseMyListAppDTO.getTotal());
            retMap.put("list",creditUseMyListAppVOList);
        }
        return Result.success(retMap).toSerializerJSON();
    }

    //web端我的用信列表
    @Override
    public String myWebList(CreditUseMyListWebDTO dto) {
        Map retMap = new HashMap();
        List<CreditUseMyListWebVO> creditUseMyListWebVOList = creditUseMapper.getMyListWebVOList(dto);
        retMap.put("total",dto.getTotal());
        retMap.put("list",creditUseMyListWebVOList);
        return Result.success(retMap).toSerializerJSON();
    }

    //web端所有用信列表
    @Override
    public String allWebList(CreditUseAllListWebDTO dto) {
        Map retMap = new HashMap();
        retMap.put("total",0);
        retMap.put("list",new ArrayList<>());
        List<CreditUseMyListWebVO> creditUseAllListWebVOList = creditUseMapper.getAllListWebVOList(dto);
        if (!ListUtil.isEmpty(creditUseAllListWebVOList)) {
            retMap.put("list",creditUseAllListWebVOList);
            retMap.put("total",dto.getTotal());
        }
        return Result.success(retMap).toSerializerJSON();
    }

    //修改还款状态
    @Override
    public String updateRefundStatus(CreditUseUpdateRefundStatusDTO dto) {
        CreditUse creditUse = creditUseMapper.selectByPrimaryKey(dto.getId());
        if (creditUse == null)
            return Result.failure("用信id未匹配到用信记录").toJSON();
        //修改数据
        creditUse.setRefundStatus(dto.getRefundStatus()).setUpdateBy(dto.getUserId()).setUpdateTime(new Date());

        if (creditUseMapper.updateByPrimaryKeySelective(creditUse) != 1)
            return Result.failure("修改失败").toJSON();

        return Result.success().toJSON();
    }

    //web端用信详情
    @Override
    public String webDetail(CreditUseDetailWebDTO dto) {
        Map retMap = new HashMap();
        //开商票类型详情
        if (dto.getUseType().intValue() == CreditUseEnum.UseType.useType0.getUseType().intValue()) {
            CreditUseDetail4TicketWebVO detail = creditUseMapper.getWebDetail(dto);
            if (detail == null)
                return Result.failure("未查询到数据").toJSON();
            retMap.put("detail",detail);
        }else {
            return Result.failure("暂不支持开商票以外的用信类型").toJSON();
        }
        return Result.success(retMap).toSerializerJSON();
    }

    //商票列表
    @Override
    public String ticketList(CreditTicketListWebDTO dto) {
        Map retMap = new HashMap();
        List<CreditTicketListWebVO> creditTicketListWebVOList = creditTicketMapper.getTicketWebList(dto);
        retMap.put("total",dto.getTotal());
        retMap.put("list",creditTicketListWebVOList);
        return Result.success(retMap).toSerializerJSON();
    }

    //商票详情
    @Override
    public String ticketDetail(BaseIdDTO dto) {
        CreditTicketDetailWebVO detail = creditTicketMapper.getTicketDetail(dto);
        if (detail == null)
            return Result.failure("未查询到数据").toJSON();
        Map retMap = new HashMap();
        retMap.put("detail",detail);
        return Result.success(retMap).toSerializerJSON();
    }

    //修改商票状态
    @Override
    public Result updateTicketStatus(BaseSingleUpdateDTO dto) {
        //查询商票
        CreditTicket ticket = creditTicketMapper.selectById(dto.getId());
        if (ticket == null) return Result.failure("商票数据不存在");
        ticket.setTicketStatus(Integer.parseInt(dto.getValue()));
        ticket.setUpdateBy(dto.getUpdateBy());
        ticket.setUpdateTime(new Date());
        //更新
        if (creditTicketMapper.updateById(ticket) != 1) {
            return Result.failure("更新失败");
        }
        return Result.success();
    }

}
