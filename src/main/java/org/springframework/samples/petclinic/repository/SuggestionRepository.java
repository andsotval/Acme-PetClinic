
package org.springframework.samples.petclinic.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.samples.petclinic.model.Suggestion;
import org.springframework.stereotype.Repository;

@Repository
public interface SuggestionRepository extends BaseRepository<Suggestion> {

	@Query("SELECT s FROM Suggestion s WHERE s.isTrash = false ORDER BY isRead, created asc")
	Collection<Suggestion> findAllEntitiesNotTrashOrderByIsReadAndCreated();

	@Query("SELECT s FROM Suggestion s WHERE s.isTrash = true ORDER BY isRead, created asc")
	Collection<Suggestion> findAllEntitiesTrashOrderByIsReadAndCreated();

	@Modifying
	@Query("UPDATE Suggestion s SET s.isTrash = true WHERE s.isTrash = false")
	void moveAllTrash();

}
