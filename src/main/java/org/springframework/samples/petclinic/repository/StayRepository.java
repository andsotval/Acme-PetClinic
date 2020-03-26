package org.springframework.samples.petclinic.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Stay;
import org.springframework.stereotype.Repository;

@Repository
public interface StayRepository extends CrudRepository<Stay, Integer>{
	
	@Query("SELECT s FROM Stay s WHERE s.isAccepted = null")
	Iterable<Stay> findAllPending();
	
	@Query("SELECT s FROM Stay s WHERE s.isAccepted = true")
	Iterable<Stay> findAllAccepted();

}
