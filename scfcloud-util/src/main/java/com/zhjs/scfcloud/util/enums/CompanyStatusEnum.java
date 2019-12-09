package com.zhjs.scfcloud.util.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 公司状态枚举类
 * <功能详细描述>
 * Version: 1.0.0, 2019-06-05 13:09
 *
 * @author liuchanghai
 * @create 2019-06-05 13:09
 * @since
 */

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum CompanyStatusEnum implements IEnum<Integer> {
    status0(0, "待激活"),
    status1(1, "已启用"),
    status2(2, "已禁用");

    private Integer status;
    private String desc;

    CompanyStatusEnum(final Integer status, final String desc) {
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
