/**
 * DP2 - Grupo 8
 * LAB F1.33
 * Date: 05-may-2020
 */

package org.springframework.samples.petclinic.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.samples.petclinic.model.Suggestion;

public interface SuggestionRepository extends BaseRepository<Suggestion> {

	@Query("SELECT s FROM Suggestion s WHERE s.isTrash = false ORDER BY isRead, created asc")
	Collection<Suggestion> findAllEntitiesNotTrashOrderByIsReadAndCreated();

	@Query("SELECT s FROM Suggestion s WHERE s.isTrash = true ORDER BY isRead, created asc")
	Collection<Suggestion> findAllEntitiesTrashOrderByIsReadAndCreated();

	@Modifying
	@Query("UPDATE Suggestion s SET s.isTrash = true WHERE s.isTrash = false")
	void moveAllTrash();

	@Query("SELECT s FROM Suggestion s WHERE s.user.username = ?1 and s.isAvailable = true ORDER BY created asc")
	Collection<Suggestion> findAllEntitiesAvailableByUsername(String username);

	@Modifying
	@Query("UPDATE Suggestion s SET s.isAvailable = false WHERE s.isAvailable = true and s.user = (SELECT u FROM User u WHERE u.username = ?1)")
	void updateAllIsAvailableFalse(String username);

}
