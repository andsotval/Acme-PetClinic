/**
 * DP2 - Grupo 8
 * LAB F1.33
 * Date: 05-may-2020
 */

package org.springframework.samples.petclinic.web;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Clinic;
import org.springframework.samples.petclinic.model.Manager;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.samples.petclinic.service.ManagerService;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.samples.petclinic.util.SessionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Juergen Hoeller
 * @author Mark Fisher
 * @author Ken Krebs
 * @author Arjen Poutsma
 */
@Controller
@RequestMapping("/vets")
public class VetController {

	private static final String	REDIRECT_OUPS			= "redirect:/oups";

	private static final String	VIEWS_VETS_AVAILABLE	= "vets/vetsAvailable";

	private ClinicService		clinicService;

	private VetService			vetService;

	private ManagerService		managerService;


	@Autowired
	public VetController(VetService vetService, ClinicService clinicService, ManagerService managerService) {
		this.vetService = vetService;
		this.clinicService = clinicService;
		this.managerService = managerService;
	}

	//este metodo devuelve la lista de vets disponibles y la de los ya registrados de la clinica
	//a la que pertenece el manager que este conectado
	@GetMapping(path = "/vetsAvailable")
	public String vetsAvailableAndOwnList(ModelMap modelMap) {
		return createModelVetsList(modelMap, "");
	}

	@GetMapping(path = "/accept/{vetId}")
	public String acceptVet(@PathVariable("vetId") int vetId, ModelMap modelMap) {
		Optional<Vet> vet = vetService.findEntityById(vetId);
		if (!vet.isPresent())
			return REDIRECT_OUPS;

		Manager manager = managerService.findPersonByUsername(SessionUtils.obtainUserInSession().getUsername());
		if (manager == null)
			return REDIRECT_OUPS;

		Clinic clinic = clinicService.findClinicByManagerId(manager.getId());

		if (vet.get().getClinic() == null) {
			vet.get().setClinic(clinic);
			vetService.saveEntity(vet.get());
			return createModelVetsList(modelMap, "Vet succesfully accepted");
		} else
			return REDIRECT_OUPS;
	}

	@GetMapping(path = "/{vetId}")
	public String showVet(@PathVariable("vetId") int vetId, ModelMap modelMap) {
		Optional<Vet> vet = vetService.findEntityById(vetId);
		if (!vet.isPresent())
			return REDIRECT_OUPS;

		Manager manager = managerService.findPersonByUsername(SessionUtils.obtainUserInSession().getUsername());
		if (manager == null)
			return REDIRECT_OUPS;

		modelMap.addAttribute("vet", vet.get());
		return "vets/vetDetails";
	}

	private String createModelVetsList(ModelMap model, String message) {
		Manager manager = managerService.findPersonByUsername(SessionUtils.obtainUserInSession().getUsername());
		if (manager == null)
			return REDIRECT_OUPS;

		Collection<Vet> vetsAvailable = vetService.findAvailableVets();
		model.addAttribute("vets2", vetsAvailable);

		Collection<Vet> hiredVets = vetService.findVetsByManager(manager.getId());
		model.addAttribute("hiredVets", hiredVets);

		model.addAttribute("message", message);
		return VIEWS_VETS_AVAILABLE;
	}

}
