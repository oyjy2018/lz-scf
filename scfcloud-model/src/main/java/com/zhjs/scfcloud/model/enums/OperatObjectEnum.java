package com.zhjs.scfcloud.model.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 操作对象枚举
 * <功能详细描述>
 * Version: 1.0.0, 2019-06-20 19:09
 *
 * @author liuchanghai
 * @create 2019-06-20 19:09
 * @since
 */

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum OperatObjectEnum implements IEnum<String> {

    credit("credit", "授信审批"),
    borrowing("borrowing", "借款审批"),
    file("file", "附件");

    private String operatObject;
    private String operatObjectCN;

    OperatObjectEnum(final String operatObject, final String operatObjectCN) {
        this.operatObject = operatObject;
        this.operatObjectCN = operatObjectCN;
    }

    @Override
    public String getValue() {
        return this.operatObject;
    }

    @JsonValue
    public String operatObjectCN(){
        return this.operatObjectCN;
    }
}
