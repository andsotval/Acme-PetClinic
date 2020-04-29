
package org.springframework.samples.petclinic.web;

import java.time.LocalDateTime;
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
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/visits")
public class VisitController {

	private VisitService visitService;

	private VetService vetService;

	private OwnerService ownerService;

	private AuthoritiesService authoritiesService;

	private static final String VIEWS_VISIT_CREATE_OR_UPDATE_FORM = "/visits/createOrUpdateVisitForm";

	@Autowired
	public VisitController(final VisitService visitService, final VetService vetService,
			final OwnerService ownerService, final AuthoritiesService authoritiesService) {
		this.visitService = visitService;
		this.vetService = vetService;
		this.ownerService = ownerService;
		this.authoritiesService = authoritiesService;
	}

	@InitBinder
	public void setAllowedFields(final WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@GetMapping(value = "/listAllPending")
	public String listAllPending(final ModelMap modelMap) {
		String view = "visits/list";

		Vet vet = vetService.findPersonByUsername(SessionUtils.obtainUserInSession().getUsername());

		if (vet != null) {
			Iterable<Visit> visits = visitService.findAllPendingByVetId(vet.getId());

			modelMap.addAttribute("visits", visits);
			modelMap.addAttribute("accepted", false);
			return view;
		} else {
			return "redirect:/oups";
		}
	}

	@GetMapping(value = "/listAllAccepted")
	public String listAllAccepted(final ModelMap modelMap) {
		String view = "visits/list";

		Vet vet = vetService.findPersonByUsername(SessionUtils.obtainUserInSession().getUsername());

		if (vet != null) {
			Iterable<Visit> visits = visitService.findAllAcceptedByVetId(vet.getId());
			modelMap.addAttribute("visits", visits);
			modelMap.addAttribute("accepted", true);
			return view;
		} else {
			return "redirect:/oups";
		}
	}

	@GetMapping(path = "/accept/{visitId}")
	public String acceptVisit(@PathVariable("visitId") final int visitId, final ModelMap modelMap) {
		Visit visit = visitService.findEntityById(visitId).get();

		Vet vet = vetService.findPersonByUsername(SessionUtils.obtainUserInSession().getUsername());

		if (visit.getClinic().getId() == vet.getClinic().getId()) {
			visit.setIsAccepted(true);
			visitService.saveEntity(visit);
		} else {
			modelMap.addAttribute("nonAuthorized", true);
		}
		return "redirect:/visits/listAllAccepted";

	}

	@GetMapping(path = "/cancel/{visitId}")
	public String cancelVisit(@PathVariable("visitId") final int visitId, final ModelMap modelMap) {
		Visit visit = visitService.findEntityById(visitId).get();

		String username = SessionUtils.obtainUserInSession().getUsername();
		String authority = authoritiesService.findAuthorityByUsername(username);

		if (authority.contains("vet")) {
			Vet vet = vetService.findPersonByUsername(username);

			if (visit.getClinic().getId() == vet.getClinic().getId()) {
				visit.setIsAccepted(false);
				visitService.saveEntity(visit);
			} else {
				modelMap.addAttribute("notAuthorizedVet", true);
			}
			return "redirect:/visits/listAllAccepted";
		}

		if (authority.contains("owner")) {
			Owner owner = ownerService.findPersonByUsername(username);

			if (visit.getPet().getOwner().getId() == owner.getId()) {
				visit.setIsAccepted(false);
				visitService.saveEntity(visit);
			} else {
				modelMap.addAttribute("notAuthorizedOwner", true);
			}
			return "redirect:/visits/listByOwner";
		}

		return "";
	}

	@GetMapping(path = "/changeDate/{visitId}")
	public String initUpdateVisit(@PathVariable("visitId") final int visitId, final ModelMap modelMap) {

		if (!visitService.findEntityById(visitId).isPresent()) {
			return "redirect:/oups";
		} else {
			Visit visit = visitService.findEntityById(visitId).get();
			modelMap.addAttribute(visit);
			return "/visits/createOrUpdateVisitForm";
		}

	}

	@PostMapping(path = "/save/{visitId}")
	public String updateVisit(@PathVariable("visitId") final int visitId, @Valid final Visit entity,
			final BindingResult result, final ModelMap modelMap) {

		String view = VisitController.VIEWS_VISIT_CREATE_OR_UPDATE_FORM;

		if (!visitService.findEntityById(visitId).isPresent())
			return "redirect:/oups";

		Vet vet = vetService.findPersonByUsername(SessionUtils.obtainUserInSession().getUsername());

		Visit visit = visitService.findEntityById(visitId).get();

		int i = 0;

		if (result.hasErrors()) {
			modelMap.addAttribute("visit", entity);
			i++;
		}

		if (visit.getClinic().getId() != vet.getClinic().getId()) {
			modelMap.addAttribute("visit", entity);
			result.rejectValue("authorized", "notAuthorized", "You are not authorized");
			i++;
		}

		if (entity.getDateTime() == null) {
			modelMap.addAttribute("visit", entity);
			result.rejectValue("dateTime", "dateNotNull", "is required");
			i++;
		} else if (entity.getDateTime().isBefore(LocalDateTime.now().plusDays(2L))) {
			modelMap.addAttribute("visit", entity);
			result.rejectValue("dateTime", "dateInFuture", "Minimum 2 days after today");
			i++;
		} else if (!entity.getDateTime().equals(visit.getDateTime())) {

			Iterable<Visit> visits = visitService.findAllByDateTime(entity.getDateTime());

			Iterable<Vet> vets = vetService.findVetsByClinicId(visit.getClinic().getId());

			Long visitsNumber = StreamSupport.stream(visits.spliterator(), false).count();

			Long vetsNumber = StreamSupport.stream(vets.spliterator(), false).count();

			if (visitsNumber >= vetsNumber) {
				modelMap.addAttribute("visit", entity);
				result.rejectValue("dateTime", "dateNotPossible", "There are no vets available at that time");
				i++;
			}
		}

		if (i == 0) {
			visit.setDescription(entity.getDescription());
			visit.setDateTime(entity.getDateTime());
			visitService.saveEntity(visit);
			modelMap.addAttribute("messageSuccesful", "Visit succesfully updated");
			return listAllAccepted(modelMap);
		} else {
			entity.setId(visit.getId());
			entity.setPet(visit.getPet());
		}

		return view;
	}

	@PostMapping(path = "/save")
	public String createVisit(@Valid final Visit entity, final BindingResult result, final ModelMap model) {

		String view = "/visits/createOrUpdateVisitForm";

		int i = 0;

		if (result.hasErrors()) {
			model.addAttribute("visit", entity);
			i++;
		}

		if (entity.getDateTime() == null) {
			model.addAttribute("visit", entity);
			result.rejectValue("dateTime", "dateNotNull", "is required");
			i++;
		} else if (entity.getDateTime().isBefore(LocalDateTime.now().plusDays(2L))) {
			model.addAttribute("visit", entity);
			result.rejectValue("dateTime", "dateInFuture", "Minimum 2 days after today");
			i++;
		} else {

			Iterable<Visit> visits = visitService.findAllByDateTime(entity.getDateTime());

			Iterable<Vet> vets = vetService.findVetsByClinicId(entity.getClinic().getId());

			Long visitsNumber = StreamSupport.stream(visits.spliterator(), false).count();

			Long vetsNumber = StreamSupport.stream(vets.spliterator(), false).count();

			if (visitsNumber >= vetsNumber) {
				model.addAttribute("visit", entity);
				result.rejectValue("dateTime", "dateNotPossible", "There are no vets available at that time");
				i++;
			}
		}

		if (i == 0) {
			visitService.saveEntity(entity);
			model.addAttribute("message", "Visit succesfully updated");
			return "redirect:/visits/listByOwner";
		} else {
			Iterable<Visit> visits = visitService.findAllByPetId(entity.getPet().getId());
			model.addAttribute("visits", visits);
			model.addAttribute("clinicId", entity.getPet().getOwner().getClinic().getId());
		}

		return view;
	}

	@GetMapping(value = "/listByOwner")
	public String listAllPendingAndAcceptedByOwner(final ModelMap modelMap) {
		String view = "visits/listByOwner";

		Owner owner = ownerService.findPersonByUsername(SessionUtils.obtainUserInSession().getUsername());

		if (owner != null) {
			Iterable<Visit> visitsPending = visitService.findAllPendingByOwnerId(owner.getId());

			Iterable<Visit> visitsAccepted = visitService.findAllAcceptedByOwnerId(owner.getId());

			modelMap.addAttribute("visitsPending", visitsPending);

			modelMap.addAttribute("visitsAccepted", visitsAccepted);

			return view;
		} else {
			return "redirect:/oups";
		}

	}

}
