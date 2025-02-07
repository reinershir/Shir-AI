package io.github.reinershir.boot.contract;

public enum DictionaryCodes {

	OPENAI_API_KEY("OPENAI_KEY"),
	OPENAI_API_HOST("OPENAI_API_HOST"),
	OPENAI_REQUEST_TIMEOUT("OPENAI_REQUEST_TIMEOUT");
	
	String value;
	private DictionaryCodes(String value) {
		this.value=value;
	}
	public String getValue() {
		return value;
	}
}
