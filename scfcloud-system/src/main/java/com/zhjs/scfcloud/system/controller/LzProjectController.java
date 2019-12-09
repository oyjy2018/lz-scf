package com.zhjs.scfcloud.system.controller;

import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.lzProject.FindProjectAssetDTO;
import com.zhjs.scfcloud.model.dto.lzProject.LzProjectBoardReportDTO;
import com.zhjs.scfcloud.model.dto.lzProject.LzProjectListDTO;
import com.zhjs.scfcloud.system.service.LzProjectService;
import com.zhjs.scfcloud.util.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 深华项目相关
 */
@RestController
@RequestMapping("/lz/project/")
public class LzProjectController {
    private Logger logger = LoggerFactory.getLogger(LzProjectController.class);

    @Autowired
    private LzProjectService lzProjectService;

    //获取项目列表
    @PostMapping("list")
    public Result list(@RequestBody LzProjectListDTO dto) {
        logger.info("subject:{},dto:{}", "获取项目列表", JsonUtil.toSerializerJSON(dto));
        return lzProjectService.list(dto);
    }

    //获取项目基本信息
    @GetMapping("basicInfo")
    public Result basicInfo(@RequestParam("contractNo") String contractNo) {
        logger.info("subject:{},contractNo:{}", "获取项目基本信息", contractNo);
        return lzProjectService.basicInfo(contractNo);
    }

    //获取项目信息
    @GetMapping("itemInfo")
    public Result itemInfo(@RequestParam("contractNo") String contractNo) {
        logger.info("subject:{},contractNo:{}", "获取项目信息", contractNo);
        return lzProjectService.itemInfo(contractNo);
    }

    @GetMapping("contract")
    public Result contractInfo(@RequestParam("contractNo") String contractNo){
        logger.info("subject:{},contractNo:{}", "获取合同信息", contractNo);
        return lzProjectService.contractInfo(contractNo);
    }

    //获取项目付款信息
    @GetMapping("payList")
    public Result payList(@RequestParam("contractNo") String contractNo) {
        logger.info("subject:{},contractNo:{}", "获取项目付款信息", contractNo);
        return lzProjectService.payList(contractNo);
    }

    /**
     * 查询项目收款数据
     * @param contractNo
     * @return
     */
    @GetMapping("gatheringList")
    public Result gatheringList(@RequestParam("contractNo") String contractNo) {
        logger.info("subject:{},contractNo:{}", "查询项目收款数据", contractNo);
        return lzProjectService.gatheringList(contractNo);
    }

    /**
     * 查询项目成本费用明细（经营利润分析B）
     * @param contractNo
     * @return
     */
    @GetMapping("findCostDetails")
    public Result findCostDetails(@RequestParam("contractNo") String contractNo) {
        logger.info("subject:{},contractNo:{}", "查询项目成本费用明细（经营利润分析B）", contractNo);
        return lzProjectService.findCostDetails(contractNo);
    }

    /**
     * 查询采购物流信息
     * @param contractNo
     * @return
     */
    @GetMapping("purchaseLogistics")
    public Result purchaseLogistics(@RequestParam("contractNo") String contractNo) {
        logger.info("subject:{},contractNo:{}", "查询采购物流信息", contractNo);
        return lzProjectService.purchaseLogistics(contractNo);
    }

    /**
     * 查询项目资产数据
     * @param dto
     * @return
     */
    @PostMapping("findProjectAsset")
    public Result findProjectAsset(@RequestBody FindProjectAssetDTO dto) {
        logger.info("subject:{},dto:{}", "查询项目资产数据", dto);
        return lzProjectService.findProjectAsset(dto);
    }

    /**
     * 借款信息
     * @param contractNo
     * @return
     */
    @GetMapping("loanList")
    public Result loanList(@RequestParam("contractNo") String contractNo,@RequestParam("isPersonal") String isPersonal) {
        logger.info("subject:{},contractNo:{},isPersonal：{}", "借款信息", contractNo,isPersonal);
        return lzProjectService.loanList(contractNo,isPersonal);
    }

    /**
     * 还款信息
     * @param loanNo
     * @return
     */
    @GetMapping("refundList")
    public Result refundList(@RequestParam("loanNo") String loanNo) {
        logger.info("subject:{},loanNo:{}", "还款信息", loanNo);
        return lzProjectService.refundList(loanNo);
    }

    /**
     * 项目文件
     * @param contractNo
     * @return
     */
    @GetMapping("fileList")
    public Result fileList(@RequestParam("contractNo") String contractNo) {
        logger.info("subject:{},contractNo:{}", "项目文件", contractNo);
        return lzProjectService.fileList(contractNo);
    }

    /**
     * 查询项目负债、利润
     * @param dto
     * @return
     */
    @PostMapping("findLiabilities")
    public Result findLiabilities(@RequestBody FindProjectAssetDTO dto) {
        logger.info("subject:{},dto:{}", "查询项目负债、利润", dto);
        return lzProjectService.findLiabilities(dto);
    }

    /**
     * 项目看板卡片数据
     * @param contractNo
     * @return
     */
    @GetMapping("board/card")
    public Result boardCard(@RequestParam("contractNo") String contractNo) {
        logger.info("subject:{},contractNo:{}", "项目看板卡片数据", contractNo);
        return lzProjectService.boardCard(contractNo);
    }

    /**
     * 项目看板报表数据
     * @param dto
     * @return
     */
    @PostMapping("board/report")
    public Result boardReport(@RequestBody LzProjectBoardReportDTO dto) {
        logger.info("subject:{},dto:{}", "项目看板报表数据", JsonUtil.toSerializerJSON(dto));
        return lzProjectService.boardReport(dto);
    }

    /**
     * 查询押金缴纳记录
     * @param contractNo
     * @return
     */
    @GetMapping("getPledgeCashPayList")
    public Result getPledgeCashPayList(@RequestParam("contractNo") String contractNo) {
        logger.info("subject:{},contractNo:{}", "查询押金缴纳记录", contractNo);
        return lzProjectService.getPledgeCashPayList(contractNo);
    }

    /**
     * 查询押金提取记录
     * @param contractNo
     * @return
     */
    @GetMapping("getPledgeCashExtractList")
    public Result getPledgeCashExtractList(@RequestParam("contractNo") String contractNo) {
        logger.info("subject:{},contractNo:{}", "查询押金提取记录", contractNo);
        return lzProjectService.getPledgeCashExtractList(contractNo);
    }

    /**
     * 保险信息
     * @param contractNo
     * @return
     */
    @GetMapping("insuranceList")
    public Result insuranceList(@RequestParam("contractNo") String contractNo) {
        logger.info("subject:{},contractNo：{}", "保险信息", contractNo);
        return lzProjectService.insuranceList(contractNo);
    }

    /**
     * 发票信息
     * @param contractNo
     * @return
     */
    @GetMapping("invoiceList")
    public Result invoiceList(@RequestParam("contractNo") String contractNo) {
        logger.info("subject:{},contractNo：{}", "发票信息", contractNo);
        return lzProjectService.invoiceList(contractNo);
    }

    /**
     * 保证金记录
     * @param contractNo
     * @return
     */
    @GetMapping("findMarginsList")
    public Result findMarginsList(@RequestParam("contractNo") String contractNo) {
        logger.info("subject:{},contractNo：{}", "保证金记录", contractNo);
        return lzProjectService.findMarginsList(contractNo);
    }

}
