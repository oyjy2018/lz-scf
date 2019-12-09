package com.zhjs.scfcloud.feign;

import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.BaseDeleteDTO;
import com.zhjs.scfcloud.model.dto.credit.CreditRecordAllListQueryDTO;
import com.zhjs.scfcloud.model.dto.credit.CreditRecordImportDTO;
import com.zhjs.scfcloud.model.dto.credit.CreditRecordMyListQueryDTO;
import com.zhjs.scfcloud.model.vo.credit.PersonalCreditVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 授信
 */

@FeignClient(value = "scfcloud-system", contextId = "creditRecord")
public interface CreditRecordFeignService {

    //我的授信
    @PostMapping("/creditRecord/myList")
    String myList(@RequestBody CreditRecordMyListQueryDTO creditRecordMyListQueryDTO);

    //所有授信
    @PostMapping("/creditRecord/allList")
    String allList(@RequestBody CreditRecordAllListQueryDTO creditRecordAllListQueryDTO);

    @ApiOperation("根据身份证查询授信记录")
    @PostMapping("/creditRecord/findPersonalCreditList")
    Result<List<PersonalCreditVO>> findPersonalCreditList(@RequestParam String idCard,@RequestParam Long companyId);

    @ApiOperation("根据授信记录ID查询")
    @PostMapping("/creditRecord/findPersonalCredit")
    Result<PersonalCreditVO> findPersonalCredit(@RequestParam Long creditRecordId);

    @ApiOperation("导入授信校验")
    @PostMapping("/creditRecord/importRecord")
    Result importRecord(@RequestBody CreditRecordImportDTO dto);

    @ApiOperation("删除授信")
    @PostMapping("/creditRecord/deleteRecord")
    Result deleteRecord(@RequestBody BaseDeleteDTO dto);

    @ApiOperation("查询授余额")
    @PostMapping("/creditRecord/findBalance")
    Result findBalance(@RequestParam("id") Long id);
}
