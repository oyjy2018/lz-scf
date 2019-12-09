package com.zhjs.scfcloud.model.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 操作类型枚举
 * <功能详细描述>
 * Version: 1.0.0, 2019-06-24 13:47
 *
 * @author liuchanghai
 * @create 2019-06-24 13:47
 * @since
 */

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum OperatTypeEnum implements IEnum<String> {

    find("find", "查看"),
    add("add", "新增"),
    commit("commit", "提交"),
    edit("edit", "修改"),
    delete("delete", "删除"),
    export("export", "导出"),
    upload("upload", "上传"),
    download("download", "下载");

    private String operatType;
    private String operatTypeDesc;

    OperatTypeEnum(final String operatType, final String operatTypeDesc) {
        this.operatType = operatType;
        this.operatTypeDesc = operatTypeDesc;
    }

    @Override
    public String getValue() {
        return this.operatType;
    }

    @JsonValue
    public String operatTypeDesc(){
        return this.operatTypeDesc;
    }
}
