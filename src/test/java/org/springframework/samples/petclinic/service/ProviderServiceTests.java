package org.springframework.samples.petclinic.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class ProviderServiceTests {
	
	@Autowired
	private ProviderService providerService;
	
	@Test
	public void testFindAvailableProvidersPositive() {
		this.providerService.findAvailableProviders().forEach(provider -> assertEquals(null, provider.getManager()));
	}
	
	
	@Test
	public void testFindProvidersByManagerIdPositive(){
		int managerId = 1;
		this.providerService.findProvidersByManagerId(managerId).forEach(provider -> assertEquals(managerId, provider.getManager().getId()));
		
	}
	@Test
	public void testFindProvidersByManagerIdNegative(){
		int managerId = 1;
		int notManagerId = 2;		
		this.providerService.findProvidersByManagerId(managerId).forEach(provider -> assertNotEquals(notManagerId, provider.getManager().getId()));
		
	}
	
	@Test
	public void testFindProvidersByManagerIdNegativeNotPresent(){
		int managerId = 15;
		this.providerService.findProvidersByManagerId(managerId).forEach(provider -> assertEquals(null, provider));
		
	}
}
