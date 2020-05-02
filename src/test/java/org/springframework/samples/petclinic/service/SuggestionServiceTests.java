
package org.springframework.samples.petclinic.service;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.samples.petclinic.model.Suggestion;
import org.springframework.samples.petclinic.model.User;
import org.springframework.stereotype.Service;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class SuggestionServiceTests {

	@Autowired
	private SuggestionService service;


	private Validator createValidator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.afterPropertiesSet();
		return localValidatorFactoryBean;
	}

	@Test
	public void testFindAllEntitiesNotTrashOrderByIsReadAndCreated() {
		Collection<Suggestion> collection = service.findAllEntitiesNotTrashOrderByIsReadAndCreated();
		assertEquals(collection.size(), 3);

		collection.forEach(s -> {
			assertEquals(s.getIsTrash(), false);
		});
	}

	@Test
	public void testFindAllEntitiesTrashOrderByIsReadAndCreated() {
		Collection<Suggestion> collection = service.findAllEntitiesTrashOrderByIsReadAndCreated();
		assertEquals(collection.size(), 1);

		collection.forEach(s -> {
			assertEquals(s.getIsTrash(), true);
		});
	}

	@Test
	public void testMoveAllTrash() {
		Collection<Suggestion> collection = service.findAllEntitiesNotTrashOrderByIsReadAndCreated();
		assertEquals(collection.size(), 3);
		collection.forEach(s -> {
			assertEquals(s.getIsTrash(), false);
		});

		service.moveAllTrash();
		collection = service.findAllEntitiesNotTrashOrderByIsReadAndCreated();
		assertEquals(collection.size(), 0);

	}

	@Test
	public void testDeleteAllTrash() {
		Collection<Suggestion> collection = service.findAllEntitiesNotTrashOrderByIsReadAndCreated();
		assertEquals(collection.size(), 3);
		collection.forEach(s -> {
			assertEquals(s.getIsTrash(), false);
		});

		service.moveAllTrash();
		collection = service.findAllEntitiesTrashOrderByIsReadAndCreated();
		assertEquals(collection.size(), 4);

		service.deleteAllTrash(collection);
		collection = (Collection<Suggestion>) service.findAllEntities();
		assertEquals(collection.size(), 0);
	}

	@Test
	public void testFindAllEntitiesByUsername() {
		Collection<Suggestion> collection = service.findAllEntitiesAvailableByUsername("owner1");
		assertEquals(collection.size(), 2);
		collection.forEach(s -> {
			assertEquals(s.getUser().getUsername(), "owner1");
			assertEquals(s.getIsAvailable(), true);
		});
	}

	@Test
	public void testUpdateAllIsAvailableFalse() {
		Collection<Suggestion> collection = service.findAllEntitiesAvailableByUsername("owner1");
		assertEquals(collection.size(), 2);
		collection.forEach(s -> {
			assertEquals(s.getUser().getUsername(), "owner1");
			assertEquals(s.getIsAvailable(), true);
		});

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
	public void testFindEntityByIdPositive() {
		Optional<Suggestion> entity = service.findEntityById(1);
		assertTrue(entity.isPresent());
		assertTrue(entity.get().getId().equals(1));
	}

	@Test
	public void testFindEntityByIdNegative() {
		Optional<Suggestion> entity = service.findEntityById(99);
		assertTrue(!entity.isPresent());
	}

	@Test
	public void testSaveEntityPositive() {
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
	public void testSaveEntityNegative() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Suggestion entity = new Suggestion();
		entity.setName("Name 1");
		entity.setDescription("");
		entity.setCreated(null);
		entity.setIsAvailable(null);
		entity.setIsRead(null);
		entity.setIsTrash(null);
		entity.setUser(null);

		Validator validator = createValidator();
		Set<ConstraintViolation<Suggestion>> constraintViolations = validator.validate(entity);
		assertEquals(constraintViolations.size(), 6);

		Iterator<ConstraintViolation<Suggestion>> it = constraintViolations.iterator();
		while (it.hasNext()) {
			ConstraintViolation<Suggestion> violation = it.next();
			String message = violation.getMessage();

			switch (violation.getPropertyPath().toString()) {
			case "description":
				assertTrue(message.equals("must not be empty") || message.equals("length must be between 3 and 250"));
				break;
			case "created":
				assertEquals(message, "must not be null");
				break;
			case "isRead":
				assertEquals(message, "must not be null");
				break;
			case "isTrash":
				assertEquals(message, "must not be null");
				break;
			case "isAvailable":
				assertEquals(message, "must not be null");
				break;
			case "user":
				assertEquals(message, "must not be null");
				break;
			default:
				break;
			}
		}
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
