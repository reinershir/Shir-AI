package io.github.reinershir.boot.core.easygenerator.model;

public class FieldInfo {

	private String name;
	private String javaType;
	private String comment;
	//对应的数据库字段名称
	private String columnName;
	private boolean isPrimaryKey = false;
	private String defaultValue;
	/**
	 * 该字段的查询条件
	 */
	private String operation;
	/**
	 * 字段长度
	 */
	private Integer columnLength;
	/**
	 * 是否允许空值
	 */
	private boolean isNull = true;
	
	public FieldInfo() {}
	
	public FieldInfo(String name,String javaType, Integer columnLength) {
		super();
		this.name = name;
		this.javaType = javaType;
		this.columnLength = columnLength;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getJavaType() {
		return javaType;
	}
	public void setJavaType(String javaType) {
		this.javaType = javaType;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public boolean getIsPrimaryKey() {
		return isPrimaryKey;
	}
	public void setIsPrimaryKey(boolean isPrimaryKey) {
		this.isPrimaryKey = isPrimaryKey;
	}
	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	public boolean getIsNull() {
		return isNull;
	}
	public void setIsNull(boolean isNull) {
		this.isNull = isNull;
	}
	public Integer getColumnLength() {
		return columnLength;
	}
	public void setColumnLength(Integer columnLength) {
		this.columnLength = columnLength;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}
	
	
}
