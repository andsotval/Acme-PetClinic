
package org.springframework.samples.petclinic.web;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

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
import org.springframework.samples.petclinic.model.Product;
import org.springframework.samples.petclinic.model.Provider;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.ProductService;
import org.springframework.samples.petclinic.service.ProviderService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ActiveProfiles("hsqldb")
@WebMvcTest(controllers = ProductController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
class ProductControllerTests {

	private static final int	TEST_PROVIDER_1	= 1;
	private static final int	TEST_PROVIDER_2	= 2;

	private static final int	TEST_MANAGER_1	= 1;

	private static final int	TEST_PRODUCT_1	= 1;
	private static final int	TEST_PRODUCT_2	= 2;
	private static final int	TEST_PRODUCT_3	= 2;
	private static final int	TEST_PRODUCT_99	= 99;

	@Autowired
	private MockMvc				mockMvc;

	@MockBean
	private ProductService		productService;

	@MockBean
	private ProviderService		providerService;

	@MockBean
	private AuthoritiesService	authoritiesService;


	@BeforeEach
	void setup() {
		LocalDate actualDate = LocalDate.now();

		User user = new User();
		user.setEnabled(true);
		user.setUsername("manager");
		user.setPassword("manager");

		Authorities authorityManager = new Authorities();
		authorityManager.setAuthority("manager");
		authorityManager.setUsername("manager");

		Manager manager = new Manager();
		manager.setUser(user);
		manager.setId(TEST_MANAGER_1);
		manager.setFirstName("manager");
		manager.setLastName("Leary");
		manager.setAddress("110 W. Liberty St.");
		manager.setCity("Madison");
		manager.setTelephone("6085551023");

		User userProvider = new User();
		userProvider.setEnabled(true);
		userProvider.setUsername("provider");
		userProvider.setPassword("provider");

		Authorities authority = new Authorities();
		authority.setAuthority("provider");
		authority.setUsername("provider");

		Provider provider = new Provider();
		provider.setUser(user);
		provider.setId(TEST_PROVIDER_1);
		provider.setFirstName("provider");
		provider.setLastName("Leary");
		provider.setAddress("110 W. Liberty St.");
		provider.setCity("Madison");
		provider.setTelephone("6085551023");
		provider.setManager(manager);

		User userProvider2 = new User();
		userProvider2.setEnabled(true);
		userProvider2.setUsername("provider");
		userProvider2.setPassword("provider");

		Authorities authorityProvider2 = new Authorities();
		authorityProvider2.setAuthority("provider");
		authorityProvider2.setUsername("provider");

		Provider provider2 = new Provider();
		provider2.setUser(user);
		provider2.setId(TEST_PROVIDER_2);
		provider2.setFirstName("provider2");
		provider2.setLastName("Leary");
		provider2.setAddress("110 W. Liberty St.");
		provider2.setCity("Madison");
		provider2.setTelephone("6085551023");
		provider2.setManager(manager);

		Product product1 = new Product();
		product1.setAvailable(true);
		product1.setId(TEST_PRODUCT_1);
		product1.setName("product1");
		product1.setPrice(1.);
		product1.setTax(1.);
		product1.setProvider(provider);

		Product product2 = new Product();
		product2.setAvailable(true);
		product2.setId(1);
		product2.setName("product2");
		product2.setPrice(1.);
		product2.setTax(1.);
		product2.setProvider(provider);

		Product product3 = new Product();
		product3.setAvailable(true);
		product3.setId(1);
		product3.setName("product3");
		product3.setPrice(1.);
		product3.setTax(1.);
		product3.setProvider(provider2);

		Collection<Product> products = new ArrayList<Product>();
		products.add(product1);
		products.add(product2);

		Optional<Product> optionalProduct = Optional.of(product1);
		Optional<Product> optionalProduct3 = Optional.of(product3);

		BDDMockito.given(productService.findAllProductsByProvider(TEST_PROVIDER_1)).willReturn(products);
		BDDMockito.given(productService.findEntityById(TEST_PRODUCT_1)).willReturn(optionalProduct);
		BDDMockito.given(productService.findEntityById(TEST_PRODUCT_3)).willReturn(optionalProduct3);

		Optional<Provider> optionalProvider = Optional.of(provider);
		Optional<Provider> optionalProvider2 = Optional.of(provider2);

		BDDMockito.given(providerService.findEntityById(TEST_PROVIDER_1)).willReturn(optionalProvider);
		BDDMockito.given(providerService.findEntityById(TEST_PROVIDER_2)).willReturn(optionalProvider2);
		BDDMockito.given(providerService.findPersonByUsername("provider")).willReturn(provider);
		BDDMockito.given(providerService.findPersonByUsername("provider2")).willReturn(provider2);

	}

	@WithMockUser(value = "provider")
	@Test
	void testListMyProducts() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/product/myProductsList")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("products"))
			.andExpect(MockMvcResultMatchers.view().name("products/listAll"));
	}

	@WithMockUser(value = "userNotAuthorized")
	@Test
	void testListMyProductsNegativeUserNotAuthorized() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/product/myProductsList")).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));

	}

	@WithMockUser(value = "provider")
	@Test
	void testShowCreateForm() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/product/initNewProduct")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("product"))
			.andExpect(MockMvcResultMatchers.view().name("products/createOrUpdateProductForm"));
	}

	@WithMockUser(value = "userNotAuthorized")
	@Test
	void testShowCreateFormNegativeUserNotAuthorized() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/product/initNewProduct")).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));
	}

	@WithMockUser(value = "provider")
	@Test
	void testSaveProduct() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/product/save").with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "Product name").param("tax", "1.").param("price", "1.").param("available", "true"))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/product/myProductsList"));
	}

	@WithMockUser(value = "provider")
	@Test
	void testSaveProductNegativeNullAttribute() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/product/save").with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "").param("tax", "1.").param("price", "1.").param("available", "true")).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("products/createOrUpdateProductForm"));
	}

	@WithMockUser(value = "UserNotauthorized")
	@Test
	void testSaveProductNegativeUserNotAuthorized() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/product/save").with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "Product name").param("tax", "1.").param("price", "1.").param("available", "true"))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));
	}

	@WithMockUser(value = "provider")
	@Test
	void testShowUpdateForm() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/product/show/{productId}", TEST_PRODUCT_1)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("product"))
			.andExpect(MockMvcResultMatchers.view().name("products/createOrUpdateProductForm"));
	}

	@WithMockUser(value = "provider")
	@Test
	void testShowUpdateFormNegativeNotExistingProduct() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/product/show/{productId}", TEST_PRODUCT_99)).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));
	}

	@WithMockUser(value = "provider")
	@Test
	void testShowUpdateFormNegativeTryAccessToOtherProviderProduct() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/product/show/{productId}", TEST_PRODUCT_3)).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));
	}

	@WithMockUser(value = "provider")
	@Test
	void testUpdateProduct() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/product/save/{productId}", TEST_PRODUCT_1).with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "Product name").param("tax", "1.").param("price", "1.").param("available", "true"))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/product/myProductsList"));
	}

	@WithMockUser(value = "provider")
	@Test
	void testUpdateProductNegativeTryAccessToOtherProviderProduct() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/product/save/{productId}", TEST_PRODUCT_3).with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "Product name").param("tax", "1.").param("price", "1.").param("available", "true"))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));
	}

	@WithMockUser(value = "provider")
	@Test
	void testUpdateProductNegativeNotExistingProduct() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/product/save/{productId}", TEST_PRODUCT_99).with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "Product name").param("tax", "1.").param("price", "1.").param("available", "true"))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));
	}

	@WithMockUser(value = "provider")
	@Test
	void testChangeStateProduct() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/product/desactivateProduct/{productId}", TEST_PRODUCT_1).with(SecurityMockMvcRequestPostProcessors.csrf())).andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/product/myProductsList"));
	}

	@WithMockUser(value = "provider")
	@Test
	void testChangeStateProductNegativeTryChangeToOtherProviderProduct() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/product/desactivateProduct/{productId}", TEST_PRODUCT_3).with(SecurityMockMvcRequestPostProcessors.csrf())).andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));
	}

}
