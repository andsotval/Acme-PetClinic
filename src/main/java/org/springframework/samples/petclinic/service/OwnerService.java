/**
 * DP2 - Grupo 8
 * LAB F1.33
 * Date: 05-may-2020
 */

package org.springframework.samples.petclinic.service;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.repository.BaseRepository;
import org.springframework.samples.petclinic.repository.OwnerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OwnerService extends PersonService<Owner> {

	private OwnerRepository ownerRepository;


	@Autowired
	public OwnerService(final BaseRepository<Owner> repository, final OwnerRepository ownerRepository) {
		super(repository, Owner.class);
		this.ownerRepository = ownerRepository;
	}

	@Transactional(readOnly = true)
	public Collection<Owner> findOwnerByLastName(final String lastName) throws DataAccessException {
		return ownerRepository.findByLastName(lastName);
	}

	@Transactional(readOnly = true)
	public Owner findByOwnerByUsername(final String username) {
		return ownerRepository.findByOwnerByUsername(username);
	}

	@Transactional(readOnly = true)
	public List<Owner> findOwnersByClinicId(final Integer id) {
		return ownerRepository.findByClinicId(id);
	}

}
