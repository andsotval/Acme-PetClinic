
package org.springframework.samples.petclinic.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class SetAcceptedVisitsAsVetUITests extends AbstractUITests {

	@Test
	public void testSetAcceptedVisitsAsVetCorrectFields() throws Exception {
		driver.get("http://localhost:" + port);

		LogInAsVet();

		driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/a/span[2]")).click();
		driver.findElement(By.linkText("List my accepted visits")).click();
		driver.findElement(By.xpath("//table[@id='vetsTable']/tbody/tr/td[4]/a/span")).click();
		driver.findElement(By.id("description")).click();
		driver.findElement(By.id("description")).clear();
		driver.findElement(By.id("description")).sendKeys("Visita Editada");
		driver.findElement(By.id("dateTime")).click();
		driver.findElement(By.id("dateTime")).click();
		driver.findElement(By.id("dateTime")).clear();
		driver.findElement(By.id("dateTime")).sendKeys("2021/08/09 09:30");
		driver.findElement(By.name("authorized")).click();
		assertEquals("Visit succesfully updated", driver.findElement(By.xpath("/html/body/div/div/div[1]")).getText());

		LogOut();

	}

	@Test
	public void testSetAcceptedVisitsAsVetErrorsOnFields() throws Exception {
		driver.get("http://localhost:" + port);

		LogInAsVet();

		driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/a/span[2]")).click();
		driver.findElement(By.linkText("List my accepted visits")).click();
		driver.findElement(By.xpath("//table[@id='vetsTable']/tbody/tr/td[4]/a/span")).click();

		driver.findElement(By.id("description")).click();
		driver.findElement(By.id("description")).clear();
		driver.findElement(By.id("dateTime")).click();
		driver.findElement(By.id("dateTime")).click();
		driver.findElement(By.id("dateTime")).clear();
		driver.findElement(By.name("authorized")).click();
		assertEquals("no puede estar vacío",
			driver.findElement(By.xpath("//form[@id=\"visit\"]/div/div/div/span[2]")).getText());
		assertEquals("no puede ser null",
			driver.findElement(By.xpath("//form[@id=\"visit\"]/div/div[2]/div/span[2]")).getText());
		driver.findElement(By.id("dateTime")).click();
		driver.findElement(By.id("dateTime")).clear();
		driver.findElement(By.id("dateTime")).sendKeys("2019/08/09 09:30");
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

	private WebDriver LogInAsVet() {
		driver.findElement(By.xpath("//a[contains(text(),'Login')]")).click();
		driver.findElement(By.id("username")).click();
		driver.findElement(By.id("username")).clear();
		driver.findElement(By.id("username")).sendKeys("vet1");
		driver.findElement(By.id("password")).click();
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("vet1");
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
