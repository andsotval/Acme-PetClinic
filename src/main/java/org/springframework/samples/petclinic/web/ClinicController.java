package org.springframework.samples.petclinic.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Clinic;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.VetService;
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
  private VetService		vetService;


	@Autowired
	public ClinicController(ClinicService clinicService, OwnerService ownerService, VetService vetService) {
		this.clinicService = clinicService;
		this.ownerService = ownerService;
    this.vetService = vetService;
	}
  
  @GetMapping(value = "/getDetail")
	public String listAllPending(final ModelMap modelMap) {
		String view = "clinics/show";
    
		Vet vet = vetService.findPersonByUsername(SessionUtils.obtainUserInSession().getUsername());

		Clinic clinic = vet.getClinic();

		modelMap.addAttribute("clinic", clinic);
		return view;

	}

	@GetMapping(path = "/owner")
	public String initClinicView(ModelMap modelMap) {
		Owner owner = obtainOwnerInSession();
		String returnView;
		modelMap.addAttribute("owner", owner);

		if (owner.getClinic() == null)
			returnView = listAvailable(modelMap);
		else
			//			returnView = "clinics/owner/" + owner.getClinic().getId();
			returnView = showClinic(owner.getId(), modelMap);

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
			return initClinicView(modelMap);
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

			returnView = initClinicView(modelMap);
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