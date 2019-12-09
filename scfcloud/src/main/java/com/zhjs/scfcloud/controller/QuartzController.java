package com.zhjs.scfcloud.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhjs.scfcloud.feign.QuartzFeignService;
import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.BaseIdDTO;
import com.zhjs.scfcloud.util.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 定时任务管理
 */
@RestController
@RequestMapping("/quartz/")
public class QuartzController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private QuartzFeignService quartzFeignService;

    //定时任务列表
    @PostMapping("jobList")
    public Result jobList(@RequestBody Page page){
        logger.info("subject:{}","任务列表");
        return quartzFeignService.jobList(page);
    }

    //暂停某个任务
    @PostMapping("pauseJob")
    public Result pauseJob(@RequestBody BaseIdDTO dto){
        logger.info("subject:{},dto:{}","暂停某个任务",dto);
        if (StringUtil.isEmpty(dto.getId())) return Result.failure("任务id不能为空");
        return quartzFeignService.pauseJob(dto.getId().intValue());
    }

    //暂停所有任务
    @PostMapping("pauseAllJob")
    public Result pauseAllJob() {
        logger.info("subject:{}", "暂停所有任务");
        return quartzFeignService.pauseAllJob();
    }

    //启动某个任务
    @PostMapping("startJob")
    public Result startJob(@RequestBody BaseIdDTO dto){
        logger.info("subject:{},dto:{}","启动某个任务",dto);
        if (StringUtil.isEmpty(dto.getId())) return Result.failure("任务id不能为空");
        return quartzFeignService.startJob(dto.getId().intValue());
    }

    //启动所有任务
    @PostMapping("startAllJob")
    public Result startAllJob(){
        logger.info("subject:{}","启动所有任务");
        return quartzFeignService.startAllJob();
    }

    //修改运行时间
    @PostMapping("updateCron")
    public Result updateCron(@RequestParam Integer id,@RequestParam String cron){
        logger.info("subject:{},id:{},cron:{}","启动某个任务",id,cron);
        return quartzFeignService.updateCron(id,cron);
    }
}
