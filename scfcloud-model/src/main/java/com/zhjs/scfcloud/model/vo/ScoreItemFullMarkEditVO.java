package com.zhjs.scfcloud.model.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ScoreItemFullMarkEditVO {

    /**
     * 满分值
     * full_mark
     */
    @NotNull(message = "满分值不能为空")
    private String fullMark;

}
