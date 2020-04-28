
package org.springframework.samples.petclinic.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
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
	public Iterable<Visit> findAllPendingByVetId(Integer vetId) {
		LocalDateTime actualDate = LocalDateTime.now();
		return visitRepository.findAllPendingByVetId(actualDate, vetId);
	}

	@Transactional(readOnly = true)
	public Iterable<Visit> findAllAcceptedByVetId(Integer vetId) {
		LocalDateTime actualDate = LocalDateTime.now();
		return visitRepository.findAllAcceptedByVetId(actualDate, vetId);
	}

	@Transactional
	public void deleteByPetId(Integer id) {
		Iterable<Visit> visit = visitRepository.findAllByPetId(id);
		visitRepository.deleteAll(visit);

	}

	@Transactional(readOnly = true)
	public Iterable<Visit> findAllPendingByOwnerId(Integer ownerId) {
		return visitRepository.findAllPendingByOwnerId(ownerId);
	}

	@Transactional
	public Iterable<Visit> findAllAcceptedByOwnerId(Integer ownerId) {
		return visitRepository.findAllAcceptedByOwnerId(ownerId);
	}

	@Transactional(readOnly = true)
	public Iterable<Visit> findAllByPetId(Integer petId) {
		return visitRepository.findAllByPetId(petId);
	}

	public Iterable<Visit> findAllByDateTime(LocalDateTime dateTime) {
		return visitRepository.findVisitsByDateTime(dateTime);
	}
	
	
	/* MÉTODO PARA LA SIGUIENTE FASE
	public boolean isInInterval(LocalDateTime dateTime) {
		LocalTime opening = LocalTime.of(8, 0);
		LocalTime closing = LocalTime.of(20, 0);
		LocalTime visitTime = LocalTime.of(dateTime.getHour(), dateTime.getMinute());
		if(visitTime.isAfter(opening) && visitTime.isBefore(closing)) {
			return true;
		} else {
			return false;
		}
	}
	 */
}
