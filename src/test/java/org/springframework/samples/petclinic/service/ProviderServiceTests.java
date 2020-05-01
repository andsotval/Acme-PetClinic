
package org.springframework.samples.petclinic.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Provider;
import org.springframework.samples.petclinic.model.Suggestion;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.stereotype.Service;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class ProviderServiceTests {

	private static final int	TEST_PROVIDER_ID		= 1;
	private static final int	TEST_FAKE_PROVIDER_ID	= 2;
	private static final int	TEST_MANAGER_ID			= 1;

	@Autowired
	private ProviderService		providerService;
	
	private Validator createValidator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.afterPropertiesSet();
		return localValidatorFactoryBean;
	}

	@Test
	public void testFindAvailableProvidersPositive() {
		this.providerService.findAvailableProviders().forEach(provider -> Assertions.assertEquals(null, provider.getManager()));
	}

	@Test
	public void testFindProvidersByManagerIdPositive() {
		int managerId = 1;
		this.providerService.findProvidersByManagerId(managerId).forEach(provider -> Assertions.assertEquals(managerId, provider.getManager().getId()));
	}

	@Test
	public void testFindProvidersByManagerIdNegative() {
		int managerId = 1;
		int notManagerId = 2;
		this.providerService.findProvidersByManagerId(managerId).forEach(provider -> Assertions.assertNotEquals(notManagerId, provider.getManager().getId()));
	}

	@Test
	public void testFindProvidersByManagerIdNegativeNotPresent() {
		int managerId = 15;
		this.providerService.findProvidersByManagerId(managerId).forEach(provider -> Assertions.assertEquals(null, provider));
	}

	@Test
	public void testfindProviderByIdPositive() {
		//Fixture
		Provider james = new Provider();
		james.setId(ProviderServiceTests.TEST_PROVIDER_ID);
		james.setFirstName("James");
		james.setLastName("Carter");
		james.setAddress("110 W. Liberty St.");
		james.setCity("Madison");
		james.setTelephone("6085551023");

		this.providerService.saveEntity(james);

		//Act
		Provider jamesTest = this.providerService.findEntityById(ProviderServiceTests.TEST_PROVIDER_ID).get();

		//Assert
		Assertions.assertEquals(james.getId(), jamesTest.getId());
	}

	@Test
	public void testfindProviderByIdNegative() {
		//Fixture
		Provider james = new Provider();
		james.setId(ProviderServiceTests.TEST_PROVIDER_ID);
		james.setFirstName("James");
		james.setLastName("Carter");
		james.setAddress("110 W. Liberty St.");
		james.setCity("Madison");
		james.setTelephone("6085551023");

		this.providerService.saveEntity(james);

		//Act
		Provider notJamesTest = this.providerService.findEntityById(ProviderServiceTests.TEST_FAKE_PROVIDER_ID).get();

		//Assert
		Assertions.assertNotEquals(james.getId(), notJamesTest.getId());
	}

	@Test
	public void testSaveProvider() {
		//Fixture
		Provider james = new Provider();
		james.setId(ProviderServiceTests.TEST_PROVIDER_ID);
		james.setFirstName("James");
		james.setLastName("Carter");
		james.setAddress("110 W. Liberty St.");
		james.setCity("Madison");
		james.setTelephone("6085551023");

		//Act
		this.providerService.saveEntity(james);

		//Assert
		Assert.assertEquals("James", this.providerService.findEntityById(ProviderServiceTests.TEST_PROVIDER_ID).get().getFirstName());
	}
	
	@Test
	public void testSaveProviderWithoutFirstName() {
		//Fixture
		Provider james = new Provider();
		james.setId(ProviderServiceTests.TEST_PROVIDER_ID);;
		james.setLastName("Carter");
		james.setAddress("110 W. Liberty St.");
		james.setCity("Madison");
		james.setTelephone("6085551023");
		
		Validator validator = createValidator();
		Set<ConstraintViolation<Provider>> constraintViolations = validator.validate(james);
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
		Optional<Provider> entity = providerService.findEntityById(1);
		assertTrue(entity.isPresent());
		providerService.deleteEntity(entity.get());
		
		Optional<Provider> deleteEntity = providerService.findEntityById(1);
		assertTrue(!deleteEntity.isPresent());
	}
	
	@Test
	public void testDeleteProviderNonExisisting() {
		Collection<Provider> collection = (Collection<Provider>) providerService.findAllEntities();
		assertEquals(collection.size(), 10);
		
		providerService.deleteEntity(null);
		
		Collection<Provider> collectionAfter = (Collection<Provider>) providerService.findAllEntities();
		assertEquals(collectionAfter.size(), 10);
	}
	
	
	@Test
	public void testDeleteProviderById() {
		Collection<Provider> collection = (Collection<Provider>) providerService.findAllEntities();
		assertEquals(collection.size(), 10);
		
		providerService.deleteEntityById(7);
		
		Collection<Provider> collectionAfter = (Collection<Provider>) providerService.findAllEntities();
		assertEquals(collectionAfter.size(), 10-1);
	}
	
	/*@Test
	public void testDeleteProviderByIdNonExisting() {
		Collection<Provider> collection = (Collection<Provider>) providerService.findAllEntities();
		assertEquals(collection.size(), 10);
		
		providerService.deleteEntityById(90000);
		
		Collection<Provider> collectionAfter = (Collection<Provider>) providerService.findAllEntities();
		assertEquals(collectionAfter.size(), 10);
	}*/

	@Test
	public void testFindAllProviders() {
		Collection<Provider> collection = (Collection<Provider>) providerService.findAllEntities();
		assertEquals(collection.size(), 10);
	}

}
