
package org.springframework.samples.petclinic.web;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

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
import org.springframework.samples.petclinic.model.Order;
import org.springframework.samples.petclinic.model.Product;
import org.springframework.samples.petclinic.model.ProductOrder;
import org.springframework.samples.petclinic.model.Provider;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.ManagerService;
import org.springframework.samples.petclinic.service.OrderService;
import org.springframework.samples.petclinic.service.ProductOrderService;
import org.springframework.samples.petclinic.service.ProductService;
import org.springframework.samples.petclinic.service.ProviderService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers = OrderController.class,
	excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
	excludeAutoConfiguration = SecurityConfiguration.class)
public class OrderControllerTests {

	private static final int	TEST_MANAGER_ID		= 1;

	private static final int	TEST_PROVIDER_ID	= 1;

	private static final int	TEST_ORDER_ID		= 1;

	private static final int	TEST_PRODUCT_ID_1	= 1;

	private static final int	TEST_PRODUCT_ID_2	= 2;

	private static Product		product1;
	private static Product		product2;

	private static Order		order;

	@Autowired
	private MockMvc				mockMvc;

	@MockBean
	private OrderService		orderService;

	@MockBean
	private ManagerService		managerService;

	@MockBean
	private ProductService		productService;

	@MockBean
	private ProviderService		providerService;

	@MockBean
	private ProductOrderService	productOrderService;


	@BeforeEach
	void setup() {

		// MANAGER 1
		User userManager1 = new User();
		userManager1.setEnabled(true);
		userManager1.setUsername("juan");
		userManager1.setPassword("juan");

		Authorities authorityManager1 = new Authorities();
		authorityManager1.setAuthority("manager");
		authorityManager1.setUsername("juan");
		Manager manager1 = new Manager();
		manager1.setId(TEST_MANAGER_ID);
		manager1.setUser(userManager1);
		manager1.setFirstName("Juan");
		manager1.setLastName("Leary");
		manager1.setAddress("110 W. Liberty St.");
		manager1.setCity("Madison");
		manager1.setTelephone("6085551023");

		BDDMockito.given(managerService.findPersonByUsername("juan")).willReturn(manager1);

		// MANAGER 2
		User userManager2 = new User();
		userManager2.setEnabled(true);
		userManager2.setUsername("fran");
		userManager2.setPassword("fran");

		Authorities authorityManager2 = new Authorities();
		authorityManager2.setAuthority("manager");
		authorityManager2.setUsername("fran");
		Manager manager2 = new Manager();
		manager2.setId(2);
		manager2.setUser(userManager2);
		manager2.setFirstName("fran");
		manager2.setLastName("Leary");
		manager2.setAddress("110 W. Liberty St.");
		manager2.setCity("Madison");
		manager2.setTelephone("6085551023");

		BDDMockito.given(managerService.findPersonByUsername("fran")).willReturn(manager2);

		User userProvider1 = new User();
		userProvider1.setEnabled(true);
		userProvider1.setUsername("pepito");
		userProvider1.setPassword("pepito");

		Authorities authorityProvider1 = new Authorities();
		authorityProvider1.setAuthority("provider");
		authorityProvider1.setUsername("pepito");
		Provider provider1 = new Provider();
		provider1.setUser(userProvider1);
		provider1.setId(TEST_PROVIDER_ID);
		provider1.setFirstName("Pepe");
		provider1.setLastName("Leary");
		provider1.setAddress("110 W. Liberty St.");
		provider1.setCity("Madison");
		provider1.setTelephone("6085551023");

		Optional<Provider> optionalProvider = Optional.of(provider1);
		BDDMockito.given(providerService.findEntityById(TEST_PROVIDER_ID)).willReturn(optionalProvider);

		User userProvider2 = new User();
		userProvider2.setEnabled(true);
		userProvider2.setUsername("pepito");
		userProvider2.setPassword("pepito");

		Authorities authorityProvider2 = new Authorities();
		authorityProvider2.setAuthority("provider");
		authorityProvider2.setUsername("pepito");
		Provider provider2 = new Provider();
		provider2.setUser(userProvider2);
		provider2.setId(TEST_PROVIDER_ID);
		provider2.setFirstName("Pepe");
		provider2.setLastName("Leary");
		provider2.setAddress("110 W. Liberty St.");
		provider2.setCity("Madison");
		provider2.setTelephone("6085551023");

		BDDMockito.given(providerService.findProvidersByManagerId(TEST_MANAGER_ID))
			.willReturn(Lists.newArrayList(provider1, provider2));

		Product product1 = new Product();
		product1.setId(TEST_PRODUCT_ID_1);
		product1.setAvailable(true);
		product1.setName("product 1");
		product1.setPrice(10.5);
		product1.setTax(21.0);
		product1.setProvider(provider1);

		OrderControllerTests.product1 = product1;

		Product product2 = new Product();
		product2.setId(TEST_PRODUCT_ID_2);
		product2.setAvailable(true);
		product2.setName("product 2");
		product2.setPrice(5.5);
		product1.setTax(21.0);
		product2.setProvider(provider1);

		OrderControllerTests.product2 = product2;

		BDDMockito.given(productService.findProductsAvailableByProviderId(TEST_PROVIDER_ID))
			.willReturn(Lists.newArrayList(product1, product2));

		Set<Product> setProducts = new HashSet<Product>();
		setProducts.add(product1);
		setProducts.add(product2);

		Order order = new Order();
		order.setId(TEST_ORDER_ID);
		order.setDate(LocalDate.now());
		order.setIsAccepted(false);
		order.setManager(manager1);
		//		order.setProduct(setProducts);

		OrderControllerTests.order = order;

		Optional<Order> optionalOrder = Optional.of(order);

		BDDMockito.given(orderService.findEntityById(TEST_ORDER_ID)).willReturn(optionalOrder);

		BDDMockito.given(productOrderService.findProviderByOrder(TEST_ORDER_ID)).willReturn(provider1);

		ProductOrder productOrder1 = new ProductOrder();
		productOrder1.setId(1);
		productOrder1.setProduct(product1);
		productOrder1.setName(product1.getName());
		productOrder1.setPrice(product1.getPrice());
		productOrder1.setTax(product1.getTax());
		productOrder1.setAmount(4);
		productOrder1.setOrder(order);

		BDDMockito.given(productOrderService.findProductOrderByOrder(TEST_ORDER_ID))
			.willReturn(Lists.newArrayList(productOrder1));

		BDDMockito.given(orderService.findAllOrdersByManagerId(TEST_ORDER_ID)).willReturn(Lists.newArrayList(order));

		BDDMockito.given(productService.findProductsByIds(Lists.newArrayList(product1.getId(), product2.getId())))
			.willReturn(Lists.newArrayList(product1, product2));
	}

