package com.zhjs.scfcloud.task.init;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhjs.scfcloud.model.entity.QuartzJob;
import com.zhjs.scfcloud.model.mapper.QuartzJobMapper;
import com.zhjs.scfcloud.task.config.QuartzScheduler;
import com.zhjs.scfcloud.util.util.ListUtil;
import org.quartz.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 初始化定时任务
 **/
@Component
public class InitQuartz {

    private static final Logger logger = LoggerFactory.getLogger(InitQuartz.class);


    @Autowired
    private Scheduler scheduler;
    @Resource
    private QuartzJobMapper quartzJobMapper;

    public void initQuartz(){
        List<QuartzJob> jobList = quartzJobMapper.selectList(new QueryWrapper<QuartzJob>().eq("run_status",1).eq("is_del",0));
        logger.info("subject:初始化定时器,size:"+(jobList == null ? 0 : jobList.size()));
        if(ListUtil.isEmpty(jobList))return;
        String jobName = null;
        String jobDesc = null;
        Class clz =  null;
        String jobCron = null;
        for (QuartzJob job : jobList) {
            jobName = job.getJobName();
            jobDesc = job.getJobDesc();
            try{
                clz = Class.forName(job.getClassPath());
            }catch (Exception e){
                logger.error("异常"+job.getClassPath(),e);
                continue;
            }
            jobCron = job.getJobCron();
            logger.info("subject:初始化定时器,jobName:"+jobName+",jobDesc:"+jobDesc+",classPath："+job.getClassPath()+",jobCron:"+jobCron);
            if(!QuartzScheduler.checkTaskJob(scheduler,jobName)){
                QuartzScheduler.addJob(scheduler,jobName,clz,jobCron);
            }
        }
   }
}
