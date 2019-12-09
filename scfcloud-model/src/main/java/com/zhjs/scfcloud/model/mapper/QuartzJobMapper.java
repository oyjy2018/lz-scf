package com.zhjs.scfcloud.model.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhjs.scfcloud.model.entity.QuartzJob;

import java.util.List;

/**
 * 定时任务表 Mapper 接口
 * @author:dailongting
 * @date:2019-08-05 11:24
 */
public interface QuartzJobMapper extends BaseMapper<QuartzJob> {
    int deleteByPrimaryKey(Integer id);

    int insert(QuartzJob record);

    int insertSelective(QuartzJob record);

    QuartzJob selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(QuartzJob record);

    int updateByPrimaryKey(QuartzJob record);

    List getJobList(Page page);

    int pauseAllJob();

    int startAllJob();
}