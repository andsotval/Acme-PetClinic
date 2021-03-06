/**
 * DP2 - Grupo 8
 * LAB F1.33
 * Date: 05-may-2020
 */

package org.springframework.samples.petclinic.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Clinic;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.repository.BaseRepository;
import org.springframework.samples.petclinic.repository.ClinicRepository;
import org.springframework.samples.petclinic.repository.NamedRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClinicService extends NamedService<Clinic> {

	private ClinicRepository clinicRepository;


	@Autowired
	public ClinicService(BaseRepository<Clinic> repository, NamedRepository<Clinic> namedRepository,
		ClinicRepository clinicRepository) {
		super(repository, namedRepository);
		this.clinicRepository = clinicRepository;
	}

	@Transactional(readOnly = true)
	public Clinic findClinicByName(String name) {
		return clinicRepository.findClinicByName(name);
	}

	@Transactional(readOnly = true)
	public Clinic findClinicByManagerId(Integer id) {
		return clinicRepository.findClinicByManagerId(id);
	}

	@Transactional(readOnly = true)
	public Collection<Pet> findPetsByClinicId(Integer id) {
		return clinicRepository.findPetsByClinicId(id);
	}
}
