/**
 * DP2 - Grupo 8
 * LAB F1.33
 * Date: 05-may-2020
 */

package org.springframework.samples.petclinic.web;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Stay;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.StayService;
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
@RequestMapping("/stays")
public class StayController {

	private static final String	VIEWS_STAY_CREATE_OR_UPDATE_FORM	= "stays/createOrUpdateStayForm";

	private static final String	REDIRECT_OUPS						= "redirect:/oups";

	private static final String	REDIRECT_STAYS_LIST_ALL_ACCEPTED	= "redirect:/stays/listAllAccepted";

	private static final String	REDIRECT_STAYS_LIST_BY_OWNER		= "redirect:/stays/listByOwner";

	private StayService			stayService;

	private VetService			vetService;

	private OwnerService		ownerService;

	private AuthoritiesService	authoritiesService;


	@Autowired
	public StayController(StayService stayService, VetService vetService, OwnerService ownerService,
		AuthoritiesService authoritiesService) {
		this.stayService = stayService;
		this.vetService = vetService;
		this.ownerService = ownerService;
		this.authoritiesService = authoritiesService;
	}

	@GetMapping(path = "/listAllPending")
	public String listAllPending(ModelMap modelMap) {
		return createModelStaysList(modelMap, false, "");
	}

	@GetMapping(path = "/listAllAccepted")
	public String listAllAccepted(ModelMap modelMap) {
		return createModelStaysList(modelMap, true, "");
	}

	@GetMapping(path = "/accept/{stayId}")
	public String acceptStay(@PathVariable("stayId") int stayId, ModelMap modelMap) {
		Vet vet = vetService.findPersonByUsername(SessionUtils.obtainUserInSession().getUsername());
		if (vet == null)
			return REDIRECT_OUPS;

		Optional<Stay> stay = stayService.findEntityById(stayId);
		if (!stay.isPresent())
			return REDIRECT_OUPS;

		if (!stay.get().getClinic().getId().equals(vet.getClinic().getId()))
			return REDIRECT_OUPS;

		stay.get().setIsAccepted(true);
		stayService.saveEntity(stay.get());

		return REDIRECT_STAYS_LIST_ALL_ACCEPTED;
	}

	@GetMapping(path = "/cancel/{stayId}")
	public String cancelStay(@PathVariable("stayId") int stayId, ModelMap modelMap) {
		Optional<Stay> stay = stayService.findEntityById(stayId);
		if (!stay.isPresent())
			return REDIRECT_OUPS;

		String username = SessionUtils.obtainUserInSession().getUsername();
		String authority = authoritiesService.findAuthorityByUsername(username);

		if (authority.equals("veterinarian")) {
			Vet vet = vetService.findPersonByUsername(username);
			if (vet == null)
				return REDIRECT_OUPS;

			if (!stay.get().getClinic().getId().equals(vet.getClinic().getId()))
				return REDIRECT_OUPS;

			stay.get().setIsAccepted(false);
			stayService.saveEntity(stay.get());

			return REDIRECT_STAYS_LIST_ALL_ACCEPTED;
		}

		else if (authority.equals("owner")) {
			Owner owner = ownerService.findPersonByUsername(username);
			if (owner == null)
				return REDIRECT_OUPS;

			if (!stay.get().getPet().getOwner().getId().equals(owner.getId()))
				return REDIRECT_OUPS;

			stay.get().setIsAccepted(false);
			stayService.saveEntity(stay.get());

			return REDIRECT_STAYS_LIST_BY_OWNER;
		}

		return REDIRECT_OUPS;
	}

	// Only vet
	@GetMapping(path = "/changeDate/{stayId}")
	public String changeDateStay(@PathVariable("stayId") int stayId, ModelMap modelMap) {
		Vet vet = vetService.findPersonByUsername(SessionUtils.obtainUserInSession().getUsername());
		if (vet == null)
			return REDIRECT_OUPS;

		Optional<Stay> stay = stayService.findEntityById(stayId);
		if (!stay.isPresent())
			return REDIRECT_OUPS;

		if (!stay.get().getClinic().getId().equals(vet.getClinic().getId()))
			return REDIRECT_OUPS;

		modelMap.addAttribute("stay", stay.get());
		modelMap.addAttribute("hasClinic", true);

		return VIEWS_STAY_CREATE_OR_UPDATE_FORM;
	}

