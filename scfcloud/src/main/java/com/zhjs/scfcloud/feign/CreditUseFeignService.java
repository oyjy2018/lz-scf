package com.zhjs.scfcloud.feign;

import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.BaseIdDTO;
import com.zhjs.scfcloud.model.dto.BaseSingleUpdateDTO;
import com.zhjs.scfcloud.model.dto.credit.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * Version: 1.0.0, 2019-06-27 11:26
 *
 * @author liuchanghai
 * @create 2019-06-27 11:26
 * @since
 */

@FeignClient(value = "scfcloud-system", contextId = "creditUse")
public interface CreditUseFeignService {

    @ApiOperation("我的用信申请列表")
    @PostMapping("/creditUse/findCreditUseApplyMyList")
    Result findCreditUseApplyMyList(@RequestBody CreditUseApplyMyListQueryDTO dto);

    @ApiOperation("所有用信申请列表")
    @PostMapping("/creditUse/findCreditUseApplyAllList")
    String findCreditUseApplyAllList(@RequestBody CreditUseApplyAllListQueryDTO dto);

    @ApiOperation("用信审批列表")
    @PostMapping("/creditUse/findCreditUseAuditList")
    Result findCreditUseAuditList(@RequestBody CreditUseAuditListQueryDTO dto);

    @ApiOperation("录入用信记录")
    @PostMapping("/creditUse/addCreditUseRecord")
    Result addCreditUseRecord(@RequestBody AddCreditUseRecordDTO dto);

    @ApiOperation("查看用信申请详情")
    @PostMapping("/creditUse/findCreditUseApplyDetails")
    Result findCreditUseApplyDetails(@RequestBody BaseIdDTO dto);

    @ApiOperation("申请文件资料")
    @PostMapping("/creditUse/document")
    Result findCreditUseApplyDocuemnt(@RequestBody BaseIdDTO dto);

    @PostMapping("/creditUse/myWebList")
    String myList(@RequestBody CreditUseMyListWebDTO dto);

    @PostMapping("/creditUse/allWebList")
    String allList(@RequestBody CreditUseAllListWebDTO dto);

    @PostMapping("/creditUse/updateRefundStatus")
    String updateRefundStatus(@RequestBody CreditUseUpdateRefundStatusDTO dto);

    @PostMapping("/creditUse/webDetail")
    String detail(@RequestBody CreditUseDetailWebDTO dto);

    @PostMapping("/creditUse/ticketList")
    String ticketList(@RequestBody CreditTicketListWebDTO dto);

    @PostMapping("/creditUse/ticketDetail")
    String ticketDetail(@RequestBody BaseIdDTO dto);

    @PostMapping("/creditUse/updateTicketStatus")
    Result updateTicketStatus(@RequestBody BaseSingleUpdateDTO dto);
}
