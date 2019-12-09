package com.zhjs.scfcloud.model.vo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * Version: 1.0.0, 2019-06-22 11:35
 *
 * @author liuchanghai
 * @create 2019-06-22 11:35
 * @since
 */

@Data
public class OperateLogVO {

    private Long id;

    /**
     * 操作时间
     */
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date operateTime;

    /**
     * 操作类型(查看;新增;修改;删除;导出;上传;下载)
     */
    private String operatType;

    /**
     * 操作对象(授信审批;借款审批;附件)
     */
    private String operatObject;

    /**
     * 操作内容
     */
    private String operateContent;

    /**
     * 操作用户名称
     */
    private String operateUserName;
}
