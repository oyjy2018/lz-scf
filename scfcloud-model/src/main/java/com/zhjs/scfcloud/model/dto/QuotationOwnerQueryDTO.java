package com.zhjs.scfcloud.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 资方-我的报价单列表查询DTO
 * @author weijie.chen
 */
@Data
public class QuotationOwnerQueryDTO {
    /**
     * 报价公司Id
     */
    private Long quotationCompanyId;

    /**
     * 询价公司名称
     */
    private String inquireCompanyName;

    /**
     * 报价状态(1:待接受报价;2:接受报价;3:不接受报价;4:已撤回)
     * status
     */
    private Integer quotationStatus;

    /**
     *  报价开始时间
     * create_time
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date quotationCreateStartTime;

    /**
     *  报价结束时间
     * create_time
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date quotationCreateEndTime;

    /**
     *
     * 报价单id
     */
    private Long quotationId;

    /**
     * 分页数
     */
    @NotNull(message = "分页数不能为空")
    private Long current;

    /**
     * 分页大小
     */
    @NotNull(message = "分页大小不能为空")
    private Long size;
}
