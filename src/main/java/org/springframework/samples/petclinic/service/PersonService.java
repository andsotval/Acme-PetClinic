/**
 * DP2 - Grupo 8
 * LAB F1.33
 * Date: 05-may-2020
 */

package org.springframework.samples.petclinic.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.samples.petclinic.model.BaseEntity;
import org.springframework.samples.petclinic.repository.BaseRepository;
import org.springframework.transaction.annotation.Transactional;

public abstract class PersonService<T extends BaseEntity> extends BaseService<T> {

	private Class<T>		clazz;

	@Autowired
	private JdbcTemplate	jdbcTemplate;


	@Autowired
	public PersonService(BaseRepository<T> repository, Class<T> clazz) {
		super(repository);
		this.clazz = clazz;
	}

	@Transactional(readOnly = true)
	public T findPersonByUsername(String username) {
		String sql = "SELECT * FROM " + clazz.getSimpleName()
			+ " WHERE USER_ID = (SELECT ID FROM USER_ACCOUNT WHERE USERNAME = ?)";

		T entity = null;

		try {
			entity = jdbcTemplate.queryForObject(sql, new Object[] {
				username
			}, BeanPropertyRowMapper.newInstance(clazz));
		} catch (DataAccessException e) {
			return null;
		}

		Optional<T> person = findEntityById(entity.getId());

		return person.isPresent() ? person.get() : null;
	}

}
