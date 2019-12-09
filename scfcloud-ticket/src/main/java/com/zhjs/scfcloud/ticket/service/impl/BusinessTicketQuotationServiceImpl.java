package com.zhjs.scfcloud.ticket.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.businessTicket.BusinessTicketQuoteAddDTO;
import com.zhjs.scfcloud.model.dto.businessTicket.QuotationDTO;
import com.zhjs.scfcloud.model.dto.esign.EsignContractContentDTO;
import com.zhjs.scfcloud.model.entity.*;
import com.zhjs.scfcloud.model.exception.ScfRuntimeException;
import com.zhjs.scfcloud.model.mapper.*;
import com.zhjs.scfcloud.model.transfer.BusinessTicketQuotationTransfer;
import com.zhjs.scfcloud.model.vo.credit.acceptor.AcceptorListVO;
import com.zhjs.scfcloud.ticket.service.BusinessTicketQuotationService;
import com.zhjs.scfcloud.ticket.service.SignService;
import com.zhjs.scfcloud.ticket.util.MySubjectUtil;
import com.zhjs.scfcloud.util.VO.EmailVO;
import com.zhjs.scfcloud.util.enums.BusinessTicketEnum;
import com.zhjs.scfcloud.util.enums.CommonEnum;
import com.zhjs.scfcloud.util.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.lang.System;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 询价相关
 */

@Service
public class BusinessTicketQuotationServiceImpl extends ServiceImpl<BusinessTicketQuotationMapper, BusinessTicketQuotation> implements BusinessTicketQuotationService {

    private Logger logger = LoggerFactory.getLogger(BusinessTicketQuotationServiceImpl.class);

    @Resource
    private BusinessTicketQuotationMapper businessTicketQuotationMapper;
    @Resource
    private BusinessTicketQuotationTransfer businessTicketQuotationTransfer;
    @Resource
    private BusinessTicketInquireMapper businessTicketInquireMapper;
    @Resource
    private EmailTool emailTool;
    @Value("${frontEndUrl.bill}")
    private String billFrontEndUrl;
    @Resource
    private UserMapper userMapper;
    @Resource
    private BusinessTicketOrderMapper businessTicketOrderMapper;
    @Resource
    private CompanyMapper companyMapper;
    @Resource
    private CompanyBankAccountMapper companyBankAccountMapper;
    @Resource
    private SignService signService;
    @Autowired
    private SmsUtil smsUtil;
    @Resource
    private AcceptorMapper acceptorMapper;
    @Resource
    private PlatformServiceRateMapper platformServiceRateMapper;

