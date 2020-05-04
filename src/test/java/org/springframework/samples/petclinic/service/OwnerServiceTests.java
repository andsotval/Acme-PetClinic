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
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.stereotype.Service;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
class OwnerServiceTests {

	@Autowired
	protected OwnerService	ownerService;

	@Autowired
	protected ClinicService	clinicService;


	private Validator createValidator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.afterPropertiesSet();
		return localValidatorFactoryBean;
	}

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

	@Test
	public void testFindAllEntities() {
		Collection<Owner> collection = (Collection<Owner>) ownerService.findAllEntities();
		assertEquals(collection.size(), 8);
	}

	@Test
	public void testFindEntityByIdPositive() {
		Optional<Owner> entity = ownerService.findEntityById(1);
		assertTrue(entity.isPresent());
		assertTrue(entity.get().getId().equals(1));
	}

	@Test
	public void testFindEntityByIdNegative() {
		Optional<Owner> entity = ownerService.findEntityById(99);
		assertTrue(!entity.isPresent());
	}

	@Test
	public void testSaveEntityPositive() {
		Collection<Owner> collection = (Collection<Owner>) ownerService.findAllEntities();
		assertEquals(collection.size(), 8);

		Owner entity = new Owner();
		entity.setFirstName("FirstName 1");
		entity.setLastName("LastName 1");
		entity.setMail("mail@mail.com");
		entity.setAddress("Address 1");
		entity.setCity("City 1");
		entity.setTelephone("912654555");

		collection = (Collection<Owner>) ownerService.findAllEntities();
		assertEquals(collection.size(), 4);

		Optional<Owner> newEntity = ownerService.findEntityById(5);
		assertTrue(newEntity.isPresent());
		assertEquals(newEntity.get().getFirstName(), "FirstName 1");
		assertEquals(newEntity.get().getLastName(), "LastName 1");
		assertEquals(newEntity.get().getMail(), "mail@mail.com");
		assertEquals(newEntity.get().getAddress(), "Address 1");
		assertEquals(newEntity.get().getCity(), "City 1");
		assertEquals(newEntity.get().getTelephone(), "912654555");
	}

	@Test
	public void testSaveEntityNegative() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Owner entity = new Owner();
		entity.setFirstName(null);
		entity.setLastName(null);
		entity.setMail(null);
		entity.setAddress(null);
		entity.setCity(null);
		entity.setTelephone(null);

		Validator validator = createValidator();
		Set<ConstraintViolation<Owner>> constraintViolations = validator.validate(entity);
		assertEquals(constraintViolations.size(), 3);
		Iterator<ConstraintViolation<Owner>> it = constraintViolations.iterator();
		while (it.hasNext()) {
			ConstraintViolation<Owner> violation = it.next();
			String message = violation.getMessage();

			switch (violation.getPropertyPath().toString()) {
			case "first_name":
				assertTrue(message.equals("must not be empty"));
				break;
			case "last_anme":
				assertTrue(message.equals("must not be empty"));
				break;
			case "mail":
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
		Optional<Owner> entity = ownerService.findEntityById(1);
		assertTrue(entity.isPresent());
		ownerService.deleteEntity(entity.get());

		Optional<Owner> deleteEntity = ownerService.findEntityById(1);
		assertTrue(!deleteEntity.isPresent());
	}

	@Test
	public void testDeleteEntityById() {
		ownerService.deleteEntityById(1);

		Optional<Owner> entity = ownerService.findEntityById(1);
		assertTrue(!entity.isPresent());
	}
}
