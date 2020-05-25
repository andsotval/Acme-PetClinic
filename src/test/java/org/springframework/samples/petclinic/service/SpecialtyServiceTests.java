/**
 * DP2 - Grupo 8
 * LAB F1.33
 * Date: 17-may-2020
 */

package org.springframework.samples.petclinic.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collection;
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
import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@ActiveProfiles("hsqldb")
@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class SpecialtyServiceTests {

	@Autowired
	protected SpecialtyService	specialtyService;

	private int					TEST_SPECIALTY_ID				= 1;

	private int					TEST_SPECIALTY_ID_NOT_PRESENT	= 100;


	private Validator createValidator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.afterPropertiesSet();
		return localValidatorFactoryBean;
	}

	@Test
	public void testFindAvailableSpecialty() {
		Collection<Specialty> specialties = specialtyService.findAvailable();

		specialties.forEach(specialty -> assertEquals(specialty.getAvailable(), true));
	}

	@Test
	public void testFindNotAvailableSpecialty() {
		Collection<Specialty> specialties = specialtyService.findNotAvailable();

		specialties.forEach(specialty -> assertEquals(specialty.getAvailable(), false));
	}

	@Test
	public void testFindAllSpecialty() {
		Collection<Specialty> specialties = (Collection<Specialty>) specialtyService.findAllEntities();
		assertEquals(specialties.size(), 9);
	}

	@Test
	public void testFindSpecialtyById() {
		Optional<Specialty> entity = specialtyService.findEntityById(TEST_SPECIALTY_ID);
		assertTrue(entity.isPresent());
		assertTrue(entity.get().getId().equals(TEST_SPECIALTY_ID));
	}

	@Test
	public void testFindSpecialtyByIdNotPresent() {
		Optional<Specialty> entity = specialtyService.findEntityById(TEST_SPECIALTY_ID_NOT_PRESENT);
		assertTrue(!entity.isPresent());
	}

	@Test
	public void testSaveSpecialty() {
		Collection<Specialty> collection = (Collection<Specialty>) specialtyService.findAllEntities();
		int collectionSize = collection.size();

		Specialty entity = new Specialty();
		entity.setAvailable(true);
		entity.setName("name1");
		specialtyService.saveEntity(entity);

		collection = (Collection<Specialty>) specialtyService.findAllEntities();
		assertEquals(collection.size(), collectionSize + 1);

		Optional<Specialty> newEntity = specialtyService.findEntityById(collectionSize + 1);
		assertTrue(newEntity.isPresent());
		assertEquals(newEntity.get().getAvailable(), true);
		assertEquals(newEntity.get().getName(), "name1");
	}

	@Test
	public void testSavePetTypeWithNullAvaliability() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Specialty entity = new Specialty();
		entity.setName("name1");

		Validator validator = createValidator();
		Set<ConstraintViolation<Specialty>> constraintViolations = validator.validate(entity);
		assertEquals(constraintViolations.size(), 1);

		ConstraintViolation<Specialty> violation = constraintViolations.iterator().next();
		assertEquals(violation.getPropertyPath().toString(), "available");
		assertEquals(violation.getMessage(), "must not be null");

	}

	@Test
	public void testDeleteSpecialty() {
		Optional<Specialty> entity = specialtyService.findEntityById(TEST_SPECIALTY_ID);
		assertTrue(entity.isPresent());
		specialtyService.deleteEntity(entity.get());

		Optional<Specialty> deleteEntity = specialtyService.findEntityById(TEST_SPECIALTY_ID);
		assertTrue(!deleteEntity.isPresent());
	}

	@Test
	public void testDeleteSpecialtyNotPresent() {
		Collection<Specialty> collection = (Collection<Specialty>) specialtyService.findAllEntities();
		int collectionSize = collection.size();

		specialtyService.deleteEntity(null);

		Collection<Specialty> newCollection = (Collection<Specialty>) specialtyService.findAllEntities();
		int newCollectionSize = newCollection.size();

		assertEquals(collectionSize, newCollectionSize);
	}

	@Test
	public void testDeleteSpecialtyById() {
		specialtyService.deleteEntityById(TEST_SPECIALTY_ID);

		Optional<Specialty> entity = specialtyService.findEntityById(TEST_SPECIALTY_ID);
		assertTrue(!entity.isPresent());
	}

	@Test
	public void testDeleteSpecialtyByIdNotPresent() {
		boolean deleted = true;

		try {
			specialtyService.deleteEntityById(TEST_SPECIALTY_ID_NOT_PRESENT);
		} catch (EmptyResultDataAccessException e) {
			deleted = false;
		}

		assertFalse(deleted);
	}

}
