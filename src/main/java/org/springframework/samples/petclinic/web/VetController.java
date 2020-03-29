/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Clinic;
import org.springframework.samples.petclinic.model.Manager;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.model.Vets;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

import javax.validation.Valid;

/**
 * @author Juergen Hoeller
 * @author Mark Fisher
 * @author Ken Krebs
 * @author Arjen Poutsma
 */
@Controller
@RequestMapping("/vets")
public class VetController {
	
	private static final String VIEWS_VET_CREATE_OR_UPDATE_FORM = "vets/createOrUpdateVetForm";
	
	private final ClinicService clinicService;
	private final VetService vetService;

	@Autowired
	public VetController(VetService clinicService) {
		this.vetService = clinicService;
	}

	@GetMapping(value = { "/vetsList" })
	public String showVetList(Map<String, Object> model) {
		// Here we are returning an object of type 'Vets' rather than a collection of Vet
		// objects
		// so it is simpler for Object-Xml mapping
		Vets vets = new Vets();
		vets.getVetList().addAll(this.vetService.findVets());
		model.put("vets", vets);
		return "vets/vetList";
	}

	@GetMapping(value = { "/vets.xml"})
	public @ResponseBody Vets showResourcesVetList() {
		// Here we are returning an object of type 'Vets' rather than a collection of Vet
		// objects
		// so it is simpler for JSon/Object mapping
		Vets vets = new Vets();
		vets.getVetList().addAll(this.vetService.findVets());
		return vets;
	}
	
	@GetMapping(value = "/vetsAvailable")
	public String vetsAvailableList(ModelMap modelMap) {
		String vista="vets/vetsAvailable";
		Iterable<Vet> vets= vetService.findAvailableVets();
		modelMap.addAttribute("vets2", vets);
		return vista;
	}
	
	@GetMapping(value = "/addVet/{vetId}")
	public String initAddVetToClinic(@PathVariable("vetId") Integer vetId, Model model) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User user = (User) authentication.getPrincipal();
		Clinic clinic = this.clinicService.findClinicByName(clinic.getClinic()).get();
		Vet vet = this.vetService.findVetById(vetId).get();
		vet.setClinic(clinic);

		model.addAttribute(vet);
		model.addAttribute(clinic);

		return VIEWS_VET_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/addVet/{vetId}")
	public String proccessAddProviderToManager(@Valid Vet vet, @Valid Clinic clinic, BindingResult result,
			@PathVariable("vetId") Integer vetId) {

		if (vet != null && clinic != null) {
			clinic.setId(vet.getClinic().getId());
			
			vet.getUser().setId(this.vetService.findVetById(vetId).get().getUser().getId());
			vet.setClinic(clinic);
			vet.setId(vetId);
			
			this.vetService.saveProvider(vet);
		} else {
			return VIEWS_VET_CREATE_OR_UPDATE_FORM;
		}

		return "redirect:/vets/listAvailable";
	}
	
	
	
	

} 
