
package org.springframework.samples.petclinic.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AddVetsToClinicAsManagerNotPresentVetUITest {

	@LocalServerPort
	private int				port;

	private WebDriver		driver;
	private StringBuffer	verificationErrors	= new StringBuffer();


	@BeforeEach
	public void setUp() throws Exception {

		//		String pathToGeckoDriver = "C:\\Users\\migue\\Desktop";
		//		System.setProperty("webdriver.gecko.driver", pathToGeckoDriver + "\\geckodriver.exe");

		driver = new FirefoxDriver();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	@Test
	public void testUntitledTestCase() throws Exception {
		driver.get("http://localhost:" + port);

		LogInAsManager();

		//selecciona el boton veterinarians
		driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/a/span[2]")).click();

		//revisa que el encabezado sea Veterinarians y el de las tablas sea correcto
		driver.findElement(By.xpath("//h1")).click();
		assertEquals("Veterinarians", driver.findElement(By.xpath("//h1")).getText());
		driver.findElement(By.xpath("/html/body/div/div/h2[1]")).click();
		assertEquals("Available veterinarians for hire",
			driver.findElement(By.xpath("/html/body/div/div/h2[1]")).getText());
		driver.findElement(By.xpath("/html/body/div/div/h2[2]")).click();
		assertEquals("Veterinarians hired by your clinic",
			driver.findElement(By.xpath("/html/body/div/div/h2[2]")).getText());
		//Intenta añadir un veterinario no existente
		driver.get("http://localhost:" + port + "/vets/accept/321");

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
