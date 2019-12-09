package com.zhjs.scfcloud.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jd.jr.cbp.sdk.entity.CommonResponse;
import com.jd.jr.cbp.sdk.entity.billpay.response.HandleOrderRespBody;
import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.businessTicket.BusinessTicketOrderAllListDTO;
import com.zhjs.scfcloud.model.dto.businessTicket.OrderBaseDTO;
import com.zhjs.scfcloud.model.entity.*;
import com.zhjs.scfcloud.model.mapper.*;
import com.zhjs.scfcloud.model.util.JdzpRespUtil;
import com.zhjs.scfcloud.model.vo.QuotationDetailVO;
import com.zhjs.scfcloud.model.vo.businessTicket.BusinessTicketInquireDetailVO;
import com.zhjs.scfcloud.model.vo.businessTicket.OrderDetailsVO;
import com.zhjs.scfcloud.system.controller.TicketOrderController;
import com.zhjs.scfcloud.system.service.TicketOrderService;
import com.zhjs.scfcloud.util.enums.BusinessTicketEnum;
import com.zhjs.scfcloud.util.util.JDZPUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

@Service
public class TicketOrderServiceImpl implements TicketOrderService {

    private Logger logger = LoggerFactory.getLogger(TicketOrderServiceImpl.class);

    @Resource
    private BusinessTicketOrderMapper businessTicketOrderMapper;
    @Resource
    private JDZPUtil jdzpUtil;
    @Resource
    private BusinessTicketFileMapper businessTicketFileMapper;
    @Resource
    private BusinessTicketInquireMapper businessTicketInquireMapper;
    @Resource
    private BusinessTicketQuotationMapper businessTicketQuotationMapper;
    @Resource
    private UserMapper userMapper;

    //获取所有商票订单
    @Override
    public String getAllOrderList(BusinessTicketOrderAllListDTO dto) {
        dto.setRecords(businessTicketOrderMapper.getAllOrderList(dto));
        return Result.successPage(dto,"list").toSerializerJSON();
    }

    //重新发起商票状态轮询
    @Override
    public String reStartTicketStatusQuery(Long id) {
        //查询订单
        BusinessTicketOrder order = businessTicketOrderMapper.selectById(id);
        if (order == null) return Result.failure("订单数据不存在").toJSON();
        int orderStatus = order.getOrderStatus().intValue();
        logger.info("orderStatus:{}",orderStatus);
        //只有待背书和待签收时可以重新发起轮询
        if (BusinessTicketEnum.OrderStatus.ORDER_STATUS_4.getStatus().intValue() != orderStatus
        &&BusinessTicketEnum.OrderStatus.ORDER_STATUS_5.getStatus().intValue() != orderStatus) {
            return Result.failure("当前状态不能重新发起商票状态轮询").toJSON();
        }
        String platformReqNo = order.getPlatformReqNo();
        CommonResponse<HandleOrderRespBody> commonResponse = jdzpUtil.handleOrder(platformReqNo,1);//固定为1 继续查询票据状态

        //解析结果
        Result analyzeResult =JdzpRespUtil.analyzeResponseJDZP(commonResponse);
        if (analyzeResult.getCode() == Result.RESULT_CODE_FAILURE) return analyzeResult.toJSON();

        return Result.success().toJSON();
    }

    //查询订单详情
    @Override
    public String findOrderDetails(OrderBaseDTO dto) {
        OrderDetailsVO details = businessTicketOrderMapper.findOrderDetails(dto.getOrderId());

        //查询票据文件
        LambdaQueryWrapper<BusinessTicketFile> wrapper = new QueryWrapper<BusinessTicketFile>().lambda();
        wrapper.eq(BusinessTicketFile::getInquireId, details.getInquireId());
        wrapper.orderByAsc(BusinessTicketFile::getSort);
        List<BusinessTicketFile> ticketUrlList = businessTicketFileMapper.selectList(wrapper);
        details.setTicketUrlList(ticketUrlList);

        if(details == null){
            return Result.failure("订单数据丢失").toJSON();
        }

        return Result.success(details).toSerializerJSON();
    }

    @Override
    public Result inquireDetail(Long inquireId) {
        BusinessTicketInquireDetailVO businessTicketInquireDetailVO = businessTicketInquireMapper.getInquireDetail(inquireId);
        if (businessTicketInquireDetailVO == null) return Result.failure("询价详情不存在");
        List<BusinessTicketFile> businessTicketFileList = businessTicketFileMapper.selectList(new QueryWrapper<BusinessTicketFile>().eq("inquire_id",businessTicketInquireDetailVO.getId()));
        businessTicketInquireDetailVO.setFileList(businessTicketFileList);
        return Result.success(businessTicketInquireDetailVO);
    }

    @Override
    public Result quotationDetail(Long quotationId) {
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
        return Result.success(result);
    }
}
