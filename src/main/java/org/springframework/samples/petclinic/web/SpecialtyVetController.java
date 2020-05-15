/**
 * DP2 - Grupo 8
 * LAB F1.33
 * Date: 05-may-2020
 */

package org.springframework.samples.petclinic.web;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.service.SpecialtyService;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.samples.petclinic.util.SessionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/specialty/vet")
public class SpecialtyVetController {

	private static final String	REDIRECT_OUPS	= "redirect:/oups";

	private SpecialtyService	specialtyService;

	private VetService			vetService;


	@Autowired
	public SpecialtyVetController(SpecialtyService specialtyService, VetService vetService) {
		this.specialtyService = specialtyService;
		this.vetService = vetService;
	}

	@GetMapping(path = "/list")
	public String list(ModelMap modelMap) {
		return createModelSpecialtyList(modelMap, "");
	}

	@GetMapping(path = "/add/{specialtyId}")
	public String add(@PathVariable("specialtyId") Integer specialtyId, ModelMap modelMap) {
		Vet vet = vetService.findPersonByUsername(SessionUtils.obtainUserInSession().getUsername());
		if (vet == null)
			return REDIRECT_OUPS;

		Optional<Specialty> specialty = specialtyService.findEntityById(specialtyId);
		if (!specialty.isPresent())
			return REDIRECT_OUPS;

		vet.addSpecialty(specialty.get());
		vetService.saveEntity(vet);

		return createModelSpecialtyList(modelMap, "Specialty succesfully added");
	}

	@GetMapping(path = "/remove/{specialtyId}")
	public String remove(@PathVariable("specialtyId") Integer specialtyId, ModelMap modelMap) {
		Vet vet = vetService.findPersonByUsername(SessionUtils.obtainUserInSession().getUsername());
		if (vet == null)
			return REDIRECT_OUPS;

		Optional<Specialty> specialty = specialtyService.findEntityById(specialtyId);
		if (!specialty.isPresent())
			return REDIRECT_OUPS;

		vet.removeSpecialty(specialty.get());
		vetService.saveEntity(vet);

		return createModelSpecialtyList(modelMap, "Specialty succesfully removed");
	}

	private String createModelSpecialtyList(ModelMap model, String message) {
		Vet vet = vetService.findPersonByUsername(SessionUtils.obtainUserInSession().getUsername());
		if (vet == null)
			return REDIRECT_OUPS;

		Collection<Specialty> specialties = specialtyService.findAvailable();
		model.addAttribute("specialties", specialties);

		Collection<Specialty> mySpecialties = vet.getSpecialties();
		model.addAttribute("mySpecialties", mySpecialties);

		model.addAttribute("message", message);
		return "/specialty/vet/list";
	}
}
