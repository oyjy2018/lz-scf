package com.zhjs.scfcloud.ticket.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.QuotationOwnerQueryDTO;
import com.zhjs.scfcloud.model.dto.QuotationTicketQueryDTO;
import com.zhjs.scfcloud.model.dto.ticket.AllQuotationListDTO;
import com.zhjs.scfcloud.model.entity.BusinessTicketInquire;
import com.zhjs.scfcloud.model.entity.BusinessTicketQuotation;
import com.zhjs.scfcloud.model.entity.User;
import com.zhjs.scfcloud.model.mapper.BusinessTicketInquireMapper;
import com.zhjs.scfcloud.model.mapper.BusinessTicketQuotationMapper;
import com.zhjs.scfcloud.model.mapper.UserMapper;
import com.zhjs.scfcloud.model.vo.QuotationDetailVO;
import com.zhjs.scfcloud.model.vo.QuotationOwnerVO;
import com.zhjs.scfcloud.model.vo.QuotationTicketVO;
import com.zhjs.scfcloud.model.vo.businessTicket.AllQuotationTicketVO;
import com.zhjs.scfcloud.ticket.service.QuotationService;
import com.zhjs.scfcloud.ticket.util.MySubjectUtil;
import com.zhjs.scfcloud.util.VO.EmailVO;
import com.zhjs.scfcloud.util.enums.BusinessTicketEnum;
import com.zhjs.scfcloud.util.util.DateUtil;
import com.zhjs.scfcloud.util.util.EmailTool;
import com.zhjs.scfcloud.util.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class QuotationServiceImpl implements QuotationService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    BusinessTicketQuotationMapper businessTicketQuotationMapper;
    @Autowired
    BusinessTicketInquireMapper businessTicketInquireMapper;
    @Autowired
    UserMapper userMapper;
    @Value("${frontEndUrl.bill}")
    private String billFrontEndUrl;
    @Autowired
    private EmailTool emailTool;


    @Override
    public IPage<QuotationTicketVO> selectQuotationTicketList(QuotationTicketQueryDTO dto) {
        Page<QuotationTicketVO> page = new Page<>(dto.getCurrent(),dto.getSize());
        List<QuotationTicketVO> list = businessTicketQuotationMapper.selectQuotationTicketList(page,
                dto.getInquireCompanyId(),
                dto.getQuotationStatus(),
                dto.getQuotationCompanyName(),
                dto.getQuotationCreateStartTime(),
                dto.getQuotationCreateEndTime(),
                dto.getQuotationId(),dto.getInquireId());
        return page.setRecords(list);
    }

    @Override
    public QuotationDetailVO selectQuotationDetail(Long quotationId) {
        QuotationDetailVO result = new QuotationDetailVO();

        BusinessTicketQuotation quotation = businessTicketQuotationMapper.selectById(quotationId);
        BusinessTicketInquire inquire = businessTicketInquireMapper.selectById(quotation.getInquireId());
        User inquireUser = userMapper.selectById(inquire.getPublishPersonId());
        User quotationUser = userMapper.selectById(quotation.getQuotationPersonId());
        //设置询价方信息
        result.setInquireCompanyName(inquire.getCompanyName());
        result.setInquireId(inquire.getId());
        result.setInquireUserName(inquire.getPublishPerson());
        result.setInquireUserPhone(inquireUser.getPhone());
        result.setInquireUserEmail(inquireUser.getEmail());
        //设置报价方信息
        result.setQuotationCompanyName(quotation.getCompanyName());
        result.setQuotationUserName(quotation.getQuotationPerson());
        result.setQuotationUserPhone(quotationUser.getPhone());
        result.setQuotationUserEmail(quotationUser.getEmail());
        //设置报价信息
        result.setQuotationTime(quotation.getCreateTime());
        result.setQuotationStatus(quotation.getStatus());
        result.setAccepterName(inquire.getAccepterName());
        result.setBillNo(inquire.getBillNo());
        result.setBillAmt(inquire.getBillAmt());
        result.setMaturityDate(inquire.getMaturityDate());
        result.setQuotationSurplusValidDays(quotation.getSurplusValidDays());
        result.setEndorsedCount(inquire.getEndorsedCount());
        result.setFlaws(inquire.getFlaws());
        result.setAmount(quotation.getAmount());
        result.setAnnualRate(quotation.getAnnualRate());
        result.setDiscountFee(quotation.getDiscountFee().setScale(2, BigDecimal.ROUND_HALF_UP));
        return result;
    }

    @Override
    public IPage<QuotationOwnerVO> selectQuotationOwnerList(QuotationOwnerQueryDTO dto) {
        Page<QuotationOwnerVO> page = new Page<>(dto.getCurrent(),dto.getSize());
        List<QuotationOwnerVO> list = businessTicketQuotationMapper.selectQuotationOwnerList(page,
                dto.getQuotationCompanyId(),
                dto.getQuotationStatus(),
                dto.getInquireCompanyName(),
                dto.getQuotationCreateStartTime(),
                dto.getQuotationCreateEndTime(),
                dto.getQuotationId());
        return page.setRecords(list);
    }

    @Override
    public Result deleteQuotationByOwner(Long quotationId,Long userId) {
        BusinessTicketQuotation quotation = businessTicketQuotationMapper.selectById(quotationId);
        if(quotation == null) return Result.failure("报价单不存在");
        if(quotation.getStatus() == BusinessTicketEnum.QuotationStatus.quotation_status_3.getStatus()){
            return Result.failure("票方已不接受此报价，无再需撤回");
        }
        if(quotation.getStatus() == BusinessTicketEnum.QuotationStatus.quotation_status_2.getStatus()){
            return Result.failure("票方已经接受此报价，不可撤回");
        }
        if(quotation.getStatus() == BusinessTicketEnum.QuotationStatus.quotation_status_4.getStatus()){
            return Result.failure("当前报价已撤回，无需再重复操作了");
        }

        quotation.setStatus(BusinessTicketEnum.QuotationStatus.quotation_status_4.getStatus());
        quotation.setUpdateBy(userId);
        quotation.setUpdateTime(new Date());
        businessTicketQuotationMapper.updateById(quotation);

        //发送邮件-异步
        sendRevokeQuotationEmail(quotation);

        return Result.success();
    }

    @Override
    public IPage<AllQuotationTicketVO> findAllQuotations(AllQuotationListDTO allQuotationListDTO,Long userId) {
        User user = userMapper.selectById(userId);
        if(StringUtils.isEmpty(user.getPermissionOrgIds())){
            return new Page<>(allQuotationListDTO.getCurrent(),allQuotationListDTO.getSize());
        }
        Page<AllQuotationTicketVO> page = new Page<>(allQuotationListDTO.getCurrent(),allQuotationListDTO.getSize());
        List<AllQuotationTicketVO> allQuotation = businessTicketQuotationMapper.selectAllQuotationsList(page,
                allQuotationListDTO.getQuotationStatus(),
                allQuotationListDTO.getQuotationCompanyName(),
                allQuotationListDTO.getQuotationCreateStartTime(),
                allQuotationListDTO.getQuotationCreateEndTime(),
                allQuotationListDTO.getQuotationId(),
                user.getPermissionOrgIds());
        return  page.setRecords(allQuotation);
    }

    /**
     * 处理撤回报价时-发送邮件
     * @param quotation
     */
    public void sendRevokeQuotationEmail(BusinessTicketQuotation quotation){
        BusinessTicketInquire inquire = businessTicketInquireMapper.selectById(quotation.getInquireId());
        //发送询价方邮件
        EmailVO emailVO = new EmailVO();
        emailVO.setTo(new String[]{userMapper.selectById(inquire.getPublishPersonId()).getEmail()});
        emailVO.setTemplate("TicketTemplate.html");
        emailVO.setTitle("【领筑票据融资平台】撤回报价提醒");
        Map<String,Object> variables = new HashMap<>();
        variables.put("fileServiceUrl", FileUtil.getFileServiceUrl());
        variables.put("companyName","【"+inquire.getCompanyName()+"】");
        variables.put("firstContent","您好，"+quotation.getCompanyName()+"撤回了对贵公司的报价【报价单编号："+quotation.getId()+"】：");
        variables.put("billNo",inquire.getBillNo());
        variables.put("accepterName",inquire.getAccepterName());
        variables.put("inquireCompanyName",inquire.getCompanyName());
        variables.put("quotationCompanyName",quotation.getCompanyName());
        variables.put("quotationTime", DateUtil.format(quotation.getCreateTime(),"yyyy-MM-dd HH:mm:ss"));
        variables.put("revokeTime",DateUtil.format(quotation.getUpdateTime(),"yyyy-MM-dd HH:mm:ss"));
        variables.put("billAmt",inquire.getBillAmt());
        variables.put("amount",quotation.getAmount());
        variables.put("firstUrl", billFrontEndUrl+"/sell/quotationdetail?quotationId="+quotation.getId());
        variables.put("firstUrlContent","点此查看报价详情");
        emailVO.setVariables(variables);
        emailTool.commonSendMail(emailVO);
        //报价方邮件
        EmailVO quotationEmailVO = new EmailVO();
        quotationEmailVO.setTo(new String[]{MySubjectUtil.getUser().getEmail()});
        quotationEmailVO.setTemplate("TicketTemplate.html");
        quotationEmailVO.setTitle("【领筑票据融资平台】撤回报价提醒");
        Map<String,Object> mapCopy = variables.entrySet().stream().collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));
        mapCopy.put("companyName","【"+quotation.getCompanyName()+"】");
        mapCopy.put("firstContent","您好，贵公司撤回了对"+inquire.getCompanyName()+"的报价【报价单编号："+quotation.getId()+"】：");
        quotationEmailVO.setVariables(mapCopy);
        emailTool.commonSendMail(quotationEmailVO);
    }

}
