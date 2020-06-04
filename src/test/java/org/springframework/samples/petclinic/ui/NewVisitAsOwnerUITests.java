
package org.springframework.samples.petclinic.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class NewVisitAsOwnerUITests extends AbstractUITests {

	@Test
	public void testNewVisitAsOwnerCorrectFields() throws Exception {
		driver.get("http://localhost:" + port);

		LogInAsOwner();

		driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/a/span[2]")).click();
		driver.findElement(By.xpath("//table[@id='petsTable']/tbody/tr/td[4]/a/span")).click();
		driver.findElement(By.id("description")).click();
		driver.findElement(By.id("description")).clear();
		driver.findElement(By.id("description")).sendKeys("Nueva visita");
		driver.findElement(By.id("dateTime")).click();
		driver.findElement(By.id("dateTime")).click();
		driver.findElement(By.id("dateTime")).clear();
		driver.findElement(By.id("dateTime")).sendKeys("2020/08/05 18:30");
		driver.findElement(By.id("description")).click();
		driver.findElement(By.id("description")).click();
		driver.findElement(By.name("authorized")).click();
		assertEquals("Visits waiting for Acceptance", driver.findElement(By.xpath("//h2[2]")).getText());
		assertEquals("Nueva visita",
			driver.findElement(By.xpath("//table[@id='visitsTable'][2]/tbody/tr/td[3]")).getText());
		assertEquals("2020-08-05 18:30:00",
			driver.findElement(By.xpath("//table[@id='visitsTable'][2]/tbody/tr/td[2]")).getText());

		LogOut();

	}

	@Test
	public void testNewVisitAsOwnerErrorsOnFields() throws Exception {
		driver.get("http://localhost:" + port);

		LogInAsOwner();

		driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/a/span[2]")).click();
		driver.findElement(By.xpath("//table[@id='petsTable']/tbody/tr/td[4]/a/span")).click();
		driver.findElement(By.id("dateTime")).click();
		driver.findElement(By.id("dateTime")).click();
		driver.findElement(By.id("dateTime")).clear();
		driver.findElement(By.id("description")).click();
		driver.findElement(By.id("description")).click();
		driver.findElement(By.name("authorized")).click();
		assertEquals("no puede estar vacío",
			driver.findElement(By.xpath("//form[@id=\"visit\"]/div/div/div/span[2]")).getText());
		assertEquals("no puede ser null",
			driver.findElement(By.xpath("//form[@id=\"visit\"]/div/div[2]/div/span[2]")).getText());
		driver.findElement(By.id("dateTime")).click();
		driver.findElement(By.id("dateTime")).clear();
		driver.findElement(By.id("dateTime")).sendKeys("2019/05/01 19:00");
		driver.findElement(By.id("description")).click();
		driver.findElement(By.name("authorized")).click();
		assertEquals("Minimum 2 days after today",
			driver.findElement(By.xpath("//form[@id=\"visit\"]/div/div[2]/div/span[2]")).getText());

		LogOut();

	}

	@AfterEach
	public void tearDown() throws Exception {
		driver.quit();
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString))
			fail(verificationErrorString);
	}

	private WebDriver LogInAsOwner() {
		driver.findElement(By.xpath("//a[contains(text(),'Login')]")).click();
		driver.findElement(By.id("username")).click();
		driver.findElement(By.id("username")).clear();
		driver.findElement(By.id("username")).sendKeys("owner1");
		driver.findElement(By.id("password")).click();
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("owner1");
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
