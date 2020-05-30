
package org.springframework.samples.petclinic.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AddVetsToClinicAsManagerUITests extends AbstractUITests {

	@Test
	public void testAddVetsToClinicAsManagerSuccesfull() throws Exception {
		driver.get("http://localhost:" + port);

		LogInAsManager();

		//selecciona el boton veterinarians
		driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/a/span[2]")).click();

		//revisa que el encabezado sea Veterinarians y el de las tablas sea correcto
		assertEquals("Veterinarians", driver.findElement(By.xpath("//h1")).getText());
		assertEquals("Available veterinarians for hire", driver.findElement(By.xpath("//h2[1]")).getText());
		assertEquals("Veterinarians hired by your clinic", driver.findElement(By.xpath("//h2[2]")).getText());

		String name = driver.findElement(By.xpath("//table[@id=\"vetsAvailableTable\"]/tbody/tr[1]/td[1]/a")).getText();
		String specialties = driver.findElement(By.xpath("//table[@id=\"vetsAvailableTable\"]/tbody/tr[1]/td[2]"))
			.getText();

		//añade a la clinica el veterinario de la primera linea
		driver.findElement(By.xpath("//table[@id=\"vetsAvailableTable\"]/tbody/tr/td[5]/a/span")).click();

		//comprueba que ahora ese elemento con ese nombre pertenece a la segunda tabla
		assertEquals(name,
			driver.findElement(By.xpath("//*[@id=\"vetsOfClinicTable\"]/tbody/tr[2]/td[1]/a")).getText());

		//entramos a la vista de detalles de ese veterinario
		driver.findElement(By.xpath("//*[@id=\"vetsOfClinicTable\"]/tbody/tr[2]/td[1]/a")).click();

		//se comprueban los datos de este  /html/body/div/div/table/tbody/tr[2]/td
		assertEquals(name, driver.findElement(By.xpath("//table[@id=\"vetTable\"]/tbody/tr[1]/td/b")).getText());
		assertEquals("C/Andalucia, 34",
			driver.findElement(By.xpath("//table[@id=\"vetTable\"]/tbody/tr[2]/td")).getText());
		assertEquals("Bilbao", driver.findElement(By.xpath("//table[@id=\"vetTable\"]/tbody/tr[3]/td")).getText());
		assertEquals("679123162", driver.findElement(By.xpath("//table[@id=\"vetTable\"]/tbody/tr[4]/td")).getText());
		assertEquals(specialties, driver.findElement(By.xpath("//table[@id=\"vetTable\"]/tbody/tr[5]/td")).getText());

		//volvemos a la lista y desconectamos
		driver.findElement(By.linkText("Back to list of veterinarian")).click();
		LogOut();

	}

	@Test
	public void testAddVetsToClinicAsManagerWithVetAlreadyLinkedToAClinic() throws Exception {
		driver.get("http://localhost:" + port);

		LogInAsManager();

		//selecciona el boton veterinarians
		driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/a/span[2]")).click();

		//revisa que el encabezado sea Veterinarians y el de las tablas sea correcto
		assertEquals("Veterinarians", driver.findElement(By.xpath("//h1")).getText());
		assertEquals("Available veterinarians for hire", driver.findElement(By.xpath("//h2[1]")).getText());
		assertEquals("Veterinarians hired by your clinic", driver.findElement(By.xpath("//h2[2]")).getText());
		//Intenta añadir un veterinario con una clinica ya asignada
		driver.get("http://localhost:" + port + "/vets/accept/1");

		//Comprueba que ha llegado a la pagina de error
		assertEquals("Something happened...", driver.findElement(By.xpath("//h2")).getText());
		assertEquals("Expected: controller used to showcase what happens when an exception is thrown",
			driver.findElement(By.xpath("//body/div/div/p")).getText());

		LogOut();

	}

	@Test
	public void testAddVetsToClinicAsManagerVetNotFound() throws Exception {
		driver.get("http://localhost:" + port);

		LogInAsManager();

		//selecciona el boton veterinarians
		driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/a/span[2]")).click();

		//revisa que el encabezado sea Veterinarians y el de las tablas sea correcto
		assertEquals("Veterinarians", driver.findElement(By.xpath("//h1")).getText());
		assertEquals("Available veterinarians for hire", driver.findElement(By.xpath("//h2[1]")).getText());
		assertEquals("Veterinarians hired by your clinic", driver.findElement(By.xpath("//h2[2]")).getText());
		//Intenta añadir un veterinario no existente
		driver.get("http://localhost:" + port + "/vets/accept/321");

		//Comprueba que ha llegado a la pagina de error
		assertEquals("Something happened...", driver.findElement(By.xpath("//h2")).getText());
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

	private WebDriver LogInAsManager() {
		driver.findElement(By.xpath("//a[contains(text(),'Login')]")).click();
		driver.findElement(By.id("username")).click();
		driver.findElement(By.id("username")).clear();
		driver.findElement(By.id("username")).sendKeys("manager1");
		driver.findElement(By.id("password")).click();
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("manager1");
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
