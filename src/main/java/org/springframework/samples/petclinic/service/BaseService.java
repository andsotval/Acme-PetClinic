
package org.springframework.samples.petclinic.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.BaseEntity;
import org.springframework.samples.petclinic.repository.BaseRepository;
import org.springframework.transaction.annotation.Transactional;

public abstract class BaseService<T extends BaseEntity> {

	private BaseRepository<T> repository;


	@Autowired
	public BaseService(BaseRepository<T> repository) {
		this.repository = repository;
	}

	@Transactional(readOnly = true)
	public Iterable<T> findAllEntities() {
		return this.repository.findAll();
	}

	@Transactional(readOnly = true)
	public Optional<T> findEntityById(int entityId) {
		return repository.findById(entityId);
	}

	@Transactional
	public T saveEntity(T entity) {
		return repository.save(entity);
	}

	@Transactional
	public void deleteEntity(T entity) {
		if(entity != null) {
			repository.delete(entity);
		}
		
	}

	@Transactional
	public void deleteEntityById(int entityId) {
		repository.deleteById(entityId);
	}

}
