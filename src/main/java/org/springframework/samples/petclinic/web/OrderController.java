package org.springframework.samples.petclinic.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Order;
import org.springframework.samples.petclinic.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/orders")
public class OrderController {
	
	private OrderService orderService;

	@GetMapping(value = "/list")
	public String listAvailable(ModelMap model) {
		String vista="orders/orderList";
		Iterable<Order> orders = this.orderService.findAllOrders();
		model.addAttribute("orders", orders);
		return vista;
	}
	
	

}
