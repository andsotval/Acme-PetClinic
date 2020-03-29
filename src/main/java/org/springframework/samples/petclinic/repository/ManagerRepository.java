package org.springframework.samples.petclinic.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Manager;

public interface ManagerRepository extends CrudRepository<Manager, Integer>{

	@Query("SELECT manager FROM Manager manager WHERE manager.user.username=?1")
	Optional<Manager> findManagerByUsername(String user);

}
