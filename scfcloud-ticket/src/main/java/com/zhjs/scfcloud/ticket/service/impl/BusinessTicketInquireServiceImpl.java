package com.zhjs.scfcloud.ticket.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.businessTicket.*;
import com.zhjs.scfcloud.model.entity.BusinessTicketFile;
import com.zhjs.scfcloud.model.entity.BusinessTicketInquire;
import com.zhjs.scfcloud.model.mapper.BusinessTicketInquireMapper;
import com.zhjs.scfcloud.model.mapper.UserMapper;
import com.zhjs.scfcloud.model.transfer.BusinessTicketInquireTransfer;
import com.zhjs.scfcloud.model.vo.businessTicket.BusinessTicketInquireDetailVO;
import com.zhjs.scfcloud.model.vo.businessTicket.BusinessTicketInquireListVO;
import com.zhjs.scfcloud.ticket.service.BusinessTicketFileService;
import com.zhjs.scfcloud.ticket.service.BusinessTicketInquireService;
import com.zhjs.scfcloud.util.VO.EmailVO;
import com.zhjs.scfcloud.util.enums.BusinessTicketEnum;
import com.zhjs.scfcloud.util.util.EmailTool;
import com.zhjs.scfcloud.util.util.FileUtil;
import com.zhjs.scfcloud.util.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 询价相关
 */

@Service
public class BusinessTicketInquireServiceImpl extends ServiceImpl<BusinessTicketInquireMapper, BusinessTicketInquire> implements BusinessTicketInquireService {

    private Logger logger = LoggerFactory.getLogger(BusinessTicketInquireServiceImpl.class);

    @Resource
    private BusinessTicketInquireTransfer businessTicketInquireTransfer;
    @Resource
    private BusinessTicketInquireMapper businessTicketInquireMapper;
    @Resource
    private BusinessTicketFileService businessTicketFileService;
    @Resource
    private UserMapper userMapper;
    @Autowired
    private EmailTool emailTool;
    @Value("${frontEndUrl.bill}")
    private String billFrontEndUrl;


    //发布询价
    @Override
    @Transactional
    public Result inquire(BusinessTicketInquireAddDTO dto) {

        //校验票据号是否存在于交易大厅
        int businessTicketInquireCount = businessTicketInquireMapper.selectCount(
                new QueryWrapper<BusinessTicketInquire>().eq("bill_no",dto.getBillNo())
                       .in("status",BusinessTicketEnum.InquireStatus.inquire_status_1.getStatus(),BusinessTicketEnum.InquireStatus.inquire_status_2.getStatus())
                       .ge("expiration_date", new Date()));
        if (businessTicketInquireCount > 0) return Result.failure("已有报价中的相同票据号码的询价单");

        //校验票据号是否存在于未完成的订单
        int count  = businessTicketInquireMapper.getInquireInActiveOrderCount(dto.getBillNo());
        if (count > 0) return Result.failure("已有交易中的相同票据号码的订单");

        //名字处理
        if (!StringUtil.isEmpty(dto.getAccepterName())) {
            String acceptorName = dto.getAccepterName().trim();
            acceptorName = acceptorName.replace("(","（").replace(")","）").replace(" ","");
            dto.setAccepterName(acceptorName);
        }
        //转换
        BusinessTicketInquire businessTicketInquire = businessTicketInquireTransfer.toBusinessTicketInquire(dto);

        businessTicketInquire.setBillStatus(BusinessTicketEnum.BillStatus.bill_status_0.getStatus()); //设置背书状态为 未背书
        businessTicketInquire.setStatus(BusinessTicketEnum.InquireStatus.inquire_status_1.getStatus()); //设置询价状态为 待报价
        businessTicketInquire.setCreateTime(new Date());
        businessTicketInquire.setCreateBy(dto.getPublishPersonId());
        businessTicketInquire.setUpdateBy(dto.getPublishPersonId());

        //保存询价信息
        if (!save(businessTicketInquire)) return Result.failure("保存失败");

        Long id = businessTicketInquire.getId();
        //保存图片信息
        dto.getFileList().stream().forEach(businessTicketFile -> businessTicketFile.setInquireId(id));
        if (!businessTicketFileService.saveBatch(dto.getFileList())){
            throw new RuntimeException("保存图片失败");
        }

        //发送邮件
        EmailVO emailVO = new EmailVO();
        emailVO.setTo(new String[]{userMapper.selectById(dto.getPublishPersonId()).getEmail()});
        emailVO.setTemplate("NoticeTemplate.html");
        emailVO.setTitle("【领筑票据融资平台】发布询价单提醒");
        Map<String,Object> variables = new HashMap<>();
        variables.put("fileServiceUrl", FileUtil.getFileServiceUrl());
        variables.put("firstH","【"+businessTicketInquire.getCompanyName()+"】");
        variables.put("content","您好，询价单编号："+id+"，状态为【"+BusinessTicketEnum.InquireStatus.inquire_status_1.getStatusCH()+"】。");
        variables.put("url", billFrontEndUrl+"/sell/inquiredetail?id="+id);
        variables.put("linkContent","点此查看询价单详情");
        emailVO.setVariables(variables);
        emailTool.commonSendMail(emailVO);

        return Result.success();
    }

