/**
 * DP2 - Grupo 8
 * LAB F1.33
 * Date: 17-may-2020
 */

package org.springframework.samples.petclinic.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
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
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@ActiveProfiles("hsqldb")
@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
class PetServiceTests {

	@Autowired
	protected PetService	petService;

	@Autowired
	protected OwnerService	ownerService;

	private int				TEST_OWNER_ID				= 1;

	private int				TEST_OWNER_ID_NOT_PRESENT	= 100;

	private int				TEST_PET_ID					= 1;

	private int				TEST_PET_ID_NOT_PRESENT		= 100;


	private Validator createValidator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.afterPropertiesSet();
		return localValidatorFactoryBean;
	}

	@Test
	public void testFindPetsByOwnerId() {
		Collection<Pet> pets = petService.findPetsByOwnerId(TEST_OWNER_ID);
		pets.forEach(p -> assertEquals(TEST_OWNER_ID, p.getOwner().getId()));
	}

	@Test
	public void testFindPetsByOwnerIdNotPresent() {
		Collection<Pet> pets = petService.findPetsByOwnerId(TEST_OWNER_ID_NOT_PRESENT);
		assertEquals(pets.size(), 0);
	}

	@Test
	public void testFindAllPets() {
		Collection<Pet> collection = (Collection<Pet>) petService.findAllEntities();
		assertEquals(collection.size(), 13);
	}

	@Test
	public void testFindPetById() {
		Optional<Pet> entity = petService.findEntityById(TEST_PET_ID);
		assertTrue(entity.isPresent());
		assertTrue(entity.get().getId().equals(TEST_PET_ID));
	}

	@Test
	public void testFindPetByIdNotPresent() {
		Optional<Pet> entity = petService.findEntityById(TEST_PET_ID_NOT_PRESENT);
		assertTrue(!entity.isPresent());
	}

	@Test
	public void testSavePet() {
		Collection<Pet> collection = (Collection<Pet>) petService.findAllEntities();
		int collectionSize = collection.size();

		LocalDate birthDate = LocalDate.now();
		Pet pet = new Pet();
		pet.setBirthDate(birthDate);
		pet.setName("Pet Name");

		Owner owner = new Owner();
		owner.setId(1);

		PetType type = new PetType();
		type.setId(1);

		pet.setOwner(owner);
		pet.setType(type);
		petService.saveEntity(pet);

		collection = (Collection<Pet>) petService.findAllEntities();
		assertEquals(collection.size(), collectionSize + 1);

		Optional<Pet> newEntity = petService.findEntityById(collectionSize + 1);
		assertTrue(newEntity.isPresent());
		assertEquals(newEntity.get().getBirthDate(), birthDate);
		assertEquals(newEntity.get().getName(), "Pet Name");
		assertEquals(newEntity.get().getOwner(), owner);
		assertEquals(newEntity.get().getType(), type);
	}

	@Test
	public void testSavePetDateNullAndNameTooShort() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Pet pet = new Pet();
		pet.setBirthDate(null);
		pet.setName("AA");

		Validator validator = createValidator();
		Set<ConstraintViolation<Pet>> constraintViolations = validator.validate(pet);
		assertEquals(2, constraintViolations.size());

		Iterator<ConstraintViolation<Pet>> it = constraintViolations.iterator();
		while (it.hasNext()) {
			ConstraintViolation<Pet> violation = it.next();
			String message = violation.getMessage();

			switch (violation.getPropertyPath().toString()) {
			case "name":
				assertEquals(message, "size must be between 3 and 50");
				break;
			case "birthDate":
				assertEquals(message, "must not be null");
				break;
			default:
				break;
			}
		}
	}

	@Test
	public void testSavePetNegativeNameTooLong() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Pet pet = new Pet();
		String name = "";
		for (int i = 0; i <= 50; i++)
			name += "A";
		pet.setName(name);

		Validator validator = createValidator();
		Set<ConstraintViolation<Pet>> constraintViolations = validator.validate(pet);
		assertEquals(2, constraintViolations.size());

		Iterator<ConstraintViolation<Pet>> it = constraintViolations.iterator();
		while (it.hasNext()) {
			ConstraintViolation<Pet> violation = it.next();
			String message = violation.getMessage();

			switch (violation.getPropertyPath().toString()) {
			case "name":
				assertEquals(message, "size must be between 3 and 50");
				break;
			case "birthDate":
				assertEquals(message, "must not be null");
				break;
			default:
				break;
			}
		}
	}

	@Test
	public void testDeletePet() {
		Optional<Pet> entity = petService.findEntityById(TEST_PET_ID);
		assertTrue(entity.isPresent());
		petService.deleteEntity(entity.get());

		Optional<Pet> deleteEntity = petService.findEntityById(TEST_PET_ID);
		assertTrue(!deleteEntity.isPresent());
	}

	@Test
	public void testDeletePetNotPresent() {
		Collection<Pet> collection = (Collection<Pet>) petService.findAllEntities();
		int collectionSize = collection.size();

		petService.deleteEntity(null);

		Collection<Pet> newCollection = (Collection<Pet>) petService.findAllEntities();
		int newCollectionSize = newCollection.size();

		assertEquals(collectionSize, newCollectionSize);
	}

	@Test
	public void testDeletePetById() {
		petService.deleteEntityById(TEST_PET_ID);

		Optional<Pet> entity = petService.findEntityById(TEST_PET_ID);
		assertTrue(!entity.isPresent());
	}

	@Test
	public void testDeletePetByIdNotPresent() {
		boolean deleted = true;

		try {
			petService.deleteEntityById(TEST_PET_ID_NOT_PRESENT);
		} catch (EmptyResultDataAccessException e) {
			deleted = false;
		}

		assertFalse(deleted);
	}

}
