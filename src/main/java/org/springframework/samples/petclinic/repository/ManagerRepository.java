
package org.springframework.samples.petclinic.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.samples.petclinic.model.Manager;
import org.springframework.stereotype.Repository;

@Repository
public interface ManagerRepository extends BaseRepository<Manager> {

	@Query("SELECT manager FROM Manager manager WHERE manager.user.username=?1")
	Manager findManagerByUsername(String user);

}
