package com.zhjs.scfcloud.util.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 公司员工状态
 * @author: weijie.chen
 * @date:2019/7/6 15:00
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum CompanyUserEnum implements IEnum<Integer> {

    status_0(0,"待激活"),
    status_1(1,"在职"),
    status_2(2,"离职");

    private Integer status;
    private String desc;

    CompanyUserEnum(final Integer status, final String desc) {
        this.status = status;
        this.desc = desc;
    }

    @Override
    public Integer getValue() {
        return this.status;
    }

    @JsonValue
    public String getDesc(){
        return this.desc;
    }

}
