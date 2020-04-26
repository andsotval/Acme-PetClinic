
package org.springframework.samples.petclinic.service;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Suggestion;
import org.springframework.samples.petclinic.model.User;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class SuggestionServiceTests {

	@Autowired
	private SuggestionService service;


	@Test
	public void testFindAllEntitiesNotTrashOrderByIsReadAndCreated() {
		Collection<Suggestion> collection = service.findAllEntitiesNotTrashOrderByIsReadAndCreated();
		assertEquals(collection.size(), 3);
	}

	@Test
	public void testFindAllEntitiesTrashOrderByIsReadAndCreated() {
		Collection<Suggestion> collection = service.findAllEntitiesTrashOrderByIsReadAndCreated();
		assertEquals(collection.size(), 1);
	}

	@Test
	public void testMoveAllTrash() {
		service.moveAllTrash();
		Collection<Suggestion> collection = service.findAllEntitiesNotTrashOrderByIsReadAndCreated();
		assertEquals(collection.size(), 0);
		collection = service.findAllEntitiesTrashOrderByIsReadAndCreated();
		assertEquals(collection.size(), 4);
	}

	@Test
	public void testDeleteAllTrash() {
		service.moveAllTrash();
		Collection<Suggestion> collection = service.findAllEntitiesTrashOrderByIsReadAndCreated();
		assertEquals(collection.size(), 4);

		service.deleteAllTrash(collection);
		collection = (Collection<Suggestion>) service.findAllEntities();
		assertEquals(collection.size(), 0);
	}

	@Test
	public void testFindAllEntitiesByUsername() {
		Collection<Suggestion> collection = service.findAllEntitiesAvailableByUsername("owner1");
		assertEquals(collection.size(), 2);
	}

	@Test
	public void testUpdateAllIsAvailableFalse() {
		Collection<Suggestion> collection = service.findAllEntitiesAvailableByUsername("owner1");
		assertEquals(collection.size(), 2);

		service.updateAllIsAvailableFalse("owner1");
		collection = service.findAllEntitiesAvailableByUsername("owner1");
		assertEquals(collection.size(), 0);
	}

	@Test
	public void testFindAllEntities() {
		Collection<Suggestion> collection = (Collection<Suggestion>) service.findAllEntities();
		assertEquals(collection.size(), 4);
	}

	@Test
	public void testFindEntityById() {
		Optional<Suggestion> entity = service.findEntityById(1);
		assertTrue(entity.isPresent());
	}

	@Test
	public void testSaveEntity() {
		Collection<Suggestion> collection = (Collection<Suggestion>) service.findAllEntities();
		assertEquals(collection.size(), 4);

		LocalDateTime date = LocalDateTime.now();
		Suggestion entity = new Suggestion();
		entity.setName("Name 1");
		entity.setDescription("Description 1");
		entity.setCreated(date);
		entity.setIsAvailable(true);
		entity.setIsRead(false);
		entity.setIsTrash(false);
		User user = new User();
		user.setId(1);
		entity.setUser(user);
		service.saveEntity(entity);

		collection = (Collection<Suggestion>) service.findAllEntities();
		assertEquals(collection.size(), 5);

		Optional<Suggestion> newEntity = service.findEntityById(5);
		assertTrue(newEntity.isPresent());
		assertEquals(newEntity.get().getName(), "Name 1");
		assertEquals(newEntity.get().getDescription(), "Description 1");
		assertEquals(newEntity.get().getCreated(), date);
		assertEquals(newEntity.get().getIsAvailable(), true);
		assertEquals(newEntity.get().getIsRead(), false);
		assertEquals(newEntity.get().getIsTrash(), false);
	}

	@Test
	public void testDeleteEntity() {
		Optional<Suggestion> entity = service.findEntityById(1);
		assertTrue(entity.isPresent());
		service.deleteEntity(entity.get());

		Optional<Suggestion> deleteEntity = service.findEntityById(1);
		assertTrue(!deleteEntity.isPresent());
	}

	@Test
	public void testDeleteEntityById() {
		service.deleteEntityById(1);

		Optional<Suggestion> entity = service.findEntityById(1);
		assertTrue(!entity.isPresent());
	}
}
