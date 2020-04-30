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
public class ReadSuggestionsAsAdminWrongSuggestionUITest {
	

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
