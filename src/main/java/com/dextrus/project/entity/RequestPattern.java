package com.dextrus.project.entity;

public class RequestPattern {
	private ConnectionDetails details;
	private String pattern;
	private String catalog;
	
	public String getCatalog() {
		return catalog;
	}
	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}
	public ConnectionDetails getDetails() {
		return details;
	}
	public void setProperties(ConnectionDetails details) {
		this.details = details;
	}
	public String getPattern() {
		return pattern;
	}
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
}
