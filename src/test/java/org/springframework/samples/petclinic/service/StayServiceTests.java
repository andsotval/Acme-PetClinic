package org.springframework.samples.petclinic.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.samples.petclinic.model.Stay;
import org.springframework.stereotype.Service;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
class StayServiceTests {

	@Autowired
	protected StayService stayService;

	private int TEST_STAY_ID = 1;

	private int TEST_STAY_ID_NOT_PRESENT = 100;

	private int TEST_OWNER_ID = 1;

	private int TEST_OWNER_ID_CAN_UNSUBSCRIBE = 4;

	private int TEST_PET_ID = 1;

	private int TEST_PET_ID_NOT_PRESENT = 100;

	private int TEST_VET_ID = 1;

	private int TEST_VET_ID_NOT_PRESENT = 100;

	private Validator createValidator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.afterPropertiesSet();
		return localValidatorFactoryBean;
	}

	@Test
	public void testFindAllPendingByVetId() {
		Collection<Stay> stays = stayService.findAllPendingByVet(TEST_VET_ID);
		stays.forEach(stay -> Assert.assertEquals(stay.getIsAccepted(), null));
	}

	public void testFindAllPendingByVetIdNotPresent() {
		Collection<Stay> stays = stayService.findAllPendingByVet(TEST_VET_ID_NOT_PRESENT);
		assertEquals(stays.size(), 0);
	}

	@Test
	public void testFindAllAcceptedByVetId() {
		Collection<Stay> stays = stayService.findAllAcceptedByVet(TEST_VET_ID);
		stays.forEach(stay -> Assert.assertEquals(stay.getIsAccepted(), true));
	}

	@Test
	public void testFindAllAcceptedByVetIdNotPresent() {
		Collection<Stay> stays = stayService.findAllAcceptedByVet(TEST_VET_ID_NOT_PRESENT);
		assertEquals(stays.size(), 0);
	}

	@Test
	public void testDeleteByPetId() {
		stayService.deleteByPetId(TEST_PET_ID);

		Collection<Stay> stays = (Collection<Stay>) stayService.findAllStayByPet(TEST_PET_ID);
		assertEquals(stays.size(), 0);
	}

	@Test
	public void testDeleteByPetIdNotPresent() {
		Collection<Stay> staysBefore = (Collection<Stay>) stayService.findAllEntities();
		Integer numStaysBefore = staysBefore.size();

		stayService.deleteByPetId(TEST_PET_ID_NOT_PRESENT);

		Collection<Stay> staysAfter = (Collection<Stay>) stayService.findAllEntities();
		Integer numStaysAfter = staysAfter.size();
		assertEquals(numStaysBefore, numStaysAfter);
	}

	@Test
	public void testFindAllStayByPet() {
		Collection<Stay> stays = (Collection<Stay>) stayService.findAllStayByPet(TEST_PET_ID);
		stays.forEach(s -> assertEquals(s.getPet().getId(), TEST_PET_ID));

	}

	@Test
	public void testFindAllStayByPetIdNotPresent() {
		Collection<Stay> stays = (Collection<Stay>) stayService.findAllStayByPet(TEST_PET_ID_NOT_PRESENT);
		assertEquals(stays.size(), 0);
	}

	@Test
	public void testFindAllPendingByOwner() {
		Collection<Stay> stays = stayService.findAllPendingByOwner(TEST_OWNER_ID);
		stays.forEach(stay -> Assert.assertEquals(stay.getIsAccepted(), null));
	}

	@Test
	public void testFindAllPendingByOwnerIdNotPresent() {
		Collection<Stay> stays = stayService.findAllPendingByVet(TEST_VET_ID_NOT_PRESENT);
		assertEquals(stays.size(), 0);
	}

	@Test
	public void testFindAllAcceptedByOwner() {
		Collection<Stay> stays = stayService.findAllAcceptedByOwner(TEST_OWNER_ID);
		stays.forEach(stay -> Assert.assertEquals(stay.getIsAccepted(), true));
	}

	@Test
	public void testFindAllAcceptedByOwnerIdNotPresent() {
		Collection<Stay> stays = stayService.findAllAcceptedByOwner(TEST_VET_ID_NOT_PRESENT);
		assertEquals(stays.size(), 0);
	}

	@Test
	public void testCanUnsubscribeWithOwnerAllowed() {
		Boolean canUnsubscribe = stayService.canUnsubscribe(TEST_OWNER_ID_CAN_UNSUBSCRIBE);
		assertTrue(canUnsubscribe);
	}

	@Test
	public void testCanUnsubscribeWithOwnerNotAllowed() {
		Boolean canUnsubscribe = stayService.canUnsubscribe(TEST_OWNER_ID);
		assertFalse(canUnsubscribe);
	}

	@Test
	public void testFindAllStays() {
		Collection<Stay> collection = (Collection<Stay>) stayService.findAllEntities();
		assertEquals(collection.size(), 11);
	}

	@Test
	public void testFindStayById() {
		Optional<Stay> entity = stayService.findEntityById(TEST_STAY_ID);
		assertTrue(entity.isPresent());
		assertEquals(entity.get().getId(), TEST_STAY_ID);
	}

	@Test
	public void testFindStayByIdNotPresent() {
		Optional<Stay> entity = stayService.findEntityById(TEST_STAY_ID_NOT_PRESENT);
		assertFalse(entity.isPresent());
	}

	@Test
	public void testSaveStay() {

		Stay entity = stayService.findEntityById(TEST_STAY_ID).get();

		entity.setDescription("New Description for Service Test");
		entity.setIsAccepted(false);

		stayService.saveEntity(entity);

		Stay savedEntity = stayService.findEntityById(TEST_STAY_ID).get();

		assertEquals(savedEntity.getDescription(), "New Description for Service Test");
		assertEquals(savedEntity.getIsAccepted(), false);
	}

	@Test
	public void testSaveStayWithNullDescription() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);

		Stay entity = stayService.findEntityById(TEST_STAY_ID).get();
		entity.setDescription(null);

		stayService.saveEntity(entity);

		Validator validator = createValidator();
		Set<ConstraintViolation<Stay>> constraintViolations = validator.validate(entity);
		assertEquals(constraintViolations.size(), 1);

		Iterator<ConstraintViolation<Stay>> it = constraintViolations.iterator();
		while (it.hasNext()) {
			ConstraintViolation<Stay> violation = it.next();
			String message = violation.getMessage();
			System.out.println(message);

			switch (violation.getPropertyPath().toString()) {
			case "description":
				assertTrue(message.equals("must not be blank"));
				break;
			default:
				break;
			}
		}

	}

	@Test
	public void testDeleteStay() {
		Optional<Stay> entity = stayService.findEntityById(TEST_STAY_ID);
		assertTrue(entity.isPresent());
		stayService.deleteEntity(entity.get());

		Optional<Stay> deleteEntity = stayService.findEntityById(TEST_STAY_ID);
		assertTrue(!deleteEntity.isPresent());
	}

	@Test
	public void testDeleteStayNotPresent() {
		Collection<Stay> collection = (Collection<Stay>) stayService.findAllEntities();
		int collectionSize = collection.size();

		stayService.deleteEntity(null);

		Collection<Stay> newCollection = (Collection<Stay>) stayService.findAllEntities();
		int newCollectionSize = newCollection.size();

		assertEquals(collectionSize, newCollectionSize);
	}

	@Test
	public void testDeleteStayById() {
		stayService.deleteEntityById(TEST_STAY_ID);

		Optional<Stay> entity = stayService.findEntityById(TEST_STAY_ID);
		assertTrue(!entity.isPresent());
	}

	@Test
	public void testDeleteStayByIdNotPresent() {
		boolean deleted = true;

		try {
			stayService.deleteEntityById(TEST_STAY_ID_NOT_PRESENT);
		} catch (EmptyResultDataAccessException e) {
			deleted = false;
		}

		assertFalse(deleted);
	}

}
