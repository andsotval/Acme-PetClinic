
package org.springframework.samples.petclinic.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.Query;
import org.springframework.samples.petclinic.model.Stay;
import org.springframework.stereotype.Repository;

@Repository
public interface StayRepository extends BaseRepository<Stay> {

	@Query("SELECT s FROM Stay s INNER JOIN Clinic c ON c.id=s.clinic.id WHERE s.isAccepted IS NULL AND ?2 IN (SELECT v2.id FROM Vet v2 WHERE v2.clinic.id=c.id) AND s.startDate > ?1 ")
	Iterable<Stay> findAllPendingByVet(LocalDate actualDate, Integer vetId);

	@Query("SELECT s FROM Stay s INNER JOIN Clinic c ON c.id=s.clinic.id WHERE s.isAccepted = true AND ?2 IN (SELECT v2.id FROM Vet v2 WHERE v2.clinic.id=c.id) AND s.startDate > ?1 ")
	Iterable<Stay> findAllAcceptedByVet(LocalDate actualDate, Integer vetId);

}
