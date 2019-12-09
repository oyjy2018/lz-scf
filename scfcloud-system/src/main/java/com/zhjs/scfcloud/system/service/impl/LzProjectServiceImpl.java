package com.zhjs.scfcloud.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.lzProject.FindProjectAssetDTO;
import com.zhjs.scfcloud.model.dto.lzProject.LzProjectBoardReportDTO;
import com.zhjs.scfcloud.model.dto.lzProject.LzProjectListDTO;
import com.zhjs.scfcloud.model.entity.*;
import com.zhjs.scfcloud.model.mapper.*;
import com.zhjs.scfcloud.model.transfer.LzProjectTransfer;
import com.zhjs.scfcloud.model.vo.lzProject.*;
import com.zhjs.scfcloud.system.service.LzProjectService;
import com.zhjs.scfcloud.util.enums.CommonEnum;
import com.zhjs.scfcloud.util.util.BigDecimalUtil;
import com.zhjs.scfcloud.util.util.DateUtil;
import com.zhjs.scfcloud.util.util.ListUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.System;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 深华项目基本信息
 */

@Service
public class LzProjectServiceImpl implements LzProjectService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private LzProjectBasicInfoMapper lzProjectBasicInfoMapper;
    @Resource
    private LzProjectTransfer projectTransfer;
    @Resource
    private LzProjectPayMapper lzProjectPayMapper;
    @Resource
    private LzProjectGatheringMapper gatheringMapper;
    @Resource
    private LzProjectBudgetMapper budgetMapper;
    @Resource
    private LzProjectContractMapper contractMapper;
    @Resource
    private LzProjectContractSubMapper contractSubMapper;
    @Resource
    private LzProjectPurchaseLogisticsMapper lzProjectPurchaseLogisticsMapper;
    @Resource
    private LzProjectFundChangeMapper fundChangeMapper;
    @Resource
    private LzProjectLoanMapper lzProjectLoanMapper;
    @Resource
    private LzProjectRefundMapper lzProjectRefundMapper;
    @Resource
    private LzProjectFileMapper lzProjectFileMapper;
    @Resource
    private LzProjectLiabilitiesMapper liabilitiesMapper;
    @Resource
    private LzProjectPledgeCashPayMapper pledgeCashPayMapper;
    @Resource
    private LzProjectPledgeCashExtractMapper pledgeCashExtractMapper;
    @Resource
    private LzProjectInsuranceMapper insuranceMapper;
    @Resource
    private LzProjectInvoiceMapper invoiceMapper;
    @Resource
    private LzProjectMarginsPayMapper marginsPayMapper;
    @Resource
    private LzProjectMarginsExtractMapper marginsExtractMapper;

    //项目列表
    @Override
    public Result list(LzProjectListDTO dto) {
        dto.setRecords(lzProjectBasicInfoMapper.list(dto));
        return Result.successPage(dto);
    }

    //项目基本信息
    @Override
    public Result basicInfo(String contractNo) {
        LzProjectBasicInfo basicInfo = lzProjectBasicInfoMapper.selectOne(new QueryWrapper<LzProjectBasicInfo>().lambda()
                .eq(LzProjectBasicInfo::getContractNo,contractNo).ne(LzProjectBasicInfo::getDeleteStatus, CommonEnum.WhetherEnum.YES.getStatus()).last("limit 1"));
        if (basicInfo == null) {
            return Result.failure("项目不存在");
        }
        //转换
        LzProjectBasicInfoVO vo = projectTransfer.toLzProjectBasicInfoVO(basicInfo);
        return Result.success(vo);
    }

    //项目信息
    @Override
    public Result itemInfo(String contractNo) {
        LzProjectBasicInfo basicInfo = lzProjectBasicInfoMapper.selectOne(new QueryWrapper<LzProjectBasicInfo>().lambda()
                .eq(LzProjectBasicInfo::getContractNo,contractNo).ne(LzProjectBasicInfo::getDeleteStatus, CommonEnum.WhetherEnum.YES.getStatus()).last("limit 1"));
        if (basicInfo == null) {
            return Result.failure("项目不存在");
        }
        //转换为项目信息
        LzProjectItemInfoVO vo = projectTransfer.toLzProjectItemInfoVO(basicInfo);
        return Result.success(vo);
    }

    @Override
    public Result contractInfo(String contractNo) {
        Map<String,Object> map = new HashMap<>();
        //
        LzProjectContract contract = contractMapper.selectOne(new QueryWrapper<LzProjectContract>().lambda()
                .eq(LzProjectContract::getContractNo, contractNo)
                .eq(LzProjectContract::getDeleteStatus, CommonEnum.WhetherEnum.NO.getStatus()));
        LzProjectContractVO contractVO = new LzProjectContractVO();
        BeanUtils.copyProperties(contract, contractVO);
        map.put("basic", contractVO);
        //
        List<LzProjectContractVO> list = new ArrayList<>();
        List<LzProjectContractSub> subList = contractSubMapper.selectList(new QueryWrapper<LzProjectContractSub>().lambda()
                .eq(LzProjectContractSub::getContractNo, contractNo)
                .eq(LzProjectContractSub::getDeleteStatus, CommonEnum.WhetherEnum.NO.getStatus()));
        for(LzProjectContractSub subContract : subList){
            LzProjectContractVO e = new LzProjectContractVO();
            BeanUtils.copyProperties(subContract, e);
            list.add(e);
        }
        map.put("sub", list);
        return Result.success(map);
    }

    //项目付款信息
    @Override
    public Result payList(String contractNo) {
        List<LzProjectPayListVO> payListVOS = lzProjectPayMapper.getPayList(contractNo);
        return Result.success(payListVOS);
    }

    @Override
    public Result gatheringList(String contractNo) {
        List<LzProjectGathering> gatheringList =
                gatheringMapper.selectList(
                        new QueryWrapper<LzProjectGathering>().lambda()
                                .eq(LzProjectGathering::getContractNo, contractNo)
                                .ne(LzProjectGathering::getDeleteStatus, CommonEnum.WhetherEnum.YES.getStatus())
                                .orderByAsc(LzProjectGathering::getGatheringDate)
                );
        return Result.success(projectTransfer.toLzProjectGatheringVOList(gatheringList));
    }

    @Override
    public Result findCostDetails(String contractNo) {
        List<LzProjectCostDetailsVO> costDetailsVOList = budgetMapper.findCostDetails(contractNo);
        Map<String, Object> jMap = new HashMap<>();
        List<LzProjectCostDetailsVO> costList = new ArrayList<>();
        List<LzProjectCostDetailsVO> profitList = new ArrayList<>();
        List<Map<String, Object>> costProfitTotalList = new ArrayList<>();
        if(costDetailsVOList != null && !costDetailsVOList.isEmpty()) {
            BigDecimal contractMoney = BigDecimalUtil.orElse(costDetailsVOList.get(0).getContractMoney()); // 主合同金额
            //经营成本合计    PS: 根据需求规定，成本合计取 成本费用预算总额合计；成本比例取 成本费用预算比例合计；
            //补充： 成本费用为 材料费、人工费、项目管理人员薪酬、项目费用、咨询服务费
            BigDecimal costTotal = BigDecimal.ZERO;
            BigDecimal costRateTotal = BigDecimal.ZERO; //经营成本比例合计
            BigDecimal profitTotal = BigDecimal.ZERO;   //经营利润合计    PS: 根据需求规定，利润合计取 主合同金额 - 成本合计；利润比例合计取 100% - 成本比例合计
            BigDecimal profitRateTotal = BigDecimal.ZERO;   //经营利润比例合计
            BigDecimal earningTotal = contractMoney;  //经营收入合计 PS: 根据需求规定，收入合计直接取主合同金额，收入合计比例取100%
            BigDecimal earningRateTotal = BigDecimal.valueOf(100);  //经营收入比例合计


            // 计算成本费用合计
            for(LzProjectCostDetailsVO vo: costDetailsVOList) {
                // 判断是否是成本费用
                if(com.zhjs.scfcloud.util.enums.LzProject.CommonEnum.FeeType.hasCostType(vo.getFeeType())) {
                    costTotal = costTotal.add(BigDecimalUtil.orElse(vo.getBudgetMoney()));
                    costRateTotal = costRateTotal.add(BigDecimalUtil.orElse(vo.getBudgetRate()));
                    costList.add(vo);
                } else {
                    profitList.add(vo);
                }
            }

            BigDecimal tmp = BigDecimal.valueOf(100);
            costRateTotal = costRateTotal.multiply(tmp).setScale(2);
            profitTotal = contractMoney.subtract(costTotal);
            profitRateTotal = tmp.subtract(costRateTotal).setScale(2);

            // 组装成本利润合计数据
            Map<String, Object> map = new HashMap<>();
            map.put("totalType", "经营成本合计");
            map.put("totalMoney", costTotal);
            map.put("totalRate", costRateTotal.toString() + "%");
            map.put("explain", "经营成本=直接成本+业务费用+项目费用+公司内部费用；比例=经营成本/合同金额");
            costProfitTotalList.add(map);

            map = new HashMap<>();
            map.put("totalType", "经营利润合计");
            map.put("totalMoney", profitTotal);
            map.put("totalRate", profitRateTotal.toString() + "%");
            map.put("explain", "经营利润=合同金额+加盟服务费-成本合计；比例=利润金额/合同金额");
            costProfitTotalList.add(map);

            map = new HashMap<>();
            map.put("totalType", "经营收入合计");
            map.put("totalMoney", earningTotal);
            map.put("totalRate", earningRateTotal.toString() + "%");
            map.put("explain", "项目经营收入总计，应为固定值100%");
            costProfitTotalList.add(map);

            jMap.put("contractMoney", contractMoney);
        }

        jMap.put("costList", costList);
        jMap.put("profitList", profitList);
        jMap.put("costProfitTotalList", costProfitTotalList);
        return Result.success(jMap);
    }

    /**
     * 查询采购物流信息
     * @param contractNo
     * @return
     */
    @Override
    public Result purchaseLogistics(String contractNo) {
        List<LzProjectPurchaseLogisticsListVO> list = lzProjectPurchaseLogisticsMapper.getListByContractNo(contractNo);
        return Result.success(list);
    }

    @Override
    public Result findProjectAsset(FindProjectAssetDTO dto) {
        List<String> paramDays = DateUtil.getDateListByMonthLastDay(dto.getStartDate(), dto.getEndDate());
        List<LzProjectFundChange> fundChangeList =
                fundChangeMapper.findProjectAsset(dto.getContractNo(), paramDays);

        List<String> days = DateUtil.getDateListByMonth(dto.getStartDate(), dto.getEndDate());
        List<BigDecimal> projectBalance = new ArrayList<>();
        List<BigDecimal> billReceivable = new ArrayList<>();
        List<BigDecimal> invoiceBalance = new ArrayList<>();
        List<BigDecimal> materialsAdvance = new ArrayList<>();
        List<BigDecimal> materialInventory = new ArrayList<>();
        List<BigDecimal> totalGather = new ArrayList<>();
        if (fundChangeList != null) {
            LzProjectFundChange fc = null;
            BigDecimal zero = BigDecimal.ZERO;
            for (int i = 0; i < days.size(); i++) {
                final int idx = i;
                fc = fundChangeList.stream()
                        .filter(t -> days.get(idx).equals(DateUtil.format(t.getExpirationDate(), "yyyy-MM")))
                        .findAny().orElse(null);
                if (fc != null) {

                    projectBalance.add(fc.getTotalBalance());
                    billReceivable.add(fc.getBillReceivable());
                    invoiceBalance.add(fc.getInvoiceBalance());
                    materialsAdvance.add(fc.getMaterialsAdvance());
                    materialInventory.add(fc.getMaterialInventory());
                    totalGather.add(fc.getTotalGather());
                } else {
                    projectBalance.add(null);
                    billReceivable.add(null);
                    invoiceBalance.add(null);
                    materialsAdvance.add(null);
                    materialInventory.add(null);
                    totalGather.add(null);
                }

            }
        }
        Map<String, Object> jMap = new HashMap<>();
        jMap.put("days", days);
        jMap.put("projectBalance", projectBalance);
        jMap.put("billReceivable", billReceivable);
        jMap.put("invoiceBalance", invoiceBalance);
        jMap.put("materialsAdvance", materialsAdvance);
        jMap.put("materialInventory", materialInventory);
        jMap.put("totalGather", totalGather);
        return Result.success(jMap);
    }

    /**
     * 借款信息
     * @param contractNo
     * @param isPersonal
     * @return
     */
    @Override
    public Result loanList(String contractNo, String isPersonal) {
        List<LzProjectLoanListVO> list = lzProjectLoanMapper.getLoanList(contractNo,isPersonal);
        return Result.success(list);
    }

    /**
     * 还款信息
     * @param loanNo
     * @return
     */
    @Override
    public Result refundList(String loanNo) {
        List<LzProjectRefundListVO> list = lzProjectRefundMapper.getRefundList(loanNo);
        return Result.success(list);
    }

    /**
     * 项目文件
     * @param contractNo
     * @return
     */
    @Override
    public Result fileList(String contractNo) {
        LambdaQueryWrapper<LzProjectFile> wrapper = new QueryWrapper<LzProjectFile>().lambda();
        wrapper.eq(LzProjectFile::getContractNo,contractNo);
        wrapper.ne(LzProjectFile::getDeleteStatus,CommonEnum.WhetherEnum.YES.getStatus());
        List<LzProjectFile> list = lzProjectFileMapper.selectList(wrapper);
        if (ListUtil.isEmpty(list)) {
            return Result.success(list);
        }
        //根据文件分类分组
        Map<String,List<LzProjectFile>> listMap =  list.stream().collect(Collectors.groupingBy(LzProjectFile::getFileClassify));
        List result = new ArrayList();
        for (String key : listMap.keySet()) {
            Map oneClassifyFileMap = new HashMap();
            List<LzProjectFile> oneClassifyFileList = listMap.get(key);
            oneClassifyFileMap.put("fileClassify",key);
            oneClassifyFileMap.put("oneClassifyFileList",oneClassifyFileList);
            oneClassifyFileMap.put("count",oneClassifyFileList.size());
            result.add(oneClassifyFileMap);
        }
        return Result.success(result);
    }

    @Override
    public Result findLiabilities(FindProjectAssetDTO dto) {
        List<String> paramDays = DateUtil.getDateListByMonthLastDay(dto.getStartDate(), dto.getEndDate());
        List<LzProjectLiabilities> liabilitiesList = liabilitiesMapper.findLiabilities(dto.getContractNo(), paramDays);

        List<String> days = DateUtil.getDateListByMonth(dto.getStartDate(), dto.getEndDate());
        List<BigDecimal> repayInterest = new ArrayList<>();
        List<BigDecimal> projectLoanRepayPrincipal = new ArrayList<>();
        List<BigDecimal> billRepayPrincipal = new ArrayList<>();
        List<BigDecimal> dealInLoanRepayPrincipal = new ArrayList<>();
        List<BigDecimal> repayMaterialsFee = new ArrayList<>();
        List<BigDecimal> pdPaidManageFee = new ArrayList<>();
        List<BigDecimal> pdPaidLaborFee = new ArrayList<>();
        List<BigDecimal> pdPaidMaterialsFee = new ArrayList<>();
        List<BigDecimal> pdPaidProjectFee = new ArrayList<>();
        List<BigDecimal> projectManageSurplus = new ArrayList<>();

        if (liabilitiesList != null) {
            BigDecimal zero = BigDecimal.ZERO;
            LzProjectLiabilities lpl = null;
            for (int i = 0; i < days.size(); i++) {
                final int idx = i;
                lpl = liabilitiesList.stream()
                        .filter(t -> days.get(idx).equals(DateUtil.format(t.getExpirationDate(), "yyyy-MM")))
                        .findAny().orElse(null);
                if (lpl != null) {
                    repayInterest.add(lpl.getRepayInterest());
                    projectLoanRepayPrincipal.add(lpl.getProjectLoanRepayPrincipal());
                    billRepayPrincipal.add(lpl.getBillRepayPrincipal());
                    dealInLoanRepayPrincipal.add(lpl.getDealInLoanRepayPrincipal());
                    repayMaterialsFee.add(lpl.getRepayMaterialsFee());
                    pdPaidManageFee.add(lpl.getPdPaidManageFee());
                    pdPaidLaborFee.add(lpl.getPdPaidLaborFee());
                    pdPaidMaterialsFee.add(lpl.getPdPaidMaterialsFee());
                    pdPaidProjectFee.add(lpl.getPdPaidProjectFee());
                    projectManageSurplus.add(lpl.getProjectManageSurplus());
                } else {
                    repayInterest.add(null);
                    projectLoanRepayPrincipal.add(null);
                    billRepayPrincipal.add(null);
                    dealInLoanRepayPrincipal.add(null);
                    repayMaterialsFee.add(null);
                    pdPaidManageFee.add(null);
                    pdPaidLaborFee.add(null);
                    pdPaidMaterialsFee.add(null);
                    pdPaidProjectFee.add(null);
                    projectManageSurplus.add(null);
                }
            }
        }

        Map<String, Object> jMap = new HashMap<>();
        jMap.put("days", days);
        jMap.put("repayInterest", repayInterest);
        jMap.put("projectLoanRepayPrincipal", projectLoanRepayPrincipal);
        jMap.put("billRepayPrincipal", billRepayPrincipal);
        jMap.put("dealInLoanRepayPrincipal", dealInLoanRepayPrincipal);
        jMap.put("repayMaterialsFee", repayMaterialsFee);
        jMap.put("pdPaidManageFee", pdPaidManageFee);
        jMap.put("pdPaidLaborFee", pdPaidLaborFee);
        jMap.put("pdPaidMaterialsFee", pdPaidMaterialsFee);
        jMap.put("pdPaidProjectFee", pdPaidProjectFee);
        jMap.put("projectManageSurplus", projectManageSurplus);
        return Result.success(jMap);
    }

    //项目看板卡片数据
    @Override
    public Result boardCard(String contractNo) {
        LzProjectFundChange fund = fundChangeMapper.selectOne(new QueryWrapper<LzProjectFundChange>().lambda()
                .eq(LzProjectFundChange::getContractNo,contractNo)
                .ne(LzProjectFundChange::getDeleteStatus,CommonEnum.WhetherEnum.YES.getStatus())
                .orderByDesc(LzProjectFundChange::getExpirationDate)
                .last("limit 1"));
        if (fund == null) {
            return Result.success();
        }
        Map retMap = new HashMap();
        retMap.put("balance",fund.getTotalBalance());
        retMap.put("pay",fund.getTotalPayment());
        retMap.put("gather",fund.getTotalGather());
        retMap.put("marginsAndPledge",fund.getTotalMarginsAndPledge());
        retMap.put("loan",fund.getTotalLoan());
        return Result.success(retMap);
    }

    //项目看板报表数据
    @Override
    public Result boardReport(LzProjectBoardReportDTO dto) {
        List<LzProjectFundChange> list = fundChangeMapper.selectList(new QueryWrapper<LzProjectFundChange>().lambda()
                .eq(LzProjectFundChange::getContractNo,dto.getContractNo())
                .ge(LzProjectFundChange::getExpirationDate,dto.getBeginDate())
                .le(LzProjectFundChange::getExpirationDate,dto.getEndDate())
                .orderByDesc(LzProjectFundChange::getExpirationDate));

        //获取两个日期之间的所有日期
        List<String> days = DateUtil.getDateList(dto.getBeginDate(),dto.getEndDate());
        List balanceArray = new ArrayList();
        List gatherArray = new ArrayList();
        List payArray = new ArrayList();
        List marginsAndPledgeArray = new ArrayList();
        List loanArray = new ArrayList();

        if (!ListUtil.isEmpty(list)) {
            Map<String, List<LzProjectFundChange>> listMap = list.stream().collect(Collectors.groupingBy(fundChange->DateUtil.format(fundChange.getExpirationDate(),"yyyy-MM-dd")));
            for (String day : days) {
                List<LzProjectFundChange> oneDayList = listMap.get(day);
                if (ListUtil.isEmpty(oneDayList)) {
                    balanceArray.add(0);
                    gatherArray.add(0);
                    payArray.add(0);
                    marginsAndPledgeArray.add(0);
                    loanArray.add(0);
                } else {
                    balanceArray.add(oneDayList.get(0).getTotalBalance());
                    gatherArray.add(oneDayList.get(0).getTotalGather());
                    payArray.add(oneDayList.get(0).getTotalPayment());
                    marginsAndPledgeArray.add(oneDayList.get(0).getTotalMarginsAndPledge());
                    loanArray.add(oneDayList.get(0).getTotalLoan());
                }
            }
        }

        Map retMap = new HashMap();
        retMap.put("days",days);
        retMap.put("balanceArray",balanceArray);
        retMap.put("gatherArray",gatherArray);
        retMap.put("payArray",payArray);
        retMap.put("marginsAndPledgeArray",marginsAndPledgeArray);
        retMap.put("loanArray",loanArray);

        return Result.success(retMap);
    }

    @Override
    public void generateData () {
        List<String> days = DateUtil.getDateList("2018-1-1","2019-12-31");
        LzProjectFundChange fundChange = new LzProjectFundChange();
//        String[] contracts = {"ASDDFSFD25152415415","史蒂芬孙的35331212sdds","HK20191111021356894","HK2019111102135680001",
//                               "HK2019111102135680002","HK2019111102135680003","HK2019111102135680004","HK2019111102135680005",
//                                "HK2019111102135680006","HK2019111102135680007","HK2019111102135680008","HK2019111102135680009",
//                                "HK2019111102135680010","HK2019111102135680011","HK2019111102135680012","HK2019111102135680013",
//                                "HK2019111102135680014","HK2019111102135680015"};
        String[] contracts = {"ct152322"};
        for (String contractNo : contracts) {
            for (String day : days) {
                fundChange.setContractNo(contractNo).setExpirationDate(DateUtil.parse(day,"yyyy-MM-dd"));
                fundChange.setTotalBalance(BigDecimal.valueOf(Math.random()*5000000 ).setScale(2,BigDecimal.ROUND_DOWN));
                fundChange.setTotalGather(BigDecimal.valueOf(Math.random()*2000000).setScale(2,BigDecimal.ROUND_DOWN));
                fundChange.setTotalPayment(BigDecimal.valueOf(Math.random()*4000000 ).setScale(2,BigDecimal.ROUND_DOWN));
                fundChange.setTotalLoan(BigDecimal.valueOf(Math.random()*3000000 ).setScale(2,BigDecimal.ROUND_DOWN));
                fundChange.setTotalMarginsAndPledge(BigDecimal.valueOf(Math.random()*1000000).setScale(2,BigDecimal.ROUND_DOWN));
                fundChange.setBillReceivable(BigDecimal.valueOf(Math.random()*1000000).setScale(2,BigDecimal.ROUND_DOWN));
                fundChange.setInvoiceBalance(BigDecimal.valueOf(Math.random()*1000000).setScale(2,BigDecimal.ROUND_DOWN));
                fundChange.setMaterialsAdvance(BigDecimal.valueOf(Math.random()*1000000).setScale(2,BigDecimal.ROUND_DOWN));
                fundChange.setMaterialInventory(BigDecimal.valueOf(Math.random()*1000000).setScale(2,BigDecimal.ROUND_DOWN));
                fundChange.setDeleteStatus(0).setCreateTime(new Date()).setCreateBy(1l).setUpdateTime(new Date()).setUpdateBy(1l);
                fundChangeMapper.insert(fundChange);
                System.out.println("insert day: "+day);
            }
        }
    }

    @Override
    public Result getPledgeCashPayList(String contractNo) {
        List<LzProjectPledgeCashPay> pledgeCashPayList =
                pledgeCashPayMapper.selectList(new QueryWrapper<LzProjectPledgeCashPay>().lambda()
                        .eq(LzProjectPledgeCashPay::getContractNo, contractNo)
                        .eq(LzProjectPledgeCashPay::getDeleteStatus, CommonEnum.WhetherEnum.NO.getStatus()).orderByAsc(LzProjectPledgeCashPay::getPayDate));
        return Result.success(projectTransfer.toLzProjectPledgeCashPayVOList(pledgeCashPayList));
    }

    @Override
    public Result getPledgeCashExtractList(String contractNo) {
        List<LzProjectPledgeCashExtract> pledgeCashExtractList =
                pledgeCashExtractMapper.selectList(new QueryWrapper<LzProjectPledgeCashExtract>().lambda()
                        .eq(LzProjectPledgeCashExtract::getContractNo, contractNo)
                        .eq(LzProjectPledgeCashExtract::getDeleteStatus, CommonEnum.WhetherEnum.NO.getStatus())
                        .orderByAsc(LzProjectPledgeCashExtract::getExtractDate));
        return Result.success(projectTransfer.toLzProjectPledgeCashExtractVOList(pledgeCashExtractList));
    }

    @Override
    public Result insuranceList(String contractNo) {
        List<LzProjectInsuranceListVO> list = insuranceMapper.getList(contractNo);
        return Result.success(list);
    }

    @Override
    public Result invoiceList(String contractNo) {
        List<LzProjectInvoiceListVO> list = invoiceMapper.getList(contractNo);
        return Result.success(list);
    }

    @Override
    public Result findMarginsList(String contractNo) {
        List<LzProjectMarginsPayVO> marginsPayList = marginsPayMapper.findMarginsPayList(contractNo);
        List<LzProjectMarginsExtractVO> marginsExtractList = marginsExtractMapper.findMarginsExtractList(contractNo);
        Map<String, Object> jMap = new HashMap<>();
        jMap.put("marginsPayList", marginsPayList);
        jMap.put("marginsExtractList", marginsExtractList);
        return Result.success(jMap);
    }
}
