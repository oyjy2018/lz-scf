package com.zhjs.scfcloud.controller;

import com.jd.jr.cbp.sdk.entity.CommonResponse;
import com.jd.jr.cbp.sdk.entity.ocr.response.OcrRespBody;
import com.zhjs.scfcloud.feign.CreditUseFeignService;
import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.*;
import com.zhjs.scfcloud.model.dto.credit.*;
import com.zhjs.scfcloud.model.util.JdzpRespUtil;
import com.zhjs.scfcloud.model.vo.UserInfoVO;
import com.zhjs.scfcloud.service.CreditApplyService;
import com.zhjs.scfcloud.util.MySubjectUtil;
import com.zhjs.scfcloud.util.util.JDZPUtil;
import com.zhjs.scfcloud.util.util.JsonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 用信管理 控制器
 * <功能详细描述>
 * Version: 1.0.0, 2019-06-27 11:25
 *
 * @author liuchanghai
 * @create 2019-06-27 11:25
 * @since
 */

@Api(tags = "用信管理")
@RestController
@RequestMapping("/creditUse/")
public class CreditUseController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CreditUseFeignService creditUseFeignService;
    @Autowired
    JDZPUtil jdzpUtil;
    @Autowired
    private CreditApplyService creditApplyService;

    @ApiOperation("我的用信申请列表")
    @PostMapping("findCreditUseApplyMyList")
    @RequiresPermissions("risk:credit:use:myApplyList")
    public Result findCreditUseApplyMyList(@RequestBody CreditUseApplyMyListQueryDTO dto) {
        logger.info("获取我的用信申请列表:{}", dto.toString());
        if (MySubjectUtil.getUser() == null) return Result.failure("无登录信息");
        dto.setUserId(MySubjectUtil.getUser().getId());
        return creditUseFeignService.findCreditUseApplyMyList(dto);
    }

    @ApiOperation("所有用信申请列表")
    @PostMapping("findCreditUseApplyAllList")
    @RequiresPermissions("risk:credit:use:allApplyList")
    public String  findCreditUseApplyAllList(@RequestBody CreditUseApplyAllListQueryDTO dto) {
        logger.info("获取所有用信申请列表:{}", dto.toString());
        UserInfoVO user = MySubjectUtil.getUser();
        if (user == null) return Result.failure("未获取到用户信息").toJSON();
        dto.setUserId(user.getId());
        dto.setCompanyId(user.getCompanyId());
        dto.setRoleIds(user.getRoleList().stream().map(role -> role.getId().toString()).collect(Collectors.joining(",")));
        dto.setPermissionType(user.getPermissionType());
        dto.setPermissionOrgIds(user.getPermissionOrgIds());
        return creditUseFeignService.findCreditUseApplyAllList(dto);
    }

    @ApiOperation("用信审批列表")
    @PostMapping("findCreditUseAuditList")
    @RequiresPermissions("risk:credit:use:auditList")
    public Result findCreditUseAuditList(@RequestBody CreditUseAuditListQueryDTO dto) {
        logger.info("获取用信审批列表:{}" , dto.toString());
        UserInfoVO user = MySubjectUtil.getUser();
        if (user == null) return Result.failure("未获取到用户信息");
        dto.setUserId(user.getId());
        dto.setCompanyId(user.getCompanyId());
        dto.setRoleIds(user.getRoleList().stream().map(role -> role.getId().toString()).collect(Collectors.joining(",")));
        dto.setPermissionType(user.getPermissionType());
        dto.setPermissionOrgIds(user.getPermissionOrgIds());
        return creditUseFeignService.findCreditUseAuditList(dto);
    }

    @ApiOperation("录入用信记录")
    @PostMapping("addCreditUseRecord")
    @RequiresPermissions("risk:credit:use:add")
    public Result addRecord(@RequestBody @Valid AddCreditUseRecordDTO dto,BindingResult result) {
        logger.info("录入用信记录:{}",dto.toString());
        if (result.hasErrors()) return Result.failure(result.getFieldError().getDefaultMessage());
        dto.setUserId(MySubjectUtil.getUser().getId());
        dto.setUserName(MySubjectUtil.getUser().getUserName());
        return creditUseFeignService.addCreditUseRecord(dto);
    }

    //商票ocr识别
    @PostMapping("/ocrTicket")
    public Result ocrTicket(@RequestBody SingleParamDTO dto){
        logger.info("subject:{},dto:{}","商票ocr识别",dto.toString());
        CommonResponse<OcrRespBody> respBody = jdzpUtil.ocrRecognize(dto.getValue(),"1");//固定传1 识别正面
        //分析结果
        Result analyzeResult = JdzpRespUtil.analyzeResponseJDZP(respBody);
        logger.info("subject:{},ocr:{}","商票OCR识别结果",JsonUtil.toJSON(respBody));
        if (analyzeResult.getCode() == Result.RESULT_CODE_FAILURE) return analyzeResult;
        OcrRespBody ocrRespBody = respBody.getData();
        Map retMap = new HashMap();
        retMap.put("ticketNo", ocrRespBody.getBillNo());
        retMap.put("ticketMoney",ocrRespBody.getBillAmt());
        retMap.put("ticketStart", ocrRespBody.getIssueDate());
        retMap.put("ticketEnd", ocrRespBody.getMaturityDate());
        retMap.put("acceptorAccount", ocrRespBody.getAccepterAccount());
        retMap.put("acceptorBank", ocrRespBody.getAccepterBankName());
        retMap.put("acceptorCompany", ocrRespBody.getAccepterName());
        retMap.put("acceptorBankNo", ocrRespBody.getAccepterBank());
        retMap.put("ticketGiveCompany", ocrRespBody.getDrawerName());
        retMap.put("ticketGetCompany", ocrRespBody.getPayeeName());
        return Result.success(retMap);
    }

    @ApiOperation("查看用信申请详情")
    @PostMapping("findCreditUseApplyDetails")
    @RequiresPermissions("risk:credit:use:applyDetail")
    public Result findCreditUseApplyDetails(@RequestBody BaseIdDTO dto) {
        logger.info("查看用信申请详情:{}", dto.toString());
        return creditUseFeignService.findCreditUseApplyDetails(dto);
    }

    @ApiOperation("查看用信申请文件资料")
    @PostMapping("document")
    @RequiresPermissions("risk:credit:use:document")
    public Result document(@RequestBody BaseIdDTO dto) {
        logger.info("查看用信申请详情:{}", dto.toString());
        return creditUseFeignService.findCreditUseApplyDocuemnt(dto);
    }

    @ApiOperation("用信申请审核日记")
    @PostMapping("/auditLog")
    @RequiresPermissions("risk:credit:use:audit:log")
    public String findAuditLogList(@RequestBody @Valid AuditLogListDTO dto, BindingResult bindingResult) {
        logger.info("subject:{},AuditLogListDTO:{}","用信申请审核日记",dto.toString());
        if (bindingResult.hasErrors()) return Result.failure(bindingResult.getFieldError().getDefaultMessage()).toJSON();
        return creditApplyService.findAuditLogList(dto);
    }

    @ApiOperation("用信申请操作日志")
    @PostMapping("/operateLog")
    @RequiresPermissions("risk:credit:use:log")
    public String findOperateLogList(@RequestBody @Valid OperateLogDTO dto, BindingResult bindingResult) {
        logger.info("subject:{},findOperateLogList:{}","用信申请操作日志",dto.toString());
        if (bindingResult.hasErrors()) return Result.failure(bindingResult.getFieldError().getDefaultMessage()).toJSON();
        return creditApplyService.findOperateLogList(dto);
    }

    @ApiOperation("我的用信列表")
    @PostMapping("myList")
    @RequiresPermissions("risk:credit:use:myList")
    public String myList(@RequestBody CreditUseMyListWebDTO dto) {
        logger.info("subject:{},CreditUseMyListWebDTO:{}" ,"我的用信列表",dto.toString());
        UserInfoVO user = MySubjectUtil.getUser();
        if (user == null)
            return Result.failure("未获取到登录信息").toJSON();
        dto.setCompanyId(user.getCompanyId());
        dto.setIdCard(user.getIdCard());
        return creditUseFeignService.myList(dto);
    }

    @ApiOperation("所有用信列表")
    @PostMapping("allList")
    @RequiresPermissions("risk:credit:use:allList")
    public String allList(@RequestBody CreditUseAllListWebDTO dto) {
        logger.info("subject:{},CreditUseMyListWebDTO:{}","所有用信列表", dto.toString());
        UserInfoVO user = MySubjectUtil.getUser();
        if (user == null)
            return Result.failure("未获取到登录信息").toJSON();
        dto.setPermissionType(user.getPermissionType());
        dto.setPermissionOrgIds(user.getPermissionOrgIds());
        dto.setCompanyId(user.getCompanyId());
        //dto.setIdCard(user.getIdCard());
        dto.setUserId(user.getId());
        return creditUseFeignService.allList(dto);
    }

    @ApiOperation("修改还款状态")
    @PostMapping("updateRefundStatus")
    @RequiresPermissions("risk:credit:use:updateStatus")
    public String updateRefundStatus(@RequestBody @Valid CreditUseUpdateRefundStatusDTO dto, BindingResult bindingResult) {
        logger.info("subject:{},CreditUseUpdateRefundStatusDTO:{}","修改还款状态", JsonUtil.toSerializerJSON(dto));
        if (bindingResult.hasErrors())
            return Result.failure(bindingResult.getFieldError().getDefaultMessage()).toJSON();

        UserInfoVO user = MySubjectUtil.getUser();
        if (user == null)
            return Result.failure("未获取到登录信息").toJSON();
        dto.setUserId(user.getId());
        return creditUseFeignService.updateRefundStatus(dto);
    }

    @ApiOperation("用信详情")
    @PostMapping("detail")
    @RequiresPermissions("risk:credit:use:detail")
    public String detail(@RequestBody @Valid CreditUseDetailWebDTO dto,BindingResult bindingResult) {
        logger.info("subject:{},CreditUseDetailWebDTO:{}","用信详情", dto.toString());
        if (bindingResult.hasErrors())
            return Result.failure(bindingResult.getFieldError().getDefaultMessage()).toJSON();
        return creditUseFeignService.detail(dto);
    }

    @ApiOperation("商票列表")
    @PostMapping("ticketList")
    @RequiresPermissions("risk:credit:use:ticket:list")
    public String ticketList(@RequestBody CreditTicketListWebDTO dto) {
        logger.info("subject:{},CreditUseMyListWebDTO:{}","商票列表", dto.toString());
        UserInfoVO user = MySubjectUtil.getUser();
        if (user == null)
            return Result.failure("未获取到登录信息").toJSON();
        dto.setCompanyId(user.getCompanyId());
        dto.setPermissionType(user.getPermissionType());
        dto.setPermissionOrgIds(user.getPermissionOrgIds());
        dto.setUserId(user.getId());
        return creditUseFeignService.ticketList(dto);
    }

    @ApiOperation("商票详情")
    @PostMapping("ticketDetail")
    @RequiresPermissions("risk:credit:use:ticket:detail")
    public String ticketDetail(@RequestBody BaseIdDTO dto) {
        logger.info("subject:{},BaseIdDTO:{}","商票详情", dto.toString());
        return creditUseFeignService.ticketDetail(dto);
    }

    @ApiOperation("修改商票状态")
    @PostMapping("updateTicketStatus")
    @RequiresPermissions("risk:credit:use:ticket:updateStatus")
    public Result updateTicketStatus(@RequestBody @Valid BaseSingleUpdateDTO dto, BindingResult bindingResult) {
        logger.info("subject:{},dto:{}","修改商票状态", JsonUtil.toSerializerJSON(dto));

        if (bindingResult.hasErrors()) return Result.failure(bindingResult.getFieldError().getDefaultMessage());
        UserInfoVO user = MySubjectUtil.getUser();
        if (user == null) return Result.failure("未获取到登录信息");
        dto.setUpdateBy(user.getId());

        return creditUseFeignService.updateTicketStatus(dto);
    }

}