    //询价（发布）列表
    @Override
    public Result inquireList(BusinessTicketInquireListDTO dto) {
        List<BusinessTicketInquireListVO> businessTicketInquireListVOList =  businessTicketInquireMapper.getInquireList(dto);
        Map retMap = new HashMap();
        retMap.put("total",dto.getTotal());
        retMap.put("list", businessTicketInquireListVOList);
        return Result.success(retMap);
    }

    //撤销发布
    @Override
    public Result cancelInquire(BusinessTicketInquireCancelDTO dto) {
        //查询询价单
        BusinessTicketInquire businessTicketInquire =businessTicketInquireMapper.selectById(dto.getId());
        if (businessTicketInquire == null) return Result.failure("询价单不存在");
        //只有待报价和竞价中可以撤销
        if (businessTicketInquire.getStatus().intValue() != BusinessTicketEnum.InquireStatus.inquire_status_1.getStatus().intValue()
        && businessTicketInquire.getStatus().intValue() != BusinessTicketEnum.InquireStatus.inquire_status_2.getStatus().intValue())
            return Result.failure("只有待报价和竞价中的询价可以撤销");
        //修改的值
        businessTicketInquire.setStatus(BusinessTicketEnum.InquireStatus.inquire_status_5.getStatus())
                .setUpdateBy(dto.getUserId()).setUpdateTime(new Date());
        //修改操作
        if (businessTicketInquireMapper.updateById(businessTicketInquire) != 1) return Result.failure("操作失败");
        return Result.success();
    }

    //询价详情
    @Override
    public Result inquireDetail(Long id) {
        BusinessTicketInquireDetailVO businessTicketInquireDetailVO = businessTicketInquireMapper.getInquireDetail(id);
        if (businessTicketInquireDetailVO == null) return Result.failure("询价详情不存在");
        List<BusinessTicketFile> businessTicketFileList = businessTicketFileService.list(new QueryWrapper<BusinessTicketFile>().eq("inquire_id",businessTicketInquireDetailVO.getId()));
        businessTicketInquireDetailVO.setFileList(businessTicketFileList);
        return Result.success(businessTicketInquireDetailVO);
    }

    //给我的询价
    @Override
    public Result myAssignInquireList(BusinessTicketInquireMyAssignListDTO dto) {
        //List<BusinessTicketInquireListVO> list = businessTicketInquireMapper.myAssignInquireList(dto);
        dto.setRecords(businessTicketInquireMapper.myAssignInquireList(dto));
        return Result.successPage(dto);
    }

    //所有询价
    @Override
    public Result allInquire(BusinessTicketAllInquireDTO dto) {
        dto.setRecords(businessTicketInquireMapper.allInquire(dto));
        return Result.successPage(dto);
    }
}
