
package org.springframework.samples.petclinic.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.service.ManagerService;
import org.springframework.samples.petclinic.service.OrderService;
import org.springframework.samples.petclinic.service.ProductOrderService;
import org.springframework.samples.petclinic.service.ProductService;
import org.springframework.samples.petclinic.service.ProviderService;
import org.springframework.samples.petclinic.web.OrderController;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ui.ModelMap;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderControllerIntegrationTests {

	private static final int	TEST_PROVIDER_ID	= 1;
	
	private static final int	TEST_ORDER_ID	= 1;

	@Autowired
	private OrderController		orderController;


	@Test
	public void TestInitCreationForm() {
		ModelMap model = new ModelMap();
		String view = orderController.initCreationForm(TEST_PROVIDER_ID, model);

		assertEquals(view, "/orders/createOrUpdateOrderForm");
		assertNotNull(model.get("products"));
	}

	@WithMockUser(username="manager1",authorities= {"manager"})
	@Test
	public void TestProcessCreationFormPositive() {
		String[] productIds = {"1","2"};
		String[] amountNumber = {"3","5"};

		ModelMap model = new ModelMap();
		String view = orderController.processCreationForm(TEST_PROVIDER_ID, model, productIds, amountNumber);

		assertEquals(view, "redirect:/orders/list");
	}
	
	@WithMockUser(username="manager1",authorities= {"manager"})
	@Test
	public void TestProcessCreationFormNegative() {
		String[] productIds = {};
		String[] amountNumber = {};

		ModelMap model = new ModelMap();
		String view = orderController.processCreationForm(TEST_PROVIDER_ID, model, productIds, amountNumber);

		assertEquals(view, "/orders/createOrUpdateOrderForm");
		assertNotNull(model.get("products"));
		assertNotNull(model.get("notProductsOrder"));
	}
	
	@WithMockUser(username="manager1",authorities= {"manager"})
	@Test
	public void TestShowOrderPositive() {
		ModelMap model = new ModelMap();
		String view = orderController.showOrder(TEST_ORDER_ID, model);

		assertEquals(view, "orders/orderDetails");
	}
	
	@WithMockUser(username="manager2",authorities= {"manager"})
	@Test
	public void TestShowOrderNotAuthorizated() {
		ModelMap model = new ModelMap();
		String view = orderController.showOrder(TEST_ORDER_ID, model);

		assertEquals(view, "redirect:/oups");
	}
	
	@WithMockUser(username="manager1",authorities= {"manager"})
	@Test
	public void TestListAvailableProviders() {
		ModelMap model = new ModelMap();
		String view = orderController.listAvailableProviders(model);

		assertEquals(view, "/orders/providers/providerList");
		assertNotNull(model.get("providers"));
	}
	
	@WithMockUser(username="manager1",authorities= {"manager"})
	@Test
	public void TestListOrders() {
		ModelMap model = new ModelMap();
		String view = orderController.listOrders(model);

		assertEquals(view, "/orders/orderList");
		assertNotNull(model.get("orders"));
	}
	
}