	@WithMockUser(value = "juan")
	@Test
	void testInitCreationForm() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/orders/new/{providerId}", TEST_PROVIDER_ID))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeExists("products"))
			.andExpect(MockMvcResultMatchers.view().name("/orders/createOrUpdateOrderForm"));
	}

	@WithMockUser(value = "juan")
	@Test
	void testProcessCreationFormPositive() throws Exception {
		mockMvc
			.perform(MockMvcRequestBuilders.post("/orders/save/{providerId}", TEST_PROVIDER_ID)
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("productIds", "1").param("productIds", "2")
				.param("amountNumber", "3").param("amountNumber", "4"))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/orders/list"));
	}

	@WithMockUser(value = "juan")
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

	@WithMockUser(value = "juan")
	@Test
	void testShowOrderPositive() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/orders/{orderId}", TEST_ORDER_ID))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeExists("order"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("productsOrder"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("provider"))
			.andExpect(MockMvcResultMatchers.view().name("orders/orderDetails"));
	}

	@WithMockUser(value = "fran")
	@Test
	void testShowOrderNegative() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/orders/{orderId}", TEST_ORDER_ID))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));
	}

	@WithMockUser(value = "juan")
	@Test
	void testShowOrderNotPresent() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/orders/{orderId}", 99))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(value = "juan")
	@Test
	void testListAvailableProviders() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/orders/providers/listAvailable"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeExists("providers"))
			.andExpect(MockMvcResultMatchers.view().name("/orders/providers/providerList"));
	}

	@WithMockUser(value = "juan")
	@Test
	void testListOrders() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/orders/list")).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeExists("orders"))
			.andExpect(MockMvcResultMatchers.view().name("/orders/orderList"));
	}

}
