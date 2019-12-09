package com.zhjs.scfcloud.task.job;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhjs.scfcloud.model.entity.*;
import com.zhjs.scfcloud.model.mapper.*;
import com.zhjs.scfcloud.util.enums.CommonEnum;
import com.zhjs.scfcloud.util.util.ListUtil;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 统计深华项目资金变动-定时器
 *
 * @author: weijie.chen
 * @date:2019/10/10
 */
public class AutoReportLzProjectFundJob extends QuartzJobBean {

    private static final Logger logger = LoggerFactory.getLogger(AutoReportLzProjectFundJob.class);

    @Resource
    private LzProjectBasicInfoMapper lzProjectBasicInfoMapper;
    @Resource
    private LzProjectFundChangeMapper lzProjectFundChangeMapper;
    @Resource
    private LzProjectGatheringMapper lzProjectGatheringMapper;
    @Resource
    private LzProjectPayMapper lzProjectPayMapper;
    @Resource
    private LzProjectLoanMapper lzProjectLoanMapper;
    @Resource
    private LzProjectRefundMapper lzProjectRefundMapper;
    @Resource
    private LzProjectMarginsPayMapper lzProjectMarginsPayMapper;
    @Resource
    private LzProjectMarginsExtractMapper lzProjectMarginsExtractMapper;
    @Resource
    private LzProjectPledgeCashPayMapper lzProjectPledgeCashPayMapper;
    @Resource
    private LzProjectPledgeCashExtractMapper lzProjectPledgeCashExtractMapper;

