/**
 * DP2 - Grupo 8
 * LAB F1.33
 * Date: 05-may-2020
 */

package org.springframework.samples.petclinic.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "user_account")
public class User extends BaseEntity {

	@Column(name = "username")
	String	username;

	@Column(name = "password")
	String	password;

	@Column(name = "enabled")
	Boolean	enabled;
}
