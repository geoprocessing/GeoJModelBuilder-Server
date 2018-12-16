package com.geojmodelbuilder.server.entities;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface WPSServiceRepository extends CrudRepository<WPSServiceSimple, Integer> {
	@Query("select w from WPSServiceSimple w where w.url = ?1")
	WPSServiceSimple findServiceByUrl(String url);
}