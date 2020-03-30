
package org.springframework.samples.petclinic.service;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Stay;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.repository.StayRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StayService {

	@Autowired
	private StayRepository stayRepository;


	@Transactional(readOnly = true)
	public Iterable<Stay> findAllPendingByVet(final Vet vet) {
		LocalDate actualDate = LocalDate.now();
		return this.stayRepository.findAllPendingByVet(actualDate, vet.getId());
	}

	@Transactional(readOnly = true)
	public Iterable<Stay> findAllAcceptedByVet(final Vet vet) {
		LocalDate actualDate = LocalDate.now();
		return this.stayRepository.findAllAcceptedByVet(actualDate, vet.getId());
	}

	@Transactional(readOnly = true)
	public Optional<Stay> findById(final int id) {
		return this.stayRepository.findById(id);
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
