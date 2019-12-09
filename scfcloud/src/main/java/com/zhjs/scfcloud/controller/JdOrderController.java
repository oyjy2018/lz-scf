package com.zhjs.scfcloud.controller;

import com.jd.jr.cbp.sdk.entity.CommonResponse;
import com.jd.jr.cbp.sdk.entity.basic.response.QueryUserCenterUrlRespBody;
import com.zhjs.scfcloud.feign.CompanyFeignService;
import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.util.JdzpRespUtil;
import com.zhjs.scfcloud.model.vo.UserInfoVO;
import com.zhjs.scfcloud.util.MySubjectUtil;
import com.zhjs.scfcloud.util.util.JDZPUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 *  京东订单相关
 * @author weijie.chen
 */

@Api(tags = "京东订单接口")
@RestController
@RequestMapping("/jdOrder/")
public class JdOrderController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private JDZPUtil jdzpUtil;
    @Autowired
    private CompanyFeignService companyFeignService;

    @ApiOperation("平台获取处理异常单URL")
    @GetMapping("queryUserCenterUrl")
    public Result queryUserCenterUrl() {
        logger.info("subject:{}","平台获取处理异常单URL");

        UserInfoVO user = MySubjectUtil.getUser();
        if (user == null) return Result.success("无登录信息");
        //先验证是否已经京东实名验证
        Result result = companyFeignService.isJdVerified(user.getCompanyId());
        if (result == null || result.getCode() != Result.RESULT_CODE_SUCCESS) {
            return Result.success("获取实名信息失败");
        }
        Boolean isJdVerified = (Boolean) result.getData();
        if (!isJdVerified) {
            return Result.success("请先完成企业实名认证");
        }

        //请求京东
        CommonResponse<QueryUserCenterUrlRespBody> commonResponse = jdzpUtil.queryUserCenterUrl(user.getCompanyName(),user.getCompanyId().toString());
        //解析
        result = JdzpRespUtil.analyzeResponseJDZP(commonResponse);
        if (result.getCode() == Result.RESULT_CODE_FAILURE) return result;
        Map retMap = new HashMap();
        retMap.put("url",commonResponse.getData().getUrl());
        return Result.success(retMap);
    }
}
