
package org.springframework.samples.petclinic.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
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
		Set<Vet> listInteger = new HashSet<Vet>();

		listInteger.add(vet);
		return this.visitRepository.findAllPendingByVet(listInteger);
	}

	public Visit findById(final int id) {
		return this.visitRepository.findByVisitId(id);
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

	@Transactional
	public Iterable<Visit> findAllbyAcceptance(boolean bool) {
		return this.visitRepository.findAllbyAcceptance(bool);
	}
	
	@Transactional
	public Iterable<Visit> findAllPending() {
		return this.visitRepository.findAllPending();
	}

}
