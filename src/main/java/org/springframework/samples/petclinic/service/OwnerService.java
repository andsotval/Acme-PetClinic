/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.samples.petclinic.service;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.repository.BaseRepository;
import org.springframework.samples.petclinic.repository.OwnerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Mostly used as a facade for all Petclinic controllers Also a placeholder
 * for @Transactional and @Cacheable annotations
 *
 * @author Michael Isvy
 */
@Service
public class OwnerService extends PersonService<Owner> {

	private OwnerRepository		ownerRepository;
	private AuthoritiesService	authoritiesService;
	private UserService			userService;


	@Autowired
	public OwnerService(BaseRepository<Owner> repository, OwnerRepository ownerRepository) {
		super(repository, Owner.class);
		this.ownerRepository = ownerRepository;
	}

	@Transactional(readOnly = true)
	public Collection<Owner> findOwnerByLastName(final String lastName) throws DataAccessException {
		return ownerRepository.findByLastName(lastName);
	}

	@Transactional
	public Optional<Owner> findByOwnerByUsername(final String username) {
		return ownerRepository.findByOwnerByUsername(username);
	}

	@Transactional
	public void saveOwner(final Owner owner) throws DataAccessException {
		//creating owner
		ownerRepository.save(owner);
		//creating user
		//this.userService.saveUser(owner.getUser());
		//creating authorities
		//this.authoritiesService.saveAuthorities(owner.getUser().getUsername(), "owner");
	}

}
