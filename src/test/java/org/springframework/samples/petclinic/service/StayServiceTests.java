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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.Month;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.stream.StreamSupport;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.samples.petclinic.model.Authorities;
import org.springframework.samples.petclinic.model.Clinic;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Stay;
import org.springframework.samples.petclinic.model.User;
import org.springframework.stereotype.Service;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
class StayServiceTests {

	@Autowired
	protected StayService	stayService;

	@Autowired
	protected VetService	vetService;


	private Validator createValidator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.afterPropertiesSet();
		return localValidatorFactoryBean;
	}


	private static final int	TEST_STAY_ID	= 1;
	private static final int	TEST_CLINIC_ID	= 1;
	private static final int	TEST_OWNER_ID	= 1;
	private static final int	TEST_PET_ID		= 1;


	@BeforeEach
	void setup() {
		//Arrange ------------------------------
		LocalDate actualDate = LocalDate.of(2019, Month.APRIL, 5);

		Clinic clinic = new Clinic();
		clinic.setId(TEST_CLINIC_ID);

		User user = new User();
		user.setEnabled(true);
		user.setUsername("pepito");
		user.setPassword("pepito");

		Authorities authority = new Authorities();
		authority.setAuthority("Owner");
		authority.setUsername("Owner");

		Owner owner = new Owner();
		owner.setUser(user);
		owner.setId(TEST_OWNER_ID);
		owner.setFirstName("owner");
		owner.setLastName("Leary");
		owner.setAddress("110 W. Liberty St.");
		owner.setCity("Madison");
		owner.setTelephone("6085551023");
		owner.setClinic(clinic);

		Pet pet = new Pet();
		pet.setId(TEST_PET_ID);
		pet.setBirthDate(LocalDate.of(2019, Month.FEBRUARY, 1));
		pet.setName("Wiskers");
		pet.setOwner(owner);

		Stay stay = new Stay();
		stay.setId(1);
		stay.setDescription("Description in test");
		stay.setIsAccepted(false);
		stay.setStartDate(actualDate);
		stay.setFinishDate(actualDate);
		stay.setClinic(clinic);
		stay.setPet(pet);

		stayService.saveEntity(stay);
	}

	@Test
	public void testFindAllPendingByVet() {
		stayService.findAllPendingByVet(1).forEach(stay -> Assert.assertEquals(stay.getIsAccepted(), null));
	}

	public void testFindAllPendingByNullVet() {
		Iterable<Stay> stays = stayService.findAllPendingByVet(null);
		int numStays = (int) StreamSupport.stream(stays.spliterator(), false).count();
		assertEquals(numStays, 0);
	}
	@Test
	public void testFindAllAcceptedByVet() {
		stayService.findAllAcceptedByVet(1).forEach(stay -> Assert.assertEquals(stay.getIsAccepted(), true));
	}
	@Test
	public void testFindAllAcceptedByNullVet() {
		Iterable<Stay> stays = stayService.findAllAcceptedByVet(null);
		int numStays = (int) StreamSupport.stream(stays.spliterator(), false).count();
		assertEquals(numStays, 0);
	}

	@Test
	public void testDeleteByPet() {
		//		//Arrange ----------------------------------
		Collection<Stay> stays = (Collection<Stay>) stayService.findAllStayByPet(TEST_PET_ID);
		assertTrue(stays.size() != 0);
		//-------------------------------------------

		//Act ---------------------------------------
		stayService.deleteByPetId(TEST_PET_ID);
		//-------------------------------------------

		//Assertion ---------------------------------
		stays = (Collection<Stay>) stayService.findAllStayByPet(TEST_PET_ID);
		assertTrue(stays.size() == 0);
		//-------------------------------------------
	}
	@Test
	public void testDeleteByNullPet() {
		//		//Arrange ----------------------------------
		Collection<Stay> staysBefore = (Collection<Stay>) stayService.findAllEntities();
		Integer numStaysBefore = staysBefore.size();
		//-------------------------------------------

		//Act ---------------------------------------
		stayService.deleteByPetId(-1);
		//-------------------------------------------

		//Assertion ---------------------------------
		Collection<Stay> staysAfter = (Collection<Stay>) stayService.findAllEntities();
		Integer numStaysAfter = staysAfter.size();
		assertTrue(numStaysBefore == numStaysAfter);
		//-------------------------------------------
	}

	@Test
	public void testFindAllStayByPet() {
		//Act --------------------------------
		Collection<Stay> stays = (Collection<Stay>) stayService.findAllStayByPet(TEST_PET_ID);
		//-----------------------------------

		//Assertion -------------------------
		assertTrue(stays.size() != 0);
		//----------------------------------
	}

	@Test
	public void testFindAllStayByNullPet() {
		//Act --------------------------------
		Collection<Stay> stays = (Collection<Stay>) stayService.findAllStayByPet(null);
		//-----------------------------------

		//Assertion -------------------------
		assertTrue(stays.size() == 0);
		//----------------------------------
	}

	@Test
	public void testFindAllPendingByOwner() {
		//Act & assert-------------------------------
		stayService.findAllPendingByOwner(TEST_OWNER_ID)
			.forEach((x) -> assertTrue(!x.getIsAccepted() && x.getPet().getOwner().getId() == TEST_OWNER_ID));
		//-------------------------------------------
	}

	@Test
	public void testFindAllPendingByNullOwner() {
		//Act ---------------------------------------
		Iterable<Stay> stays = stayService.findAllPendingByOwner(null);
		int numStays = (int) StreamSupport.stream(stays.spliterator(), false).count();
		//-------------------------------------------

		//Assert ------------------------------------
		assertEquals(numStays, 0);
	}

	@Test
	public void testFindAllAcceptedByOwner() {
		//Act & assert-------------------------------
		stayService.findAllAcceptedByOwner(TEST_OWNER_ID)
			.forEach((x) -> assertTrue(x.getIsAccepted() && x.getPet().getOwner().getId() == TEST_OWNER_ID));
		//-------------------------------------------
	}

	@Test
	public void testFindAllAcceptedByNullOwner() {
		//Act ---------------------------------------
		Iterable<Stay> stays = stayService.findAllAcceptedByOwner(null);
		int numStays = (int) StreamSupport.stream(stays.spliterator(), false).count();
		//-------------------------------------------

		//Assert ------------------------------------
		assertEquals(numStays, 0);
		//-------------------------------------------
	}

	@Test
	public void saveStay() {
		//Fixture -----------------------------------
		LocalDate actualDate = LocalDate.of(2019, Month.APRIL, 5);

		Clinic clinic = new Clinic();
		clinic.setId(2);

		User user = new User();
		user.setEnabled(true);
		user.setUsername("pepito");
		user.setPassword("pepito");

		Authorities authority = new Authorities();
		authority.setAuthority("Owner");
		authority.setUsername("Owner");

		Owner owner = new Owner();
		owner.setUser(user);
		owner.setId(2);
		owner.setFirstName("owner");
		owner.setLastName("Leary");
		owner.setAddress("110 W. Liberty St.");
		owner.setCity("Madison");
		owner.setTelephone("6085551023");
		owner.setClinic(clinic);

		Pet pet = new Pet();
		pet.setId(2);
		pet.setBirthDate(LocalDate.of(2019, Month.FEBRUARY, 1));
		pet.setName("Wiskers");
		pet.setOwner(owner);

		Stay stay = new Stay();
		stay.setId(2);
		stay.setDescription("Description in test");
		stay.setIsAccepted(false);
		stay.setStartDate(actualDate);
		stay.setFinishDate(actualDate);
		stay.setClinic(clinic);
		stay.setPet(pet);

		//Act -----------------------------------
		stayService.saveEntity(stay);
		//---------------------------------------

		//Assert --------------------------------
		Optional<Stay> stayOptional = stayService.findEntityById(2);
		Stay staySaved = stayOptional.get();

		assertTrue(stayOptional.isPresent());
		assertEquals(stay.getId(), staySaved.getId());
		assertEquals(stay.getDescription(), staySaved.getDescription());
		assertEquals(stay.getIsAccepted(), staySaved.getIsAccepted());
		assertEquals(stay.getStartDate(), staySaved.getStartDate());
		assertEquals(stay.getFinishDate(), staySaved.getFinishDate());
		assertEquals(stay.getClinic().getId(), staySaved.getClinic().getId());
		assertEquals(stay.getPet().getId(), staySaved.getPet().getId());
	}
	@Test
	public void saveStayNegative() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Clinic clinic = new Clinic();
		clinic.setId(2);

		Stay stay = new Stay();
		stay.setId(2);
		stay.setDescription(null);

		Validator validator = createValidator();
		Set<ConstraintViolation<Stay>> constraintViolations = validator.validate(stay);
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

}
