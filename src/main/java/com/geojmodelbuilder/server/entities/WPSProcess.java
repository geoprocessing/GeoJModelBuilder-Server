package com.geojmodelbuilder.server.entities;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity 
public class WPSProcess extends AbstractResource{
   
	@Column(name="serviceid")
    private Integer ServiceId;
    
    public Integer getServiceId() {
		return ServiceId;
	}
	public void setServiceId(Integer serviceId) {
		ServiceId = serviceId;
	}
	
}
