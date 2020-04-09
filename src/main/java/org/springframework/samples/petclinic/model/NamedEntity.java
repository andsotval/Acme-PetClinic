/**
 * DP2 - Grupo 8
 * LAB F1.33
 * Date: 03-mar-2020
 * User: carlo
 */
package org.springframework.samples.petclinic.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Size;

@MappedSuperclass
public class NamedEntity extends BaseEntity {

    @Size(min = 3, max = 50)
	@Column(name = "name")
	private String name;

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return this.getName();
	}

}
