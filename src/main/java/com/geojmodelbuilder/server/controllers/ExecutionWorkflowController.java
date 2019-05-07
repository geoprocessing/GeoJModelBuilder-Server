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
import com.geojmodelbuilder.server.entities.AbstractResource;
import com.geojmodelbuilder.server.entities.ExecutedWorkflowInfo;
import com.geojmodelbuilder.server.entities.ExecutedWorkflowInfoRepository;
import com.geojmodelbuilder.server.util.ExecutedWorkflowInfoUtil;
import com.geojmodelbuilder.xml.deserialization.XML2Instance;

@RestController
@RequestMapping(path = "/workflow/execution")
public class ExecutionWorkflowController implements IListener {
	@Autowired
	private ExecutedWorkflowInfoRepository execrepoitory;
	private static Map<String, WorkflowExecutor> RunningPool = new HashMap<String, WorkflowExecutor>();

	/**
	 * executes the workflow
	 * 
	 * @param xmlText
	 *            , xml text that represents the workflow
	 * @return
	 */
	@PostMapping("/submit")
	public ServerResponse add(@RequestBody String xmlText) {

		XML2Instance xml2Instance = new XML2Instance();
		WorkflowInstance workflowInstance = xml2Instance.parse(xmlText);

		if (workflowInstance == null) {
			String err = xml2Instance.getErrInfo();
			return new ServerResponse(400, "Failure", err);
		}
		String uuid = IDGenerator.uuid();
		WorkflowExecutor executor = new WorkflowExecutor(workflowInstance);
		executor.run();
		executor.getEngine().subscribe(this, EventType.Stopped);
		RunningPool.put(uuid, executor);

		return new ServerResponse(200, "success", uuid);
	}

	/**
	 * executes the workflow
	 * 
	 * @param xmlText
	 *            , xml text that represents the workflow
	 * @return
	 */
	@PostMapping("/submit/{uuid}")
	public ServerResponse add2(@RequestBody String xmlText,@PathVariable String uuid) {

		XML2Instance xml2Instance = new XML2Instance();
		WorkflowInstance workflowInstance = xml2Instance.parse(xmlText);

		if (workflowInstance == null) {
			String err = xml2Instance.getErrInfo();
			return new ServerResponse(400, "Failure", err);
		}
		WorkflowExecutor executor = new WorkflowExecutor(workflowInstance);
		executor.run();
		executor.getEngine().subscribe(this, EventType.Stopped);
		RunningPool.put(uuid, executor);

		return new ServerResponse(200, "success", uuid);
	}
	
	/**
	 * return all the workflows that are running.
	 * 
	 * @return
	 */
	@GetMapping("/running/all")
	public ServerResponse Running() {
		List<RunningResource> workflows = new ArrayList<ExecutionWorkflowController.RunningResource>();
		for (String uuid : RunningPool.keySet()) {
			RunningResource resource = new ExecutionWorkflowController.RunningResource();
			resource.setUuid(uuid);

			WorkflowExecutor executor = RunningPool.get(uuid);
			IWorkflowInstance workflowInstance = executor.getEngine()
					.getWorkflow();

			resource.setIdentifier(workflowInstance.getID());
			resource.setTitle(workflowInstance.getName());
			resource.setDescription(workflowInstance.getDescription());
			workflows.add(resource);
		}
		return new ServerResponse(200, "success", workflows);
	}

	/**
	 * check the status of the workflow execution.
	 * 
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

			processes = executor.getFailedIProcess();
			for(IProcess process:processes){
				resource.addFailure(process.getName());
			}
			
			processes = executor.getRunning();

			for(IProcess process:processes){
				resource.addRunning(process.getName());
			}
			
			return new ServerResponse(200, ExecutorStatus.RUNNING.toString(),
					resource);
		}

		ExecutedWorkflowInfo workflowInfo = execrepoitory
				.findWorkflowByTaskId(uuid);
		if (workflowInfo == null)
			return new ServerResponse(400, "no info with this id", "");

		if (workflowInfo.isSucceeded())
			resource.setStatus(ExecutorStatus.SUCCEEDED);
		else {
			resource.setStatus(ExecutorStatus.FAILED);
		}

		// resource.setId(workflowInfo.getId());
		resource.setTitle(workflowInfo.getTitle());
		resource.setDescription(workflowInfo.getDescription());
		return new ServerResponse(200, resource.getStatus().toString(),
				resource);
	}

	@GetMapping("/status2/{uuid}")
	public ServerResponse status2(@PathVariable String uuid) {
		WorkflowExecutor executor = RunningPool.get(uuid);
		AbstractExecutedResource resource = new AbstractExecutedResource();
		if (executor != null) {
			List<IProcess> processes = executor.getExecutedProcess();
			resource.setStatus(ExecutorStatus.RUNNING);
			for (IProcess process : processes) {
				resource.addSuccess(process.getID());
			}

			processes = executor.getFailedIProcess();
			for(IProcess process:processes){
				resource.addFailure(process.getID());
			}
			
			processes = executor.getRunning();

			for(IProcess process:processes){
				resource.addRunning(process.getID());
			}
			
			
			int finishedNo = executor.getEngine().getWorkflow().getProcesses().size();
			if(finishedNo==resource.getSuccessList().size()){
				if(RunningPool.containsKey(uuid))
					RunningPool.remove(uuid);
			}
				
			return new ServerResponse(200, ExecutorStatus.RUNNING.toString(),
					resource);
		}

		ExecutedWorkflowInfo workflowInfo = execrepoitory
				.findWorkflowByTaskId(uuid);
		if (workflowInfo == null)
			return new ServerResponse(400, "no info with this id", "");

		if (workflowInfo.isSucceeded())
			resource.setStatus(ExecutorStatus.SUCCEEDED);
		else {
			resource.setStatus(ExecutorStatus.FAILED);
		}

		// resource.setId(workflowInfo.getId());
		resource.setTitle(workflowInfo.getTitle());
		resource.setDescription(workflowInfo.getDescription());
		return new ServerResponse(200, resource.getStatus().toString(),
				resource);
	}
	
	/**
	 * check the status of the workflow execution.
	 * 
	 * @param uuid
	 * @return
	 */
	@GetMapping("/stop/{uuid}")
	public ServerResponse stop(@PathVariable String uuid) {
		WorkflowExecutor executor = RunningPool.get(uuid);
		RunningPool.remove(uuid);
		AbstractExecutedResource resource = new AbstractExecutedResource();

		if (executor == null)
			return new ServerResponse(400, "This task is not running", "");

		executor.getEngine().dispose();

		List<IProcess> processes = executor.getExecutedProcess();
		// sresource.setStatus(ExecutorStatus.RUNNING);
		for (IProcess process : processes) {
			resource.addSuccess(process.getName());
		}

		processes = executor.getFailedIProcess();
		for (IProcess process : processes) {
			resource.addFailure(process.getName());
		}

		return new ServerResponse(200, "Stopped", resource);
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
		ExecutedWorkflowInfo workflowInfo = new ExecutedWorkflowInfoUtil()
				.generate(uuid, executor);
		execrepoitory.save(workflowInfo);
		return true;
	}

	@Override
	public void onEvent(IProcessEvent arg0) {
		this.update();
	}

	public class RunningResource extends AbstractResource {
		private String uuid;

		public String getUuid() {
			return uuid;
		}

		public void setUuid(String uuid) {
			this.uuid = uuid;
		}
	}
}
