package com.zhjs.scfcloud.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author:liuchanghai
 * @date:2019-06-21 16:32
 */
@Data
@Accessors(chain = true)
@TableName("scf_operate_log")
public class OperateLog {
    /**
     * id
     * id
     */
    private Long id;

    /**
     * 公司ID
     * company_id
     */
    private Long companyId;

    /**
     * 操作的业务单据ID
     * business_id
     */
    private Long businessId;

    /**
     * 业务类型ID
     * business_type_id
     */
    private Long businessTypeId;

    /**
     * 操作时间
     * operate_time
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date operateTime;

    /**
     * 操作类型(find查看;add新增;edit修改;delete删除;export导出;upload上传;download下载)
     * operat_type
     */
    private String operatType;

    /**
     * 操作对象(credit授信审批;borrowing借款审批;file附件)
     * operat_object
     */
    private String operatObject;

    /**
     * 操作内容
     * operate_content
     */
    private String operateContent;

    /**
     * 操作用户ID
     * operate_user_id
     */
    private Long operateUserId;

    /**
     * 操作用户名称
     * operate_user_name
     */
    private String operateUserName;
}