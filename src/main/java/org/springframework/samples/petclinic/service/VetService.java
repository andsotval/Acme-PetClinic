/**
 * DP2 - Grupo 8
 * LAB F1.33
 * Date: 05-may-2020
 */

package org.springframework.samples.petclinic.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.repository.BaseRepository;
import org.springframework.samples.petclinic.repository.VetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VetService extends PersonService<Vet> {

	private VetRepository vetRepository;


	@Autowired
	public VetService(BaseRepository<Vet> repository, VetRepository vetRepository) {
		super(repository, Vet.class);
		this.vetRepository = vetRepository;
	}

	@Transactional(readOnly = true)
	public Collection<Vet> findAvailableVets() {
		return vetRepository.findAvailableVets();
	}

	@Transactional(readOnly = true)
	public Collection<Vet> findVetsByManager(Integer managerId) {
		return vetRepository.findVetsByManager(managerId);
	}

	@Transactional(readOnly = true)
	public Collection<Vet> findVetsByClinicId(Integer clinicId) {
		return vetRepository.findVetsByClinicId(clinicId);
	}

}
