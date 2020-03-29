
package org.springframework.samples.petclinic.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Stay;
import org.springframework.stereotype.Repository;

@Repository
public interface StayRepository extends CrudRepository<Stay, Integer> {

	@Query("SELECT s FROM Stay s WHERE s.isAccepted = null AND s.startDate > ?1")
	Iterable<Stay> findAllPending(LocalDate actualDate);

	@Query("SELECT s FROM Stay s WHERE s.isAccepted = true AND s.startDate > ?1")
	Iterable<Stay> findAllAccepted(LocalDate actualDate);

}
