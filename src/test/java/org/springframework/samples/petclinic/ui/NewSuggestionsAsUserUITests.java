
package org.springframework.samples.petclinic.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class NewSuggestionsAsUserUITests extends AbstractUITests {

	@Test
	public void testUntitledTestCase() throws Exception {
		driver.get("http://localhost:" + port);

		LogInAsOwner();

		driver.get("http://localhost:" + port + "/suggestion/user/list");
		driver.findElement(By.xpath("//h2")).click();
		assertEquals("Suggestions send", driver.findElement(By.xpath("//h2")).getText());
		driver.findElement(By.linkText("New Suggestion")).click();
		driver.findElement(By.xpath("//h2")).click();
		assertEquals("New Suggestion", driver.findElement(By.xpath("//h2")).getText());
		driver.findElement(By.id("name")).click();
		driver.findElement(By.id("name")).clear();
		driver.findElement(By.id("name")).sendKeys("SUGGESTION");
		driver.findElement(By.id("description")).click();
		driver.findElement(By.id("description")).clear();
		driver.findElement(By.id("description")).sendKeys("UI TESTING SUGGESTION DESCRIPTION");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		assertEquals("Suggestions send", driver.findElement(By.xpath("//h2")).getText());
		assertEquals("SUGGESTION",
			driver.findElement(By.xpath("//table[@id='staysTable']/tbody/tr[3]/td/a/strong")).getText());

		LogOut();

	}

	@Test
	public void testNewSuggestionWrongParameters() throws Exception {
		driver.get("http://localhost:" + port);

		LogInAsOwner();

		driver.get("http://localhost:" + port + "/suggestion/user/list");
		driver.findElement(By.xpath("//h2")).click();
		assertEquals("Suggestions send", driver.findElement(By.xpath("//h2")).getText());
		driver.findElement(By.linkText("New Suggestion")).click();
		driver.findElement(By.xpath("//h2")).click();
		assertEquals("New Suggestion", driver.findElement(By.xpath("//h2")).getText());
		driver.findElement(By.id("name")).click();
		driver.findElement(By.id("name")).clear();
		driver.findElement(By.id("name")).sendKeys("");
		driver.findElement(By.id("description")).click();
		driver.findElement(By.id("description")).clear();
		driver.findElement(By.id("description")).sendKeys("TEST WITH NULL TITLE MUST SHOW AN ERROR");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		driver.findElement(By.xpath("//h2")).click();
		assertEquals("New Suggestion", driver.findElement(By.xpath("//h2")).getText());
		assertEquals("el tamaño tiene que estar entre 3 y 50",
			driver.findElement(By.xpath("//form[@id='create-suggestion-form']/div/div/span[2]")).getText());

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
