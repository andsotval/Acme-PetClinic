package org.springframework.samples.petclinic.service;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Manager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class ProviderServiceTests {
	
	@Autowired
	private ProviderService providerService;
	
	@Autowired
	private ManagerService managerService;
	
	@Test
	public void testFindAvailableProviders() {
		
		this.providerService.findAllProviders()
			.forEach(provider -> assertEquals(provider.getManager(),null));
	}
	
	@Test
	public void testFindProvidersByManagerId() {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		User user = (User) authentication.getPrincipal();

		Manager manager = this.managerService.findManagerByUsername(user.getUsername()).get();
		
		this.providerService.findAllProviders()
			.forEach(provider -> assertEquals(this.providerService.findProvidersByManagerId(provider.getManager().getId()),manager.getId()));
	}
	
	
	@Test
	public void testFindProviderById() {
		
		this.providerService.findAllProviders()
			.forEach(provider -> assertEquals(this.providerService.findProvidersByManagerId(provider.getId()),provider.getId()));
	}

}
