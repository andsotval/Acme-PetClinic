
package org.springframework.samples.petclinic.web;

import java.util.Optional;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Authorities;
import org.springframework.samples.petclinic.model.Manager;
import org.springframework.samples.petclinic.model.Provider;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.ManagerService;
import org.springframework.samples.petclinic.service.ProviderService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/**
 * Test class for the {@link VetController}
 */
@WebMvcTest(controllers = ProviderController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
class ProviderControllerTests {

	//Provider with no Manager associated
	private static final int	TEST_PROVIDER1_ID	= 1;
	//Provider with a Manager associated
	private static final int	TEST_PROVIDER2_ID	= 2;

	private static final int	TEST_MANAGER_ID		= 3;

	@MockBean
	private ProviderService		providerService;

	@MockBean
	private ManagerService		managerService;

	@Autowired
	private MockMvc				mockMvc;


	@BeforeEach
	void setup() {

		Provider james = new Provider();
		james.setId(ProviderControllerTests.TEST_PROVIDER1_ID);
		james.setFirstName("James");
		james.setLastName("Carter");
		james.setAddress("110 W. Liberty St.");
		james.setCity("Madison");
		james.setTelephone("6085551023");

		Optional<Provider> opt = Optional.of(james);
		BDDMockito.given(this.providerService.findEntityById(ProviderControllerTests.TEST_PROVIDER1_ID)).willReturn(opt);

		Provider helen = new Provider();
		helen.setFirstName("Helen");
		helen.setLastName("Leary");
		helen.setAddress("110 W. Liberty St.");
		helen.setCity("Madison");
		helen.setTelephone("6085551023");
		helen.setId(ProviderControllerTests.TEST_PROVIDER2_ID);

		Optional<Provider> opt2 = Optional.of(helen);
		BDDMockito.given(this.providerService.findEntityById(ProviderControllerTests.TEST_PROVIDER2_ID)).willReturn(opt2);

		BDDMockito.given(this.providerService.findAvailableProviders()).willReturn(Lists.newArrayList(james, helen));

		User user = new User();
		user.setEnabled(true);
		user.setUsername("pepito");
		user.setPassword("pepito");

		Authorities authority = new Authorities();
		authority.setAuthority("manager");
		authority.setUsername("pepito");
		Manager pepe = new Manager();
		pepe.setUser(user);
		pepe.setId(ProviderControllerTests.TEST_MANAGER_ID);
		pepe.setFirstName("Pepe");
		pepe.setLastName("Leary");
		pepe.setAddress("110 W. Liberty St.");
		pepe.setCity("Madison");
		pepe.setTelephone("6085551023");

		BDDMockito.given(this.managerService.findPersonByUsername("pepito")).willReturn(pepe);

	}

	@WithMockUser(value = "spring")
	@Test
	void testShowProvidersAvailable() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/providers/listAvailable")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("providers"))
			.andExpect(MockMvcResultMatchers.view().name("providers/providersList"));
	}

	@WithMockUser(value = "pepito", authorities = {
		"manager"
	})
	@Test
	void testDoNotAcceptRepeatedProvider() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/providers/addProvider/{providerId}", ProviderControllerTests.TEST_PROVIDER2_ID))/* .andExpect(MockMvcResultMatchers.model().attributeExists("message")) */
			.andExpect(MockMvcResultMatchers.view().name("redirect:/providers/listAvailable"));
	}

	@WithMockUser(value = "pepito", authorities = {
		"manager"
	})
	@Test
	void testAcceptVet() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/providers/addProvider/{providerId}", ProviderControllerTests.TEST_PROVIDER1_ID)).andExpect(MockMvcResultMatchers.status().isFound())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/providers/listAvailable"));
	}

}
