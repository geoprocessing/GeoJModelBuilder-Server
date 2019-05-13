/**
 * Copyright (C) 2013 - 2016 Wuhan University
 * 
 * This program is free software; you can redistribute and/or modify it under 
 * the terms of the GNU General Public License version 2 as published by the 
 * Free Software Foundation.
 * 
 * This program is distributed WITHOUT ANY WARRANTY; even without the implied
 * WARRANTY OF MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 */
package com.geojmodelbuilder.server.entities;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Mingda Zhang
 *
 */
public class DesProcessInstance{

	private String id,name,description,title;
	private List<DesInputParameter> inputs;
	private List<DesOutputParameter> outputs;
	public DesProcessInstance() {
		this.inputs = new ArrayList<DesInputParameter>();
		this.outputs = new ArrayList<DesOutputParameter>();
	}

	public void addInput(DesInputParameter input){
		this.inputs.add(input);
	}
	
	public void addOutput(DesOutputParameter output){
		this.outputs.add(output);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<DesInputParameter> getInputs() {
		return inputs;
	}

	public void setInputs(List<DesInputParameter> inputs) {
		this.inputs = inputs;
	}

	public List<DesOutputParameter> getOutputs() {
		return outputs;
	}

	public void setOutputs(List<DesOutputParameter> outputs) {
		this.outputs = outputs;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}


}
