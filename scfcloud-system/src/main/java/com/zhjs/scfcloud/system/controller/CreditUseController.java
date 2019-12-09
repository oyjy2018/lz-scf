package com.zhjs.scfcloud.system.controller;

import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.BaseIdDTO;
import com.zhjs.scfcloud.model.dto.BaseSingleUpdateDTO;
import com.zhjs.scfcloud.model.dto.credit.*;
import com.zhjs.scfcloud.system.service.CreditUseService;
import com.zhjs.scfcloud.util.util.JsonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用信管理 控制器
 * <功能详细描述>
 * Version: 1.0.0, 2019-06-27 11:05
 *
 * @author liuchanghai
 * @create 2019-06-27 11:05
 * @since
 */

@Api(tags = "用信管理")
@RestController
@RequestMapping("/creditUse/")
public class CreditUseController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CreditUseService creditUseService;

    @ApiOperation("我的用信申请列表")
    @PostMapping("findCreditUseApplyMyList")
    public Result findCreditUseApplyMyList(@RequestBody CreditUseApplyMyListQueryDTO dto) {
        logger.info("获取我的用信申请列表:{}", dto.toString());
        return creditUseService.findCreditUseApplyMyList(dto);
    }

    @ApiOperation("所有用信申请列表")
    @PostMapping("findCreditUseApplyAllList")
    public String findCreditUseApplyAllList(@RequestBody CreditUseApplyAllListQueryDTO dto) {
        logger.info("获取所有用信申请列表:{}",JsonUtil.toSerializerJSON(dto));
        return creditUseService.findCreditUseApplyAllList(dto).toSerializerJSON();
    }

    @ApiOperation("用信审批列表")
    @PostMapping("findCreditUseAuditList")
    public Result findCreditUseAuditList(@RequestBody CreditUseAuditListQueryDTO dto) {
        logger.info("获取用信审批列表{}", dto.toString());
        return creditUseService.findCreditUseAuditList(dto);
    }

    @ApiOperation("录入用信记录")
    @PostMapping("addCreditUseRecord")
    public Result addCreditUseRecord(@RequestBody AddCreditUseRecordDTO dto) {
        logger.info("录入用信记录{}" + dto.toString());
        return creditUseService.addCreditUseRecord(dto);
    }

    @ApiOperation("查看用信申请详情")
    @PostMapping("findCreditUseApplyDetails")
    public Result findCreditUseApplyDetails(@RequestBody BaseIdDTO dto) {
        logger.info("查看用信申请详情{}" + dto.toString());
        return creditUseService.findCreditUseApplyDetails(dto);
    }

    @ApiOperation("查看用信申请文件资料")
    @PostMapping("document")
    @RequiresPermissions("risk:credit:use:document")
    public Result document(@RequestBody BaseIdDTO dto) {
        logger.info("查看用信申请详情:{}", dto.toString());
        return creditUseService.findCreditUseApplyDocuemnt(dto);
    }

    @ApiOperation("App端-查看用信申请详情")
    @PostMapping("findAppCreditUseApplyDetails")
    public Result findAppCreditUseApplyDetails(@RequestBody BaseIdDTO dto) {
        logger.info("App端-查看用信申请详情{}" + dto.toString());
        return creditUseService.findAppCreditUseApplyDetails(dto);
    }

    //我的用信列表
    @PostMapping("myAppList")
    String myAppList(@RequestBody CreditUseMyListAppDTO creditUseMyListAppDTO){
        logger.info("subject:{},creditUseMyListAppDTO:{}","App端-我的用信列表", JsonUtil.toJSON(creditUseMyListAppDTO));
        return creditUseService.myAppList(creditUseMyListAppDTO);
    }

    //web端我的用信列表
    @PostMapping("myWebList")
    public String myWebList(@RequestBody CreditUseMyListWebDTO dto){
        logger.info("subject:{},CreditUseMyListWebDTO:{}","web端我的用信列表", JsonUtil.toJSON(dto));
        return creditUseService.myWebList(dto);
    }

    //web端所有用信列表
    @PostMapping("allWebList")
    public String allWebList(@RequestBody CreditUseAllListWebDTO dto){
        logger.info("subject:{},CreditUseAllListWebDTO:{}","web端所有用信列表", JsonUtil.toJSON(dto));
        return creditUseService.allWebList(dto);
    }

    //修改还款状态
    @PostMapping("updateRefundStatus")
    public String updateRefundStatus(@RequestBody CreditUseUpdateRefundStatusDTO dto) {
        logger.info("subject:{},CreditUseUpdateRefundStatusDTO:{}","修改还款状态", JsonUtil.toJSON(dto));
        return creditUseService.updateRefundStatus(dto);
    }

    //web端用信详情
    @PostMapping("webDetail")
    public String webDetail(@RequestBody CreditUseDetailWebDTO dto) {
        logger.info("subject:{},CreditUseDetailWebDTO:{}","web端用信详情", JsonUtil.toJSON(dto));
        return creditUseService.webDetail(dto);
    }

    //商票列表
    @PostMapping("ticketList")
    public String ticketList(@RequestBody CreditTicketListWebDTO dto) {
        logger.info("subject:{},CreditTicketListWebDTO:{}","商票列表", JsonUtil.toJSON(dto));
        return creditUseService.ticketList(dto);
    }

    //商票详情
    @PostMapping("ticketDetail")
    public String ticketDetail(@RequestBody BaseIdDTO dto) {
        logger.info("subject:{},BaseIdDTO:{}","商票列表", JsonUtil.toJSON(dto));
        return creditUseService.ticketDetail(dto);
    }

    //修改商票状态
    @PostMapping("/updateTicketStatus")
    public Result updateTicketStatus(@RequestBody BaseSingleUpdateDTO dto){
        logger.info("subject:{},dto:{}","修改商票状态", JsonUtil.toSerializerJSON(dto));
        return creditUseService.updateTicketStatus(dto);
    }
}
