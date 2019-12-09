package com.zhjs.scfcloud.model.dto.file;

import com.zhjs.scfcloud.model.dto.FileDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author: dailongting
 * @date:2019/6/25 17:58
 */
@Data
public class AddFileDTO {
    @ApiModelProperty("公司ID")
    private Long companyId;

    @ApiModelProperty("业务类型ID")
    @NotNull(message = "业务类型ID不能为空")
    private Long businessTypeId;

    @ApiModelProperty("业务ID")
    @NotNull(message = "业务ID不能为空")
    private Long businessId;

    @ApiModelProperty("扭转前流程CODE")
    private String beforeFlow;

    @ApiModelProperty("扭转后流程CODE")
    private String afterFlow;

    @ApiModelProperty("文件CODE")
    private String fileCode;

    private FileDTO file;

}
