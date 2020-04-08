
package org.springframework.samples.petclinic.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Stay;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.repository.BaseRepository;
import org.springframework.samples.petclinic.repository.StayRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StayService extends BaseService<Stay> {

	private StayRepository stayRepository;


	@Autowired
	public StayService(BaseRepository<Stay> repository, StayRepository stayRepository) {
		super(repository);
		this.stayRepository = stayRepository;
	}

	@Transactional(readOnly = true)
	public Iterable<Stay> findAllPendingByVet(final Vet vet) {
		LocalDate actualDate = LocalDate.now();
		return stayRepository.findAllPendingByVet(actualDate, vet.getId());
	}

	@Transactional(readOnly = true)
	public Iterable<Stay> findAllAcceptedByVet(final Vet vet) {
		LocalDate actualDate = LocalDate.now();
		return stayRepository.findAllAcceptedByVet(actualDate, vet.getId());
	}

}
