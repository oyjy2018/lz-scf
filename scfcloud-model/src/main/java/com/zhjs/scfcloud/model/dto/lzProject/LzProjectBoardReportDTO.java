package com.zhjs.scfcloud.model.dto.lzProject;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 深华项目看板 折线图报表 入参
 */

@Data
public class LzProjectBoardReportDTO extends Page {

    //项目合同编号
    @NotBlank(message = "合同号不能为空")
    private String contractNo;

    //开始日期
    @NotBlank(message = "开始日期不能为空")
    private String beginDate;

    @NotBlank(message = "结束日期不能为空")
    private String endDate;

}
