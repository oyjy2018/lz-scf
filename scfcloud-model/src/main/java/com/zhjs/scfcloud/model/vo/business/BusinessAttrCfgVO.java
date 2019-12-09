package com.zhjs.scfcloud.model.vo.business;

import com.zhjs.scfcloud.model.entity.BusinessAttrVal;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.Transient;

import java.util.List;
import java.util.Map;

/**
 * @author: dailongting
 * @date:2019/6/12 18:10
 */
@Data
public class BusinessAttrCfgVO {

    @ApiModelProperty("业务属性ID")
    private Long businessAttrId;

    @ApiModelProperty("业务类型ID")
    private Long businessTypeId;

    @ApiModelProperty("公司ID")
    private Long companyId;

    @ApiModelProperty("业务属性字段名称")
    private String columnName;

    @ApiModelProperty("业务属性字段中文名")
    private String columnCh;

    @ApiModelProperty("业务属性字段展示类型")
    private String columnType;

    @ApiModelProperty("是否是封闭式字段")
    private Integer hasClosedType;

    @ApiModelProperty("业务属性字段单位")
    private String columnUnit;

    @ApiModelProperty("业务属性字段分组")
    private String columnGroup;

    @ApiModelProperty("是否可出现多次")
    private Integer hasPlurality;

    @ApiModelProperty("是否只读")
    private Integer hasReadOnly;

    @ApiModelProperty("字段提示")
    private String columnTip;

    @ApiModelProperty("字段错误提示消息")
    private String columnErrMsg;

    @ApiModelProperty("是否必填")
    private Integer required;

    @ApiModelProperty("字段校验公式")
    private String verifyFormula;

    @ApiModelProperty("字段分类")
    private String classifyName;

    @ApiModelProperty("前置字段ID")
    private Long preColumnId;

    @ApiModelProperty("前置字段值")
    private String preColumnValue;

    @ApiModelProperty("下级字段ID")
    private Long juniorColumnId;

    @ApiModelProperty("默认值类型")
    private String defaultValueType;

    @ApiModelProperty("默认值")
    private String defaultValue;

    @ApiModelProperty("是否换行")
    private Integer hasBr;

    @ApiModelProperty("是否创建/草稿页面可用")
    private Integer hasCreateNeed;

    @ApiModelProperty("是否创建/草稿页面可见")
    private Integer hasCreateVisible;

    @ApiModelProperty("是否编辑页面可见")
    private Integer hasEditVisible;

    @ApiModelProperty("是否查看页面可见")
    private Integer hasViewVisible;

    @ApiModelProperty("是否审批页面可见")
    private Integer hasAuditVisible;

    @ApiModelProperty("有效状态")
    private Integer status;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("备注")
    private String remark;

    /**
     * 授信项目ID
     * PS：当字段为项目表（credit_item）时使用
     */
    @Transient
    private Long projectId;

    /**
     * 字段封闭式值集合
     */
    @Transient
    private Map<String, Object> attrValObj;

    /**
     * 字段值
     */
    @Transient
    private String columnVal;

    public static BusinessAttrCfgVO newInitInstance(Long id, String columnName,String columnCh,String columnType,
                                                    String columnGroup, String classifyName,String defaultValueType,
                                                    String defaultValue,Long projectId){
        BusinessAttrCfgVO vo = new BusinessAttrCfgVO();
        vo.setBusinessAttrId(id);
        vo.setColumnName(columnName);
        vo.setColumnCh(columnCh);
        vo.setColumnType(columnType);
        vo.setColumnGroup(columnGroup);
        vo.setClassifyName(classifyName);
        vo.setDefaultValueType(defaultValueType);
        vo.setDefaultValue(defaultValue);
        vo.setProjectId(projectId);
        return vo;
    }

}
