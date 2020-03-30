
package org.springframework.samples.petclinic.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Optional;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Authorities;
import org.springframework.samples.petclinic.model.Manager;
import org.springframework.samples.petclinic.model.Provider;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.ManagerService;
import org.springframework.samples.petclinic.service.OrderService;
import org.springframework.samples.petclinic.service.ProductService;
import org.springframework.samples.petclinic.service.ProviderService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = OrderController.class,
	excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
	excludeAutoConfiguration = SecurityConfiguration.class)
public class OrderControllerTest {

	private static final int	TEST_PROVIDER_ID	= 1;

	private static final int	TEST_MANAGER_ID		= 3;

	@Autowired
	private OrderService		orderService;

	@Autowired
	private ManagerService		managerService;

	@Autowired
	private ProductService		productService;

	@Autowired
	private ProviderService		providerService;

	@Autowired
	private MockMvc				mockMvc;


	@BeforeEach
	void setup() {

		Provider james = new Provider();
		james.setId(TEST_PROVIDER_ID);
		james.setFirstName("James");
		james.setLastName("Carter");
		james.setAddress("110 W. Liberty St.");
		james.setCity("Madison");
		james.setTelephone("6085551023");

		Optional<Provider> opt = Optional.of(james);
		BDDMockito.given(providerService.findProviderById(TEST_PROVIDER_ID)).willReturn(opt);

		Provider helen = new Provider();
		helen.setFirstName("Helen");
		helen.setLastName("Leary");
		helen.setAddress("110 W. Liberty St.");
		helen.setCity("Madison");
		helen.setTelephone("6085551023");
		helen.setId(2);

		BDDMockito.given(providerService.findAvailableProviders()).willReturn(Lists.newArrayList(james, helen));

		User user = new User();
		user.setEnabled(true);
		user.setUsername("pepito");
		user.setPassword("pepito");

		Authorities authority = new Authorities();
		authority.setAuthority("manager");
		authority.setUsername("pepito");
		Manager pepe = new Manager();
		pepe.setUser(user);
		pepe.setId(TEST_MANAGER_ID);
		pepe.setFirstName("Pepe");
		pepe.setLastName("Leary");
		pepe.setAddress("110 W. Liberty St.");
		pepe.setCity("Madison");
		pepe.setTelephone("6085551023");

		Optional<Manager> manager = Optional.of(pepe);
		BDDMockito.given(managerService.findManagerByUsername("pepito")).willReturn(manager);

	}

	@WithMockUser(value = "spring")
	@Test
	void testInitCreationForm() throws Exception {
		mockMvc.perform(get("/orders/new/{providerId}", TEST_PROVIDER_ID)).andExpect(status().isOk())
			.andExpect(model().attributeExists("order")).andExpect(model().attributeExists("products"))
			.andExpect(view().name("/orders/createOrUpdateOrderForm"));
	}

}
