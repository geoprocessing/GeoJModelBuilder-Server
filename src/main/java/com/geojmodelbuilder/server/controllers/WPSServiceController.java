package com.geojmodelbuilder.server.controllers;

import java.util.ArrayList;
import java.util.List;

import net.opengis.wps.x100.ProcessDescriptionType;
import net.opengis.wps.x100.ProcessDescriptionsDocument;

import org.apache.xmlbeans.XmlOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.geojmodelbuilder.core.resource.ogc.wps.WPSProcess;
import com.geojmodelbuilder.core.resource.ogc.wps.WPService;
import com.geojmodelbuilder.server.ServerResponse;
import com.geojmodelbuilder.server.entities.WPSProcessRepository;
import com.geojmodelbuilder.server.entities.WPSProcessSimple;
import com.geojmodelbuilder.server.entities.WPSServiceRepository;
import com.geojmodelbuilder.server.entities.WPSServiceSimple;

@RestController
@RequestMapping(path="/service") 
public class WPSServiceController {
	@Autowired
	private WPSServiceRepository wpsRepository;
	
	@Autowired
	private WPSProcessRepository processRepository;
	/**
	 * Submit a new template workflow
	 * @param xmlText xml text that represents the workflow
	 * @return
	 */
	 @GetMapping("/add")
	 public ServerResponse add(@RequestParam("url") String url,@RequestParam("version") String version){
		 
		 WPService wps = new WPService();
		 wps.setUrl(url);
		 wps.setVersion(version);
		 wps.getProcesses();
		 
		 boolean flag = wps.parseService();
		 if(!flag)
			 return new ServerResponse(400, "failed", "fail to parse the service");
		 
		 WPSServiceSimple wpsService = new WPSServiceSimple();
//		 wpsService.setIdentifier(wps.getName());
		 wpsService.setUrl(url);
		 wpsService.setVersion(version);
		 
		 List<WPSProcessSimple> processList = new ArrayList<WPSProcessSimple>();
		 for(WPSProcess process:wps.getProcesses()){
			 WPSProcessSimple simpleProcess = new WPSProcessSimple();
			 ProcessDescriptionsDocument doc = ProcessDescriptionsDocument.Factory.newInstance();
			 doc.addNewProcessDescriptions().setProcessDescriptionArray(new ProcessDescriptionType[]{process.getProcessDescriptionType()});
//			 doc.addNewProcessDescriptions().setProcessDescriptionArray(0, process.getProcessDescriptionType());
			 simpleProcess.setXmlText(doc.xmlText(new XmlOptions().setSavePrettyPrint()));
			 simpleProcess.setName(process.getName());
//			 simpleProcess.setServiceId(serviceId);
			 wpsService.getProcesses().add(simpleProcess);
			 processList.add(simpleProcess);
//			 processRepository.save(simpleProcess);
		 }
		 
		 wpsRepository.save(wpsService);
		 Integer serviceId = wpsService.getId();
		 wpsRepository.save(wpsService);
		 for(WPSProcessSimple processSimple:processList){
			 processSimple.setServiceId(serviceId);
			 processRepository.save(processSimple);
		 }
		 
		 return new ServerResponse(200, "success", wpsService.getId());
	 }
	 
	 /**
	  * returns all processes
	  * @return
	  */
	 @GetMapping("/all")
	 public ServerResponse all()
	 {
		 Iterable<WPSServiceSimple> allServices = wpsRepository.findAll();
		 if(allServices == null)
			 return new ServerResponse(200, "success", "there is no service available");
		 
		 //do not return the XML text
		 for(WPSServiceSimple service:allServices){
			 for(WPSProcessSimple process:service.getProcesses()){
				 process.setXmlText("");
			 }
		 }
		 return new ServerResponse(200, "success", allServices);
	 }
	 
	 @GetMapping("/process/detail")
	 public Object process(@RequestParam("url") String url,@RequestParam("name") String name ){
		 WPSServiceSimple service = wpsRepository.findServiceByUrl(url);
		 if(service == null)
			 return new ServerResponse(400, "there is no such service", "");
		 WPSProcessSimple processSimple = null;
		 for(WPSProcessSimple process:service.getProcesses()){
			 if(process.getName().equals(name)){
				 processSimple = process;
				 break;
			 }
		 }
		 
		 if(processSimple == null)
			 return new ServerResponse(400, "there is no such process", "");
		 
		 return processSimple.getXmlText();
	 }
}
