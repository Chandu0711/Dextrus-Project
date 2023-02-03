package com.dextrus.project.entity;

public class RequestBodyQuery {

	private  ConnectionDetails details;
	private String query;
	
	public ConnectionDetails getDetails() {
		return details;
	}
	public void setProperties( ConnectionDetails details) {
		this.details = details;
	}
	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}
}
