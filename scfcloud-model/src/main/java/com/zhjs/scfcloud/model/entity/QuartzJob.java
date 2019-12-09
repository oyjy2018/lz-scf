package com.zhjs.scfcloud.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author:dailongting
 * @date:2019-08-05 11:24
 */
@Data
@Accessors(chain = true)
@TableName("scf_cfg_quartz_job")
public class QuartzJob {
    /**
     * 
     * id
     */
    private Integer id;

    /**
     * 任务id
     * job_name
     */
    private String jobName;

    /**
     * 任务描述
     * job_desc
     */
    private String jobDesc;

    /**
     * 任务类路径
     * class_path
     */
    private String classPath;

    /**
     * 任务cron表达式
     * job_cron
     */
    private String jobCron;

    /**
     * 运行状态（1：运行；0：停止）
     * run_status
     */
    private Integer runStatus;

    /**
     * 删除状态（1：已删；0：未删）
     * is_del
     */
    private Integer isDel;

    /**
     * 
     * create_time
     */
    private Date createTime;

    /**
     * 
     * create_by
     */
    private Long createBy;

    /**
     * 
     * update_time
     */
    private Date updateTime;

    /**
     * 
     * update_by
     */
    private Long updateBy;
}