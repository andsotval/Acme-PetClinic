/**
 * DP2 - Grupo 8
 * LAB F1.33
 * Date: 05-may-2020
 */

package org.springframework.samples.petclinic.web;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.StreamSupport;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.samples.petclinic.service.VisitService;
import org.springframework.samples.petclinic.util.SessionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/visits")
public class VisitController {

	private static final String	VIEWS_VISIT_CREATE_OR_UPDATE_FORM	= "visits/createOrUpdateVisitForm";

	private static final String	REDIRECT_OUPS						= "redirect:/oups";

	private static final String	REDIRECT_VISIT_LIST_ALL_ACCEPTED	= "redirect:/visits/listAllAccepted";

	private static final String	REDIRECT_VISIT_LIST_BY_OWNER		= "redirect:/visits/listByOwner";

	private VisitService		visitService;

	private VetService			vetService;

	private OwnerService		ownerService;

	private AuthoritiesService	authoritiesService;


	@Autowired
	public VisitController(final VisitService visitService, final VetService vetService,
		final OwnerService ownerService, final AuthoritiesService authoritiesService) {
		this.visitService = visitService;
		this.vetService = vetService;
		this.ownerService = ownerService;
		this.authoritiesService = authoritiesService;
	}

	@GetMapping(value = "/listAllPending")
	public String listAllPending(ModelMap modelMap) {
		return createModelVisitsList(modelMap, false, "");
	}

	@GetMapping(value = "/listAllAccepted")
	public String listAllAccepted(ModelMap modelMap) {
		return createModelVisitsList(modelMap, true, "");
	}

	@GetMapping(path = "/accept/{visitId}")
	public String acceptVisit(@PathVariable("visitId") int visitId, ModelMap modelMap) {

		Vet vet = vetService.findPersonByUsername(SessionUtils.obtainUserInSession().getUsername());
		if (vet == null)
			return REDIRECT_OUPS;

		Optional<Visit> visit = visitService.findEntityById(visitId);
		if (!visit.isPresent())
			return REDIRECT_OUPS;

		if (!visit.get().getClinic().getId().equals(vet.getClinic().getId()))
			return REDIRECT_OUPS;

		visit.get().setIsAccepted(true);
		visitService.saveEntity(visit.get());

		return REDIRECT_VISIT_LIST_ALL_ACCEPTED;
	}

	@GetMapping(path = "/cancel/{visitId}")
	public String cancelVisit(@PathVariable("visitId") int visitId, ModelMap modelMap) {
		Optional<Visit> visit = visitService.findEntityById(visitId);
		if (!visit.isPresent())
			return REDIRECT_OUPS;

		String username = SessionUtils.obtainUserInSession().getUsername();
		String authority = authoritiesService.findAuthorityByUsername(username);
		if (authority == null)
			return REDIRECT_OUPS;

		if (authority.equals("veterinarian")) {
			Vet vet = vetService.findPersonByUsername(username);
			if (vet == null)
				return REDIRECT_OUPS;

			if (!visit.get().getClinic().getId().equals(vet.getClinic().getId()))
				return REDIRECT_OUPS;

			visit.get().setIsAccepted(false);
			visitService.saveEntity(visit.get());

			return REDIRECT_VISIT_LIST_ALL_ACCEPTED;
		}

		else if (authority.equals("owner")) {
			Owner owner = ownerService.findPersonByUsername(username);
			if (owner == null)
				return REDIRECT_OUPS;

			if (!visit.get().getPet().getOwner().getId().equals(owner.getId()))
				return REDIRECT_OUPS;

			visit.get().setIsAccepted(false);
			visitService.saveEntity(visit.get());

			return REDIRECT_VISIT_LIST_BY_OWNER;
		}

		return REDIRECT_OUPS;
	}

	@GetMapping(path = "/changeDate/{visitId}")
	public String initUpdateVisit(@PathVariable("visitId") final int visitId, final ModelMap modelMap) {
		Vet vet = vetService.findPersonByUsername(SessionUtils.obtainUserInSession().getUsername());
		if (vet == null)
			return REDIRECT_OUPS;

		Optional<Visit> visit = visitService.findEntityById(visitId);
		if (!visit.isPresent())
			return REDIRECT_OUPS;

		if (!visit.get().getClinic().getId().equals(vet.getClinic().getId()))
			return REDIRECT_OUPS;

		modelMap.addAttribute("visit", visit.get());
		modelMap.addAttribute("hasClinic", true);

		return VIEWS_VISIT_CREATE_OR_UPDATE_FORM;

	}

