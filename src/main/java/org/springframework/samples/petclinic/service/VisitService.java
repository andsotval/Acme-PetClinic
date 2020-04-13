
package org.springframework.samples.petclinic.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.repository.BaseRepository;
import org.springframework.samples.petclinic.repository.VisitRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VisitService extends BaseService<Visit> {

	private VisitRepository visitRepository;


	@Autowired
	public VisitService(BaseRepository<Visit> repository, VisitRepository visitRepository) {
		super(repository);
		this.visitRepository = visitRepository;
	}

	@Transactional(readOnly = true)
	public Iterable<Visit> findAllPendingByVet(final Vet vet) {
		LocalDate actualDate = LocalDate.now();
		return visitRepository.findAllPendingByVet(actualDate, vet.getId());
	}

	@Transactional(readOnly = true)
	public Iterable<Visit> findAllAcceptedByVet(final Vet vet) {
		LocalDate actualDate = LocalDate.now();
		return visitRepository.findAllAcceptedByVet(actualDate, vet.getId());
	}

}
