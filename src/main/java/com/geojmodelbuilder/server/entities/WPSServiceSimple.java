package com.geojmodelbuilder.server.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity 
@Table(name="wps_service")
public class WPSServiceSimple implements Serializable{
   
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<WPSProcessSimple> processes = new ArrayList<WPSProcessSimple>();
    
    private String version;
    
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
    private String url;
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public List<WPSProcessSimple> getProcesses() {
		return processes;
	}
	public void setProcesses(List<WPSProcessSimple> processes) {
		this.processes = processes;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
}
