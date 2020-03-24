package org.springframework.samples.petclinic.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Stay;

public interface StayRepository extends CrudRepository<Stay, Integer>{

}
