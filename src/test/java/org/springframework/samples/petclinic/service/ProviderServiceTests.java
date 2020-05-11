
package org.springframework.samples.petclinic.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.samples.petclinic.model.Provider;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@ActiveProfiles("hsqldb")
@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class ProviderServiceTests {

	private int				TEST_MANAGER_ID					= 1;

	private int				TEST_MANAGER_ID_NOT_PRESENT		= 100;

	private int				TEST_PROVIDER_ID				= 1;

	private int				TEST_PROVIDER_ID_NOT_PRESENT	= 100;

	@Autowired
	private ProviderService	providerService;


	private Validator createValidator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.afterPropertiesSet();
		return localValidatorFactoryBean;
	}

	@Test
	public void testFindAvailableProviders() {
		Collection<Provider> providers = providerService.findAvailableProviders();
		providers.forEach(provider -> assertEquals(null, provider.getManager()));
	}

	@Test
	public void testFindProvidersByManagerId() {
		Collection<Provider> providers = providerService.findProvidersByManagerId(TEST_MANAGER_ID);
		providers.forEach(provider -> assertEquals(TEST_MANAGER_ID, provider.getManager().getId()));
	}

	@Test
	public void testFindProvidersByManagerIdNotPresent() {
		Collection<Provider> providers = providerService.findProvidersByManagerId(TEST_MANAGER_ID_NOT_PRESENT);
		assertEquals(0, providers.size());
	}

	@Test
	public void testFindAllProviders() {
		Collection<Provider> collection = (Collection<Provider>) providerService.findAllEntities();
		assertEquals(collection.size(), 10);
	}

	@Test
	public void testFindProviderById() {
		Optional<Provider> entity = providerService.findEntityById(TEST_PROVIDER_ID);
		assertTrue(entity.isPresent());
		assertEquals(entity.get().getId(), TEST_PROVIDER_ID);
	}

	@Test
	public void testFindProviderByIdNotPresent() {
		Optional<Provider> entity = providerService.findEntityById(TEST_PROVIDER_ID_NOT_PRESENT);
		assertFalse(entity.isPresent());
	}

	@Test
	public void testSaveProvider() {

		Collection<Provider> collection = (Collection<Provider>) providerService.findAllEntities();
		int collectionSize = collection.size();

		Provider entity = new Provider();
		entity.setFirstName("James");
		entity.setLastName("Carter");
		entity.setAddress("110 W. Liberty St.");
		entity.setCity("Madison");
		entity.setTelephone("608555123");
		entity.setMail("provider@mail.com");

		providerService.saveEntity(entity);

		Collection<Provider> newCollection = (Collection<Provider>) providerService.findAllEntities();
		int newCollectionSize = newCollection.size();

		assertEquals(collectionSize + 1, newCollectionSize);

		Assert.assertEquals("James", providerService.findEntityById(newCollectionSize).get().getFirstName());
	}

	@Test
	public void testSaveProviderWithoutFirstName() {
		Provider entity = new Provider();
		entity.setLastName("Carter");
		entity.setAddress("110 W. Liberty St.");
		entity.setCity("Madison");
		entity.setTelephone("6085551023");

		Validator validator = createValidator();
		Set<ConstraintViolation<Provider>> constraintViolations = validator.validate(entity);
		assertEquals(constraintViolations.size(), 3);

		Iterator<ConstraintViolation<Provider>> it = constraintViolations.iterator();
		while (it.hasNext()) {
			ConstraintViolation<Provider> violation = it.next();
			String message = violation.getMessage();
			System.out.println(message);

			switch (violation.getPropertyPath().toString()) {
			case "first name":
				assertTrue(message.equals("no puede estar vacío"));
				break;
			default:
				break;
			}
		}
	}

	@Test
	public void testDeleteProvider() {
		Optional<Provider> entity = providerService.findEntityById(TEST_PROVIDER_ID);
		assertTrue(entity.isPresent());
		providerService.deleteEntity(entity.get());

		Optional<Provider> deleteEntity = providerService.findEntityById(TEST_PROVIDER_ID);
		assertTrue(!deleteEntity.isPresent());
	}

	@Test
	public void testDeleteProviderNotPresent() {
		Collection<Provider> collection = (Collection<Provider>) providerService.findAllEntities();
		int collectionSize = collection.size();

		providerService.deleteEntity(null);

		Collection<Provider> newCollection = (Collection<Provider>) providerService.findAllEntities();
		int newCollectionSize = newCollection.size();

		assertEquals(collectionSize, newCollectionSize);
	}

	@Test
	public void testDeleteProviderById() {
		providerService.deleteEntityById(TEST_PROVIDER_ID);

		Optional<Provider> entity = providerService.findEntityById(TEST_PROVIDER_ID);
		assertTrue(!entity.isPresent());
	}

	@Test
	public void testDeleteProviderByIdNotPresent() {
		boolean deleted = true;

		try {
			providerService.deleteEntityById(TEST_PROVIDER_ID_NOT_PRESENT);
		} catch (EmptyResultDataAccessException e) {
			deleted = false;
		}

		assertFalse(deleted);
	}

}
