package ua.tqs;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class AppTest {
    WebDriver browser;

    @BeforeEach
    public void setup() {
        // System.setProperty("webdriver.chrome.driver", "/opt/WebDriver/bin/chromedriver");
		browser = new ChromeDriver();
        browser.manage().window().maximize();
    }

    @AfterEach
    public void teardown() {
		browser.close();    
    }

    @Test
    public void tutorialTest() throws InterruptedException {

		browser.get("https://www.saucelabs.com");

	    WebElement href = browser.findElement(By.xpath("//a[@href='https://accounts.saucelabs.com/']"));
		assertTrue((href.isDisplayed()));
    }

    
}
