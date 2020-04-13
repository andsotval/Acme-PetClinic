
package org.springframework.samples.petclinic.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.Clinic;
import org.springframework.stereotype.Repository;

@Repository
public interface ClinicRepository extends BaseRepository<Clinic> {

	@Query("SELECT clinic FROM Clinic clinic WHERE clinic.name=?1")
	Clinic findClinicByName(String name);

	@Query("SELECT clinic FROM Clinic clinic WHERE clinic.manager.id = :id")
	Clinic findClinicByManagerId(@Param("id") Integer id);
}
