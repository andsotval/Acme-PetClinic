
package org.springframework.samples.petclinic.service;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

import javax.validation.ValidationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.samples.petclinic.model.NamedEntity;
import org.springframework.samples.petclinic.repository.BaseRepository;
import org.springframework.samples.petclinic.repository.NamedRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// 20200526: Este servicio se ha implementado debido a no haber podido
// encontrar una solución para el error devuelto por la anotación de JPA
// @column(unique = true) para MySQL
@Service
public abstract class NamedService<T extends NamedEntity> extends BaseService<T> {

	private static final Logger	log	= LoggerFactory.getLogger(NamedService.class);

	private NamedRepository<T>	namedRepository;

	private static Method		method;


	public NamedService(BaseRepository<T> repository, NamedRepository<T> namedRepository) {
		super(repository);
		this.namedRepository = namedRepository;
		try {
			method = NamedEntity.class.getDeclaredMethod("getName");
		} catch (NoSuchMethodException | SecurityException e) {
			log.error("Faild to instance NamedService. ", e);
		}
	}

	@Transactional(readOnly = true)
	private Collection<T> findByName(String name) {
		return namedRepository.findByName(name);
	}

	@Override
	@Transactional
	public T saveEntity(T entity) throws RuntimeException {
		if (entity.getId() == null)
			return persist(entity);
		else
			return merge(entity);
	}

	private T persist(T entity) throws RuntimeException {
		try {
			String name = (String) method.invoke(entity);
			if (!findByName(name).isEmpty())
				throw new ValidationException("The indicated name already exists");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return super.saveEntity(entity);
	}

	private T merge(T entity) throws RuntimeException {
		try {
			String name = (String) method.invoke(entity);
			List<T> collection = (List<T>) findByName(name);
			if (collection.size() >= 1) {
				T _entity = collection.get(0);
				if (!_entity.getId().equals(entity.getId()))
					throw new ValidationException("The indicated name already exists");
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return super.saveEntity(entity);
	}

}
