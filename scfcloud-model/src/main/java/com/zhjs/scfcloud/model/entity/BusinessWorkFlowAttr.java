package com.zhjs.scfcloud.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;

import com.zhjs.scfcloud.model.dto.business.AddWorkFlowAttrDTO;
import com.zhjs.scfcloud.util.util.StringUtil;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author:dailongting
 * @date:2019-06-15 17:57
 */
@Data
@Accessors(chain = true)
@TableName("scf_cfg_business_work_flow_attr")
public class BusinessWorkFlowAttr {
    /**
     * 业务流程属性ID
     * id
     */
    private Long id;

    /**
     * 公司ID
     * company_id
     */
    private Long companyId;

    /**
     * 业务类型ID
     * business_type_id
     */
    private Long businessTypeId;

    /**
     * 业务属性配置ID
     * business_attr_id
     */
    private Long businessAttrId;

    /**
     * 业务流程id
     * work_flow_id
     */
    private Long workFlowId;

    /**
     * 是否必填
     * required
     */
    private Integer required;

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
     * 排序
     * sort
     */
    private Integer sort;

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
     * 
     * status
     */
    private Byte status;

    public boolean settingEqual(AddWorkFlowAttrDTO add){
        if(add == null){
            return false;
        }

        if(this.businessAttrId == null || add.getBusinessAttrId() == null){
            return false;
        }

        if(this.businessAttrId.longValue() != add.getBusinessAttrId().longValue()){
            return false;
        }

        if(this.required == null || add.getRequired() == null){
            return false;
        }

        if(this.required.intValue() != add.getRequired().intValue()){
            return false;
        }

        if(StringUtil.isEmpty(this.defaultValueType) != StringUtil.isEmpty(add.getDefaultValueType())){
            return false;
        }else{
            if(!StringUtil.isEmpty(this.defaultValueType)){
                if(!this.defaultValueType.equals(add.getDefaultValueType())){
                    return false;
                }
            }
        }

        if(StringUtil.isEmpty(this.defaultValue) != StringUtil.isEmpty(add.getDefaultValue())){
            return false;
        }else{
            if(!StringUtil.isEmpty(this.defaultValue)){
                if(!this.defaultValue.equals(add.getDefaultValue())){
                    return false;
                }
            }
        }

        return true;
    }
}