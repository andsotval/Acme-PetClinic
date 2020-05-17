/**
 * DP2 - Grupo 8
 * LAB F1.33
 * Date: 05-may-2020
 */

package org.springframework.samples.petclinic.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.repository.BaseRepository;
import org.springframework.samples.petclinic.repository.SpecialtyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SpecialtyService extends BaseService<Specialty> {

	private SpecialtyRepository specialtyRepository;


	@Autowired
	public SpecialtyService(BaseRepository<Specialty> repository, SpecialtyRepository specialtyRepository) {
		super(repository);
		this.specialtyRepository = specialtyRepository;
	}

	@Transactional(readOnly = true)
	public Collection<Specialty> findAvailable() {
		return specialtyRepository.findAvailable();
	}

	@Transactional(readOnly = true)
	public Collection<Specialty> findNotAvailable() {
		return specialtyRepository.findNotAvailable();
	}
}
