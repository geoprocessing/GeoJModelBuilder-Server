package com.geojmodelbuilder.server.util;

import com.geojmodelbuilder.core.instance.IInputParameter;
import com.geojmodelbuilder.core.instance.IOutputParameter;
import com.geojmodelbuilder.core.instance.impl.InputParameter;
import com.geojmodelbuilder.core.instance.impl.OutputParameter;
import com.geojmodelbuilder.core.resource.ogc.wps.WPSProcess;
import com.geojmodelbuilder.server.entities.DesInputParameter;
import com.geojmodelbuilder.server.entities.DesOutputParameter;
import com.geojmodelbuilder.server.entities.DesProcessInstance;

public class WPSDesGenerator {
	public static DesProcessInstance DesInstance(WPSProcess wpsProcess){
		DesProcessInstance desInstance = new DesProcessInstance();
		desInstance.setName(wpsProcess.getName());
		desInstance.setId(wpsProcess.getID());
		desInstance.setDescription(wpsProcess.getDescription());
		desInstance.setTitle(wpsProcess.getTitle());
		
		for(IInputParameter input:wpsProcess.getInputs()){
			DesInputParameter desInputParameter = new DesInputParameter();
			desInputParameter.setID(input.getID());
			desInputParameter.setName(input.getName());
			desInputParameter.setDescription(((InputParameter)input).getDescription());
			desInstance.addInput(desInputParameter);
		}
		
		for(IOutputParameter output:wpsProcess.getOutputs()){
			DesOutputParameter despaParameter = new DesOutputParameter();
			despaParameter.setID(output.getID());
			despaParameter.setName(output.getName());
			despaParameter.setDescription(((OutputParameter)output).getDescription());
			desInstance.addOutput(despaParameter);
		}
		
		return desInstance;
	}
}
