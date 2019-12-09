package com.zhjs.scfcloud.app.controller;

import com.zhjs.scfcloud.app.feign.CreditUseFeignService;
import com.zhjs.scfcloud.app.util.MySubjectUtil;
import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.BaseIdDTO;
import com.zhjs.scfcloud.model.dto.credit.CreditUseMyListAppDTO;
import com.zhjs.scfcloud.model.entity.User;
import com.zhjs.scfcloud.model.vo.app.UserInfoVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用信管理 控制器
 * <功能详细描述>
 * Version: 1.0.0, 2019-06-27 12:00
 *
 * @author liuchanghai
 * @create 2019-06-27 12:00
 * @since
 */

@Api(tags = "用信管理")
@RestController
@RequestMapping("/creditUse/")
public class CreditUseController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CreditUseFeignService creditUseFeignService;

    @ApiOperation("查看开票申请详情")
    @PostMapping("findCreditUseApplyDetails")
    public Result findCreditUseApplyDetails(@RequestBody BaseIdDTO dto) {
        logger.info("查看开票申请详情{}" + dto.toString());
        return creditUseFeignService.findCreditUseApplyDetails(dto);
    }

    @ApiOperation("我的用信")
    @PostMapping("myList")
    public String myList(@RequestBody CreditUseMyListAppDTO creditUseMyListAppDTO) {
        logger.info("subject:{}","我的用信");
        User user = MySubjectUtil.getUser();
        if (user == null)
            return Result.failure("登录信息为空").toJSON();
        creditUseMyListAppDTO.setCompanyId(MySubjectUtil.getCompanyId());
        creditUseMyListAppDTO.setIdCard(user.getIdCard());
        return creditUseFeignService.myList(creditUseMyListAppDTO);
    }
}
