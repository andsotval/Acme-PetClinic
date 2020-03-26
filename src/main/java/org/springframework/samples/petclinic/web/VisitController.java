/**
 * DP2 - Grupo 8
 * LAB F1.33
 * Date: 05-mar-2020
 * User: carlo
 */

package org.springframework.samples.petclinic.web;

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

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Michael Isvy
 */
@Controller
@RequestMapping("/visits")
public class VisitController {

	private final PetService	petService;
	@Autowired
	private VisitService		visitService;
	@Autowired
	private VetService			vetService;


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
		if (result.hasErrors()) {
			return "pets/createOrUpdateVisitForm";
		} else {
			this.petService.saveVisit(visit);
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

		Iterable<Visit> visits = this.visitService.findAllAccepted();

		modelMap.addAttribute("visits", visits);
		return view;

	}

	@GetMapping(path = "/accept/{visitId}")
	public String acceptVisit(@PathVariable("visitId") final int visitId, final ModelMap modelMap) {
		Visit visit = this.visitService.findById(visitId);

		this.visitService.acceptVisit(visit);

		return "redirect:/visits/listAllAccepted";

	}

	@GetMapping(path = "/cancel/{visitId}")
	public String cancelVisit(@PathVariable("visitId") final int visitId, final ModelMap modelMap) {
		Visit visit = this.visitService.findById(visitId);

		this.visitService.cancelVisit(visit);

		return "redirect:/visits/listAllAccepted";

	}

}
