
package org.springframework.samples.petclinic.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Clinic;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Stay;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.model.Visit;
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

	private final ClinicService	clinicService;
	private final OwnerService	ownerService;
	private VetService			vetService;
	private StayService			stayService;
	private VisitService		visitService;


	@Autowired
	public ClinicController(final ClinicService clinicService, OwnerService ownerService, VetService vetService,
		VisitService visitService, StayService stayService) {
		this.clinicService = clinicService;
		this.ownerService = ownerService;
		this.vetService = vetService;
		this.visitService = visitService;
		this.stayService = stayService;
	}

	@GetMapping(value = "/getDetail")
	public String getDetail(final ModelMap modelMap) {
		String view = "/clinics/clinicDetails";

		List<String> roles = SessionUtils.obtainRoleUserInSession();

		Clinic clinic = null;

		if (roles.contains("veterinarian")) {
			Vet vet = vetService.findPersonByUsername(SessionUtils.obtainUserInSession().getUsername());
			clinic = vet.getClinic();
			List<Owner> owners = ownerService.findOwnersByClinicId(clinic.getId());
			modelMap.addAttribute("owners", owners);
		} else if (roles.contains("owner")) {
			Owner owner = ownerService.findPersonByUsername(SessionUtils.obtainUserInSession().getUsername());
			modelMap.addAttribute("owner", owner);
			clinic = owner.getClinic();

			List<Visit> visitsAccepted = (List<Visit>) visitService.findAllAcceptedByOwnerId(owner.getId());

			List<Visit> visitsPending = (List<Visit>) visitService.findAllPendingByOwnerId(owner.getId());

			List<Stay> staysAccepted = (List<Stay>) stayService.findAllAcceptedByOwner(owner);

			List<Stay> stayPending = (List<Stay>) stayService.findAllPendingByOwner(owner);

			Boolean notUnsubscribe = visitsAccepted.size() > 0 || visitsPending.size() > 0 || staysAccepted.size() > 0
				|| stayPending.size() > 0;

			modelMap.addAttribute("notUnsubscribe", notUnsubscribe);

		}

		modelMap.addAttribute("clinic", clinic);
		return view;

	}

	@GetMapping(path = "/owner")
	public String initClinicView(final ModelMap modelMap) {
		Owner owner = obtainOwnerInSession();
		String returnView;
		modelMap.addAttribute("owner", owner);

		if (owner.getClinic() == null)
			returnView = listAvailable(modelMap);
		else
			//			returnView = "clinics/owner/" + owner.getClinic().getId();
			returnView = "redirect:/clinics/getDetail";

		return returnView;
	}

	@GetMapping(path = "/owner/{clinicId}")
	public String showClinic(@PathVariable("clinicId") final Integer clinicId, final ModelMap modelMap) {
		Owner owner = obtainOwnerInSession();
		Clinic clinic = clinicService.findEntityById(clinicId).get();

		modelMap.addAttribute("clinic", clinic);
		modelMap.addAttribute("owner", owner);

		return "/clinics/owner/clinicDetails";
	}

	@GetMapping(path = "/owner/unsubscribeFromClinic")
	public String unsubscribeOwnerFromClinic(final ModelMap modelMap) {
		Owner owner = obtainOwnerInSession();
		Clinic clinic = owner.getClinic();
		clinic = clinicService.findEntityById(clinic.getId()).get();

		List<Visit> visitsAccepted = (List<Visit>) visitService.findAllAcceptedByOwnerId(owner.getId());

		List<Visit> visitsPending = (List<Visit>) visitService.findAllPendingByOwnerId(owner.getId());

		List<Stay> staysAccepted = (List<Stay>) stayService.findAllAcceptedByOwner(owner);

		List<Stay> stayPending = (List<Stay>) stayService.findAllPendingByOwner(owner);

		Boolean notUnsubscribe = visitsAccepted.size() > 0 || visitsPending.size() > 0 || staysAccepted.size() > 0
			|| stayPending.size() > 0;

		if (clinic != null && !notUnsubscribe) {
			owner.setClinic(null);
			ownerService.saveEntity(owner);
			return initClinicView(modelMap);
		} else if (clinic != null && notUnsubscribe) {
			modelMap.addAttribute("message", "Este propietario tiene alguna visit o stay pendiente o aceptada");
			return "redirect:/clinics/getDetail";
		} else {
			modelMap.addAttribute("message", "Este propietario no esta dado de alta en ninguna clínica");
			return "redirect:/clinics/getDetail";
		}
	}

	@GetMapping(path = "/owner/listAvailable")
	public String listAvailable(final ModelMap modelMap) {
		Iterable<Clinic> clinicList = clinicService.findAllEntities();
		modelMap.addAttribute("clinics", clinicList);
		return "/clinics/owner/clinicsList";
	}

	@GetMapping(path = "/owner/subscribeToClinic/{clinicId}")
	public String subscribeToClinic(@PathVariable("clinicId") final Integer clinicId, final ModelMap modelMap) {
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
