
package org.springframework.samples.petclinic.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Clinic;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.util.SessionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/clinics")
public class ClinicController {

	private final ClinicService	clinicService;
	private final OwnerService	ownerService;


	@Autowired
	public ClinicController(ClinicService clinicService, OwnerService ownerService) {
		this.clinicService = clinicService;
		this.ownerService = ownerService;
	}

	@GetMapping(path = "/owner")
	public String initClinicView() {
		Owner owner = obtainOwnerInSession();
		String returnView = "redirect:/clinics/owner/";

		if (owner.getClinic() == null)
			returnView += "listAvailable";
		else
			returnView += owner.getClinic().getId();

		return returnView;
	}

	@GetMapping(path = "/owner/{clinicId}")
	public String showClinic(@PathVariable("clinicId") Integer clinicId, ModelMap modelMap) {
		Owner owner = obtainOwnerInSession();
		Clinic clinic = clinicService.findEntityById(clinicId).get();

		modelMap.addAttribute("clinic", clinic);
		modelMap.addAttribute("owner", owner);

		return "/clinics/owner/clinicDetails";
	}

	@GetMapping(path = "/owner/unsubscribeFromClinic")
	public String unsubscribeOwnerFromClinic(ModelMap modelMap) {
		Owner owner = obtainOwnerInSession();
		Clinic clinic = owner.getClinic();
		clinic = clinicService.findEntityById(clinic.getId()).get();

		if (clinic != null) {
			owner.setClinic(null);
			ownerService.saveEntity(owner);
			return "redirect:/clinics/owner";
		} else {
			modelMap.addAttribute("message", "Este propietario no esta dado de alta en ninguna clínica");
			return "redirect:/oups";
		}
	}

	@GetMapping(path = "/owner/listAvailable")
	public String listAvailable(ModelMap modelMap) {
		Iterable<Clinic> clinicList = clinicService.findAllEntities();
		modelMap.addAttribute("clinics", clinicList);
		return "/clinics/owner/clinicsList";
	}

	@GetMapping(path = "/owner/subscribeToClinic/{clinicId}")
	public String subscribeToClinic(@PathVariable("clinicId") Integer clinicId, ModelMap modelMap) {
		Owner owner = obtainOwnerInSession();
		Clinic clinic = clinicService.findEntityById(clinicId).get();

		String returnView;
		if (owner.getClinic() == null) {
			owner.setClinic(clinic);
			ownerService.saveEntity(owner);

			returnView = "redirect:/clinics/owner";
		} else {
			modelMap.addAttribute("message",
				"No es posible dar de alta al propietario a dicha clinica porque ya esta dado de alta a una clinica");
			returnView = "redirect:/oups";
		}

		return returnView;
	}

	private Owner obtainOwnerInSession() {
		Owner owner = ownerService.findPersonByUsername(SessionUtils.obtainUserInSession().getUsername());
		return ownerService.findEntityById(owner.getId()).get();
	}

}
