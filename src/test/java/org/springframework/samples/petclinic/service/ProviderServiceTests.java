
package org.springframework.samples.petclinic.service;

import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Provider;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class ProviderServiceTests {

	private static final int	TEST_PROVIDER_ID		= 1;
	private static final int	TEST_FAKE_PROVIDER_ID	= 2;
	private static final int	TEST_MANAGER_ID			= 1;

	@Autowired
	private ProviderService		providerService;


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
}
