
package org.springframework.samples.petclinic.service;

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
	public Iterable<Provider> findAvailableProviders() {
		return providerRepository.findAvailableProviders();
	}

	@Transactional(readOnly = true)
	public Iterable<Provider> findProvidersByManagerId(final int managerId) {
		return providerRepository.findProvidersByManagerId(managerId);
	}

}
