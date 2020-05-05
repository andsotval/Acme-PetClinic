
package org.springframework.samples.petclinic.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

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
import org.springframework.samples.petclinic.model.Clinic;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.stereotype.Service;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class ClinicServiceTests {

	@Autowired
	protected ClinicService clinicService;


	private Validator createValidator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.afterPropertiesSet();
		return localValidatorFactoryBean;
	}

	@Test
	public void TestFindClinicByManagerIdPositive() {
		int managerId = 1;
		Clinic clinic = clinicService.findClinicByManagerId(managerId);
		assertEquals(managerId, clinic.getManager().getId());
	}
	@Test
	public void TestFindClinicByManagerIdNegative() {
		int managerId = 1;
		int notManagerId = 2;
		Clinic clinic = clinicService.findClinicByManagerId(managerId);
		assertNotEquals(notManagerId, clinic.getManager().getId());
	}

	@Test
	public void TestFindClinicByManagerIdNegativeNotPresent() {
		int managerId = 15;
		Clinic clinic = clinicService.findClinicByManagerId(managerId);
		assertNull(clinic);
	}

	@Test
	public void TestFindClinicByNamePositive() {
		String name = "Clinic1";
		assertEquals(name, clinicService.findClinicByName(name).getName());
	}

	@Test
	public void TestFindClinicByNameNegativeNotPresent() {
		String name = "ClinicaDePrueba";
		assertEquals(null, clinicService.findClinicByName(name));
	}

	@Test
	public void TestFindClinicByNameNegative() {
		String name = "Clinic1";
		String nameWrong = "Clinic2";
		assertNotEquals(nameWrong, clinicService.findClinicByName(name).getName());
	}

	@Test
	public void TestFindPetsByClinicPositive() {
		String name = "Clinic1";

		Clinic clinic = clinicService.findClinicByName(name);
		assertNotNull(clinic);
		Iterable<Pet> pets = clinicService.findPetsCyClinic(clinic);
		assertNotNull(pets);
	}

	@Test
	public void TestFindPetsByClinicNegative() {
		String name = "ClinicaDePrueba";

		Clinic clinic = clinicService.findClinicByName(name);
		assertNull(clinic);
		Iterable<Pet> pets = clinicService.findPetsCyClinic(clinic);
		assertNotNull(pets);
	}

	@Test
	public void testFindAllEntities() {
		Collection<Clinic> collection = (Collection<Clinic>) clinicService.findAllEntities();
		assertEquals(collection.size(), 8);
	}

	@Test
	public void testFindEntityByIdPositive() {
		Optional<Clinic> entity = clinicService.findEntityById(1);
		assertTrue(entity.isPresent());
		assertTrue(entity.get().getId().equals(1));
	}

	@Test
	public void testFindEntityByIdNegative() {
		Optional<Clinic> entity = clinicService.findEntityById(99);
		assertTrue(!entity.isPresent());
	}

	@Test
	public void testSaveEntityPositive() {
		Collection<Clinic> collection = (Collection<Clinic>) clinicService.findAllEntities();
		assertEquals(collection.size(), 8);

		Clinic entity = new Clinic();
		entity.setName("Name 1");
		entity.setAddress("Address 1");
		entity.setCity("City 1");
		entity.setTelephone("912654555");
		clinicService.saveEntity(entity);

		collection = (Collection<Clinic>) clinicService.findAllEntities();
		assertEquals(collection.size(), 9);

		Optional<Clinic> newEntity = clinicService.findEntityById(9);
		assertTrue(newEntity.isPresent());
		assertEquals(newEntity.get().getName(), "Name 1");
		assertEquals(newEntity.get().getAddress(), "Address 1");
		assertEquals(newEntity.get().getCity(), "City 1");
		assertEquals(newEntity.get().getTelephone(), "912654555");
	}

	@Test
	public void testSaveEntityNegative() {
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
	public void testDeleteEntity() {
		Optional<Clinic> entity = clinicService.findEntityById(1);
		assertTrue(entity.isPresent());
		clinicService.deleteEntity(entity.get());

		Optional<Clinic> deleteEntity = clinicService.findEntityById(1);
		assertTrue(!deleteEntity.isPresent());
	}

	@Test
	public void testDeleteEntityById() {
		clinicService.deleteEntityById(1);

		Optional<Clinic> entity = clinicService.findEntityById(1);
		assertTrue(!entity.isPresent());
	}
}
