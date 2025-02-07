package io.github.reinershir.boot.core.easygenerator.model;

import java.util.List;

public class GenerateInfo {

	private String tableName;
	private String modelName;
	/**
	 * 实体类说明，用于生成代码时的注释
	 */
	private String modelDescription;
	/**
	 * 根据数据库查询到的字段信息
	 */
	private List<FieldInfo> fieldInfos;
	
	public GenerateInfo(String tableName, String modelName, String modelDescription) {
		super();
		this.tableName = tableName;
		this.modelName = modelName;
		this.modelDescription = modelDescription;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getModelName() {
		return modelName;
	}
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	public List<FieldInfo> getFieldInfos() {
		return fieldInfos;
	}
	public void setFieldInfos(List<FieldInfo> fieldInfos) {
		this.fieldInfos = fieldInfos;
	}
	public String getModelDescription() {
		return modelDescription;
	}
	public void setModelDescription(String modelDescription) {
		this.modelDescription = modelDescription;
	}
	
	
	
}
