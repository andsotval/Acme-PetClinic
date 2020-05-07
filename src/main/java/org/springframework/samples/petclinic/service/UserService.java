/**
 * DP2 - Grupo 8
 * LAB F1.33
 * Date: 05-may-2020
 */

package org.springframework.samples.petclinic.service;

import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.repository.BaseRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService extends PersonService<User> {

	public UserService(BaseRepository<User> repository) {
		super(repository, User.class);
	}

}
