package com.zhjs.scfcloud.api;

import com.zhjs.scfcloud.feign.ApiFeignService;
;
import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.credit.CreditRecordBalanceQueryDTO;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


/**
 * 对外开放api
 */
@RestController
@RequestMapping("/api/credit/")
public class CreditController {

    private Logger logger = LoggerFactory.getLogger(CreditController.class);

    @Autowired
    private ApiFeignService apiFeignService;

    @ApiOperation("查询项目授信剩余额度")
    @PostMapping("findCreditBalance")
    public Result findCreditBalance(@RequestBody @Valid CreditRecordBalanceQueryDTO dto, BindingResult result){
        if (result.hasFieldErrors()) return Result.failure(result.getFieldError().getDefaultMessage());

        return apiFeignService.findCreditBalance(dto);
    }

}
