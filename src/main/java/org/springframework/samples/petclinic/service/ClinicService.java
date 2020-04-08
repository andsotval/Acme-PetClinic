
package org.springframework.samples.petclinic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Clinic;
import org.springframework.samples.petclinic.repository.BaseRepository;
import org.springframework.samples.petclinic.repository.ClinicRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClinicService extends BaseService<Clinic> {

	private ClinicRepository clinicRepository;


	@Autowired
	public ClinicService(BaseRepository<Clinic> repository, ClinicRepository clinicRepository) {
		super(repository);
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
}
