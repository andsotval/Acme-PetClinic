
package org.springframework.samples.petclinic.model;

import java.time.LocalDate;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
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
@Table(name = "purchase_order")
public class Order extends BaseEntity {

	@Column(name = "order_date")
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	@NotNull
	private LocalDate		date;

	@Column(name = "is_accepted")
	@NotNull
	private Boolean			isAccepted;

	@ManyToMany(fetch = FetchType.LAZY)
	@NotEmpty
	private Set<Product>	product;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "manager_id", nullable = false)
	private Manager			manager;
}
