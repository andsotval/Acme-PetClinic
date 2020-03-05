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
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Simple JavaBean domain object representing a visit.
 *
 * @author Ken Krebs
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "visit")
public class Visit extends BaseEntity {

	/**
	 * Holds value of property date.
	 */
	@Column(name = "visit_date")
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private LocalDate	date;

	/**
	 * Holds value of property description.
	 */
	@NotEmpty
	@Column(name = "description")
	private String		description;

	@Column(name = "is_accepted")
	@NotNull
	private Boolean		isAccepted;

	@ManyToOne
	@JoinColumn(name = "clinic_id")
	private Clinic		clinic;

	@ManyToOne
	@JoinColumn(name = "pet_id")
	private Pet			pet;


	/**
	 * Creates a new instance of Visit for the current date
	 */
	public Visit() {
		this.date = LocalDate.now();
	}

}
