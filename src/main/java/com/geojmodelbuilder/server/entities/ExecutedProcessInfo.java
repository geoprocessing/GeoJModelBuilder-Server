package com.geojmodelbuilder.server.entities;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * query the process execution information throw the task id and process id.
 * 
 * @author Da
 *
 */
@Entity
@Table(name = "executed_process_info")
public class ExecutedProcessInfo  {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	@Column(name = "taskId")
	private String taskId;
	
	private String output;

	private String processId;
	// execution task id
	// whether executed successfully
	private boolean succeeded;
	@JsonFormat
	private Date startTime;
	@JsonFormat
	private Date endTime;

	@Lob
	@Column(length = 512)
	private String xmlText;

	@Lob
	@Column(length = 512)
	private String errInfo;

	@ManyToOne(cascade = CascadeType.ALL, optional = false)
	@JoinColumn(name = "taskId", referencedColumnName = "taskId", insertable = false, updatable = false)
	private ExecutedWorkflowInfo workflowInfo;

	public String getXmlText() {
		return xmlText;
	}

	public void setXmlText(String xmlText) {
		this.xmlText = xmlText;
	}

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
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

	public String getErrInfo() {
		return errInfo;
	}

	public void setErrInfo(String errInfo) {
		this.errInfo = errInfo;
	}
	public String getOutput() {
		return output;
	}
	public void setOutput(String output) {
		this.output = output;
	}
}
