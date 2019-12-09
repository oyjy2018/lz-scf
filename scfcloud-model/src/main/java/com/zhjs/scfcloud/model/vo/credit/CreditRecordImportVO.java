package com.zhjs.scfcloud.model.vo.credit;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * 授信导入VO
 */
@Data
public class CreditRecordImportVO {

    /**
     * 授信人姓名
     */
    private String customerName;

    /**
     * 身份证号
     */
    private String idCard;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 深华工程系统项目ID
     */
    private String relateProjectId;

    /**
     * 授信金额
     */
    private String creditAmount;

    /**
     * 授信开始日期
     */
    private String creditStartDate;

    /**
     * 授信结束日期
     */
    private String creditEndDate;

    /**
     * 错误信息
     */
    private String errorMessage;


}
