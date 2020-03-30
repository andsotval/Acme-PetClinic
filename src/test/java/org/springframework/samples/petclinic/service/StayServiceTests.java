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
import org.springframework.samples.petclinic.model.Stay;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
class StayServiceTests {

	@Autowired
	protected StayService	stayService;

	@Autowired
	protected VetService	vetService;


	@Test
	public void testCancelStay() {
		int id = 1;
		Stay stay = this.stayService.findById(id).get();
		this.stayService.cancelStay(stay);
		stay = this.stayService.findById(stay.getId()).get();
		assert stay.getIsAccepted() == false;
	}

	@Test
	public void testAcceptStay() {
		int id = 1;
		Stay stay = this.stayService.findById(id).get();
		this.stayService.acceptStay(stay);
		stay = this.stayService.findById(stay.getId()).get();
		assert stay.getIsAccepted() == true;
	}

	@Test
	public void testFindAllPendingByVet() {
		this.vetService.findVets().forEach(v -> this.stayService.findAllPendingByVet(v).forEach(visit -> Assert.assertEquals(visit.getIsAccepted(), null)));
	}

	@Test
	public void testFindAllAcceptedByVet() {
		this.vetService.findVets().forEach(v -> this.stayService.findAllAcceptedByVet(v).forEach(visit -> Assert.assertEquals(visit.getIsAccepted(), true)));

	}

}
