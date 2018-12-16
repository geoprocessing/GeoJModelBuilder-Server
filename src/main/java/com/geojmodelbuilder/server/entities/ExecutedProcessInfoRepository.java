package com.geojmodelbuilder.server.entities;

import org.springframework.data.repository.CrudRepository;

public interface ExecutedProcessInfoRepository extends CrudRepository<ExecutedProcessInfo, Integer> {
	/*
	@Query("select w from ExecutedWorkflowInfo w where w.taskId = ?1")
	ExecutedWorkflowInfo findWorkflowByTaskId(String taskId);
	*/
}