	// Only vet
	@PostMapping(path = "/save/{stayId}")
	public String updateStay(@PathVariable("stayId") int stayId, @Valid Stay entity, BindingResult result,
		ModelMap model) {
		Vet vet = vetService.findPersonByUsername(SessionUtils.obtainUserInSession().getUsername());
		if (vet == null)
			return REDIRECT_OUPS;

		Optional<Stay> stay = stayService.findEntityById(stayId);
		if (!stay.isPresent())
			return REDIRECT_OUPS;

		if (!stay.get().getClinic().getId().equals(vet.getClinic().getId()))
			return REDIRECT_OUPS;

		model.addAttribute("stay", entity);

		result = checkStartDate(entity.getStartDate(), result);
		result = checkFinishDate(entity.getStartDate(), entity.getFinishDate(), result);

		if (result.hasErrors()) {
			entity.setPet(stay.get().getPet());
			model.addAttribute("hasClinic", true);
			return VIEWS_STAY_CREATE_OR_UPDATE_FORM;
		}

		stay.get().setDescription(entity.getDescription());
		stay.get().setStartDate(entity.getStartDate());
		stay.get().setFinishDate(entity.getFinishDate());
		stayService.saveEntity(stay.get());
		model.remove("stay", entity);
		model.addAttribute("message", "Stay succesfully updated");
		return createModelStaysList(model, true, "Stay succesfully updated");
	}

	// Only owner
	@PostMapping(path = "/save")
	public String newStay(@Valid Stay entity, BindingResult result, ModelMap model) {
		Owner owner = ownerService.findPersonByUsername(SessionUtils.obtainUserInSession().getUsername());
		if (owner == null)
			return REDIRECT_OUPS;

		model.addAttribute("stay", entity);

		result = checkStartDate(entity.getStartDate(), result);
		result = checkFinishDate(entity.getStartDate(), entity.getFinishDate(), result);

		if (result.hasErrors()) {
			Collection<Stay> stays = stayService.findAllStayByPet(entity.getPet().getId());
			model.addAttribute("stays", stays);
			model.addAttribute("hasClinic", true);
			return VIEWS_STAY_CREATE_OR_UPDATE_FORM;
		}

		stayService.saveEntity(entity);
		model.addAttribute("message", "Stay succesfully updated");
		return REDIRECT_STAYS_LIST_BY_OWNER;

	}

	@GetMapping(value = "/listByOwner")
	public String listAllPendingByOwner(ModelMap modelMap) {
		Owner owner = ownerService.findPersonByUsername(SessionUtils.obtainUserInSession().getUsername());
		if (owner == null)
			return REDIRECT_OUPS;

		Collection<Stay> staysPending = stayService.findAllPendingByOwner(owner.getId());
		Collection<Stay> staysAccepted = stayService.findAllAcceptedByOwner(owner.getId());

		modelMap.addAttribute("staysPending", staysPending);
		modelMap.addAttribute("staysAccepted", staysAccepted);

		return "stays/listByOwner";

	}

	private String createModelStaysList(ModelMap model, boolean status, String message) {
		Vet vet = vetService.findPersonByUsername(SessionUtils.obtainUserInSession().getUsername());
		if (vet == null)
			return REDIRECT_OUPS;

		Collection<Stay> stays = status ? stayService.findAllAcceptedByVet(vet.getId())
			: stayService.findAllPendingByVet(vet.getId());

		model.addAttribute("stays", stays);
		model.addAttribute("accepted", status);
		model.addAttribute("message", message);

		return "stays/list";
	}

	private BindingResult checkStartDate(LocalDate date, BindingResult result) {
		if (date != null)
			if (date.isBefore(LocalDate.now().plusDays(2L)))
				result.rejectValue("startDate", "startFuturePlus2Days", "Minimum 2 days after today");

		return result;
	}

	private BindingResult checkFinishDate(LocalDate startDate, LocalDate finishDate, BindingResult result) {
		if (finishDate != null)
			if (startDate != null) {
				if (finishDate.isAfter(startDate.plusDays(7L)))
					result.rejectValue("finishDate", "finishDateMinimumOneWeek",
						"Stays cannot last longer than one week");
				if (finishDate.isBefore(startDate.plusDays(1L)))
					result.rejectValue("finishDate", "finishDateAfterStartDate",
						"The finish date must be after the start date");
			}

		return result;
	}

}
