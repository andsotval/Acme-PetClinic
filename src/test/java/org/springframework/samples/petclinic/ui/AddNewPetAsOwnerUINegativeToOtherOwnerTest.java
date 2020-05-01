
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
public class AddNewPetAsOwnerUINegativeToOtherOwnerTest {

	@LocalServerPort
	private int				port;

	private WebDriver		driver;
	private StringBuffer	verificationErrors	= new StringBuffer();


	@BeforeEach
	public void setUp() throws Exception {
		String pathToGeckoDriver = "C:\\Users\\96jos\\Documents";
		System.setProperty("webdriver.gecko.driver", pathToGeckoDriver + "\\geckodriver.exe");
		driver = new FirefoxDriver();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	@Test
	public void testUntitledTestCase() throws Exception {
		driver.get("http://localhost:" + port);

		LogInAsOwner();

		driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li/a/span[2]")).click();
		driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/a/span[2]")).click();
		driver.findElement(By.xpath("//body/div")).click();
		driver.findElement(By.linkText("Add new Pet")).click();
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		assertEquals("el tamaño tiene que estar entre 3 y 50", driver.findElement(By.xpath("//form[@id='pet']/div/div[2]/div/span[2]")).getText());
		driver.findElement(By.id("birthDate")).click();
		driver.findElement(By.id("birthDate")).click();
		driver.findElement(By.id("birthDate")).clear();
		driver.findElement(By.id("birthDate")).sendKeys("2100/04/04");
		driver.findElement(By.xpath("//body/div")).click();
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		assertEquals("the birth date cannot be in future", driver.findElement(By.xpath("//form[@id='pet']/div/div[3]/div/span[2]")).getText());
		driver.findElement(By.id("birthDate")).click();
		driver.findElement(By.id("birthDate")).clear();
		driver.findElement(By.id("birthDate")).sendKeys("2020/04/04");
		driver.findElement(By.id("name")).click();
		driver.findElement(By.id("name")).clear();
		driver.findElement(By.id("name")).sendKeys("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		assertEquals("el tamaño tiene que estar entre 3 y 50", driver.findElement(By.xpath("//form[@id='pet']/div/div[2]/div/span[2]")).getText());
		driver.findElement(By.id("name")).clear();
		driver.findElement(By.id("name")).sendKeys("");
		driver.findElement(By.id("name")).clear();
		driver.findElement(By.id("name")).sendKeys("Wiskers");
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
