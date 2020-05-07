/**
 * DP2 - Grupo 8
 * LAB F1.33
 * Date: 05-may-2020
 */

package org.springframework.samples.petclinic.web;

import java.util.Collection;
import java.util.Optional;

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

	private static final String	REDIRECT_OUPS	= "redirect:/oups";

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
		return createModelSuggestionsList(modelMap, false, "");
	}

	@GetMapping(path = "/listTrash")
	public String listTrash(ModelMap modelMap) {
		return createModelSuggestionsList(modelMap, true, "");
	}

	@GetMapping(path = "/details/{suggestionId}")
	public String detail(@PathVariable("suggestionId") Integer suggestionId, ModelMap modelMap) {
		Optional<Suggestion> suggestion = suggestionService.findEntityById(suggestionId);
		if (!suggestion.isPresent())
			return REDIRECT_OUPS;

		if (!suggestion.get().getIsRead()) {
			suggestion.get().setIsRead(true);
			suggestionService.saveEntity(suggestion.get());
		}

		modelMap.addAttribute("suggestion", suggestion.get());

		String username = suggestion.get().getUser().getUsername();
		String authority = authoritiesService.findAuthorityByUsername(username);
		modelMap.addAttribute("authority", authority.substring(0, 1).toUpperCase() + authority.substring(1));

		Person person = obtainPerson(authority, username);
		modelMap.addAttribute("person", person);

		if (suggestion.get().getIsTrash())
			modelMap.addAttribute("isTrash", true);
		else
			modelMap.addAttribute("isTrash", false);

		return "suggestion/admin/details";
	}

	@GetMapping(path = "/read/{suggestionId}")
	public String read(@PathVariable("suggestionId") Integer suggestionId, ModelMap modelMap) {
		Optional<Suggestion> suggestion = suggestionService.findEntityById(suggestionId);
		if (!suggestion.isPresent())
			return REDIRECT_OUPS;

		suggestion.get().setIsRead(true);
		suggestionService.saveEntity(suggestion.get());

		return createModelSuggestionsList(modelMap, false, "Suggestion succesfully updated");
	}

	@GetMapping(path = "/notRead/{suggestionId}")
	public String notRead(@PathVariable("suggestionId") Integer suggestionId, ModelMap modelMap) {
		Optional<Suggestion> suggestion = suggestionService.findEntityById(suggestionId);
		if (!suggestion.isPresent())
			return REDIRECT_OUPS;

		suggestion.get().setIsRead(false);
		suggestionService.saveEntity(suggestion.get());

		return createModelSuggestionsList(modelMap, false, "Suggestion succesfully updated");
	}

	@GetMapping(path = "/moveTrash/{suggestionId}")
	public String moveTrash(@PathVariable("suggestionId") Integer suggestionId, ModelMap modelMap) {
		Optional<Suggestion> suggestion = suggestionService.findEntityById(suggestionId);
		if (!suggestion.isPresent())
			return REDIRECT_OUPS;

		suggestion.get().setIsTrash(true);
		suggestionService.saveEntity(suggestion.get());

		return createModelSuggestionsList(modelMap, false, "Suggestion send to trahs succesfully");
	}

	@GetMapping(path = "/moveAllTrash")
	public String moveAllTrash(ModelMap modelMap) {
		suggestionService.moveAllTrash();
		return createModelSuggestionsList(modelMap, false, "All suggestions send to trahs succesfully");
	}

	@GetMapping(path = "/delete/{suggestionId}")
	public String delete(@PathVariable("suggestionId") Integer suggestionId, ModelMap modelMap) {
		suggestionService.deleteEntityById(suggestionId);

		return createModelSuggestionsList(modelMap, true, "Suggestion succesfully deleted");
	}

	@GetMapping(path = "/deleteAllTrash")
	public String deleteAllTrash(ModelMap modelMap) {
		suggestionService.deleteAllTrash(suggestionService.findAllEntitiesTrashOrderByIsReadAndCreated());

		return createModelSuggestionsList(modelMap, true, "All suggestions succesfully deleted");
	}

	private String createModelSuggestionsList(ModelMap model, boolean trash, String message) {
		Collection<Suggestion> list = trash ? suggestionService.findAllEntitiesTrashOrderByIsReadAndCreated()
			: suggestionService.findAllEntitiesNotTrashOrderByIsReadAndCreated();
		model.addAttribute("suggestions", list);
		model.addAttribute("isTrash", trash);
		model.addAttribute("message", message);
		return "suggestion/admin/list";
	}

	private Person obtainPerson(String authority, String username) {
		switch (authority) {
		case "manager":
			return managerService.findPersonByUsername(username);
		case "owner":
			return ownerService.findPersonByUsername(username);
		case "provider":
			return providerService.findPersonByUsername(username);
		case "veterinarian":
			return vetService.findPersonByUsername(username);
		default:
			return null;
		}
	}

}
