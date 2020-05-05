
package org.springframework.samples.petclinic.ui;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ClinicDetailAsVetUITests extends AbstractUITests {

	@Test
	public void testCheckDetailClinic() throws Exception {
		driver.get("http://localhost:" + port);

		LogInAsOwner("vet1", "vet1");

		driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[4]/a/span[2]")).click();

		//CheckName
		try {
			assertNotNull(driver.findElement(By.xpath("//td")).getText());
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}
		try {
			assertTrue(driver.findElement(By.xpath("//td")).getText() != "");
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}
		//CheckAddress
		try {
			assertNotNull(driver.findElement(By.xpath("//tr[2]/td")).getText());
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}
		try {
			assertTrue(driver.findElement(By.xpath("//tr[2]/td")).getText() != "");
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}
		//CheckCity
		try {
			assertNotNull(driver.findElement(By.xpath("//tr[3]/td")).getText());
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}
		try {
			assertTrue(driver.findElement(By.xpath("//tr[3]/td")).getText() != "");
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}
		//CheckTelephone
		try {
			assertNotNull(driver.findElement(By.xpath("//tr[4]/td")).getText());
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}
		try {
			assertTrue(driver.findElement(By.xpath("//tr[4]/td")).getText() != "");
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}
		//CheckUser
		try {
			assertTrue(driver.findElements(By.xpath("//table[@id='ownersTable']")).size() == 1);
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}
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
