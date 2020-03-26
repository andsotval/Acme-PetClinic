package org.springframework.samples.petclinic.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Stay;
import org.springframework.samples.petclinic.service.StayService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/stays")
public class StayController {
	
	@Autowired
	private StayService stayService;
	
	@GetMapping(value = "/listAllPending")
	public String listAllPending(final ModelMap modelMap) {
		String view = "stays/listAllPending";
		Iterable<Stay> stays = this.stayService.findAllPending();
		modelMap.addAttribute("stays", stays);
		return view;
		
	}
	
	@GetMapping(value = "/listAllAccepted")
	public String listAllAccepted(final ModelMap modelMap) {
		String view = "stays/listAllAccepted";
		Iterable<Stay> stays = this.stayService.findAllAccepted();
		modelMap.addAttribute("stays", stays);
		return view;
		
	}

}
