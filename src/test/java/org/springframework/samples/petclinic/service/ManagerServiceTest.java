
package org.springframework.samples.petclinic.service;


import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.springframework.samples.petclinic.model.Manager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class ManagerServiceTest {

	@Autowired
	protected ManagerService managerService;
	
	private int TEST_MANAGER_ID = 1;
	
	private int TEST_MANAGER_ID_NOT_PRESENT = 100;
	
	private Validator createValidator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.afterPropertiesSet();
		return localValidatorFactoryBean;
	}

	@Test
	public void testFindAllManagers() {
		Collection<Manager> collection = (Collection<Manager>) managerService.findAllEntities();
		assertEquals(collection.size(), 5);
	}

	@Test
	public void testFindManagerById() {
		Optional<Manager> entity = managerService.findEntityById(TEST_MANAGER_ID);
		assertTrue(entity.isPresent());
		assertTrue(entity.get().getId().equals(1));
	}

	@Test
	public void testFindClinicByIdNotPresent() {
		Optional<Manager> entity = managerService.findEntityById(TEST_MANAGER_ID_NOT_PRESENT);
		assertTrue(!entity.isPresent());
	}

	@Test
	public void testSaveManager() {
		Collection<Manager> collection = (Collection<Manager>) managerService.findAllEntities();
		int collectionSize = collection.size();

		Manager entity = new Manager();
		entity.setFirstName("Manager Name");
		entity.setLastName("Manager Surname");
		entity.setAddress("Manager Address");
		entity.setCity("Manager City");
		entity.setTelephone("609817527");
		entity.setMail("manager@mail.com");
		managerService.saveEntity(entity);

		collection = (Collection<Manager>) managerService.findAllEntities();
		assertEquals(collection.size(), collectionSize+1);

		Optional<Manager> newEntity = managerService.findEntityById(collectionSize+1);
		assertTrue(newEntity.isPresent());
		assertEquals(newEntity.get().getFirstName(), "Manager Name");
		assertEquals(newEntity.get().getLastName(), "Manager Surname");
		assertEquals(newEntity.get().getAddress(), "Manager Address");
		assertEquals(newEntity.get().getCity(), "Manager City");
		assertEquals(newEntity.get().getTelephone(), "609817527");
	}

	@Test
	public void testSaveManagerWithoutFirstName() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		
		Manager entity = new Manager();
		entity.setFirstName(null);
		entity.setLastName("Manager Surname");
		entity.setAddress("Manager Address");
		entity.setCity("Manager City");
		entity.setTelephone("609817527");

		Validator validator = createValidator();
		Set<ConstraintViolation<Manager>> constraintViolations = validator.validate(entity);
		assertEquals(constraintViolations.size(), 2);
		
		Iterator<ConstraintViolation<Manager>> it = constraintViolations.iterator();
		while (it.hasNext()) {
			ConstraintViolation<Manager> violation = it.next();
			String message = violation.getMessage();

			switch (violation.getPropertyPath().toString()) {
			case "first_name":
				assertTrue(message.equals("must not be empty"));
				break;
			default:
				break;
			}
		}
	}

	@Test
	public void testDeleteManager() {
		Optional<Manager> entity = managerService.findEntityById(TEST_MANAGER_ID);
		assertTrue(entity.isPresent());
		managerService.deleteEntity(entity.get());

		Optional<Manager> deleteEntity = managerService.findEntityById(TEST_MANAGER_ID);
		assertTrue(!deleteEntity.isPresent());
	}

	@Test
	public void testDeleteEntityById() {
		managerService.deleteEntityById(TEST_MANAGER_ID);

		Optional<Manager> entity = managerService.findEntityById(TEST_MANAGER_ID);
		assertTrue(!entity.isPresent());
	}

}
