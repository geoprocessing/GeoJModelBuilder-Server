package com.geojmodelbuilder.server.entities;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface WPSProcessRepository extends CrudRepository<WPSProcessSimple, Integer> {
	/*
	 * error ocurred when there are more than one records
	 * */
	@Query("select w from WPSProcessSimple w where w.name = ?1")
	WPSProcessSimple findProcessByName(String name);
}