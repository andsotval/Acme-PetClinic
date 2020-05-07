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

	private static final String	VIEWS_PETTYPE_CREATE_OR_UPDATE_FORM	= "pettype/createOrUpdatePettypeForm";

	private static final String	REDIRECT_OUPS						= "redirect:/oups";

	private PetTypeService		petTypeService;


	@Autowired
	public PetTypeController(PetTypeService petTypeService) {
		this.petTypeService = petTypeService;
	}

	@GetMapping(path = "/listAvailable")
	public String listAvailable(ModelMap model) {
		return createModelPettypeList(model, true, "");
	}

	@GetMapping(path = "/listNotAvailable")
	public String listNotAvailable(ModelMap model) {
		return createModelPettypeList(model, false, "");
	}

	@GetMapping(path = "/new")
	public String initCreationForm(ModelMap model) {

		PetType petType = new PetType();
		petType.setAvailable(true);
		model.addAttribute("petType", petType);

		return VIEWS_PETTYPE_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(path = "/new")
	public String processCreationForm(@Valid PetType petType, BindingResult result, ModelMap model) {
		if (result.hasErrors()) {
			model.addAttribute("petType", petType);
			return VIEWS_PETTYPE_CREATE_OR_UPDATE_FORM;
		} else {
			petTypeService.saveEntity(petType);
			return createModelPettypeList(model, true, "Pet type succesfully saved");
		}
	}

	@GetMapping(path = "/edit/{pettypeId}")
	public String initUpdateForm(@PathVariable("pettypeId") int petTypeId, ModelMap model) {
		Optional<PetType> petType = petTypeService.findEntityById(petTypeId);
		if (!petType.isPresent())
			return REDIRECT_OUPS;

		model.addAttribute("petType", petType.get());

		return VIEWS_PETTYPE_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(path = "/edit/{pettypeId}")
	public String processUpdateForm(@PathVariable("pettypeId") int petTypeId, @Valid PetType entity,
		BindingResult result, ModelMap model) {
		Optional<PetType> petType = petTypeService.findEntityById(petTypeId);
		if (!petType.isPresent())
			return REDIRECT_OUPS;

		if (result.hasErrors())
			model.addAttribute("petType", entity);
		else {
			petType.get().setName(entity.getName());
			petTypeService.saveEntity(petType.get());

			if (petType.get().getAvailable())
				return createModelPettypeList(model, true, "Pet type succesfully updated");
			else
				return createModelPettypeList(model, false, "Pet type succesfully updated");
		}

		return VIEWS_PETTYPE_CREATE_OR_UPDATE_FORM;
	}

	@GetMapping(path = "/available/{pettypeId}")
	public String available(@PathVariable("pettypeId") int petTypeId, ModelMap model) {
		Optional<PetType> petType = petTypeService.findEntityById(petTypeId);
		if (!petType.isPresent())
			return REDIRECT_OUPS;

		petType.get().setAvailable(true);
		petTypeService.saveEntity(petType.get());

		return createModelPettypeList(model, true, "Pet type succesfully updated");
	}

	@GetMapping(path = "/notAvailable/{pettypeId}")
	public String notAvailable(@PathVariable("pettypeId") int petTypeId, ModelMap model) {
		Optional<PetType> petType = petTypeService.findEntityById(petTypeId);
		if (!petType.isPresent())
			return REDIRECT_OUPS;

		petType.get().setAvailable(false);
		petTypeService.saveEntity(petType.get());

		return createModelPettypeList(model, false, "Pet type succesfully updated");
	}

	private String createModelPettypeList(ModelMap model, boolean available, String message) {
		Collection<PetType> petTypeList = available ? petTypeService.findAvailable()
			: petTypeService.findNotAvailable();
		model.addAttribute("pettypes", petTypeList);
		model.addAttribute("available", available);
		model.addAttribute("message", message);

		return "/pettype/list";
	}
}
