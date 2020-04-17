
package org.springframework.samples.petclinic.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.repository.ManagerRepository;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class ManagerServiceTest {

	@Autowired
	protected ManagerRepository managerRepository;


	@Test
	public void TestFindManagerByUsernamePositive() {
		String user = "manager1";
		String usernameDB = managerRepository.findManagerByUsername(user).getUser().getUsername();
		assertEquals(user, usernameDB);
	}

	@Test
	public void TestFindManagerByUsernameNegativeNotPresent() {
		String user = "manager_de_prueba";
		assertEquals(null, managerRepository.findManagerByUsername(user));
	}

	@Test
	public void TestFindManagerByUsernameNegative() {
		String user = "manager1";
		String userWrong = "manager2";
		String usernameDB = managerRepository.findManagerByUsername(user).getUser().getUsername();
		assertNotEquals(userWrong, usernameDB);
	}

}
