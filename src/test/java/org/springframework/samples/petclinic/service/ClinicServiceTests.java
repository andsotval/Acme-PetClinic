
package org.springframework.samples.petclinic.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Clinic;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class ClinicServiceTest {

	@Autowired
	protected ClinicService clinicService;


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
}
