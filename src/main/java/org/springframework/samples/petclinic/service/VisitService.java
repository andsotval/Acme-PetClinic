
package org.springframework.samples.petclinic.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.repository.VisitRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VisitService {

	@Autowired
	private VisitRepository visitRepository;


	@Transactional
	public Iterable<Visit> findAllPending() {
		return this.visitRepository.findAll();
	}
	
	@Transactional
	public Iterable<Visit> findAllAccepted() {
		return this.visitRepository.findAll();
	}
	
	@Transactional
	public Optional<Visit> findById(int id) {
		return this.visitRepository.findById(id);
	}
	
	public void delete(Visit visit) {
		this.visitRepository.delete(visit);
	}

}