    @Override
    @Transactional
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        logger.info("===========>任务开始执行-统计深华项目资金变动");
        List<LzProjectBasicInfo> projectList = lzProjectBasicInfoMapper.selectList(new QueryWrapper<>());
        if (!ListUtil.isEmpty(projectList)) {
            List<LzProjectFundChange> allFundChangeList = new ArrayList<>();
            projectList.stream().forEach(project -> {
                List<LzProjectFundChange> oneFundChangeList =  handleOneProject(project);
                if (!ListUtil.isEmpty(oneFundChangeList)) {
                    allFundChangeList.addAll(oneFundChangeList);
                }
            });
            //批量插入
        }
        logger.info("===========>任务执行结束-统计深华项目资金变动");
    }

    //处理单个项目
    private List<LzProjectFundChange> handleOneProject(LzProjectBasicInfo project) {
        //获取收款信息
        Map<Date, BigDecimal> gatherMap = getGatherMap(project);
        //获取付款信息
        Map<Date, BigDecimal> payMap = getPayMap(project);
        //获取借款信息
        Map<Date, BigDecimal> loanMap = getLoanMap(project);
        //获取还款信息
        Map<Date, BigDecimal> refundMap = getRefundMap(project);
        //获取保证金信息
        Map<Date, BigDecimal> marginsPayMap = getMarginsPayMap(project);
        //获取保证金提取信息
        Map<Date, BigDecimal> marginsExtractMap = getMarginsExtractMap(project);
        //获取押金信息
        Map<Date, BigDecimal> pledgeCashPayMap = getPledgeCashPayMap(project);
        //获取押金提取信息
        Map<Date, BigDecimal> pledgeCashExtractMap = getPledgeCashExtractMap(project);
        //获取有资金变化的最早时间
        Map<String, Date> maxMinDateMap = getMaxMinDate(gatherMap, payMap, loanMap, refundMap, marginsPayMap, marginsExtractMap, pledgeCashPayMap, pledgeCashExtractMap);
        //计算每天的各类累计金额
        List<LzProjectFundChange> fundChangeList = computeFundChange(project.getContractNo(), maxMinDateMap, gatherMap, payMap, loanMap, refundMap, marginsPayMap, marginsExtractMap, pledgeCashPayMap, pledgeCashExtractMap);
        return fundChangeList;
    }

    private List<LzProjectFundChange> computeFundChange(String contractNo, Map<String, Date> dateMap, Map<Date, BigDecimal> gatherMap, Map<Date, BigDecimal> payMap, Map<Date, BigDecimal> loanMap, Map<Date, BigDecimal> refundMap, Map<Date, BigDecimal> marginsPayMap, Map<Date, BigDecimal> marginsExtractMap, Map<Date, BigDecimal> pledgeCashPayMap, Map<Date, BigDecimal> pledgeCashExtractMap) {
        List<LzProjectFundChange> fundChangeList = new ArrayList<>();
        Date expirationDate = dateMap.get("minDate");
        Date endDate = dateMap.get("maxDate");
        BigDecimal balance = BigDecimal.ZERO; //余额 项目余额 = 回款+借款+保证金+押金-付款
        BigDecimal gather = BigDecimal.ZERO;  //收款
        BigDecimal pay = BigDecimal.ZERO;  //付款
        BigDecimal loan = BigDecimal.ZERO;  //借款
        BigDecimal refund = BigDecimal.ZERO;  //还款
        BigDecimal margins = BigDecimal.ZERO;  //保证金
        BigDecimal marginsPay = BigDecimal.ZERO;  //保证金支付
        BigDecimal marginsExtract = BigDecimal.ZERO;  //保证金提取
        BigDecimal pledgeCash = BigDecimal.ZERO;  //押金
        BigDecimal pledgeCashPay = BigDecimal.ZERO;  //押金支付
        BigDecimal pledgeCashExtract = BigDecimal.ZERO; //押金提取
        while (endDate.getTime() >= expirationDate.getTime()) {
            gather = gather.add(gatherMap.get(expirationDate) == null ? BigDecimal.ZERO : gatherMap.get(expirationDate));
            pay = pay.add(payMap.get(expirationDate) == null ? BigDecimal.ZERO : payMap.get(expirationDate));
            refund = refund.add(refundMap.get(expirationDate) == null ? BigDecimal.ZERO : refundMap.get(expirationDate));
            loan = loan.add(loanMap.get(expirationDate) == null ? BigDecimal.ZERO : loanMap.get(expirationDate));
            marginsPay = marginsPay.add(marginsPayMap.get(expirationDate) == null ? BigDecimal.ZERO : marginsPayMap.get(expirationDate));
            marginsExtract = marginsExtract.add(marginsExtractMap.get(expirationDate) == null ? BigDecimal.ZERO : marginsExtractMap.get(expirationDate));
            margins = marginsPay.subtract(marginsExtract);
            pledgeCashPay = pledgeCashPay.add(pledgeCashPayMap.get(expirationDate) == null ? BigDecimal.ZERO : pledgeCashPayMap.get(expirationDate));
            pledgeCashExtract = pledgeCashExtract.add(pledgeCashExtractMap.get(expirationDate) == null ? BigDecimal.ZERO : pledgeCashExtractMap.get(expirationDate));
            pledgeCash = pledgeCashPay.subtract(pledgeCashExtract);
            balance = gather.add(loan.subtract(refund)).add(margins).add(pledgeCash).subtract(pay);

            LzProjectFundChange fundChange = new LzProjectFundChange();
            fundChange.setContractNo(contractNo);
            fundChange.setExpirationDate(expirationDate);
            fundChange.setTotalBalance(balance);
            fundChange.setTotalGather(gather);
            fundChange.setTotalLoan(loan.subtract(refund));
            fundChange.setTotalMarginsAndPledge(margins.add(pledgeCash));
            fundChange.setTotalPayment(pay);
            fundChange.setDeleteStatus(CommonEnum.WhetherEnum.NO.getStatus());
            fundChange.setCreateBy(1l);
            fundChange.setCreateTime(new Date());
            fundChangeList.add(fundChange);
        }
        return fundChangeList;
    }

    private Map<String, Date> getMaxMinDate(Map<Date, BigDecimal>... maps) {
        Date maxDate = null;
        Date minDate = null;
        for (int i = 0; i < maps.length; i++) {
            Map<Date, BigDecimal> map = maps[i];
            Set<Date> set = map.keySet();
            Object[] obj = set.toArray();
            Arrays.sort(obj);
            if (maxDate == null || maxDate.getTime() < ((Date) obj[obj.length - 1]).getTime()) {
                maxDate = (Date) obj[obj.length - 1];
            }
            if (minDate == null || minDate.getTime() > ((Date) obj[0]).getTime()) {
                minDate = (Date) obj[0];
            }
        }
        Map<String, Date> retMap = new HashMap();
        retMap.put("maxDate", maxDate);
        retMap.put("minDate", minDate);
        return retMap;
    }

    //获取单个项目的押金提取信息
    private Map<Date, BigDecimal> getPledgeCashExtractMap(LzProjectBasicInfo project) {
        Map<Date, BigDecimal> pledgeCashExtractMap = new HashMap<>();
        //查询项目所有押金提取信息
        List<LzProjectPledgeCashExtract> extractList = lzProjectPledgeCashExtractMapper.selectList(new QueryWrapper<LzProjectPledgeCashExtract>().lambda()
                .eq(LzProjectPledgeCashExtract::getContractNo, project.getContractNo())
                .ne(LzProjectPledgeCashExtract::getDeleteStatus, CommonEnum.WhetherEnum.YES.getStatus()));
        //无数据直接返回
        if (ListUtil.isEmpty(extractList)) {
            return pledgeCashExtractMap;
        }
        //根据付提取日期分组
        Map<Date, List<LzProjectPledgeCashExtract>> mapList = extractList.stream().collect(Collectors.groupingBy(LzProjectPledgeCashExtract::getExtractDate));

        //计算每天的押金提取额
        for (Date oneDay : mapList.keySet()) {
            List<LzProjectPledgeCashExtract> oneDayList = mapList.get(oneDay);
            BigDecimal oneDayMoney = new BigDecimal(0);
            for (LzProjectPledgeCashExtract pledgeCashExtract : oneDayList) {
                oneDayMoney = oneDayMoney.add(pledgeCashExtract.getExtractMoney());
            }
            pledgeCashExtractMap.put(oneDay, oneDayMoney);
        }
        return pledgeCashExtractMap;
    }

    //获取单个项目的押金信息
    private Map<Date, BigDecimal> getPledgeCashPayMap(LzProjectBasicInfo project) {
        Map<Date, BigDecimal> pledgeCashPayMap = new HashMap<>();
        //查询项目所有押金付款信息
        List<LzProjectPledgeCashPay> extractList = lzProjectPledgeCashPayMapper.selectList(new QueryWrapper<LzProjectPledgeCashPay>().lambda()
                .eq(LzProjectPledgeCashPay::getContractNo, project.getContractNo())
                .ne(LzProjectPledgeCashPay::getDeleteStatus, CommonEnum.WhetherEnum.YES.getStatus()));
        //无数据直接返回
        if (ListUtil.isEmpty(extractList)) {
            return pledgeCashPayMap;
        }
        //根据付款日期分组
        Map<Date, List<LzProjectPledgeCashPay>> mapList = extractList.stream().collect(Collectors.groupingBy(LzProjectPledgeCashPay::getPayDate));

        //计算每天的押金额
        for (Date oneDay : mapList.keySet()) {
            List<LzProjectPledgeCashPay> oneDayList = mapList.get(oneDay);
            BigDecimal oneDayMoney = new BigDecimal(0);
            for (LzProjectPledgeCashPay pledgeCashPay : oneDayList) {
                oneDayMoney = oneDayMoney.add(pledgeCashPay.getPayMoney());
            }
            pledgeCashPayMap.put(oneDay, oneDayMoney);
        }
        return pledgeCashPayMap;
    }

    //获取单个项目的保证金提取信息
    private Map<Date, BigDecimal> getMarginsExtractMap(LzProjectBasicInfo project) {
        Map<Date, BigDecimal> marginsExtractMap = new HashMap<>();
        //查询项目所有保证金提取信息
        List<LzProjectMarginsExtract> extractList = lzProjectMarginsExtractMapper.selectList(new QueryWrapper<LzProjectMarginsExtract>().lambda()
                .eq(LzProjectMarginsExtract::getContractNo, project.getContractNo())
                .ne(LzProjectMarginsExtract::getDeleteStatus, CommonEnum.WhetherEnum.YES.getStatus()));
        //无数据直接返回
        if (ListUtil.isEmpty(extractList)) {
            return marginsExtractMap;
        }
        //根据提取日期分组
        Map<Date, List<LzProjectMarginsExtract>> mapList = extractList.stream().collect(Collectors.groupingBy(LzProjectMarginsExtract::getExtractDate));

        //计算每天的提取保证金总额
        for (Date oneDay : mapList.keySet()) {
            List<LzProjectMarginsExtract> oneExtractList = mapList.get(oneDay);
            BigDecimal oneDayExtract = new BigDecimal(0);
            for (LzProjectMarginsExtract extract : oneExtractList) {
                oneDayExtract = oneDayExtract.add(extract.getExtractMoney());
            }
            marginsExtractMap.put(oneDay, oneDayExtract);
        }
        return marginsExtractMap;
    }

    //获取单个项目的保证金信息
    private Map<Date, BigDecimal> getMarginsPayMap(LzProjectBasicInfo project) {
        Map<Date, BigDecimal> marginsPayMap = new HashMap<>();
        //查询项目所有保证金信息
        List<LzProjectMarginsPay> payList = lzProjectMarginsPayMapper.selectList(new QueryWrapper<LzProjectMarginsPay>().lambda()
                .eq(LzProjectMarginsPay::getContractNo, project.getContractNo())
                .ne(LzProjectMarginsPay::getDeleteStatus, CommonEnum.WhetherEnum.YES.getStatus()));
        //无数据直接返回
        if (ListUtil.isEmpty(payList)) {
            return marginsPayMap;
        }
        //根据保证金付款日期分组
        Map<Date, List<LzProjectMarginsPay>> mapList = payList.stream().collect(Collectors.groupingBy(LzProjectMarginsPay::getPayDate));

        //计算每天的保证金金额
        for (Date oneDay : mapList.keySet()) {
            List<LzProjectMarginsPay> onePayList = mapList.get(oneDay);
            BigDecimal oneDayPay = new BigDecimal(0);
            for (LzProjectMarginsPay pay : onePayList) {
                oneDayPay = oneDayPay.add(pay.getPayMoney());
            }
            marginsPayMap.put(oneDay, oneDayPay);
        }
        return marginsPayMap;
    }

    //获取单个项目的还款信息
    private Map<Date, BigDecimal> getRefundMap(LzProjectBasicInfo project) {
        Map<Date, BigDecimal> loanMap = new HashMap<>();
        //查询项目所有还款信息
        List<LzProjectRefund> loanList = lzProjectRefundMapper.selectList(new QueryWrapper<LzProjectRefund>().lambda()
                .eq(LzProjectRefund::getContractNo, project.getContractNo())
                .ne(LzProjectRefund::getDeleteStatus, CommonEnum.WhetherEnum.YES.getStatus()));
        //无数据直接返回
        if (ListUtil.isEmpty(loanList)) {
            return loanMap;
        }
        //根据借款日期分组
        Map<Date, List<LzProjectRefund>> mapList = loanList.stream().collect(Collectors.groupingBy(LzProjectRefund::getRefundDate));
        //计算每天的借款额
        for (Date oneDay : mapList.keySet()) {
            List<LzProjectRefund> oneDayList = mapList.get(oneDay);
            BigDecimal oneDayMoney = new BigDecimal(0);
            for (LzProjectRefund refund : oneDayList) {
                oneDayMoney = oneDayMoney.add(refund.getRefundMoney());
            }
            loanMap.put(oneDay, oneDayMoney);
        }

        return loanMap;
    }

    //获取单个项目的借款信息
    private Map<Date, BigDecimal> getLoanMap(LzProjectBasicInfo project) {
        Map<Date, BigDecimal> loanMap = new HashMap<>();
        //查询项目所有借款信息
        List<LzProjectLoan> loanList = lzProjectLoanMapper.selectList(new QueryWrapper<LzProjectLoan>().lambda()
                .eq(LzProjectLoan::getContractNo, project.getContractNo())
                .ne(LzProjectLoan::getDeleteStatus, CommonEnum.WhetherEnum.YES.getStatus()));
        //无数据直接返回
        if (ListUtil.isEmpty(loanList)) {
            return loanMap;
        }
        //根据借款日期分组
        Map<Date, List<LzProjectLoan>> loanMapList = loanList.stream().collect(Collectors.groupingBy(LzProjectLoan::getLoanDate));
        //计算每天的借款额
        for (Date oneDay : loanMapList.keySet()) {
            List<LzProjectLoan> oneLoanList = loanMapList.get(oneDay);
            BigDecimal oneDayLoan = new BigDecimal(0);
            for (LzProjectLoan loan : oneLoanList) {
                oneDayLoan = oneDayLoan.add(loan.getLoanMoney());
            }
            loanMap.put(oneDay, oneDayLoan);
        }

        return loanMap;
    }

    //获取单个项目的付款信息
    private Map<Date, BigDecimal> getPayMap(LzProjectBasicInfo project) {
        Map<Date, BigDecimal> payMap = new HashMap<>();
        //查询项目所有付款信息
        List<LzProjectPay> payList = lzProjectPayMapper.selectList(new QueryWrapper<LzProjectPay>().lambda()
                .eq(LzProjectPay::getContractNo, project.getContractNo())
                .ne(LzProjectPay::getDeleteStatus, CommonEnum.WhetherEnum.YES.getStatus()));
        //无数据直接返回
        if (ListUtil.isEmpty(payList)) {
            return payMap;
        }
        //根据付款日期分组
        Map<Date, List<LzProjectPay>> mapList = payList.stream().collect(Collectors.groupingBy(LzProjectPay::getPayDate));

        //计算每天的付款额
        for (Date oneDay : mapList.keySet()) {
            List<LzProjectPay> onePayList = mapList.get(oneDay);
            BigDecimal oneDayPay = new BigDecimal(0);
            for (LzProjectPay pay : onePayList) {
                oneDayPay = oneDayPay.add(pay.getActualPayMoney());
            }
            payMap.put(oneDay, oneDayPay);
        }
        return payMap;
    }

    //获取单个项目的收款信息
    private Map<Date, BigDecimal> getGatherMap(LzProjectBasicInfo project) {
        Map<Date, BigDecimal> gatherMap = new HashMap<>();
        //查询项目所有收款信息
        List<LzProjectGathering> gatheringList = lzProjectGatheringMapper.selectList(new QueryWrapper<LzProjectGathering>().lambda()
                .eq(LzProjectGathering::getContractNo, project.getContractNo())
                .ne(LzProjectGathering::getDeleteStatus, CommonEnum.WhetherEnum.YES.getStatus()));
        //无数据直接返回
        if (ListUtil.isEmpty(gatheringList)) {
            return gatherMap;
        }
        //根据收款日期分组
        Map<Date, List<LzProjectGathering>> mapList = gatheringList.stream().collect(Collectors.groupingBy(LzProjectGathering::getGatheringDate));

        //计算每天的收款额
        for (Date oneDay : mapList.keySet()) {
            List<LzProjectGathering> oneGatherList = mapList.get(oneDay);
            BigDecimal oneDayGather = new BigDecimal(0);
            for (LzProjectGathering gathering : oneGatherList) {
                oneDayGather = oneDayGather.add(gathering.getGatheringMoney());
            }
            gatherMap.put(oneDay, oneDayGather);
        }
        return gatherMap;
    }
}
