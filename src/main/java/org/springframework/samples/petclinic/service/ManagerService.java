package org.springframework.samples.petclinic.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Manager;
import org.springframework.samples.petclinic.repository.ManagerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ManagerService {

	private ManagerRepository managerRepository;

	@Autowired
	public ManagerService(ManagerRepository managerRepository) {
		this.managerRepository = managerRepository;
	}

	@Transactional(readOnly = true)
	public Iterable<Manager> findAllManagers() {
		return this.managerRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Optional<Manager> findManagerById(final int managerId) {
		return this.managerRepository.findById(managerId);
	}

	@Transactional(readOnly = true)
	public Optional<Manager> findManagerByUsername(String user) {
		return this.managerRepository.findManagerByUsername(user);
	}

}
