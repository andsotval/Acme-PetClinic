
package org.springframework.samples.petclinic.e2e;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.ProductService;
import org.springframework.samples.petclinic.service.ProviderService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
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
class ProductControllerE2ETests {

	private static final int	TEST_PROVIDER_1	= 1;
	private static final int	TEST_PROVIDER_2	= 2;

	private static final int	TEST_PRODUCT	= 4;
	private static final int	TEST_PRODUCT_3	= 2;
	private static final int	TEST_PRODUCT_99	= 99;

	@Autowired
	private MockMvc				mockMvc;

	@Autowired
	private ProductService		productService;

	@Autowired
	private ProviderService		providerService;

	@Autowired
	private AuthoritiesService	authoritiesService;


	@WithMockUser(value = "provider1", authorities = {
		"provider"
	})
	@Test
	void testListMyProducts() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/product/myProductsList")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("products"))
			.andExpect(MockMvcResultMatchers.view().name("products/listAll"));
	}

	@WithMockUser(value = "userNotAuthorized", authorities = {
		"provider"
	})
	@Test
	void testListMyProductsNegativeUserNotAuthorized() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/product/myProductsList")).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));

	}

	@WithMockUser(value = "provider1", authorities = {
		"provider"
	})
	@Test
	void testShowCreateForm() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/product/initNewProduct")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("product"))
			.andExpect(MockMvcResultMatchers.view().name("products/createOrUpdateProductForm"));
	}

	@WithMockUser(value = "userNotAuthorized", authorities = {
		"provider"
	})
	@Test
	void testShowCreateFormNegativeUserNotAuthorized() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/product/initNewProduct")).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));
	}

	@WithMockUser(value = "provider1", authorities = {
		"provider"
	})
	@Test
	void testSaveProduct() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/product/save").with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "Product name").param("tax", "1.").param("price", "1.").param("available", "true"))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/product/myProductsList"));
	}

	@WithMockUser(value = "provider1", authorities = {
		"provider"
	})
	@Test
	void testSaveProductNegativeNullAttribute() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/product/save").with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "").param("tax", "1.").param("price", "1.").param("available", "true")).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("products/createOrUpdateProductForm"));
	}

	@WithMockUser(value = "UserNotauthorized", authorities = {
		"provider"
	})
	@Test
	void testSaveProductNegativeUserNotAuthorized() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/product/save").with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "Product name").param("tax", "1.").param("price", "1.").param("available", "true"))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));
	}

	@WithMockUser(value = "provider1", authorities = {
		"provider"
	})
	@Test
	void testShowUpdateForm() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/product/show/{productId}", TEST_PRODUCT)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("product"))
			.andExpect(MockMvcResultMatchers.view().name("products/createOrUpdateProductForm"));
	}

	@WithMockUser(value = "provider1", authorities = {
		"provider"
	})
	@Test
	void testShowUpdateFormNegativeNotExistingProduct() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/product/show/{productId}", TEST_PRODUCT_99)).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));
	}

	@WithMockUser(value = "provider1", authorities = {
		"provider"
	})
	@Test
	void testShowUpdateFormNegativeTryAccessToOtherProviderProduct() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/product/show/{productId}", TEST_PRODUCT_3)).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));
	}

	@WithMockUser(value = "provider1", authorities = {
		"provider"
	})
	@Test
	void testUpdateProduct() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/product/save/{productId}", TEST_PRODUCT).with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "Product name").param("tax", "1.").param("price", "1.").param("available", "true"))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/product/myProductsList"));
	}

	@WithMockUser(value = "provider1", authorities = {
		"provider"
	})
	@Test
	void testUpdateProductNegativeTryAccessToOtherProviderProduct() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/product/save/{productId}", TEST_PRODUCT_3).with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "Product name").param("tax", "1.").param("price", "1.").param("available", "true"))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));
	}

	@WithMockUser(value = "provider1", authorities = {
		"provider"
	})
	@Test
	void testUpdateProductNegativeNotExistingProduct() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/product/save/{productId}", TEST_PRODUCT_99).with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "Product name").param("tax", "1.").param("price", "1.").param("available", "true"))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));
	}

	@WithMockUser(value = "provider1", authorities = {
		"provider"
	})
	@Test
	void testChangeStateProduct() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/product/desactivateProduct/{productId}", TEST_PRODUCT).with(SecurityMockMvcRequestPostProcessors.csrf())).andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/product/myProductsList"));
	}

	@WithMockUser(value = "provider1", authorities = {
		"provider"
	})
	@Test
	void testChangeStateProductNegativeTryChangeToOtherProviderProduct() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/product/desactivateProduct/{productId}", TEST_PRODUCT_3).with(SecurityMockMvcRequestPostProcessors.csrf())).andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));
	}

}
