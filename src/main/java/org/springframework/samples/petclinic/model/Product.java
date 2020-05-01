
package org.springframework.samples.petclinic.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "product")
public class Product extends NamedEntity {

	@Column(name = "price")
	@NotNull
	@Min(value = 0)
	private Double		price;

	@Column(name = "tax")
	@NotNull
	@Min(value = 0)
	private Double		tax;

	@Column(name = "is_available")
	@NotNull
	private Boolean		available;

	@ManyToOne
	@JoinColumn(name = "provider_id")
	private Provider	provider;


	@Override
	public String toString() {
		return getName() + " - " + getPrice() + "€";
	}
}
