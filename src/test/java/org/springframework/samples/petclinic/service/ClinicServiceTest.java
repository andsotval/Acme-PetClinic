package org.springframework.samples.petclinic.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Clinic;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class ClinicServiceTest {
	
	@Autowired
	protected ClinicService clinicService;
	
	@Test
	public void TestFindClinicByManagerIdPositive() {
		int managerId = 1;
		Clinic clinic = this.clinicService.findClinicByManagerId(managerId);
		assertEquals(managerId, clinic.getManager().getId());
	}
	@Test
	public void TestFindClinicByManagerIdNegative() {
		int managerId = 1;
		int notManagerId = 2;
		Clinic clinic = this.clinicService.findClinicByManagerId(managerId);
		assertNotEquals(notManagerId, clinic.getManager().getId());
	}
	
	@Test
	public void TestFindClinicByManagerIdNegativeNotPresent() {
		int managerId = 15;
		Clinic clinic = this.clinicService.findClinicByManagerId(managerId);
		assertEquals(null, clinic);
	}
	
	@Test
	public void TestFindClinicByNamePositive() {
		String name = "Clinic1";
		assertEquals(name, this.clinicService.findClinicByName(name).get().getName());	
	}
	
	@Test
	public void TestFindClinicByNameNegativeNotPresent() {
		String name = "ClinicaDePrueba";
		assertEquals(false, this.clinicService.findClinicByName(name).isPresent());	
	}
	
	@Test
	public void TestFindClinicByNameNegative() {
		String name = "Clinic1";
		String nameWrong = "Clinic2";
		assertNotEquals(nameWrong, this.clinicService.findClinicByName(name).get().getName());	
	}

}
