
package org.springframework.samples.petclinic.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.time.LocalDate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class NewStayAsOwnerUITests extends AbstractUITests {

	private LocalDate	startdate				= LocalDate.now().plusMonths(3L);
	private LocalDate	finishdate				= LocalDate.now().plusMonths(3L).plusDays(4L);
	private LocalDate	startdateWrong			= LocalDate.now().minusMonths(3L);
	private LocalDate	finishdateWrong			= LocalDate.now().plusMonths(3L).plusDays(4L);
	private LocalDate	finishdateWrongTooEarly	= LocalDate.now().plusMonths(3L).minusDays(4L);


	@Test
	public void testNewStayCorrectFields() throws Exception {
		driver.get("http://localhost:" + port);

		LogInAsOwner();

		driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/a/span[2]")).click();
		driver.findElement(By.xpath("//table[@id='petsTable']/tbody/tr/td")).click();
		assertEquals("Basil", driver.findElement(By.linkText("Basil")).getText());
		driver.findElement(By.xpath("//table[@id='petsTable']/tbody/tr/td[5]/a/span")).click();
		driver.findElement(By.xpath("//h2")).click();
		assertEquals("New Stay", driver.findElement(By.xpath("//h2")).getText());
		driver.findElement(By.xpath("//body/div")).click();
		assertEquals("Basil", driver.findElement(By.xpath("//td")).getText());
		driver.findElement(By.id("description")).click();
		driver.findElement(By.id("description")).click();
		driver.findElement(By.id("description")).clear();
		driver.findElement(By.id("description")).sendKeys("UI TESTING");
		driver.findElement(By.id("startDate")).click();
		driver.findElement(By.id("startDate")).clear();
		driver.findElement(By.id("startDate")).sendKeys(dateToString(startdate));
		driver.findElement(By.id("finishDate")).click();
		driver.findElement(By.id("finishDate")).click();
		driver.findElement(By.id("finishDate")).clear();
		driver.findElement(By.id("finishDate")).sendKeys(dateToString(finishdate));
		driver.findElement(By.xpath("//form[@id='stay']/div[2]/div")).click(); //repetimos el click para que de tiempo
		driver.findElement(By.xpath("//form[@id='stay']/div[2]/div")).click(); //a cerrar el selector de fecha
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		driver.findElement(By.xpath("//table[@id='staysTable']/tbody/tr[2]/td")).click();
		assertEquals("Basil", driver.findElement(By.xpath("//table[@id='staysTable']/tbody/tr[2]/td")).getText());
		driver.findElement(By.xpath("//table[@id='staysTable']/tbody/tr[2]/td[2]")).click();
		assertEquals(dateToStringInTable(startdate),
			driver.findElement(By.xpath("//table[@id='staysTable']/tbody/tr[2]/td[2]")).getText());
		driver.findElement(By.xpath("//body/div")).click();
		driver.findElement(By.xpath("//table[@id='staysTable']/tbody/tr[2]/td[3]")).click();
		assertEquals(dateToStringInTable(finishdate),
			driver.findElement(By.xpath("//table[@id='staysTable']/tbody/tr[2]/td[3]")).getText());
		driver.findElement(By.xpath("//table[@id='staysTable']/tbody/tr[2]/td[4]")).click();
		assertEquals("UI TESTING",
			driver.findElement(By.xpath("//table[@id='staysTable']/tbody/tr[2]/td[4]")).getText());
		driver.findElement(By.xpath("//table[@id='staysTable']/tbody/tr[2]/td[5]")).click();
		assertEquals("PENDING", driver.findElement(By.xpath("//table[@id='staysTable']/tbody/tr[2]/td[5]")).getText());
		driver.findElement(By.xpath("//h1")).click();
		assertEquals("My Stays", driver.findElement(By.xpath("//h1")).getText());
		driver.findElement(By.xpath("//h2[2]")).click();
		assertEquals("Stays waiting for Acceptance", driver.findElement(By.xpath("//h2[2]")).getText());

		LogOut();
	}

	@Test
	public void testNewStayErrorsOnFields() throws Exception {
		driver.get("http://localhost:" + port);

		LogInAsOwner();

		//Accede a la lista de mascotas
		driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/a/span[2]")).click();
		driver.findElement(By.xpath("//table[@id='petsTable']/tbody/tr/td")).click();
		assertEquals("Basil", driver.findElement(By.linkText("Basil")).getText()); //Comprueba el nombre del perro

		//Accede al formulario para crear una nueva estancia y comprueba que ha llegado bien
		driver.findElement(By.xpath("//table[@id='petsTable']/tbody/tr/td[5]/a/span")).click();
		assertEquals("New Stay", driver.findElement(By.xpath("//h2")).getText());
		assertEquals("Basil", driver.findElement(By.xpath("//td")).getText());
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		assertEquals("no puede estar vacío",
			driver.findElement(By.xpath("//form[@id='stay']/div/div/div/span[2]")).getText());
		assertEquals("no puede ser null",
			driver.findElement(By.xpath("//form[@id='stay']/div/div[2]/div/span[2]")).getText());
		assertEquals("no puede ser null",
			driver.findElement(By.xpath("//form[@id='stay']/div/div[3]/div/span[2]")).getText());

		driver.findElement(By.id("description")).click();
		driver.findElement(By.id("description")).click();
		driver.findElement(By.id("description")).clear();
		driver.findElement(By.id("description")).sendKeys("UI TESTING");

		//Rellena los campos de fechas con valores erroneos y comprueba que muestra los mensajes pertinentes
		driver.findElement(By.id("startDate")).click();
		driver.findElement(By.id("startDate")).clear();
		driver.findElement(By.id("startDate")).sendKeys(dateToString(startdateWrong));
		driver.findElement(By.id("finishDate")).click();
		driver.findElement(By.id("finishDate")).click();
		driver.findElement(By.id("finishDate")).clear();
		driver.findElement(By.id("finishDate")).sendKeys(dateToString(finishdateWrong));
		driver.findElement(By.xpath("//form[@id='stay']/div[2]/div")).click(); //repetimos el click para que de tiempo
		driver.findElement(By.xpath("//form[@id='stay']/div[2]/div")).click(); //a cerrar el selector de fecha
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		assertEquals("Minimum 2 days after today",
			driver.findElement(By.xpath("//form[@id='stay']/div/div[2]/div/span[2]")).getText());
		assertEquals("Stays cannot last longer than one week",
			driver.findElement(By.xpath("//form[@id='stay']/div/div[3]/div/span[2]")).getText());

		driver.findElement(By.id("startDate")).click();
		driver.findElement(By.id("startDate")).clear();
		driver.findElement(By.id("startDate")).sendKeys(dateToString(startdate));
		driver.findElement(By.id("finishDate")).click();
		driver.findElement(By.id("finishDate")).click();
		driver.findElement(By.id("finishDate")).clear();
		driver.findElement(By.id("finishDate")).sendKeys(dateToString(finishdateWrongTooEarly));
		driver.findElement(By.xpath("//form[@id='stay']/div[2]/div")).click(); //repetimos el click para que de tiempo
		driver.findElement(By.xpath("//form[@id='stay']/div[2]/div")).click(); //a cerrar el selector de fecha
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		assertEquals("The finish date must be after the start date",
			driver.findElement(By.xpath("//form[@id='stay']/div/div[3]/div/span[2]")).getText());

		LogOut();
	}

	private String dateToString(LocalDate date) {
		String s = "";
		if (date.getMonthValue() < 10)
			s = "0";

		String r = "";
		if (date.getDayOfMonth() < 10)
			r = "0";
		return date.getYear() + "/" + s + date.getMonthValue() + "/" + r + date.getDayOfMonth();
	}

	private String dateToStringInTable(LocalDate date) {
		String s = "";
		if (date.getMonthValue() < 10)
			s = "0";

		String r = "";
		if (date.getDayOfMonth() < 10)
			r = "0";
		return date.getYear() + "-" + s + date.getMonthValue() + "-" + r + date.getDayOfMonth();
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

	@AfterEach
	public void tearDown() throws Exception {
		driver.quit();
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString))
			fail(verificationErrorString);
	}

}
