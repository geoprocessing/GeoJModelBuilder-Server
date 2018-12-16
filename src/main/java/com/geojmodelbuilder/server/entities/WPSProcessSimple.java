package com.geojmodelbuilder.server.entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

@Entity 
public class WPSProcessSimple{
   
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
    private String name;
    
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	@Column(name="serviceid")
    private Integer ServiceId;
    

	@Lob
	@Column(length = 512)
	private String xmlText;
	
	@ManyToOne(cascade = CascadeType.ALL, optional = false)
	@JoinColumn(name = "serviceid", referencedColumnName = "id",insertable=false,updatable=false)
	private WPSServiceSimple wpserService;
	
    public Integer getServiceId() {
		return ServiceId;
	}
	public void setServiceId(Integer serviceId) {
		ServiceId = serviceId;
	}
	public String getXmlText() {
		return xmlText;
	}
	public void setXmlText(String xmlText) {
		this.xmlText = xmlText;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
