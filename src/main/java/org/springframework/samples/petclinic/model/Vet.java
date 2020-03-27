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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.xml.bind.annotation.XmlElement;

import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Simple JavaBean domain object representing a veterinarian.
 *
 * @author Ken Krebs
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @author Arjen Poutsma
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "vet")
public class Vet extends Person {

	@Column(name = "address")
	@NotEmpty
	private String			address;

	@Column(name = "city")
	@NotEmpty
	private String			city;

	@Column(name = "telephone")
	@NotEmpty
	@Pattern(regexp = "")
	private String			telephone;

	@Column(name = "mail")
	@NotEmpty
	@Pattern(regexp = "")
	private String			mail;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "vet_specialties", joinColumns = @JoinColumn(name = "vet_id"), inverseJoinColumns = @JoinColumn(name = "specialty_id"))
	private Set<Specialty>	specialties;

	@ManyToOne
	@JoinColumn(name = "clinic_id")
	private Clinic			clinic;


	protected Set<Specialty> getSpecialtiesInternal() {
		if (this.specialties == null) {
			this.specialties = new HashSet<>();
		}
		return this.specialties;
	}

	protected void setSpecialtiesInternal(final Set<Specialty> specialties) {
		this.specialties = specialties;
	}

	@XmlElement
	public List<Specialty> getSpecialties() {
		List<Specialty> sortedSpecs = new ArrayList<>(this.getSpecialtiesInternal());
		PropertyComparator.sort(sortedSpecs, new MutableSortDefinition("name", true, true));
		return Collections.unmodifiableList(sortedSpecs);
	}

	public int getNrOfSpecialties() {
		return this.getSpecialtiesInternal().size();
	}

	public void addSpecialty(final Specialty specialty) {
		this.getSpecialtiesInternal().add(specialty);
	}

}
