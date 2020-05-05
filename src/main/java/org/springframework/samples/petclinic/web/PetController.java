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
import org.springframework.samples.petclinic.model.Clinic;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Stay;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.PetTypeService;
import org.springframework.samples.petclinic.service.StayService;
import org.springframework.samples.petclinic.service.VisitService;
import org.springframework.samples.petclinic.util.SessionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/pets")
public class PetController {

	private static final String	VIEWS_PETS_CREATE_OR_UPDATE_FORM	= "pets/createOrUpdatePetForm";

	private PetTypeService		petTypeService;
	private PetService			petService;
	private VisitService		visitService;
	private StayService			stayService;
	private OwnerService		ownerService;


	@Autowired
	public PetController(PetService petService, OwnerService ownerService, VisitService visitService,
		StayService stayService, PetTypeService petTypeService) {
		this.petService = petService;
		this.petTypeService = petTypeService;
		this.ownerService = ownerService;
		this.stayService = stayService;
		this.visitService = visitService;
	}

	@ModelAttribute("types")
	public Iterable<PetType> populatePetTypes() {
		return petTypeService.findAvailable();
	}

	@GetMapping(path = "/listMyPets")
	public String listMyPets(ModelMap modelMap) {
		String view = "pets/list";

		Owner owner = ownerService.findPersonByUsername(SessionUtils.obtainUserInSession().getUsername());

		Iterable<Pet> pets = petService.findPetsByOwnerId(owner.getId());

		modelMap.addAttribute("pets", pets);
		modelMap.addAttribute("ownerId", owner.getId());

		return view;
	}

	@GetMapping(value = "/new/{ownerId}")
	public String newPet(@PathVariable("ownerId") int ownerId, ModelMap model) {
		Pet pet = new Pet();
		Owner owner = ownerService.findEntityById(ownerId).get();
		pet.setOwner(owner);
		model.addAttribute("pet", pet);
		return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(path = "/save")
	public String savePet(@Valid Pet pet, BindingResult result, ModelMap modelMap) {
		String view = VIEWS_PETS_CREATE_OR_UPDATE_FORM;

		Owner owner = ownerService.findPersonByUsername(SessionUtils.obtainUserInSession().getUsername());

		int i = 0;
		if (result.hasErrors()) {
			modelMap.addAttribute("pet", pet);
			i++;
		}
		if (pet.getOwner().getId() != owner.getId()) {
			//			Exception e = new Exception("No estás autorizado");
			//			modelMap.addAttribute("exception", e);
			view = "redirect:/oups";
			i++;
		}
		if (pet.getBirthDate() == null) {
			modelMap.addAttribute("pet", pet);
			result.rejectValue("birthDate", "birthDateNotNull", "is required");
			i++;

		} else if (pet.getBirthDate().isAfter(LocalDate.now())) {
			modelMap.addAttribute("pet", pet);
			result.rejectValue("birthDate", "birthDateFuture", "the birth date cannot be in future");
			i++;
		}

		if (pet.getType() == null) {
			modelMap.addAttribute("pet", pet);
			result.rejectValue("type", "petTypeNotNull", "is required");
			i++;

		}

		if (i == 0) {
			petService.saveEntity(pet);
			modelMap.addAttribute("message", "Pet succesfully saved");
			return "redirect:/pets/listMyPets";
		}

		return view;
	}

	@GetMapping(path = "/delete/{petId}")
	public String deletePet(@PathVariable("petId") int petId, ModelMap modelMap) {
		Pet pet = petService.findEntityById(petId).get();

		Owner owner = ownerService.findPersonByUsername(SessionUtils.obtainUserInSession().getUsername());

		if (pet.getOwner().getId() == owner.getId()) {
			stayService.deleteByPetId(pet.getId());
			visitService.deleteByPetId(pet.getId());
			petService.deleteEntityById(pet.getId());
		} else
			modelMap.addAttribute("nonAuthorized", "No estás autorizado");
		return "redirect:/pets/listMyPets";

	}

	@GetMapping(value = "/newVisit/{petId}")
	public String newVisit(@PathVariable("petId") int petId, ModelMap model) {
		Visit visit = new Visit();
		Pet pet = petService.findEntityById(petId).get();
		Clinic c = pet.getOwner().getClinic();
		if (c != null) {
			visit.setClinic(pet.getOwner().getClinic());
			model.addAttribute("clinicId", pet.getOwner().getClinic().getId());
		} else
			model.addAttribute("hasClinic", false);
		visit.setPet(pet);
		model.addAttribute("visit", visit);
		Iterable<Visit> visits = visitService.findAllByPetId(petId);
		model.addAttribute("visits", visits);

		return "visits/createOrUpdateVisitForm";
	}

	@GetMapping(value = "/newStay/{petId}")
	public String newStay(@PathVariable("petId") int petId, ModelMap model) {
		Stay stay = new Stay();
		Pet pet = petService.findEntityById(petId).get();
		Clinic c = pet.getOwner().getClinic();
		if (c != null) {
			stay.setClinic(pet.getOwner().getClinic());
			model.addAttribute("clinicId", pet.getOwner().getClinic().getId());
		} else
			model.addAttribute("hasClinic", false);
		stay.setPet(pet);
		model.addAttribute("stay", stay);
		Iterable<Stay> stays = stayService.findAllStayByPet(petId);
		model.addAttribute("stays", stays);
		return "stays/createOrUpdateStayForm";
	}

	@GetMapping(value = "/{petId}/edit")
	public String initUpdateForm(@PathVariable("petId") int petId, ModelMap model) {
		Pet pet = petService.findEntityById(petId).get();
		model.addAttribute("pet", pet);
		return PetController.VIEWS_PETS_CREATE_OR_UPDATE_FORM;
	}

	// Estos métodos ya estaban en PetClinic pero de momento no los usamos
	//	@InitBinder("owner")
	//	public void initOwnerBinder(WebDataBinder dataBinder) {
	//		dataBinder.setDisallowedFields("id");
	//	}
	//
	//	@InitBinder("pet")
	//	public void initPetBinder(WebDataBinder dataBinder) {
	//		dataBinder.setValidator(new PetValidator());
	//	}

}
