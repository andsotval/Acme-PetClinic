
package org.springframework.samples.petclinic.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

@TestMethodOrder(OrderAnnotation.class)
public class StayAsVetUITests extends AbstractUITests {

	@Test
	@Order(6)
	public void testStayPendingCheckTableWithElements() throws Exception {
		driver.get("http://localhost:" + port);

		LogInAsOwner("vet1", "vet1");

		driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[3]/a/span[2]")).click();
		int sizeVet1 = driver.findElements(By.xpath("//table[@id='staysTable']")).size();
		if (sizeVet1 == 0)
			assertEquals("You have checked out all the pending stays", driver.findElement(By.xpath("//h3")).getText());
		else
			for (int i = 1; i <= sizeVet1; i++)
				assertEquals("PENDING",
					driver.findElement(By.xpath("//table[@id='staysTable']/tbody/tr[" + i + "]/td[4]")).getText());

		LogOut();

	}

	@Test
	@Order(5)
	public void testStayPendingCheckTableWithoutElements() throws Exception {
		driver.get("http://localhost:" + port);

		LogInAsOwner("vet7", "vet7");

		driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[3]/a/span[2]")).click();
		int sizeVet7 = driver.findElements(By.xpath("//table[@id='staysTable']")).size();
		if (sizeVet7 == 0)
			assertEquals("You have checked out all the pending stays", driver.findElement(By.xpath("//h3")).getText());
		else
			for (int i = 1; i <= sizeVet7; i++)
				assertEquals("PENDING",
					driver.findElement(By.xpath("//table[@id='staysTable']/tbody/tr[" + i + "]/td[4]")).getText());
		LogOut();
	}

	@Test
	@Order(1)
	public void testCheckAcceptStayPending() throws Exception {
		driver.get("http://localhost:" + port);

		LogInAsOwner("vet4", "vet4");

		driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[3]/a/span[2]")).click();

		String start = driver.findElement(By.xpath("//table[@id='staysTable']/tbody/tr/td")).getText();
		String finish = driver.findElement(By.xpath("//table[@id='staysTable']/tbody/tr/td[2]")).getText();
		String description = driver.findElement(By.xpath("//table[@id='staysTable']/tbody/tr/td[3]")).getText();
		driver.findElement(By.xpath("//table[@id='staysTable']/tbody/tr/td[5]/a/span")).click();
		assertEquals(start, driver.findElement(By.xpath("//table[@id='staysTable']/tbody/tr[2]/td")).getText());
		assertEquals(finish, driver.findElement(By.xpath("//table[@id='staysTable']/tbody/tr[2]/td[2]")).getText());
		assertEquals(description,
			driver.findElement(By.xpath("//table[@id='staysTable']/tbody/tr[2]/td[3]")).getText());
		assertEquals("ACCEPTED", driver.findElement(By.xpath("//table[@id='staysTable']/tbody/tr[2]/td[4]")).getText());

		LogOut();

	}

	@Test
	@Order(3)
	public void testAcceptStayPendingAndModifyAfterAcceptanceSuccesfully() throws Exception {
		driver.get("http://localhost:" + port);

		LogInAsOwner("vet3", "vet3");

		driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[3]/a/span[2]")).click();
		driver.findElement(By.xpath("//table[@id='staysTable']/tbody/tr/td[5]/a/span")).click(); //acepto la stay

		driver.findElement(By.xpath("//table[@id='staysTable']/tbody/tr/td[5]/a/span")).click(); //modifico la stay
		driver.findElement(By.id("description")).click();
		driver.findElement(By.id("description")).clear();
		driver.findElement(By.id("description")).sendKeys("stay Modificada");
		driver.findElement(By.id("startDate")).click();
		driver.findElement(By.id("startDate")).clear();
		driver.findElement(By.id("startDate")).sendKeys("2020/07/09");
		driver.findElement(By.xpath("//body/div/div")).click();
		driver.findElement(By.id("finishDate")).click();
		driver.findElement(By.id("finishDate")).clear();
		driver.findElement(By.id("finishDate")).sendKeys("2020/07/12");
		driver.findElement(By.xpath("//form[@id='stay']/div[2]/div")).click();
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		assertEquals("2020-07-09", driver.findElement(By.xpath("//table[@id=\"staysTable\"]/tbody/tr/td")).getText());
		assertEquals("2020-07-12",
			driver.findElement(By.xpath("//table[@id=\"staysTable\"]/tbody/tr/td[2]")).getText());
		assertEquals("stay Modificada",
			driver.findElement(By.xpath("//table[@id=\"staysTable\"]/tbody/tr/td[3]")).getText());
		assertEquals("Stay succesfully updated", driver.findElement(By.xpath("/html/body/div/div/div[1]")).getText());

		LogOut();

	}

