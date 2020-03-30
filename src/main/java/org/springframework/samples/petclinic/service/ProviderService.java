
package org.springframework.samples.petclinic.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Provider;
import org.springframework.samples.petclinic.repository.ProviderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProviderService {

	private ProviderRepository providerRepository;


	@Autowired
	public ProviderService(ProviderRepository providerRepository) {
		this.providerRepository = providerRepository;
	}

	@Transactional(readOnly = true)
	public Iterable<Provider> findAllProviders() {
		return providerRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Iterable<Provider> findAvailableProviders() {
		return providerRepository.findAvailableProviders();
	}

	@Transactional(readOnly = true)
	public Optional<Provider> findProviderById(final int providerId) {
		return providerRepository.findById(providerId);
	}

	@Transactional(readOnly = true)
	public Iterable<Provider> findProvidersByManagerId(final int managerId) {
		return providerRepository.findProvidersByManagerId(managerId);
	}

	@Transactional
	public void saveProvider(final Provider provider) {
		providerRepository.save(provider);
	}

}
