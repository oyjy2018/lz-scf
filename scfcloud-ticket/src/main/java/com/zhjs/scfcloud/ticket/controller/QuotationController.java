package com.zhjs.scfcloud.ticket.controller;

import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.QuotationOwnerQueryDTO;
import com.zhjs.scfcloud.model.dto.QuotationTicketQueryDTO;
import com.zhjs.scfcloud.model.dto.ticket.AllQuotationListDTO;
import com.zhjs.scfcloud.model.vo.UserInfoVO;
import com.zhjs.scfcloud.ticket.service.QuotationService;
import com.zhjs.scfcloud.ticket.util.MySubjectUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author weijie.chen
 */
@Api("报价controller")
@RestController
public class QuotationController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    QuotationService quotationService;

    @ApiOperation("票方-报价单列表")
    @PostMapping("/quotation/ticket")
    @RequiresPermissions("ticket:quotation:list")
    public Result ticketQuotation(@RequestBody QuotationTicketQueryDTO dto){
        logger.info("subject:{},dto:{}","票方-报价单列表",dto.toString());

        UserInfoVO user = MySubjectUtil.getUser();
        if (user == null) return Result.failure("没有登录信息");

        dto.setInquireCompanyId(user.getCompanyId());
        return Result.success(quotationService.selectQuotationTicketList(dto));
    }

    @ApiOperation("报价单详情")
    @GetMapping("/quotation/{quotationId}/detail")
    @RequiresPermissions(value = {"ticket:quotation:detail","ticket:quotation:detail:inquire"},logical = Logical.OR)
    public Result detail(@PathVariable Long quotationId){
        logger.info("subject:{},quotationId:{}","报价单详情",quotationId.toString());

        UserInfoVO user = MySubjectUtil.getUser();
        if (user == null) return Result.failure("没有登录信息");

        return Result.success(quotationService.selectQuotationDetail(quotationId));
    }

    @ApiOperation("资产-我的报价")
    @PostMapping("/quotation/owner")
    @RequiresPermissions("ticket:quotation:owner")
    public Result owner(@RequestBody QuotationOwnerQueryDTO dto){
        logger.info("subject:{},dto:{}","票方-我的报价",dto.toString());

        UserInfoVO user = MySubjectUtil.getUser();
        if (user == null) return Result.failure("没有登录信息");

        dto.setQuotationCompanyId(user.getCompanyId());
        return Result.success(quotationService.selectQuotationOwnerList(dto));
    }

    @ApiOperation("撤销报价")
    @DeleteMapping("/quotation/owner/{quotationId}")
    @RequiresPermissions("ticket:quotation:cancel")
    public Result delete(@PathVariable Long quotationId){
        UserInfoVO user = MySubjectUtil.getUser();
        if (user == null) return Result.failure("没有登录信息");
        return quotationService.deleteQuotationByOwner(quotationId,user.getId());
    }

    @ApiOperation("所有报价单列表")
    @GetMapping("/quotation/ticket/all")
    @RequiresPermissions("operationsMgt:allQuotation:view")
    public Result allQuotations(AllQuotationListDTO dto){
        logger.info("subject:{},dto:{}","所有报价单列表",dto.toString());
        UserInfoVO user = MySubjectUtil.getUser();
        if (user == null) return Result.failure("没有登录信息");
        return Result.success(quotationService.findAllQuotations(dto,user.getId()));
    }
}