	@Test
	@Order(4)
	public void testAcceptStayPendingAndModifyAfterAcceptanceWithErrorsOnFields() throws Exception {
		driver.get("http://localhost:" + port);

		LogInAsOwner("vet5", "vet5");

		driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[3]/a/span[2]")).click();
		driver.findElement(By.xpath("//table[@id='staysTable']/tbody/tr/td[5]/a/span")).click(); //acepto la stay

		driver.findElement(By.xpath("//table[@id='staysTable']/tbody/tr/td[5]/a/span")).click(); //modifico la stay

		driver.findElement(By.id("description")).click();
		driver.findElement(By.id("description")).clear();
		driver.findElement(By.id("startDate")).click();
		driver.findElement(By.id("startDate")).clear();
		driver.findElement(By.id("finishDate")).click();
		driver.findElement(By.id("finishDate")).clear();
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		assertEquals("no puede estar vacío",
			driver.findElement(By.xpath("//form[@id=\"stay\"]/div/div/div/span[2]")).getText());
		assertEquals("no puede ser null",
			driver.findElement(By.xpath("//form[@id='stay']/div/div[2]/div/span[2]")).getText());
		assertEquals("no puede ser null",
			driver.findElement(By.xpath("//form[@id='stay']/div/div[3]/div/span[2]")).getText());
		driver.findElement(By.id("description")).click();
		driver.findElement(By.id("description")).clear();
		driver.findElement(By.id("description")).sendKeys("stay Modificada");
		driver.findElement(By.id("startDate")).click();
		driver.findElement(By.id("startDate")).clear();
		driver.findElement(By.id("startDate")).sendKeys("2020/04/09");
		driver.findElement(By.xpath("//body/div/div")).click();
		driver.findElement(By.id("finishDate")).click();
		driver.findElement(By.id("finishDate")).clear();
		driver.findElement(By.id("finishDate")).sendKeys("2020/04/18");
		driver.findElement(By.xpath("//form[@id='stay']/div[2]/div")).click();
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		assertEquals("Minimum 2 days after today",
			driver.findElement(By.xpath("//form[@id='stay']/div/div[2]/div/span[2]")).getText());
		assertEquals("Stays cannot last longer than one week",
			driver.findElement(By.xpath("//form[@id='stay']/div/div[3]/div/span[2]")).getText());

		driver.findElement(By.id("startDate")).click();
		driver.findElement(By.id("startDate")).clear();
		driver.findElement(By.id("startDate")).sendKeys("2020/06/09");
		driver.findElement(By.xpath("//body/div/div")).click();
		driver.findElement(By.id("finishDate")).click();
		driver.findElement(By.id("finishDate")).clear();
		driver.findElement(By.id("finishDate")).sendKeys("2020/06/08");
		driver.findElement(By.xpath("//form[@id='stay']/div[2]/div")).click();
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		assertEquals("The finish date must be after the start date",
			driver.findElement(By.xpath("//form[@id='stay']/div/div[3]/div/span[2]")).getText());

		LogOut();

	}

	@Test
	@Order(2)
	public void testDeleteStaySuccesfully() throws Exception {
		driver.get("http://localhost:" + port);

		LogInAsOwner("vet4", "vet4");

		driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[3]/a/span[2]")).click();
		driver.findElement(By.linkText("List my accepted stays")).click();
		driver.findElement(By.xpath("//table[@id=\"staysTable\"]/tbody/tr/td[6]/a/span")).click();
		//*[@id="staysTable"]/tbody/tr/td[6]/a/span
		if (driver.findElements(By.xpath("//table[@id='staysTable']")).size() == 0)
			assertEquals("You don't have accepted stays",
				driver.findElement(By.xpath("/html/body/div/div/table/tbody/tr/td/h3")).getText());
		LogOut();

	}

	@AfterEach
	public void tearDown() throws Exception {
		driver.quit();
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString))
			fail(verificationErrorString);
	}

	private WebDriver LogInAsOwner(String username, String password) {
		driver.findElement(By.xpath("//a[contains(text(),'Login')]")).click();
		driver.findElement(By.id("username")).click();
		driver.findElement(By.id("username")).clear();
		driver.findElement(By.id("username")).sendKeys(username);
		driver.findElement(By.id("password")).click();
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys(password);
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		return driver;
	}

	private WebDriver LogOut() {
		driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a/strong")).click();
		driver.findElement(By.linkText("Logout")).click();
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		return driver;
	}

}
