/**
 * DP2 - Grupo 8
 * LAB F1.33
 * Date: 05-may-2020
 */

package org.springframework.samples.petclinic.web;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Clinic;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.StayService;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.samples.petclinic.service.VisitService;
import org.springframework.samples.petclinic.util.SessionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/clinics")
public class ClinicController {

	private static final String	REDIRECT_OUPS	= "redirect:/oups";

	private ClinicService		clinicService;
	private OwnerService		ownerService;
	private VetService			vetService;
	private StayService			stayService;
	private VisitService		visitService;
	private AuthoritiesService	authoritiesService;


	@Autowired
	public ClinicController(ClinicService clinicService, OwnerService ownerService, VetService vetService,
		VisitService visitService, StayService stayService, AuthoritiesService authoritiesService) {
		this.clinicService = clinicService;
		this.ownerService = ownerService;
		this.vetService = vetService;
		this.visitService = visitService;
		this.stayService = stayService;
		this.authoritiesService = authoritiesService;
	}

	@GetMapping(value = "/getDetail")
	public String getDetail(ModelMap modelMap) {
		String username = SessionUtils.obtainUserInSession().getUsername();
		String authority = authoritiesService.findAuthorityByUsername(username);
		if (authority == null)
			return REDIRECT_OUPS;

		Clinic clinic = null;

		if (authority.equals("veterinarian")) {
			Vet vet = vetService.findPersonByUsername(username);
			if (vet == null)
				return REDIRECT_OUPS;

			clinic = vet.getClinic();
			if (clinic == null) {
				//TODO DESARROLLAR EN EL SPRINT 3
				modelMap.addAttribute("developing", "We are very sorry, but this section is not yet available");
				return "/clinics/clinicDetails";
			}

			List<Owner> owners = ownerService.findOwnersByClinicId(clinic.getId());
			modelMap.addAttribute("owners", owners);
		}

		else if (authority.equals("owner")) {
			Owner owner = ownerService.findPersonByUsername(username);
			if (owner == null)
				return REDIRECT_OUPS;

			modelMap.addAttribute("owner", owner);
			clinic = owner.getClinic();

			Boolean notUnsubscribe = !canUnsubscribe(owner.getId());
			modelMap.addAttribute("notUnsubscribe", notUnsubscribe);

		}

		else
			return REDIRECT_OUPS;

		modelMap.addAttribute("clinic", clinic);
		return "/clinics/clinicDetails";

	}

	@GetMapping(path = "/owner")
	public String initClinicView(ModelMap modelMap) {
		Owner owner = ownerService.findPersonByUsername(SessionUtils.obtainUserInSession().getUsername());
		if (owner == null)
			return REDIRECT_OUPS;

		if (owner.getClinic() == null) {
			Iterable<Clinic> clinicList = clinicService.findAllEntities();
			modelMap.addAttribute("clinics", clinicList);
			return "/clinics/owner/clinicsList";
		} else
			return "redirect:/clinics/getDetail";
	}

	@GetMapping(path = "/owner/{clinicId}")
	public String showClinic(@PathVariable("clinicId") Integer clinicId, ModelMap modelMap) {
		Owner owner = ownerService.findPersonByUsername(SessionUtils.obtainUserInSession().getUsername());
		if (owner == null)
			return REDIRECT_OUPS;

		Optional<Clinic> clinic = clinicService.findEntityById(clinicId);
		if (!clinic.isPresent())
			return REDIRECT_OUPS;

		if (owner.getClinic() != null)
			if (!clinic.get().getId().equals(owner.getClinic().getId()))
				return REDIRECT_OUPS;

		modelMap.addAttribute("clinic", clinic.get());
		modelMap.addAttribute("owner", owner);

		return "/clinics/owner/clinicDetails";
	}

	@GetMapping(path = "/owner/unsubscribeFromClinic")
	public String unsubscribeOwnerFromClinic(ModelMap modelMap) {
		Owner owner = ownerService.findPersonByUsername(SessionUtils.obtainUserInSession().getUsername());
		if (owner == null)
			return REDIRECT_OUPS;

		Clinic clinic = owner.getClinic();
		if (clinic == null)
			return REDIRECT_OUPS;

		clinic = clinicService.findEntityById(clinic.getId()).get();

		if (clinic != null && canUnsubscribe(owner.getId())) {
			owner.setClinic(null);
			ownerService.saveEntity(owner);
			return initClinicView(modelMap);
		} else if (clinic != null && !canUnsubscribe(owner.getId())) {
			modelMap.addAttribute("message", "This owner has a pending or accepted visit or stay");
			return "redirect:/clinics/getDetail";
		} else {
			modelMap.addAttribute("message", "This owner is not registered in any clinic");
			return "redirect:/clinics/getDetail";
		}
	}

	@GetMapping(path = "/owner/subscribeToClinic/{clinicId}")
	public String subscribeToClinic(@PathVariable("clinicId") Integer clinicId, ModelMap modelMap) {
		Owner owner = ownerService.findPersonByUsername(SessionUtils.obtainUserInSession().getUsername());
		if (owner == null)
			return REDIRECT_OUPS;

		Optional<Clinic> clinic = clinicService.findEntityById(clinicId);
		if (!clinic.isPresent())
			return REDIRECT_OUPS;

		if (owner.getClinic() == null) {
			owner.setClinic(clinic.get());
			ownerService.saveEntity(owner);

			return initClinicView(modelMap);
		} else
			return REDIRECT_OUPS;

	}

	private boolean canUnsubscribe(Integer ownerId) {
		return visitService.canUnsubscribe(ownerId) && stayService.canUnsubscribe(ownerId);
	}

}
