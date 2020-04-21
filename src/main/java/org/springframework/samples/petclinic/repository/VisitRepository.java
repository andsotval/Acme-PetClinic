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

import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.stereotype.Repository;

@Repository
public interface VisitRepository extends BaseRepository<Visit> {

	@Query("SELECT v FROM Visit v INNER JOIN Clinic c ON c.id=v.clinic.id WHERE v.isAccepted IS NULL AND ?2 IN (SELECT v2.id FROM Vet v2 WHERE v2.clinic.id=c.id) AND v.date > ?1 ")
	Iterable<Visit> findAllPendingByVet(LocalDate actualDate, Integer vetId);

	@Query("SELECT v FROM Visit v INNER JOIN Clinic c ON c.id=v.clinic.id WHERE v.isAccepted = true AND ?2 IN (SELECT v2.id FROM Vet v2 WHERE v2.clinic.id=c.id) AND v.date > ?1 ")
	Iterable<Visit> findAllAcceptedByVet(LocalDate actualDate, Integer vetId);
	
	@Query("SELECT v FROM Visit v WHERE v.pet.id = ?1 ")
	Collection<Visit> findByPetId(int petId);
	
	@Query("SELECT v FROM Visit v WHERE v.isAccepted IS NULL AND v.pet.owner.id=?1")
	Iterable<Visit> findAllPendingByOwner(Integer ownerId);

}
