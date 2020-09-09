package cc.yao01.rabbit.generator.config.dbloader;

import cc.yao01.rabbit.generator.datamodel.ColumnModel;
import cc.yao01.rabbit.generator.datamodel.TableModel;
import cc.yao01.rabbit.generator.utils.StringUtil;
import lombok.Data;

import java.sql.JDBCType;

/**
 * @author 姚华成
 * @date 2017-10-19
 */
@Data
public class IntrospectedColumn {
    private static JavaTypeResolver javaTypeResolver = new JavaTypeResolver();
    private IntrospectedTable table;
    private String actualColumnName;
    private int jdbcType;
    private boolean nullable;
    private int length;
    private int scale;
    private boolean identity;
    private boolean isSequenceColumn;
    private boolean isColumnNameDelimited;
    private IntrospectedTable introspectedTable;
    private String remarks;
    private String defaultValue;
    private boolean isAutoIncrement;
    private boolean isGeneratedColumn;
    private boolean isGeneratedAlways;

    private String getJdbcTypeName() {
        return JDBCType.valueOf(jdbcType).getName();
    }

    ColumnModel genColumnModel(TableModel tableModel) {
        ColumnModel columnModel = new ColumnModel(tableModel);
        columnModel.setColumnName(getActualColumnName());
        columnModel.setColumnNameCap(getActualColumnName().toUpperCase());
        columnModel.setJdbcType(getJdbcTypeName());
        columnModel.setColumnLength(getLength());
        columnModel.setJavaType(javaTypeResolver.calculateJavaType(this));
        columnModel.setJavaField(StringUtil.getCamelCaseString(getActualColumnName(), false));
        columnModel.setJavaFieldFirstCap(StringUtil.getCamelCaseString(getActualColumnName(), true));
        columnModel.setRemarks(getRemarks());
        return columnModel;
    }

}
