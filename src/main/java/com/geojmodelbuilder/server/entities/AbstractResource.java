package com.geojmodelbuilder.server.entities;

import javax.persistence.MappedSuperclass;


@MappedSuperclass
public class AbstractResource {
	
	private String identifier;
	private String title;
	private String description;
	
	public String getIdentifier() {
		return identifier;
	}
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