    //报价
    @Override
    @Transactional
    public Result quote(BusinessTicketQuoteAddDTO dto) {

        BusinessTicketInquire businessTicketInquire = businessTicketInquireMapper.selectById(dto.getInquireId());
        if (businessTicketInquire == null) return Result.failure("询价单不存在");
        if (businessTicketInquire.getCompanyId().intValue() == dto.getCompanyId().intValue()) return Result.failure("报价公司和询价公司不能为同一家");
        //状态判断
        if (businessTicketInquire.getStatus().intValue() != BusinessTicketEnum.InquireStatus.inquire_status_1.getStatus().intValue() &&
                businessTicketInquire.getStatus().intValue() != BusinessTicketEnum.InquireStatus.inquire_status_2.getStatus().intValue()) {
            return Result.failure("询价单当前状态不能报价");
        }
        //判断是否已过询价截止日期
        if (businessTicketInquire.getExpirationDate().getTime() < System.currentTimeMillis()) return Result.failure("询价已截止");

        //成交金额必须大于票面金额的70%
        if (dto.getAmount().compareTo(businessTicketInquire.getBillAmt().multiply(new BigDecimal(0.7))) <= 0) {
            return Result.failure("成交金额必须大于票面金额的70%");
        }

        //查询这个询价单 当前公司是否已经报价过
        QueryWrapper<BusinessTicketQuotation> businessTicketQuotationQueryWrapper = new QueryWrapper<>();
        businessTicketQuotationQueryWrapper.eq("inquire_id",dto.getInquireId());
        businessTicketQuotationQueryWrapper.eq("company_id",dto.getCompanyId());
        businessTicketQuotationQueryWrapper.in("status","1,2");
        List<BusinessTicketQuotation> businessTicketQuotationList = businessTicketQuotationMapper.selectList(businessTicketQuotationQueryWrapper);
        if (!ListUtil.isEmpty(businessTicketQuotationList)) return Result.failure("抱歉，贵公司已经报价，目前该报价单处于【待接受报价】中");

        //查询公司是否开启了限制承兑方
        Company company = companyMapper.selectById(dto.getCompanyId());
        if (company == null) return Result.failure("公司信息不存在");
        //开启承兑方限制时 才做控制
        if (company.getIsAcceptorLimit().intValue() == CommonEnum.WhetherEnum.YES.getStatus().intValue()) {
            //名字处理
            String acceptorName = businessTicketInquire.getAccepterName();
            if (!StringUtil.isEmpty(acceptorName)){
                acceptorName = acceptorName.trim();
                acceptorName = acceptorName.replace("(","（").replace(")","）").replace(" ","");
            }
            //检验此票据承兑方剩余额度是否足够
            AcceptorListVO acceptorListVO = acceptorMapper.getRemainLimitMoney(dto.getCompanyId(),acceptorName);
            //无配置
            if (acceptorListVO == null ) {
                return Result.failure("您开启了承兑方限制，但是承兑方【"+acceptorName+"】没有添加额度");
            }
            BigDecimal remainLimitMoney = acceptorListVO.getRemainLimitMoney();
            //票面金额大于剩余额度
            if (businessTicketInquire.getBillAmt().compareTo(remainLimitMoney) > 0) {
                return Result.failure("承兑方【"+acceptorName+"】剩余额度不足，剩余额度为："+remainLimitMoney+"元");
            }
        }

        //转换
        BusinessTicketQuotation businessTicketQuotation = businessTicketQuotationTransfer.toBusinessTicketQuotation(dto);

        businessTicketQuotation.setStatus(BusinessTicketEnum.QuotationStatus.quotation_status_1.getStatus()); // 询价状态 设为待报价
        businessTicketQuotation.setCreateTime(new Date());
        businessTicketQuotation.setCreateBy(dto.getQuotationPersonId());
        businessTicketQuotation.setUpdateBy(dto.getQuotationPersonId());

        if (!save(businessTicketQuotation)) return Result.failure("保存报价失败");
        //更新询价状态为竞价中
        businessTicketInquire.setStatus(BusinessTicketEnum.InquireStatus.inquire_status_2.getStatus())
                .setUpdateBy(dto.getQuotationPersonId());
        if (businessTicketInquireMapper.updateById(businessTicketInquire) != 1) {
            throw new ScfRuntimeException("更新询价单状态失败");
        }

        //发送邮件
        sendQuoteEmail(businessTicketInquire,businessTicketQuotation);

        return Result.success();
    }

