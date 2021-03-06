/**
 * DP2 - Grupo 8
 * LAB F1.33
 * Date: 05-may-2020
 */

package org.springframework.samples.petclinic.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.repository.BaseRepository;
import org.springframework.samples.petclinic.repository.NamedRepository;
import org.springframework.samples.petclinic.repository.PetRepository;
import org.springframework.stereotype.Service;

@Service
public class PetService extends NamedService<Pet> {

	private PetRepository petRepository;


	@Autowired
	public PetService(BaseRepository<Pet> repository, NamedRepository<Pet> namedRepository,
		PetRepository petRepository) {
		super(repository, namedRepository);
		this.petRepository = petRepository;
	}

	public Collection<Pet> findPetsByOwnerId(Integer id) {
		return petRepository.findPetsByOwnerId(id);
	}

}
