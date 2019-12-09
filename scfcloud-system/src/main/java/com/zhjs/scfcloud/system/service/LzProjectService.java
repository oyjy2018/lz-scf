package com.zhjs.scfcloud.system.service;

import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.lzProject.FindProjectAssetDTO;
import com.zhjs.scfcloud.model.dto.lzProject.LzProjectBoardReportDTO;
import com.zhjs.scfcloud.model.dto.lzProject.LzProjectListDTO;

/**
 * 深华项目基本信息
 */

public interface LzProjectService  {

    Result list(LzProjectListDTO dto);

    Result basicInfo(String contractNo);

    Result itemInfo(String contractNo);

    /**
     * 查询项目合同信息
     * @param contractNo
     * @return
     */
    Result contractInfo(String contractNo);

    Result payList(String contractNo);

    /**
     * 查询项目收款数据
     * @param contractNo
     * @return
     */
    Result gatheringList(String contractNo);

    /**
     * 查询项目成本费用明细
     * @param contractNo
     * @return
     */
    Result findCostDetails(String contractNo);

    /**
     * 查询采购物流信息
     * @param contractNo
     * @return
     */
    Result purchaseLogistics(String contractNo);

    /**
     * 查询项目资产数据
     * @param dto
     * @return
     */
    Result findProjectAsset(FindProjectAssetDTO dto);

    /**
     * 借款信息
     * @param contractNo
     * @param isPersonal
     * @return
     */
    Result loanList(String contractNo, String isPersonal);

    /**
     * 还款信息
     * @param loanNo
     * @return
     */
    Result refundList(String loanNo);

    /**
     * 项目文件
     * @param contractNo
     * @return
     */
    Result fileList(String contractNo);

    /**
     * 查询项目负债、利润
     * @param dto
     * @return
     */
    Result findLiabilities(FindProjectAssetDTO dto);

    /**
     * 项目看板卡片数据
     * @param contractNo
     * @return
     */
    Result boardCard(String contractNo);

    /**
     * 项目看板报表数据
     * @param dto
     * @return
     */
    Result boardReport(LzProjectBoardReportDTO dto);

    void generateData();

    /**
     * 查询押金缴纳记录
     * @param contractNo
     * @return
     */
    Result getPledgeCashPayList(String contractNo);

    /**
     * 查询押金提取记录
     * @param contractNo
     * @return
     */
    Result getPledgeCashExtractList(String contractNo);

    /**
     * 保险信息
     * @param contractNo
     * @return
     */
    Result insuranceList(String contractNo);

    /**
     * 发票信息
     * @param contractNo
     * @return
     */
    Result invoiceList(String contractNo);

    /**
     * 保证金记录
     * @param contractNo
     * @return
     */
    Result findMarginsList(String contractNo);
}
