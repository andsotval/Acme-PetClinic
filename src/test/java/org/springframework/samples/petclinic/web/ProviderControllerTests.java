/**
 * DP2 - Grupo 8
 * LAB F1.33
 * Date: 17-may-2020
 */

package org.springframework.samples.petclinic.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

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
import org.springframework.samples.petclinic.service.ProductService;
import org.springframework.samples.petclinic.service.ProviderService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ActiveProfiles("hsqldb")
@WebMvcTest(controllers = ProviderController.class,
	excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
	excludeAutoConfiguration = SecurityConfiguration.class)
class ProviderControllerTests {

	//Provider with no Manager associated
	private static final int	TEST_PROVIDER1_ID	= 1;
	//Provider with a Manager associated
	private static final int	TEST_PROVIDER2_ID	= 2;
	//Provider that doesn't exists in the system
	private static final int	TEST_PROVIDER99_ID	= 999;

	private static final int	TEST_MANAGER_ID		= 3;

	@MockBean
	private ProviderService		providerService;

	@MockBean
	private ManagerService		managerService;

	@MockBean
	private ProductService		productService;

	@Autowired
	private MockMvc				mockMvc;


	@BeforeEach
	void setup() {

		User user = new User();
		user.setEnabled(true);
		user.setUsername("manager1");
		user.setPassword("manager1");

		Authorities authority = new Authorities();
		authority.setAuthority("manager");
		authority.setUsername("manager1");
		Manager pepe = new Manager();
		pepe.setUser(user);
		pepe.setId(ProviderControllerTests.TEST_MANAGER_ID);
		pepe.setFirstName("Pepe");
		pepe.setLastName("Leary");
		pepe.setAddress("110 W. Liberty St.");
		pepe.setCity("Madison");
		pepe.setTelephone("6085551023");

		User user2 = new User();
		user2.setEnabled(true);
		user2.setUsername("manager2");
		user2.setPassword("manager2");

		Authorities authority2 = new Authorities();
		authority2.setAuthority("manager");
		authority2.setUsername("manager2");
		Manager pepe2 = new Manager();
		pepe2.setUser(user);
		pepe2.setId(ProviderControllerTests.TEST_MANAGER_ID);
		pepe2.setFirstName("Pepe2");
		pepe2.setLastName("Leary");
		pepe2.setAddress("110 W. Liberty St.");
		pepe2.setCity("Madison");
		pepe2.setTelephone("6085551023");

		BDDMockito.given(managerService.findPersonByUsername("manager1")).willReturn(pepe);
		BDDMockito.given(managerService.findPersonByUsername("manager2")).willReturn(pepe2);

		Provider james = new Provider();
		james.setId(ProviderControllerTests.TEST_PROVIDER1_ID);
		james.setFirstName("James");
		james.setLastName("Carter");
		james.setAddress("110 W. Liberty St.");
		james.setCity("Madison");
		james.setTelephone("6085551023");
		james.setManager(pepe);
		Optional<Provider> opt = Optional.of(james);
		BDDMockito.given(providerService.findEntityById(ProviderControllerTests.TEST_PROVIDER1_ID)).willReturn(opt);

		Provider helen = new Provider();
		helen.setFirstName("Helen");
		helen.setLastName("Leary");
		helen.setAddress("110 W. Liberty St.");
		helen.setCity("Madison");
		helen.setTelephone("6085551023");
		helen.setId(ProviderControllerTests.TEST_PROVIDER2_ID);

		Optional<Provider> opt2 = Optional.of(helen);
		BDDMockito.given(providerService.findEntityById(ProviderControllerTests.TEST_PROVIDER2_ID)).willReturn(opt2);

		BDDMockito.given(providerService.findAvailableProviders()).willReturn(Lists.newArrayList(helen));

	}

	@WithMockUser(value = "manager1", authorities = {
		"manager"
	})
	@Test
	void testListAvailable() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/providers/listAvailable"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("providers/providersList"));
	}

	@WithMockUser(value = "provider")
	@Test
	void testListAvailableNegativeNotAuthorized() throws Exception {
		mockMvc.perform(get("/providers/listAvailable")).andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/oups"));
	}

	@WithMockUser(value = "manager1")
	@Test
	void testInitAddProviderToManager() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/providers/addProvider/{providerId}", TEST_PROVIDER2_ID))
			.andExpect(status().isOk())
			.andExpect(MockMvcResultMatchers.model().attribute("message", "Provider succesfully added"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("availableProviders"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("addedProviders"))
			.andExpect(MockMvcResultMatchers.view().name("providers/providersList"));
	}

	@WithMockUser(value = "provider")
	@Test
	void testInitAddProviderToManagerNegativeNotAuthorized() throws Exception {
		mockMvc.perform(get("/providers/addProvider/{providerId}", TEST_PROVIDER1_ID))
			.andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/oups"));
	}

	@WithMockUser(value = "manager1")
	@Test
	void testInitAddProviderToManagerNegativeNotExistingProvider() throws Exception {
		mockMvc.perform(get("/providers/addProvider/{providerId}", TEST_PROVIDER99_ID)).andExpect(status().isOk())
			.andExpect(MockMvcResultMatchers.model().attribute("message",
				"We are very sorry, but the selected provider does not exist"))
			.andExpect(view().name("providers/providersList"));
	}

	@WithMockUser(value = "manager1")
	@Test
	void testInitAddProviderToManagerNegativeAlreadyAddedProvider() throws Exception {
		mockMvc.perform(get("/providers/addProvider/{providerId}", TEST_PROVIDER1_ID)).andExpect(status().isOk())
			.andExpect(MockMvcResultMatchers.model().attribute("message",
				"We are very sorry, it is not possible to add a Provider that is already assigned to another Manager"))
			.andExpect(view().name("providers/providersList"));
	}

	@WithMockUser(value = "manager1")
	@Test
	void testListProductsByProvider() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/providers/listProductsByProvider/{providerId}", TEST_PROVIDER2_ID))
			.andExpect(status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("products"))
			.andExpect(MockMvcResultMatchers.view().name("providers/providerProductsList"));
	}

	@WithMockUser(value = "FalseManager")
	@Test
	void testListProductsByProviderNegativeNotExistingManager() throws Exception {
		mockMvc.perform(get("/providers/listProductsByProvider/{providerId}", TEST_PROVIDER1_ID))
			.andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/oups"));
	}

	@WithMockUser(value = "manager1")
	@Test
	void testListProductsByProviderNegativeNotExistingProvider() throws Exception {
		mockMvc.perform(get("/providers/listProductsByProvider/{providerId}", TEST_PROVIDER99_ID))
			.andExpect(MockMvcResultMatchers.model().attribute("message",
				"We are very sorry, but the selected provider does not exist"))
			.andExpect(status().isOk()).andExpect(view().name("providers/providersList"));
	}

	@WithMockUser(value = "manager2")
	@Test
	void testListProductsByProviderNegativeListOtherManagersProviders() throws Exception {
		mockMvc.perform(get("/providers/listProductsByProvider/{providerId}", TEST_PROVIDER1_ID))
			.andExpect(status().isOk())
			.andExpect(MockMvcResultMatchers.model().attribute("message",
				"We are very sorry, but you cannot see the products of a supplier assigned to another manager"))
			.andExpect(view().name("providers/providersList"));
	}

}
