/**
 * DP2 - Grupo 8
 * LAB F1.33
 * Date: 05-may-2020
 */

package org.springframework.samples.petclinic.web;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.service.PetTypeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/pettype")
public class PetTypeController {

	private PetTypeService petTypeService;


	@Autowired
	public PetTypeController(PetTypeService petTypeService) {
		this.petTypeService = petTypeService;
	}

	@GetMapping(path = "/listAvailable")
	public String listAvailable(ModelMap model) {
		Collection<PetType> petTypeList = petTypeService.findAvailable();
		model.addAttribute("pettypes", petTypeList);
		model.addAttribute("available", true);

		return "/pettype/list";
	}

	@GetMapping(path = "/listNotAvailable")
	public String listNotAvailable(ModelMap model) {
		Collection<PetType> petTypeList = petTypeService.findNotAvailable();
		model.addAttribute("pettypes", petTypeList);
		model.addAttribute("available", false);

		return "/pettype/list";
	}

	@GetMapping(path = "/new")
	public String initCreationForm(ModelMap model) {

		PetType petType = new PetType();
		petType.setAvailable(true);
		model.addAttribute("petType", petType);

		return "pettype/createOrUpdatePettypeForm";
	}

	@PostMapping(path = "/new")
	public String processCreationForm(@Valid PetType petType, BindingResult result, ModelMap model) {
		if (result.hasErrors()) {
			model.addAttribute("petType", petType);
			return "pettype/createOrUpdatePettypeForm";
		} else {
			petTypeService.saveEntity(petType);
			return "redirect:/pettype/listAvailable";
		}
	}

	@GetMapping(path = "/edit/{pettypeId}")
	public String initUpdateForm(@PathVariable("pettypeId") int petTypeId, ModelMap model) {
		PetType petType = petTypeService.findEntityById(petTypeId).get();
		model.addAttribute("petType", petType);

		return "pettype/createOrUpdatePettypeForm";
	}

	@PostMapping(path = "/edit/{pettypeId}")
	public String processUpdateForm(@PathVariable("pettypeId") int petTypeId, @Valid PetType entity,
		BindingResult result, ModelMap model) {
		PetType petType = petTypeService.findEntityById(petTypeId).get();

		petType.setName(entity.getName());

		if (result.hasErrors())
			model.addAttribute("petType", entity);
		else {
			petTypeService.saveEntity(petType);

			if (petType.getAvailable())
				return "redirect:/pettype/listAvailable";
			else
				return "redirect:/pettype/listNotAvailable";
		}

		return "pettype/createOrUpdatePettypeForm";
	}

	@GetMapping(path = "/available/{pettypeId}")
	public String available(@PathVariable("pettypeId") int petTypeId, ModelMap model) {
		PetType petType = petTypeService.findEntityById(petTypeId).get();

		petType.setAvailable(true);
		petTypeService.saveEntity(petType);

		return "redirect:/pettype/listAvailable";
	}

	@GetMapping(path = "/notAvailable/{pettypeId}")
	public String notAvailable(@PathVariable("pettypeId") int petTypeId, ModelMap model) {
		PetType petType = petTypeService.findEntityById(petTypeId).get();

		petType.setAvailable(false);
		petTypeService.saveEntity(petType);

		return "redirect:/pettype/listNotAvailable";
	}

	private String createModelPettypeList(ModelMap model, boolean available, String message) {
		Collection<PetType> petTypeList = available ? petTypeService.findAvailable()
			: petTypeService.findNotAvailable();
		model.addAttribute("pettypes", petTypeList);
		model.addAttribute("available", false);

		return "/pettype/list";
	}
}
