
package org.springframework.samples.petclinic.e2e;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
@DirtiesContext(classMode = ClassMode.BEFORE_CLASS)
class ProviderControllerE2ETests {

	private static final int	TEST_PROVIDER1_ID	= 1;
	@Autowired
	private MockMvc				mockMvc;


	@WithMockUser(value = "manager1", authorities = {
		"manager"
	})
	@Test
	void testListAvailable() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/providers/listAvailable"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("providers/providersList"));
	}

	@WithMockUser(username = "provider1")
	@Test
	void TestListAvailableAsRoleNotAuthorizated() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/providers/listAvailable")).andExpect(status().isForbidden());
	}

	@WithMockUser(value = "manager99", authorities = {
		"manager"
	})
	@Test
	void testListAvailableAsManagerNotPresent() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/providers/listAvailable"))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));
	}

	@WithMockUser(value = "manager99", authorities = {
		"manager"
	})
	@Test
	void TestInitAddProviderToManagerAsManagerNotPresent() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/providers/addProvider/{providerId}", TEST_PROVIDER1_ID))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));
	}

	@WithMockUser(value = "manager1", authorities = {
		"manager"
	})
	@Test
	void testInitAddProviderToManager() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/providers/addProvider/{providerId}", TEST_PROVIDER1_ID))
			.andExpect(status().isOk()).andExpect(MockMvcResultMatchers.view().name("providers/providersList"));
	}

	@WithMockUser(value = "manager1", authorities = {
		"manager"
	})
	@Test
	void TestInitAddProviderToManagerNegativeNotExistingProvider() throws Exception {
		mockMvc.perform(get("/providers/addProvider/{providerId}", 99)).andExpect(status().isOk())
			.andExpect(view().name("providers/providersList"));
	}

	@WithMockUser(value = "manager1", authorities = {
		"manager"
	})
	@Test
	void TestInitAddProviderToManagerNegativeAlreadyAddedProvider() throws Exception {
		mockMvc.perform(get("/providers/addProvider/{providerId}", TEST_PROVIDER1_ID)).andExpect(status().isOk())
			.andExpect(view().name("providers/providersList"));
	}

	@WithMockUser(value = "manager1", authorities = {
		"manager"
	})
	@Test
	void testListProductsByProvider() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/providers/listProductsByProvider/{providerId}", TEST_PROVIDER1_ID))
			.andExpect(status().isOk()).andExpect(MockMvcResultMatchers.view().name("providers/providersList"));
	}

	@WithMockUser(value = "manager99", authorities = {
		"manager"
	})
	@Test
	void TestListProductsByProviderAsManagerNotPresent() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/providers/listProductsByProvider/{providerId}", TEST_PROVIDER1_ID))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));
	}

	@WithMockUser(value = "provider1", authorities = {
		"provider"
	})
	@Test
	void TestListProductsByProviderAsRoleNotAuthorizated() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/providers/listProductsByProvider/{providerId}", TEST_PROVIDER1_ID))
			.andExpect(status().isForbidden());
	}

	@WithMockUser(value = "manager1", authorities = {
		"manager"
	})
	@Test
	void TestListProductsByProviderNotExistingProvider() throws Exception {
		mockMvc.perform(get("/providers/listProductsByProvider/{providerId}", 99)).andExpect(status().isOk())
			.andExpect(view().name("providers/providersList"));
	}
}
