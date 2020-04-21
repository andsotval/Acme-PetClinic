
package org.springframework.samples.petclinic.service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Stay;
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

	public void deleteByPetId(Integer id) {
		Collection<Visit> visit = this.visitRepository.findByPetId(id);
		visitRepository.deleteAll(visit);
		
	}
	
	@Transactional(readOnly = true)
	public Iterable<Visit> findAllPendingByOwner(final Owner owner) {
		return visitRepository.findAllPendingByOwner(owner.getId());
	}

}
