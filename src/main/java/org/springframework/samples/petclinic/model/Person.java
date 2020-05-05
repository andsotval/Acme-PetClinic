/**
 * DP2 - Grupo 8
 * LAB F1.33
 * Date: 03-mar-2020
 * User: carlo
 */

package org.springframework.samples.petclinic.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import org.springframework.core.style.ToStringCreator;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Simple JavaBean domain object representing an person.
 *
 * @author Ken Krebs
 */
@Data
@EqualsAndHashCode(callSuper = true)
@MappedSuperclass
public class Person extends BaseEntity {

	@Column(name = "first_name")
	@NotEmpty
	protected String	firstName;

	@Column(name = "last_name")
	@NotEmpty
	protected String	lastName;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User		user;

	@Column(name = "address")
	@NotEmpty
	private String		address;

	@Column(name = "city")
	@NotEmpty
	private String		city;

	@Column(name = "telephone")
	@Pattern(regexp = "(6|7|9)[0-9]{8}")
	@NotEmpty
	private String		telephone;

	@Column(name = "mail")
	@Pattern(regexp = "^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$")
	@NotEmpty
	private String		mail;


	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(final String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(final String lastName) {
		this.lastName = lastName;
	}

	public User getUser() {
		return user;
	}

	public void setUser(final User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return new ToStringCreator(this).append("id", getId()).append("new", isNew()).append("lastName", getLastName())
			.append("firstName", getFirstName()).append("address", getAddress()).append("city", getCity())
			.append("telephone", getTelephone()).toString();
	}

}
