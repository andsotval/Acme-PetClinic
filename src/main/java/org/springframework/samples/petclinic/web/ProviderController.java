
package org.springframework.samples.petclinic.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Manager;
import org.springframework.samples.petclinic.model.Product;
import org.springframework.samples.petclinic.model.Provider;
import org.springframework.samples.petclinic.service.ManagerService;
import org.springframework.samples.petclinic.service.ProductService;
import org.springframework.samples.petclinic.service.ProviderService;
import org.springframework.samples.petclinic.util.SessionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/providers")
public class ProviderController {

	private final ManagerService	managerService;
	private final ProviderService	providerService;
	private final ProductService	productService;


	@Autowired
	public ProviderController(final ManagerService managerService, final ProviderService providerService, final ProductService productService) {
		this.managerService = managerService;
		this.providerService = providerService;
		this.productService = productService;

	}

	@GetMapping(value = "/listAvailable")
	public String listAvailable(final ModelMap model) {
		Manager manager = managerService.findPersonByUsername(SessionUtils.obtainUserInSession().getUsername());
		Integer managerId = manager.getId();

		Iterable<Provider> availableProviderList = providerService.findAvailableProviders();
		Iterable<Provider> addedProviderList = providerService.findProvidersByManagerId(managerId);
		model.addAttribute("availableProviders", availableProviderList);
		model.addAttribute("addedProviders", addedProviderList);
		return "providers/providersList";
	}

	/* @ModelAttribute */
	@GetMapping(value = "/addProvider/{providerId}")
	public String initAddProviderToManager(@PathVariable("providerId") final Integer providerId, final Model model) {

		Manager manager = managerService.findPersonByUsername(SessionUtils.obtainUserInSession().getUsername());

		Provider provider = providerService.findEntityById(providerId).get();
		if (provider.getManager() == null) {
			provider.setManager(manager);
			providerService.saveEntity(provider);
		} else {
			model.addAttribute("message", "No es posible añadir un Provider que ya esta asignado a otro Manager");
			return "redirect:/providers/listAvailable";
		}

		return "redirect:/providers/listAvailable";
	}

	@GetMapping(value = "/listProductsByProvider/{providerId}")
	public String listProductsByProvider(@PathVariable("providerId") final Integer providerId, final ModelMap model) {
		Provider provider = providerService.findEntityById(providerId).get();
		Iterable<Product> availableProducts = productService.findProductsAvailableByProviderId(providerId);
		model.addAttribute("provider", provider);
		model.addAttribute("products", availableProducts);
		return "providers/providerProductsList";
	}
}
