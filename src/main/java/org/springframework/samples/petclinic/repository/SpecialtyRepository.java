/**
 * DP2 - Grupo 8
 * LAB F1.33
 * Date: 05-may-2020
 */

package org.springframework.samples.petclinic.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.samples.petclinic.model.Specialty;

public interface SpecialtyRepository extends BaseRepository<Specialty> {

	@Query("SELECT s FROM Specialty s WHERE s.available = true")
	Collection<Specialty> findAvailable();

	@Query("SELECT s FROM Specialty s WHERE s.available = false")
	Collection<Specialty> findNotAvailable();

}
