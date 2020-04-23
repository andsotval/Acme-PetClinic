
package org.springframework.samples.petclinic.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "suggestion")
public class Suggestion extends NamedEntity {

	@Column(name = "description")
	@NotEmpty
	private String			description;

	@Column(name = "is_read")
	@NotNull
	private Boolean			isRead;

	@Column(name = "created")
	@DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
	@NotNull
	private LocalDateTime	created;

	@Column(name = "is_trash")
	@NotNull
	private Boolean			isTrash;

	@OneToOne
	@JoinColumn(name = "user_id")
	private User			user;
}
