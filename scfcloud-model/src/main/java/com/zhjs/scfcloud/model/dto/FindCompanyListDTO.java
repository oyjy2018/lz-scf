package com.zhjs.scfcloud.model.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class FindCompanyListDTO{

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
    /**
     * 公司名称
     */
    private String companyName;
    /**
     * 公司状态列表(0：待激活；1：已启用；2：已禁用);
     */
    private List<Integer> status;

}
