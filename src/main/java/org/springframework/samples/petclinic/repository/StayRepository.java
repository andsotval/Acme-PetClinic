/**
 * DP2 - Grupo 8
 * LAB F1.33
 * Date: 05-may-2020
 */

package org.springframework.samples.petclinic.repository;

import java.time.LocalDate;
import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.samples.petclinic.model.Stay;

public interface StayRepository extends BaseRepository<Stay> {

	@Query("SELECT s FROM Stay s INNER JOIN Clinic c ON c.id=s.clinic.id WHERE s.isAccepted IS NULL AND ?2 IN (SELECT v2.id FROM Vet v2 WHERE v2.clinic.id=c.id) AND s.startDate > ?1 ")
	Collection<Stay> findAllPendingByVet(LocalDate actualDate, Integer vetId);

	@Query("SELECT s FROM Stay s INNER JOIN Clinic c ON c.id=s.clinic.id WHERE s.isAccepted = true AND ?2 IN (SELECT v2.id FROM Vet v2 WHERE v2.clinic.id=c.id) AND s.startDate > ?1 ")
	Collection<Stay> findAllAcceptedByVet(LocalDate actualDate, Integer vetId);

	@Query("SELECT s FROM Stay s WHERE s.pet.id = ?1 ")
	Collection<Stay> findByPetId(Integer id);

	@Query("SELECT s FROM Stay s WHERE s.isAccepted IS NULL AND s.pet.owner.id=?1")
	Collection<Stay> findAllPendingByOwner(Integer id);

	@Query("SELECT s FROM Stay s WHERE s.isAccepted IS TRUE AND s.pet.owner.id=?1")
	Collection<Stay> findAllAcceptedByOwner(Integer id);

	@Query("SELECT s FROM Stay s WHERE (s.isAccepted IS TRUE OR s.isAccepted IS NULL) AND s.pet.owner.id=?1")
	Collection<Stay> findAllAcceptedOrPendingByOwner(Integer id);

}
