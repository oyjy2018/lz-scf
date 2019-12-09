package com.zhjs.scfcloud.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author:liuchanghai
 * @date:2019-07-06 18:36
 */
@Data
@Accessors(chain = true)
@TableName("scf_cfg_business_attr")
public class BusinessAttr {
    /**
     * 
     * id
     */
    private Long id;

    /**
     * 业务类型ID
     * business_type_id
     */
    private Long businessTypeId;

    /**
     * 公司ID
     * company_id
     */
    private Long companyId;

    /**
     * 所属表
     * table_name
     */
    private String tableName;

    /**
     * 列名
     * column_name
     */
    private String columnName;

    /**
     * 列展示名(页面名)
     * column_ch
     */
    private String columnCh;

    /**
     * 是否系统字段
     * has_system
     */
    private Integer hasSystem;

    /**
     * 字段类型
     * column_type
     */
    private String columnType;

    /**
     * 是否是封闭式字段（1：是；0：否）
     * has_closed_type
     */
    private Integer hasClosedType;

    /**
     * 字段单位
     * column_unit
     */
    private String columnUnit;

    /**
     * 字段分组（同一分组的字段需要一起展示）
     * column_group
     */
    private String columnGroup;

    /**
     * 是否出现多次
     * has_plurality
     */
    private Integer hasPlurality;

    /**
     * 是否只读
     * has_read_only
     */
    private Integer hasReadOnly;

    /**
     * 字段提示
     * column_tip
     */
    private String columnTip;

    /**
     * 字段错误提示消息
     * column_err_msg
     */
    private String columnErrMsg;

    /**
     * 是否必填
     * required
     */
    private Integer required;

    /**
     * 字段校验公式
     * verify_formula
     */
    private String verifyFormula;

    /**
     * 文件上传数量限制
     * upload_limit
     */
    private Integer uploadLimit;

    /**
     * 归类ID
     * classify_id
     */
    private Long classifyId;

    /**
     * 前置业务属性ID
     * pre_column_id
     */
    private Long preColumnId;

    /**
     * 前置业务属性值
     * pre_column_value
     */
    private String preColumnValue;

    /**
     * 下级业务属性ID
     * junior_column_id
     */
    private Long juniorColumnId;

    /**
     * 默认值类型
     * default_value_type
     */
    private String defaultValueType;

    /**
     * 默认值
     * default_value
     */
    private String defaultValue;

    /**
     * 是否换行
     * has_br
     */
    private Integer hasBr;

    /**
     * 业务创建页面是否需要
     * has_create_need
     */
    private Integer hasCreateNeed;

    /**
     * 业务创建页面是否可见
     * has_create_visible
     */
    private Integer hasCreateVisible;

    /**
     * 业务编辑页面是否可见
     * has_edit_visible
     */
    private Integer hasEditVisible;

    /**
     * 业务查看页面是否可见
     * has_view_visible
     */
    private Integer hasViewVisible;

    /**
     * 业务审核页面是否可以见
     * has_audit_visible
     */
    private Integer hasAuditVisible;

    /**
     * 排序
     * sort
     */
    private Integer sort;

    /**
     * 字段备注
     * remark
     */
    private String remark;

    /**
     * 状态（1：有效；0：无效）
     * status
     */
    private Integer status;

    /**
     * 创建时间
     * create_time
     */
    private Date createTime;

    /**
     * 创建人
     * create_by
     */
    private Long createBy;

    /**
     * 更新时间
     * update_time
     */
    private Date updateTime;

    /**
     * 更新人
     * update_by
     */
    private Long updateBy;
}