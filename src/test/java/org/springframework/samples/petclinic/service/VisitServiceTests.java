
package org.springframework.samples.petclinic.service;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.stereotype.Service;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
class VisitServiceTests {

	@Autowired
	protected VisitService	visitService;

	private int				TEST_VISIT_ID					= 1;

	private int				TEST_VISIT_ID_NOT_PRESENT		= 100;

	private int				TEST_VET_ID						= 1;

	private int				TEST_VET_ID_NOT_PRESENT			= 100;

	private int				TEST_PET_ID						= 1;

	private int				TEST_PET_ID_NOT_PRESENT			= 100;

	private int				TEST_OWNER_ID					= 1;

	private int				TEST_OWNER_ID_CAN_UNSUBSCRIBE	= 2;

	private int				TEST_OWNER_ID_NOT_PRESENT		= 100;

	private LocalDateTime	TEST_DATE_TIME					= LocalDateTime.of(2020, 8, 9, 9, 30, 00);


	private Validator createValidator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.afterPropertiesSet();
		return localValidatorFactoryBean;
	}

	@Test
	public void testFindAllVisitsPendingByVetId() {
		Iterable<Visit> visits = visitService.findAllPendingByVetId(TEST_VET_ID);
		visits.forEach(v -> assertNull(v.getIsAccepted()));
	}

	@Test
	public void testFindAllVisitsPendingByVetIdNotPresent() {
		Collection<Visit> visits = visitService.findAllPendingByVetId(TEST_VET_ID_NOT_PRESENT);
		assertEquals(visits.size(), 0);
	}

	@Test
	public void testFindAllVisitsAcceptedByVetId() {
		Iterable<Visit> visits = visitService.findAllAcceptedByVetId(TEST_VET_ID);
		visits.forEach(v -> assertTrue(v.getIsAccepted()));
	}

	@Test
	public void testFindAllVisitsAcceptedByVetIdNotPresent() {
		Collection<Visit> visits = visitService.findAllAcceptedByVetId(TEST_VET_ID_NOT_PRESENT);
		assertEquals(visits.size(), 0);
	}

	@Test
	public void testDeleteVisitByPetId() {
		visitService.deleteByPetId(TEST_PET_ID);

		Collection<Visit> visits = (Collection<Visit>) visitService.findAllEntities();
		visits.forEach(v -> assertNotEquals(v.getPet().getId(), TEST_PET_ID));
	}

	@Test
	public void testDeleteVisitByPetIdNotPresent() {
		Collection<Visit> collection = (Collection<Visit>) visitService.findAllEntities();
		int collectionSize = collection.size();

		visitService.deleteByPetId(TEST_PET_ID_NOT_PRESENT);

		Collection<Visit> newCollection = (Collection<Visit>) visitService.findAllEntities();
		int newCollectionSize = newCollection.size();

		assertEquals(collectionSize, newCollectionSize);
	}

	@Test
	public void testFindAllVisitsPendingByOwnerId() {
		Collection<Visit> visits = visitService.findAllPendingByOwnerId(TEST_OWNER_ID);
		visits.forEach(v -> {
			assertNull(v.getIsAccepted());
			assertEquals(v.getPet().getOwner(), TEST_OWNER_ID);
		});
	}

	@Test
	public void testFindAllVisitsPendingByOwnerIdNotPresent() {
		Collection<Visit> visits = visitService.findAllPendingByOwnerId(TEST_OWNER_ID_NOT_PRESENT);
		assertEquals(visits.size(), 0);
	}

	@Test
	public void testFindAllVisitsAcceptedByOwnerId() {
		Collection<Visit> visits = visitService.findAllAcceptedByOwnerId(TEST_OWNER_ID);
		visits.forEach(v -> {
			assertTrue(v.getIsAccepted());
			assertEquals(v.getPet().getOwner().getId(), TEST_OWNER_ID);
		});
	}

	@Test
	public void testFindAllVisitsAcceptedByOwnerIdNotPresent() {
		Collection<Visit> visits = visitService.findAllAcceptedByOwnerId(TEST_OWNER_ID_NOT_PRESENT);
		assertEquals(visits.size(), 0);
	}

	@Test
	public void testFindAllVisitsByPetId() {
		Collection<Visit> visits = visitService.findAllByPetId(TEST_PET_ID);
		visits.forEach(v -> assertEquals(v.getPet().getId(), TEST_PET_ID));
	}

	@Test
	public void testFindAllVisitsByPetIdNotPresent() {
		Collection<Visit> visits = visitService.findAllByPetId(TEST_PET_ID_NOT_PRESENT);
		assertEquals(visits.size(), 0);
	}

	@Test
	public void testFindAllVisitsByDateTime() {
		Collection<Visit> visits = visitService.findAllByDateTime(TEST_DATE_TIME);
		visits.forEach(v -> assertEquals(v.getDateTime(), TEST_DATE_TIME));
	}

	@Test
	public void testFindAllVisitsByDateTimeWithNullDateTime() {
		Collection<Visit> visits = visitService.findAllByDateTime(null);
		assertEquals(visits.size(), 0);
	}

	@Test
	public void testCanUnsubscribeWithOwnerAllowed() {
		Boolean canUnsubscribe = visitService.canUnsubscribe(TEST_OWNER_ID_CAN_UNSUBSCRIBE);
		assertTrue(canUnsubscribe);
	}

	@Test
	public void testCanUnsubscribeWithOwnerNotAllowed() {
		Boolean canUnsubscribe = visitService.canUnsubscribe(TEST_OWNER_ID);
		assertFalse(canUnsubscribe);
	}

	@Test
	public void testFindAllVisits() {
		Collection<Visit> collection = (Collection<Visit>) visitService.findAllEntities();
		assertEquals(collection.size(), 11);
	}

	@Test
	public void testFindVisitsById() {
		Optional<Visit> entity = visitService.findEntityById(TEST_VISIT_ID);
		assertTrue(entity.isPresent());
		assertTrue(entity.get().getId().equals(TEST_VET_ID));
	}

	@Test
	public void testFindVisitByIdNotPresent() {
		Optional<Visit> entity = visitService.findEntityById(TEST_VISIT_ID_NOT_PRESENT);
		assertTrue(!entity.isPresent());
	}

	@Test
	public void testSaveVisit() {
		Collection<Visit> collection = (Collection<Visit>) visitService.findAllEntities();
		int size = collection.size();

		LocalDateTime now = LocalDateTime.now();
		Visit entity = new Visit();
		entity.setDescription("Description of the visit");
		entity.setDateTime(now);
		visitService.saveEntity(entity);

		collection = (Collection<Visit>) visitService.findAllEntities();
		assertEquals(collection.size(), size + 1);

		Optional<Visit> newEntity = visitService.findEntityById(size + 1);
		assertTrue(newEntity.isPresent());
		assertEquals(newEntity.get().getDescription(), "Description of the visit");
		assertEquals(newEntity.get().getDateTime(), now);
	}

	@Test
	public void testSaveVisitWithoutDescription() {
		LocalDateTime now = LocalDateTime.now();
		Visit entity = new Visit();
		entity.setDateTime(now);

		Validator validator = createValidator();
		Set<ConstraintViolation<Visit>> constraintViolations = validator.validate(entity);
		assertEquals(constraintViolations.size(), 1);

		Iterator<ConstraintViolation<Visit>> it = constraintViolations.iterator();
		while (it.hasNext()) {
			ConstraintViolation<Visit> violation = it.next();
			String message = violation.getMessage();

			switch (violation.getPropertyPath().toString()) {
			case "description":
				assertTrue(message.equals("must not be empty") || message.equals("no puede estar vacío"));
				break;
			default:
				break;
			}
		}

	}

	@Test
	public void testDeleteVisit() {
		Optional<Visit> entity = visitService.findEntityById(TEST_VISIT_ID);
		assertTrue(entity.isPresent());
		visitService.deleteEntity(entity.get());

		Optional<Visit> deleteEntity = visitService.findEntityById(TEST_VISIT_ID);
		assertTrue(!deleteEntity.isPresent());
	}

	@Test
	public void testDeleteVisitNotPresent() {
		Collection<Visit> collection = (Collection<Visit>) visitService.findAllEntities();
		int collectionSize = collection.size();

		visitService.deleteEntity(null);

		Collection<Visit> newCollection = (Collection<Visit>) visitService.findAllEntities();
		int newCollectionSize = newCollection.size();

		assertEquals(collectionSize, newCollectionSize);
	}

	@Test
	public void testDeleteVisitById() {
		visitService.deleteEntityById(TEST_VET_ID);

		Optional<Visit> entity = visitService.findEntityById(TEST_VISIT_ID);
		assertTrue(!entity.isPresent());
	}

	@Test
	public void testDeleteVisitByIdNotPresent() {
		boolean deleted = true;

		try {
			visitService.deleteEntityById(TEST_VISIT_ID_NOT_PRESENT);
		} catch (EmptyResultDataAccessException e) {
			deleted = false;
		}

		assertFalse(deleted);
	}

}
