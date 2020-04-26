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

package org.springframework.samples.petclinic.service;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
class VisitServiceTests {

	@Autowired
	protected VisitService	service;

	@Test
	public void testFindAllPendingByVet() {
		Iterable<Visit> visits = service.findAllPendingByVetId(1);
		
	}
	
	@Test
	public void testFindAllAcceptedByVet() {
		Iterable<Visit> visits = service.findAllAcceptedByVetId(1);
	}
	
	@Test
	public void testDeleteByPetId() {
		service.deleteByPetId(1);
	}
	
	@Test
	public void testFindAllPendingByOwnerId() {
		Iterable<Visit> visits = service.findAllPendingByOwnerId(1);
	}
	
	@Test
	public void testFindAllAcceptedByOwnerId() {
		Iterable<Visit> visits = service.findAllAcceptedByOwnerId(1);
	}
	
	@Test
	public void testFindAllByPetId() {
		Iterable<Visit> visits = service.findAllByPetId(1);
	}
	
	@Test
	public void testFindAllByDateTime() {
		LocalDateTime dateTime = LocalDateTime.of(2020, 6, 9, 8, 30, 00);
		Iterable<Visit> visits = service.findAllByDateTime(dateTime);
	}



}
