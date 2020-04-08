
package org.springframework.samples.petclinic.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.BaseEntity;

public abstract interface BaseRepository<T extends BaseEntity> extends CrudRepository<T, Integer> {

}
