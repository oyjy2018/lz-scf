package com.zhjs.scfcloud.model.vo.business;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author:oyjy
 * @date:2019-06-13 11:50
 */
@Data
@Accessors(chain = true)
@TableName("scf_cfg_business_work_flow_attr")
public class BusinessWorkFlowAttrExtendVO extends BusinessAttrCfgVO{
    //流程id
    private Long workFlowId;
}