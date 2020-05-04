
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
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
class ProviderControllerE2ETests {

	//Provider with no Manager associated
	private static final int	TEST_PROVIDER1_ID	= 1;
	//Provider with a Manager associated
	private static final int	TEST_PROVIDER2_ID	= 2;

	private static final int	TEST_MANAGER_ID		= 3;

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

	@WithMockUser(value = "manager1", authorities = {
		"manager"
	})
	@Test
	void testInitAddProviderToManager() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/providers/addProvider/{providerId}", TEST_PROVIDER1_ID))
			.andExpect(status().isFound())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/providers/listAvailable"));
	}

	@WithMockUser(value = "manager1", authorities = {
		"manager"
	})
	@Test
	void testInitAddProviderToManagerNegative() throws Exception {
		mockMvc.perform(get("/providers/addProvider/{providerId}", TEST_PROVIDER1_ID))
			.andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/providers/listAvailable"));
	}

	@WithMockUser(value = "manager1", authorities = {
		"manager"
	})
	@Test
	void testListProductsByProvider() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/providers/listProductsByProvider/{providerId}", TEST_PROVIDER1_ID))
			.andExpect(status().isOk()).andExpect(MockMvcResultMatchers.view().name("providers/providerProductsList"));
	}

	@WithMockUser(value = "manager1", authorities = {
		"manager"
	})
	@Test
	void testListProductsByProviderNegative() throws Exception {
		mockMvc.perform(get("/providers/listProductsByProvider/{providerId}", 99)).andExpect(status().isOk())
			.andExpect(view().name("exception"));
	}
}
