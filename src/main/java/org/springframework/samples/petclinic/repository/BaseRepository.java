/**
 * DP2 - Grupo 8
 * LAB F1.33
 * Date: 05-may-2020
 */

package org.springframework.samples.petclinic.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.BaseEntity;
import org.springframework.stereotype.Repository;

@Repository
public abstract interface BaseRepository<T extends BaseEntity> extends CrudRepository<T, Integer> {

}
