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
	public Iterable<Stay> findAllbyAcceptance(Boolean bool) {
		return stayRepository.findAllbyAcceptance(bool);
	}
	
	@Transactional
	public Stay findById(final int id) {
		return this.stayRepository.findByStayId(id);
	}

	@Transactional
	public void delete(final Stay stay) {
		this.stayRepository.delete(stay);
	}

	@Transactional
	public void save(final Stay stay) {
		this.stayRepository.save(stay);
	}

	@Transactional
	public void cancelStay(final Stay stay) {
		stay.setIsAccepted(false);
		this.save(stay);
	}

	@Transactional
	public void acceptStay(final Stay stay) {
		stay.setIsAccepted(true);
		this.save(stay);
	}
}
