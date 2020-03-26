/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.samples.petclinic.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.model.Visit;

/**
 * Repository class for <code>Visit</code> domain objects All method names are compliant
 * with Spring Data naming conventions so this interface can easily be extended for Spring
 * Data See here:
 * http://static.springsource.org/spring-data/jpa/docs/current/reference/html/jpa.repositories.html#jpa.query-methods.query-creation
 *
 * @author Ken Krebs
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @author Michael Isvy
 */
public interface VisitRepository extends CrudRepository<Visit, Integer> {

	@Query("SELECT v FROM Visit v WHERE v.id = ?1")
	Visit findByVisitId(Integer visitId);

	@Query("SELECT v FROM Visit v INNER JOIN Clinic c ON c.id=v.clinic.id WHERE v.isAccepted IS NULL AND c.vet IN (?1)")
	Iterable<Visit> findAllPendingByVet(Set<Vet> vets);

	List<Visit> findByPetId(Integer petId);

	@Query("SELECT v FROM Visit v WHERE v.isAccepted = ?1")
	Iterable<Visit> findAllbyAcceptance(boolean bool);
	
	@Query("SELECT v FROM Visit v WHERE v.isAccepted = null")
	Iterable<Visit> findAllPending();

}
