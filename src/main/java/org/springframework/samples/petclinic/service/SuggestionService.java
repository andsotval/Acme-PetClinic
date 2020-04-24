
package org.springframework.samples.petclinic.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Suggestion;
import org.springframework.samples.petclinic.repository.BaseRepository;
import org.springframework.samples.petclinic.repository.SuggestionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SuggestionService extends BaseService<Suggestion> {

	private SuggestionRepository suggestionRepository;


	@Autowired
	public SuggestionService(BaseRepository<Suggestion> repository, SuggestionRepository suggestionRepository) {
		super(repository);
		this.suggestionRepository = suggestionRepository;
	}

	@Transactional(readOnly = true)
	public Collection<Suggestion> findAllEntitiesNotTrashOrderByIsReadAndCreated() {
		return suggestionRepository.findAllEntitiesNotTrashOrderByIsReadAndCreated();
	}

	@Transactional(readOnly = true)
	public Collection<Suggestion> findAllEntitiesTrashOrderByIsReadAndCreated() {
		return suggestionRepository.findAllEntitiesTrashOrderByIsReadAndCreated();
	}

	@Transactional
	public void moveAllTrash() {
		suggestionRepository.moveAllTrash();
	}

	@Transactional
	public void deleteAllTrash(Collection<Suggestion> collection) {
		suggestionRepository.deleteAll(collection);
	}

	@Transactional(readOnly = true)
	public Collection<Suggestion> findAllEntitiesByUsername(String username) {
		return suggestionRepository.findAllEntitiesByUsername(username);
	}

	@Transactional
	public void updateAllIsAvailableFalse(String username) {
		suggestionRepository.updateAllIsAvailableFalse(username);
	}
}
