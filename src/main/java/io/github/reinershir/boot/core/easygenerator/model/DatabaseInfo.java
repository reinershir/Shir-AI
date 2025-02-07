package io.github.reinershir.boot.core.easygenerator.model;

public class DatabaseInfo {

	private String url;
	private String name;
	private String password;
	private String driver;
	
	public DatabaseInfo(String url, String name, String password, String driver) {
		super();
		this.url = url;
		this.name = name;
		this.password = password;
		this.driver = driver;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getDriver() {
		return driver;
	}
	public void setDriver(String driver) {
		this.driver = driver;
	}
	
	
}
