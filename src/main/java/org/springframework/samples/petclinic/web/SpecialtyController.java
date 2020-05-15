/**
 * DP2 - Grupo 8
 * LAB F1.33
 * Date: 05-may-2020
 */

package org.springframework.samples.petclinic.web;

import java.util.Collection;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.service.SpecialtyService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/specialty")
public class SpecialtyController {

	private static final String	VIEWS_SPECIALTY_CREATE_OR_UPDATE_FORM	= "specialty/createOrUpdateSpecialtyForm";

	private static final String	REDIRECT_OUPS							= "redirect:/oups";

	private SpecialtyService	specialtyService;


	@Autowired
	public SpecialtyController(SpecialtyService specialtyService) {
		this.specialtyService = specialtyService;
	}

	@GetMapping(path = "/listAvailable")
	public String listAvailable(ModelMap model) {
		return createModelSpecialtyList(model, true, "");
	}

	@GetMapping(path = "/listNotAvailable")
	public String listNotAvailable(ModelMap model) {
		return createModelSpecialtyList(model, false, "");
	}

	@GetMapping(path = "/new")
	public String initCreationForm(ModelMap model) {

		Specialty specialty = new Specialty();
		specialty.setAvailable(true);
		model.addAttribute("specialty", specialty);

		return VIEWS_SPECIALTY_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(path = "/new")
	public String processCreationForm(@Valid Specialty specialty, BindingResult result, ModelMap model) {
		if (result.hasErrors()) {
			model.addAttribute("specialty", specialty);
			return VIEWS_SPECIALTY_CREATE_OR_UPDATE_FORM;
		} else {
			specialtyService.saveEntity(specialty);
			return createModelSpecialtyList(model, true, "Specialty succesfully saved");
		}
	}

	@GetMapping(path = "/edit/{specialtyId}")
	public String initUpdateForm(@PathVariable("specialtyId") int specialtyId, ModelMap model) {
		Optional<Specialty> specialty = specialtyService.findEntityById(specialtyId);
		if (!specialty.isPresent())
			return REDIRECT_OUPS;

		model.addAttribute("specialty", specialty.get());

		return VIEWS_SPECIALTY_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(path = "/edit/{specialtyId}")
	public String processUpdateForm(@PathVariable("specialtyId") int specialtyId, @Valid Specialty entity,
		BindingResult result, ModelMap model) {
		Optional<Specialty> specialty = specialtyService.findEntityById(specialtyId);
		if (!specialty.isPresent())
			return REDIRECT_OUPS;

		if (result.hasErrors())
			model.addAttribute("specialty", entity);
		else {
			specialty.get().setName(entity.getName());
			specialtyService.saveEntity(specialty.get());

			if (specialty.get().getAvailable())
				return createModelSpecialtyList(model, true, "Specialty succesfully updated");
			else
				return createModelSpecialtyList(model, false, "Specialty succesfully updated");
		}

		return VIEWS_SPECIALTY_CREATE_OR_UPDATE_FORM;
	}

	@GetMapping(path = "/available/{specialtyId}")
	public String available(@PathVariable("specialtyId") int specialtyId, ModelMap model) {
		Optional<Specialty> specialty = specialtyService.findEntityById(specialtyId);
		if (!specialty.isPresent())
			return REDIRECT_OUPS;

		specialty.get().setAvailable(true);
		specialtyService.saveEntity(specialty.get());

		return createModelSpecialtyList(model, true, "Specialty succesfully updated");
	}

	@GetMapping(path = "/notAvailable/{specialtyId}")
	public String notAvailable(@PathVariable("specialtyId") int specialtyId, ModelMap model) {
		Optional<Specialty> specialty = specialtyService.findEntityById(specialtyId);
		if (!specialty.isPresent())
			return REDIRECT_OUPS;

		specialty.get().setAvailable(false);
		specialtyService.saveEntity(specialty.get());

		return createModelSpecialtyList(model, false, "Specialty succesfully updated");
	}

	private String createModelSpecialtyList(ModelMap model, boolean available, String message) {
		Collection<Specialty> specialtyList = available ? specialtyService.findAvailable()
			: specialtyService.findNotAvailable();
		model.addAttribute("specialties", specialtyList);
		model.addAttribute("available", available);
		model.addAttribute("message", message);

		return "/specialty/list";
	}
}
