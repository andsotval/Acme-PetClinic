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
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@ActiveProfiles("hsqldb")
@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class PetTypeServiceTests {

	@Autowired
	protected PetTypeService	petTypeService;

	private int					TEST_PET_TYPE_ID				= 1;

	private int					TEST_PET_TYPE_ID_NOT_PRESENT	= 100;


	private Validator createValidator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.afterPropertiesSet();
		return localValidatorFactoryBean;
	}

	@Test
	public void testFindAvailablePetTypes() {
		Collection<PetType> petTypes = petTypeService.findAvailable();

		petTypes.forEach(pettype -> assertEquals(pettype.getAvailable(), true));
	}

	@Test
	public void testFindNotAvailablePetTypes() {
		Collection<PetType> petTypes = petTypeService.findNotAvailable();

		petTypes.forEach(pettype -> assertEquals(pettype.getAvailable(), false));
	}

	@Test
	public void testFindAllPetTypes() {
		Collection<PetType> petTypes = (Collection<PetType>) petTypeService.findAllEntities();
		assertEquals(petTypes.size(), 7);
	}

	@Test
	public void testFindPetTypeById() {
		Optional<PetType> entity = petTypeService.findEntityById(TEST_PET_TYPE_ID);
		assertTrue(entity.isPresent());
		assertTrue(entity.get().getId().equals(TEST_PET_TYPE_ID));
	}

	@Test
	public void testFindPetTypeByIdNotPresent() {
		Optional<PetType> entity = petTypeService.findEntityById(TEST_PET_TYPE_ID_NOT_PRESENT);
		assertTrue(!entity.isPresent());
	}

	@Test
	public void testSavePetType() {
		Collection<PetType> collection = (Collection<PetType>) petTypeService.findAllEntities();
		int collectionSize = collection.size();

		PetType entity = new PetType();
		entity.setAvailable(true);
		entity.setName("name1");
		petTypeService.saveEntity(entity);

		collection = (Collection<PetType>) petTypeService.findAllEntities();
		assertEquals(collection.size(), collectionSize + 1);

		Optional<PetType> newEntity = petTypeService.findEntityById(collectionSize + 1);
		assertTrue(newEntity.isPresent());
		assertEquals(newEntity.get().getAvailable(), true);
		assertEquals(newEntity.get().getName(), "name1");
	}

	@Test
	public void testSavePetTypeWithNullAvaliability() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		PetType entity = new PetType();
		entity.setName("name1");

		Validator validator = createValidator();
		Set<ConstraintViolation<PetType>> constraintViolations = validator.validate(entity);
		assertEquals(constraintViolations.size(), 1);

		ConstraintViolation<PetType> violation = constraintViolations.iterator().next();
		assertEquals(violation.getPropertyPath().toString(), "available");
		assertEquals(violation.getMessage(), "must not be null");

	}

	@Test
	public void testDeletePetType() {
		Optional<PetType> entity = petTypeService.findEntityById(TEST_PET_TYPE_ID);
		assertTrue(entity.isPresent());
		petTypeService.deleteEntity(entity.get());

		Optional<PetType> deleteEntity = petTypeService.findEntityById(TEST_PET_TYPE_ID);
		assertTrue(!deleteEntity.isPresent());
	}

	@Test
	public void testDeletePetTypeNotPresent() {
		Collection<PetType> collection = (Collection<PetType>) petTypeService.findAllEntities();
		int collectionSize = collection.size();

		petTypeService.deleteEntity(null);

		Collection<PetType> newCollection = (Collection<PetType>) petTypeService.findAllEntities();
		int newCollectionSize = newCollection.size();

		assertEquals(collectionSize, newCollectionSize);
	}

	@Test
	public void testDeletePetTypeById() {
		petTypeService.deleteEntityById(TEST_PET_TYPE_ID);

		Optional<PetType> entity = petTypeService.findEntityById(TEST_PET_TYPE_ID);
		assertTrue(!entity.isPresent());
	}

	@Test
	public void testDeletePetTypeByIdNotPresent() {
		boolean deleted = true;

		try {
			petTypeService.deleteEntityById(TEST_PET_TYPE_ID_NOT_PRESENT);
		} catch (EmptyResultDataAccessException e) {
			deleted = false;
		}

		assertFalse(deleted);
	}

}
