/**
 * DP2 - Grupo 8
 * LAB F1.33
 * Date: 05-may-2020
 */

package org.springframework.samples.petclinic.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.repository.BaseRepository;
import org.springframework.samples.petclinic.repository.NamedRepository;
import org.springframework.samples.petclinic.repository.PetTypeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PetTypeService extends NamedService<PetType> {

	private PetTypeRepository petTypeRepository;


	@Autowired
	public PetTypeService(BaseRepository<PetType> repository, NamedRepository<PetType> namedRepository,
		PetTypeRepository petTypeRepository) {
		super(repository, namedRepository);
		this.petTypeRepository = petTypeRepository;
	}

	@Transactional(readOnly = true)
	public Collection<PetType> findAvailable() {
		return petTypeRepository.findAvailable();
	}

	@Transactional(readOnly = true)
	public Collection<PetType> findNotAvailable() {
		return petTypeRepository.findNotAvailable();
	}
}
