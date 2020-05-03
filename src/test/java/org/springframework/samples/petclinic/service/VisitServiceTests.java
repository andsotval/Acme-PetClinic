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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;
import java.util.stream.StreamSupport;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.stereotype.Service;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
class VisitServiceTests {

	@Autowired
	protected VisitService service;


	private Validator createValidator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.afterPropertiesSet();
		return localValidatorFactoryBean;
	}

	@Test
	public void testFindAllPendingByVet() {
		Iterable<Visit> visits = service.findAllPendingByVetId(4);
		visits.forEach(v -> assertNull(v.getIsAccepted()));
	}

	@Test
	public void testFindAllPendingByVetNonExisitingVet() {
		Iterable<Visit> visits = service.findAllPendingByVetId(null);
		Integer visitsNumber = (int) StreamSupport.stream(visits.spliterator(), false).count();
		assertEquals(visitsNumber, 0);
	}

	@Test
	public void testFindAllAcceptedByVet() {
		Iterable<Visit> visits = service.findAllAcceptedByVetId(1);
		visits.forEach(v -> assertTrue(v.getIsAccepted()));
	}

	@Test
	public void testFindAllAcceptedByVetNonExisitingVet() {
		Iterable<Visit> visits = service.findAllAcceptedByVetId(null);
		Integer visitsNumber = (int) StreamSupport.stream(visits.spliterator(), false).count();
		assertEquals(visitsNumber, 0);
	}

	@Test
	public void testDeleteByPetId() {
		service.deleteByPetId(1);

	}

	@Test
	public void testDeleteByPetIdNonExisitngPet() {
		service.deleteByPetId(null);
	}

	@Test
	public void testFindAllPendingByOwnerId() {
		Iterable<Visit> visits = service.findAllPendingByOwnerId(4);
		visits.forEach(v -> assertNull(v.getIsAccepted()));
	}

	@Test
	public void testFindAllPendingByOwnerIdNonExisitngOwner() {
		Iterable<Visit> visits = service.findAllPendingByOwnerId(null);
		Integer visitsNumber = (int) StreamSupport.stream(visits.spliterator(), false).count();
		assertEquals(visitsNumber, 0);
	}

	@Test
	public void testFindAllAcceptedByOwnerId() {
		Iterable<Visit> visits = service.findAllAcceptedByOwnerId(1);
		visits.forEach(v -> assertTrue(v.getIsAccepted()));
	}

	@Test
	public void testFindAllAcceptedByOwnerIdNonExistingOwner() {
		Iterable<Visit> visits = service.findAllAcceptedByOwnerId(null);
		Integer visitsNumber = (int) StreamSupport.stream(visits.spliterator(), false).count();
		assertEquals(visitsNumber, 0);
	}

	@Test
	public void testFindAllByPetId() {
		Iterable<Visit> visits = service.findAllByPetId(1);
		Integer visitsNumber = (int) StreamSupport.stream(visits.spliterator(), false).count();
		assertEquals(visitsNumber, 1);
	}

	@Test
	public void testFindAllByPetIdNonExistingPet() {
		Iterable<Visit> visits = service.findAllByPetId(null);
		assertNull(visits);
	}

	@Test
	public void testFindAllByDateTime() {
		LocalDateTime dateTime = LocalDateTime.of(2020, 8, 9, 9, 30, 00);
		Iterable<Visit> visits = service.findAllByDateTime(dateTime);
		Integer visitsNumber = (int) StreamSupport.stream(visits.spliterator(), false).count();
		assertEquals(visitsNumber, 1);
	}

	@Test
	public void testFindAllByDateTimeNullTime() {
		Iterable<Visit> visits = service.findAllByDateTime(null);
		Integer visitsNumber = (int) StreamSupport.stream(visits.spliterator(), false).count();
		assertEquals(visitsNumber, 0);
	}

	@Test
	public void testSaveVisit() {
		Collection<Visit> collection = (Collection<Visit>) service.findAllEntities();
		int size = collection.size();

		LocalDateTime now = LocalDateTime.now();
		Visit entity = new Visit();
		entity.setDescription("Description of the visit");
		entity.setDateTime(now);
		service.saveEntity(entity);

		collection = (Collection<Visit>) service.findAllEntities();
		assertEquals(collection.size(), size+1);

		Optional<Visit> newEntity = service.findEntityById(12);
		assertTrue(newEntity.isPresent());
		assertEquals(newEntity.get().getDescription(), "Description of the visit");
		assertEquals(newEntity.get().getDateTime(), now);
	}

	@Test
	public void testSaveVisitWithoutDescription() {
		LocalDateTime now = LocalDateTime.now();
		Visit entity = new Visit();
		entity.setDateTime(now);

		Validator validator = createValidator();
		Set<ConstraintViolation<Visit>> constraintViolations = validator.validate(entity);
		assertEquals(constraintViolations.size(), 1);

		Iterator<ConstraintViolation<Visit>> it = constraintViolations.iterator();
		while (it.hasNext()) {
			ConstraintViolation<Visit> violation = it.next();
			String message = violation.getMessage();
			System.out.println(message);

			switch (violation.getPropertyPath().toString()) {
			case "description":
				assertTrue(message.equals("no puede estar vacío"));
				break;
			default:
				break;
			}
		}

	}

	@Test
	public void testDeleteVisit() {
		Optional<Visit> entity = service.findEntityById(1);
		assertTrue(entity.isPresent());
		service.deleteEntity(entity.get());

		Optional<Visit> deleteEntity = service.findEntityById(1);
		assertTrue(!deleteEntity.isPresent());
	}

	@Test
	public void testDeleteVisitNonExisisting() {
		Collection<Visit> collection = (Collection<Visit>) service.findAllEntities();
		assertEquals(collection.size(), 11);

		service.deleteEntity(null);

		Collection<Visit> collectionAfter = (Collection<Visit>) service.findAllEntities();
		assertEquals(collectionAfter.size(), 11);
	}

	@Test
	public void testDeleteVisitById() {
		Collection<Visit> collection = (Collection<Visit>) service.findAllEntities();
		assertEquals(collection.size(), 11);

		service.deleteEntityById(1);

		Collection<Visit> collectionAfter = (Collection<Visit>) service.findAllEntities();
		assertEquals(collectionAfter.size(), 11 - 1);
	}

	/*
	 * @Test
	 * public void testDeleteVisitByIdNonExisting() {
	 * Collection<Visit> collection = (Collection<Visit>) service.findAllEntities();
	 * assertEquals(collection.size(), 11);
	 * 
	 * service.deleteEntityById(90000);
	 * 
	 * Collection<Visit> collectionAfter = (Collection<Visit>) service.findAllEntities();
	 * assertEquals(collectionAfter.size(), 11);
	 * }
	 */

	@Test
	public void testFindAllVisits() {
		Collection<Visit> collection = (Collection<Visit>) service.findAllEntities();
		assertEquals(collection.size(), 11);
	}

	@Test
	public void testFindVisitById() {
		Optional<Visit> entity = service.findEntityById(1);
		assertTrue(entity.isPresent());
		assertEquals(entity.get().getId(), 1);
	}

	@Test
	public void testFindVisitByIdNonExisiting() {
		Optional<Visit> entity = service.findEntityById(900000);
		assertFalse(entity.isPresent());
	}

}
