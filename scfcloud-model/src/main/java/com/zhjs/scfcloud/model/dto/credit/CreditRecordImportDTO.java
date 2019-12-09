package com.zhjs.scfcloud.model.dto.credit;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 授信导入dto
 */
@Data
public class CreditRecordImportDTO {

    @NotBlank(message = "文件地址不能为空")
    private String fileUrl;

    private Long companyId;

    private Long userId;
}
