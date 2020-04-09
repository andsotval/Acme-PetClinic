
package org.springframework.samples.petclinic.repository;

import org.springframework.samples.petclinic.model.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends BaseRepository<User> {

}
