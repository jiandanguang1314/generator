package cc.yao01.rabbit.generator.datamodel;

import lombok.Data;

/**
 *
 * @author 姚华成
 * @date 2017-10-19
 */
@Data
public class ColumnModel implements IDataModel {
	private TableModel tableModel; 
	private String columnName;
    private String columnNameCap;
	private int columnLength;
	private String jdbcType; 
	private String javaType; 
	private String javaField;
    private String javaFieldFirstCap;
    private String remarks;
	public ColumnModel(TableModel tableModel) {
		this.tableModel = tableModel;
	}
}