    /**
     *  处理报价时-发送邮件
     * @param businessTicketInquire
     * @param businessTicketQuotation
     */
    private void sendQuoteEmail(BusinessTicketInquire businessTicketInquire,BusinessTicketQuotation businessTicketQuotation){
        //询价方邮件
        EmailVO emailVO = new EmailVO();
        emailVO.setTo(new String[]{userMapper.selectById(businessTicketInquire.getPublishPersonId()).getEmail()});
        emailVO.setTemplate("TicketTemplate.html");
        emailVO.setTitle("【领筑票据融资平台】询价单的报价提醒");
        Map<String,Object> variables = new HashMap<>();
        variables.put("fileServiceUrl", FileUtil.getFileServiceUrl());
        variables.put("companyName","【"+businessTicketInquire.getCompanyName()+"】");
        variables.put("firstContent","您好，询价单编号："+businessTicketInquire.getId()+"，有新的报价：");
        variables.put("billNo",businessTicketInquire.getBillNo());
        variables.put("accepterName",businessTicketInquire.getAccepterName());
        variables.put("quotationCompanyName",businessTicketQuotation.getCompanyName());
        variables.put("quotationTime",DateUtil.format(businessTicketQuotation.getCreateTime(),"yyyy-MM-dd HH:mm:ss"));
        variables.put("billAmt",businessTicketInquire.getBillAmt());
        variables.put("amount",businessTicketQuotation.getAmount());
        variables.put("firstUrl", billFrontEndUrl+"/sell/inquiredetail?id="+businessTicketInquire.getId());
        variables.put("firstUrlContent","点此查看询价单详情");
        emailVO.setVariables(variables);
        emailTool.commonSendMail(emailVO);
        //报价方邮件
        EmailVO quotationEmailVO = new EmailVO();
        quotationEmailVO.setTo(new String[]{MySubjectUtil.getUser().getEmail()});
        quotationEmailVO.setTemplate("TicketTemplate.html");
        quotationEmailVO.setTitle("【领筑票据融资平台】报价提醒");
        Map<String,Object> quotationVariables = new HashMap<>();
        quotationVariables.put("fileServiceUrl", FileUtil.getFileServiceUrl());
        quotationVariables.put("companyName","【"+businessTicketQuotation.getCompanyName()+"】");
        quotationVariables.put("firstContent","您好，贵公司对【询价单编号："+businessTicketInquire.getId()+"】提交了报价：");
        quotationVariables.put("billNo",businessTicketInquire.getBillNo());
        quotationVariables.put("accepterName",businessTicketInquire.getAccepterName());
        quotationVariables.put("inquireCompanyName",businessTicketInquire.getCompanyName());
        quotationVariables.put("quotationCompanyName",businessTicketQuotation.getCompanyName());
        quotationVariables.put("quotationTime",DateUtil.format(businessTicketQuotation.getCreateTime(),"yyyy-MM-dd HH:mm:ss"));
        quotationVariables.put("billAmt",businessTicketInquire.getBillAmt());
        quotationVariables.put("amount",businessTicketQuotation.getAmount());
        quotationVariables.put("firstUrl", billFrontEndUrl+"/sell/quotationdetail?quotationId="+businessTicketQuotation.getId());
        quotationVariables.put("firstUrlContent","点此查看报价单详情");
        quotationEmailVO.setVariables(quotationVariables);
        emailTool.commonSendMail(quotationEmailVO);
    }

