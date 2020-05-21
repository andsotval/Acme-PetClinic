/**
 * DP2 - Grupo 8
 * LAB F1.33
 * Date: 17-may-2020
 */

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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ui.ModelMap;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = ClassMode.BEFORE_CLASS)
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
	@WithMockUser(username = "manager1", authorities = {
		"manager"
	})
	@Test
	public void TestInitCreationFormPositive() {
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

	@WithMockUser(username = "manager99", authorities = {
		"manager"
	})
	@Test
	public void TestInitCreationFormAsManagerNotExisting() {
		ModelMap model = new ModelMap();
		String view = orderController.initCreationForm(TEST_PROVIDER_ID, model);

		assertEquals(view, "redirect:/oups");
	}

	@WithMockUser(username = "manager2", authorities = {
		"manager"
	})
	@Test
	public void TestInitCreationFormAsManagerNotAuthorized() {
		ModelMap model = new ModelMap();
		String view = orderController.initCreationForm(TEST_PROVIDER_ID, model);

		assertEquals(view, "redirect:/oups");
	}

	@WithMockUser(username = "manager1", authorities = {
		"manager"
	})
	@Test
	public void TestInitCreationFormAsProviderNotExisting() {
		ModelMap model = new ModelMap();
		String view = orderController.initCreationForm(99, model);

		assertEquals(view, "redirect:/oups");
	}

	@WithMockUser(username = "owner1", authorities = {
		"owner"
	})
	@Test
	public void TestInitCreationFormAsRoleNotAuthorizated() {
		ModelMap model = new ModelMap();
		String view = orderController.initCreationForm(TEST_PROVIDER_ID, model);

		assertEquals(view, "redirect:/oups");
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

		assertEquals(view, "/orders/orderList");

	}

	@WithMockUser(username = "manager2", authorities = {
		"manager"
	})
	@Test
	public void TestProcessCreationFormAsManagerNotAuthorized() {
		String[] productIds = {
			"1", "2"
		};
		String[] amountNumber = {
			"3", "5"
		};
		ModelMap model = new ModelMap();
		String view = orderController.processCreationForm(TEST_PROVIDER_ID, model, productIds, amountNumber);

		assertEquals(view, "redirect:/oups");
	}

	@WithMockUser(username = "manager99", authorities = {
		"manager"
	})
	@Test
	public void TestProcessCreationFormAsManagerNotExisting() {
		String[] productIds = {
			"1", "2"
		};
		String[] amountNumber = {
			"3", "5"
		};
		ModelMap model = new ModelMap();
		String view = orderController.processCreationForm(TEST_PROVIDER_ID, model, productIds, amountNumber);

		assertEquals(view, "redirect:/oups");
	}

	@WithMockUser(username = "manager1", authorities = {
		"manager"
	})
	@Test
	public void TestProcessCreationFormAsProviderNotExisting() {
		String[] productIds = {
			"1", "2"
		};
		String[] amountNumber = {
			"3", "5"
		};
		ModelMap model = new ModelMap();
		String view = orderController.processCreationForm(99, model, productIds, amountNumber);

		assertEquals(view, "redirect:/oups");
	}

	@WithMockUser(username = "owner1", authorities = {
		"owner"
	})
	@Test
	public void TestProcessCreationFormAsRoleNotAuthorizated() {
		String[] productIds = {
			"1", "2"
		};
		String[] amountNumber = {
			"3", "5"
		};
		ModelMap model = new ModelMap();
		String view = orderController.processCreationForm(99, model, productIds, amountNumber);

		assertEquals(view, "redirect:/oups");
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
	}

	@WithMockUser(username = "manager99", authorities = {
		"manager"
	})
	@Test
	public void TestShowOrderAsManagerNotExisting() {
		ModelMap model = new ModelMap();
		String view = orderController.showOrder(TEST_ORDER_ID, model);

		assertEquals(view, "redirect:/oups");
	}

	@WithMockUser(username = "manager2", authorities = {
		"manager"
	})
	@Test
	public void TestShowOrderNotPresent() {
		ModelMap model = new ModelMap();
		String view = orderController.showOrder(99, model);

		assertEquals(view, "redirect:/oups");
	}

	@WithMockUser(username = "owner1", authorities = {
		"owner"
	})
	@Test
	public void TestShowOrderAsRoleNotAuthorizated() {
		ModelMap model = new ModelMap();
		String view = orderController.showOrder(99, model);

		assertEquals(view, "redirect:/oups");
	}

	@SuppressWarnings("unchecked")
	@WithMockUser(username = "manager1", authorities = {
		"manager"
	})
	@Test
	public void TestListAvailableProvidersPositive() {
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

	@WithMockUser(username = "manager99", authorities = {
		"manager"
	})
	@Test
	public void TestListAvailableProvidersAsManagerNotExisting() {
		ModelMap model = new ModelMap();
		String view = orderController.listAvailableProviders(model);

		assertEquals(view, "redirect:/oups");
	}

	@WithMockUser(username = "owner1", authorities = {
		"owner"
	})
	@Test
	public void TestListAvailableProvidersAsRoleNotAuthorizated() {
		ModelMap model = new ModelMap();
		String view = orderController.listAvailableProviders(model);

		assertEquals(view, "redirect:/oups");
	}

	@SuppressWarnings("unchecked")
	@WithMockUser(username = "manager1", authorities = {
		"manager"
	})
	@Test
	public void TestListOrdersPositive() {
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

	@WithMockUser(username = "manager99", authorities = {
		"manager"
	})
	@Test
	public void TestListOrdersAsManagerNotExisting() {
		ModelMap model = new ModelMap();
		String view = orderController.listOrders(model);

		assertEquals(view, "redirect:/oups");
	}

	@WithMockUser(username = "owner1", authorities = {
		"owner"
	})
	@Test
	public void TestListOrdersAsRoleNotAuthorizated() {
		ModelMap model = new ModelMap();
		String view = orderController.listOrders(model);

		assertEquals(view, "redirect:/oups");
	}

}
