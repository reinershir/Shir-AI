package io.github.reinershir.boot.core.easygenerator.model;

import lombok.Data;

@Data
public class TableColumn {

	private String columnName;
	Integer columnLength;
	String columnType;
	String comment;
	String defaultValue;
	boolean isNull;
	String javaType;
	
}
