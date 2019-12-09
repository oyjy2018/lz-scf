package com.zhjs.scfcloud.task.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.entity.QuartzJob;
import com.zhjs.scfcloud.model.mapper.QuartzJobMapper;
import com.zhjs.scfcloud.task.config.QuartzScheduler;
import com.zhjs.scfcloud.task.service.JobService;
import com.zhjs.scfcloud.util.util.ListUtil;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class JobServiceImpl extends ServiceImpl<QuartzJobMapper, QuartzJob> implements JobService  {
    @Resource
    private QuartzJobMapper quartzJobMapper;
    @Resource
    private Scheduler scheduler;

    /**
     * 获取定时任务列表
     * @param page
     * @return
     */
    @Override
    public Result jobList(Page page) {
        page.setRecords(quartzJobMapper.getJobList(page));
        return Result.successPage(page);
    }

    /**
     * 暂停某个任务
     * @param id
     * @return
     */
    @Override
    @Transactional
    public Result pauseJob(Integer id) {
        //修改数据库中的运行状态
        QuartzJob job = quartzJobMapper.selectById(id);
        if (job == null) return Result.failure("任务信息不存在");
        job.setRunStatus(0);//将运行状态改为停止
        quartzJobMapper.updateById(job);

        String jobName = job.getJobName();

        //在quartz中暂停任务
        try {
            QuartzScheduler.pauseJob(scheduler,jobName);
        } catch (SchedulerException e) {
            throw new RuntimeException("暂停任务异常");
        }
        return Result.success();
    }

    /**
     * 暂停所有任务
     * @return
     */
    @Override
    @Transactional
    public Result pauseAllJob() {
        //停止数据库所有运行中的任务
        int effect = quartzJobMapper.pauseAllJob();
        if (effect == 0) return Result.failure("无运行中的定时任务");

        //在quartz中暂停任务
        try {
            QuartzScheduler.pauseAllJob(scheduler);
        } catch (SchedulerException e) {
            e.printStackTrace();
            throw new RuntimeException("暂停所有任务异常");
        }
        return Result.success();
    }

    /**
     * 启动某个任务
     * @param id
     * @return
     */
    @Override
    @Transactional
    public Result startJob(Integer id) {
        //修改数据库中的运行状态
        QuartzJob job = quartzJobMapper.selectById(id);
        if (job == null) return Result.failure("任务信息不存在");
        job.setRunStatus(1);//将运行状态改为运行
        quartzJobMapper.updateById(job);

        String jobName = job.getJobName();
        //检查是否存在
        if (QuartzScheduler.checkTaskJob(scheduler,jobName)) {
            try {
                QuartzScheduler.resumeJob(scheduler,jobName);
            } catch (SchedulerException e) {
                throw new RuntimeException("恢复定时任务异常");
            }
        }else {
            Class clz = null;
            try {
                clz = Class.forName(job.getClassPath());
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("转换定时任务类异常");
            }
            String jobCron = job.getJobCron();
            QuartzScheduler.addJob(scheduler,jobName,clz,jobCron);
        }
        return Result.success();
    }

    /**
     * 启动所有任务
     * @return
     */
    @Override
    @Transactional
    public Result startAllJob() {
        List<QuartzJob> jobList = quartzJobMapper.selectList(new QueryWrapper<QuartzJob>().eq("run_status",0).eq("is_del",0));
        if (ListUtil.isEmpty(jobList)) return Result.failure("无停止中的定时任务");

        //修改数据库状态
        quartzJobMapper.startAllJob();

        //修改quartz的运行状态

        //唤醒所有被暂停的任务
        try {
            QuartzScheduler.resumeAllJob(scheduler);
        } catch (SchedulerException e) {
            throw new RuntimeException("唤醒所有被暂停的任务时异常");
        }

        //新任务加入quartz
        for (QuartzJob quartzJob:jobList) {
            String jobName = quartzJob.getJobName();
            //检查是否存在
            if (!QuartzScheduler.checkTaskJob(scheduler,jobName)) {
                Class clz = null;
                try {
                    clz = Class.forName(quartzJob.getClassPath());
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException("转换定时任务类异常");
                }
                String jobCron = quartzJob.getJobCron();
                QuartzScheduler.addJob(scheduler,jobName,clz,jobCron);
            }
        }

        return Result.success();
    }

    //修改运行时间
    @Override
    @Transactional
    public Result updateCron(Integer id, String cron) {
        QuartzJob job = quartzJobMapper.selectById(id);
        if (job == null) return Result.failure("定时任务数据不存在");
        if (cron.equals(job.getJobCron())) return Result.failure("运行时间未改变");

        job.setJobCron(cron);
        quartzJobMapper.updateById(job);//更新数据库
        String jobName = job.getJobName();
        //未添加到quartz 直接返回成为你刚刚
        if (!QuartzScheduler.checkTaskJob(scheduler,jobName)){
            return Result.success();
        }
        //修改quartz中的运行时间
        try {
            if (!QuartzScheduler.modifyJob(scheduler,jobName,cron)){
                throw new RuntimeException("修改运行时间失败");
            }
        } catch (SchedulerException e) {
            throw new RuntimeException("修改运行时间异常");
        }
        return Result.success();
    }
}
