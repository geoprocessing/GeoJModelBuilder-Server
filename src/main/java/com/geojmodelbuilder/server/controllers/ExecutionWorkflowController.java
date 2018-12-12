package com.geojmodelbuilder.server.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.geojmodelbuilder.core.IProcess;
import com.geojmodelbuilder.core.instance.IWorkflowInstance;
import com.geojmodelbuilder.core.instance.impl.WorkflowInstance;
import com.geojmodelbuilder.core.utils.IDGenerator;
import com.geojmodelbuilder.engine.IListener;
import com.geojmodelbuilder.engine.IProcessEvent;
import com.geojmodelbuilder.engine.IProcessEvent.EventType;
import com.geojmodelbuilder.engine.impl.WorkflowExecutor;
import com.geojmodelbuilder.engine.impl.WorkflowExecutor.ExecutorStatus;
import com.geojmodelbuilder.server.ServerResponse;
import com.geojmodelbuilder.server.entities.AbstractExecutedResource;
import com.geojmodelbuilder.server.entities.ExecutedWorkflowInfo;
import com.geojmodelbuilder.server.entities.ExecutedWorkflowInfoRepository;
import com.geojmodelbuilder.xml.deserialization.XML2Instance;
import com.geojmodelbuilder.xml.serialization.Instance2XML;

@RestController
@RequestMapping(path="/workflow/execution") 
public class ExecutionWorkflowController implements IListener{
	@Autowired
	private ExecutedWorkflowInfoRepository execrepoitory;
	private static Map<String, WorkflowExecutor> ExecutorPool = new HashMap<String, WorkflowExecutor>();
	
	 @PostMapping("/submit")
	 public ServerResponse add(@RequestBody String xmlText){
		 
		 XML2Instance xml2Instance = new XML2Instance();
		 WorkflowInstance workflowInstance = xml2Instance.parse(xmlText);
		 
		 if(workflowInstance == null)
		 {
			 String err = xml2Instance.getErrInfo();
			 return new ServerResponse(400, "Failure", err);
		 }
		 String uuid = IDGenerator.uuid();
		 WorkflowExecutor executor = new WorkflowExecutor(workflowInstance);
		 executor.run();
		 executor.getEngine().subscribe(this, EventType.Stopped);
		 ExecutorPool.put(uuid,executor );
	
		 return new ServerResponse(200, "success", uuid);
	 }
	 
	 /*
	 @RequestMapping("/all")
	 public ServerResponse all()
	 {
		 Iterable<ExecutedWorkflowInfo> allWorkflowInfos = execrepoitory.findAll();
		 
		 List<AbstractResource> workflows = new ArrayList<AbstractResource>();
		 for(TemplateWorkflowInfo workflowInfo : allWorkflowInfos){
			 AbstractResource resource = new AbstractResource();
			 resource.setId(workflowInfo.getId());
			 resource.setIdentifier(workflowInfo.getIdentifier());
			 resource.setTitle(workflowInfo.getTitle());
			 resource.setDescription(workflowInfo.getDescription());
			 workflows.add(resource);
		 }
		 return new ServerResponse(200, "success", workflows);
	 }
	 */
	 
	 @RequestMapping("/status/{uuid}")
		public ServerResponse status(@PathVariable String uuid) {
			WorkflowExecutor executor = ExecutorPool.get(uuid);
			AbstractExecutedResource resource = new AbstractExecutedResource();
			if (executor != null) {
				List<IProcess> processes = executor.getExecutedProcess();
				resource.setStatus(ExecutorStatus.RUNNING);
				for (IProcess process : processes) {
					resource.addSuccess(process.getName());
				}

				return new ServerResponse(200, ExecutorStatus.RUNNING.toString(), resource);
			}
			
			ExecutedWorkflowInfo workflowInfo = execrepoitory.findWorkflowByTaskId(uuid);
			if(workflowInfo == null)
				return new ServerResponse(400, "no info with this id", "");
			
			if(workflowInfo.isSucceeded())
				resource.setStatus(ExecutorStatus.SUCCEEDED);
			else {
				resource.setStatus(ExecutorStatus.FAILED);
			}
			
			resource.setId(workflowInfo.getId());
			resource.setTitle(workflowInfo.getTitle());
			resource.setDescription(workflowInfo.getDescription());		
			return new ServerResponse(200, resource.getStatus().toString(), resource);
		}

	
	 /**
	  * update the executor pool
	  */
	public void update() {
		for (String uuid : ExecutorPool.keySet()) {
			WorkflowExecutor executor = ExecutorPool.get(uuid);
			if (executor.getStatus() != WorkflowExecutor.ExecutorStatus.RUNNING) {
				this.save(uuid, executor);
				ExecutorPool.remove(uuid);
			}
		}
	}

	public boolean save(String uuid, WorkflowExecutor executor) {
		IWorkflowInstance workflowInstance = executor.getEngine().getWorkflow();
		ExecutedWorkflowInfo workflowInfo = new ExecutedWorkflowInfo();
		workflowInfo.setTitle(workflowInstance.getName());
		workflowInfo.setDescription(workflowInstance.getDescription());
		workflowInfo.setTaskId(uuid);
		workflowInfo.setIdentifier(workflowInstance.getID());
		
		if(executor.getStatus()== WorkflowExecutor.ExecutorStatus.FAILED)
			workflowInfo.setSucceeded(false);
		else {
			workflowInfo.setSucceeded(true);
		}
		
		// need to save the provenance information
		Instance2XML instance2xml = new Instance2XML(workflowInstance);
		workflowInfo.setXmlText(instance2xml.xmlText());

		//new ExecutedWorkflowDB().save(workflowInfo);
		execrepoitory.save(workflowInfo);
		//templateRepository.save(templateWorkflowInfo);
		return true;
	}
	@Override
	public void onEvent(IProcessEvent arg0) {
		this.update();
	}
}
