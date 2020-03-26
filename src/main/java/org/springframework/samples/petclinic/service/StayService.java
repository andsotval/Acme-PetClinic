package org.springframework.samples.petclinic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Stay;
import org.springframework.samples.petclinic.repository.StayRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StayService {

	@Autowired
	private StayRepository stayRepository;

	@Transactional
	public Iterable<Stay> findAllPending() {
		return stayRepository.findAllPending();
	}

	@Transactional
	public Iterable<Stay> findAllAccepted() {
		return stayRepository.findAllAccepted();
	}

}
