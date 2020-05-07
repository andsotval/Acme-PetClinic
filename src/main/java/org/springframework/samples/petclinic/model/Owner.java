/**
 * DP2 - Grupo 8
 * LAB F1.33
 * Date: 05-may-2020
 */

package org.springframework.samples.petclinic.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.core.style.ToStringCreator;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "owner")
public class Owner extends Person {

	@ManyToOne
	@JoinColumn(name = "clinic_id")
	private Clinic clinic;


	@Override
	public String toString() {
		return new ToStringCreator(this).append("id", getId()).append("new", isNew()).append("lastName", getLastName())
			.append("firstName", getFirstName()).append("address", getAddress()).append("city", getCity())
			.append("telephone", getTelephone()).toString();
	}

}