    @Override
    @Transactional
    public Result acceptedQuotation(QuotationDTO dto) {
        BusinessTicketQuotation quotation = businessTicketQuotationMapper.selectById(dto.getQuotationId());
        if(quotation == null) {
            return Result.failure("该报价数据丢失");
        }
        //判断报价单状态是否为待接受报价
        if(BusinessTicketEnum.QuotationStatus.quotation_status_1.getStatus().intValue() != quotation.getStatus().intValue()){
            Optional<BusinessTicketEnum.QuotationStatus> optional = BusinessTicketEnum.QuotationStatus.getEnumByStatus(quotation.getStatus());
            return Result.failure("接受报价失败！该报价单状态为【"+(optional.isPresent() ? optional.get().getStatusCH():"未知")+"】");
        }

        BusinessTicketInquire inquire = businessTicketInquireMapper.selectById(quotation.getInquireId());
        if(inquire == null){
            return Result.failure("该报价单关联的询价单数据丢失");
        }

        //判断该询价单是否可接受报价
        if(!BusinessTicketEnum.InquireStatus.hasAcceptedQuota(inquire.getStatus())){
            return Result.failure("接受报价失败！该报价单关联的询价单状态为【"+BusinessTicketEnum.InquireStatus.getStatusCH(inquire.getStatus())+"】");
        }

        Date sysDate = new Date();

        inquire.setStatus(BusinessTicketEnum.InquireStatus.inquire_status_3.getStatus());
        inquire.setUpdateBy(dto.getUserId());
        if(businessTicketInquireMapper.updateById(inquire) == 0){
            return Result.failure("接受报价失败！更新询价状态失败！");
        }

        quotation.setStatus(BusinessTicketEnum.QuotationStatus.quotation_status_2.getStatus());
        quotation.setAcceptedTime(sysDate);
        quotation.setUpdateBy(dto.getUserId());
        if(businessTicketQuotationMapper.updateById(quotation) == 0){
            new ScfRuntimeException("接受报价失败！更新报价状态失败！");
        }

        //接受报价后创建领筑交易平台订单记录
        BusinessTicketOrder order = new BusinessTicketOrder();
        order.setInquireId(inquire.getId());
        order.setQuotationId(quotation.getId());
        order.setAmounts(quotation.getAmount());
        order.setSurplusValidDays(DateUtil.getTimeDistance(sysDate, inquire.getMaturityDate()));
        order.setContractSignStatus(0);
        order.setOrderStatus(BusinessTicketEnum.OrderStatus.ORDER_STATUS_1.getStatus());
        order.setCreateBy(dto.getUserId());
        order.setCreateTime(sysDate);
        order.setUpdateTime(sysDate);
        order.setUpdateBy(dto.getUserId());
        //查询此时的服务费率
        PlatformServiceRate platformServiceRate = platformServiceRateMapper.selectOne(new QueryWrapper<PlatformServiceRate>().lambda()
                .eq(PlatformServiceRate::getSystemId,2)
                .eq(PlatformServiceRate::getDeleteStatus,CommonEnum.WhetherEnum.NO.getStatus())
                .last("limit 1"));
        if (platformServiceRate != null && platformServiceRate.getRate() != null ) {
            order.setServiceRate(platformServiceRate.getRate());
        }else {
            order.setServiceRate(BigDecimal.ZERO);
        }
        businessTicketOrderMapper.insert(order);
        if(order.getId() == null || order.getId() == 0){
            new ScfRuntimeException("接受报价失败！创建订单失败！");
        }

        /**  生成合同  begin **/
        //参数dto
        EsignContractContentDTO contractContentDTO = new EsignContractContentDTO();
        contractContentDTO.setOrderId(order.getId());
        contractContentDTO.setAnnualRate(quotation.getAnnualRate());
        contractContentDTO.setAmounts(order.getAmounts());
        //查询甲方（询价方）信息
        Company inquireCompany = companyMapper.selectById(inquire.getCompanyId());
        if (inquireCompany == null) throw new ScfRuntimeException("询价公司信息不存在");
        contractContentDTO.setAName(inquireCompany.getCompanyName());
        contractContentDTO.setALegalPerson(inquireCompany.getLegalPersonName());
        contractContentDTO.setAAddress(inquireCompany.getDetailAddr());
        contractContentDTO.setAContactNumber(inquireCompany.getContactNumber());

        //查询乙方（报价方）信息
        Company quotationCompany = companyMapper.selectById(quotation.getCompanyId());
        if (quotationCompany == null) throw new ScfRuntimeException("报价公司信息不存在");
        contractContentDTO.setBName(quotationCompany.getCompanyName());
        contractContentDTO.setBLegalPerson(quotationCompany.getLegalPersonName());
        contractContentDTO.setBAddress(quotationCompany.getDetailAddr());
        contractContentDTO.setBContactNumber(quotationCompany.getContactNumber());

        //调用
        Result createContractResult = signService.createContract(contractContentDTO);
        if (createContractResult.getCode() == Result.RESULT_CODE_FAILURE) throw new ScfRuntimeException(createContractResult.getMessage());

        /**  生成合同  end **/

//        //抛线程异步发邮件  避免超时
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                // TODO Auto-generated method stub
//
//                //发送询价方通知邮件
//                Map<String, Object> valueMap = new HashMap<>();
//
//                valueMap.put("inquireId", quotation.getInquireId());
//                valueMap.put("inquireCompanyName", inquire.getCompanyName());
//                valueMap.put("quotationCompanyName", quotation.getCompanyName());
//                valueMap.put("updateTime", DateUtil.format(quotation.getAcceptedTime(),"yyyy-MM-dd HH:mm:ss"));
//                valueMap.put("billAmt", inquire.getBillAmt());
//                valueMap.put("amount", quotation.getAmount());
//                valueMap.put("discountFee", quotation.getDiscountFee());
//                valueMap.put("accepterName", inquire.getAccepterName());
//                valueMap.put("fileServiceUrl", FileUtil.getFileServiceUrl());
//                valueMap.put("emailType", "接受");
//
//                valueMap.put("to", new String[]{user.getEmail()});
//                valueMap.put("title", "【领筑票据交易平台】贵公司接受了"+quotation.getCompanyName()+"的报价，请知悉。(询价单ID："+quotation.getInquireId()+")");
//                valueMap.put("template","BusinessTicketInquireEmail.html");
//                emailTool.sendMailVCodeTemplate(valueMap);
//
//                //发送报价方通知邮件
//                valueMap.put("to", new String[]{quotaUser.getEmail()});
//                valueMap.put("title", "【领筑票据交易平台】"+inquire.getCompanyName()+"接受了贵公司的报价，请知悉。(询价单ID："+quotation.getInquireId()+")");
//                valueMap.put("template","BusinessTicketQuotationEmail.html");
//                emailTool.sendMailVCodeTemplate(valueMap);
//            }
//        }).start();

        /** 发送邮件 **/
        sendAcceptedQuotationEmail(quotation,inquire,order.getId());

        return Result.success("接受报价成功");
    }

