
package org.springframework.samples.petclinic.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Manager;
import org.springframework.samples.petclinic.model.Provider;
import org.springframework.samples.petclinic.service.ManagerService;
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


	@Autowired
	public ProviderController(final ManagerService managerService, final ProviderService providerService) {
		this.managerService = managerService;
		this.providerService = providerService;

	}

	@GetMapping(value = "/listAvailable")
	public String listAvailable(final ModelMap model) {
		Iterable<Provider> providerList = this.providerService.findAvailableProviders();
		model.addAttribute("providers", providerList);
		return "providers/providersList";
	}

	/* @ModelAttribute */
	@GetMapping(value = "/addProvider/{providerId}")
	public String initAddProviderToManager(@PathVariable("providerId") final Integer providerId, final Model model) {

		Manager manager = this.managerService.findPersonByUsername(SessionUtils.obtainUserInSession().getUsername());

		Provider provider = this.providerService.findEntityById(providerId).get();
		if (provider.getManager() == null) {
			provider.setManager(manager);
			this.providerService.saveEntity(provider);
		} else {
			model.addAttribute("message", "No es posible añadir un Provider que ya esta asignado a otro Manager");
			return "redirect:/providers/listAvailable";
		}

		return "redirect:/providers/listAvailable";
	}

}
