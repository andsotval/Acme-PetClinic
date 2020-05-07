/**
 * DP2 - Grupo 8
 * LAB F1.33
 * Date: 05-may-2020
 */

package org.springframework.samples.petclinic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Authorities;
import org.springframework.samples.petclinic.repository.AuthoritiesRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthoritiesService {

	private AuthoritiesRepository authoritiesRepository;


	@Autowired
	public AuthoritiesService(final AuthoritiesRepository authoritiesRepository) {
		this.authoritiesRepository = authoritiesRepository;
	}

	@Transactional(readOnly = true)
	public String findAuthorityByUsername(String username) {
		return authoritiesRepository.findAuthorityByUsername(username);
	}

	@Transactional
	public void saveAuthorities(final Authorities authorities) throws DataAccessException {
		authoritiesRepository.save(authorities);
	}

}
