/**
 * DP2 - Grupo 8
 * LAB F1.33
 * Date: 05-may-2020
 */

package org.springframework.samples.petclinic.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Provider;
import org.springframework.samples.petclinic.repository.BaseRepository;
import org.springframework.samples.petclinic.repository.ProviderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProviderService extends PersonService<Provider> {

	private ProviderRepository providerRepository;


	@Autowired
	public ProviderService(BaseRepository<Provider> repository, ProviderRepository providerRepository) {
		super(repository, Provider.class);
		this.providerRepository = providerRepository;
	}

	@Transactional(readOnly = true)
	public Collection<Provider> findAvailableProviders() {
		return providerRepository.findAvailableProviders();
	}

	@Transactional(readOnly = true)
	public Collection<Provider> findProvidersByManagerId(int managerId) {
		return providerRepository.findProvidersByManagerId(managerId);
	}

}
