package com.geojmodelbuilder.server.entities;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ExecutedWorkflowInfoRepository extends CrudRepository<ExecutedWorkflowInfo, Integer> {
	@Query("select w from ExecutedWorkflowInfo w where w.taskId = ?1")
	ExecutedWorkflowInfo findWorkflowByTaskId(String taskId);
	
}