/**
 * DP2 - Grupo 8
 * LAB F1.33
 * Date: 03-mar-2020
 * User: carlo
 */

package org.springframework.samples.petclinic.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "administrator")
public class Administrator extends BaseEntity {

	/** The user. */
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User user;

}
