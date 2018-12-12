package com.geojmodelbuilder.server.entities;

import java.util.ArrayList;
import java.util.List;

import com.geojmodelbuilder.engine.impl.WorkflowExecutor.ExecutorStatus;

public class AbstractExecutedResource extends AbstractResource{

	private ExecutorStatus status;
	private List<String> runningProcess = new ArrayList<String>();
	private List<String > successList = new ArrayList<String>();
	private List<String > failureList = new ArrayList<String>();
	
	public ExecutorStatus getStatus() {
		return this.status;
	}

	public void setStatus(ExecutorStatus status) {
		this.status = status;
	}
	
    public void addSuccess(String success){
    	this.successList.add(success);
    }
    
    public void addFailure(String failure){
    	this.failureList.add(failure);
    }
    
    public void addRunning(String running){
    	this.runningProcess.add(running);
    }
}
