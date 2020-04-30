package org.springframework.samples.petclinic.ui;


import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.Assert.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReadSuggestionsAsAdminUITest {
	

	@LocalServerPort
	private int port;
	
	private WebDriver driver;
	private StringBuffer verificationErrors = new StringBuffer();

	@BeforeEach
	public void setUp() throws Exception {		
		driver = new FirefoxDriver();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	@Test
	public void testUntitledTestCase() throws Exception {
		driver.get("http://localhost:" + port);
		
		LogInAsAdmin();
	    
	    driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[3]/a/span[2]")).click();
	    driver.findElement(By.xpath("//h2")).click();
	    assertEquals("Suggestions Received", driver.findElement(By.xpath("//h2")).getText());
	    assertEquals("Intervalo de tiempo en estancias", driver.findElement(By.xpath("//table[@id='staysTable']/tbody/tr/td/a/strong")).getText());
	    assertEquals("Mas proveedores", driver.findElement(By.xpath("//table[@id='staysTable']/tbody/tr[2]/td/a/strong")).getText());
	    assertEquals("Mas clínicas", driver.findElement(By.xpath("//table[@id='staysTable']/tbody/tr[3]/td/a/strong")).getText());
	    driver.findElement(By.xpath("//table[@id='staysTable']/tbody/tr/td[3]/a/span")).click();
	    driver.findElement(By.xpath("//table[@id='staysTable']/tbody/tr/td[3]/a/span")).click();
	    driver.findElement(By.xpath("//table[@id='staysTable']/tbody/tr/td[3]/a/span")).click();
	    assertEquals("Mas clínicas", driver.findElement(By.xpath("//table[@id='staysTable']/tbody/tr/td/a/strong")).getText());
	    assertEquals("Intervalo de tiempo en estancias", driver.findElement(By.xpath("//table[@id='staysTable']/tbody/tr[2]/td/a/strong")).getText());
	    assertEquals("Mas proveedores", driver.findElement(By.xpath("//table[@id='staysTable']/tbody/tr[3]/td/a/strong")).getText());
	    
		LogOut();

	}


	@AfterEach
	public void tearDown() throws Exception {
		driver.quit();
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
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
