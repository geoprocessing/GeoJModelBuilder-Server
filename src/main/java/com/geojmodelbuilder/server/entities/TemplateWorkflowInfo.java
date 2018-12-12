package com.geojmodelbuilder.server.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;

@Entity
public class TemplateWorkflowInfo extends AbstractResource{
	
	@Lob 
	@Column(nullable=false,length=512)
    private String xmlText;

	public String getXmlText() {
		return xmlText;
	}

	public void setXmlText(String xmlText) {
		this.xmlText = xmlText;
	}
}
