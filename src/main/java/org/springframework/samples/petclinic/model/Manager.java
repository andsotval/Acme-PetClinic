/**
 * DP2 - Grupo 8
 * LAB F1.33
 * Date: 03-mar-2020
 * User: carlo
 */

package org.springframework.samples.petclinic.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "manager")
public class Manager extends Person {

	@Column(name = "address")
	@NotEmpty
	private String	address;

	@Column(name = "city")
	@NotEmpty
	private String	city;

	@Column(name = "telephone")
	@Pattern(regexp = "[+]346[0-9]{8}")
	@NotEmpty
	private String	telephone;

	@Column(name = "mail")
	@Pattern(regexp = "^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$")
	@NotEmpty
	private String	mail;

}////
