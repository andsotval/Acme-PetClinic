package org.springframework.samples.petclinic.ui;

import java.util.regex.Pattern;
import java.time.LocalDate;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class NewStayAsOwnerUITest {
	
	private LocalDate startdate = LocalDate.now().plusMonths(3L);
	private LocalDate finishdate = LocalDate.now().plusMonths(3L).plusDays(4L);
	

	@LocalServerPort
	private int port;
	
	private WebDriver driver;
	private String baseUrl;
	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();

	@BeforeEach
	public void setUp() throws Exception {
		driver = new FirefoxDriver();
		baseUrl = "https://www.google.com/";
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	@Test
	public void testUntitledTestCase() throws Exception {
		driver.get("http://localhost:" + port /*+ "/login"*/);
	    driver.findElement(By.xpath("//a[contains(text(),'Login')]")).click();
	    driver.findElement(By.id("username")).click();
	    driver.findElement(By.id("username")).clear();
	    driver.findElement(By.id("username")).sendKeys("owner1");
	    driver.findElement(By.id("password")).click();
	    driver.findElement(By.id("password")).clear();
	    driver.findElement(By.id("password")).sendKeys("owner1");
	    driver.findElement(By.xpath("//button[@type='submit']")).click();
	    driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/a/span[2]")).click();
	    driver.findElement(By.xpath("//table[@id='petsTable']/tbody/tr/td")).click();
	    assertEquals("Leo", driver.findElement(By.linkText("Leo")).getText());
	    driver.findElement(By.xpath("//table[@id='petsTable']/tbody/tr/td[5]/a/span")).click();
	    driver.findElement(By.xpath("//h2")).click();
	    assertEquals("New Stay", driver.findElement(By.xpath("//h2")).getText());
	    driver.findElement(By.xpath("//body/div")).click();
	    assertEquals("Leo", driver.findElement(By.xpath("//td")).getText());
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
	    driver.findElement(By.xpath("//form[@id='stay']/div[2]/div")).click();
	    driver.findElement(By.xpath("//button[@type='submit']")).click();
	    driver.findElement(By.xpath("//table[@id='staysTable']/tbody/tr[2]/td")).click();
	    assertEquals("Leo", driver.findElement(By.xpath("//table[@id='staysTable']/tbody/tr[2]/td")).getText());
	    driver.findElement(By.xpath("//table[@id='staysTable']/tbody/tr[2]/td[2]")).click();
	    assertEquals(dateToStringInTable(startdate), driver.findElement(By.xpath("//table[@id='staysTable']/tbody/tr[2]/td[2]")).getText());
	    driver.findElement(By.xpath("//body/div")).click();
	    driver.findElement(By.xpath("//table[@id='staysTable']/tbody/tr[2]/td[3]")).click();
	    assertEquals(dateToStringInTable(finishdate), driver.findElement(By.xpath("//table[@id='staysTable']/tbody/tr[2]/td[3]")).getText());
	    driver.findElement(By.xpath("//table[@id='staysTable']/tbody/tr[2]/td[4]")).click();
	    assertEquals("UI TESTING", driver.findElement(By.xpath("//table[@id='staysTable']/tbody/tr[2]/td[4]")).getText());
	    driver.findElement(By.xpath("//table[@id='staysTable']/tbody/tr[2]/td[5]")).click();
	    assertEquals("PENDING", driver.findElement(By.xpath("//table[@id='staysTable']/tbody/tr[2]/td[5]")).getText());
	    driver.findElement(By.xpath("//h1")).click();
	    assertEquals("My Stays", driver.findElement(By.xpath("//h1")).getText());
	    driver.findElement(By.xpath("//h2[2]")).click();
	    assertEquals("Stays waiting for Acceptance", driver.findElement(By.xpath("//h2[2]")).getText());
	    driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a/strong")).click();
	    driver.findElement(By.linkText("Logout")).click();
	    driver.findElement(By.xpath("//button[@type='submit']")).click();
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

	@AfterEach
	public void tearDown() throws Exception {
		driver.quit();
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	}

	private boolean isElementPresent(By by) {
		try {
			driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	private boolean isAlertPresent() {
		try {
			driver.switchTo().alert();
			return true;
		} catch (NoAlertPresentException e) {
			return false;
		}
	}

	private String closeAlertAndGetItsText() {
		try {
			Alert alert = driver.switchTo().alert();
			String alertText = alert.getText();
			if (acceptNextAlert) {
				alert.accept();
			} else {
				alert.dismiss();
			}
			return alertText;
		} finally {
			acceptNextAlert = true;
		}
	}
}
