
package org.springframework.samples.petclinic.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
import org.springframework.samples.petclinic.model.Manager;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@ActiveProfiles("hsqldb")
@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class ManagerServiceTest {

	@Autowired
	protected ManagerService	managerService;

	private int					TEST_MANAGER_ID				= 1;

	private int					TEST_MANAGER_ID_NOT_PRESENT	= 100;


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
		assertTrue(entity.get().getId().equals(TEST_MANAGER_ID));
	}

	@Test
	public void testFindManagerByIdNotPresent() {
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
		assertEquals(collection.size(), collectionSize + 1);

		Optional<Manager> newEntity = managerService.findEntityById(collectionSize + 1);
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
	public void testDeleteManagerNotPresent() {
		Collection<Manager> collection = (Collection<Manager>) managerService.findAllEntities();
		int collectionSize = collection.size();

		managerService.deleteEntity(null);

		Collection<Manager> newCollection = (Collection<Manager>) managerService.findAllEntities();
		int newCollectionSize = newCollection.size();

		assertEquals(collectionSize, newCollectionSize);
	}

	@Test
	public void testDeleteManagerById() {
		managerService.deleteEntityById(TEST_MANAGER_ID);

		Optional<Manager> entity = managerService.findEntityById(TEST_MANAGER_ID);
		assertTrue(!entity.isPresent());
	}

	@Test
	public void testDeleteManagerByIdNotPresent() {
		boolean deleted = true;

		try {
			managerService.deleteEntityById(TEST_MANAGER_ID_NOT_PRESENT);
		} catch (EmptyResultDataAccessException e) {
			deleted = false;
		}

		assertFalse(deleted);
	}

}
