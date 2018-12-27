package com.geojmodelbuilder.server.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Excuted workflow 
 * @author mingda zhang
 *
 */
@Entity
@Table(name="executed_workflow_info")
public class ExecutedWorkflowInfo extends AbstractResource implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
    
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	//refer to the template workflow if exists.
	private String templateId;
	//execution task id
	@Column(name="taskId")
	private String taskId;
	//whether executed successfully
	private boolean succeeded;
	@JsonFormat
	private Date startTime;
	@JsonFormat
	private Date endTime;

	@Lob 
	@Column(length=512)
    private String xmlText;
	
	@Lob 
	@Column(length=512)
    private String errInfo;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<ExecutedProcessInfo> processInfos = new ArrayList<ExecutedProcessInfo>();
	
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
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public List<ExecutedProcessInfo> getProcessInfos() {
		return processInfos;
	}
	public void setProcessInfos(List<ExecutedProcessInfo> processInfos) {
		this.processInfos = processInfos;
	}
	public String getErrInfo() {
		return errInfo;
	}
	public void setErrInfo(String errInfo) {
		this.errInfo = errInfo;
	}
}
