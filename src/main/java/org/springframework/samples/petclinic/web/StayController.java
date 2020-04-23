
package org.springframework.samples.petclinic.web;

import java.time.LocalDate;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Stay;
import org.springframework.samples.petclinic.model.Vet;
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

	private static final String	VIEWS_STAY_CREATE_OR_UPDATE_FORM	= "/stays/createOrUpdateStayForm";

	private StayService			stayService;

	private VetService			vetService;
	
	private OwnerService		ownerService;


	@Autowired
	public StayController(StayService stayService, VetService vetService, OwnerService ownerService) {
		this.stayService = stayService;
		this.vetService = vetService;
		this.ownerService = ownerService;
	}


	@GetMapping(path = "/listAllPending")
	public String listAllPending(final ModelMap modelMap) {
		String view = "stays/list";

		Vet vet = vetService.findPersonByUsername(SessionUtils.obtainUserInSession().getUsername());

		Iterable<Stay> stays = stayService.findAllPendingByVet(vet);
		modelMap.addAttribute("stays", stays);
		modelMap.addAttribute("accepted", false);
		return view;

	}

	@GetMapping(path = "/listAllAccepted")
	public String listAllAccepted(final ModelMap modelMap) {
		String view = "stays/list";

		Vet vet = vetService.findPersonByUsername(SessionUtils.obtainUserInSession().getUsername());

		Iterable<Stay> stays = stayService.findAllAcceptedByVet(vet);
		modelMap.addAttribute("stays", stays);
		modelMap.addAttribute("accepted", true);
		return view;

	}

	@GetMapping(path = "/accept/{stayId}")
	public String acceptStay(@PathVariable("stayId") final int stayId, final ModelMap modelMap) {
		Stay stay = stayService.findEntityById(stayId).get();

		Vet vet = vetService.findPersonByUsername(SessionUtils.obtainUserInSession().getUsername());

		if (stay.getClinic().getId() == vet.getClinic().getId()) {
			stay.setIsAccepted(true);
			stayService.saveEntity(stay);
		} else
			modelMap.addAttribute("nonAuthorized", "No estás autorizado");

		return "redirect:/stays/listAllAccepted";

	}

	@GetMapping(path = "/cancel/{stayId}")
	public String cancelStay(@PathVariable("stayId") final int stayId, final ModelMap modelMap) {
		Stay stay = stayService.findEntityById(stayId).get();

		Vet vet = vetService.findPersonByUsername(SessionUtils.obtainUserInSession().getUsername());

		if (stay.getClinic().getId() == vet.getClinic().getId()) {
			stay.setIsAccepted(false);
			stayService.saveEntity(stay);
		} else
			modelMap.addAttribute("nonAuthorized", "No estás autorizado");

		return "redirect:/stays/listAllAccepted";

	}

	@GetMapping(path = "/changeDate/{stayId}")
	public String changeDateStay(@PathVariable("stayId") final int stayId, final ModelMap modelMap) {
		Stay stay = stayService.findEntityById(stayId).get();
		modelMap.addAttribute("stay", stay);
		return StayController.VIEWS_STAY_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(path = "/save/{stayId}")
	public String updateStay(@PathVariable("stayId") int stayId, @Valid Stay entity, BindingResult result,
		ModelMap modelMap) {

		String view = StayController.VIEWS_STAY_CREATE_OR_UPDATE_FORM;

		if (!stayService.findEntityById(stayId).isPresent())
			return "redirect:/oups";

		Vet vet = vetService.findPersonByUsername(SessionUtils.obtainUserInSession().getUsername());

		Stay stay = stayService.findEntityById(stayId).get();
		stay.setDescription(entity.getDescription());
		stay.setStartDate(entity.getStartDate());
		stay.setFinishDate(entity.getFinishDate());

		if (result.hasErrors())
			modelMap.addAttribute("stay", entity);
		else if (stay.getClinic().getId() != vet.getClinic().getId())
			modelMap.addAttribute("nonAuthorized", "No estás autorizado");
		else if (!entity.getStartDate().isBefore(entity.getFinishDate())) {
			modelMap.addAttribute("stay", entity);
			result.rejectValue("startDate", "startLaterFinish", "the start date cannot be later than the finish date");
		} else if (entity.getFinishDate().isAfter(entity.getStartDate().plusDays(7L))) {
			modelMap.addAttribute("stay", entity);
			result.rejectValue("finishDate", "sevenDays", "you cannot book a stay longer than seven days");
		} else {
			stayService.saveEntity(stay);
			modelMap.addAttribute("message", "Stay succesfully updated");
			return listAllAccepted(modelMap);
		}

		return view;
	}

	@PostMapping(path = "/save")
	public String newStay(@Valid final Stay entity, final BindingResult result, final ModelMap model) {

		String view = "stays/createOrUpdateStayForm";
		
		int i = 0;
		
		if (result.hasErrors()) {
			model.addAttribute("stay", entity);
			i++;
		}
		if(entity.getStartDate() == null) {
			model.addAttribute("stay", entity);
			result.rejectValue("startDate", "startDateNotNull", "is required");
			i++;
		}else {
			if(entity.getStartDate().isBefore(LocalDate.now().plusDays(2L))) {
				model.addAttribute("stay", entity);
				result.rejectValue("startDate", "startFuturePlus2Days", "Minimum 2 days after today");
				i++;
			}
		}
		
		if(entity.getFinishDate() == null) {
			model.addAttribute("stay", entity);
			result.rejectValue("finishDate", "finishDateNotNull", "is required");
			i++;
		}else {
			if(entity.getFinishDate().isAfter(entity.getStartDate().plusDays(7L))) {
				model.addAttribute("stay", entity);
				result.rejectValue("finishDate", "finishDateMinimumOneWeek", "Stays cannot last longer than one week");
				i++;
			}
			if(entity.getFinishDate().isBefore(entity.getStartDate())) {
				model.addAttribute("stay", entity);
				result.rejectValue("finishDate", "finishDateAfteStartDate", "The finish date must be after the start date");
				i++;
			}
		}
		
		if(i == 0) {
			stayService.saveEntity(entity);
			model.addAttribute("message", "Stay succesfully updated");
			return "redirect:/stays/listByOwner";
		}else {
			Iterable<Stay> stays = stayService.findAllStayByPet(entity.getPet().getId());
			model.addAttribute("stays", stays);
			model.addAttribute("clinicId", entity.getPet().getOwner().getClinic().getId());
		}

		return view;
	}
	
	@GetMapping(value = "/listByOwner")
	public String listAllPendingByOwner(final ModelMap modelMap) {
		String view = "stays/listByOwner";

		Owner owner = this.ownerService.findPersonByUsername(SessionUtils.obtainUserInSession().getUsername());

		Iterable<Stay> staysPending = this.stayService.findAllPendingByOwner(owner);
		
		Iterable<Stay> staysAccepted = this.stayService.findAllAcceptedByOwner(owner);

		modelMap.addAttribute("staysPending", staysPending);
		
		modelMap.addAttribute("staysAccepted", staysAccepted);

		return view;

	}
}
