
package org.springframework.samples.petclinic.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ListSuggestionsTrashAsAdminUITests extends AbstractUITests {

	@Test
	public void testAddSuggestionToTrashListSuccesfull() throws Exception {
		driver.get("http://localhost:" + port);

		LogInAsAdmin();

		driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[3]/a/span[2]")).click();
		driver.findElement(By.xpath("//h2")).click();
		assertEquals("Suggestions Received", driver.findElement(By.xpath("//h2")).getText());
		String s = driver.findElement(By.xpath("//table[@id='suggestionsTable']/tbody/tr/td/a/strong")).getText();
		driver.findElement(By.xpath("//table[@id='suggestionsTable']/tbody/tr/td[3]/a[2]/span")).click();
		driver.findElement(By.linkText("List Trash")).click();
		assertEquals(s, driver.findElement(By.xpath("//table[@id='suggestionsTable']/tbody/tr/td/a/strong")).getText());
		driver.findElement(By.xpath("//h2")).click();
		assertEquals("Suggestions Trash", driver.findElement(By.xpath("//h2")).getText());

		LogOut();

	}

	@Test
	public void testAddNonExistingSuggestionToTrash() throws Exception {
		driver.get("http://localhost:" + port);

		LogInAsAdmin();

		driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[3]/a/span[2]")).click();
		driver.findElement(By.xpath("//h2")).click();

		assertEquals("Suggestions Received", driver.findElement(By.xpath("//h2")).getText());

		//Intenta mandar a la papelera mediante la url una suggestion que no existe
		driver.get("http://localhost:" + port + "/suggestion/admin/moveTrash/999");

		//Comprueba que ha llegado a la pagina de error
		driver.findElement(By.xpath("//h2")).click();
		assertEquals("Something happened...", driver.findElement(By.xpath("//h2")).getText());
		driver.findElement(By.xpath("//body/div/div/p")).click();
		assertEquals("Expected: controller used to showcase what happens when an exception is thrown",
			driver.findElement(By.xpath("//body/div/div/p")).getText());

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

	private WebDriver LogOut() {
		driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a/strong")).click();
		driver.findElement(By.linkText("Logout")).click();
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		return driver;
	}

}
