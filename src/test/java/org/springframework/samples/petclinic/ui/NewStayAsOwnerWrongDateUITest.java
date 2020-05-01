package org.springframework.samples.petclinic.ui;

import java.time.LocalDate;
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
public class NewStayAsOwnerWrongDateUITest {
	
	private LocalDate startdate = LocalDate.now().minusMonths(3L);
	private LocalDate finishdate = LocalDate.now().plusMonths(3L).plusDays(4L);
	

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
	public void testNewStayWithWrongDate() throws Exception {
		driver.get("http://localhost:" + port);
	    
		LogInAsOwner();

		//Accede a la lista de mascotas
	    driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/a/span[2]")).click();
	    driver.findElement(By.xpath("//table[@id='petsTable']/tbody/tr/td")).click();
	    assertEquals("Leo", driver.findElement(By.linkText("Leo")).getText()); //Comprueba el nombre del perro
	    
	    //Accede al formulario para crear una nueva estancia y comprueba que ha llegado bien
	    driver.findElement(By.xpath("//table[@id='petsTable']/tbody/tr/td[5]/a/span")).click();
	    driver.findElement(By.xpath("//h2")).click();
	    assertEquals("New Stay", driver.findElement(By.xpath("//h2")).getText());
	    driver.findElement(By.xpath("//body/div")).click();
	    assertEquals("Leo", driver.findElement(By.xpath("//td")).getText());
	    driver.findElement(By.id("description")).click();
	    driver.findElement(By.id("description")).click();
	    driver.findElement(By.id("description")).clear();
	    driver.findElement(By.id("description")).sendKeys("UI TESTING");
	    
	    //Rellena los campos de fechas con valores erroneos y comprueba que muestra los mensajes pertinentes
	    driver.findElement(By.id("startDate")).click();
	    driver.findElement(By.id("startDate")).clear();
	    driver.findElement(By.id("startDate")).sendKeys(dateToString(startdate));
	    driver.findElement(By.id("finishDate")).click();
	    driver.findElement(By.id("finishDate")).click();
	    driver.findElement(By.id("finishDate")).clear();
	    driver.findElement(By.id("finishDate")).sendKeys(dateToString(finishdate));
	    driver.findElement(By.xpath("//form[@id='stay']/div[2]/div")).click();
	    driver.findElement(By.xpath("//button[@type='submit']")).click();
	    assertEquals("Minimum 2 days after today", driver.findElement(By.xpath("//form[@id='stay']/div/div[2]/div/span[2]")).getText());
	    assertEquals("Stays cannot last longer than one week", driver.findElement(By.xpath("//form[@id='stay']/div/div[3]/div/span[2]")).getText());
	    
	    LogOut();
	}

	private String dateToString(LocalDate date) {
		String s = "";
		if(date.getMonthValue() < 10) {
			s = "0";
		}
		
		String r = "";
		if(date.getDayOfMonth() < 10) {
			r = "0";
		}
		return date.getYear() + "/" + s + date.getMonthValue() + "/" + r + date.getDayOfMonth();
	}
	
	private String dateToStringInTable(LocalDate date) {
		String s = "";
		if(date.getMonthValue() < 10) {
			s = "0";
		}
		
		String r = "";
		if(date.getDayOfMonth() < 10) {
			r = "0";
		}
		return date.getYear() + "-" + s + date.getMonthValue() + "-" + r + date.getDayOfMonth();
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

	@AfterEach
	public void tearDown() throws Exception {
		driver.quit();
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	}

}
