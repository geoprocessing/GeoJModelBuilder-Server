package com.geojmodelbuilder.server.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.geojmodelbuilder.server.ServerResponse;
import com.geojmodelbuilder.server.entities.ExecutedWorkflowInfo;
import com.geojmodelbuilder.server.entities.ExecutedWorkflowInfoRepository;
import com.geojmodelbuilder.server.util.ExecutedWorkflowInfoUtil;

@RestController
@RequestMapping(path="/workflow/executed") 
public class ExecutedWorkflowController {
	@Autowired
	private ExecutedWorkflowInfoRepository execrepoitory;
	
	/**
	 * return the detail of the execution
	 * @param uuid
	 * @return
	 */
	@GetMapping("/detail")
	public Object detail(@RequestParam("uuid") String uuid) {
		 ExecutedWorkflowInfo workflowInfo = execrepoitory.findWorkflowByTaskId(uuid);
		 if(workflowInfo == null)
			 return new ServerResponse(400, "no task with this id", "");
		 /*
		 XML2Instance xml2Instance = new XML2Instance();
		 IWorkflowInstance workflowInstance = xml2Instance.parse(workflowInfo.getXmlText());
		 
		 return workflowInstance;
		 */
		 
		 return workflowInfo;
//		 return workflowInfo.getXmlText();
	 }
	 
	/**
	 * return the execution information
	 * @param uuid
	 * @return
	 */
	@GetMapping("/info")
	public ServerResponse info(@RequestParam("uuid") String uuid) {
		 ExecutedWorkflowInfo workflowInfo = execrepoitory.findWorkflowByTaskId(uuid);
		 if(workflowInfo == null)
			 return new ServerResponse(400, "no task with this id", "");

		 ExecutedWorkflowInfoUtil.exclueXML(workflowInfo);
		 return new ServerResponse(200, "success", workflowInfo);
	 }
	
	/**
	 * get all the executed workflows
	 * @return
	 */
	@GetMapping("/all")
	public ServerResponse all(){
		Iterable<ExecutedWorkflowInfo> workflows = execrepoitory.findAll();
		
		List<ExecutedWorkflowInfo> list = new ArrayList<ExecutedWorkflowInfo>();
		for(ExecutedWorkflowInfo workflow:workflows){
			ExecutedWorkflowInfoUtil.exclueXML(workflow);
			list.add(workflow);
		}
		
		return new ServerResponse(200, "success", list);
	}

}
