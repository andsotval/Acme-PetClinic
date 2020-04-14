
package org.springframework.samples.petclinic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Manager;
import org.springframework.samples.petclinic.repository.BaseRepository;
import org.springframework.samples.petclinic.repository.ManagerRepository;
import org.springframework.stereotype.Service;

@Service
public class ManagerService extends PersonService<Manager> {

	private ManagerRepository managerRepository;


	@Autowired
	public ManagerService(BaseRepository<Manager> repository, ManagerRepository managerRepository) {
		super(repository, Manager.class);
		this.managerRepository = managerRepository;
	}

}
