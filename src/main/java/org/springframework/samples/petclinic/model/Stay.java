
package org.springframework.samples.petclinic.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "stay")
public class Stay extends BaseEntity {

	@Column(name = "start_date")
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private LocalDate	startDate;

	@Column(name = "finish_date")
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private LocalDate	finishDate;

	@Column(name = "description")
	@NotBlank
	private String		description;

	@Column(name = "is_accepted")
	@NotNull
	private Boolean		isAccepted;

	@ManyToOne
	@JoinColumn(name = "clinic_id")
	private Clinic		clinic;

	@ManyToOne
	@JoinColumn(name = "pet_id", referencedColumnName = "id")
	private Pet			pet;

}