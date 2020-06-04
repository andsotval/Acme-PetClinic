/**
 * DP2 - Grupo 8
 * LAB F1.33
 * Date: 05-may-2020
 */

package org.springframework.samples.petclinic.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "purchase_order")
public class Order extends BaseEntity {

	@Column(name = "order_date")
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	@NotNull
	private LocalDate	date;

	@Column(name = "is_accepted")
	private Boolean		isAccepted;

	@ManyToOne(optional = false)
	@JoinColumn(name = "manager_id", nullable = false)
	private Manager		manager;
}
