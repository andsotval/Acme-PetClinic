/**
 * DP2 - Grupo 8
 * LAB F1.33
 * Date: 05-may-2020
 */

package org.springframework.samples.petclinic.model;

import java.time.LocalDateTime;

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

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "visit")
public class Visit extends BaseEntity {

	@Column(name = "visit_date")
	@DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
	@NotNull
	private LocalDateTime	dateTime;

	@NotEmpty
	@Column(name = "description")
	private String			description;

	@Column(name = "is_accepted")
	private Boolean			isAccepted;

	@ManyToOne
	@JoinColumn(name = "clinic_id")
	private Clinic			clinic;

	@ManyToOne
	@JoinColumn(name = "pet_id")
	private Pet				pet;


	/**
	 * Creates a new instance of Visit for the current date
	 */
	public Visit() {
		dateTime = LocalDateTime.now();
	}

}
