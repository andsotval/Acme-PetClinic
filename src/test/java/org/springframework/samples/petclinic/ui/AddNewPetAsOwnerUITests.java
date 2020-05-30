
package org.springframework.samples.petclinic.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

public class AddNewPetAsOwnerUITests extends AbstractUITests {

	@Test
	public void testAddNewPetCorrectFields() throws Exception {
		driver.get("http://localhost:" + port);

		LogInAsOwner();

		driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/a/span[2]")).click();
		driver.findElement(By.linkText("Add new Pet")).click();
		driver.findElement(By.id("name")).click();
		driver.findElement(By.id("name")).clear();
		driver.findElement(By.id("name")).sendKeys("Nombre de prueba");
		driver.findElement(By.id("birthDate")).clear();
		driver.findElement(By.id("birthDate")).sendKeys("2020/04/04");
		driver.findElement(By.id("pet")).click();
		new Select(driver.findElement(By.id("type"))).selectByVisibleText("cat");
		driver.findElement(By.xpath("//option[@value='cat']")).click();
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		assertEquals("Nombre de prueba", driver.findElement(By.linkText("Nombre de prueba")).getText());

		LogOut();

	}

	@Test
	public void testAddNewPetErrorsOnFields() throws Exception {
		driver.get("http://localhost:" + port);

		LogInAsOwner();

		driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/a/span[2]")).click();
		driver.findElement(By.linkText("Add new Pet")).click();
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		assertEquals("el tamaño tiene que estar entre 3 y 50",
			driver.findElement(By.xpath("//form[@id='pet']/div/div[2]/div/span[2]")).getText());
		assertEquals("no puede ser null",
			driver.findElement(By.xpath("//form[@id='pet']/div/div[3]/div/span[2]")).getText());
		assertEquals("is required",
			driver.findElement(By.xpath("/html/body/div[1]/div/form/div[1]/div[4]/div/div/span[2]")).getText());
		driver.findElement(By.id("birthDate")).click();
		driver.findElement(By.id("birthDate")).click();
		driver.findElement(By.id("birthDate")).clear();
		driver.findElement(By.id("birthDate")).sendKeys("2100/04/04");
		driver.findElement(By.id("name")).click();
		driver.findElement(By.id("name")).click();
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		assertEquals("the birth date cannot be in future",
			driver.findElement(By.xpath("//form[@id='pet']/div/div[3]/div/span[2]")).getText());
		driver.findElement(By.id("birthDate")).click();
		driver.findElement(By.id("birthDate")).clear();
		driver.findElement(By.id("birthDate")).sendKeys("2020/04/04");
		driver.findElement(By.id("name")).click();
		driver.findElement(By.id("name")).clear();
		driver.findElement(By.id("name")).sendKeys(
			"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		assertEquals("el tamaño tiene que estar entre 3 y 50",
			driver.findElement(By.xpath("//form[@id='pet']/div/div[2]/div/span[2]")).getText());
		driver.findElement(By.id("name")).clear();
		driver.findElement(By.id("name")).sendKeys("Wiskers");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		assertEquals("is required",
			driver.findElement(By.xpath("/html/body/div[1]/div/form/div[1]/div[4]/div/div/span[2]")).getText());
		new Select(driver.findElement(By.id("type"))).selectByVisibleText("cat");
		driver.findElement(By.xpath("//option[@value='cat']")).click();
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		assertEquals("Wiskers", driver.findElement(By.linkText("Wiskers")).getText());

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
