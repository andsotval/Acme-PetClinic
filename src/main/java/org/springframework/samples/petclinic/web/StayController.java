package org.springframework.samples.petclinic.web;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Stay;
import org.springframework.samples.petclinic.service.StayService;
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
	
	@Autowired
	private StayService stayService;
	
	@GetMapping(value = "/listAllPending")
	public String listAllPending(final ModelMap modelMap) {
		String view = "stays/list";
		Iterable<Stay> stays = this.stayService.findAllPending();
		modelMap.addAttribute("stays", stays);
		return view;
		
	}
	
	@GetMapping(value = "/listAllAccepted")
	public String listAllAccepted(final ModelMap modelMap) {
		String view = "stays/list";
		Iterable<Stay> stays = this.stayService.findAllbyAcceptance(true);
		modelMap.addAttribute("stays", stays);
		return view;
		
	}

	@GetMapping(path = "/accept/{stayId}")
	public String acceptStay(@PathVariable("stayId") final int stayId, final ModelMap modelMap) {
		Stay stay = this.stayService.findById(stayId);

		this.stayService.acceptStay(stay);

		return "redirect:/stays/listAllAccepted";

	}
	
	@GetMapping(path = "/cancel/{stayId}")
	public String cancelStay(@PathVariable("stayId") final int stayId, final ModelMap modelMap) {
		Stay stay = this.stayService.findById(stayId);

		this.stayService.cancelStay(stay);

		return "redirect:/stays/listAllAccepted";

	}
	
	@GetMapping(path = "/changeDate/{stayId}")
	public String changeDateStay(@PathVariable("stayId") final int stayId, final ModelMap modelMap) {
		Stay stay = this.stayService.findById(stayId);
		modelMap.addAttribute("stay", stay);
		return "/stays/createOrUpdateStayForm";
	}
	
	@PostMapping(path = "/save")
	public String updateStay(@Valid Stay stay, BindingResult result, final ModelMap modelMap) {
		String view = "/stays/createOrUpdateStayForm";
		if(result.hasErrors()) {
			modelMap.addAttribute("stay", stay);
			return view;
		}else {
			stay.setIsAccepted(true);
			this.stayService.save(stay);
			modelMap.addAttribute("message", "Stay succesfully updated");
			view = listAllAccepted(modelMap);
			return "redirect:/stays/listAllAccepted";
		}
	}
	
}
