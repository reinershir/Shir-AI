package io.github.reinershir.boot.core.easygenerator.model;

import java.util.List;

public class EntityInfo {
	
	private String packageName;
	
	private String entityName;
	
	private List<FieldInfo> fields;
	
	

	public EntityInfo(String packageName, String entityName,
			List<FieldInfo> fields) {
		super();
		this.packageName = packageName;
		this.entityName = entityName;
		this.fields = fields;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public List<FieldInfo> getFields() {
		return fields;
	}

	public void setFields(List<FieldInfo> fields) {
		this.fields = fields;
	}
	
	

}
