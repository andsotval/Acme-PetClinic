
package org.springframework.samples.petclinic.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Authorities;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthoritiesRepository extends CrudRepository<Authorities, String> {

}
