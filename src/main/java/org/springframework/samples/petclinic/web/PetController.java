/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.samples.petclinic.web;

import java.time.LocalDate;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/pets")
public class PetController {

	private static final String	VIEWS_PETS_CREATE_OR_UPDATE_FORM	= "pets/createOrUpdatePetForm";

	private final PetService	petService;
	private final OwnerService	ownerService;


	@Autowired
	public PetController(final PetService petService, final OwnerService ownerService) {
		this.petService = petService;
		this.ownerService = ownerService;
	}

	@ModelAttribute("types")
	public Iterable<PetType> populatePetTypes() {
		return petService.findPetTypes();
	}

	@GetMapping(path = "/listMyPets")
	public String listMyPets(final ModelMap modelMap) {
		String view = "pets/list";

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		User user = (User) authentication.getPrincipal();

		Owner owner = ownerService.findByOwnerByUsername(user.getUsername()).get();

		Iterable<Pet> pets = petService.findPetsByOwnerId(owner.getId());

		modelMap.addAttribute("pets", pets);
		modelMap.addAttribute("ownerId", owner.getId());

		return view;
	}

	@GetMapping(value = "/new/{ownerId}")
	public String newPet(@PathVariable("ownerId") int ownerId, final ModelMap model) {
		Pet pet = new Pet();
		Owner owner = ownerService.findEntityById(ownerId).get();
		owner.addPet(pet);
		model.addAttribute("pet", pet);
		return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(path = "/save")
	public String savePet(@Valid final Pet pet, final BindingResult result, final ModelMap modelMap) {
		String view = "pets/listMyPets";

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		User user = (User) authentication.getPrincipal();

		Owner owner = ownerService.findByOwnerByUsername(user.getUsername()).get();

		//esto hay que verlo
		pet.setOwner(owner);

		if (pet.getOwner().getId() != owner.getId())
			modelMap.addAttribute("nonAuthorized", "No estás autorizado");
		else if (pet.getBirthDate().isAfter(LocalDate.now())) {
			result.rejectValue("birthDate", "birthDateFuture", "the birth date cannot be in future");
			modelMap.addAttribute("pet", pet);
		} else {
			petService.saveEntity(pet);
			modelMap.addAttribute("message", "Stay succesfully updated");
			return "redirect:/pets/listMyPets";
		}

		return view;
	}

	@GetMapping(path = "/delete/{petId}")
	public String cancelVisit(@PathVariable("petId") final int petId, final ModelMap modelMap) {
		Pet pet = petService.findEntityById(petId).get();

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		User user = (User) authentication.getPrincipal();

		Owner owner = ownerService.findByOwnerByUsername(user.getUsername()).get();

		if (pet.getOwner().getId() == owner.getId())
			petService.deleteEntity(pet);
		else
			modelMap.addAttribute("nonAuthorized", "No estás autorizado");
		return "redirect:/pets/listMyPets";

	}

	@GetMapping(value = "/newVisit/{petId}")
	public String newVisit(@PathVariable("petId") int petId, final ModelMap model) {
		Visit visit = new Visit();
		Pet pet = petService.findEntityById(petId).get();
		visit.setClinic(pet.getOwner().getClinic());
		visit.setPet(pet);
		model.addAttribute("visit", visit);
		return "pets/createVisitForm";
	}

	//	@ModelAttribute("types")
	//	public Collection<PetType> populatePetTypes() {
	//		return this.petService.findPetTypes();
	//	}
	//
	//	@ModelAttribute("owner")
	//	public Owner findOwner(@PathVariable("ownerId") final int ownerId) {
	//		return this.ownerService.findOwnerById(ownerId);
	//	}

	/*
	 * @ModelAttribute("pet")
	 * public Pet findPet(@PathVariable("petId") Integer petId) {
	 * Pet result=null;
	 * if(petId!=null)
	 * result=this.clinicService.findPetById(petId);
	 * else
	 * result=new Pet();
	 * return result;
	 * }
	 */

	@InitBinder("owner")
	public void initOwnerBinder(final WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@InitBinder("pet")
	public void initPetBinder(final WebDataBinder dataBinder) {
		dataBinder.setValidator(new PetValidator());
	}

	//	@GetMapping(value = "/pets/new")
	//	public String initCreationForm(Owner owner, ModelMap model) {
	//		Pet pet = new Pet();
	//		owner.addPet(pet);
	//		model.put("pet", pet);
	//		return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
	//	}
	//
	//	@PostMapping(value = "/pets/new")
	//	public String processCreationForm(Owner owner, @Valid Pet pet, BindingResult result, ModelMap model) {
	//		if (result.hasErrors()) {
	//			model.put("pet", pet);
	//			return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
	//		}
	//		else {
	//                    try{
	//                    	owner.addPet(pet);
	//                    	this.petService.savePet(pet);
	//                    }catch(DuplicatedPetNameException ex){
	//                        result.rejectValue("name", "duplicate", "already exists");
	//                        return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
	//                    }
	//                    return "redirect:/owners/{ownerId}";
	//		}
	//	}

	@GetMapping(value = "/pets/{petId}/edit")
	public String initUpdateForm(@PathVariable("petId") final int petId, final ModelMap model) {
		Pet pet = petService.findEntityById(petId).get();
		model.put("pet", pet);
		return PetController.VIEWS_PETS_CREATE_OR_UPDATE_FORM;
	}

	/**
	 *
	 * @param pet
	 * @param result
	 * @param petId
	 * @param model
	 * @param owner
	 * @param model
	 * @return
	 */
	//	@PostMapping(value = "/pets/{petId}/edit")
	//	public String processUpdateForm(@Valid final Pet pet, final BindingResult result, final Owner owner, @PathVariable("petId") final int petId, final ModelMap model) {
	//		if (result.hasErrors()) {
	//			model.put("pet", pet);
	//			return PetController.VIEWS_PETS_CREATE_OR_UPDATE_FORM;
	//		} else {
	//			Pet petToUpdate = this.petService.findPetById(petId);
	//			BeanUtils.copyProperties(pet, petToUpdate, "id", "owner", "visits");
	//			try {
	//				this.petService.savePet(petToUpdate);
	//			} catch (DuplicatedPetNameException ex) {
	//				result.rejectValue("name", "duplicate", "already exists");
	//				return PetController.VIEWS_PETS_CREATE_OR_UPDATE_FORM;
	//			}
	//			return "redirect:/owners/{ownerId}";
	//		}
	//	}

}
