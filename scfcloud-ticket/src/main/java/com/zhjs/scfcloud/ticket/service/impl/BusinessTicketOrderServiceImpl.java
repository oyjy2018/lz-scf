package com.zhjs.scfcloud.ticket.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jd.jr.cbp.sdk.SdkException;
import com.jd.jr.cbp.sdk.entity.CommonResponse;
import com.jd.jr.cbp.sdk.entity.billpay.common.AccountDetail;
import com.jd.jr.cbp.sdk.entity.billpay.common.BillDetail;
import com.jd.jr.cbp.sdk.entity.billpay.request.CreateOrderReqBody;
import com.jd.jr.cbp.sdk.entity.billpay.response.CreateOrderRespBody;
import com.jd.jr.cbp.sdk.entity.billpay.response.GetCashierUrlRespBody;
import com.jd.jr.cbp.sdk.entity.billpay.response.HandleOrderRespBody;
import com.jd.jr.cbp.sdk.entity.notify.request.NotifyEcdsResult;
import com.jd.jr.cbp.sdk.entity.notify.request.OrderStatusMessageBody;
import com.jd.jr.cbp.sdk.enums.SdkCodeEnum;
import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.businessTicket.BusinessTicketOrderAllListDTO;
import com.zhjs.scfcloud.model.dto.businessTicket.OrderBaseDTO;
import com.zhjs.scfcloud.model.dto.businessTicket.QueryOrderListDTO;
import com.zhjs.scfcloud.model.dto.businessTicket.RevokeOrderDTO;
import com.zhjs.scfcloud.model.entity.*;
import com.zhjs.scfcloud.model.mapper.*;
import com.zhjs.scfcloud.model.util.JdzpRespUtil;
import com.zhjs.scfcloud.model.vo.businessTicket.OrderDetailsVO;
import com.zhjs.scfcloud.model.vo.businessTicket.OrderListVO;
import com.zhjs.scfcloud.ticket.service.BusinessTicketOrderService;
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
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author: dailongting
 * @date:2019/7/17 14:34
 */
