/**
 * DP2 - Grupo 8
 * LAB F1.33
 * Date: 05-may-2020
 */

package org.springframework.samples.petclinic.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.samples.petclinic.model.Provider;

public interface ProviderRepository extends BaseRepository<Provider> {

	@Query("SELECT provider FROM Provider provider WHERE provider.manager.id = null")
	Collection<Provider> findAvailableProviders();

	@Query("SELECT provider FROM Provider provider WHERE provider.manager.id=?1")
	Collection<Provider> findProvidersByManagerId(int managerId);

}
