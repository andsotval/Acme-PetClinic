
package org.springframework.samples.petclinic.service;

import org.springframework.samples.petclinic.model.Manager;
import org.springframework.samples.petclinic.repository.BaseRepository;
import org.springframework.stereotype.Service;

@Service
public class ManagerService extends PersonService<Manager> {

	public ManagerService(BaseRepository<Manager> repository) {
		super(repository, Manager.class);
	}

}
