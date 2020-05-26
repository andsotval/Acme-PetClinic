/**
 * DP2 - Grupo 8
 * LAB F1.33
 * Date: 05-may-2020
 */

package org.springframework.samples.petclinic.repository;

import java.util.Collection;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.samples.petclinic.model.NamedEntity;

// 20200526: Este repositorio se ha implementado debido a no haber podido
// encontrar una solución para el error devuelto por la anotación de JPA
// @column(unique = true) para MySQL
@NoRepositoryBean
public abstract interface NamedRepository<T extends NamedEntity> extends BaseRepository<T> {

	Collection<T> findByName(String name);

}
