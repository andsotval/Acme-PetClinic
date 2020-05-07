/**
 * DP2 - Grupo 8
 * LAB F1.33
 * Date: 05-may-2020
 */

package org.springframework.samples.petclinic.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.Owner;

public interface OwnerRepository extends BaseRepository<Owner> {

	@Query("SELECT DISTINCT owner FROM Owner owner WHERE owner.lastName LIKE :lastName%")
	Collection<Owner> findByLastName(@Param("lastName") String lastName);

	@Query("SELECT o FROM Owner o WHERE o.user.username = ?1")
	Owner findByOwnerByUsername(String username);

	@Query("SELECT o FROM Owner o WHERE o.clinic.id = ?1")
	List<Owner> findByClinicId(Integer id);

}
