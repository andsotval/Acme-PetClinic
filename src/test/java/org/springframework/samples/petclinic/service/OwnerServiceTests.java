
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
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.stereotype.Service;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
class OwnerServiceTests {

	@Autowired
	protected OwnerService	ownerService;

	@Autowired
	protected ClinicService	clinicService;

	private String			TEST_OWNER_LAST_NAME				= "Padilla";

	private String			TEST_OWNER_LAST_NAME_NOT_PRESENT	= "Last Name Not Present";

	private String			TEST_USERNAME						= "owner1";

	private String			TEST_USERNAME_NOT_PRESENT			= "owner user name not present";

	private int				TEST_CLINIC_ID						= 1;

	private int				TEST_CLINIC_ID_NOT_PRESENT			= 100;

	private int				TEST_OWNER_ID						= 1;

	private int				TEST_OWNER_ID_NOT_PRESENT			= 100;


	private Validator createValidator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.afterPropertiesSet();
		return localValidatorFactoryBean;
	}

	@Test
	public void testFindOwnerByLastName() {
		Collection<Owner> owners = ownerService.findOwnerByLastName(TEST_OWNER_LAST_NAME);
		assertEquals(owners.size(), 1);
	}

	@Test
	public void testFindOwnerByLastNameNotPresent() {
		Collection<Owner> owners = ownerService.findOwnerByLastName(TEST_OWNER_LAST_NAME_NOT_PRESENT);
		assertEquals(owners.size(), 0);
	}

	@Test
	public void testFindOwnerByUsername() {
		String name = ownerService.findByOwnerByUsername(TEST_USERNAME).getUser().getUsername();
		assertEquals(name, TEST_USERNAME);
	}

	@Test
	public void testFindOwnerByUsernameNotPresent() {
		Owner owner = ownerService.findByOwnerByUsername(TEST_USERNAME_NOT_PRESENT);
		assertEquals(null, owner);
	}

	@Test
	public void testFindOwnersByClinicId() {
		Iterable<Owner> owners = ownerService.findOwnersByClinicId(TEST_CLINIC_ID);
		owners.forEach(o -> assertEquals(TEST_CLINIC_ID, o.getClinic().getId()));
	}

	@Test
	public void testFindOwnersByClinicIdNotPresent() {
		Collection<Owner> owners = ownerService.findOwnersByClinicId(TEST_CLINIC_ID_NOT_PRESENT);
		assertEquals(0, owners.size());
	}

	@Test
	public void testFindAllOwners() {
		Collection<Owner> collection = (Collection<Owner>) ownerService.findAllEntities();
		assertEquals(collection.size(), 11);
	}

	@Test
	public void testFindOwnerById() {
		Optional<Owner> entity = ownerService.findEntityById(TEST_OWNER_ID);
		assertTrue(entity.isPresent());
		assertTrue(entity.get().getId().equals(TEST_OWNER_ID));
	}

	@Test
	public void testFindEntityByIdNegative() {
		Optional<Owner> entity = ownerService.findEntityById(TEST_OWNER_ID_NOT_PRESENT);
		assertTrue(!entity.isPresent());
	}

	@Test
	public void testSaveOwner() {
		Collection<Owner> collection = (Collection<Owner>) ownerService.findAllEntities();
		int collectionSize = collection.size();

		Owner entity = new Owner();
		entity.setFirstName("FirstName 1");
		entity.setLastName("LastName 1");
		entity.setMail("mail@mail.com");
		entity.setAddress("Address 1");
		entity.setCity("City 1");
		entity.setTelephone("912654555");
		ownerService.saveEntity(entity);

		collection = (Collection<Owner>) ownerService.findAllEntities();
		assertEquals(collection.size(), collectionSize + 1);

		Optional<Owner> newEntity = ownerService.findEntityById(collectionSize + 1);
		assertTrue(newEntity.isPresent());
		assertEquals(newEntity.get().getFirstName(), "FirstName 1");
		assertEquals(newEntity.get().getLastName(), "LastName 1");
		assertEquals(newEntity.get().getMail(), "mail@mail.com");
		assertEquals(newEntity.get().getAddress(), "Address 1");
		assertEquals(newEntity.get().getCity(), "City 1");
		assertEquals(newEntity.get().getTelephone(), "912654555");
	}

	@Test
	public void testSaveOwnerEmptyParameters() {
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
		assertEquals(constraintViolations.size(), 6);
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
	public void testDeleteOwner() {
		Optional<Owner> entity = ownerService.findEntityById(TEST_OWNER_ID);
		assertTrue(entity.isPresent());
		ownerService.deleteEntity(entity.get());

		Optional<Owner> deleteEntity = ownerService.findEntityById(TEST_OWNER_ID);
		assertTrue(!deleteEntity.isPresent());
	}

	@Test
	public void testDeleteOwnerNotPresent() {
		Collection<Owner> collection = (Collection<Owner>) ownerService.findAllEntities();
		int collectionSize = collection.size();

		ownerService.deleteEntity(null);

		Collection<Owner> newCollection = (Collection<Owner>) ownerService.findAllEntities();
		int newCollectionSize = newCollection.size();

		assertEquals(collectionSize, newCollectionSize);
	}

	@Test
	public void testDeleteOwnerById() {
		ownerService.deleteEntityById(TEST_OWNER_ID);

		Optional<Owner> entity = ownerService.findEntityById(TEST_OWNER_ID);
		assertTrue(!entity.isPresent());
	}

	@Test
	public void testDeleteOwnerByIdNotPresent() {
		boolean deleted = true;

		try {
			ownerService.deleteEntityById(TEST_OWNER_ID_NOT_PRESENT);
		} catch (EmptyResultDataAccessException e) {
			deleted = false;
		}

		assertFalse(deleted);
	}
}
