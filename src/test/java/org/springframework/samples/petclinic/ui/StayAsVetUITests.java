
package org.springframework.samples.petclinic.ui;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class StayAsVetUITests extends AbstractUITests {

	@Test
	public void testCheckListStayPending() throws Exception {
		driver.get("http://localhost:" + port);

		LogInAsOwner("vet1", "vet1");

		driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[3]/a/span[2]")).click();

		//CheckUser
		try {
			assertTrue(driver.findElements(By.xpath("//table[@id='staysTable']")).size() == 1);
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}
		LogOut();

	}

	@Test
	public void testCheckAcceptStayPending() throws Exception {
		driver.get("http://localhost:" + port);

		LogInAsOwner("vet1", "vet1");

		driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[3]/a/span[2]")).click();

		//CheckUser
		try {
			assertTrue(driver.findElements(By.xpath("//table[@id='staysTable']")).size() == 1);
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}

		driver.findElement(By.xpath("//table[@id='staysTable']/tbody/tr/td[5]/a/span")).click();

		LogOut();

	}

	@Test
	public void testCheckModifyStayPending() throws Exception {
		driver.get("http://localhost:" + port);

		LogInAsOwner("vet1", "vet1");

		driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[3]/a/span[2]")).click();

		//CheckUser
		try {
			assertTrue(driver.findElements(By.xpath("//table[@id='staysTable']")).size() == 1);
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}

		driver.findElement(By.linkText("List my accepted stays")).click();
		driver.findElement(By.xpath("//table[@id='staysTable']/tbody/tr/td[5]/a/span")).click();
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

		LogOut();

	}

	@Test
	public void testCheckDeleteStayPending() throws Exception {
		driver.get("http://localhost:" + port);

		LogInAsOwner("vet1", "vet1");

		driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[3]/a/span[2]")).click();

		//CheckUser
		try {
			assertTrue(driver.findElements(By.xpath("//table[@id='staysTable']")).size() == 1);
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}

		driver.findElement(By.xpath("//table[@id='staysTable']/tbody/tr/td[6]/a/span")).click();

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
