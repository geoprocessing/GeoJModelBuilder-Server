package com.geojmodelbuilder.server.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.geojmodelbuilder.core.instance.impl.WorkflowInstance;
import com.geojmodelbuilder.core.utils.IDGenerator;
import com.geojmodelbuilder.engine.impl.WorkflowExecutor;
import com.geojmodelbuilder.server.ServerResponse;
import com.geojmodelbuilder.server.entities.AbstractResource;
import com.geojmodelbuilder.server.entities.TemplateWorkflowInfo;
import com.geojmodelbuilder.server.entities.TemplateWorkflowInfoRepository;
import com.geojmodelbuilder.xml.deserialization.XML2Instance;

@RestController
@RequestMapping(path="/workflow/template") 
public class TemplateWorkflowController {
	@Autowired
	private TemplateWorkflowInfoRepository repository;
	private static Map<String, WorkflowExecutor> executor = new HashMap<String, WorkflowExecutor>();
	
	/**
	 * Submit a new template workflow
	 * @param xmlText xml text that represents the workflow
	 * @return
	 */
	 @PostMapping("/add")
	 public ServerResponse add(@RequestBody String xmlText){
		 
		 XML2Instance xml2Instance = new XML2Instance();
		 WorkflowInstance workflowInstance = xml2Instance.parse(xmlText);
		 
		 if(workflowInstance == null)
		 {
			 String err = xml2Instance.getErrInfo();
			 return new ServerResponse(400, "Failure", err);
		 }
		 
		 executor.put(IDGenerator.uuid(), new WorkflowExecutor(workflowInstance));
	
		 TemplateWorkflowInfo workflowInfo = new TemplateWorkflowInfo();
		 workflowInfo.setTitle(workflowInstance.getName());
		 workflowInfo.setDescription(workflowInstance.getDescription());
		 
		 workflowInfo.setIdentifier(workflowInstance.getID());
		 workflowInfo.setXmlText(xmlText);
		 
		 //workflowDb.save(workflowInfo);
		 repository.save(workflowInfo);
		 
		 return new ServerResponse(200, "success", workflowInfo.getId());
	 }
	 
	 /**
	  * returns all template workflows
	  * @return
	  */
	 @GetMapping("/all")
	 public ServerResponse all()
	 {
		 Iterable<TemplateWorkflowInfo> allWorkflowInfos = repository.findAll();
		 
		 List<AbstractResource> workflows = new ArrayList<AbstractResource>();
		 for(TemplateWorkflowInfo workflowInfo : allWorkflowInfos){
			 AbstractResource resource = new AbstractResource();
			 //resource.setId(workflowInfo.getId());
			 resource.setIdentifier(workflowInfo.getIdentifier());
			 resource.setTitle(workflowInfo.getTitle());
			 resource.setDescription(workflowInfo.getDescription());
			 workflows.add(resource);
		 }
		 return new ServerResponse(200, "success", workflows);
	 }
	 
	 /**
	  * return the xml that represents the workflow
	  * @param identifier, identifier for this workflow
	  * @return
	  */
	 @GetMapping("/detail/")
	 public Object detail(@RequestParam("identifier") String identifier){
		TemplateWorkflowInfo  workflow = repository.findWorkflowById(identifier);
		if(workflow!=null)
			return workflow.getXmlText();
		
		return new ServerResponse(200, "no value", "there is no recod with this id");
	 }
	 
	 /**
	  * return the xml that represents the workflow
	  * @param id
	  * @return
	  */
	 @GetMapping("/detail/{id}")
	 public Object detail(@PathVariable Integer id){
		Optional<TemplateWorkflowInfo>  workflow = repository.findById(id);
		if(workflow.isPresent())
			return workflow.get().getXmlText();
		
		return new ServerResponse(200, "no value", "there is no recod with this id");
	 }
}
