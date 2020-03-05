/**
 * DP2 - Grupo 8
 * LAB F1.33
 * Date: 03-mar-2020
 * User: carlo
 */

package org.springframework.samples.petclinic.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Simple business object representing a pet.
 *
 * @author Ken Krebs
 * @author Juergen Hoeller
 * @author Sam Brannen
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "pet")
public class Pet extends NamedEntity {

	@Column(name = "birth_date")
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private LocalDate	birthDate;

	@OneToOne
	@JoinColumn(name = "type_id", referencedColumnName = "id")
	private PetType		type;

	@ManyToOne
	@JoinColumn(name = "owner_id", nullable = false)
	private Owner		owner;

	//	@OneToMany(cascade = CascadeType.ALL, mappedBy = "pet", fetch = FetchType.EAGER)
	//	private Set<Visit>	visits;
	//
	//
	//	protected Set<Visit> getVisitsInternal() {
	//		if (this.visits == null) {
	//			this.visits = new HashSet<>();
	//		}
	//		return this.visits;
	//	}
	//
	//	protected void setVisitsInternal(final Set<Visit> visits) {
	//		this.visits = visits;
	//	}
	//
	//	public List<Visit> getVisits() {
	//		List<Visit> sortedVisits = new ArrayList<>(this.getVisitsInternal());
	//		PropertyComparator.sort(sortedVisits, new MutableSortDefinition("date", false, false));
	//		return Collections.unmodifiableList(sortedVisits);
	//	}
	//
	//	public void addVisit(final Visit visit) {
	//		this.getVisitsInternal().add(visit);
	//		visit.setPet(this);
	//	}

}
