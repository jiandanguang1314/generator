package cc.yao01.rabbit.generator.config;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 姚华成
 * @date 2017-11-20
 */
@Data
public class BizModelsConfig {
    private boolean generateTables;
    private GenerateFlags defaultGenerateFlags = new GenerateFlags();

    private String classPathEntry;
    private JdbcConnectionConfig jdbcConnectionConfig;

    private List<TableConfig> tables = new ArrayList<>();


    public void addTables(TableConfig tableConfig) {
        this.tables.add(tableConfig);
    }

}
