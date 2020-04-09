
package org.springframework.samples.petclinic.web;

import java.time.LocalDate;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.samples.petclinic.service.VisitService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/visits")
public class VisitController {

	private final PetService	petService;
	@Autowired
	private VisitService		visitService;

	@Autowired
	private VetService			vetService;

	private static final String	VIEWS_VISIT_CREATE_OR_UPDATE_FORM	= "/visits/createOrUpdateVisitForm";


	@Autowired
	public VisitController(final PetService petService) {
		this.petService = petService;
	}

	@InitBinder
	public void setAllowedFields(final WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	/**
	 * Called before each and every @GetMapping or @PostMapping annotated method. 2 goals:
	 * - Make sure we always have fresh data - Since we do not use the session scope, make
	 * sure that Pet object always has an id (Even though id is not part of the form
	 * fields)
	 *
	 * @param petId
	 * @return Pet
	 */
	//	@ModelAttribute("visit")
	//	public Visit loadPetWithVisit(@PathVariable("petId") final int petId) {
	//		Pet pet = this.petService.findPetById(petId);
	//		Visit visit = new Visit();
	//		pet.addVisit(visit);
	//		return visit;
	//	}

	// Spring MVC calls method loadPetWithVisit(...) before initNewVisitForm is called
	@GetMapping(value = "/owners/*/pets/{petId}/visits/new")
	public String initNewVisitForm(@PathVariable("petId") final int petId, final Map<String, Object> model) {
		return "pets/createOrUpdateVisitForm";
	}

	// Spring MVC calls method loadPetWithVisit(...) before processNewVisitForm is called
	@PostMapping(value = "/owners/{ownerId}/pets/{petId}/visits/new")
	public String processNewVisitForm(@Valid final Visit visit, final BindingResult result) {
		if (result.hasErrors())
			return "pets/createOrUpdateVisitForm";
		else {
			visitService.saveEntity(visit);
			return "redirect:/owners/{ownerId}";
		}
	}

	//	@GetMapping(value = "/owners/*/pets/{petId}/visits")
	//	public String showVisits(@PathVariable final int petId, final Map<String, Object> model) {
	//		model.put("visits", this.petService.findPetById(petId).getVisits());
	//		return "visitList";
	//	}

	@GetMapping(value = "/listAllPending")
	public String listAllPending(final ModelMap modelMap) {
		String view = "visits/list";
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		User user = (User) authentication.getPrincipal();

		Vet vet = vetService.findByVetByUsername(user.getUsername());

		Iterable<Visit> visits = visitService.findAllPendingByVet(vet);

		modelMap.addAttribute("visits", visits);
		modelMap.addAttribute("accepted", false);
		return view;

	}

	@GetMapping(value = "/listAllAccepted")
	public String listAllAccepted(final ModelMap modelMap) {
		String view = "visits/list";

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		User user = (User) authentication.getPrincipal();

		Vet vet = vetService.findByVetByUsername(user.getUsername());

		Iterable<Visit> visits = visitService.findAllAcceptedByVet(vet);
		modelMap.addAttribute("visits", visits);
		modelMap.addAttribute("accepted", true);
		return view;

	}

	@GetMapping(path = "/accept/{visitId}")
	public String acceptVisit(@PathVariable("visitId") final int visitId, final ModelMap modelMap) {
		Visit visit = visitService.findEntityById(visitId).get();

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		User user = (User) authentication.getPrincipal();

		Vet vet = vetService.findByVetByUsername(user.getUsername());

		if (visit.getClinic().getId() == vet.getClinic().getId()) {
			visit.setIsAccepted(true);
			visitService.saveEntity(visit);
		} else
			modelMap.addAttribute("nonAuthorized", "No estás autorizado");

		return "redirect:/visits/listAllAccepted";

	}

	@GetMapping(path = "/cancel/{visitId}")
	public String cancelVisit(@PathVariable("visitId") final int visitId, final ModelMap modelMap) {
		Visit visit = visitService.findEntityById(visitId).get();

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		User user = (User) authentication.getPrincipal();

		Vet vet = vetService.findByVetByUsername(user.getUsername());

		if (visit.getClinic().getId() == vet.getClinic().getId()) {
			visit.setIsAccepted(false);
			visitService.saveEntity(visit);
		} else
			modelMap.addAttribute("nonAuthorized", "No estás autorizado");

		return "redirect:/visits/listAllAccepted";

	}

	@GetMapping(path = "/changeDate/{visitId}")
	public String changeDatevisit(@PathVariable("visitId") final int visitId, final ModelMap modelMap) {
		Visit visit = visitService.findEntityById(visitId).get();
		modelMap.addAttribute(visit);
		return "/visits/createOrUpdateVisitForm";
	}

	/*
	 * @PostMapping(path = "/save")
	 * public String newVisit(@Valid final Visit visit, final BindingResult result, final ModelMap modelMap) {
	 * String view = "/visits/createOrUpdateVisitForm";
	 * if (result.hasErrors()) {
	 * modelMap.addAttribute("visit", visit);
	 * return view;
	 * } else {
	 * visit.setIsAccepted(true);
	 * this.visitService.save(visit);
	 * modelMap.addAttribute("message", "Visit succesfully updated");
	 * view = this.listAllAccepted(modelMap);
	 * return "redirect:/visits/listAllAccepted";
	 * }
	 * }
	 */

	@PostMapping(path = "/save/{visitId}")
	public String updateVisit(@PathVariable("visitId") final int visitId, @Valid final Visit entity,
		final BindingResult result, final ModelMap modelMap) {

		String view = VisitController.VIEWS_VISIT_CREATE_OR_UPDATE_FORM;

		if (!visitService.findEntityById(visitId).isPresent())
			return "redirect:/oups";

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		User user = (User) authentication.getPrincipal();

		Vet vet = vetService.findByVetByUsername(user.getUsername());

		Visit visit = visitService.findEntityById(visitId).get();

		visit.setDescription(entity.getDescription());
		visit.setDate(entity.getDate());

		if (result.hasErrors())
			modelMap.addAttribute("visit", visit);
		else if (visit.getClinic().getId() != vet.getClinic().getId())
			modelMap.addAttribute("nonAuthorized", "No estás autorizado");
		else if (entity.getDate().isBefore(LocalDate.now().plusDays(2L))) {
			result.rejectValue("date", "startLaterFinish", "Posponer con 2 días de antelación");
			modelMap.addAttribute("visit", visit);
		} else {
			visitService.saveEntity(visit);
			modelMap.addAttribute("message", "Visit succesfully updated");
			view = listAllAccepted(modelMap);
			return "redirect:/visits/listAllAccepted";
		}

		return view;
	}

}
