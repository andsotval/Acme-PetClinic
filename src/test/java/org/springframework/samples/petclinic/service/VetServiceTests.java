package org.springframework.samples.petclinic.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.stereotype.Service;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
class VetServiceTests {

	@Autowired
	protected VetService vetService;

	private int TEST_VET_ID = 1;

	private int TEST_VET_ID_NOT_PRESENT = 100;

	private int TEST_MANAGER_ID = 1;

	private int TEST_MANAGER_ID_NOT_PRESENT = 100;

	private int TEST_CLINIC_ID = 1;

	private int TEST_CLINIC_ID_NOT_PRESENT = 100;

	private Validator createValidator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.afterPropertiesSet();
		return localValidatorFactoryBean;
	}

	@Test
	public void testFindAvailableVets() {
		Collection<Vet> vets = vetService.findAvailableVets();
		vets.forEach(v -> assertEquals(null, v.getClinic()));
	}

	@Test
	public void testFindVetsByManagerId() {
		Collection<Vet> vets = vetService.findVetsByManager(TEST_MANAGER_ID);
		vets.forEach(v -> assertEquals(TEST_MANAGER_ID, v.getClinic().getManager().getId()));
	}

	@Test
	public void testFindVetsByManagerIdNotPresent() {
		Collection<Vet> vets = vetService.findVetsByManager(TEST_MANAGER_ID_NOT_PRESENT);
		assertEquals(vets.size(), 0);
	}

	@Test
	public void testFindVetsByClinicId() {
		Collection<Vet> vets = vetService.findVetsByManager(TEST_CLINIC_ID);
		vets.forEach(vet -> assertEquals(TEST_CLINIC_ID, vet.getClinic().getId()));
	}

	@Test
	public void testFindVetsByClinicIdNegative() {
		Collection<Vet> vets = vetService.findVetsByManager(TEST_CLINIC_ID_NOT_PRESENT);
		assertEquals(vets.size(), 0);
	}

	@Test
	public void testFindAllVets() {
		Collection<Vet> collection = (Collection<Vet>) vetService.findAllEntities();
		assertEquals(collection.size(), 9);
	}

	@Test
	public void testFindVetById() {
		Optional<Vet> entity = vetService.findEntityById(TEST_VET_ID);
		assertTrue(entity.isPresent());
		assertTrue(entity.get().getId().equals(TEST_VET_ID));
	}

	@Test
	public void testFindVetByIdNotPresent() {
		Optional<Vet> entity = vetService.findEntityById(TEST_VET_ID_NOT_PRESENT);
		assertTrue(!entity.isPresent());
	}

	@Test
	public void testSaveVet() {

		Collection<Vet> collection = (Collection<Vet>) vetService.findAllEntities();
		int collectionSize = collection.size();

		Vet entity = new Vet();
		entity.setFirstName("James");
		entity.setLastName("Carter");
		entity.setAddress("110 W. Liberty St.");
		entity.setCity("Madison");
		entity.setTelephone("608555123");
		entity.setMail("Vet@mail.com");

		vetService.saveEntity(entity);

		Collection<Vet> newCollection = (Collection<Vet>) vetService.findAllEntities();
		int newCollectionSize = newCollection.size();

		assertEquals(collectionSize + 1, newCollectionSize);

		Assert.assertEquals("James", this.vetService.findEntityById(newCollectionSize).get().getFirstName());
	}

	@Test
	public void testSaveVetWithoutFirstName() {
		Vet entity = new Vet();
		entity.setLastName("Carter");
		entity.setAddress("110 W. Liberty St.");
		entity.setCity("Madison");
		entity.setTelephone("6085551023");

		Validator validator = createValidator();
		Set<ConstraintViolation<Vet>> constraintViolations = validator.validate(entity);
		assertEquals(constraintViolations.size(), 3);

		Iterator<ConstraintViolation<Vet>> it = constraintViolations.iterator();
		while (it.hasNext()) {
			ConstraintViolation<Vet> violation = it.next();
			String message = violation.getMessage();
			System.out.println(message);

			switch (violation.getPropertyPath().toString()) {
			case "first name":
				assertTrue(message.equals("no puede estar vacío"));
				break;
			default:
				break;
			}
		}
	}

	@Test
	public void testDeleteVet() {
		Optional<Vet> entity = vetService.findEntityById(TEST_VET_ID);
		assertTrue(entity.isPresent());
		vetService.deleteEntity(entity.get());

		Optional<Vet> deleteEntity = vetService.findEntityById(TEST_VET_ID);
		assertTrue(!deleteEntity.isPresent());
	}

	@Test
	public void testDeleteVetNotPresent() {
		Collection<Vet> collection = (Collection<Vet>) vetService.findAllEntities();
		int collectionSize = collection.size();

		vetService.deleteEntity(null);

		Collection<Vet> newCollection = (Collection<Vet>) vetService.findAllEntities();
		int newCollectionSize = newCollection.size();

		assertEquals(collectionSize, newCollectionSize);
	}

	@Test
	public void testDeleteVetById() {
		vetService.deleteEntityById(TEST_VET_ID);

		Optional<Vet> entity = vetService.findEntityById(TEST_VET_ID);
		assertTrue(!entity.isPresent());
	}

	@Test
	public void testDeleteVetByIdNotPresent() {
		boolean deleted = true;

		try {
			vetService.deleteEntityById(TEST_VET_ID_NOT_PRESENT);
		} catch (EmptyResultDataAccessException e) {
			deleted = false;
		}

		assertFalse(deleted);
	}

}
