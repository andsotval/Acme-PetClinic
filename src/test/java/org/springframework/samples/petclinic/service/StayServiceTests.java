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

import java.time.LocalDate;
import java.time.Month;
import java.util.Collection;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Authorities;
import org.springframework.samples.petclinic.model.Clinic;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Stay;
import org.springframework.samples.petclinic.model.User;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
class StayServiceTests {

	@Autowired
	protected StayService		stayService;

	@Autowired
	protected VetService		vetService;

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
		vetService.findAllEntities().forEach(v -> stayService.findAllPendingByVet(v).forEach(stay -> Assert.assertEquals(stay.getIsAccepted(), null)));
	}

	@Test
	public void testFindAllAcceptedByVet() {
		vetService.findAllEntities().forEach(v -> stayService.findAllAcceptedByVet(v).forEach(stay -> Assert.assertEquals(stay.getIsAccepted(), true)));
	}

	@Test
	public void testDeleteByPet() {
		//		//Arrange ----------------------------------
		//		LocalDate actualDate = LocalDate.of(2019, Month.APRIL, 5);
		//
		//		Clinic clinic = new Clinic();
		//		clinic.setId(TEST_CLINIC_ID);
		//
		//		User user = new User();
		//		user.setEnabled(true);
		//		user.setUsername("pepito");
		//		user.setPassword("pepito");
		//
		//		Authorities authority = new Authorities();
		//		authority.setAuthority("Owner");
		//		authority.setUsername("Owner");
		//
		//		Owner owner = new Owner();
		//		owner.setUser(user);
		//		owner.setId(TEST_OWNER_ID);
		//		owner.setFirstName("owner");
		//		owner.setLastName("Leary");
		//		owner.setAddress("110 W. Liberty St.");
		//		owner.setCity("Madison");
		//		owner.setTelephone("6085551023");
		//		owner.setClinic(clinic);
		//
		//		Pet pet = new Pet();
		//		pet.setId(TEST_PET_ID);
		//		pet.setBirthDate(LocalDate.of(2019, Month.FEBRUARY, 1));
		//		pet.setName("Wiskers");
		//		pet.setOwner(owner);
		//
		//		Stay stay = new Stay();
		//		stay.setId(1);
		//		stay.setDescription("Description in test");
		//		stay.setIsAccepted(false);
		//		stay.setStartDate(actualDate);
		//		stay.setFinishDate(actualDate);
		//		stay.setClinic(clinic);
		//		stay.setPet(pet);
		//
		//		stayService.saveEntity(stay);
		//
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
	public void testFindAllStayByPet() {

		//		//Arrange ------------------------------
		//		LocalDate actualDate = LocalDate.of(2019, Month.APRIL, 5);
		//
		//		Clinic clinic = new Clinic();
		//		clinic.setId(TEST_CLINIC_ID);
		//
		//		User user = new User();
		//		user.setEnabled(true);
		//		user.setUsername("pepito");
		//		user.setPassword("pepito");
		//
		//		Authorities authority = new Authorities();
		//		authority.setAuthority("Owner");
		//		authority.setUsername("Owner");
		//
		//		Owner owner = new Owner();
		//		owner.setUser(user);
		//		owner.setId(TEST_OWNER_ID);
		//		owner.setFirstName("owner");
		//		owner.setLastName("Leary");
		//		owner.setAddress("110 W. Liberty St.");
		//		owner.setCity("Madison");
		//		owner.setTelephone("6085551023");
		//		owner.setClinic(clinic);
		//
		//		Pet pet = new Pet();
		//		pet.setId(TEST_PET_ID);
		//		pet.setBirthDate(LocalDate.of(2019, Month.FEBRUARY, 1));
		//		pet.setName("Wiskers");
		//		pet.setOwner(owner);
		//
		//		Stay stay = new Stay();
		//		stay.setId(1);
		//		stay.setDescription("Description in test");
		//		stay.setIsAccepted(false);
		//		stay.setStartDate(actualDate);
		//		stay.setFinishDate(actualDate);
		//		stay.setClinic(clinic);
		//		stay.setPet(pet);
		//
		//		stayService.saveEntity(stay);
		//------------------------------------

		//Act --------------------------------
		Collection<Stay> stays = (Collection<Stay>) stayService.findAllStayByPet(TEST_PET_ID);
		//-----------------------------------

		//Assertion -------------------------
		assertTrue(stays.size() != 0);
		//----------------------------------
	}

	@Test
	public void testFindAllPendingByOwner() {
		//Act & assert-------------------------------
		stayService.findAllPendingByOwner(TEST_OWNER_ID).forEach((x) -> assertTrue(!x.getIsAccepted() && x.getPet().getOwner().getId() == TEST_OWNER_ID));
		//-------------------------------------------
	}

	@Test
	public void testFindAllAcceptedByOwner() {
		//Act & assert-------------------------------
		stayService.findAllAcceptedByOwner(TEST_OWNER_ID).forEach((x) -> assertTrue(x.getIsAccepted() && x.getPet().getOwner().getId() == TEST_OWNER_ID));
		//-------------------------------------------
	}

}
