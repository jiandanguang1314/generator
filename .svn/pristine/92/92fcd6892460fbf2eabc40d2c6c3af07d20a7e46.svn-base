package cc.yao01.rabbit.generator.config.dbloader;

import cc.yao01.rabbit.generator.config.TableConfig;
import cc.yao01.rabbit.generator.datamodel.ProjectModel;
import cc.yao01.rabbit.generator.datamodel.TableModel;
import cc.yao01.rabbit.generator.utils.StringUtil;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 姚华成
 * @date 2017-10-19
 */
@Data
public class IntrospectedTable {
    private String tableName;
    private String remarks;
    private String tableType;

    private List<IntrospectedColumn> primaryKeyColumns = new ArrayList<>();
    private List<IntrospectedColumn> baseColumns = new ArrayList<>();
    private List<IntrospectedColumn> blobColumns = new ArrayList<>();

    public TableModel genTableModel(TableConfig tableConfig, ProjectModel projectModel) {
        TableModel tableModel = new TableModel(projectModel);
        tableModel.setTableName(tableConfig.getTableName());
        tableModel.setRemarks(this.getRemarks());
        tableModel.setClassName(tableConfig.getDomainClassName());
        tableModel.setVariableName(StringUtil.getVariableName(tableConfig.getDomainClassName()));
        tableModel.setProjectRootPath(projectModel.getProjectRootPath());
        tableModel.setPackagePath(tableConfig.getPackageName().replaceAll("\\.", "/"));
        tableModel.setPackageName(tableConfig.getPackageName());
        tableModel.setAuthor(projectModel.getAuthor());

        tableModel.getGenerateFlags().putAll(tableConfig.getGenerateFlags());

        for (IntrospectedColumn column : baseColumns) {
            tableModel.addBaseColumn(column.genColumnModel(tableModel));
        }
        for (IntrospectedColumn column : primaryKeyColumns) {
            tableModel.addPrimaryKeyColumn(column.genColumnModel(tableModel));
        }

        return tableModel;
    }

    public void addPrimaryKeyColumn(IntrospectedColumn primaryKeyColumn) {
        this.primaryKeyColumns.add(primaryKeyColumn);
    }

    public void addBaseColumns(IntrospectedColumn baseColumn) {
        this.baseColumns.add(baseColumn);
    }

    public void addBlobColumns(IntrospectedColumn blobColumn) {
        this.blobColumns.add(blobColumn);
    }
}
