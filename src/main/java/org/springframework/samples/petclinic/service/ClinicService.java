package org.springframework.samples.petclinic.service;

import java.util.Optional;

import org.springframework.samples.petclinic.model.Clinic;
import org.springframework.samples.petclinic.model.Manager;
import org.springframework.samples.petclinic.repository.ClinicRepository;

public class ClinicService {

	private ClinicRepository clinicRepository;
	
	public Optional<Clinic> findClinicByName(String name) {
		return this.clinicRepository.findClinicByName(name);
	}
}
