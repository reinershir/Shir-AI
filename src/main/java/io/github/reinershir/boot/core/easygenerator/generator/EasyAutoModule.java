package io.github.reinershir.boot.core.easygenerator.generator;

public enum EasyAutoModule {
	CONTROLLER_CLASS("controller"),
	CRITERIA_CLASS("criteria"),
	SERVICE_INTERFACE("service"),
	SERVICE_IMPLEMENTS("serviceImpl"),
	PAGE("page"),
	MODEL("model"),
	MYBATIS_MAPPER_INTERFACE("dao"),
	MYBATIS_MAPPER_XML("mapper");
	
	String value;
	private EasyAutoModule(String value) {
		this.value=value;
	}
	public String getValue() {
		return value;
	}
	
	
}
