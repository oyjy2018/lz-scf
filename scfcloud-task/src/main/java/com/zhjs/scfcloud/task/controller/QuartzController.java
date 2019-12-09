package com.zhjs.scfcloud.task.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.task.service.JobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/quartz/")
public class QuartzController {
    private static final Logger logger = LoggerFactory.getLogger(QuartzController.class);
    @Resource
    private JobService jobService;

    //定时任务列表
    @PostMapping("jobList")
    public Result jobList(@RequestBody Page page){
        logger.info("subject:{}","任务列表");
        return jobService.jobList(page);
    }

    //暂停某个任务
    @PostMapping("pauseJob")
    public Result pauseJob(@RequestParam Integer id){
        logger.info("subject:{},id:{}","暂停某个任务",id);
        return jobService.pauseJob(id);
    }

    //暂停所有任务
    @PostMapping("pauseAllJob")
    public Result pauseAllJob() {
        logger.info("subject:{}", "暂停所有任务");
        return jobService.pauseAllJob();
    }

    //启动某个任务
    @PostMapping("startJob")
    public Result startJob(@RequestParam Integer id){
        logger.info("subject:{},id:{}","启动某个任务",id);
        return jobService.startJob(id);
    }

    //启动所有任务
    @PostMapping("startAllJob")
    public Result startAllJob(){
        logger.info("subject:{}","启动所有任务");
        return jobService.startAllJob();
    }

    //修改运行时间
    @PostMapping("updateCron")
    public Result updateCron(@RequestParam Integer id,@RequestParam String cron){
        logger.info("subject:{},id:{},cron:{}","启动某个任务",id,cron);
        return jobService.updateCron(id,cron);
    }

}
