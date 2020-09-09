package cc.yao01.rabbit.generator.datamodel;

import cc.yao01.rabbit.generator.config.GenerateFlags;
import lombok.Data;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 姚华成
 * @date 2017-10-19
 */
@Data
public class TableModel implements IDataModel {
    @NonNull
    private ProjectModel project;
    private String tableName;
    private String className;
    private String variableName;
    private String projectRootPath;
    private String packageName;
    private String packagePath;
    private String author;
    private String remarks;

    private GenerateFlags generateFlags = new GenerateFlags();

    private List<ColumnModel> allColumns = new ArrayList<>();
    private ColumnModel pk;
    private List<ColumnModel> primaryKeyColumns = new ArrayList<>();
    private List<ColumnModel> baseColumns = new ArrayList<>();
    private List<ColumnModel> blobColumns = new ArrayList<>();
    public ColumnModel getPk() {
        pk = this.primaryKeyColumns.get(0);
        return pk;
    }

    public void addPrimaryKeyColumn(ColumnModel primaryKeyColumn) {
        this.primaryKeyColumns.add(primaryKeyColumn);
        this.allColumns.add(primaryKeyColumn);
    }

    public void addBaseColumn(ColumnModel baseColumn) {
        this.baseColumns.add(baseColumn);
        this.allColumns.add(baseColumn);
    }

    public GenerateFlags getGenerateFlags() {
        return generateFlags;
    }

    public boolean isGenerate(String fileType) {
        Boolean result = generateFlags.get(fileType);
        return result == null || result;
    }

}
