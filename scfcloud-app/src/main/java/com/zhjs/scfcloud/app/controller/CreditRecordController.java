package com.zhjs.scfcloud.app.controller;

import com.zhjs.scfcloud.app.feign.CreditRecordFeignService;
import com.zhjs.scfcloud.app.util.MySubjectUtil;
import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.credit.CreditRecordMyListQueryDTO;
import com.zhjs.scfcloud.model.entity.User;
import com.zhjs.scfcloud.util.util.JsonUtil;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;

/**
 * @author: dailongting
 * @date:2019/6/12 12:00
 */
@RestController
@RequestMapping("/creditRecord/")
public class CreditRecordController {
    private Logger logger = LoggerFactory.getLogger(CreditRecordController.class);

    @Resource
    private CreditRecordFeignService creditRecordFeignService;


    @ApiOperation("我的授信")
    @PostMapping("myList")
    public String myList(@RequestBody CreditRecordMyListQueryDTO creditRecordMyListQueryDTO){
        logger.info("subject:{},creditApplyFileSaveDTO:{}","我的授信", JsonUtil.toJSON(creditRecordMyListQueryDTO));
        User user = MySubjectUtil.getUser();
        if (user == null) return Result.failure("未获取到登录信息").toJSON();
        creditRecordMyListQueryDTO.setIdCard(user.getIdCard());
        creditRecordMyListQueryDTO.setCompanyId(MySubjectUtil.getCompanyId());
        return creditRecordFeignService.myList(creditRecordMyListQueryDTO);
    }



}
