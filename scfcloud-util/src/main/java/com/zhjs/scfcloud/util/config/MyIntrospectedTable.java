package com.zhjs.scfcloud.util.config;

import org.mybatis.generator.api.FullyQualifiedTable;
import org.mybatis.generator.api.GeneratedXmlFile;
import org.mybatis.generator.codegen.mybatis3.IntrospectedTableMyBatis3Impl;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @author: dailongting
 * @date:2019/6/13 17:06
 */
public class MyIntrospectedTable extends IntrospectedTableMyBatis3Impl {

    @Override
    public List<GeneratedXmlFile> getGeneratedXmlFiles() {
        internalAttributes
                .put(
                        InternalAttribute.ATTR_ALIASED_FULLY_QUALIFIED_TABLE_NAME_AT_RUNTIME,
                        this.fullyQualifiedTable.getIntrospectedTableName());
        internalAttributes
                .put(
                        InternalAttribute.ATTR_FULLY_QUALIFIED_TABLE_NAME_AT_RUNTIME,
                        this.fullyQualifiedTable.getIntrospectedTableName());

        return super.getGeneratedXmlFiles();
    }
}
