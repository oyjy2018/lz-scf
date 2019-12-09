package com.zhjs.scfcloud.feign;

import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.lzProject.FindProjectAssetDTO;
import com.zhjs.scfcloud.model.dto.lzProject.LzProjectBoardReportDTO;
import com.zhjs.scfcloud.model.dto.lzProject.LzProjectListDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author: dailongting
 * @date:2019/5/21 16:24
 */
@FeignClient(value = "scfcloud-system", contextId = "lz/project")
public interface LzProjectFeignService {

    @PostMapping("/lz/project/list")
    Result list(@RequestBody LzProjectListDTO dto);

    @GetMapping("/lz/project/basicInfo")
    Result basicInfo(@RequestParam("contractNo") String contractNo);

    @GetMapping("/lz/project/itemInfo")
    Result itemInfo(@RequestParam("contractNo") String contractNo);

    @GetMapping("/lz/project/contract")
    Result contractInfo(@RequestParam("contractNo") String contractNo);

    @GetMapping("/lz/project/payList")
    Result payList(@RequestParam("contractNo") String contractNo);

    /**
     * 查询项目收款数据
     * @param contractNo
     * @return
     */
    @GetMapping("/lz/project/gatheringList")
    Result gatheringList(@RequestParam("contractNo") String contractNo);

    /**
     * 查询项目成本费用明细（经营利润分析B）
     * @param contractNo
     * @return
     */
    @GetMapping("/lz/project/findCostDetails")
    Result findCostDetails(@RequestParam("contractNo") String contractNo);

    /**
     * 查询采购物流信息
     * @param contractNo
     * @return
     */
    @GetMapping("/lz/project/purchaseLogistics")
    Result purchaseLogistics(@RequestParam("contractNo") String contractNo);

    /**
     * 查询项目资产数据
     * @param dto
     * @return
     */
    @PostMapping("/lz/project/findProjectAsset")
    Result findProjectAsset(FindProjectAssetDTO dto);

    /**
     * 借款信息
     * @param contractNo
     * @return
     */
    @GetMapping("/lz/project/loanList")
    Result loanList(@RequestParam("contractNo") String contractNo,@RequestParam("isPersonal") String isPersonal);

    /**
     * 还款信息
     * @param loanNo
     * @return
     */
    @GetMapping("/lz/project/refundList")
    Result refundList(@RequestParam("loanNo") String loanNo);

    /**
     * 项目文件
     * @param contractNo
     * @return
     */
    @GetMapping("/lz/project/fileList")
    Result fileList(@RequestParam("contractNo") String contractNo);

    /**
     * 查询项目负债、利润
     * @param dto
     * @return
     */
    @PostMapping("/lz/project/findLiabilities")
    Result findLiabilities(@RequestBody FindProjectAssetDTO dto);

    /**
     * 项目看板卡片数据
     * @param contractNo
     * @return
     */
    @GetMapping("/lz/project/board/card")
    Result boardCard(@RequestParam("contractNo") String contractNo);

    /**
     * 项目看板报表数据
     * @param dto
     * @return
     */
    @PostMapping("/lz/project/board/report")
    Result boardReport(@RequestBody LzProjectBoardReportDTO dto);

    /**
     * 查询押金缴纳记录
     * @param contractNo
     * @return
     */
    @GetMapping("/lz/project/getPledgeCashPayList")
    Result getPledgeCashPayList(@RequestParam("contractNo") String contractNo);

    /**
     * 查询押金提取记录
     * @param contractNo
     * @return
     */
    @GetMapping("/lz/project/getPledgeCashExtractList")
    Result getPledgeCashExtractList(@RequestParam("contractNo") String contractNo);

    /**
     * 保险信息
     * @param contractNo
     * @return
     */
    @GetMapping("/lz/project/insuranceList")
    Result insuranceList(@RequestParam("contractNo") String contractNo);

    /**
     * 保险信息
     * @param contractNo
     * @return
     */
    @GetMapping("/lz/project/invoiceList")
    Result invoiceList(@RequestParam("contractNo") String contractNo);

    /**
     * 保证金记录
     * @param contractNo
     * @return
     */
    @GetMapping("/lz/project/findMarginsList")
    Result findMarginsList(@RequestParam("contractNo") String contractNo);
}
