package org.springframework.samples.petclinic.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.samples.petclinic.model.Clinic;

public interface ClinicRepository {
	@Query("SELECT clinic FROM Clinic clinic WHERE clinic.name=?1")
	Optional<Clinic> findClinicByName(String name);
}
