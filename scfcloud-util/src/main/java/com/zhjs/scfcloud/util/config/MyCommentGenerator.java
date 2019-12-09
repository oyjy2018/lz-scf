package com.zhjs.scfcloud.util.config;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.internal.DefaultCommentGenerator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 * @author: dailongting
 * @date:2019/6/5 14:48
 */
public class MyCommentGenerator extends DefaultCommentGenerator {

    private String operationOf;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private CompilationUnit compilationUnit;

    public MyCommentGenerator(){
        super();
    }

    @Override
    public void addRootComment(XmlElement rootElement) {
        System.out.println(rootElement.getFormattedContent(5));
        super.addRootComment(rootElement);
    }

    @Override
    public void addComment(XmlElement xmlElement) {

    }

    @Override
    public void addGeneralMethodComment(org.mybatis.generator.api.dom.java.Method method, IntrospectedTable introspectedTable) {

    }

    @Override
    public void addFieldComment(Field field, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {
//        super.addFieldComment(field, introspectedTable, introspectedColumn);

        field.addJavaDocLine("/**");
        field.addJavaDocLine(" * "+introspectedColumn.getRemarks());
        field.addJavaDocLine(" * "+introspectedColumn.getActualColumnName());
        field.addJavaDocLine(" */");
    }

    @Override
    public void addConfigurationProperties(Properties properties) {
        super.addConfigurationProperties(properties);
        this.operationOf = properties.getProperty("operationOf");
    }
}
