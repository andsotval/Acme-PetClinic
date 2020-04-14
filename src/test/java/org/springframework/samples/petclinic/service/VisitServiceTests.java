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

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
class VisitServiceTests {

	@Autowired
	protected VisitService	visitService;

	@Autowired
	protected VetService	vetService;


	@Test
	public void testFindAllPendingByVet() {
		vetService.findAllEntities().forEach(v -> visitService.findAllPendingByVet(v)
			.forEach(visit -> Assert.assertEquals(visit.getIsAccepted(), null)));
	}

	@Test
	public void testFindAllAcceptedByVet() {
		vetService.findAllEntities().forEach(v -> visitService.findAllAcceptedByVet(v)
			.forEach(visit -> Assert.assertEquals(visit.getIsAccepted(), true)));

	}

}
