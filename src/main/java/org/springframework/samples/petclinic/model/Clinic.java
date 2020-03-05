
package org.springframework.samples.petclinic.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "clinic")
public class Clinic extends NamedEntity {

	@Column(name = "address")
	@NotEmpty
	private String		address;

	@Column(name = "city")
	@NotEmpty
	private String		city;

	@Column(name = "telephone")
	@NotEmpty
	@Pattern(regexp = "")
	private String		telephone;

	@OneToOne
	@JoinColumn(name = "manager_id", referencedColumnName = "id")
	private Manager		manager;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "clinic", fetch = FetchType.EAGER)
	private Set<Stay>	stay;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "clinic", fetch = FetchType.EAGER)
	private Set<Visit>	visit;

	@OneToMany(mappedBy = "clinic", fetch = FetchType.EAGER)
	private Set<Vet>	vet;

	@OneToMany(mappedBy = "clinic", fetch = FetchType.EAGER)
	private Set<Owner>	owner;

}
