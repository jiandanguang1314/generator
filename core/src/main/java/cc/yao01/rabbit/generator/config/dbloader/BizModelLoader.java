package cc.yao01.rabbit.generator.config.dbloader;

import cc.yao01.rabbit.generator.config.*;
import cc.yao01.rabbit.generator.datamodel.TableModel;
import cc.yao01.rabbit.generator.datamodel.ProjectModel;
import cc.yao01.rabbit.generator.exception.BizException;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * @author 姚华成
 * @date 2017-10-19
 */
public class BizModelLoader {

    public List<TableModel> loadBizModel(BizModelsConfig bizModelsConfig, ProjectModel projectModel) {
        List<TableModel> tableModels = new ArrayList<>();
        if (!bizModelsConfig.isGenerateTables()) {
            return tableModels;
        }
        try (Connection connection = this.getConnection(bizModelsConfig.getJdbcConnectionConfig())) {
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            List<TableConfig> tableConfigs = bizModelsConfig.getTables();
            for (TableConfig tableConfig : tableConfigs) {
                String tableName = tableConfig.getTableName();
                IntrospectedTable table = new IntrospectedTable();

                // 获取表的信息
                try (ResultSet rs = databaseMetaData.getTables(null, null, tableName, null)) {
                    if (!rs.next()) {
                        continue;
                    }
                    table.setTableName(tableName);
                    table.setTableType(rs.getString("TABLE_TYPE"));
                    table.setRemarks(rs.getString("REMARKS"));
                }
                // 获取字段列表信息
                try (ResultSet rs = databaseMetaData.getColumns(null, null, tableName, null)) {
                    while (rs.next()) {
                        IntrospectedColumn column = new IntrospectedColumn();
                        column.setIntrospectedTable(table);
                        table.addBaseColumns(column);

                        column.setActualColumnName(rs.getString("COLUMN_NAME"));
                        column.setJdbcType(rs.getInt("DATA_TYPE"));
                        column.setLength(rs.getInt("COLUMN_SIZE"));
                        column.setScale(rs.getInt("DECIMAL_DIGITS"));
                        column.setNullable(rs.getBoolean("NULLABLE"));
                        column.setRemarks(rs.getString("REMARKS"));
                        column.setAutoIncrement(rs.getBoolean("IS_AUTOINCREMENT"));
                        column.setGeneratedColumn(rs.getBoolean("IS_GENERATEDCOLUMN"));
                    }
                }
                // 获取主键
                TreeMap<Integer, String> pkColumnNames = new TreeMap<>();
                try (ResultSet rs = databaseMetaData.getPrimaryKeys(null, null, tableName)) {
                    while (rs.next()) {
                        pkColumnNames.put(rs.getInt("KEY_SEQ"), rs.getString("COLUMN_NAME"));
                    }
                }
                List<IntrospectedColumn> baseColumns = table.getBaseColumns();

                for (String columnName : pkColumnNames.values()) {
                    for (IntrospectedColumn column : baseColumns) {
                        if (columnName.equals(column.getActualColumnName())) {
                            table.addPrimaryKeyColumn(column);
                            break;
                        }
                    }
                }
                baseColumns.removeAll(table.getPrimaryKeyColumns());

                tableModels.add(table.genTableModel(tableConfig, projectModel));
            }
        } catch (Exception e) {
            throw new BizException("加载数据表的元数据的数据库配置时出错：", e);
        }

        return tableModels;
    }

    /**
     * Gets the connection.
     *
     * @return the connection
     * @throws SQLException the SQL exception
     */
    private Connection getConnection(JdbcConnectionConfig jdbcConnectionConfig) throws SQLException {
        ConnectionFactory connectionFactory;
        connectionFactory = new JdbcConnectionFactory(jdbcConnectionConfig);
        return connectionFactory.getConnection();
    }

}
