
package org.springframework.samples.petclinic.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ReadSuggestionsAsAdminUITests extends AbstractUITests {

	@Test
	public void testReadSuggestion() throws Exception {
		driver.get("http://localhost:" + port);

		LogInAsAdmin();

		driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[3]/a/span[2]")).click();
		driver.findElement(By.xpath("//h2")).click();
		assertEquals("Suggestions Received", driver.findElement(By.xpath("//h2")).getText());
		assertEquals("Intervalo de tiempo en estancias",
			driver.findElement(By.xpath("//table[@id='staysTable']/tbody/tr/td/a/strong")).getText());
		assertEquals("Mas proveedores",
			driver.findElement(By.xpath("//table[@id='staysTable']/tbody/tr[2]/td/a/strong")).getText());
		assertEquals("Mas clínicas",
			driver.findElement(By.xpath("//table[@id='staysTable']/tbody/tr[3]/td/a/strong")).getText());
		driver.findElement(By.xpath("//table[@id='staysTable']/tbody/tr/td[3]/a/span")).click();
		driver.findElement(By.xpath("//table[@id='staysTable']/tbody/tr/td[3]/a/span")).click();
		driver.findElement(By.xpath("//table[@id='staysTable']/tbody/tr/td[3]/a/span")).click();
		assertEquals("Mas clínicas",
			driver.findElement(By.xpath("//table[@id='staysTable']/tbody/tr/td/a/strong")).getText());
		assertEquals("Intervalo de tiempo en estancias",
			driver.findElement(By.xpath("//table[@id='staysTable']/tbody/tr[2]/td/a/strong")).getText());
		assertEquals("Mas proveedores",
			driver.findElement(By.xpath("//table[@id='staysTable']/tbody/tr[3]/td/a/strong")).getText());

		LogOut();

	}

	@Test
	public void testReadSuggestionNotFound() throws Exception {
		driver.get("http://localhost:" + port);

		LogInAsAdmin();

		//Accede a la lista de suggestions y comprueba que ha llegado
		driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[3]/a/span[2]")).click();
		driver.findElement(By.xpath("//h2")).click();
		assertEquals("Suggestions Received", driver.findElement(By.xpath("//h2")).getText());

		//Intenta marcar mediante la url una suggestion que no existe
		driver.get("http://localhost:" + port + "/suggestion/admin/read/999");

		//Comprueba que ha llegado a la pagina de error
		driver.findElement(By.xpath("//h2")).click();
		assertEquals("Something happened...", driver.findElement(By.xpath("//h2")).getText());
		driver.findElement(By.xpath("//body/div/div/p")).click();
		assertEquals("No value present", driver.findElement(By.xpath("//body/div/div/p")).getText());

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
