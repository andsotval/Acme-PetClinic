/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.samples.petclinic.service;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

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
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.stereotype.Service;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 * Integration test of the Service and the Repository layer.
 * <p>
 * ClinicServiceSpringDataJpaTests subclasses benefit from the following services provided
 * by the Spring TestContext Framework:
 * </p>
 * <ul>
 * <li><strong>Spring IoC container caching</strong> which spares us unnecessary set up
 * time between test execution.</li>
 * <li><strong>Dependency Injection</strong> of test fixture instances, meaning that we
 * don't need to perform application context lookups. See the use of
 * {@link Autowired @Autowired} on the <code>{@link
 * ClinicServiceTests#clinicService clinicService}</code> instance variable, which uses
 * autowiring <em>by type</em>.
 * <li><strong>Transaction management</strong>, meaning each test method is executed in
 * its own transaction, which is automatically rolled back by default. Thus, even if tests
 * insert or otherwise change database state, there is no need for a teardown or cleanup
 * script.
 * <li>An {@link org.springframework.context.ApplicationContext ApplicationContext} is
 * also inherited and can be used for explicit bean lookup if necessary.</li>
 * </ul>
 *
 * @author Ken Krebs
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @author Michael Isvy
 * @author Dave Syer
 */

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
class PetServiceTests {

	@Autowired
	protected PetService	petService;

	@Autowired
	protected OwnerService	ownerService;


	private Validator createValidator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.afterPropertiesSet();
		return localValidatorFactoryBean;
	}

	@Test
	public void TestFindPetsByOwnerIdPositive() {
		int ownerId = 2;
		petService.findPetsByOwnerId(ownerId).forEach(p -> assertEquals(ownerId, p.getOwner().getId()));
	}

	@Test
	public void TestFindPetsByOwnerIdNegative() {
		int ownerId = 2;
		int notOwnerId = 4;
		petService.findPetsByOwnerId(notOwnerId).forEach(p -> assertNotEquals(ownerId, p.getOwner().getId()));
	}

	@Test
	public void TestFindPetsByOwnerIdNotPresent() {
		int ownerId = 20;
		petService.findPetsByOwnerId(ownerId).forEach(p -> assertEquals(null, p.getOwner()));
	}

	@Test
	public void testFindAllEntities() {
		Collection<Pet> collection = (Collection<Pet>) petService.findAllEntities();
		assertEquals(collection.size(), 13);
	}

	@Test
	public void testFindEntityByIdPositive() {
		Optional<Pet> entity = petService.findEntityById(1);
		assertTrue(entity.isPresent());
		assertTrue(entity.get().getId().equals(1));
	}

	@Test
	public void testFindEntityByIdNegative() {
		Optional<Pet> entity = petService.findEntityById(99);
		assertTrue(!entity.isPresent());
	}

	@Test
	public void testSaveEntityPositive() {
		Collection<Pet> collection = (Collection<Pet>) petService.findAllEntities();
		assertEquals(collection.size(), 13);

		LocalDate birthDate = LocalDate.now();
		String name = "PET14";
		Pet pet = new Pet();
		pet.setBirthDate(birthDate);
		pet.setName(name);

		Owner owner = new Owner();
		owner.setId(1);

		PetType type = new PetType();
		type.setId(1);

		pet.setOwner(owner);
		pet.setType(type);
		petService.saveEntity(pet);

		collection = (Collection<Pet>) petService.findAllEntities();
		assertEquals(collection.size(), 14);

		Optional<Pet> newEntity = petService.findEntityById(14);
		assertTrue(newEntity.isPresent());
		assertEquals(newEntity.get().getBirthDate(), birthDate);
		assertEquals(newEntity.get().getName(), name);
		assertEquals(newEntity.get().getOwner(), owner);
		assertEquals(newEntity.get().getType(), type);
	}

	@Test
	public void testSaveEntityNegativeDateNullAndNameShort() {
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
	public void testSaveEntityNegativeNameTooLong() {
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
	public void testDeleteEntity() {
		Optional<Pet> entity = petService.findEntityById(1);
		assertTrue(entity.isPresent());
		petService.deleteEntity(entity.get());

		Optional<Pet> deleteEntity = petService.findEntityById(1);
		assertTrue(!deleteEntity.isPresent());
	}

	@Test
	public void testDeleteEntityById() {
		petService.deleteEntityById(1);

		Optional<Pet> entity = petService.findEntityById(1);
		assertTrue(!entity.isPresent());
	}

	//	@Test
	//	void shouldFindPetWithCorrectId() {
	//		Pet pet7 = this.petService.findPetById(7);
	//		Assertions.assertThat(pet7.getName()).startsWith("Samantha");
	//		Assertions.assertThat(pet7.getOwner().getFirstName()).isEqualTo("Jean");
	//
	//	}
	//
	//	@Test
	//	void shouldFindAllPetTypes() {
	//		Collection<PetType> petTypes = this.petService.findPetTypes();
	//
	//		PetType petType1 = EntityUtils.getById(petTypes, PetType.class, 1);
	//		Assertions.assertThat(petType1.getName()).isEqualTo("cat");
	//		PetType petType4 = EntityUtils.getById(petTypes, PetType.class, 4);
	//		Assertions.assertThat(petType4.getName()).isEqualTo("snake");
	//	}
	//
	//	@Test
	//	@Transactional
	//	public void shouldInsertPetIntoDatabaseAndGenerateId() {
	//		Owner owner6 = this.ownerService.findOwnerById(6);
	//		int found = owner6.getPets().size();
	//
	//		Pet pet = new Pet();
	//		pet.setName("bowser");
	//		Collection<PetType> types = this.petService.findPetTypes();
	//		pet.setType(EntityUtils.getById(types, PetType.class, 2));
	//		pet.setBirthDate(LocalDate.now());
	//		owner6.addPet(pet);
	//		assertThat(owner6.getPets().size()).isEqualTo(found + 1);
	//
	//            try {
	//                this.petService.savePet(pet);
	//            } catch (DuplicatedPetNameException ex) {
	//                Logger.getLogger(PetServiceTests.class.getName()).log(Level.SEVERE, null, ex);
	//            }
	//		this.ownerService.saveOwner(owner6);
	//
	//		owner6 = this.ownerService.findOwnerById(6);
	//		assertThat(owner6.getPets().size()).isEqualTo(found + 1);
	//		// checks that id has been generated
	//		assertThat(pet.getId()).isNotNull();
	//	}
	//
	//	@Test
	//	@Transactional
	//	public void shouldThrowExceptionInsertingPetsWithTheSameName() {
	//		Owner owner6 = this.ownerService.findOwnerById(6);
	//		Pet pet = new Pet();
	//		pet.setName("wario");
	//		Collection<PetType> types = this.petService.findPetTypes();
	//		pet.setType(EntityUtils.getById(types, PetType.class, 2));
	//		pet.setBirthDate(LocalDate.now());
	//		owner6.addPet(pet);
	//		try {
	//			petService.savePet(pet);
	//		} catch (DuplicatedPetNameException e) {
	//			// The pet already exists!
	//			e.printStackTrace();
	//		}
	//
	//		Pet anotherPetWithTheSameName = new Pet();
	//		anotherPetWithTheSameName.setName("wario");
	//		anotherPetWithTheSameName.setType(EntityUtils.getById(types, PetType.class, 1));
	//		anotherPetWithTheSameName.setBirthDate(LocalDate.now().minusWeeks(2));
	//		Assertions.assertThrows(DuplicatedPetNameException.class, () ->{
	//			owner6.addPet(anotherPetWithTheSameName);
	//			petService.savePet(anotherPetWithTheSameName);
	//		});
	//	}
	//
	//	@Test
	//	@Transactional
	//	public void shouldUpdatePetName() throws Exception {
	//		Pet pet7 = this.petService.findPetById(7);
	//		String oldName = pet7.getName();
	//
	//		String newName = oldName + "X";
	//		pet7.setName(newName);
	//		this.petService.savePet(pet7);
	//
	//		pet7 = this.petService.findPetById(7);
	//		assertThat(pet7.getName()).isEqualTo(newName);
	//	}
	//
	//	@Test
	//	@Transactional
	//	public void shouldThrowExceptionUpdatingPetsWithTheSameName() {
	//		Owner owner6 = this.ownerService.findOwnerById(6);
	//		Pet pet = new Pet();
	//		pet.setName("wario");
	//		Collection<PetType> types = this.petService.findPetTypes();
	//		pet.setType(EntityUtils.getById(types, PetType.class, 2));
	//		pet.setBirthDate(LocalDate.now());
	//		owner6.addPet(pet);
	//
	//		Pet anotherPet = new Pet();
	//		anotherPet.setName("waluigi");
	//		anotherPet.setType(EntityUtils.getById(types, PetType.class, 1));
	//		anotherPet.setBirthDate(LocalDate.now().minusWeeks(2));
	//		owner6.addPet(anotherPet);
	//
	//		try {
	//			petService.savePet(pet);
	//			petService.savePet(anotherPet);
	//		} catch (DuplicatedPetNameException e) {
	//			// The pets already exists!
	//			e.printStackTrace();
	//		}
	//
	//		Assertions.assertThrows(DuplicatedPetNameException.class, () ->{
	//			anotherPet.setName("wario");
	//			petService.savePet(anotherPet);
	//		});
	//	}
	//
	//	@Test
	//	@Transactional
	//	public void shouldAddNewVisitForPet() {
	//		Pet pet7 = this.petService.findPetById(7);
	//		int found = pet7.getVisits().size();
	//		Visit visit = new Visit();
	//		pet7.addVisit(visit);
	//		visit.setDescription("test");
	//		this.petService.saveVisit(visit);
	//            try {
	//                this.petService.savePet(pet7);
	//            } catch (DuplicatedPetNameException ex) {
	//                Logger.getLogger(PetServiceTests.class.getName()).log(Level.SEVERE, null, ex);
	//            }
	//
	//		pet7 = this.petService.findPetById(7);
	//		assertThat(pet7.getVisits().size()).isEqualTo(found + 1);
	//		assertThat(visit.getId()).isNotNull();
	//	}
	//
	//	@Test
	//	void shouldFindVisitsByPetId() throws Exception {
	//		Collection<Visit> visits = this.petService.findVisitsByPetId(7);
	//		Assertions.assertThat(visits.size()).isEqualTo(2);
	//		Visit[] visitArr = visits.toArray(new Visit[visits.size()]);
	//		Assertions.assertThat(visitArr[0].getPet()).isNotNull();
	//		Assertions.assertThat(visitArr[0].getDate()).isNotNull();
	//		Assertions.assertThat(visitArr[0].getPet().getId()).isEqualTo(7);
	//	}

}
