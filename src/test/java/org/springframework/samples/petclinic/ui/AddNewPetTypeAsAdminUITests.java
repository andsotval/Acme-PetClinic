
package org.springframework.samples.petclinic.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AddNewPetTypeAsAdminUITests extends AbstractUITests {

	@Test
	public void testAddNewPeyTypePositiveTestCase() throws Exception {
		driver.get("http://localhost:" + port);

		LogInAsAdmin();

		driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a/strong")).click();
		assertEquals("admin",
			driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/ul/li/div/div/div[2]/p/strong")).getText());
		driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/a/span[2]")).click();
		assertEquals("Pet Type", driver.findElement(By.xpath("//h2")).getText());
		driver.findElement(By.linkText("Create a new Pet type")).click();
		driver.findElement(By.id("name")).click();
		driver.findElement(By.id("name")).clear();
		driver.findElement(By.id("name")).sendKeys("conejo");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		assertEquals("conejo", driver.findElement(By.xpath("//table[@id='pettypeTable']/tbody/tr[7]/td")).getText());

		LogOut();

	}

	@Test
	public void testAddNewPetTypeFormErrorsNegativeTestCase() throws Exception {
		driver.get("http://localhost:" + port);

		LogInAsAdmin();

		driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li/a/span[2]")).click();
		driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/a/span[2]")).click();
		driver.findElement(By.linkText("Create a new Pet type")).click();
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		assertEquals("el tamaño tiene que estar entre 3 y 50",
			driver.findElement(By.xpath("//form[@id='create-pettype-form']/div/div/span[2]")).getText());

		LogOut();

	}

	@Test
	public void testAddNeePetTypeAsOwnerNegativeTestCase() throws Exception {
		driver.get("http://localhost:" + port);

		LogInAsOwner();

		driver.get("http://localhost:" + port + "/pettype/new");
		assertEquals("Whitelabel Error Page", driver.findElement(By.xpath("//h1")).getText());
		driver.get("http://localhost:" + port);

		LogOut();

	}

	@AfterEach
	public void tearDown() throws Exception {
		driver.quit();
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString))
			fail(verificationErrorString);
	}

	private WebDriver LogInAsAdmin() {
		driver.findElement(By.xpath("//a[contains(text(),'Login')]")).click();
		driver.findElement(By.id("username")).click();
		driver.findElement(By.id("username")).clear();
		driver.findElement(By.id("username")).sendKeys("admin");
		driver.findElement(By.id("password")).click();
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("admin");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		return driver;
	}

	private WebDriver LogInAsOwner() {
		driver.findElement(By.xpath("//a[contains(text(),'Login')]")).click();
		driver.findElement(By.id("username")).click();
		driver.findElement(By.id("username")).clear();
		driver.findElement(By.id("username")).sendKeys("owner2");
		driver.findElement(By.id("password")).click();
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("owner2");
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
