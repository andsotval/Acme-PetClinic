
package org.springframework.samples.petclinic.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "product_order")
public class ProductOrder extends NamedEntity {

	@Column(name = "price")
	@NotNull
	@Min(value = 0)
	private Double	price;

	@Column(name = "tax")
	@NotNull
	@Range(min = 0, max = 100)
	private Double	tax;

	@Column(name = "amount")
	@NotNull
	@Min(value = 1)
	private Integer	amount;

	@ManyToOne(optional = false)
	private Product	product;

	@ManyToOne(optional = true)
	private Order	order;


	@Override
	public String toString() {
		return "ProductOrder [price=" + price + ", tax=" + tax + ", amount=" + amount + ", product=" + product
			+ ", order=" + order + "]";
	}

}
