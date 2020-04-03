package org.springframework.samples.petclinic.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Clinic;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.repository.ClinicRepository;
import org.springframework.stereotype.Service;
@Service
public class ClinicService {

	@Autowired
	private ClinicRepository clinicRepository;
	
	public Optional<Clinic> findClinicByName(String name) {
		return this.clinicRepository.findClinicByName(name);
	}

	public Clinic findClinicByManagerId(Integer id) {
		return this.clinicRepository.findClinicByManagerId(id);
	}
	
	public Iterable<Pet> findPetsCyClinic(Clinic id){
		return this.clinicRepository.findPetsByClinic(id);
	}
}
