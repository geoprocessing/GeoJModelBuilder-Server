package com.geojmodelbuilder.server.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;

/**
 * Excuted workflow 
 * @author mingda zhang
 *
 */
@Entity
public class ExecutedWorkflowInfo extends AbstractResource{
	
	@Lob 
	@Column(nullable=false,length=512)
    private String xmlText;
	//refer to the template workflow if exists.
	private String templateId;
	//execution task id
	private String taskId;
	//whether executed successfully
	private boolean succeeded;
	
	public String getXmlText() {
		return xmlText;
	}
	public void setXmlText(String xmlText) {
		this.xmlText = xmlText;
	}
	public String getTemplateId() {
		return templateId;
	}
	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public boolean isSucceeded() {
		return succeeded;
	}
	public void setSucceeded(boolean succeeded) {
		this.succeeded = succeeded;
	}
}
