/**
 * DP2 - Grupo 8
 * LAB F1.33
 * Date: 05-may-2020
 */

package org.springframework.samples.petclinic.web;

import java.util.Map;

import javax.validation.Valid;

import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Authorities;
import org.springframework.samples.petclinic.model.Manager;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Person;
import org.springframework.samples.petclinic.model.Provider;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.ManagerService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.ProviderService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Michael Isvy
 */
@Controller
public class UserController {

	private static final String	VIEWS_OWNER_CREATE_FORM	= "users/createUserForm";

	private OwnerService		ownerService;

	private ManagerService		managerService;

	private VetService			vetService;

	private ProviderService		providerService;

	private UserService			userService;

	private AuthoritiesService	authoritiesService;

	@Autowired
	private Mapper				mapper;


	@Autowired
	public UserController(OwnerService ownerService, ManagerService managerService, VetService vetService,
		ProviderService providerService, UserService userService, AuthoritiesService authoritiesService) {
		this.ownerService = ownerService;
		this.managerService = managerService;
		this.vetService = vetService;
		this.providerService = providerService;
		this.userService = userService;
		this.authoritiesService = authoritiesService;
	}

	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@GetMapping(value = "/users/new")
	public String initCreationForm(Map<String, Object> model) {
		Person person = new Person();
		model.put("person", person);
		return VIEWS_OWNER_CREATE_FORM;
	}

	@PostMapping(value = "/users/new")
	public String processCreationForm(@RequestParam("role") String role, @Valid Person person, BindingResult result) {
		if (result.hasErrors())
			return VIEWS_OWNER_CREATE_FORM;

		// Creamos el usuario
		User user = mapper.map(person, User.class);
		User _user = userService.saveEntity(user);

		// Dependiendo del rol, creamos el authorities y el actor correspondiente
		if (role.equals("owner"))
			createOwner(person, _user);

		else if (role.equals("manager"))
			createManager(person, _user);

		else if (role.equals("vet"))
			createVet(person, _user);

		else if (role.equals("provider"))
			createProvider(person, _user);

		else
			return VIEWS_OWNER_CREATE_FORM;

		return "redirect:/login";
	}

	private void createProvider(Person person, User user) {
		Provider provider = mapper.map(person, Provider.class);

		Authorities auth = new Authorities();
		auth.setAuthority("provider");
		auth.setUsername(user.getUsername());

		authoritiesService.saveAuthorities(auth);

		provider.setUser(user);
		providerService.saveEntity(provider);
	}

	private void createVet(Person person, User user) {
		Vet vet = mapper.map(person, Vet.class);

		Authorities auth = new Authorities();
		auth.setAuthority("veterinarian");
		auth.setUsername(user.getUsername());

		authoritiesService.saveAuthorities(auth);

		vet.setUser(user);
		vetService.saveEntity(vet);
	}

	private void createManager(Person person, User user) {
		Manager manager = mapper.map(person, Manager.class);

		Authorities auth = new Authorities();
		auth.setAuthority("manager");
		auth.setUsername(user.getUsername());

		authoritiesService.saveAuthorities(auth);

		manager.setUser(user);
		managerService.saveEntity(manager);
	}

	private void createOwner(Person person, User user) {
		Owner owner = mapper.map(person, Owner.class);

		Authorities auth = new Authorities();
		auth.setAuthority("owner");
		auth.setUsername(user.getUsername());

		authoritiesService.saveAuthorities(auth);

		owner.setUser(user);
		ownerService.saveEntity(owner);
	}

}
