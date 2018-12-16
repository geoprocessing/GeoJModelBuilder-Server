package com.geojmodelbuilder.server.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
import com.geojmodelbuilder.server.entities.AbstractResource;
import com.geojmodelbuilder.server.entities.ExecutedWorkflowInfo;
import com.geojmodelbuilder.server.entities.ExecutedWorkflowInfoRepository;
import com.geojmodelbuilder.server.util.ExecutedWorkflowInfoGenerator;
import com.geojmodelbuilder.xml.deserialization.XML2Instance;

@RestController
@RequestMapping(path="/workflow/execution") 
public class ExecutionWorkflowController implements IListener{
	@Autowired
	private ExecutedWorkflowInfoRepository execrepoitory;
	private static Map<String, WorkflowExecutor> RunningPool = new HashMap<String, WorkflowExecutor>();
	
	/**
	 * executes the workflow
	 * @param xmlText, xml text that represents the workflow
	 * @return
	 */
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
		 RunningPool.put(uuid,executor );
	
		 return new ServerResponse(200, "success", uuid);
	 }
	 
	 /**
	  * Check the detail of the workflow execution
	  * @param uuid
	  * @return
	  */
	@GetMapping("/detail/")
	public Object detail(@RequestParam("uuid") String uuid) {
		 ExecutedWorkflowInfo workflowInfo = execrepoitory.findWorkflowByTaskId(uuid);
		 if(workflowInfo == null)
			 return new ServerResponse(400, "no task with this id", "");
		 XML2Instance xml2Instance = new XML2Instance();
		 IWorkflowInstance workflowInstance = xml2Instance.parse(workflowInfo.getXmlText());
		 
		 return workflowInstance;
//		 return workflowInfo.getXmlText();
	 }
	 
	/**
	 * return all the workflows that are running.
	 * @return
	 */
	 @GetMapping("/running/all")
	 public ServerResponse Running(){
		 List<RunningResource> workflows = new ArrayList<ExecutionWorkflowController.RunningResource>();
		 for(String uuid:RunningPool.keySet()){
			 RunningResource resource = new ExecutionWorkflowController.RunningResource();
			 resource.uuid = uuid;
			 
			 WorkflowExecutor executor = RunningPool.get(uuid);
			 IWorkflowInstance workflowInstance = executor.getEngine().getWorkflow();
			 
			 resource.setIdentifier(workflowInstance.getID());
			 resource.setTitle(workflowInstance.getName());
			 resource.setDescription(workflowInstance.getDescription());
			 workflows.add(resource);
		 }
		 return new ServerResponse(200, "success", workflows);
	 }
	 
	 /**
	  * check the status of the workflow execution.
	  * @param uuid
	  * @return
	  */
	 @GetMapping("/status/{uuid}")
		public ServerResponse status(@PathVariable String uuid) {
			WorkflowExecutor executor = RunningPool.get(uuid);
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
			
			//resource.setId(workflowInfo.getId());
			resource.setTitle(workflowInfo.getTitle());
			resource.setDescription(workflowInfo.getDescription());		
			return new ServerResponse(200, resource.getStatus().toString(), resource);
		}

	 /**
	  * update the executor pool
	  */
	public synchronized void update() {
		for (String uuid : RunningPool.keySet()) {
			WorkflowExecutor executor = RunningPool.get(uuid);
			if (executor.getStatus() != WorkflowExecutor.ExecutorStatus.RUNNING) {
				this.save(uuid, executor);
				RunningPool.remove(uuid);
			}
		}
	}

	public boolean save(String uuid, WorkflowExecutor executor) {
		ExecutedWorkflowInfo workflowInfo = new ExecutedWorkflowInfoGenerator().generate(uuid, executor);
		execrepoitory.save(workflowInfo);
		return true;
	}
	
	@Override
	public void onEvent(IProcessEvent arg0) {
		this.update();
	}
	
	private class RunningResource extends AbstractResource{
		protected String uuid;
	}
}
