
package org.springframework.samples.petclinic.web;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Person;
import org.springframework.samples.petclinic.model.Suggestion;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.ManagerService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.ProviderService;
import org.springframework.samples.petclinic.service.SuggestionService;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/suggestion/admin")
public class SuggestionAdminController {

	private SuggestionService	suggestionService;

	private AuthoritiesService	authoritiesService;

	private ManagerService		managerService;

	private OwnerService		ownerService;

	private ProviderService		providerService;

	private VetService			vetService;


	@Autowired
	public SuggestionAdminController(SuggestionService suggestionService, AuthoritiesService authoritiesService,
		ManagerService managerService, OwnerService ownerService, ProviderService providerService,
		VetService vetService) {
		this.suggestionService = suggestionService;
		this.authoritiesService = authoritiesService;
		this.managerService = managerService;
		this.ownerService = ownerService;
		this.providerService = providerService;
		this.vetService = vetService;
	}

	@GetMapping(path = "/list")
	public String list(ModelMap modelMap) {
		Collection<Suggestion> list = suggestionService.findAllEntitiesNotTrashOrderByIsReadAndCreated();
		modelMap.addAttribute("suggestions", list);
		modelMap.addAttribute("isTrash", false);
		return "suggestion/admin/list";
	}

	@GetMapping(path = "/listTrash")
	public String listTrash(ModelMap modelMap) {
		Collection<Suggestion> list = suggestionService.findAllEntitiesTrashOrderByIsReadAndCreated();
		modelMap.addAttribute("suggestions", list);
		modelMap.addAttribute("isTrash", true);
		return "suggestion/admin/list";
	}

	@GetMapping(path = "/details/{suggestionId}")
	public String detail(@PathVariable("suggestionId") Integer suggestionId, ModelMap modelMap) {
		Suggestion suggestion = suggestionService.findEntityById(suggestionId).get();

		if (!suggestion.getIsRead()) {
			suggestion.setIsRead(true);
			suggestion = suggestionService.saveEntity(suggestion);
		}

		modelMap.addAttribute("suggestion", suggestion);

		String username = suggestion.getUser().getUsername();
		String authority = authoritiesService.findAuthorityByUsername(username);
		modelMap.addAttribute("authority", authority.substring(0, 1).toUpperCase() + authority.substring(1));

		Person person = obtainPerson(authority, username);
		modelMap.addAttribute("person", person);

		if (suggestion.getIsTrash())
			modelMap.addAttribute("isTrash", true);
		else
			modelMap.addAttribute("isTrash", false);

		return "suggestion/admin/details";
	}

	@GetMapping(path = "/read/{suggestionId}")
	public String read(@PathVariable("suggestionId") Integer suggestionId, ModelMap modelMap) {
		Suggestion suggestion = suggestionService.findEntityById(suggestionId).get();

		suggestion.setIsRead(true);
		suggestionService.saveEntity(suggestion);

		return "redirect:/suggestion/admin/list";
	}

	@GetMapping(path = "/notRead/{suggestionId}")
	public String notRead(@PathVariable("suggestionId") Integer suggestionId, ModelMap modelMap) {
		Suggestion suggestion = suggestionService.findEntityById(suggestionId).get();

		suggestion.setIsRead(false);
		suggestionService.saveEntity(suggestion);

		return "redirect:/suggestion/admin/list";
	}

	@GetMapping(path = "/moveTrash/{suggestionId}")
	public String moveTrash(@PathVariable("suggestionId") Integer suggestionId, ModelMap modelMap) {
		Suggestion suggestion = suggestionService.findEntityById(suggestionId).get();

		suggestion.setIsTrash(true);
		suggestionService.saveEntity(suggestion);

		return "redirect:/suggestion/admin/list";
	}

	@GetMapping(path = "/moveAllTrash")
	public String moveTrash(ModelMap modelMap) {
		suggestionService.moveAllTrash();
		return "redirect:/suggestion/admin/list";
	}

	@GetMapping(path = "/delete/{suggestionId}")
	public String delete(@PathVariable("suggestionId") Integer suggestionId, ModelMap modelMap) {
		suggestionService.deleteEntityById(suggestionId);

		return "redirect:/suggestion/admin/listTrash";
	}

	@GetMapping(path = "/deleteAllTrash")
	public String deleteAllTrash(ModelMap modelMap) {
		suggestionService.deleteAllTrash(suggestionService.findAllEntitiesTrashOrderByIsReadAndCreated());

		return "redirect:/suggestion/admin/listTrash";
	}

	private Person obtainPerson(String authority, String username) {
		switch (authority) {
		case "manager":
			return managerService.findPersonByUsername(username);
		case "owner":
			return ownerService.findPersonByUsername(username);
		case "provider":
			return providerService.findPersonByUsername(username);
		case "vet":
			return vetService.findPersonByUsername(username);
		default:
			return null;
		}

	}
}