	@PostMapping(path = "/save/{visitId}")
	public String updateVisit(@PathVariable("visitId") int visitId, @Valid Visit entity, BindingResult result,
		ModelMap modelMap) {
		Vet vet = vetService.findPersonByUsername(SessionUtils.obtainUserInSession().getUsername());
		if (vet == null)
			return REDIRECT_OUPS;

		Optional<Visit> visit = visitService.findEntityById(visitId);
		if (!visit.isPresent())
			return REDIRECT_OUPS;

		if (!visit.get().getClinic().getId().equals(vet.getClinic().getId()))
			return REDIRECT_OUPS;

		modelMap.addAttribute("visit", entity);

		result = checkDate(entity.getDateTime(), visit.get().getDateTime(), visit.get().getClinic().getId(), result);

		if (result.hasErrors()) {
			entity.setPet(visit.get().getPet());
			modelMap.addAttribute("hasClinic", true);
			return VIEWS_VISIT_CREATE_OR_UPDATE_FORM;
		}

		visit.get().setDescription(entity.getDescription());
		visit.get().setDateTime(entity.getDateTime());
		visitService.saveEntity(visit.get());
		modelMap.addAttribute("messageSuccesful", "Visit succesfully updated");
		modelMap.remove("visit", entity);
		modelMap.addAttribute("message", "Visit succesfully updated");
		return createModelVisitsList(modelMap, true, "Visit succesfully updated");
	}

	// Only owner
	@PostMapping(path = "/save")
	public String createVisit(@Valid Visit entity, BindingResult result, ModelMap model) {
		Owner owner = ownerService.findPersonByUsername(SessionUtils.obtainUserInSession().getUsername());
		if (owner == null)
			return REDIRECT_OUPS;

		model.addAttribute("visit", entity);

		result = checkDate(entity.getDateTime(), null, entity.getClinic().getId(), result);

		if (result.hasErrors()) {
			Iterable<Visit> visits = visitService.findAllByPetId(entity.getPet().getId());
			model.addAttribute("visits", visits);
			model.addAttribute("hasClinic", true);
			return VIEWS_VISIT_CREATE_OR_UPDATE_FORM;
		}

		visitService.saveEntity(entity);
		model.addAttribute("message", "Visit succesfully created");
		return REDIRECT_VISIT_LIST_BY_OWNER;
	}

	@GetMapping(value = "/listByOwner")
	public String listAllPendingAndAcceptedByOwner(final ModelMap modelMap) {
		Owner owner = ownerService.findPersonByUsername(SessionUtils.obtainUserInSession().getUsername());
		if (owner == null)
			return REDIRECT_OUPS;

		Collection<Visit> visitsPending = visitService.findAllPendingByOwnerId(owner.getId());
		Collection<Visit> visitsAccepted = visitService.findAllAcceptedByOwnerId(owner.getId());

		modelMap.addAttribute("visitsPending", visitsPending);
		modelMap.addAttribute("visitsAccepted", visitsAccepted);

		return "visits/listByOwner";
	}

	private String createModelVisitsList(ModelMap model, boolean status, String message) {
		Vet vet = vetService.findPersonByUsername(SessionUtils.obtainUserInSession().getUsername());
		if (vet == null)
			return REDIRECT_OUPS;

		Collection<Visit> visits = status ? visitService.findAllAcceptedByVetId(vet.getId())
			: visitService.findAllPendingByVetId(vet.getId());

		model.addAttribute("visits", visits);
		model.addAttribute("accepted", status);
		model.addAttribute("message", message);

		return "visits/list";
	}

	private BindingResult checkDate(LocalDateTime newDateTime, LocalDateTime oldDateTime, int clinicId,
		BindingResult result) {
		if (newDateTime != null)
			if (newDateTime.isBefore(LocalDateTime.now().plusDays(2L)))
				result.rejectValue("dateTime", "dateInFuture", "Minimum 2 days after today");
			else if (!newDateTime.equals(oldDateTime)) {
				Iterable<Visit> visits = visitService.findAllByDateTime(newDateTime);
				Iterable<Vet> vets = vetService.findVetsByClinicId(clinicId);

				Long visitsNumber = StreamSupport.stream(visits.spliterator(), false).count();
				Long vetsNumber = StreamSupport.stream(vets.spliterator(), false).count();

				if (visitsNumber >= vetsNumber)
					result.rejectValue("dateTime", "dateNotPossible", "There are no vets available at that time");
			}

		return result;
	}

}
