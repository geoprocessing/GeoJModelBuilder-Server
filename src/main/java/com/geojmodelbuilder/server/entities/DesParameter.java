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

import com.geojmodelbuilder.core.instance.IProcessInstance;

/**
 * @author Mingda Zhang
 *
 */
public abstract class DesParameter {
	private String name;
	//private IProcessInstance owner;
	private String id;
	private String description;
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	
	public IProcessInstance getOwner() {
		return null;
	}
	
	public void setID(String id){
		this.id = id;
	}
	
	public String getID() {
		return this.id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
