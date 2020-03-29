package org.springframework.samples.petclinic.web;

import javax.validation.Valid;

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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/providers")
public class ProviderController {

	private static final String VIEWS_PROVIDER_CREATE_OR_UPDATE_FORM = "providers/createOrUpdateProviderForm";

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
		return "providers/providersList";
	}

	@GetMapping(value = "/addProvider/{providerId}")
	public String initAddProviderToManager(@PathVariable("providerId") Integer providerId, Model model) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User user = (User) authentication.getPrincipal();
		Manager manager = this.managerService.findManagerByUsername(user.getUsername()).get();
		Provider provider = this.providerService.findProviderById(providerId).get();
		provider.setManager(manager);

		model.addAttribute(provider);
		model.addAttribute(manager);

		return VIEWS_PROVIDER_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/addProvider/{providerId}")
	public String proccessAddProviderToManager(@Valid Provider provider, @Valid Manager manager, BindingResult result,
			@PathVariable("providerId") Integer providerId) {

		if (provider != null && manager != null) {
			manager.setId(provider.getManager().getId());
			
			provider.getUser().setId(this.providerService.findProviderById(providerId).get().getUser().getId());
			provider.setManager(manager);
			provider.setId(providerId);
			
			this.providerService.saveProvider(provider);
		} else {
			return VIEWS_PROVIDER_CREATE_OR_UPDATE_FORM;
		}

		return "redirect:/providers/listAvailable";
	}

//	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//	User user = (User) authentication.getPrincipal();
//	
//	Optional<Provider> provider = this.providerService.findProviderById(providerId);
//	Optional<Manager> manager = this.managerService.findManagerByUsername(user.getUsername());

}
