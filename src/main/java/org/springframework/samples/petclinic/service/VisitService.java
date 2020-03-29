
package org.springframework.samples.petclinic.service;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Stay;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.repository.VisitRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VisitService {

	@Autowired
	private VisitRepository visitRepository;


	@Transactional
	public Iterable<Visit> findAllPendingByVet(final Vet vet) {
		return this.visitRepository.findAllPendingByVet(vet.getId());
	}

	@Transactional(readOnly = true)
	public Optional<Visit> findById(final int id) {
		return visitRepository.findById(id);
	}

	@Transactional
	public void delete(final Visit visit) {
		this.visitRepository.delete(visit);
	}

	@Transactional
	public void save(final Visit visit) {
		this.visitRepository.save(visit);
	}

	@Transactional
	public void cancelVisit(final Visit visit) {
		visit.setIsAccepted(false);
		this.save(visit);
	}

	@Transactional
	public void acceptVisit(final Visit visit) {
		visit.setIsAccepted(true);
		this.save(visit);
	}

	/*@Transactional
	public Iterable<Visit> findAllbyAcceptance(final boolean bool) {
		return this.visitRepository.findAllbyAcceptance(bool);
	}*/

	/*@Transactional
	public Iterable<Visit> findAllPending() {
		return this.visitRepository.findAllPending();
	}
	*/
	@Transactional(readOnly = true)
	public Iterable<Visit> findAllPending() {
		LocalDate actualDate = LocalDate.now();
		return visitRepository.findAllPending(actualDate);
	}

	@Transactional(readOnly = true)
	public Iterable<Visit> findAllAccepted() {
		LocalDate actualDate = LocalDate.now();
		return visitRepository.findAllAccepted(actualDate);
	}

}