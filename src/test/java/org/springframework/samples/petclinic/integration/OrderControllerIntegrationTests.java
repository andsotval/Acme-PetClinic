
package org.springframework.samples.petclinic.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.model.Manager;
import org.springframework.samples.petclinic.model.Order;
import org.springframework.samples.petclinic.model.Product;
import org.springframework.samples.petclinic.model.ProductOrder;
import org.springframework.samples.petclinic.model.Provider;
import org.springframework.samples.petclinic.service.ManagerService;
import org.springframework.samples.petclinic.service.OrderService;
import org.springframework.samples.petclinic.service.ProductOrderService;
import org.springframework.samples.petclinic.service.ProductService;
import org.springframework.samples.petclinic.service.ProviderService;
import org.springframework.samples.petclinic.util.SessionUtils;
import org.springframework.samples.petclinic.web.OrderController;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ui.ModelMap;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderControllerIntegrationTests {

	private static final int	TEST_PROVIDER_ID	= 1;

	private static final int	TEST_ORDER_ID		= 1;

	@Autowired
	private OrderController		orderController;

	@Autowired
	private OrderService		orderService;

	@Autowired
	private ProductService		productService;

	@Autowired
	private ManagerService		managerService;

	@Autowired
	private ProductOrderService	productOrderService;

	@Autowired
	private ProviderService		providerService;


	@SuppressWarnings("unchecked")
	@Test
	public void TestInitCreationForm() {
		ModelMap model = new ModelMap();
		String view = orderController.initCreationForm(TEST_PROVIDER_ID, model);

		assertEquals(view, "/orders/createOrUpdateOrderForm");

		Collection<Product> list = productService.findProductsAvailableByProviderId(TEST_PROVIDER_ID);
		assertNotNull(model.get("products"));
		assertEquals(((Collection<Product>) model.get("products")).size(), list.size());
		((Collection<Product>) model.get("products")).forEach(product -> {
			list.contains(product);
		});

		assertNotNull(model.get("notProductsOrder"));
		assertEquals(model.get("notProductsOrder"), "");
	}

	@WithMockUser(username = "manager1", authorities = {
		"manager"
	})
	@Test
	public void TestProcessCreationFormPositive() {
		String[] productIds = {
			"1", "2"
		};
		String[] amountNumber = {
			"3", "5"
		};

		ModelMap model = new ModelMap();
		String view = orderController.processCreationForm(TEST_PROVIDER_ID, model, productIds, amountNumber);

		Manager manager = managerService.findPersonByUsername(SessionUtils.obtainUserInSession().getUsername());
		Collection<Order> orders = orderService.findAllOrdersByManagerId(manager.getId());
		List<Order> order = orders.stream().filter(o -> o.getDate().equals(LocalDate.now())
			&& o.getIsAccepted().equals(false) && o.getManager().getId().equals(manager.getId()))
			.collect(Collectors.toList());

		Collection<ProductOrder> productOrders = productOrderService.findProductOrderByOrder(order.get(0).getId());
		productOrders.forEach(po -> {
			assertTrue(po.getProduct().getId().equals(1) || po.getProduct().getId().equals(2));
			assertTrue(po.getAmount().equals(3) || po.getAmount().equals(5));
		});

		assertEquals(view, "redirect:/orders/list");

	}

	@SuppressWarnings("unchecked")
	@WithMockUser(username = "manager1", authorities = {
		"manager"
	})
	@Test
	public void TestProcessCreationFormNegative() {
		String[] productIds = {};
		String[] amountNumber = {};

		ModelMap model = new ModelMap();
		String view = orderController.processCreationForm(TEST_PROVIDER_ID, model, productIds, amountNumber);

		assertEquals(view, "/orders/createOrUpdateOrderForm");

		Collection<Product> list = productService.findProductsAvailableByProviderId(TEST_PROVIDER_ID);
		assertNotNull(model.get("products"));
		assertEquals(((Collection<Product>) model.get("products")).size(), list.size());
		((Collection<Product>) model.get("products")).forEach(product -> {
			list.contains(product);
		});

		assertNotNull(model.get("notProductsOrder"));
		assertEquals(model.get("notProductsOrder"), "You have not added any quantity for the products");
	}

	@SuppressWarnings("unchecked")
	@WithMockUser(username = "manager1", authorities = {
		"manager"
	})
	@Test
	public void TestShowOrderPositive() {
		ModelMap model = new ModelMap();
		String view = orderController.showOrder(TEST_ORDER_ID, model);

		assertEquals(view, "orders/orderDetails");

		Order order = orderService.findEntityById(TEST_ORDER_ID).get();
		assertNotNull(model.get("order"));
		assertEquals(model.get("order").toString(), order.toString());

		Collection<ProductOrder> list = productOrderService.findProductOrderByOrder(TEST_ORDER_ID);
		assertNotNull(model.get("productsOrder"));
		assertEquals(((Collection<ProductOrder>) model.get("productsOrder")).size(), list.size());
		((Collection<ProductOrder>) model.get("productsOrder")).forEach(productOrder -> {
			list.contains(productOrder);
		});

		assertNotNull(model.get("provider"));
	}

	@WithMockUser(username = "manager2", authorities = {
		"manager"
	})
	@Test
	public void TestShowOrderNotAuthorizated() {
		ModelMap model = new ModelMap();
		String view = orderController.showOrder(TEST_ORDER_ID, model);

		assertEquals(view, "redirect:/oups");

		assertNotNull(model.get("message"));
		assertEquals(model.get("message"), "Se esta intentando acceder a un pedido que no pertenece al manager actual");
	}

	@SuppressWarnings("unchecked")
	@WithMockUser(username = "manager1", authorities = {
		"manager"
	})
	@Test
	public void TestListAvailableProviders() {
		ModelMap model = new ModelMap();
		String view = orderController.listAvailableProviders(model);

		assertEquals(view, "/orders/providers/providerList");

		Manager manager = managerService.findPersonByUsername(SessionUtils.obtainUserInSession().getUsername());
		Collection<Provider> list = providerService.findProvidersByManagerId(manager.getId());
		assertNotNull(model.get("providers"));
		assertEquals(((Collection<Provider>) model.get("providers")).size(), list.size());
		((Collection<Provider>) model.get("providers")).forEach(provider -> {
			list.contains(provider);
		});
	}

	@SuppressWarnings("unchecked")
	@WithMockUser(username = "manager1", authorities = {
		"manager"
	})
	@Test
	public void TestListOrders() {
		ModelMap model = new ModelMap();
		String view = orderController.listOrders(model);

		assertEquals(view, "/orders/orderList");

		Manager manager = managerService.findPersonByUsername(SessionUtils.obtainUserInSession().getUsername());
		Collection<Order> list = orderService.findAllOrdersByManagerId(manager.getId());
		assertNotNull(model.get("orders"));
		assertEquals(((Collection<Order>) model.get("orders")).size(), list.size());
		((Collection<Order>) model.get("orders")).forEach(order -> {
			list.contains(order);
		});
	}

}
