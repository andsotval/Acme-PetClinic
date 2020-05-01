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
public class SetAcceptedVisitsAsVetUITest {
	

	@LocalServerPort
	private int port;
	
	private WebDriver driver;
	private StringBuffer verificationErrors = new StringBuffer();

	@BeforeEach
	public void setUp() throws Exception {	
		
		//String pathToGeckoDriver="C:\\Users\\geste\\Desktop\\DP";
		//System.setProperty("webdriver.gecko.driver", pathToGeckoDriver + "\\geckodriver.exe");
		
		driver = new FirefoxDriver();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	@Test
	public void testUntitledTestCase() throws Exception {
		driver.get("http://localhost:" + port);
		
		LogInAsVet();
	    
		driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/a/span[2]")).click();
	    driver.findElement(By.linkText("List my accepted visits")).click();
	    driver.findElement(By.xpath("//table[@id='vetsTable']/tbody/tr/td[4]/a/span")).click();
	    driver.findElement(By.id("dateTime")).click();
	    driver.findElement(By.id("dateTime")).click();
	    driver.findElement(By.id("dateTime")).clear();
	    driver.findElement(By.id("dateTime")).sendKeys("2021/08/09 09:30:00");
	    driver.findElement(By.name("authorized")).click();
	    driver.findElement(By.xpath("//table[@id='vetsTable']/tbody/tr/td")).click();
		
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
