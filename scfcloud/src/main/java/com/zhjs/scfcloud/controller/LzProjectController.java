package com.zhjs.scfcloud.controller;

import com.zhjs.scfcloud.feign.LzProjectFeignService;
import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.lzProject.FindProjectAssetDTO;
import com.zhjs.scfcloud.model.dto.lzProject.LzProjectBoardReportDTO;
import com.zhjs.scfcloud.model.dto.lzProject.LzProjectListDTO;
import com.zhjs.scfcloud.util.util.JsonUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 深华项目相关
 */
@RestController
@RequestMapping("/lz/project/")
public class LzProjectController {
    private Logger logger = LoggerFactory.getLogger(LzProjectController.class);

    @Autowired
    private LzProjectFeignService lzProjectFeignService;

    //获取项目列表
    @PostMapping("list")
    @RequiresPermissions("common:lzProject:list")
    public Result list(@RequestBody LzProjectListDTO dto) {
        logger.info("subject:{},dto:{}", "获取项目列表", JsonUtil.toSerializerJSON(dto));
        return lzProjectFeignService.list(dto);
    }

    //获取项目基本信息
    @GetMapping("basicInfo")
    @RequiresPermissions("common:lzProject:basicInfo")
    public Result BasicInfo(@RequestParam("contractNo") String contractNo) {
        logger.info("subject:{},contractNo:{}", "获取项目基本信息", contractNo);
        return lzProjectFeignService.basicInfo(contractNo);
    }

    //获取项目信息
    @GetMapping("itemInfo")
    @RequiresPermissions("common:lzProject:itemInfo")
    public Result itemInfo(@RequestParam("contractNo") String contractNo) {
        logger.info("subject:{},contractNo:{}", "获取项目信息", contractNo);
        return lzProjectFeignService.itemInfo(contractNo);
    }

    //获取合同信息
    @GetMapping("contract")
    @RequiresPermissions("common:lzProject:contract")
    public Result contractInfo(@RequestParam("contractNo") String contractNo) {
        logger.info("subject:{},contractNo:{}", "获取合同信息", contractNo);
        return lzProjectFeignService.contractInfo(contractNo);
    }

    //获取项目付款信息
    @GetMapping("payList")
    @RequiresPermissions("common:lzProject:payList")
    public Result payList(@RequestParam("contractNo") String contractNo) {
        logger.info("subject:{},contractNo:{}", "获取项目付款信息", contractNo);
        return lzProjectFeignService.payList(contractNo);
    }

    /**
     * 查询项目收款数据
     * @param contractNo
     * @return
     */
    @GetMapping("gatheringList")
    @RequiresPermissions("common:lzProject:gatheringList")
    public Result gatheringList(@RequestParam("contractNo") String contractNo) {
        logger.info("subject:{},contractNo:{}", "查询项目收款数据", contractNo);
        return lzProjectFeignService.gatheringList(contractNo);
    }

    /**
     * 查询项目成本费用明细（经营利润分析B）
     * @param contractNo
     * @return
     */
    @GetMapping("findCostDetails")
    @RequiresPermissions("common:lzProject:analysisB")
    public Result findCostDetails(@RequestParam("contractNo") String contractNo) {
        logger.info("subject:{},contractNo:{}", "查询项目成本费用明细（经营利润分析B）", contractNo);
        return lzProjectFeignService.findCostDetails(contractNo);
    }

    /**
     * 查询采购物流信息
     * @param contractNo
     * @return
     */
    @GetMapping("purchaseLogistics")
    @RequiresPermissions("common:lzProject:purchaseLogistics")
    public Result purchaseLogistics(@RequestParam("contractNo") String contractNo) {
        logger.info("subject:{},contractNo:{}", "查询采购物流信息", contractNo);
        return lzProjectFeignService.purchaseLogistics(contractNo);
    }

    /**
     * 查询项目资产数据
     * @param dto
     * @return
     */
    @PostMapping("findProjectAsset")
    @RequiresPermissions("common:lzProject:analysisA")
    public Result findProjectAsset(@Valid @RequestBody FindProjectAssetDTO dto, BindingResult result) {
        logger.info("subject:{},dto:{}", "查询项目资产数据", dto);
        if (result.hasFieldErrors()) {
            return Result.failure(result.getFieldError().getDefaultMessage());
        }
        return lzProjectFeignService.findProjectAsset(dto);
    }

    /**
     * 借款信息
     * @param contractNo
     * @param isPersonal
     * @return
     */
    @GetMapping("loanList")
    @RequiresPermissions("common:lzProject:loanList")
    public Result loanList(@RequestParam("contractNo") String contractNo,@RequestParam("isPersonal") String isPersonal) {
        logger.info("subject:{},contractNo:{},isPersonal：{}", "借款信息", contractNo,isPersonal);
        return lzProjectFeignService.loanList(contractNo,isPersonal);
    }

