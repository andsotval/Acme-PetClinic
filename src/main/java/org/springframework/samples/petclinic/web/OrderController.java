/**
 * DP2 - Grupo 8
 * LAB F1.33
 * Date: 05-may-2020
 */

package org.springframework.samples.petclinic.web;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/orders")
public class OrderController {

	private static final String	VIEWS_ORDERS_CREATE_OR_UPDATE_FORM	= "/orders/createOrUpdateOrderForm";

	private static final String	REDIRECT_OUPS						= "redirect:/oups";

	private OrderService		orderService;
	private ManagerService		managerService;
	private ProductService		productService;
	private ProductOrderService	productOrderService;
	private ProviderService		providerService;


	@Autowired
	public OrderController(OrderService orderService, ManagerService managerService, ProductService productService,
		ProductOrderService productOrderService, ProviderService providerService) {
		this.orderService = orderService;
		this.managerService = managerService;
		this.productService = productService;
		this.providerService = providerService;
		this.productOrderService = productOrderService;
	}

	// inicio de creacion de Order
	@GetMapping(path = "/new/{providerId}")
	public String initCreationForm(@PathVariable("providerId") int providerId, ModelMap model) {
		Manager manager = managerService.findPersonByUsername(SessionUtils.obtainUserInSession().getUsername());
		if (manager == null)
			return REDIRECT_OUPS;

		Optional<Provider> provider = providerService.findEntityById(providerId);
		if (!provider.isPresent())
			return REDIRECT_OUPS;

		if (!provider.get().getManager().getId().equals(manager.getId()))
			return REDIRECT_OUPS;

		Collection<Product> products = productService.findProductsAvailableByProviderId(providerId);
		model.addAttribute("products", products);

		model.addAttribute("notProductsOrder", "");

		return VIEWS_ORDERS_CREATE_OR_UPDATE_FORM;
	}

	// confirmacion de la creacion de una Order
	@PostMapping(path = "/save/{providerId}")
	public String processCreationForm(@PathVariable("providerId") int providerId, ModelMap model,
		@RequestParam(name = "productIds", required = false) String[] productIds,
		@RequestParam(name = "amountNumber", required = false) String[] amountNumber) {
		Manager manager = managerService.findPersonByUsername(SessionUtils.obtainUserInSession().getUsername());
		if (manager == null)
			return REDIRECT_OUPS;

		Optional<Provider> provider = providerService.findEntityById(providerId);
		if (!provider.isPresent())
			return REDIRECT_OUPS;

		if (!provider.get().getManager().getId().equals(manager.getId()))
			return REDIRECT_OUPS;

		Collection<Product> productsSelected = productService.findProductsByIds(
			Arrays.stream(productIds).mapToInt(Integer::valueOf).boxed().collect(Collectors.toList()));

		Collection<Integer> amountNumberSelected = Arrays.stream(amountNumber).mapToInt(Integer::valueOf).boxed()
			.collect(Collectors.toList());

		Collection<ProductOrder> productsOrderList = createProductsOrderByProducts(productsSelected,
			amountNumberSelected);

		if (!productsOrderList.iterator().hasNext()) {
			Collection<Product> products = productService.findProductsAvailableByProviderId(providerId);
			model.addAttribute("products", products);
			model.addAttribute("notProductsOrder", "You have not added any quantity for the products");

			return VIEWS_ORDERS_CREATE_OR_UPDATE_FORM;
		}

		Order order = new Order();
		order.setDate(LocalDate.now());
		order.setIsAccepted(false);
		order.setManager(manager);

		order = orderService.saveEntity(order);

		Iterator<ProductOrder> it = productsOrderList.iterator();
		while (it.hasNext()) {
			ProductOrder po = it.next();
			po.setOrder(order);

			productOrderService.saveEntity(po);
		}

		return createModelOrderList(model, "Order succesfully saved");

	}

	// detalle de una Order
	@GetMapping("/{orderId}")
	public String showOrder(@PathVariable("orderId") int orderId, ModelMap modelMap) {
		Manager manager = managerService.findPersonByUsername(SessionUtils.obtainUserInSession().getUsername());
		if (manager == null)
			return REDIRECT_OUPS;

		Optional<Order> order = orderService.findEntityById(orderId);
		if (!order.isPresent())
			return REDIRECT_OUPS;

		if (!order.get().getManager().getId().equals(manager.getId()))
			return REDIRECT_OUPS;

		Provider provider = productOrderService.findProviderByOrder(orderId);
		Collection<ProductOrder> productsOrder = productOrderService.findProductOrderByOrder(orderId);

		modelMap.addAttribute("order", order.get());
		modelMap.addAttribute("productsOrder", productsOrder);
		modelMap.addAttribute("provider", provider);

		return "orders/orderDetails";

	}

	// listado de providers
	@GetMapping(path = "/providers/listAvailable")
	public String listAvailableProviders(ModelMap modelMap) {
		Manager manager = managerService.findPersonByUsername(SessionUtils.obtainUserInSession().getUsername());
		if (manager == null)
			return REDIRECT_OUPS;

		Collection<Provider> providerList = providerService.findProvidersByManagerId(manager.getId());
		modelMap.addAttribute("providers", providerList);

		return "/orders/providers/providerList";
	}

	// listado de orders
	@GetMapping(path = "/list")
	public String listOrders(ModelMap modelMap) {
		return createModelOrderList(modelMap, "");
	}

	private String createModelOrderList(ModelMap model, String message) {
		Manager manager = managerService.findPersonByUsername(SessionUtils.obtainUserInSession().getUsername());
		if (manager == null)
			return REDIRECT_OUPS;

		Collection<Order> orderList = orderService.findAllOrdersByManagerId(manager.getId());
		model.addAttribute("orders", orderList);
		model.addAttribute("message", message);

		return "/orders/orderList";
	}

	private Collection<ProductOrder> createProductsOrderByProducts(Collection<Product> products,
		Collection<Integer> amounts) {
		Iterator<Product> productsSelected = products.iterator();
		Iterator<Integer> amountsSelected = amounts.iterator();
		Collection<ProductOrder> productsOrderList = new ArrayList<ProductOrder>();
		while (productsSelected.hasNext()) {
			Product product = productsSelected.next();
			ProductOrder pOrder = new ProductOrder();
			pOrder.setPrice(product.getPrice());
			pOrder.setTax(product.getTax());
			pOrder.setProduct(product);

			Integer amount = amountsSelected.next();
			if (!amount.equals(0)) {
				pOrder.setAmount(amount);
				productsOrderList.add(pOrder);
			}
		}
		return productsOrderList;
	}

}
