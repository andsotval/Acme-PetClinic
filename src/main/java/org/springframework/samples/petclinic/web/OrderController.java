
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
import org.springframework.web.servlet.ModelAndView;

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
	@GetMapping(value = "/new/{providerId}")
	public String initCreationForm(@PathVariable("providerId") int providerId, ModelMap model) {
		Manager manager = managerService.findPersonByUsername(SessionUtils.obtainUserInSession().getUsername());

		Order order = new Order();
		order.setManager(manager);
		model.addAttribute("order", order);

		Iterable<Product> product = productService.findProductsAvailableByProviderId(providerId);
		model.addAttribute("products", product);

		return VIEWS_ORDERS_CREATE_OR_UPDATE_FORM;
	}

	//confirmacion de la creacion de una Order
	@PostMapping(value = "/new/{providerId}")
	public String processCreationForm(@Valid Order order, BindingResult result, ModelMap model) {
		if (result.hasErrors())
			return VIEWS_ORDERS_CREATE_OR_UPDATE_FORM;
		else {
			try {
				orderService.saveEntity(order);
			} catch (Exception e) {
				System.out.println("No se ha podido crear: " + e.getMessage());

			}

			return "redirect:/orders/" + order.getId();
		}
	}

	//Order Details
	@GetMapping("/{orderId}")
	public ModelAndView showOrder(@PathVariable("orderId") int orderId, ModelMap modelMap) {
		ModelAndView mav = new ModelAndView("orders/orderDetails");
		Order order = orderService.findEntityById(orderId).get();

		Provider orderProvider = order.getProduct().iterator().next().getProvider();
		modelMap.addAttribute("orderProvider", orderProvider);

		mav.addObject(order);

		return mav;
	}

	//listado de providers
	@GetMapping(value = "/providers/listAvailable")
	public String listAvailableProviders(ModelMap modelMap) {
		Manager manager = managerService.findPersonByUsername(SessionUtils.obtainUserInSession().getUsername());

		Iterable<Provider> providerList = providerService.findProvidersByManagerId(manager.getId());
		modelMap.addAttribute("providers", providerList);

		return "/orders/providers/providerList";
	}

	//listado de orders
	@GetMapping(value = "/list")
	public String listOrders(ModelMap modelMap) {
		Manager manager = managerService.findPersonByUsername(SessionUtils.obtainUserInSession().getUsername());

		Iterable<Order> orderList = orderService.findAllOrdersByManagerId(manager.getId());
		modelMap.addAttribute("orders", orderList);

		return "/orders/orderList";
	}

}
