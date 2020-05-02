
package org.springframework.samples.petclinic.service;

import java.time.LocalDate;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Stay;
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
	public Iterable<Stay> findAllPendingByVet(final Integer vetId) {
		LocalDate actualDate = LocalDate.now();
		return stayRepository.findAllPendingByVet(actualDate, vetId);
	}

	@Transactional(readOnly = true)
	public Iterable<Stay> findAllAcceptedByVet(final Integer vetId) {
		LocalDate actualDate = LocalDate.now();
		return stayRepository.findAllAcceptedByVet(actualDate, vetId);
	}

	@Transactional
	public void deleteByPetId(Integer id) {
		Collection<Stay> stay = stayRepository.findByPetId(id);
		stayRepository.deleteAll(stay);
	}

	@Transactional(readOnly = true)
	public Iterable<Stay> findAllStayByPet(final Integer petId) {
		return stayRepository.findByPetId(petId);
	}

	@Transactional(readOnly = true)
	public Iterable<Stay> findAllPendingByOwner(final Integer ownerId) {
		return stayRepository.findAllPendingByOwner(ownerId);
	}

	public Iterable<Stay> findAllAcceptedByOwner(final Integer ownerId) {
		return stayRepository.findAllAcceptedByOwner(ownerId);
	}

}