    /**
     * 处理接受报价时发送邮件
     * @param quotation
     * @param inquire
     * @param orderId
     */
    private void sendAcceptedQuotationEmail(BusinessTicketQuotation quotation,BusinessTicketInquire inquire,Long orderId){
        String inquireEmail = MySubjectUtil.getUser().getEmail();
        User quotationUser = userMapper.selectById(quotation.getQuotationPersonId());
        //询价方邮件
        EmailVO emailVO = new EmailVO();
        emailVO.setTo(new String[]{inquireEmail});
        emailVO.setTemplate("TicketTemplate.html");
        emailVO.setTitle("【领筑票据融资平台】接受报价提醒");
        Map<String,Object> variables = new HashMap<>();
        variables.put("fileServiceUrl", FileUtil.getFileServiceUrl());
        variables.put("companyName","【"+inquire.getCompanyName()+"】");
        variables.put("firstContent","您好，贵公司接受了"+quotation.getCompanyName()+"的报价【报价单编号："+quotation.getId()+"】：");
        variables.put("billNo",inquire.getBillNo());
        variables.put("accepterName",inquire.getAccepterName());
        variables.put("inquireCompanyName",inquire.getCompanyName());
        variables.put("quotationCompanyName",quotation.getCompanyName());
        variables.put("quotationTime",DateUtil.format(quotation.getCreateTime(),"yyyy-MM-dd HH:mm:ss"));
        variables.put("accepterTime",DateUtil.format(quotation.getAcceptedTime(),"yyyy-MM-dd HH:mm:ss"));
        variables.put("billAmt",inquire.getBillAmt());
        variables.put("amount",quotation.getAmount());
        variables.put("secondContent","系统已创建了一笔订单【订单编号："+orderId+"】，订单状态为【"+BusinessTicketEnum.OrderStatus.ORDER_STATUS_1.getStatusCH()+"】。");
        variables.put("firstUrl", billFrontEndUrl+"/sell/orderdetail?orderId="+orderId);
        variables.put("firstUrlContent","点此查看订单详情");
        variables.put("secondUrl", billFrontEndUrl+"/sell/quotationdetail?quotationId="+quotation.getId());
        variables.put("secondUrlContent","点此查看报价详情");
        emailVO.setVariables(variables);
        emailTool.commonSendMail(emailVO);

        //报价方邮件
        EmailVO quotationEmailVO = new EmailVO();
        quotationEmailVO.setTo(new String[]{quotationUser.getEmail()});
        quotationEmailVO.setTemplate("TicketTemplate.html");
        quotationEmailVO.setTitle("【领筑票据融资平台】接受报价提醒");
        Map<String,Object> mapCopy = variables.entrySet().stream().collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));
        mapCopy.put("companyName","【"+quotation.getCompanyName()+"】");
        mapCopy.put("firstContent","您好，"+inquire.getCompanyName()+"接受了贵公司的报价【报价单编号："+quotation.getId()+"】：");
        quotationEmailVO.setVariables(mapCopy);
        emailTool.commonSendMail(quotationEmailVO);

        //订单邮件
        //卖家
        EmailVO emailByOrderInquire = new EmailVO();
        emailByOrderInquire.setTo(new String[]{inquireEmail});
        emailByOrderInquire.setTemplate("NoticeTemplate.html");
        emailByOrderInquire.setTitle("【领筑票据融资平台】创建订单提醒");
        Map<String,Object> variablesByOrder = new HashMap<>();
        variablesByOrder.put("fileServiceUrl", FileUtil.getFileServiceUrl());
        variablesByOrder.put("firstH","【"+inquire.getCompanyName()+"】");
        variablesByOrder.put("content","您好，订单编号："+orderId+"，状态为【"+BusinessTicketEnum.OrderStatus.ORDER_STATUS_1.getStatusCH()+"】。");
        variablesByOrder.put("url", billFrontEndUrl+"/sell/orderdetail?orderId="+orderId);
        variablesByOrder.put("linkContent","点此查看订单详情");
        emailByOrderInquire.setVariables(variablesByOrder);
        emailTool.commonSendMail(emailByOrderInquire);
        //买家
        EmailVO emailByOrderQuotation = new EmailVO();
        emailByOrderQuotation.setTo(new String[]{quotationUser.getEmail()});
        emailByOrderQuotation.setTemplate("NoticeTemplate.html");
        emailByOrderQuotation.setTitle("【领筑票据融资平台】创建订单提醒");
        Map<String,Object> mapCopyByOrder = variablesByOrder.entrySet().stream().collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));
        mapCopyByOrder.put("firstH","【"+quotation.getCompanyName()+"】");
        mapCopyByOrder.put("content","您好，订单编号："+orderId+"，状态为【"+BusinessTicketEnum.OrderStatus.ORDER_STATUS_1.getStatusCH()+"】，请前往平台签署交易合同。");
        emailByOrderQuotation.setVariables(mapCopyByOrder);
        emailTool.commonSendMail(emailByOrderQuotation);
        //发送短信
        Map<String, String> params = new HashMap<>();
        params.put("orderNo",orderId.toString());
        params.put("orderStatus",BusinessTicketEnum.OrderStatus.ORDER_STATUS_1.getStatusCH());
        smsUtil.lzySmsBsend(quotationUser.getPhone(), SmsUtil.lzy_sms_type_S501,params);
    }

    @Override
    public Result rejectQuotation(QuotationDTO dto) {
        BusinessTicketQuotation quotation = businessTicketQuotationMapper.selectById(dto.getQuotationId());
        if(quotation == null) {
            return Result.failure("该报价数据丢失");
        }
        //判断报价单状态是否为待接受报价
        if(BusinessTicketEnum.QuotationStatus.quotation_status_1.getStatus().intValue() != quotation.getStatus().intValue()){
            Optional<BusinessTicketEnum.QuotationStatus> optional = BusinessTicketEnum.QuotationStatus.getEnumByStatus(quotation.getStatus());
            return Result.failure("拒绝报价失败！该报价单状态为【"+(optional.isPresent() ? optional.get().getStatusCH():"未知")+"】");
        }

        quotation.setStatus(BusinessTicketEnum.QuotationStatus.quotation_status_3.getStatus());
        quotation.setUpdateBy(dto.getUserId());
        quotation.setUpdateTime(new Date());
        if(businessTicketQuotationMapper.updateById(quotation) == 0){
            new ScfRuntimeException("拒绝报价失败！更新报价状态失败！");
        }

        BusinessTicketInquire inquire = businessTicketInquireMapper.selectById(quotation.getInquireId());
//        User quotaUser = userMapper.selectById(quotation.getQuotationPersonId());
//        //抛线程异步发邮件  避免超时
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                // TODO Auto-generated method stub
//                //发送报价方通知邮件
//                Map<String, Object> valueMap = new HashMap<>();
//                valueMap.put("inquireId", quotation.getInquireId());
//                valueMap.put("inquireCompanyName", inquire.getCompanyName());
//                valueMap.put("quotationCompanyName", quotation.getCompanyName());
//                valueMap.put("updateTime", DateUtil.format(quotation.getUpdateTime(),"yyyy-MM-dd HH:mm:ss"));
//                valueMap.put("billAmt", inquire.getBillAmt());
//                valueMap.put("amount", quotation.getAmount());
//                valueMap.put("discountFee", quotation.getDiscountFee());
//                valueMap.put("accepterName", inquire.getAccepterName());
//                valueMap.put("fileServiceUrl", FileUtil.getFileServiceUrl());
//                valueMap.put("emailType", "拒绝");
//
//                valueMap.put("to", new String[]{quotaUser.getEmail()});
//                valueMap.put("title", "【领筑票据交易平台】"+inquire.getCompanyName()+"拒绝了贵公司的报价，请知悉。(询价单ID："+quotation.getInquireId()+")");
//                valueMap.put("template","BusinessTicketQuotationEmail.html");
//                emailTool.sendMailVCodeTemplate(valueMap);
//            }
//        }).start();

        /** 发送邮件-异步 */
        sendRejectQuotationEmail(quotation,inquire);
        return Result.success("拒绝报价成功");
    }

    /**
     * 处理拒绝报价时的邮件发送
     * @param quotation
     * @param inquire
     */
    public void sendRejectQuotationEmail(BusinessTicketQuotation quotation,BusinessTicketInquire inquire){
        //询价方邮件
        EmailVO emailVO = new EmailVO();
        emailVO.setTo(new String[]{MySubjectUtil.getUser().getEmail()});
        emailVO.setTemplate("TicketTemplate.html");
        emailVO.setTitle("【领筑票据融资平台】退回报价提醒");
        Map<String,Object> variables = new HashMap<>();
        variables.put("fileServiceUrl", FileUtil.getFileServiceUrl());
        variables.put("companyName","【"+"卖家公司名称"+"】");
        variables.put("firstContent"," 您好，贵公司退回了"+quotation.getCompanyName()+"的报价【报价单编号："+quotation.getId()+"】：");
        variables.put("billNo",inquire.getBillNo());
        variables.put("accepterName",inquire.getAccepterName());
        variables.put("inquireCompanyName",inquire.getCompanyName());
        variables.put("quotationCompanyName",quotation.getCompanyName());
        variables.put("quotationTime",DateUtil.format(quotation.getCreateTime(),"yyyy-MM-dd HH:mm:ss"));
        variables.put("rejectTime",DateUtil.format(quotation.getUpdateTime(),"yyyy-MM-dd HH:mm:ss"));
        variables.put("billAmt",inquire.getBillAmt());
        variables.put("amount",quotation.getAmount());
        variables.put("firstUrl", billFrontEndUrl+"/sell/quotationdetail?quotationId="+quotation.getId());
        variables.put("firstUrlContent","点此查看报价详情");
        emailVO.setVariables(variables);
        emailTool.commonSendMail(emailVO);
        //报价方邮件
        EmailVO quotationEmailVO = new EmailVO();
        quotationEmailVO.setTo(new String[]{userMapper.selectById(quotation.getQuotationPersonId()).getEmail()});
        quotationEmailVO.setTemplate("TicketTemplate.html");
        quotationEmailVO.setTitle("【领筑票据融资平台】退回报价提醒");
        Map<String,Object> mapCopy = variables.entrySet().stream().collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));
        mapCopy.put("companyName","【"+quotation.getCompanyName()+"】");
        mapCopy.put("firstContent","您好，"+inquire.getCompanyName()+"退回了贵公司的报价【报价单编号："+quotation.getId()+"】：");
        quotationEmailVO.setVariables(mapCopy);
        emailTool.commonSendMail(quotationEmailVO);
    }
}
