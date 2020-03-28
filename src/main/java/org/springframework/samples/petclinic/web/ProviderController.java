package org.springframework.samples.petclinic.web;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Manager;
import org.springframework.samples.petclinic.model.Provider;
import org.springframework.samples.petclinic.service.ManagerService;
import org.springframework.samples.petclinic.service.ProviderService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/providers")
public class ProviderController {

	private final ManagerService managerService;
	private final ProviderService providerService;

	@Autowired
	public ProviderController(final ManagerService managerService, final ProviderService providerService) {
		this.managerService = managerService;
		this.providerService = providerService;

	}

	@GetMapping(value = "/listAvailable")
	public String listAvailable(ModelMap model) {
		Iterable<Provider> providerList = this.providerService.findAvailableProviders();
		model.addAttribute("providers", providerList);
		return "providers/List";
	}

	@GetMapping(value = "/addProvider/{providerId}")
	public String addProvider(@PathVariable("providerId") Integer providerId, ModelMap modelMap) {

//		Integer managerId = Integer.parseInt(request.getParameter("managerId"));
//		Integer providerId = Integer.parseInt(request.getParameter("providerId"));
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User user = (User) authentication.getPrincipal();
		
		Optional<Provider> provider = this.providerService.findProviderById(providerId);
		Optional<Manager> manager = this.managerService.findManagerByUsername(user.getUsername());

		if (provider.isPresent() && manager.isPresent()) {
			this.providerService.addProvider(provider.get(), manager.get());
			modelMap.addAttribute("message", "Provider succesfully added to the manager");
		} else {
			modelMap.addAttribute("message", "Provider not added to the manager");
		}

		return "redirect:/providers/listAvailable";
	}
}
