package com.zhjs.scfcloud.model.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 授信审核结果枚举
 * <功能详细描述>
 * Version: 1.0.0, 2019-06-19 14:22
 *
 * @author liuchanghai
 * @create 2019-06-19 14:22
 * @since
 */

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum CreditAuditResultEnum implements IEnum<Integer> {

    status1(1, "通过"),
    status2(2, "退回"),
    status3(3, "拒绝");

    private Integer status;
    private String desc;

    CreditAuditResultEnum(final Integer status, final String desc) {
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
