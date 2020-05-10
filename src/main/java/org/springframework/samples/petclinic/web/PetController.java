/**
 * DP2 - Grupo 8
 * LAB F1.33
 * Date: 05-may-2020
 */

package org.springframework.samples.petclinic.web;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

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

	private static final String	REDIRECT_OUPS						= "redirect:/oups";

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
	public Collection<PetType> populatePetTypes() {
		return petTypeService.findAvailable();
	}

	@GetMapping(path = "/listMyPets")
	public String listMyPets(ModelMap modelMap) {
		return createModelPetList(modelMap, "");
	}

	@GetMapping(value = "/new")
	public String newPet(ModelMap model) {
		Owner owner = ownerService.findPersonByUsername(SessionUtils.obtainUserInSession().getUsername());
		if (owner == null)
			return REDIRECT_OUPS;

		Pet pet = new Pet();
		pet.setOwner(owner);
		model.addAttribute("pet", pet);
		return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(path = "/save")
	public String savePet(@Valid Pet pet, BindingResult result, ModelMap modelMap) {
		Owner owner = ownerService.findPersonByUsername(SessionUtils.obtainUserInSession().getUsername());
		if (owner == null)
			return REDIRECT_OUPS;

		if (!owner.getId().equals(pet.getOwner().getId()))
			return REDIRECT_OUPS;

		boolean hasErrors = false;

		if (result.hasErrors()) {
			modelMap.addAttribute("pet", pet);
			hasErrors = true;
		}
		if (pet.getBirthDate() == null) {
			modelMap.addAttribute("pet", pet);
			result.rejectValue("birthDate", "birthDateNotNull", "is required");
			hasErrors = true;
		} else if (pet.getBirthDate().isAfter(LocalDate.now())) {
			modelMap.addAttribute("pet", pet);
			result.rejectValue("birthDate", "birthDateFuture", "the birth date cannot be in future");
			hasErrors = true;
		}
		if (pet.getType() == null) {
			modelMap.addAttribute("pet", pet);
			result.rejectValue("type", "petTypeNotNull", "is required");
			hasErrors = true;
		}

		if (hasErrors)
			return VIEWS_PETS_CREATE_OR_UPDATE_FORM;

		petService.saveEntity(pet);

		return createModelPetList(modelMap, "Pet succesfully saved");
	}

	@GetMapping(path = "/delete/{petId}")
	public String deletePet(@PathVariable("petId") int petId, ModelMap modelMap) {
		Owner owner = ownerService.findPersonByUsername(SessionUtils.obtainUserInSession().getUsername());
		if (owner == null)
			return REDIRECT_OUPS;

		Optional<Pet> pet = petService.findEntityById(petId);
		if (!pet.isPresent())
			return REDIRECT_OUPS;

		if (!owner.getId().equals(pet.get().getOwner().getId()))
			return REDIRECT_OUPS;

		stayService.deleteByPetId(pet.get().getId());
		visitService.deleteByPetId(pet.get().getId());
		petService.deleteEntityById(pet.get().getId());

		return createModelPetList(modelMap, "Pet succesfully deleted");

	}

	@GetMapping(value = "/newVisit/{petId}")
	public String newVisit(@PathVariable("petId") int petId, ModelMap model) {
		Owner owner = ownerService.findPersonByUsername(SessionUtils.obtainUserInSession().getUsername());
		if (owner == null)
			return REDIRECT_OUPS;

		Optional<Pet> pet = petService.findEntityById(petId);
		if (!pet.isPresent())
			return REDIRECT_OUPS;

		if (!owner.getId().equals(pet.get().getOwner().getId()))
			return REDIRECT_OUPS;

		Visit visit = new Visit();
		Clinic clinic = owner.getClinic();
		if (clinic != null) {
			visit.setClinic(clinic);
			model.addAttribute("clinicId", clinic.getId());
			model.addAttribute("hasClinic", true);
		} else
			model.addAttribute("hasClinic", false);

		visit.setPet(pet.get());
		model.addAttribute("visit", visit);
		Collection<Visit> visits = visitService.findAllByPetId(petId);
		model.addAttribute("visits", visits);

		return "visits/createOrUpdateVisitForm";
	}

	@GetMapping(value = "/newStay/{petId}")
	public String newStay(@PathVariable("petId") int petId, ModelMap model) {
		Owner owner = ownerService.findPersonByUsername(SessionUtils.obtainUserInSession().getUsername());
		if (owner == null)
			return REDIRECT_OUPS;

		Optional<Pet> pet = petService.findEntityById(petId);
		if (!pet.isPresent())
			return REDIRECT_OUPS;

		if (!owner.getId().equals(pet.get().getOwner().getId()))
			return REDIRECT_OUPS;

		Stay stay = new Stay();
		Clinic clinic = owner.getClinic();
		if (clinic != null) {
			stay.setClinic(clinic);
			model.addAttribute("hasClinic", true);
		} else
			model.addAttribute("hasClinic", false);

		stay.setPet(pet.get());
		model.addAttribute("stay", stay);
		Collection<Stay> stays = stayService.findAllStayByPet(petId);
		model.addAttribute("stays", stays);
		return "stays/createOrUpdateStayForm";
	}

	@GetMapping(value = "/{petId}/edit")
	public String initUpdateForm(@PathVariable("petId") int petId, ModelMap model) {
		Owner owner = ownerService.findPersonByUsername(SessionUtils.obtainUserInSession().getUsername());
		if (owner == null)
			return REDIRECT_OUPS;

		Optional<Pet> pet = petService.findEntityById(petId);
		if (!pet.isPresent())
			return REDIRECT_OUPS;

		if (!owner.getId().equals(pet.get().getOwner().getId()))
			return REDIRECT_OUPS;

		model.addAttribute("pet", pet.get());
		return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
	}

	private String createModelPetList(ModelMap model, String message) {
		Owner owner = ownerService.findPersonByUsername(SessionUtils.obtainUserInSession().getUsername());
		if (owner == null)
			return REDIRECT_OUPS;

		Collection<Pet> pets = petService.findPetsByOwnerId(owner.getId());
		model.addAttribute("pets", pets);
		model.addAttribute("message", message);

		return "pets/list";
	}

}
