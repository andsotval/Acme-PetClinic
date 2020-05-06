/**
 * DP2 - Grupo 8
 * LAB F1.33
 * Date: 05-may-2020
 */

package org.springframework.samples.petclinic.web;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Manager;
import org.springframework.samples.petclinic.model.Product;
import org.springframework.samples.petclinic.model.Provider;
import org.springframework.samples.petclinic.service.ManagerService;
import org.springframework.samples.petclinic.service.ProductService;
import org.springframework.samples.petclinic.service.ProviderService;
import org.springframework.samples.petclinic.util.SessionUtils;
import org.springframework.stereotype.Controller;
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
	public ProviderController(final ManagerService managerService, final ProviderService providerService,
		final ProductService productService) {
		this.managerService = managerService;
		this.providerService = providerService;
		this.productService = productService;
	}

	@GetMapping(value = "/listAvailable")
	public String listAvailable(final ModelMap model) {
		createModelListAvailable(model, "");
		return "providers/providersList";
	}

	@GetMapping(value = "/addProvider/{providerId}")
	public String initAddProviderToManager(@PathVariable("providerId") final Integer providerId, final ModelMap model) {
		Manager manager = managerService.findPersonByUsername(SessionUtils.obtainUserInSession().getUsername());
		if (manager == null)
			return "redirect:/oups";

		Optional<Provider> provider = providerService.findEntityById(providerId);
		if (!provider.isPresent())
			return createModelListAvailable(model, "We are very sorry, but the selected provider does not exist");
		else if (provider.get().getManager() == null) {
			provider.get().setManager(manager);
			providerService.saveEntity(provider.get());
		} else
			return createModelListAvailable(model,
				"We are very sorry, it is not possible to add a Provider that is already assigned to another Manager");

		return "redirect:/providers/listAvailable";
	}

	@GetMapping(value = "/listProductsByProvider/{providerId}")
	public String listProductsByProvider(@PathVariable("providerId") final Integer providerId, final ModelMap model) {
		Manager manager = managerService.findPersonByUsername(SessionUtils.obtainUserInSession().getUsername());
		if (manager == null)
			return "redirect:/oups";

		Optional<Provider> provider = providerService.findEntityById(providerId);
		if (!provider.isPresent())
			return createModelListAvailable(model, "We are very sorry, but the selected provider does not exist");

		if (!providerService.findAvailableProviders().contains(provider.get()))
			return createModelListAvailable(model,
				"We are very sorry, but you cannot see the products of a supplier assigned to another manager");

		Iterable<Product> availableProducts = productService.findProductsAvailableByProviderId(providerId);
		model.addAttribute("provider", provider.get());
		model.addAttribute("products", availableProducts);
		return "providers/providerProductsList";
	}

	private String createModelListAvailable(ModelMap model, String message) {
		Manager manager = managerService.findPersonByUsername(SessionUtils.obtainUserInSession().getUsername());
		if (manager == null)
			return "redirect:oups";

		Collection<Provider> availableProviderList = providerService.findAvailableProviders();
		Collection<Provider> addedProviderList = providerService.findProvidersByManagerId(manager.getId());
		model.addAttribute("availableProviders", availableProviderList);
		model.addAttribute("addedProviders", addedProviderList);
		model.addAttribute("message", message);
		return "providers/providersList";
	}
}
