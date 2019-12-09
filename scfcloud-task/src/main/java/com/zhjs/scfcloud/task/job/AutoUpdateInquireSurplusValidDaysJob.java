package com.zhjs.scfcloud.task.job;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhjs.scfcloud.model.entity.BusinessTicketInquire;
import com.zhjs.scfcloud.model.mapper.BusinessTicketInquireMapper;
import com.zhjs.scfcloud.util.util.DateUtil;
import com.zhjs.scfcloud.util.util.ListUtil;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 自动修改询价单中票据的剩余天数
 */
public class AutoUpdateInquireSurplusValidDaysJob extends QuartzJobBean {
    private static final Logger logger = LoggerFactory.getLogger(AutoUpdateInquireSurplusValidDaysJob.class);

    @Resource
    private BusinessTicketInquireMapper businessTicketInquireMapper;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("===========>任务开始执行-自动修改询价单票据的剩余天数");
        autoUpdateInquireSurplusValidDays();
        logger.info("===========>任务结束执行-自动修改询价单票据的剩余天数");
    }

    //实现代码
    public void autoUpdateInquireSurplusValidDays() {
        //查询所有待报价和竞价中的询价
        List<BusinessTicketInquire> businessTicketInquireList = businessTicketInquireMapper.selectList(new QueryWrapper<>());
        if (ListUtil.isEmpty(businessTicketInquireList)) {
            logger.info("----------->需要更新剩余时间的询价单数量:0");
            return;
        }
        //过滤掉剩余天数为0的
        businessTicketInquireList = businessTicketInquireList.stream().filter(businessTicketInquire ->
            businessTicketInquire.getSurplusValidDays().intValue() != 0
        ).collect(Collectors.toList());

        logger.info("----------->需要更新剩余时间的询价单数量:{}",businessTicketInquireList == null ? 0:businessTicketInquireList.size());
        if (ListUtil.isEmpty(businessTicketInquireList)) {
            return;
        }

        businessTicketInquireList.stream().forEach(businessTicketInquire -> {
            int surplusValidDays = DateUtil.getTimeDistance(new Date(),DateUtil.getLastDateTimeForDate(businessTicketInquire.getMaturityDate()));
            if (surplusValidDays < 0) surplusValidDays = 0;
            businessTicketInquire.setSurplusValidDays(surplusValidDays);
        });
        businessTicketInquireMapper.updateSurplusValidDaysBatch(businessTicketInquireList);
     }
}
