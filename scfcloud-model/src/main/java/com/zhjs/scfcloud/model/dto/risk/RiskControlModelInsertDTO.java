package com.zhjs.scfcloud.model.dto.risk;

import com.zhjs.scfcloud.model.annotation.IsLong;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 风控模型列 入参
 */

@Data
public class RiskControlModelInsertDTO  {
    //模型名
    @NotBlank(message = "模型名称不能为空")
    @Size(max = 150,message = "模型名称长度不能超过150位")
    private String modelName;

    //所属平台id
    @NotBlank(message = "所属平台id不能为空")
    @IsLong(message = "平台id必须为整数")
    private String systemId;

    //所属业务ID
    @NotBlank(message = "所属业务ID不能为空")
    @IsLong(message = "所属业务ID必须为整数")
    private String businessTypeId;

    //所属业务
    @NotBlank(message = "所属业务名不能为空")
    @Size(max = 50,message = "所属业务名长度不能超过50位")
    private String businessTypeName;

    //模型说明
    @Size(max = 1500,message = "模型说明长度不能超过1500位")
    private String modelExplain;

    private Long userId;

    private Long companyId;

}
