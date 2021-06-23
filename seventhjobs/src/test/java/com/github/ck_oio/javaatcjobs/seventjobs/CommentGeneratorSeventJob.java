package com.github.ck_oio.javaatcjobs.seventjobs;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.config.PropertyRegistry;
import org.mybatis.generator.internal.DefaultCommentGenerator;
import org.mybatis.generator.internal.util.StringUtility;
import org.springframework.util.StringUtils;

import java.util.Properties;

import static org.mybatis.generator.internal.util.StringUtility.isTrue;

public class CommentGeneratorSeventJob extends DefaultCommentGenerator {
    // 压制生成所有注释
    private boolean suppressAllComment;
    // 添加表中列的注释
    private boolean addRemarkComment;

    @Override
    public void addConfigurationProperties(Properties properties) {
        super.addConfigurationProperties(properties);
        suppressAllComment = isTrue(properties.getProperty(PropertyRegistry.COMMENT_GENERATOR_SUPPRESS_ALL_COMMENTS));
        addRemarkComment = isTrue(properties.getProperty(PropertyRegistry.COMMENT_GENERATOR_ADD_REMARK_COMMENTS));
    }

    @Override
    public void addFieldComment(Field field, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {
        if (suppressAllComment)
            return;
        field.addJavaDocLine("/*");
        final String remarks = introspectedColumn.getRemarks();
        if (addRemarkComment && StringUtility.stringHasValue(remarks)) {
            final String[] remarkLines= remarks.split(System.getProperty("line.separator"));
            for(var remarkLine:remarkLines)
                field.addJavaDocLine("* " + remarkLine);
        }
        // 添加列名注释
        field.addJavaDocLine(" * " + introspectedColumn.getActualColumnName());
        field.addJavaDocLine("*/");
    }
}
