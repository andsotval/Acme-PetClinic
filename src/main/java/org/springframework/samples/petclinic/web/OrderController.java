package org.springframework.samples.petclinic.web;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Manager;
import org.springframework.samples.petclinic.model.Order;
import org.springframework.samples.petclinic.model.Product;
import org.springframework.samples.petclinic.service.ManagerService;
import org.springframework.samples.petclinic.service.OrderService;
import org.springframework.samples.petclinic.service.ProductService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/orders")
public class OrderController {
	
	private static final String VIEWS_ORDERS_CREATE_OR_UPDATE_FORM = "orders/createOrUpdateOrderForm";
	
	private final OrderService orderService;
	private final ManagerService managerService;
	private final ProductService productService;
	
	@Autowired
	public OrderController(final OrderService orderService, final ManagerService managerService, final ProductService productService) {
		this.orderService = orderService;
		this.managerService = managerService;
		this.productService = productService;
	}
	
//	@GetMapping(value = "/products/listAvailable")
//	public String listAvailable(ModelMap modelMap) {
//		Manager manager = obtainManagerInSession();
//		Iterable<Product> productsList = this.productService.findProductsAvailableForManager(manager.getId());
//		modelMap.addAttribute("products", productsList);
//		return "/orders/products/productsList";
//	}
	
	@ModelAttribute("products")
	public Iterable<Product> populateProducts() {
		Manager manager = obtainManagerInSession();
		return this.productService.findProductsAvailableForManager(manager.getId());
	}
	
	@GetMapping(value = "/new")
	public String initCreationForm(Map<String, Object> model) {
		
		Order order = new Order();
		Manager manager = obtainManagerInSession();
		order.setManager(manager);
		model.put("order", order);
		
		return VIEWS_ORDERS_CREATE_OR_UPDATE_FORM;
	}
	
	@PostMapping(value = "/orders/new")
	public String processCreationForm(@Valid Order order, BindingResult result) {
		if (result.hasErrors()) {
			return VIEWS_ORDERS_CREATE_OR_UPDATE_FORM;
		}
		else {
			this.orderService.saveOrder(order);
			
			return "redirect:/owners/" + order.getId();
		}
	}
	
	private Manager obtainManagerInSession() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User user = (User) authentication.getPrincipal();
		Manager manager = this.managerService.findManagerByUsername(user.getUsername()).get();
		return manager;
		
	}
	

}
