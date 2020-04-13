
package org.springframework.samples.petclinic.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Manager;
import org.springframework.samples.petclinic.model.Provider;
import org.springframework.samples.petclinic.service.ManagerService;
import org.springframework.samples.petclinic.service.ProviderService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
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


	@Autowired
	public ProviderController(final ManagerService managerService, final ProviderService providerService) {
		this.managerService = managerService;
		this.providerService = providerService;

	}

	@GetMapping(value = "/listAvailable")
	public String listAvailable(ModelMap model) {
		Iterable<Provider> providerList = providerService.findAvailableProviders();
		model.addAttribute("providers", providerList);
		return "providers/providersList";
	}

	@GetMapping(value = "/addProvider/{providerId}")
	public String initAddProviderToManager(@PathVariable("providerId") Integer providerId, Model model) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User user = (User) authentication.getPrincipal();
		Manager manager = managerService.findPersonByUsername(user.getUsername());

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

}
