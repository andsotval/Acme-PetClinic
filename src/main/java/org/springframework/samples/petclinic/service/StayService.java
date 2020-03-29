
package org.springframework.samples.petclinic.service;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Stay;
import org.springframework.samples.petclinic.repository.StayRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StayService {

	@Autowired
	private StayRepository stayRepository;


	@Transactional(readOnly = true)
	public Iterable<Stay> findAllPending() {
		LocalDate actualDate = LocalDate.now();
		return stayRepository.findAllPending(actualDate);
	}

	@Transactional(readOnly = true)
	public Iterable<Stay> findAllAccepted() {
		LocalDate actualDate = LocalDate.now();
		return stayRepository.findAllAccepted(actualDate);
	}

	@Transactional(readOnly = true)
	public Optional<Stay> findById(final int id) {
		return stayRepository.findById(id);
	}

	@Transactional
	public void delete(final Stay stay) {
		stayRepository.delete(stay);
	}

	@Transactional
	public void save(final Stay stay) {
		stayRepository.save(stay);
	}

	@Transactional
	public void cancelStay(final Stay stay) {
		stay.setIsAccepted(false);
		save(stay);
	}

	@Transactional
	public void acceptStay(final Stay stay) {
		stay.setIsAccepted(true);
		save(stay);
	}
}
