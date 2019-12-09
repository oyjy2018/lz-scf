package com.zhjs.scfcloud.task.job;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhjs.scfcloud.model.entity.BusinessTicketInquire;
import com.zhjs.scfcloud.model.mapper.BusinessTicketInquireMapper;
import com.zhjs.scfcloud.model.mapper.UserMapper;
import com.zhjs.scfcloud.util.VO.EmailVO;
import com.zhjs.scfcloud.util.enums.BusinessTicketEnum;
import com.zhjs.scfcloud.util.util.DateUtil;
import com.zhjs.scfcloud.util.util.EmailTool;
import com.zhjs.scfcloud.util.util.FileUtil;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.quartz.QuartzJobBean;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 自动修改询价单状态(已过报价截止日期改为报价已截止）
 */
public class AutoUpdateInquireStatusJob extends QuartzJobBean {
    private static final Logger logger = LoggerFactory.getLogger(AutoUpdateInquireStatusJob.class);

    @Resource
    private BusinessTicketInquireMapper businessTicketInquireMapper;
    @Resource
    private UserMapper userMapper;
    @Autowired
    private EmailTool emailTool;
    @Value("${frontEndUrl.bill}")
    private String billFrontEndUrl;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("===========>任务开始执行-自动修改询价单状态");
        autoUpdateInquireStatus();
        logger.info("===========>任务结束执行-自动修改询价单状态");
    }

    //实现代码
    public void autoUpdateInquireStatus() {
        //查询所有待报价和竞价中且发布截止日期大于当前时间的询价
//        int effect = businessTicketInquireMapper.updateOverdueInquireStatus(DateUtil.getTodayDateStr("yyyy-MM-dd"));
//        logger.info("----------->成功更新已经过期的询价单状态的数量:{}",effect);
        QueryWrapper<BusinessTicketInquire> inquireQueryWrapper = new QueryWrapper<>();
        inquireQueryWrapper.in("status",1,2).lt("expiration_date",new Date());
        List<BusinessTicketInquire> inquireList = businessTicketInquireMapper.selectList(inquireQueryWrapper);
        inquireList.forEach(inquire -> {
            inquire.setUpdateTime(new Date());
            inquire.setStatus(BusinessTicketEnum.InquireStatus.inquire_status_4.getStatus());
            businessTicketInquireMapper.updateById(inquire);
            //发送邮件
            sendEmail(inquire);
        });
     }

    /**
     * 发送邮件
     * @param inquire
     */
    private void sendEmail(BusinessTicketInquire inquire){
         EmailVO emailVO = new EmailVO();
         emailVO.setTo(new String[]{userMapper.selectById(inquire.getPublishPersonId()).getEmail()});
         emailVO.setTemplate("TicketTemplate.html");
         emailVO.setTitle("【领筑票据融资平台】询价已截止提醒");
         Map<String,Object> variables = new HashMap<>();
         variables.put("fileServiceUrl", FileUtil.getFileServiceUrl());
         variables.put("companyName","【"+inquire.getCompanyName()+"】");
         variables.put("firstContent","您好，贵公司的询价单已截止报价【询价单编号："+inquire.getId()+"】：");
         variables.put("billNo",inquire.getBillNo());
         variables.put("accepterName",inquire.getAccepterName());
         variables.put("inquireCompanyName",inquire.getCompanyName());
         variables.put("deadline", DateUtil.format(inquire.getUpdateTime(),"yyyy-MM-dd HH:mm:ss"));
         variables.put("billAmt",inquire.getBillAmt());
         variables.put("secondContent","如需继续发布，请登录平台操作。");
         variables.put("firstUrl", billFrontEndUrl+"/sell/inquiredetail?id="+inquire.getId());
         variables.put("firstUrlContent","点此查看询价详情");
         emailVO.setVariables(variables);
         emailTool.commonSendMail(emailVO);
     }
}
