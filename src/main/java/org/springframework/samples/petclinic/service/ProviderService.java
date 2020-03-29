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
	public Optional<Provider> findProviderById(final int id) {
		return this.providerRepository.findById(id);
	}

	@Transactional
	public void saveProvider(final Provider provider) {
		this.providerRepository.save(provider);
	}

}
