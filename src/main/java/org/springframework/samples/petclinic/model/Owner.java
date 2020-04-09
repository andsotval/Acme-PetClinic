/**
 * DP2 - Grupo 8
 * LAB F1.33
 * Date: 03-mar-2020
 * User: carlo
 */

package org.springframework.samples.petclinic.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "owner")
public class Owner extends Person {

	@ManyToOne
	@JoinColumn(name = "clinic_id")
	private Clinic clinic;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)

	private Set<Pet> pets;

	protected Set<Pet> getPetsInternal() {
		if (this.pets == null) {
			this.pets = new HashSet<>();
		}
		return this.pets;
	}

	public void addPet(final Pet pet) {
		this.getPetsInternal().add(pet);
		pet.setOwner(this);
	}
	//
	//

	//
	// protected void setPetsInternal(final Set<Pet> pets) {
	// this.pets = pets;
	// }
	//
	// public List<Pet> getPets() {
	// List<Pet> sortedPets = new ArrayList<>(this.getPetsInternal());
	// PropertyComparator.sort(sortedPets, new MutableSortDefinition("name", true,
	// true));
	// return Collections.unmodifiableList(sortedPets);
	// }
	//

	//
	// public boolean removePet(final Pet pet) {
	// return this.getPetsInternal().remove(pet);
	// }
	//
	// /**
	// * Return the Pet with the given name, or null if none found for this Owner.
	// *
	// * @param name
	// * to test
	// * @return true if pet name is already in use
	// */
	// public Pet getPet(final String name) {
	// return this.getPet(name, false);
	// }
	//
	// public Pet getPetwithIdDifferent(String name, final Integer id) {
	// name = name.toLowerCase();
	// for (Pet pet : this.getPetsInternal()) {
	// String compName = pet.getName();
	// compName = compName.toLowerCase();
	// if (compName.equals(name) && pet.getId() != id) {
	// return pet;
	// }
	// }
	// return null;
	// }
	//
	// /**
	// * Return the Pet with the given name, or null if none found for this Owner.
	// *
	// * @param name
	// * to test
	// * @return true if pet name is already in use
	// */
	// public Pet getPet(String name, final boolean ignoreNew) {
	// name = name.toLowerCase();
	// for (Pet pet : this.getPetsInternal()) {
	// if (!ignoreNew || !pet.isNew()) {
	// String compName = pet.getName();
	// compName = compName.toLowerCase();
	// if (compName.equals(name)) {
	// return pet;
	// }
	// }
	// }
	// return null;
	// }

	@Override
	public String toString() {
		return new ToStringCreator(this).append("id", this.getId()).append("new", this.isNew())
				.append("lastName", this.getLastName()).append("firstName", this.getFirstName())
				.append("address", this.getAddress()).append("city", this.getCity())
				.append("telephone", this.getTelephone()).toString();
	}

}
