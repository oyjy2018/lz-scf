package com.zhjs.scfcloud.system.controller;

import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.BaseDeleteDTO;
import com.zhjs.scfcloud.model.dto.credit.*;
import com.zhjs.scfcloud.model.vo.credit.PersonalCreditVO;
import com.zhjs.scfcloud.system.service.CreditRecordService;
import com.zhjs.scfcloud.util.util.JsonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 授信申请相关
 */
@Api(tags = "授信管理")
@RestController
@RequestMapping("/creditRecord/")
public class CreditRecordController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private CreditRecordService creditRecordService;


    //我的授信列表
    @PostMapping("myList")
    public String myList(@RequestBody CreditRecordMyListQueryDTO creditRecordMyListQueryDTO) {
        logger.info("subject:{},creditRecordMyListQueryDTO:{}","我的授信", JsonUtil.toJSON(creditRecordMyListQueryDTO));
        return creditRecordService.myList(creditRecordMyListQueryDTO);
    }

    //所有授信
    @PostMapping("/allList")
    public String allList(@RequestBody CreditRecordAllListQueryDTO creditRecordAllListQueryDTO) {
        logger.info("subject:{},creditRecordAllListQueryDTO:{}","所有授信", JsonUtil.toJSON(creditRecordAllListQueryDTO));
        return creditRecordService.allList(creditRecordAllListQueryDTO);
    }

    @ApiOperation("根据身份证查询授信记录")
    @PostMapping("/findPersonalCreditList")
    public Result<List<PersonalCreditVO>> findPersonalCreditList(@RequestParam String idCard,@RequestParam Long companyId){
        logger.info("根据身份证查询授信记录：{}",idCard);
        List<PersonalCreditVO> list = creditRecordService.findPersonalCreditList(idCard,companyId);
        //如果查不到匹配的记录，返回空集合
        if(list == null) {
            list = new ArrayList<>();
        }
        return Result.success(list);
    }

    @ApiOperation("根据授信记录ID查询")
    @PostMapping("/findPersonalCredit")
    public Result<PersonalCreditVO> findPersonalCredit(@RequestParam Long creditRecordId){
        logger.info("根据授信记录ID查询：{}",creditRecordId);
        PersonalCreditVO vo = creditRecordService.findPersonalCredit(creditRecordId);
        if(vo == null) {
            return Result.failure("授信记录数据丢失");
        }
        return Result.success(vo);
    }

    @ApiOperation("导入授信")
    @PostMapping("/importRecord")
    public Result importRecord(@RequestBody CreditRecordImportDTO dto) {
        logger.info("subject:{},dto:{}","导入授信", JsonUtil.toJSON(dto));
        return creditRecordService.importRecord(dto);
    }

    @ApiOperation("删除授信")
    @PostMapping("/deleteRecord")
    public Result deleteRecord(@RequestBody BaseDeleteDTO dto){
        logger.info("subject:{},dto:{}","删除授信", JsonUtil.toJSON(dto));
        return creditRecordService.deleteRecord(dto);
    }

    @ApiOperation("获取项目授信余额-外部")
    @PostMapping("/findCreditBalance")
    public Result findCreditBalance(@RequestBody CreditRecordBalanceQueryDTO dto) {
        logger.info("subject:{},dto:{}","获取项目授信余额", JsonUtil.toJSON(dto));
        return creditRecordService.findCreditBalance(dto);
    }

    @ApiOperation("查询授余额-内部")
    @PostMapping("/findBalance")
    public Result findBalance(@RequestParam("id") Long id){
        logger.info("subject:{},id:{}","获取项目授信余额", id);
        return creditRecordService.findBalance(id);
    }
}

