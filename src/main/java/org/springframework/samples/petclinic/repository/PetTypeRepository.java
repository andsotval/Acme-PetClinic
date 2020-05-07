/**
 * DP2 - Grupo 8
 * LAB F1.33
 * Date: 05-may-2020
 */

package org.springframework.samples.petclinic.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.samples.petclinic.model.PetType;

public interface PetTypeRepository extends BaseRepository<PetType> {

	@Query("SELECT pt FROM PetType pt WHERE pt.available = true")
	Collection<PetType> findAvailable();

	@Query("SELECT pt FROM PetType pt WHERE pt.available = false")
	Collection<PetType> findNotAvailable();

}
