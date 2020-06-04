/**
 * DP2 - Grupo 8
 * LAB F1.33
 * Date: 05-may-2020
 */

package org.springframework.samples.petclinic.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.samples.petclinic.model.Pet;

public interface PetRepository extends NamedRepository<Pet> {

	@Query("SELECT p FROM Pet p WHERE p.owner.id = ?1")
	Collection<Pet> findPetsByOwnerId(Integer id);

}
