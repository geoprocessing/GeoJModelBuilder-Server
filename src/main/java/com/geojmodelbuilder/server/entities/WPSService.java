package com.geojmodelbuilder.server.entities;

import javax.persistence.Entity;

@Entity 
public class WPSService extends AbstractResource{
   
    private String url;
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
}
