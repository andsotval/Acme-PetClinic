
package org.springframework.samples.petclinic.web;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.validation.Valid;

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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/orders")
public class OrderController {

	private static final String VIEWS_ORDERS_CREATE_OR_UPDATE_FORM = "/orders/createOrUpdateOrderForm";

	private OrderService orderService;
	private ManagerService managerService;
	private ProductService productService;
	private ProductOrderService productOrderService;
	private ProviderService providerService;

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

		Iterable<ProductOrder> productsOrder = createProductsOrderByProviderId(providerId);

		Iterable<Product> productsOfProvider = productService.findProductsAvailableByProviderId(providerId);
		
		Order order = new Order();
		order.setManager(managerService.findPersonByUsername(SessionUtils.obtainUserInSession().getUsername()));
		order.setDate(LocalDate.now());
		order.setIsAccepted(false);

		model.addAttribute("order", order);

		model.addAttribute("providerId", providerId);

		model.addAttribute("products", productsOrder);

		return VIEWS_ORDERS_CREATE_OR_UPDATE_FORM;
	}

	// confirmacion de la creacion de una Order
	@PostMapping(path = "/save/{providerId}")
	public String processCreationForm(@PathVariable("providerId") int providerId, @Valid Order order,
			BindingResult result, ModelMap model) {
		String returnView;

		Object o = model.getAttribute("products");
		Iterable<ProductOrder> products = (Iterable<ProductOrder>) o;

		if (result.hasErrors()) {
			model.addAttribute("order", order);
			Iterable<ProductOrder> productsOrder = createProductsOrderByProviderId(providerId);
			model.addAttribute("products", productsOrder);

			return VIEWS_ORDERS_CREATE_OR_UPDATE_FORM;
		} else {
			Provider provider = providerService.findEntityById(providerId).get();
			Boolean security = provider.getManager().getId() == managerService
					.findPersonByUsername(SessionUtils.obtainUserInSession().getUsername()).getId();

			if (security) {
				orderService.saveEntity(order);
				// productOrderService.saveEntity(productOrder);
				returnView = "redirect:/orders/" + order.getId();
			} else {
				model.addAttribute("message",
						"Se esta intentando crear un pedido con un proveedor al que el manager actual no está asociado");
				returnView = "redirect:/oups";
			}

			return returnView;
		}
	}

	// Order Details
	@GetMapping("/{orderId}")
	public String showOrder(@PathVariable("orderId") int orderId, ModelMap modelMap) {
		Manager managerLogged = managerService.findPersonByUsername(SessionUtils.obtainUserInSession().getUsername());
		Order order = orderService.findEntityById(orderId).get();
		String returnView = "orders/orderDetails";

		if (order.getManager().getId() == managerLogged.getId()) {
			Provider provider = productOrderService.findProductOrderByOrder(orderId).iterator().next().getProduct()
					.getProvider();
			Iterable<ProductOrder> productsOrder = productOrderService.findProductOrderByOrder(orderId);

			modelMap.addAttribute("order", order);
			modelMap.addAttribute("productsOrder", productsOrder);
			modelMap.addAttribute("provider", provider);
		} else {
			modelMap.addAttribute("message",
					"Se esta intentando acceder a un pedido que no pertenece al manager actual");

			returnView = "redirect:/oups";
		}
		return returnView;
	}

	// listado de providers
	@GetMapping(path = "/providers/listAvailable")
	public String listAvailableProviders(ModelMap modelMap) {
		Manager manager = managerService.findPersonByUsername(SessionUtils.obtainUserInSession().getUsername());

		Iterable<Provider> providerList = providerService.findProvidersByManagerId(manager.getId());
		modelMap.addAttribute("providers", providerList);

		return "/orders/providers/providerList";
	}

	// listado de orders
	@GetMapping(path = "/list")
	public String listOrders(ModelMap modelMap) {
		Manager manager = managerService.findPersonByUsername(SessionUtils.obtainUserInSession().getUsername());

		Iterable<Order> orderList = orderService.findAllOrdersByManagerId(manager.getId());
		Iterable<ProductOrder> productOrder = productOrderService
				.findProductOrderByOrder(orderList.iterator().next().getId());
		modelMap.addAttribute("productOrder", productOrder);
		// }
		modelMap.addAttribute("orders", orderList);

		return "/orders/orderList";
	}

	private Iterable<ProductOrder> createProductsOrderByProviderId(int providerId) {
		Iterable<Product> productsOfProvider = productService.findProductsAvailableByProviderId(providerId);
		Iterator<Product> productsAvailable = productsOfProvider.iterator();
		List<ProductOrder> productsOrderList = new ArrayList<ProductOrder>();
		while (productsAvailable.hasNext()) {
			Product product = productsAvailable.next();
			ProductOrder pOrder = new ProductOrder();
			pOrder.setName(product.getName());
			pOrder.setPrice(product.getPrice());
			pOrder.setTax(product.getTax());
			pOrder.setProduct(product);
			pOrder.setAmount(0);
			productsOrderList.add(pOrder);
		}
		return productsOrderList;
	}

}
