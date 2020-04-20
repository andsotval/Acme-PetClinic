
package org.springframework.samples.petclinic.web;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Manager;
import org.springframework.samples.petclinic.model.Order;
import org.springframework.samples.petclinic.model.Product;
import org.springframework.samples.petclinic.model.Provider;
import org.springframework.samples.petclinic.service.ManagerService;
import org.springframework.samples.petclinic.service.OrderService;
import org.springframework.samples.petclinic.service.ProductService;
import org.springframework.samples.petclinic.service.ProviderService;
import org.springframework.samples.petclinic.util.SessionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/orders")
public class OrderController {

	private static final String		VIEWS_ORDERS_CREATE_OR_UPDATE_FORM	= "/orders/createOrUpdateOrderForm";

	private final OrderService		orderService;
	private final ManagerService	managerService;
	private final ProductService	productService;
	private final ProviderService	providerService;


	@Autowired
	public OrderController(final OrderService orderService, final ManagerService managerService,
		final ProductService productService, final ProviderService providerService) {
		this.orderService = orderService;
		this.managerService = managerService;
		this.productService = productService;
		this.providerService = providerService;
	}

	//inicio de creacion de Order
	@GetMapping(path = "/new/{providerId}")
	public String initCreationForm(@PathVariable("providerId") int providerId, ModelMap model) {

		Order order = new Order();
		order.setManager(managerService.findPersonByUsername(SessionUtils.obtainUserInSession().getUsername()));
		model.addAttribute("order", order);

		Iterable<Product> product = productService.findProductsAvailableByProviderId(providerId);
		model.addAttribute("products", product);

		return VIEWS_ORDERS_CREATE_OR_UPDATE_FORM;
	}

	//confirmacion de la creacion de una Order
	@PostMapping(path = "/new/{providerId}")
	public String processCreationForm(@PathVariable("providerId") int providerId, @Valid Order order,
		BindingResult result, ModelMap model) {
		String returnView;

		if (result.hasErrors())
			return VIEWS_ORDERS_CREATE_OR_UPDATE_FORM;
		else {
			Provider provider = providerService.findEntityById(providerId).get();
			Boolean security = provider.getManager().getId() == managerService
				.findPersonByUsername(SessionUtils.obtainUserInSession().getUsername()).getId();

			if (security) {
				orderService.saveEntity(order);
				returnView = "redirect:/orders/" + order.getId();
			} else {
				model.addAttribute("message",
					"Se esta intentando crear un pedido con un proveedor al que el manager actual no está asociado");
				returnView = "redirect:/oups";
			}

			return returnView;
		}
	}

	//Order Details
	@GetMapping("/{orderId}")
	public String showOrder(@PathVariable("orderId") int orderId, ModelMap modelMap) {
		Order order = orderService.findEntityById(orderId).get();
		String returnView = "orders/orderDetails";

		if (order.getManager().getId() == managerService
			.findPersonByUsername(SessionUtils.obtainUserInSession().getUsername()).getId()) {
			Provider orderProvider = order.getProduct().iterator().next().getProvider();
			modelMap.addAttribute("orderProvider", orderProvider);

			modelMap.addAttribute("order", order);
		} else {
			modelMap.addAttribute("message",
				"Se esta intentando acceder a un pedido que no pertenece al manager actual");
			returnView = "redirect:/oups";
		}
		return returnView;
	}

	//listado de providers
	@GetMapping(path = "/providers/listAvailable")
	public String listAvailableProviders(ModelMap modelMap) {
		Manager manager = managerService.findPersonByUsername(SessionUtils.obtainUserInSession().getUsername());

		Iterable<Provider> providerList = providerService.findProvidersByManagerId(manager.getId());
		modelMap.addAttribute("providers", providerList);

		return "/orders/providers/providerList";
	}

	//listado de orders
	@GetMapping(path = "/list")
	public String listOrders(ModelMap modelMap) {
		Manager manager = managerService.findPersonByUsername(SessionUtils.obtainUserInSession().getUsername());

		Iterable<Order> orderList = orderService.findAllOrdersByManagerId(manager.getId());
		modelMap.addAttribute("orders", orderList);

		return "/orders/orderList";
	}

}
