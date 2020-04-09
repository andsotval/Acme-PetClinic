
package org.springframework.samples.petclinic.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.samples.petclinic.model.Provider;
import org.springframework.stereotype.Repository;

@Repository
public interface ProviderRepository extends BaseRepository<Provider> {

	@Query("SELECT provider FROM Provider provider WHERE provider.manager.id = null")
	Collection<Provider> findAvailableProviders();

	@Query("SELECT provider FROM Provider provider WHERE provider.manager.id=?1")
	Iterable<Provider> findProvidersByManagerId(int managerId);

}