    /**
     * 还款信息
     * @param loanNo
     * @return
     */
    @GetMapping("refundList")
    @RequiresPermissions("common:lzProject:refundList")
    public Result refundList(@RequestParam("loanNo") String loanNo) {
        logger.info("subject:{},loanNo:{}", "还款信息", loanNo);
        return lzProjectFeignService.refundList(loanNo);
    }

    /**
     * 项目文件
     * @param contractNo
     * @return
     */
    @GetMapping("fileList")
    @RequiresPermissions("common:lzProject:fileList")
    public Result fileList(@RequestParam("contractNo") String contractNo) {
        logger.info("subject:{},contractNo:{}", "项目文件", contractNo);
        return lzProjectFeignService.fileList(contractNo);
    }

    /**
     * 查询项目负债、利润
     * @param dto
     * @return
     */
    @PostMapping("findLiabilities")
    @RequiresPermissions("common:lzProject:analysisA")
    public Result findLiabilities(@Valid @RequestBody FindProjectAssetDTO dto, BindingResult result) {
        logger.info("subject:{},dto:{}", "查询项目负债、利润", dto);
        if (result.hasFieldErrors()) {
            return Result.failure(result.getFieldError().getDefaultMessage());
        }
        return lzProjectFeignService.findLiabilities(dto);
    }

    /**
     * 项目看板卡片数据
     * @param contractNo
     * @return
     */
    @GetMapping("board/card")
    @RequiresPermissions("common:lzProject:board")
    public Result boardCard(@RequestParam("contractNo") String contractNo) {
        logger.info("subject:{},contractNo:{}", "项目看板卡片数据", contractNo);
        return lzProjectFeignService.boardCard(contractNo);
    }

    /**
     * 项目看板报表数据
     * @param dto
     * @return
     */
    @PostMapping("board/report")
    @RequiresPermissions("common:lzProject:board")
    public Result boardReport(@Valid @RequestBody LzProjectBoardReportDTO dto, BindingResult result) {
        logger.info("subject:{},dto:{}", "项目看板报表数据", JsonUtil.toSerializerJSON(dto));
        if (result.hasFieldErrors()) {
            return Result.failure(result.getFieldError().getDefaultMessage());
        }
        return lzProjectFeignService.boardReport(dto);
    }

    /**
     * 查询押金缴纳记录
     * @param contractNo
     * @return
     */
    @GetMapping("getPledgeCashPayList")
    @RequiresPermissions("common:lzProject:getPledgeCash")
    public Result getPledgeCashPayList(@RequestParam("contractNo") String contractNo) {
        logger.info("subject:{},contractNo:{}", "查询押金缴纳记录", contractNo);
        return lzProjectFeignService.getPledgeCashPayList(contractNo);
    }

    /**
     * 查询押金提取记录
     * @param contractNo
     * @return
     */
    @GetMapping("getPledgeCashExtractList")
    @RequiresPermissions("common:lzProject:getPledgeCash")
    public Result getPledgeCashExtractList(@RequestParam("contractNo") String contractNo) {
        logger.info("subject:{},contractNo:{}", "查询押金提取记录", contractNo);
        return lzProjectFeignService.getPledgeCashExtractList(contractNo);
    }

    /**
     * 保险信息
     * @param contractNo
     * @return
     */
    @GetMapping("insuranceList")
    @RequiresPermissions("common:lzProject:insuranceList")
    public Result insuranceList(@RequestParam("contractNo") String contractNo) {
        logger.info("subject:{},contractNo：{}", "保险信息", contractNo);
        return lzProjectFeignService.insuranceList(contractNo);
    }

    /**
     * 发票信息
     * @param contractNo
     * @return
     */
    @GetMapping("invoiceList")
    @RequiresPermissions("common:lzProject:invoiceList")
    public Result invoiceList(@RequestParam("contractNo") String contractNo) {
        logger.info("subject:{},contractNo：{}", "发票信息", contractNo);
        return lzProjectFeignService.invoiceList(contractNo);
    }

    /**
     * 保证金记录
     * @param contractNo
     * @return
     */
    @GetMapping("findMarginsList")
    @RequiresPermissions("common:lzProject:margins")
    public Result findMarginsList(@RequestParam("contractNo") String contractNo) {
        logger.info("subject:{},contractNo：{}", "保证金记录", contractNo);
        return lzProjectFeignService.findMarginsList(contractNo);
    }
}
