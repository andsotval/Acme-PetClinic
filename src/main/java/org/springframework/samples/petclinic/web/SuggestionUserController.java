/**
 * DP2 - Grupo 8
 * LAB F1.33
 * Date: 05-may-2020
 */

package org.springframework.samples.petclinic.web;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Person;
import org.springframework.samples.petclinic.model.Suggestion;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.ManagerService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.ProviderService;
import org.springframework.samples.petclinic.service.SuggestionService;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.samples.petclinic.util.SessionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/suggestion/user")
public class SuggestionUserController {

	private static final String	REDIRECT_OUPS					= "redirect:/oups";

	private static final String	VIEWS_SUGGESTION_CREATE_FORM	= "suggestion/user/createSuggestionForm";

	private SuggestionService	suggestionService;

	private AuthoritiesService	authoritiesService;

	private ManagerService		managerService;

	private OwnerService		ownerService;

	private ProviderService		providerService;

	private VetService			vetService;


	@Autowired
	public SuggestionUserController(SuggestionService suggestionService, AuthoritiesService authoritiesService,
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
		return createModelSuggestionsList(modelMap, "");
	}

	@GetMapping(path = "/details/{suggestionId}")
	public String detail(@PathVariable("suggestionId") Integer suggestionId, ModelMap modelMap) {
		Optional<Suggestion> suggestion = suggestionService.findEntityById(suggestionId);
		if (!suggestion.isPresent())
			return REDIRECT_OUPS;

		if (!suggestion.get().getUser().getUsername().equals(SessionUtils.obtainUserInSession().getUsername()))
			return REDIRECT_OUPS;

		modelMap.addAttribute("suggestion", suggestion.get());

		return "suggestion/user/details";
	}

	@GetMapping(value = "/new")
	public String create(ModelMap modelMap) {
		Suggestion suggestion = new Suggestion();
		String username = SessionUtils.obtainUserInSession().getUsername();
		String authority = authoritiesService.findAuthorityByUsername(username);
		if (authority == null)
			return REDIRECT_OUPS;

		Person person = obtainPerson(authority, username);

		suggestion.setCreated(LocalDateTime.now());
		suggestion.setUser(person.getUser());
		suggestion.setIsAvailable(true);
		suggestion.setIsRead(false);
		suggestion.setIsTrash(false);

		modelMap.addAttribute("suggestion", suggestion);
		return VIEWS_SUGGESTION_CREATE_FORM;
	}

	@PostMapping(path = "/save")
	public String save(@Valid Suggestion suggestion, BindingResult result, ModelMap modelMap) {
		if (result.hasErrors()) {
			modelMap.addAttribute("suggestion", suggestion);
			return VIEWS_SUGGESTION_CREATE_FORM;
		} else {
			suggestionService.saveEntity(suggestion);
			return createModelSuggestionsList(modelMap, "Suggestion succesfully created");
		}

	}

	@GetMapping(path = "/delete/{suggestionId}")
	public String delete(@PathVariable("suggestionId") Integer suggestionId, ModelMap modelMap) {
		Optional<Suggestion> suggestion = suggestionService.findEntityById(suggestionId);
		if (!suggestion.isPresent())
			return REDIRECT_OUPS;

		suggestion.get().setIsAvailable(false);
		suggestionService.saveEntity(suggestion.get());

		return createModelSuggestionsList(modelMap, "Suggestion succesfully deleted");
	}

	@GetMapping(path = "/deleteAll")
	public String deleteAll(ModelMap modelMap) {
		suggestionService.updateAllIsAvailableFalse(SessionUtils.obtainUserInSession().getUsername());
		return createModelSuggestionsList(modelMap, "All suggestion succesfully deleted");
	}

	private String createModelSuggestionsList(ModelMap model, String message) {
		Collection<Suggestion> list = suggestionService
			.findAllEntitiesAvailableByUsername(SessionUtils.obtainUserInSession().getUsername());
		model.addAttribute("suggestions", list);
		return "suggestion/user/list";
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
