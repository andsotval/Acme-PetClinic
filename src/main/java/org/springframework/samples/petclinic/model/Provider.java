/**
 * DP2 - Grupo 8
 * LAB F1.33
 * Date: 03-mar-2020
 * User: carlo
 */

package org.springframework.samples.petclinic.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "provider")
public class Provider extends Person {



	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "manager_id", nullable = true)
	private Manager	manager;

	//	@OneToMany(cascade = CascadeType.ALL, mappedBy = "provider", fetch = FetchType.EAGER)
	//	private Set<Product>	product;

}