@Service
public class BusinessTicketOrderServiceImpl implements BusinessTicketOrderService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private BusinessTicketOrderMapper businessTicketOrderMapper;
    @Resource
    private BusinessTicketFileMapper businessTicketFileMapper;
    @Resource
    private JDZPUtil jdzpUtil;
    @Resource
    private BusinessTicketInquireMapper businessTicketInquireMapper;
    @Resource
    private BusinessTicketQuotationMapper businessTicketQuotationMapper;
    @Resource
    private CompanyBankAccountMapper companyBankAccountMapper;
    @Resource
    private OperateLogMapper operateLogMapper;
    @Resource
    private BusinessTicketOrderEndorseMapper businessTicketOrderEndorseMapper;
    @Resource
    private UserMapper userMapper;

    @Autowired
    private EmailTool emailTool;
    @Value("${frontEndUrl.bill}")
    private String billFrontEndUrl;
    @Autowired
    private SmsUtil smsUtil;

    public static void main(String[] args) {
        System.out.println(BigDecimal.ZERO.compareTo(new BigDecimal(0)));
    }

    @Override
    public Result findOrderList(QueryOrderListDTO dto) {
        //处理成交时间
        if(!StringUtil.isEmpty(dto.getDealStartDate())){
            dto.setDealStartDate(dto.getDealStartDate()+" 00:00:00");
        }
        if(!StringUtil.isEmpty(dto.getDealEndDate())){
            dto.setDealEndDate(dto.getDealEndDate()+" 23:59:59");
        }

        List<OrderListVO> orderListVOList = businessTicketOrderMapper.findOrderList(dto);
        Map retMap = new HashMap();
        retMap.put("total",dto.getTotal());
        retMap.put("list", orderListVOList);
        return Result.success(retMap);
    }

    @Override
    public OrderDetailsVO findOrderDetails(OrderBaseDTO dto) {
        OrderDetailsVO details = businessTicketOrderMapper.findOrderDetails(dto.getOrderId());

        //查询票据文件
        LambdaQueryWrapper<BusinessTicketFile> wrapper = new QueryWrapper<BusinessTicketFile>().lambda();
        wrapper.eq(BusinessTicketFile::getInquireId, details.getInquireId());
        wrapper.orderByAsc(BusinessTicketFile::getSort);
        List<BusinessTicketFile> ticketUrlList = businessTicketFileMapper.selectList(wrapper);
        details.setTicketUrlList(ticketUrlList);
        return details;
    }

    @Override
    public Result payOrder(OrderBaseDTO dto) {
        BusinessTicketOrder order = businessTicketOrderMapper.selectById(dto.getOrderId());
        if(order == null){
            return Result.failure("订单数据不存在");
        }

        if(BusinessTicketEnum.OrderStatus.ORDER_STATUS_3.getStatus().intValue() != order.getOrderStatus().intValue()){
            return Result.failure("订单发起支付失败！订单状态为【"+ BusinessTicketEnum.OrderStatus.getStatusCHByStatus(order.getOrderStatus()) +"】");
        }

        String returnUrl = "&cb=" + URLEncoder.encode(PropertiesUtil.getProperty("jdzp.platform.return.url"));
        //判断订单支付状态为支付中，则去京东智票获取订单收银台地址
        if(order.getPayStatus() != null && BusinessTicketEnum.OrderPayStatus.Order_Pay_Status_1.getStatus().intValue() == order.getPayStatus().intValue()){
            CommonResponse<GetCashierUrlRespBody> resp = jdzpUtil.getCashierUrl(order.getPlatformReqNo());
            Result result = JdzpRespUtil.analyzeResponseJDZP(resp);
            if(result.getCode() != Result.RESULT_CODE_SUCCESS){
                return Result.failure("订单继续支付失败！失败原因："+result.getMessage());
            }
            Map<String, Object> jMap = new HashMap<>();
            jMap.put("cashierDeskUrl",resp.getData().getCashierDeskUrl()+returnUrl);
            return Result.success("继续支付成功",jMap);
        }else if(order.getPayStatus() != null && BusinessTicketEnum.OrderPayStatus.Order_Pay_Status97.getStatus().intValue() == order.getPayStatus().intValue()){
            return Result.failure("当前订单正在进行支付过期处理，请稍后再进行操作！");
        }

        //询价（票方）
        BusinessTicketInquire inquire = businessTicketInquireMapper.selectById(order.getInquireId());
        if(inquire == null){
            return Result.failure("订单发起支付失败！关联商票数据不存在！");
        }

        //报价（买方）
        BusinessTicketQuotation quotation = businessTicketQuotationMapper.selectById(order.getQuotationId());
        if(quotation == null){
            return Result.failure("订单发起支付失败！关联报价数据不存在！");
        }

        //查询票方收款账户
        LambdaQueryWrapper<CompanyBankAccount> wrapper = new QueryWrapper<CompanyBankAccount>().lambda();
        wrapper.eq(CompanyBankAccount::getCompanyId, inquire.getCompanyId());
        wrapper.eq(CompanyBankAccount::getIsRepayDefault, CommonEnum.WhetherEnum.YES.getStatus());
        CompanyBankAccount inquireBank = companyBankAccountMapper.selectOne(wrapper);
        if(inquireBank == null){
            return Result.failure("订单发起支付失败！票方（卖方）无默认收款账户，请联系票方进行设置！");
        }

        //查询买方收票账户
        wrapper = new QueryWrapper<CompanyBankAccount>().lambda();
        wrapper.eq(CompanyBankAccount::getCompanyId, quotation.getCompanyId());
        wrapper.eq(CompanyBankAccount::getIsReceiptDefault, CommonEnum.WhetherEnum.YES.getStatus());
        CompanyBankAccount quotationBank = companyBankAccountMapper.selectOne(wrapper);
        if(quotationBank == null){
            return Result.failure("订单发起支付失败！买方无默认收票账户，请买方进行设置！");
        }

        //开始向京东智票发起创建订单请求
        Date sysDate = new Date();
        //创建请求参数对象
        CreateOrderReqBody createOrderReqBody = new CreateOrderReqBody();
        //没有特殊注明可为空的参数就是必须传值
        //平台请求流水号
        createOrderReqBody.setPlatformReqNo(JDZPUtil.getPlatformReqNo(order.getId()));
        //平台订单号
        createOrderReqBody.setPlatformOrderNo(JDZPUtil.getPlatformReqNo(order.getId()));
        //平台下单时间: yyyyMMddHHmmss
        createOrderReqBody.setPlatformOrderDate(DateUtil.format(sysDate,"yyyyMMddHHmmss"));
        //平台合同编码
        createOrderReqBody.setPlatformContractNo(order.getContractNo());
        //订单过期时间 yyyyMMddHHmmss
        createOrderReqBody.setExpireDate(DateUtil.format(DateUtil.add(sysDate,5,2),"yyyyMMddHHmmss"));
        //贴现率 可为空(不写此值)
        createOrderReqBody.setTransRate("0");
        //交易金额（不含手续费），单位：分
        Long transAmt = order.getAmounts().multiply(BigDecimal.valueOf(100)).setScale(0,BigDecimal.ROUND_HALF_UP).longValue();
        createOrderReqBody.setTransAmt(transAmt);
        //手续费金额，单位：分
        Long transCharge = 0L;
        if (order.getServiceRate() != null && order.getServiceRate().compareTo(BigDecimal.ZERO) > 0) {
            transCharge = new BigDecimal(transAmt).multiply(order.getServiceRate()).divide(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_HALF_UP).longValue();
        }
        createOrderReqBody.setTransCharge(transCharge);
        //买方用户平台方ID
        createOrderReqBody.setBuyerUserId(quotation.getCompanyId().toString());

        //创建买方收票账户详情对象
        AccountDetail quotationAccount = new AccountDetail();
        //绑定ID，通过2#接口可查询到
        quotationAccount.setBindId(quotationBank.getBindId());
        //开户行联行号
        quotationAccount.setBankCnaps(quotationBank.getBankCnaps());
        //银行账号
        quotationAccount.setAccountNo(quotationBank.getBankAccountNo());
        //买方收票账户（ECDS验证）
        createOrderReqBody.setBuyerAccount(quotationAccount);

        //创建票方收款账户详情对象
        AccountDetail inquireAccount = new AccountDetail();
        //绑定ID，通过2#接口可查询到
        inquireAccount.setBindId(inquireBank.getBindId());
        //开户行联行号
        inquireAccount.setBankCnaps(inquireBank.getBankCnaps());
        //银行账号
        inquireAccount.setAccountNo(inquireBank.getBankAccountNo());
        //卖方用户平台方ID
        createOrderReqBody.setSellerUserId(inquire.getCompanyId().toString());
        //卖方收款卖方持票账户账户（放款）
        createOrderReqBody.setSellerAccountA(inquireAccount);
        //（ECDS验证）
        createOrderReqBody.setSellerAccountB(inquireAccount);

        //创建票据对象
        BillDetail billDetail = new BillDetail();
        //票据号码
        billDetail.setBillNo(inquire.getBillNo());
        //票面金额，单位：分
        billDetail.setBillAmt(inquire.getBillAmt().multiply(BigDecimal.valueOf(100)).setScale(0,BigDecimal.ROUND_HALF_UP).longValue());
        //出票人名称 可为空(可不传此值)
        billDetail.setDrawerName("");
        //出票人开户行行号 可为空(可不传此值)
        billDetail.setDrawerBank("");
        //出票人开户账号 可为空(可不传此值)
        billDetail.setDrawerAccount("");
        //承兑人名称  可为空(可不传此值)
        billDetail.setAccepterName("");
        //承兑人开户行行号 可为空(可不传此值)
        billDetail.setAccepterBank("");
        //承兑人开户账号 可为空(可不传此值)
        billDetail.setAccepterAccount("");
        //收款人名称 可为空(可不传此值)
        billDetail.setPayeeName("");
        //收款人开户行行号 可为空(可不传此值)
        billDetail.setPayeeBank("");
        //收款人开户账号 可为空(可不传此值)
        billDetail.setPayeeAccount("");
        //票据信息
        createOrderReqBody.setBillDetail(billDetail);

        logger.info("createOrder param:{}",JsonUtil.toSerializerJSON(createOrderReqBody));
        CommonResponse<CreateOrderRespBody> resp = jdzpUtil.createOrder(createOrderReqBody);
        Result result = JdzpRespUtil.analyzeResponseJDZP(resp);
        if(result.getCode() != Result.RESULT_CODE_SUCCESS){
            return Result.failure("订单发起支付失败！失败原因："+result.getMessage());
        }

        //解析创建京东订单接口响应数据
        CreateOrderRespBody respBody = resp.getData();

        order.setJdOrderNo(respBody.getJdOrderNo());
        order.setPlatformReqNo(respBody.getPlatformReqNo());
        order.setOrderNo(createOrderReqBody.getPlatformOrderNo());
        order.setPayLaunchTime(DateUtil.parse(createOrderReqBody.getPlatformOrderDate(),"yyyyMMddHHmmss"));
        order.setPayOverdueTime(DateUtil.parse(createOrderReqBody.getExpireDate(),"yyyyMMddHHmmss"));
        order.setPayStatus(BusinessTicketEnum.OrderPayStatus.Order_Pay_Status_1.getStatus());
        order.setUpdateBy(dto.getUserId());
        order.setUpdateTime(sysDate);
        businessTicketOrderMapper.updateById(order);

        Map<String, Object> jMap = new HashMap<>();
        jMap.put("cashierDeskUrl",respBody.getCashierDeskUrl()+returnUrl);
        return Result.success("发起支付成功",jMap);
    }

    @Override
    public Result revokeOrder(RevokeOrderDTO dto) {
        BusinessTicketOrder order = businessTicketOrderMapper.selectById(dto.getOrderId());
        if(order == null){
            return Result.failure("订单数据不存在");
        }

        if(!BusinessTicketEnum.OrderStatus.hasAllowRevoke(order.getOrderStatus().intValue())){
            return Result.failure("撤消订单失败！订单状态为【"+ BusinessTicketEnum.OrderStatus.getStatusCHByStatus(order.getOrderStatus()) +"】");
        }

        //判断是否已经发起支付（是否已创建京东智票订单）
        if(order.getPayStatus() == null){
            order.setOrderStatus(BusinessTicketEnum.OrderStatus.ORDER_STATUS_98.getStatus());
        }else{
            if(BusinessTicketEnum.OrderPayStatus.Order_Pay_Status97.getStatus().intValue() == order.getPayStatus().intValue()){
                return Result.failure("当前订单正在进行支付过期处理，请稍后再进行操作！");
            }

            //京东智票订单操作接口，2代表撤单操作
            CommonResponse<HandleOrderRespBody> resp = jdzpUtil.handleOrder(order.getPlatformReqNo(),2);
            logger.info("京东智票,撤单结果："+ resp.toString());
            Result result = JdzpRespUtil.analyzeResponseJDZP(resp);
            if(result.getCode() != Result.RESULT_CODE_SUCCESS){
                return Result.failure("撤消订单失败！撤销京东智票订单错误："+result.getMessage());
            }

            order.setOrderStatus(BusinessTicketEnum.OrderStatus.ORDER_STATUS_97.getStatus());
        }

        order.setUpdateBy(dto.getUserId());
        order.setUpdateTime(new Date());
        order.setCancelPerson(dto.getUserName());
        order.setCancelPersonId(dto.getUserId());

        //判断是票方撤消还是买方撤消
        if(BusinessTicketEnum.UserType.USER_TYPE_1.getStatus().intValue() == dto.getUserType().intValue()){
            order.setCancelType(BusinessTicketEnum.CancelType.CANCEL_TYPE_2.getStatus());
        }else if(BusinessTicketEnum.UserType.USER_TYPE_2.getStatus().intValue() == dto.getUserType().intValue()){
            order.setCancelType(BusinessTicketEnum.CancelType.CANCEL_TYPE_1.getStatus());
        }else{
            return Result.failure("撤消订单失败！用户类型不明确！");
        }

        if(businessTicketOrderMapper.updateById(order) == 0){
            return Result.failure("撤销订单发生异常！更新订单状态失败！");
        }
        return Result.success("撤销订单成功！");
    }

    @Override
    public Result updateOrderStatus(OrderStatusMessageBody orderStatusMessageBody) {
        LambdaQueryWrapper<BusinessTicketOrder> wrapper = new QueryWrapper<BusinessTicketOrder>().lambda();
        wrapper.eq(BusinessTicketOrder::getPlatformReqNo,orderStatusMessageBody.getPlatformReqNo());
        wrapper.eq(BusinessTicketOrder::getJdOrderNo,orderStatusMessageBody.getJdOrderNo());
        BusinessTicketOrder order = businessTicketOrderMapper.selectOne(wrapper);
        if(order == null){
            return Result.failure("处理京东智票异步回调通知异常：找不到关联的订单！请求流水号："+orderStatusMessageBody.getPlatformReqNo());
        }

        int jdOrderStatus = Integer.valueOf(orderStatusMessageBody.getJdOrderStatus());

        //判断是否是支付中状态
        if(BusinessTicketEnum.JdOrderStatus.hasPaying(jdOrderStatus)){
            order.setOrderStatus(BusinessTicketEnum.OrderStatus.ORDER_STATUS_3.getStatus());
            order.setPayStatus(BusinessTicketEnum.OrderPayStatus.Order_Pay_Status_1.getStatus());
        }else if(BusinessTicketEnum.JdOrderStatus.hasWaitEndorse(jdOrderStatus)){ //判断是否处于领筑订单的支付成功-待背书状态
            order.setOrderStatus(BusinessTicketEnum.OrderStatus.ORDER_STATUS_4.getStatus());
            //如果通知买方支付成功，则更新订单支付状态
            if(BusinessTicketEnum.JdOrderStatus.JdOrderStatus2.getStatus() == jdOrderStatus){
                order.setPayStatus(BusinessTicketEnum.OrderPayStatus.Order_Pay_Status_99.getStatus());
                order.setPayEndTime(new Date());
            }
        }else if(BusinessTicketEnum.JdOrderStatus.JdOrderStatus3.getStatus() == jdOrderStatus){//支付失败
            order.setPayStatus(BusinessTicketEnum.OrderPayStatus.Order_Pay_Status98.getStatus());
        }else if(BusinessTicketEnum.JdOrderStatus.JdOrderStatus7.getStatus() == jdOrderStatus){//撤单成功
            //如果当前订单处于支付撤销中/支付撤销失败，则系统认为该订单正在发起支付撤销，因为支付撤销与订单撤销都是撤销京东智票的订单，所以都是同一个通知
            if(BusinessTicketEnum.OrderPayStatus.Order_Pay_Status97.getStatus().intValue() == order.getPayStatus().intValue()
                    || BusinessTicketEnum.OrderPayStatus.Order_Pay_Status96.getStatus().intValue() == order.getPayStatus().intValue()){
                order.setPayStatus(null);
            }else{
                order.setOrderStatus(BusinessTicketEnum.OrderStatus.ORDER_STATUS_98.getStatus());
            }
        }else if(BusinessTicketEnum.JdOrderStatus.JdOrderStatus8.getStatus() == jdOrderStatus){//撤单失败
            //如果当前订单处于支付撤销中/支付撤销失败，则系统认为该订单正在发起支付撤销，因为支付撤销与订单撤销都是撤销京东智票的订单，所以都是同一个通知
            if(BusinessTicketEnum.OrderPayStatus.Order_Pay_Status97.getStatus().intValue() == order.getPayStatus().intValue()
                    || BusinessTicketEnum.OrderPayStatus.Order_Pay_Status96.getStatus().intValue() == order.getPayStatus().intValue()){
                order.setPayStatus(BusinessTicketEnum.OrderPayStatus.Order_Pay_Status96.getStatus());
            }else{
                order.setOrderStatus(BusinessTicketEnum.OrderStatus.order_status_96.getStatus());
            }
        }else if(BusinessTicketEnum.JdOrderStatus.JdOrderStatus5.getStatus() == jdOrderStatus){//已背书-待签收
            order.setOrderStatus(BusinessTicketEnum.OrderStatus.ORDER_STATUS_5.getStatus());
        }else if(BusinessTicketEnum.JdOrderStatus.JdOrderStatus10.getStatus() == jdOrderStatus){//已背书-已签收
            order.setOrderStatus(BusinessTicketEnum.OrderStatus.ORDER_STATUS_6.getStatus());
        }else if(BusinessTicketEnum.JdOrderStatus.JdOrderStatus11.getStatus() == jdOrderStatus){//交易成功
            order.setOrderStatus(BusinessTicketEnum.OrderStatus.ORDER_STATUS_99.getStatus());
            order.setRemitStatus(BusinessTicketEnum.OrderRemitStatus.ORDER_REMIT_STATUS_99.getStatus());
            order.setCommDeductStatus(BusinessTicketEnum.OrderCommDeductStatus.ORDER_COMM_DEDUCT_STATUS_99.getStatus());
            order.setDealTime(LocalDateTime.now());
        }else{//非上述状态的通知，不做处理，默认通过
            return Result.success();
        }
        order.setUpdateTime(new Date());
        if(businessTicketOrderMapper.updateById(order) == 0){
            return Result.failure("处理京东智票异步回调通知异常：更新订单状态失败！请通知我方运营或产品！");
        }

        //发送通知处理
        if(order.getOrderStatus() == BusinessTicketEnum.OrderStatus.ORDER_STATUS_4.getStatus()
                || order.getOrderStatus() == BusinessTicketEnum.OrderStatus.ORDER_STATUS_5.getStatus()
                    || order.getOrderStatus() == BusinessTicketEnum.OrderStatus.ORDER_STATUS_6.getStatus()
                        || order.getOrderStatus() == BusinessTicketEnum.OrderStatus.ORDER_STATUS_99.getStatus()){

            handleSendEmail(order,order.getOrderStatus());
        }

        return Result.success();
    }

    /**
     * 发送通知：支付成功-待背书
     * @param order
     */
    private void handleSendEmail(BusinessTicketOrder order,Integer orderStatus){
        BusinessTicketInquire inquire = businessTicketInquireMapper.selectById(order.getInquireId());
        User inquireUser = userMapper.selectById(inquire.getPublishPersonId());
        BusinessTicketQuotation quotation = businessTicketQuotationMapper.selectById(order.getQuotationId());
        User quotationUser = userMapper.selectById(quotation.getQuotationPersonId());
        //卖家
        EmailVO emailVO = new EmailVO();
        emailVO.setTo(new String[]{inquireUser.getEmail()});
        emailVO.setTemplate("NoticeTemplate.html");
        emailVO.setTitle("【领筑票据融资平台】订单状态更新提醒");
        Map<String,Object> variables = new HashMap<>();
        variables.put("fileServiceUrl", FileUtil.getFileServiceUrl());
        variables.put("firstH","【"+inquire.getCompanyName()+"】");
        if(orderStatus == BusinessTicketEnum.OrderStatus.ORDER_STATUS_4.getStatus()){
            variables.put("content","您好，订单编号："+order.getId()+"，状态为【"+BusinessTicketEnum.OrderStatus.ORDER_STATUS_4.getStatusCH()+"】，请前往企业网银ECDS系统将票据背书给资方【"+quotation.getCompanyName()+"】。");
        }else if(orderStatus == BusinessTicketEnum.OrderStatus.ORDER_STATUS_5.getStatus()){
            variables.put("content","您好，订单编号："+order.getId()+"，状态为【"+BusinessTicketEnum.OrderStatus.ORDER_STATUS_5.getStatusCH()+"】。");
        }else if(orderStatus == BusinessTicketEnum.OrderStatus.ORDER_STATUS_6.getStatus()){
            variables.put("content","您好，订单编号："+order.getId()+"，状态为【"+BusinessTicketEnum.OrderStatus.ORDER_STATUS_6.getStatusCH()+"】。");
        }else{
            variables.put("content","您好，订单编号："+order.getId()+"，状态为【"+BusinessTicketEnum.OrderStatus.ORDER_STATUS_99.getStatusCH()+"】。");
        }
        variables.put("url", billFrontEndUrl+"/sell/orderdetail?orderId="+order.getId());
        variables.put("linkContent","点此查看订单详情");
        emailVO.setVariables(variables);
        emailTool.commonSendMail(emailVO);
        //买家
        EmailVO quotationEmailVO = new EmailVO();
        quotationEmailVO.setTo(new String[]{quotationUser.getEmail()});
        quotationEmailVO.setTemplate("NoticeTemplate.html");
        quotationEmailVO.setTitle("【领筑票据融资平台】订单状态更新提醒");
        Map<String,Object> mapCopy = variables.entrySet().stream().collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));
        mapCopy.put("firstH","【"+quotation.getCompanyName()+"】");
        if(orderStatus == BusinessTicketEnum.OrderStatus.ORDER_STATUS_4.getStatus()){
            mapCopy.put("content","您好，订单编号："+order.getId()+"，状态为【"+BusinessTicketEnum.OrderStatus.ORDER_STATUS_4.getStatusCH()+"】。");
            //发送短信-卖方
            Map<String, String> params = new HashMap<>();
            params.put("orderNo",order.getId()+"");
            params.put("orderStatus",BusinessTicketEnum.OrderStatus.ORDER_STATUS_4.getStatusCH());
            params.put("buyerCompanyName",quotation.getCompanyName());
            smsUtil.lzySmsBsend(inquireUser.getPhone(), SmsUtil.lzy_sms_type_S504,params);
        }else if(orderStatus == BusinessTicketEnum.OrderStatus.ORDER_STATUS_5.getStatus()){
            mapCopy.put("content","您好，订单编号："+order.getId()+"，状态为【"+BusinessTicketEnum.OrderStatus.ORDER_STATUS_5.getStatusCH()+"】，请前往企业网银ECDS系统收票。");
            //发送短信-买方
            Map<String, String> params = new HashMap<>();
            params.put("orderNo",order.getId()+"");
            params.put("orderStatus",BusinessTicketEnum.OrderStatus.ORDER_STATUS_5.getStatusCH());
            smsUtil.lzySmsBsend(quotationUser.getPhone(), SmsUtil.lzy_sms_type_S505,params);
        }else if(orderStatus == BusinessTicketEnum.OrderStatus.ORDER_STATUS_6.getStatus()){
            mapCopy.put("content","您好，订单编号："+order.getId()+"，状态为【"+BusinessTicketEnum.OrderStatus.ORDER_STATUS_6.getStatusCH()+"】。");
        }else{
            mapCopy.put("content","您好，订单编号："+order.getId()+"，状态为【"+BusinessTicketEnum.OrderStatus.ORDER_STATUS_99.getStatusCH()+"】。");
        }
        quotationEmailVO.setVariables(mapCopy);
        emailTool.commonSendMail(quotationEmailVO);

    }


    @Override
    @Transactional
    public Result updateOrderBillStatus(NotifyEcdsResult ecdsResultMessageBody) {
        // 先判断订单任务状态，如果是 0、3，则判断票据状态。如果是1、2 则票据状态无意义，此时为空，
        if(ecdsResultMessageBody.getOrderStatus() == 0 || ecdsResultMessageBody.getOrderStatus() == 3){
            LambdaQueryWrapper<BusinessTicketOrder> wrapper = new QueryWrapper<BusinessTicketOrder>().lambda();
            wrapper.eq(BusinessTicketOrder::getOrderNo,ecdsResultMessageBody.getPlatformOrderNo());
            BusinessTicketOrder order = businessTicketOrderMapper.selectOne(wrapper);
            if(order == null){
                return Result.failure("处理京东智票异步回调通知异常：找不到关联的订单！平台订单号："+ecdsResultMessageBody.getPlatformOrderNo());
            }

            BusinessTicketInquire inquire = businessTicketInquireMapper.selectById(order.getInquireId());
            if(inquire == null){
                return Result.failure("处理京东智票异步回调通知异常：找不到关联的票据信息！平台订单号："+ecdsResultMessageBody.getPlatformOrderNo());
            }
            //背书待签收
            if(ecdsResultMessageBody.getBillStatus() == 1){
                inquire.setBillStatus(BusinessTicketEnum.BillStatus.bill_status_1.getStatus());
                order.setOrderStatus(BusinessTicketEnum.OrderStatus.ORDER_STATUS_5.getStatus());
            }else if(ecdsResultMessageBody.getBillStatus() == 2){   //背书已签收
                inquire.setBillStatus(BusinessTicketEnum.BillStatus.bill_status_2.getStatus());
                order.setOrderStatus(BusinessTicketEnum.OrderStatus.ORDER_STATUS_6.getStatus());
                try{
                    //保存背书记录
                    BusinessTicketOrderEndorse endorse = new BusinessTicketOrderEndorse();
                    endorse.setOrderId(order.getId())
                            .setBillNo(ecdsResultMessageBody.getBillNo())
                            .setBillEndDate(DateUtil.parse(ecdsResultMessageBody.getBillEndDate(),"yyyyMMdd"))
                            .setBillAmt(BigDecimal.valueOf(ecdsResultMessageBody.getBillAmt()).divide(BigDecimal.valueOf(100)))
                            .setEndorserName(ecdsResultMessageBody.getEndorserName())
                            .setEndorserAccount(ecdsResultMessageBody.getEndorserAccount())
                            .setEndorseeName(ecdsResultMessageBody.getEndorseeName())
                            .setEndorseeAccount(ecdsResultMessageBody.getEndorseeAccount())
                            .setEndorseDate(new Date())
                            .setCreateBy(1L)
                            .setCreateTime(new Date())
                    ;
                    //背书记录保存失败不能影响流程
                    if(businessTicketOrderEndorseMapper.insert(endorse) == 0){
                        logger.error("保存背书记录失败，订单ID：{}",order.getId());
                    }
                }catch (Exception e){
                    logger.error("保存背书记录异常：订单ID【{}】，异常信息：{}",order.getId(),e.getMessage());
                }
            }

            order.setUpdateTime(new Date());
            if(businessTicketOrderMapper.updateById(order) == 0){
                return Result.failure("处理京东智票异步回调通知异常：更新背书状态失败！请通知我方运营或产品！");
            }
            if(businessTicketInquireMapper.updateById(inquire) == 0){
                throw  new SdkException(SdkCodeEnum.SDK_UNKNOWN,"处理京东智票异步回调通知异常：更新背书状态失败！请通知我方运营或产品！");
            }

            //发送通知处理
            if(order.getOrderStatus() == BusinessTicketEnum.OrderStatus.ORDER_STATUS_5.getStatus()
                    || order.getOrderStatus() == BusinessTicketEnum.OrderStatus.ORDER_STATUS_6.getStatus()){
                handleSendEmail(order,order.getOrderStatus());
            }
        }
        return Result.success();
    }

    //所有订单
    @Override
    public String allOrder(BusinessTicketOrderAllListDTO dto) {
        dto.setRecords(businessTicketOrderMapper.getAllOrderList(dto));
        return Result.successPage(dto,"list").toSerializerJSON();
    }

    //重新发起商票状态轮询
    @Override
    public Result reStartTicketStatusQuery(Long id) {
        //查询订单
        BusinessTicketOrder order = businessTicketOrderMapper.selectById(id);
        if (order == null) return Result.failure("订单数据不存在");
        int orderStatus = order.getOrderStatus().intValue();
        logger.info("orderStatus:{}",orderStatus);
        //只有待背书和待签收时可以重新发起轮询
        if (BusinessTicketEnum.OrderStatus.ORDER_STATUS_4.getStatus().intValue() != orderStatus
                &&BusinessTicketEnum.OrderStatus.ORDER_STATUS_5.getStatus().intValue() != orderStatus) {
            return Result.failure("当前状态不能重新发起商票状态轮询");
        }
        String platformReqNo = order.getPlatformReqNo();
        CommonResponse<HandleOrderRespBody> commonResponse = jdzpUtil.handleOrder(platformReqNo,1);//固定为1 继续查询票据状态

        //解析结果
        Result analyzeResult =JdzpRespUtil.analyzeResponseJDZP(commonResponse);
        if (analyzeResult.getCode() == Result.RESULT_CODE_FAILURE) return analyzeResult;

        return Result.success();
    }
}
