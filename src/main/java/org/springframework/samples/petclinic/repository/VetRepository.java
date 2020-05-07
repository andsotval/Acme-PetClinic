/**
 * DP2 - Grupo 8
 * LAB F1.33
 * Date: 05-may-2020
 */

package org.springframework.samples.petclinic.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.samples.petclinic.model.Vet;

public interface VetRepository extends BaseRepository<Vet> {

	@Query("SELECT vet FROM Vet vet WHERE vet.clinic = null")
	Collection<Vet> findAvailableVets();

	@Query("SELECT v FROM Vet v WHERE v.user.username = ?1")
	Vet findByVetByUsername(String username);

	@Query("SELECT v FROM Vet v where v.clinic.manager.id=?1")
	Collection<Vet> findVetsByManager(int managerId);

	@Query("SELECT v FROM Vet v where v.clinic.id=?1")
	Collection<Vet> findVetsByClinicId(Integer clinicId);

}
