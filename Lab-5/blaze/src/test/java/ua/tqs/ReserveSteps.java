package ua.tqs;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.bonigarcia.wdm.WebDriverManager;

public class ReserveSteps {
    private WebDriver driver;

    @Given("that I am on the website {string}")
    public void that_i_am_on_the_website(String url) {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.get(url);
    }

    @When("I select {string} as depature")
    public void i_select_as_depature(String string) {
        WebElement dropdown = driver.findElement(By.name("fromPort"));
        dropdown.findElement(By.xpath("//option[. = '"+ string +"']")).click();
    }

    @When("I select {string} as destination")
    public void i_select_as_destination(String string) {
        WebElement dropdown = driver.findElement(By.name("toPort"));
        dropdown.findElement(By.xpath("//option[. = '" + string + "']")).click();
    }

    @When("I click the button")
    public void i_click_the_button() {
        driver.findElement(By.cssSelector(".btn-primary")).click();
    }
    
    @Then("{string} should appear on the page")
    public void should_appear_on_the_page(String string) {
        driver.findElement(
            By.xpath("//*[contains(text(), '" + string + "')]"));
    }


    @Given("I select {string} as departure")
    public void given__i_select_as_departure(String string) {
        i_select_as_depature(string);
    }

    @When("I select the option number {int}")
    public void i_select_the_option_number(Integer int1) {
        driver.findElement(By.cssSelector("tr:nth-child("+ int1 + ") .btn")).click();
    }

    @When("I type the name {string} on the name input")
    public void i_type_the_name_on_the_name_input(String string) {
        driver.findElement(By.id("inputName")).click();
        driver.findElement(By.id("inputName")).sendKeys("Mariana");
    }

    @When("I click on the purchase button")
    public void i_click_on_the_purchase_button() {
        driver.findElement(By.cssSelector(".btn-primary")).click();
    }

    @Then("status should be equal to {string}")
    public void status_should_be_equal_to(String string) {
        assertThat(driver.findElement(By.cssSelector("tr:nth-child(2) > td:nth-child(2)")).getText(), is("PendingCapture"));
    }

    @Then("page title should be equal to {string}")
    public void page_title_should_be_equal_to(String string) {
        assertThat(driver.getTitle(), is(string));
    }
}
