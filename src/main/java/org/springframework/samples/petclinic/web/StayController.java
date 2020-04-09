
package org.springframework.samples.petclinic.web;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.samples.petclinic.model.Stay;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.service.StayService;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/stays")
public class StayController {

	private static final String	VIEWS_STAY_CREATE_OR_UPDATE_FORM	= "/stays/createOrUpdateStayForm";

	@Autowired
	private StayService			stayService;

	@Autowired
	private VetService			vetService;


	@GetMapping(value = "/listAllPending")
	public String listAllPending(final ModelMap modelMap) {
		String view = "stays/list";

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		User user = (User) authentication.getPrincipal();

		Vet vet = vetService.findByVetByUsername(user.getUsername());

		Iterable<Stay> stays = stayService.findAllPendingByVet(vet);
		modelMap.addAttribute("stays", stays);
		modelMap.addAttribute("accepted", false);
		return view;

	}

	@GetMapping(value = "/listAllAccepted")
	public String listAllAccepted(final ModelMap modelMap) {
		String view = "stays/list";

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		User user = (User) authentication.getPrincipal();

		Vet vet = vetService.findByVetByUsername(user.getUsername());

		Iterable<Stay> stays = stayService.findAllAcceptedByVet(vet);
		modelMap.addAttribute("stays", stays);
		modelMap.addAttribute("accepted", true);
		return view;

	}

	@GetMapping(path = "/accept/{stayId}")
	public String acceptStay(@PathVariable("stayId") final int stayId, final ModelMap modelMap) {
		Stay stay = stayService.findEntityById(stayId).get();

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		User user = (User) authentication.getPrincipal();

		Vet vet = this.vetService.findByVetByUsername(user.getUsername());
		
		if(stay.getClinic().getId() == vet.getClinic().getId()) {
			this.stayService.acceptStay(stay);
		}else {
			modelMap.addAttribute("nonAuthorized", "No estás autorizado");
		}
		

		return "redirect:/stays/listAllAccepted";

	}

	@GetMapping(path = "/cancel/{stayId}")
	public String cancelStay(@PathVariable("stayId") final int stayId, final ModelMap modelMap) {
		Stay stay = stayService.findEntityById(stayId).get();

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		User user = (User) authentication.getPrincipal();

		Vet vet = this.vetService.findByVetByUsername(user.getUsername());
		
		if(stay.getClinic().getId() == vet.getClinic().getId()) {
			this.stayService.cancelStay(stay);
		}else {
			modelMap.addAttribute("nonAuthorized", "No estás autorizado");
		}

		return "redirect:/stays/listAllAccepted";

	}

	@GetMapping(path = "/changeDate/{stayId}")
	public String changeDateStay(@PathVariable("stayId") final int stayId, final ModelMap modelMap) {
		Stay stay = stayService.findEntityById(stayId).get();
		modelMap.addAttribute("stay", stay);
		return StayController.VIEWS_STAY_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(path = "/save/{stayId}")
	public String updateStay(@PathVariable("stayId") final int stayId, @Valid final Stay entity,
		final BindingResult result, final ModelMap modelMap, HttpServletResponse resp) {
		// HttpServletResponse resp esto hay que meterlo en los metodos que queramos modificar la respuesta http

		// se modifica como vemos en la siguiente linea
		// aqui teneis un ejemplo de como se mete el codigo 400, que significa que algun parametro esta mal
		resp.setStatus(HttpStatus.BAD_REQUEST.value());
		String view = StayController.VIEWS_STAY_CREATE_OR_UPDATE_FORM;

		if (!stayService.findEntityById(stayId).isPresent()) {
			// este por ejemplo es que no ha encontrado el stay a modificar
			resp.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			// entonces por cojones hace falta el redirect
			// y el codigo de respuesta http devuelto siempre en este caso es el 302
			return "redirect:/oups";
		}
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		User user = (User) authentication.getPrincipal();

		Vet vet = this.vetService.findByVetByUsername(user.getUsername());

		Stay stay = stayService.findEntityById(stayId).get();
		stay.setDescription(entity.getDescription());
		stay.setStartDate(entity.getStartDate());
		stay.setFinishDate(entity.getFinishDate());

		if (result.hasErrors())
			modelMap.addAttribute("stay", stay);
		} else if (stay.getClinic().getId() != vet.getClinic().getId()){
			modelMap.addAttribute("nonAuthorized", "No estás autorizado");
		}else if (!entity.getStartDate().isBefore(entity.getFinishDate())) {
			result.rejectValue("startDate", "startLaterFinish", "the start date cannot be later than the finish date");
			modelMap.addAttribute("stay", stay);
		} else if (entity.getFinishDate().isAfter(entity.getStartDate().plusDays(7L))) {
			result.rejectValue("finishDate", "sevenDays", "you cannot book a stay longer than seven days");
			modelMap.addAttribute("stay", stay);
		} else {
			stayService.saveEntity(stay);
			modelMap.addAttribute("message", "Stay succesfully updated");
			view = listAllAccepted(modelMap);
			// este es el correcto OK -> 200 yes
			resp.setStatus(HttpStatus.OK.value());
		}

		// IMPORTANTE: intentar no hacer nunca un redirect, si es posible evitarlo como en este caso, que antes había
		// uno y ahora no lo hay.
		// es decir, si el redirect, es a un metodo de este mismo controlador, llamar directamente al metodo
		return view;
	}

}
