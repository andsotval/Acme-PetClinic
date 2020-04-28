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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Clinic;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
class OwnerServiceTests {

	@Autowired
	protected OwnerService	ownerService;

	@Autowired
	protected ClinicService	clinicService;


	@Test
	public void TestFindClinicByManagerIdPositive() {
		String name = "Padilla";
		Collection<Owner> owners = ownerService.findOwnerByLastName(name);
		assertEquals(owners.size(), 1);
	}
	@Test
	public void TestFindClinicByManagerIdNegative() {
		String name = "APELIIDOPRUEBA";
		Collection<Owner> owners = ownerService.findOwnerByLastName(name);
		assertEquals(owners.size(), 0);
	}

	@Test
	public void TestFindClinicByManagerIdNegativeNotPresent() {
		String name = "Clinic1";
		Collection<Owner> owners = ownerService.findOwnerByLastName(name);
		assertEquals(owners.size(), 0);
		;
	}

	@Test
	public void TestFindClinicByNamePositive() {
		String name = "owner1";
		assertEquals(name, ownerService.findByOwnerByUsername(name).getUser().getUsername());
	}

	@Test
	public void TestFindClinicByNameNegativeNotPresent() {
		String name = "OWNERPRUEBA";
		assertEquals(null, ownerService.findByOwnerByUsername(name));
	}

	@Test
	public void TestFindClinicByNameNegative() {
		String name = "owner1";
		String nameWrong = "owner2";
		assertNotEquals(nameWrong, ownerService.findByOwnerByUsername(name).getUser().getUsername());
	}

	@Test
	public void TestFindPetsByClinicPositive() {
		Clinic clinic = clinicService.findEntityById(1).get();
		assertNotNull(clinic);
		Iterable<Owner> pets = ownerService.findOwnersByClinicId(clinic.getId());
		assertNotNull(pets);
	}

	@Test
	public void TestFindPetsByClinicNegative() {
		Clinic clinic = null;
		assertNull(clinic);
		Iterable<Owner> pets = ownerService.findOwnersByClinicId(null);
		assertNotNull(pets);
	}

}
