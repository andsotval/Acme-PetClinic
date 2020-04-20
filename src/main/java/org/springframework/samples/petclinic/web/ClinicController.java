
package org.springframework.samples.petclinic.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Clinic;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/clinics")
public class ClinicController {

	@Autowired
	private ClinicService	clinicService;

	@Autowired
	private VetService		vetService;


	@GetMapping(value = "/getDetail")
	public String listAllPending(final ModelMap modelMap) {
		String view = "clinics/show";
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		User user = (User) authentication.getPrincipal();

		Vet vet = this.vetService.findByVetByUsername(user.getUsername());

		Clinic clinic = vet.getClinic();

		modelMap.addAttribute("clinic", clinic);
		return view;

	}

}
