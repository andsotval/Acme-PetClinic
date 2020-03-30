package org.springframework.samples.petclinic.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Provider;
import org.springframework.stereotype.Repository;

@Repository
public interface ProviderRepository extends CrudRepository<Provider, Integer> {

	@Query("SELECT provider FROM Provider provider WHERE provider.manager.id=?1")
	Iterable<Provider> findProvidersByManagerId(int managerId);

}
