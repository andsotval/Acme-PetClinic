package org.springframework.samples.petclinic.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.samples.petclinic.model.Clinic;
import org.springframework.samples.petclinic.model.Manager;

public interface ClinicRepository {
	@Query("SELECT clinic FROM Clinic clinic WHERE clinic.name=?1")
	Optional<Clinic> findClinicByName(String name);

	@Query("SELECT clinic FROM Clinic clinic WHERE clinic.manager=?1")
	Clinic findClinicByManager(Manager manager);
}
