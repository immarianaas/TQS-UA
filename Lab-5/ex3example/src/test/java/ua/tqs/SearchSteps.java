package ua.tqs;

import java.util.NoSuchElementException;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.bonigarcia.wdm.WebDriverManager;

public class SearchSteps {
    private WebDriver webDriver;
    
    @When("I navigate to {string}")
    public void i_navigate_to(String url) {
        WebDriverManager.chromedriver().setup();
        webDriver = new ChromeDriver();
        webDriver.get(url);
    }

    @When("I type {string}")
    public void i_type(String string) {
        webDriver.findElement(By.name("q")).sendKeys(string);
    }

    @When("I press Enter")
    public void i_press_enter() {
        webDriver.findElement(By.name("q")).sendKeys(Keys.ENTER);
    }

    @Then("I should be shown results including {string}")
    public void i_should_be_shown_results_including(String result) {
        try {
            webDriver.findElement(
                By.xpath("//*[contains(text(), '" + result + "')]"));
        } catch (NoSuchElementException e) {
            throw new AssertionError("\"" + result + "\" not available in results");
        } finally {
            webDriver.quit();
        }
            
    }
}
