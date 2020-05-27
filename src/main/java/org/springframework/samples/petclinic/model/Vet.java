/**
 * DP2 - Grupo 8
 * LAB F1.33
 * Date: 03-mar-2020
 * User: carlo
 */

package org.springframework.samples.petclinic.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "vet")
public class Vet extends Person {

	// TODO Este fecth esta EAGER porque si no fallan los test
	// En producción debería ser LAZY
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "vet_specialties", joinColumns = @JoinColumn(name = "vet_id"),
		inverseJoinColumns = @JoinColumn(name = "specialty_id"))
	private Set<Specialty>	specialties;

	@ManyToOne
	@JoinColumn(name = "clinic_id")
	private Clinic			clinic;


	protected Set<Specialty> getSpecialtiesInternal() {
		if (specialties == null)
			specialties = new HashSet<>();
		return specialties;
	}

	protected void setSpecialtiesInternal(final Set<Specialty> specialties) {
		this.specialties = specialties;
	}

	public List<Specialty> getSpecialties() {
		List<Specialty> sortedSpecs = new ArrayList<>(getSpecialtiesInternal());
		PropertyComparator.sort(sortedSpecs, new MutableSortDefinition("name", true, true));
		return Collections.unmodifiableList(sortedSpecs);
	}

	public int getNrOfSpecialties() {
		return getSpecialtiesInternal().size();
	}

	public void addSpecialty(final Specialty specialty) {
		getSpecialtiesInternal().add(specialty);
	}

	public void removeSpecialty(final Specialty specialty) {
		getSpecialtiesInternal().remove(specialty);
	}

}
