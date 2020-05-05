
package org.springframework.samples.petclinic.e2e;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
public class OrderControllerE2ETests {

	private static final int	TEST_PROVIDER_ID	= 1;

	private static final int	TEST_ORDER_ID		= 1;

	@Autowired
	private MockMvc				mockMvc;


	@WithMockUser(value = "manager1", authorities = {
		"manager"
	})
	@Test
	void testInitCreationForm() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/orders/new/{providerId}", TEST_PROVIDER_ID))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeExists("products"))
			.andExpect(MockMvcResultMatchers.view().name("/orders/createOrUpdateOrderForm"));
	}

	@WithMockUser(value = "manager1", authorities = {
		"manager"
	})
	@Test
	void testProcessCreationFormPositive() throws Exception {
		mockMvc
			.perform(MockMvcRequestBuilders.post("/orders/save/{providerId}", TEST_PROVIDER_ID)
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("productIds", "1").param("productIds", "2")
				.param("amountNumber", "3").param("amountNumber", "4"))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/orders/list"));
	}

	@WithMockUser(value = "manager1", authorities = {
		"manager"
	})
	@Test
	void testProcessCreationFormNegative() throws Exception {
		mockMvc
			.perform(MockMvcRequestBuilders.post("/orders/save/{providerId}", TEST_PROVIDER_ID)
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("productIds", "").param("amountNumber", ""))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeExists("products"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("notProductsOrder"))
			.andExpect(MockMvcResultMatchers.view().name("/orders/createOrUpdateOrderForm"));
	}

	@WithMockUser(value = "manager1", authorities = {
		"manager"
	})
	@Test
	void testShowOrderPositive() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/orders/{orderId}", TEST_ORDER_ID))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeExists("order"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("productsOrder"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("provider"))
			.andExpect(MockMvcResultMatchers.view().name("orders/orderDetails"));
	}

	@WithMockUser(value = "manager2", authorities = {
		"manager"
	})
	@Test
	void testShowOrderNegative() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/orders/{orderId}", TEST_ORDER_ID))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));
	}

	@WithMockUser(value = "manager1", authorities = {
		"manager"
	})
	@Test
	void testShowOrderNotPresent() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/orders/{orderId}", 99))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(value = "manager1", authorities = {
		"manager"
	})
	@Test
	void testListAvailableProviders() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/orders/providers/listAvailable"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeExists("providers"))
			.andExpect(MockMvcResultMatchers.view().name("/orders/providers/providerList"));
	}

	@WithMockUser(value = "manager1", authorities = {
		"manager"
	})
	@Test
	void testListOrders() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/orders/list")).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeExists("orders"))
			.andExpect(MockMvcResultMatchers.view().name("/orders/orderList"));
	}
}