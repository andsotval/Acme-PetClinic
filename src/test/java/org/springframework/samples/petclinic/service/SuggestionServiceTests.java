/**
 * DP2 - Grupo 8
 * LAB F1.33
 * Date: 17-may-2020
 */

package org.springframework.samples.petclinic.service;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

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
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.samples.petclinic.model.Suggestion;
import org.springframework.samples.petclinic.model.User;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@ActiveProfiles("hsqldb")
@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class SuggestionServiceTests {

	@Autowired
	private SuggestionService	suggestionService;

	private String				TEST_USERNAME					= "owner1";

	private String				TEST_USERNAME_NOT_PRESENT		= "username that is not present";

	private int					TEST_SUGGESTION_ID				= 1;

	private int					TEST_SUGGESTION_ID_NOT_PRESENT	= 100;


	private Validator createValidator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.afterPropertiesSet();
		return localValidatorFactoryBean;
	}

	//A suggestion can be in the TRASH or not
	//Once a suggestion is in the TRASH, it can be deleted
	//Furthermore, a suggestion can be readed

	@Test
	public void testFindAllSuggestionsNotTrashOrderByIsReadAndCreated() {
		Collection<Suggestion> suggestions = suggestionService.findAllEntitiesNotTrashOrderByIsReadAndCreated();
		suggestions.forEach(s -> assertEquals(s.getIsTrash(), false));
	}

	@Test
	public void testFindAllSuggestionsTrashOrderByIsReadAndCreated() {
		Collection<Suggestion> suggestions = suggestionService.findAllEntitiesTrashOrderByIsReadAndCreated();
		suggestions.forEach(s -> assertEquals(s.getIsTrash(), true));
	}

	@Test
	public void testMoveAllSuggestionsToTrash() {
		Collection<Suggestion> suggestions = suggestionService.findAllEntitiesNotTrashOrderByIsReadAndCreated();
		suggestions.forEach(s -> assertEquals(s.getIsTrash(), false));

		suggestionService.moveAllTrash();

		suggestions = suggestionService.findAllEntitiesNotTrashOrderByIsReadAndCreated();
		assertEquals(suggestions.size(), 0);

	}

	@Test
	public void testDeleteAllSuggestionsInTheTrash() {
		Collection<Suggestion> suggestions = suggestionService.findAllEntitiesNotTrashOrderByIsReadAndCreated();
		suggestions.forEach(s -> assertEquals(s.getIsTrash(), false));

		suggestionService.moveAllTrash();

		suggestions = suggestionService.findAllEntitiesTrashOrderByIsReadAndCreated();
		suggestionService.deleteAllTrash(suggestions);

		suggestions = (Collection<Suggestion>) suggestionService.findAllEntities();
		assertEquals(suggestions.size(), 0);
	}

	@Test
	public void testFindAllSuggestionsAvailableByUsername() {
		Collection<Suggestion> suggestions = suggestionService.findAllEntitiesAvailableByUsername(TEST_USERNAME);
		suggestions.forEach(s -> {
			assertEquals(s.getUser().getUsername(), TEST_USERNAME);
			assertEquals(s.getIsAvailable(), true);
		});
	}

	@Test
	public void testFindAllSuggestionsAvailableByUsernameNotPresent() {
		Collection<Suggestion> suggestions = suggestionService
			.findAllEntitiesAvailableByUsername(TEST_USERNAME_NOT_PRESENT);
		assertEquals(suggestions.size(), 0);
	}

	@Test
	public void testUpdateAllSuggestionsToNotAvailable() {
		Collection<Suggestion> suggestions = suggestionService.findAllEntitiesAvailableByUsername(TEST_USERNAME);
		suggestions.forEach(s -> {
			assertEquals(s.getUser().getUsername(), "owner1");
			assertEquals(s.getIsAvailable(), true);
		});

		suggestionService.updateAllIsAvailableFalse(TEST_USERNAME);
		suggestions = suggestionService.findAllEntitiesAvailableByUsername(TEST_USERNAME);
		assertEquals(suggestions.size(), 0);
	}

	@Test
	public void testUpdateAllSuggestionsToNotAvailableOfUserNotPresent() {
		Collection<Suggestion> suggestions = suggestionService
			.findAllEntitiesAvailableByUsername(TEST_USERNAME_NOT_PRESENT);
		assertEquals(suggestions.size(), 0);

		suggestionService.updateAllIsAvailableFalse(TEST_USERNAME_NOT_PRESENT);

		suggestions = suggestionService.findAllEntitiesAvailableByUsername(TEST_USERNAME_NOT_PRESENT);
		assertEquals(suggestions.size(), 0);
	}

	@Test
	public void testFindAllSuggestions() {
		Collection<Suggestion> collection = (Collection<Suggestion>) suggestionService.findAllEntities();
		assertEquals(collection.size(), 4);
	}

	@Test
	public void testFindSuggestionById() {
		Optional<Suggestion> entity = suggestionService.findEntityById(TEST_SUGGESTION_ID);
		assertTrue(entity.isPresent());
		assertTrue(entity.get().getId().equals(TEST_SUGGESTION_ID));
	}

	@Test
	public void testFindSuggestionByIdNotPresent() {
		Optional<Suggestion> entity = suggestionService.findEntityById(TEST_SUGGESTION_ID_NOT_PRESENT);
		assertTrue(!entity.isPresent());
	}

	@Test
	public void testSaveSuggestion() {
		Collection<Suggestion> collection = (Collection<Suggestion>) suggestionService.findAllEntities();
		int collectionSize = collection.size();

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
		suggestionService.saveEntity(entity);

		collection = (Collection<Suggestion>) suggestionService.findAllEntities();
		assertEquals(collection.size(), collectionSize + 1);

		Optional<Suggestion> newEntity = suggestionService.findEntityById(collectionSize + 1);
		assertTrue(newEntity.isPresent());
		assertEquals(newEntity.get().getName(), "Name 1");
		assertEquals(newEntity.get().getDescription(), "Description 1");
		assertEquals(newEntity.get().getCreated(), date);
		assertEquals(newEntity.get().getIsAvailable(), true);
		assertEquals(newEntity.get().getIsRead(), false);
		assertEquals(newEntity.get().getIsTrash(), false);
	}

	@Test
	public void testSaveSuggestionWithNullParameters() {
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
	public void testDeleteSuggestion() {
		Optional<Suggestion> entity = suggestionService.findEntityById(TEST_SUGGESTION_ID);
		assertTrue(entity.isPresent());
		suggestionService.deleteEntity(entity.get());

		Optional<Suggestion> deleteEntity = suggestionService.findEntityById(TEST_SUGGESTION_ID);
		assertTrue(!deleteEntity.isPresent());
	}

	@Test
	public void testDeleteStayNotPresent() {
		Collection<Suggestion> collection = (Collection<Suggestion>) suggestionService.findAllEntities();
		int collectionSize = collection.size();

		suggestionService.deleteEntity(null);

		Collection<Suggestion> newCollection = (Collection<Suggestion>) suggestionService.findAllEntities();
		int newCollectionSize = newCollection.size();

		assertEquals(collectionSize, newCollectionSize);
	}

	@Test
	public void testDeleteSuggestionById() {
		suggestionService.deleteEntityById(TEST_SUGGESTION_ID);

		Optional<Suggestion> entity = suggestionService.findEntityById(TEST_SUGGESTION_ID);
		assertTrue(!entity.isPresent());
	}

	@Test
	public void testDeleteSuggestionByIdNotPresent() {
		boolean deleted = true;

		try {
			suggestionService.deleteEntityById(TEST_SUGGESTION_ID_NOT_PRESENT);
		} catch (EmptyResultDataAccessException e) {
			deleted = false;
		}

		assertFalse(deleted);
	}
}
