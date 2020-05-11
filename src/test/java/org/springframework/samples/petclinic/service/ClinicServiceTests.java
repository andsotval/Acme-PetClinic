
package org.springframework.samples.petclinic.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
import org.springframework.samples.petclinic.model.Clinic;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.stereotype.Service;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class ClinicServiceTests {

	@Autowired
	protected ClinicService	clinicService;

	private int				TEST_CLINIC_ID					= 1;

	private int				TEST_CLINIC_ID_NOT_PRESENT		= 100;

	private String			TEST_CLINIC_NAME				= "Clinic1";

	private String			TEST_CLINIC_NAME_NOT_PRESENT	= "ClinicNotPresent";

	private int				TEST_MANAGER_ID					= 1;

	private int				TEST_MANAGER_ID_NOT_PRESENT		= 100;


	private Validator createValidator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.afterPropertiesSet();
		return localValidatorFactoryBean;
	}

	@Test
	public void TestFindClinicByName() {
		Clinic clinic = clinicService.findClinicByName(TEST_CLINIC_NAME);
		String name = clinic.getName();
		assertEquals(name, TEST_CLINIC_NAME);
	}

	@Test
	public void TestFindClinicByNameNotPresent() {
		Clinic clinic = clinicService.findClinicByName(TEST_CLINIC_NAME_NOT_PRESENT);
		assertNull(clinic);
	}

	@Test
	public void TestFindClinicByManagerId() {
		Clinic clinic = clinicService.findClinicByManagerId(TEST_MANAGER_ID);
		Integer id = clinic.getManager().getId();
		assertEquals(TEST_MANAGER_ID, id);
	}

	@Test
	public void TestFindClinicByManagerIdNotPresent() {
		Clinic clinic = clinicService.findClinicByManagerId(TEST_MANAGER_ID_NOT_PRESENT);
		assertNull(clinic);
	}

	@Test
	public void TestFindPetsByClinicId() {
		Collection<Pet> pets = clinicService.findPetsByClinicId(TEST_CLINIC_ID);
		pets.forEach(p -> assertEquals(TEST_CLINIC_ID, p.getOwner().getClinic().getId()));
	}

	@Test
	public void TestFindPetsByClinicIdNotPresent() {
		Collection<Pet> pets = clinicService.findPetsByClinicId(TEST_CLINIC_ID_NOT_PRESENT);
		assertEquals(0, pets.size());
	}

	@Test
	public void testFindAllClinics() {
		Collection<Clinic> collection = (Collection<Clinic>) clinicService.findAllEntities();
		assertEquals(collection.size(), 8);
	}

	@Test
	public void testFindClinicById() {
		Optional<Clinic> entity = clinicService.findEntityById(TEST_CLINIC_ID);
		assertTrue(entity.isPresent());
		assertTrue(entity.get().getId().equals(1));
	}

	@Test
	public void testFindClinicByIdNotPresent() {
		Optional<Clinic> entity = clinicService.findEntityById(TEST_CLINIC_ID_NOT_PRESENT);
		assertTrue(!entity.isPresent());
	}

	@Test
	public void testSaveClinic() {
		Collection<Clinic> collection = (Collection<Clinic>) clinicService.findAllEntities();
		int collectionSize = collection.size();

		Clinic entity = new Clinic();
		entity.setName("Name 1");
		entity.setAddress("Address 1");
		entity.setCity("City 1");
		entity.setTelephone("912654555");
		clinicService.saveEntity(entity);

		collection = (Collection<Clinic>) clinicService.findAllEntities();
		assertEquals(collection.size(), collectionSize + 1);

		Optional<Clinic> newEntity = clinicService.findEntityById(collectionSize + 1);
		assertTrue(newEntity.isPresent());
		assertEquals(newEntity.get().getName(), "Name 1");
		assertEquals(newEntity.get().getAddress(), "Address 1");
		assertEquals(newEntity.get().getCity(), "City 1");
		assertEquals(newEntity.get().getTelephone(), "912654555");
	}

	@Test
	public void testSaveClinicWithEmptyParameters() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Clinic entity = new Clinic();
		entity.setName("Name 1");
		entity.setAddress(null);
		entity.setCity(null);
		entity.setTelephone(null);

		Validator validator = createValidator();
		Set<ConstraintViolation<Clinic>> constraintViolations = validator.validate(entity);
		assertEquals(constraintViolations.size(), 3);
		Iterator<ConstraintViolation<Clinic>> it = constraintViolations.iterator();
		while (it.hasNext()) {
			ConstraintViolation<Clinic> violation = it.next();
			String message = violation.getMessage();

			switch (violation.getPropertyPath().toString()) {
			case "description":
				assertTrue(message.equals("must not be empty"));
				break;
			case "address":
				assertTrue(message.equals("must not be empty"));
				break;
			case "city":
				assertTrue(message.equals("must not be empty"));
				break;
			case "telephone":
				assertTrue(message.equals("must not be empty"));
				break;
			default:
				break;
			}
		}
	}

	@Test
	public void testDeleteClinic() {
		Optional<Clinic> entity = clinicService.findEntityById(TEST_CLINIC_ID);
		assertTrue(entity.isPresent());
		clinicService.deleteEntity(entity.get());

		Optional<Clinic> deleteEntity = clinicService.findEntityById(TEST_CLINIC_ID);
		assertTrue(!deleteEntity.isPresent());
	}

	@Test
	public void testDeleteClinicNotPresent() {
		Collection<Clinic> collection = (Collection<Clinic>) clinicService.findAllEntities();
		int collectionSize = collection.size();

		clinicService.deleteEntity(null);

		Collection<Clinic> newCollection = (Collection<Clinic>) clinicService.findAllEntities();
		int newCollectionSize = newCollection.size();

		assertEquals(collectionSize, newCollectionSize);
	}

	@Test
	public void testDeleteClinicById() {
		clinicService.deleteEntityById(TEST_CLINIC_ID);

		Optional<Clinic> entity = clinicService.findEntityById(TEST_CLINIC_ID);
		assertTrue(!entity.isPresent());
	}

	@Test
	public void testDeleteClinicByIdNotPresent() {
		boolean deleted = true;

		try {
			clinicService.deleteEntityById(TEST_CLINIC_ID_NOT_PRESENT);
		} catch (EmptyResultDataAccessException e) {
			deleted = false;
		}

		assertFalse(deleted);
	}
}
