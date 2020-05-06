/**
 * DP2 - Grupo 8
 * LAB F1.33
 * Date: 05-may-2020
 */

package org.springframework.samples.petclinic.repository;

import java.time.LocalDateTime;
import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.samples.petclinic.model.Visit;

public interface VisitRepository extends BaseRepository<Visit> {

	@Query("SELECT v FROM Visit v INNER JOIN Clinic c ON c.id=v.clinic.id WHERE v.isAccepted IS NULL AND ?2 IN (SELECT v2.id FROM Vet v2 WHERE v2.clinic.id=c.id) AND v.dateTime > ?1 ")
	Collection<Visit> findAllPendingByVetId(LocalDateTime actualDate, Integer vetId);

	@Query("SELECT v FROM Visit v INNER JOIN Clinic c ON c.id=v.clinic.id WHERE v.isAccepted = true AND ?2 IN (SELECT v2.id FROM Vet v2 WHERE v2.clinic.id=c.id) AND v.dateTime > ?1 ")
	Collection<Visit> findAllAcceptedByVetId(LocalDateTime actualDate, Integer vetId);

	@Query("SELECT v FROM Visit v WHERE v.pet.id = ?1 ")
	Collection<Visit> findAllByPetId(int id);

	@Query("SELECT v FROM Visit v WHERE v.isAccepted IS NULL AND v.pet.owner.id=?1")
	Collection<Visit> findAllPendingByOwnerId(Integer id);

	@Query("SELECT v FROM Visit v WHERE v.isAccepted IS TRUE AND v.pet.owner.id=?1")
	Collection<Visit> findAllAcceptedByOwnerId(Integer id);

	@Query("SELECT v FROM Visit v WHERE v.dateTime=?1")
	Collection<Visit> findVisitsByDateTime(LocalDateTime dateTime);

	@Query("SELECT v FROM Visit v WHERE (v.isAccepted IS TRUE OR v.isAccepted IS NULL) AND v.pet.owner.id=?1")
	Collection<Visit> findAllAcceptedOrPendingByOwner(Integer id);

}
