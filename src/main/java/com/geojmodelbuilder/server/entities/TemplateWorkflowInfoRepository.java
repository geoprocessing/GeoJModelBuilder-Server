package com.geojmodelbuilder.server.entities;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface TemplateWorkflowInfoRepository extends CrudRepository<TemplateWorkflowInfo, Integer> {
	@Query("select w from TemplateWorkflowInfo w where w.identifier = ?1")
	TemplateWorkflowInfo findWorkflowById(String identifier);
}