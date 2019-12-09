package com.zhjs.scfcloud.app.feign;

import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.credit.CreditRecordMyListQueryDTO;
import com.zhjs.scfcloud.model.vo.credit.PersonalCreditVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 授信申请
 */

@FeignClient(value = "scfcloud-system", contextId = "creditRecord")
public interface CreditRecordFeignService {

    //授信申请保存草稿
    @PostMapping("/creditRecord/myList")
    String myList(@RequestBody CreditRecordMyListQueryDTO creditRecordMyListQueryDTO);

    @ApiOperation("根据身份证查询授信记录")
    @PostMapping("/creditRecord/findPersonalCreditList")
    Result<List<PersonalCreditVO>> findPersonalCreditList(@RequestParam String idCard,@RequestParam Long companyId);

    @ApiOperation("根据授信记录ID查询")
    @PostMapping("/creditRecord/findPersonalCredit")
    Result<PersonalCreditVO> findPersonalCredit(@RequestParam Long creditRecordId);
}
