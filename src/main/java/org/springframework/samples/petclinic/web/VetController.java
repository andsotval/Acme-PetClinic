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

import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Clinic;
import org.springframework.samples.petclinic.model.Manager;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.model.Vets;
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

	private final ClinicService		clinicService;
	private final VetService		vetService;
	private final ManagerService	managerService;


	@Autowired
	public VetController(VetService vetService, ClinicService clinicService, ManagerService managerService) {
		this.vetService = vetService;
		this.clinicService = clinicService;
		this.managerService = managerService;
	}

	@GetMapping(value = {
		"/vetsList"
	})
	public String showVetList(Map<String, Object> model) {
		// Here we are returning an object of type 'Vets' rather than a collection of Vet
		// objects
		// so it is simpler for Object-Xml mapping
		Vets vets = new Vets();
		vets.getVetList().addAll((Collection<Vet>) vetService.findAllEntities());
		model.put("vets", vets);
		return "vets/vetList";
	}

	//este metodo devuelve la lista de vets disponibles y la de los ya registrados de la clinica
	//a la que pertenece el manager que este conectado
	@GetMapping(path = "/vetsAvailable")
	public String vetsAvailableAndOwnList(ModelMap modelMap) {
		String vista = "vets/vetsAvailable";

		Manager manager = managerService.findPersonByUsername(SessionUtils.obtainUserInSession().getUsername());

		if (manager != null) {
			Iterable<Vet> vetsAvailable = vetService.findAvailableVets();
			modelMap.addAttribute("vets2", vetsAvailable);

			Iterable<Vet> hiredVets = vetService.findVetsByManager(manager.getId());
			modelMap.addAttribute("hiredVets", hiredVets);

			return vista;
		} else
			return "redirect:/oups";

	}

	@GetMapping(path = "/accept/{vetId}")
	public String acceptVet(@PathVariable("vetId") final int vetId, final ModelMap modelMap) {
		String returnView;

		Manager manager = managerService.findPersonByUsername(SessionUtils.obtainUserInSession().getUsername());
		Clinic clinic = clinicService.findClinicByManagerId(manager.getId());
		Vet vet = vetService.findEntityById(vetId).get();

		if (vet.getClinic() == null) {
			vet.setClinic(clinic);
			vetService.saveEntity(vet);
			returnView = "redirect:/vets/vetsAvailable";
		} else
			returnView = "redirect:/oups";

		return returnView;
	}

	@GetMapping(path = "/{vetId}")
	public String showVet(@PathVariable("vetId") int vetId, ModelMap modelMap) {
		Manager manager = managerService.findPersonByUsername(SessionUtils.obtainUserInSession().getUsername());

		if (manager != null) {
			Vet vet = vetService.findEntityById(vetId).get();
			modelMap.addAttribute("vet", vet);
			return "vets/vetDetails";
		} else
			return "redirect:/oups";
	}

}
