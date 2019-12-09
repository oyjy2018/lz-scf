package com.zhjs.scfcloud.api;

import com.zhjs.scfcloud.feign.ApiFeignService;
import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.CheckPhoneVCodeDTO;
import com.zhjs.scfcloud.model.dto.EmailVCodeDTO;
import com.zhjs.scfcloud.model.dto.NatureSelectListDTO;
import com.zhjs.scfcloud.model.dto.PhoneVaildCodeDTO;
import com.zhjs.scfcloud.model.dto.business.QueryBusinessAttrDTO;
import com.zhjs.scfcloud.util.ShiroSessionUtil;
import com.zhjs.scfcloud.util.constant.RedisConstant;
import com.zhjs.scfcloud.util.util.EmailUtil;
import com.zhjs.scfcloud.util.util.RedisUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

/**
 * @author: dailongting
 * @date:2019/5/24 14:02
 */
@Api(tags = "Api接口")
@RestController
@RequestMapping("api")
public class ApiController {

    private Logger logger = LoggerFactory.getLogger(ApiController.class);

    @Autowired
    private ApiFeignService apiFeignService;
    @Autowired
    private EmailUtil emailUtil;
    @Autowired
    private RedisUtil redisUtil;

    @ApiOperation("选择行类")
    @PostMapping("nature/select")
    public String natureSelectList(@RequestBody NatureSelectListDTO dto){
        return apiFeignService.natureSelectList(dto).toJSON();
    }

    @ApiOperation("公司人员规模")
    @PostMapping("staffsize/select")
    public String staffSizeSelectList(){
        return apiFeignService.staffSizeSelectList().toJSON();
    }

    @ApiOperation("查询系统版本")
    @PostMapping("getSystemVersionList")
    public String getSystemVersionList() {
        return apiFeignService.getSystemVersionList().toJSON();
    }

    @ApiOperation("查询金融产品下拉列表")
    @PostMapping("product/select/list")
    public String getProductList() {
        return apiFeignService.getProductSelectList().toJSON();
    }

    @ApiOperation("获取手机短信验证码")
    @PostMapping("vcode/phone/get")
    public String getPhoneVCode(@RequestBody @Valid PhoneVaildCodeDTO dto, BindingResult result) {
        if (result.hasErrors()) {
            return Result.failure(result.getFieldError().getDefaultMessage()).toJSON();
        }
        return apiFeignService.getPhoneVCode(dto).toJSON();
    }

    @ApiOperation("手机验证码校验")
    @PostMapping("vcode/phone/check")
    public String getVCodeCheck(@RequestBody CheckPhoneVCodeDTO dto) {
        return apiFeignService.getVCodeCheck(dto).toJSON();
    }

    @ApiOperation("获取邮箱验证码")
    @PostMapping("getEmailVCode")
    public Result getEmailVCode(@RequestBody EmailVCodeDTO dto) {
        return apiFeignService.getEmailVCode(dto);
    }

    @ApiOperation("邮箱验证码校验")
    @PostMapping("emailVCodeCheck")
    public Result emailVCodeCheck(@RequestBody EmailVCodeDTO dto) {
        return apiFeignService.emailVCodeCheck(dto);
    }

    @ApiOperation("获取银行下拉列表")
    @PostMapping("bank/select/list")
    public String getBankSelectList() {
        return apiFeignService.getBankSelectList().toJSON();
    }

    @GetMapping("testEmail")
    public String testEmail(@RequestBody QueryBusinessAttrDTO dto){
        long a = System.currentTimeMillis();
        System.out.println("----------查询redis开始时间："+a);
//        redisUtil.hget(RedisConstant.REDIS_KEY_BUSINESS_ATTR_VAL,"1");
        ShiroSessionUtil.testAAA();
        System.out.println("----------查询redis结束用时："+(System.currentTimeMillis()-a));
        return Result.success().toJSON();
    }

    @PostMapping("/biz")
    public String saveBusinessDraft(@RequestBody Map map, BindingResult bindingResult){
        if (bindingResult.hasErrors())
            return Result.failure(bindingResult.getFieldError().getDefaultMessage()).toJSON();
        return Result.success(map).toJSON();
        //做业务字段校验
        // return businessFeignService.saveBusinessDraft(dto);
    }
}
