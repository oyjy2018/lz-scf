package com.zhjs.scfcloud.model.dto.lzProject;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author: dailongting
 * @date:2019/11/4 17:13
 */
@Data
public class FindProjectAssetDTO {
    /**
     * 项目ID（项目合同号）
     */
    @NotBlank(message = "合同号不能为空")
    private String contractNo;

    @NotBlank(message = "开始时间")
    private String startDate;

    @NotBlank(message = "结束时间")
    private String endDate;

}
