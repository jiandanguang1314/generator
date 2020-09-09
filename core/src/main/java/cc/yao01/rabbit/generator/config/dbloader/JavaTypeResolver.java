package cc.yao01.rabbit.generator.config.dbloader;

import cc.yao01.rabbit.generator.config.dbloader.IntrospectedColumn;

import java.math.BigDecimal;
import java.sql.Types;
import java.util.*;

/**
 *
 * @author 姚华成
 * @date 2017-10-19
 */
public class JavaTypeResolver {

	private static final String TYPE_RESOLVER_FORCE_BIG_DECIMALS = "forceBigDecimals";
	private static final int LONG_MAX_LENGTH = 18;
	private static final int INTEGER_MAX_LENGTH = 9;
	private static final int SHORT_MAX_LENGTH = 4;

    private List<String> warnings;

    private Properties properties;

    private boolean forceBigDecimals;

    private Map<Integer, String> typeMap;

	public JavaTypeResolver() {
		properties = new Properties();
		typeMap = new HashMap<>();

		typeMap.put(Types.ARRAY, Object.class.getSimpleName());
		typeMap.put(Types.BIGINT, Long.class.getSimpleName());
		typeMap.put(Types.BINARY, "byte[]");
		typeMap.put(Types.BIT, Boolean.class.getSimpleName());
		typeMap.put(Types.BLOB, "byte[]");
		typeMap.put(Types.BOOLEAN, Boolean.class.getSimpleName());
		typeMap.put(Types.CHAR, String.class.getSimpleName());
		typeMap.put(Types.CLOB, String.class.getSimpleName());
		typeMap.put(Types.DATALINK, Object.class.getSimpleName());
		typeMap.put(Types.DATE, Date.class.getName());
		typeMap.put(Types.DECIMAL, BigDecimal.class.getName());
		typeMap.put(Types.DISTINCT, Object.class.getSimpleName());
		typeMap.put(Types.DOUBLE, Double.class.getSimpleName());
		typeMap.put(Types.FLOAT, Double.class.getSimpleName());
		typeMap.put(Types.INTEGER, Integer.class.getSimpleName());
		typeMap.put(Types.JAVA_OBJECT, Object.class.getSimpleName());
		typeMap.put(Types.LONGNVARCHAR, String.class.getSimpleName());
		typeMap.put(Types.LONGVARBINARY, "byte[]");
		typeMap.put(Types.LONGVARCHAR, String.class.getSimpleName());
		typeMap.put(Types.NCHAR, String.class.getSimpleName());
		typeMap.put(Types.NCLOB, String.class.getSimpleName());
		typeMap.put(Types.NVARCHAR, String.class.getSimpleName());
		typeMap.put(Types.NULL, Object.class.getSimpleName());
		typeMap.put(Types.NUMERIC, BigDecimal.class.getName());
		typeMap.put(Types.OTHER, Object.class.getSimpleName());
		typeMap.put(Types.REAL, Float.class.getSimpleName());
		typeMap.put(Types.REF, Object.class.getSimpleName());
		typeMap.put(Types.SMALLINT, Integer.class.getSimpleName());
		typeMap.put(Types.STRUCT, Object.class.getSimpleName());
		typeMap.put(Types.TIME, Date.class.getName());
		typeMap.put(Types.TIMESTAMP, Date.class.getName());
		typeMap.put(Types.TINYINT, Integer.class.getSimpleName());
		typeMap.put(Types.VARBINARY, "byte[]");
		typeMap.put(Types.VARCHAR, String.class.getSimpleName());
	}

	public void addConfigurationProperties(Properties properties) {
		this.properties.putAll(properties);
		forceBigDecimals = Boolean.valueOf(properties.getProperty(TYPE_RESOLVER_FORCE_BIG_DECIMALS));
	}

	public String calculateJavaType(IntrospectedColumn introspectedColumn) {
		String answer = typeMap.get(introspectedColumn.getJdbcType());

		if (answer != null) {
			answer = overrideDefaultType(introspectedColumn, answer);
            answer = answer.replaceFirst("java.lang.", "");
		}
		return answer;
	}

	protected String overrideDefaultType(IntrospectedColumn column, String defaultType) {
		String answer = defaultType;

		switch (column.getJdbcType()) {
			case Types.BIT:
				answer = calculateBitReplacement(column, defaultType);
				break;
			case Types.DECIMAL:
			case Types.NUMERIC:
				answer = calculateBigDecimalReplacement(column, defaultType);
				break;
			default:
				// 什么都不用干
		}

		return answer;
	}

	protected String calculateBitReplacement(IntrospectedColumn column, String defaultType) {
		String answer;

		if (column.getLength() > 1) {
			answer = "byte[]";
		} else {
			answer = defaultType;
		}

		return answer;
	}

	protected String calculateBigDecimalReplacement(IntrospectedColumn column, String defaultType) {
		String answer;

		if (column.getScale() > 0 || column.getLength() > LONG_MAX_LENGTH || forceBigDecimals) {
			answer = defaultType;
		} else if (column.getLength() > INTEGER_MAX_LENGTH) {
			answer = Long.class.getName();
		} else if (column.getLength() > SHORT_MAX_LENGTH) {
			answer = Integer.class.getName();
		} else {
			answer = Integer.class.getName();
		}

		return answer;
	}

}
