package org.springframework.samples.petclinic.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Clinic;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.web.bind.annotation.PathVariable;

public interface ClinicRepository extends CrudRepository<Clinic, Integer>{
	@Query("SELECT clinic FROM Clinic clinic WHERE clinic.name=?1")
	Optional<Clinic> findClinicByName(String name);

	@Query("SELECT clinic FROM Clinic clinic WHERE clinic.manager.id = :id")
	Clinic findClinicByManagerId(@PathVariable("id") Integer id);

	@Query("SELECT p FROM Pet p WHERE p.owner.clinic = ?1")
	Iterable<Pet> findPetsByClinic(Clinic id);
}
