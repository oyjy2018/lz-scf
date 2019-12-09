package com.zhjs.scfcloud.feign;

import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.CheckPhoneVCodeDTO;
import com.zhjs.scfcloud.model.dto.EmailVCodeDTO;
import com.zhjs.scfcloud.model.dto.NatureSelectListDTO;
import com.zhjs.scfcloud.model.dto.PhoneVaildCodeDTO;
import com.zhjs.scfcloud.model.dto.credit.CreditRecordBalanceQueryDTO;
import com.zhjs.scfcloud.model.vo.DictKeyValueListVO;
import com.zhjs.scfcloud.model.vo.ProductListVO;
import com.zhjs.scfcloud.model.vo.SystemVVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author: dailongting
 * @date:2019/5/24 15:26
 */
@FeignClient(value = "scfcloud-system", contextId = "api")
public interface  ApiFeignService {

    @ApiOperation("选择行类")
    @PostMapping("/api/nature/select")
    Result natureSelectList(@RequestBody NatureSelectListDTO dto);

    @ApiOperation("公司人员规模")
    @PostMapping("/api/staffsize/select")
    Result<List<DictKeyValueListVO>> staffSizeSelectList();

    @ApiOperation("查询系统版本")
    @PostMapping("/api/getSystemVersionList")
    Result<List<SystemVVO>> getSystemVersionList();

    @ApiOperation("查询金融产品下拉列表")
    @PostMapping("/product/select/list")
    Result<List<ProductListVO>> getProductSelectList();

    @ApiOperation("获取手机短信验证码")
    @PostMapping("/smscode/getPhoneVCode")
    Result getPhoneVCode(@RequestBody PhoneVaildCodeDTO dto);

    @ApiOperation("手机验证码校验")
    @PostMapping("/smscode/phoneCheck")
    Result getVCodeCheck(@RequestBody CheckPhoneVCodeDTO dto);

    @ApiOperation("获取邮箱验证码")
    @PostMapping("/smscode/getEmailVCode")
    Result getEmailVCode(@RequestBody EmailVCodeDTO dto);

    @ApiOperation("邮箱验证码校验")
    @PostMapping("/smscode/emailVCodeCheck")
    Result emailVCodeCheck(@RequestBody EmailVCodeDTO dto);

    @ApiOperation("获取银行下拉列表")
    @PostMapping("/bank/select/list")
    Result getBankSelectList();

    @ApiOperation("获取项目授信余额")
    @PostMapping("/creditRecord/findCreditBalance")
    Result findCreditBalance(@RequestBody CreditRecordBalanceQueryDTO dto);
}
