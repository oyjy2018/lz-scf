package com.zhjs.scfcloud.util.config;

import org.mybatis.generator.api.FullyQualifiedTable;
import org.mybatis.generator.api.GeneratedXmlFile;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.plugins.UnmergeableXmlMappersPlugin;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

/**
 * @author: dailongting
 * @date:2019/6/6 17:55
 */
public class MyPluginAdapter extends UnmergeableXmlMappersPlugin {


    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private void addModelClassJavaDocLine(TopLevelClass topLevelClass, IntrospectedTable introspectedTable){
        topLevelClass.addImportedType("lombok.Data");
        topLevelClass.addImportedType("lombok.experimental.Accessors");
        topLevelClass.addImportedType("com.baomidou.mybatisplus.annotation.TableName");

        topLevelClass.addAnnotation("@Data");
        topLevelClass.addAnnotation("@Accessors(chain = true)");
        topLevelClass.addAnnotation("@TableName(\""+introspectedTable.getFullyQualifiedTable().getIntrospectedTableName()+"\")");

        topLevelClass.addJavaDocLine("/**");
        topLevelClass.addJavaDocLine(" * @author:"+this.properties.getProperty("operationOf"));
        topLevelClass.addJavaDocLine(" * @date:"+this.dateFormat.format(new Date()));
        topLevelClass.addJavaDocLine(" */");
    }

    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        addModelClassJavaDocLine(topLevelClass,introspectedTable);
        return true;
    }

    @Override
    public boolean modelRecordWithBLOBsClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        addModelClassJavaDocLine(topLevelClass,introspectedTable);
        return true;
    }

    @Override
    public boolean modelSetterMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        return false;
    }

    @Override
    public boolean modelGetterMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        return false;
    }

    @Override
    public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        interfaze.addImportedType(new FullyQualifiedJavaType("com.baomidou.mybatisplus.core.mapper.BaseMapper"));
        interfaze.addImportedType(new FullyQualifiedJavaType("com.zhjs.scfcloud.model.entity."+introspectedTable.getFullyQualifiedTable().getDomainObjectName()));
        interfaze.addSuperInterface(new FullyQualifiedJavaType("BaseMapper<"+introspectedTable.getFullyQualifiedTable().getDomainObjectName()+">"));

        interfaze.addJavaDocLine("/**");
        interfaze.addJavaDocLine(" * "+introspectedTable.getRemarks()+" Mapper 接口");
        interfaze.addJavaDocLine(" * @author:"+this.properties.getProperty("operationOf"));
        interfaze.addJavaDocLine(" * @date:"+this.dateFormat.format(new Date()));
        interfaze.addJavaDocLine(" */");
        return true;
    }

    @Override
    public boolean sqlMapSelectAllElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean sqlMapGenerated(GeneratedXmlFile sqlMap, IntrospectedTable introspectedTable) {
        return super.sqlMapGenerated(sqlMap, introspectedTable);
    }

    @Override
    public List<GeneratedXmlFile> contextGenerateAdditionalXmlFiles(IntrospectedTable introspectedTable) {
        return super.contextGenerateAdditionalXmlFiles(introspectedTable);
    }

    @Override
    public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {
        System.out.println(introspectedTable.getFullyQualifiedTable());
        return super.sqlMapDocumentGenerated(document, introspectedTable);
    }

    @Override
    public boolean clientInsertMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean clientInsertSelectiveMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean clientDeleteByPrimaryKeyMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean clientSelectByPrimaryKeyMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean clientUpdateByPrimaryKeySelectiveMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean clientUpdateByPrimaryKeyWithoutBLOBsMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean sqlMapInsertElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return false;
    }

    /**
     * 去掉XML的InsertSelective
     * @param element
     * @param introspectedTable
     * @return
     */
    @Override
    public boolean sqlMapInsertSelectiveElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return false;
    }

    /**
     * 去掉XML的DeleteByPrimaryKey
     * @param element
     * @param introspectedTable
     * @return
     */
    @Override
    public boolean sqlMapDeleteByPrimaryKeyElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return false;
    }

    /**
     * 去掉XML的SelectByPrimaryKey
     * @param element
     * @param introspectedTable
     * @return
     */
    @Override
    public boolean sqlMapSelectByPrimaryKeyElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return false;
    }

    /**
     * 去掉XML的UpdateByPrimaryKeySelective
     * @param element
     * @param introspectedTable
     * @return
     */
    @Override
    public boolean sqlMapUpdateByPrimaryKeySelectiveElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return false;
    }

    /**
     * 去掉XML的UpdateByPrimaryKey
     * @param element
     * @param introspectedTable
     * @return
     */
    @Override
    public boolean sqlMapUpdateByPrimaryKeyWithoutBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return false;
    }

    /**
     * 去掉XML的ResultMap
     * @param element
     * @param introspectedTable
     * @return
     */
    @Override
    public boolean sqlMapResultMapWithoutBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return false;
    }
}
