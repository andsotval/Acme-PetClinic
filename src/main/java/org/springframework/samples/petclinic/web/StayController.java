
package org.springframework.samples.petclinic.web;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Stay;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.service.StayService;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
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

	@Autowired
	private StayService			stayService;

	@Autowired
	private VetService			vetService;


	@GetMapping(value = "/listAllPending")
	public String listAllPending(final ModelMap modelMap) {
		String view = "stays/list";

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		User user = (User) authentication.getPrincipal();

		Vet vet = this.vetService.findByVetByUsername(user.getUsername());

		Iterable<Stay> stays = this.stayService.findAllPendingByVet(vet);
		modelMap.addAttribute("stays", stays);
		return view;

	}

	@GetMapping(value = "/listAllAccepted")
	public String listAllAccepted(final ModelMap modelMap) {
		String view = "stays/list";

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		User user = (User) authentication.getPrincipal();

		Vet vet = this.vetService.findByVetByUsername(user.getUsername());

		Iterable<Stay> stays = this.stayService.findAllAcceptedByVet(vet);
		modelMap.addAttribute("stays", stays);
		return view;

	}

	@GetMapping(path = "/accept/{stayId}")
	public String acceptStay(@PathVariable("stayId") final int stayId, final ModelMap modelMap) {
		Stay stay = this.stayService.findById(stayId).get();

		this.stayService.acceptStay(stay);

		return "redirect:/stays/listAllAccepted";

	}

	@GetMapping(path = "/cancel/{stayId}")
	public String cancelStay(@PathVariable("stayId") final int stayId, final ModelMap modelMap) {
		Stay stay = this.stayService.findById(stayId).get();

		this.stayService.cancelStay(stay);

		return "redirect:/stays/listAllAccepted";

	}

	@GetMapping(path = "/changeDate/{stayId}")
	public String changeDateStay(@PathVariable("stayId") final int stayId, final ModelMap modelMap) {
		Stay stay = this.stayService.findById(stayId).get();
		modelMap.addAttribute("stay", stay);
		return StayController.VIEWS_STAY_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(path = "/save/{stayId}")
	public String updateStay(@PathVariable("stayId") final int stayId, @Valid final Stay entity, final BindingResult result, final ModelMap modelMap) {
		String view = StayController.VIEWS_STAY_CREATE_OR_UPDATE_FORM;

		if (!this.stayService.findById(stayId).isPresent()) {
			return "redirect:/oups";
		}

		Stay stay = this.stayService.findById(stayId).get();
		stay.setDescription(entity.getDescription());
		stay.setStartDate(entity.getStartDate());
		stay.setFinishDate(entity.getFinishDate());

		if (result.hasErrors()) {
			modelMap.addAttribute("stay", stay);
		} else if (!entity.getStartDate().isBefore(entity.getFinishDate())) {
			result.rejectValue("startDate", "startLaterFinish", "the start date cannot be later than the finish date");
			modelMap.addAttribute("stay", stay);
		} else if (entity.getFinishDate().isAfter(entity.getStartDate().plusDays(7L))) {
			result.rejectValue("finishDate", "sevenDays", "you cannot book a stay longer than seven days");
			modelMap.addAttribute("stay", stay);
		} else {
			this.stayService.save(stay);
			modelMap.addAttribute("message", "Stay succesfully updated");
			view = this.listAllAccepted(modelMap);
			return "redirect:/stays/listAllAccepted";
		}

		return view;
	}

}