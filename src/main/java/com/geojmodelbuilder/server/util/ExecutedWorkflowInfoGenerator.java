package com.geojmodelbuilder.server.util;

import cn.edu.whu.geos.wls.x10.ProcessInstanceDocument;

import com.geojmodelbuilder.core.instance.IWorkflowInstance;
import com.geojmodelbuilder.core.provenance.IProcessProv;
import com.geojmodelbuilder.core.provenance.IWorkflowProv;
import com.geojmodelbuilder.engine.impl.WorkflowExecutor;
import com.geojmodelbuilder.server.entities.ExecutedProcessInfo;
import com.geojmodelbuilder.server.entities.ExecutedWorkflowInfo;
import com.geojmodelbuilder.xml.serialization.Instance2XML;

public class ExecutedWorkflowInfoGenerator {
	public ExecutedWorkflowInfo generate(String taskId, WorkflowExecutor executor){
		IWorkflowInstance workflowInstance = executor.getEngine().getWorkflow();
		ExecutedWorkflowInfo workflowInfo = new ExecutedWorkflowInfo();
		workflowInfo.setTitle(workflowInstance.getName());
		workflowInfo.setDescription(workflowInstance.getDescription());
		workflowInfo.setTaskId(taskId);
		workflowInfo.setIdentifier(workflowInstance.getID());
		
		IWorkflowProv workflowProv = executor.getEngine().getWorkflowTrace();
		workflowInfo.setSucceeded(workflowProv.getStatus());
		workflowInfo.setStartTime(workflowProv.getStartTime());
		workflowInfo.setEndTime(workflowProv.getEndTime());
		
		Instance2XML instance2xml = new Instance2XML(workflowInstance);
		workflowInfo.setXmlText(instance2xml.xmlText());
		
		for(IProcessProv processProv : workflowProv.getProcesses()){
			String processId = processProv.getProcess().getID();
			ExecutedProcessInfo executedProcessInfo = new ExecutedProcessInfo();
			executedProcessInfo.setStartTime(processProv.getStartTime());
			executedProcessInfo.setEndTime(processProv.getEndTime());
			executedProcessInfo.setProcessId(processId);
			executedProcessInfo.setSucceeded(processProv.getStatus());
			executedProcessInfo.setTaskId(taskId);
			executedProcessInfo.setErrInfo(processProv.getErrInfo());
			
			ProcessInstanceDocument procDoc = instance2xml.getProcessDoc(processId);
			if(procDoc!=null)
				executedProcessInfo.setXmlText(procDoc.xmlText());
			workflowInfo.getProcessInfos().add(executedProcessInfo);
		}
		
		return workflowInfo;
	}
}
