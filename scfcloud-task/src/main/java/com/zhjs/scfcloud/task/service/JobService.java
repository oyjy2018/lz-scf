package com.zhjs.scfcloud.task.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.entity.QuartzJob;

public interface JobService extends IService<QuartzJob> {
    Result jobList(Page page);

    Result pauseJob(Integer id);

    Result pauseAllJob();

    Result startJob(Integer id);

    Result startAllJob();

    Result updateCron(Integer id, String cron);
}
