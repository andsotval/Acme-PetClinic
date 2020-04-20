
package org.springframework.samples.petclinic.web;

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

	private static final String	VIEWS_PETTYPE_CREATE_OR_UPDATE_FORM	= "/pettype/createOrUpdatePettypeForm";

	private PetTypeService		petTypeService;


	@Autowired
	public PetTypeController(PetTypeService petTypeService) {
		this.petTypeService = petTypeService;
	}

	@GetMapping(path = "/list")
	public String list(ModelMap modelMap) {
		Iterable<PetType> petTypeList = petTypeService.findAvailable();
		modelMap.addAttribute("pettypes", petTypeList);

		return "/pettype/list";
	}

	@GetMapping(path = "/new")
	public String initCreationForm(ModelMap model) {

		model.addAttribute("pettype", new PetType());

		return VIEWS_PETTYPE_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(path = "/new")
	public String processCreationForm(@Valid PetType pettype, BindingResult result, ModelMap model) {
		if (result.hasErrors())
			return VIEWS_PETTYPE_CREATE_OR_UPDATE_FORM;
		else {
			petTypeService.saveEntity(pettype);
			return list(model);
		}
	}

	@GetMapping(path = "/edit/{pettypeId}")
	public String initUpdateForm(@PathVariable("pettypeId") int pettypeId, ModelMap model) {
		PetType petType = petTypeService.findEntityById(pettypeId).get();
		model.addAttribute("pettype", petType);

		return VIEWS_PETTYPE_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(path = "/edit/{pettypeId}")
	public String processUpdateForm(@PathVariable("pettypeId") int pettypeId, @Valid PetType entity,
		BindingResult result, ModelMap model) {
		PetType petType = petTypeService.findEntityById(pettypeId).get();

		petType.setName(entity.getName());

		if (result.hasErrors())
			model.addAttribute("pettype", petType);
		else {
			petTypeService.saveEntity(petType);
			return list(model);
		}

		return VIEWS_PETTYPE_CREATE_OR_UPDATE_FORM;
	}

	@GetMapping(path = "/delete/{pettypeId}")
	public String delete(@PathVariable("pettypeId") int pettypeId, ModelMap model) {
		PetType petType = petTypeService.findEntityById(pettypeId).get();

		petType.setAvailable(false);
		petTypeService.saveEntity(petType);

		return list(model);
	}
}
