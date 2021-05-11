package ua.tqs.airquality;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.JavascriptExecutor;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class WebInterfaceTest {


    private WebDriver driver;
    private Map<String, Object> vars;
    JavascriptExecutor js;

    @BeforeEach
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        js = (JavascriptExecutor) driver;
        vars = new HashMap<>();
    }
    @AfterEach
    public void tearDown() {
        driver.quit();
    }
    @Test
    public void hw() {
        driver.get("http://localhost:8080/");
        driver.manage().window().setSize(new Dimension(924, 1053));
        driver.findElement(By.cssSelector(".container")).click();
        assertThat(driver.findElement(By.cssSelector("h1")).getText(), is("Air Quality Forecast!"));
        driver.findElement(By.cssSelector("body")).click();
        assertThat(driver.findElement(By.cssSelector("h3")).getText(), is("Select your location:"));
        driver.findElement(By.id("dropdownMenuButton")).click();
        driver.findElement(By.id("helsinki")).click();
        driver.findElement(By.cssSelector(".container")).click();
        driver.findElement(By.cssSelector(".h5")).click();
        driver.findElement(By.cssSelector(".h5")).click();
        assertThat(driver.findElement(By.cssSelector("u")).getText(), is("Showing results for Helsinki"));
        driver.findElement(By.cssSelector("div:nth-child(3) > .table th:nth-child(2)")).click();
        driver.findElement(By.cssSelector("div:nth-child(3) > .table th:nth-child(2)")).click();
        {
            WebElement element = driver.findElement(By.cssSelector("div:nth-child(3) > .table th:nth-child(2)"));
            Actions builder = new Actions(driver);
            builder.doubleClick(element).perform();
        }
        assertThat(driver.findElement(By.cssSelector("div:nth-child(3) > .table th:nth-child(2)")).getText(), is("Avg"));
        driver.findElement(By.cssSelector("div:nth-child(3) > .table th:nth-child(3)")).click();
        driver.findElement(By.cssSelector("div:nth-child(3) > .table th:nth-child(3)")).click();
        {
            WebElement element = driver.findElement(By.cssSelector("div:nth-child(3) > .table th:nth-child(3)"));
            Actions builder = new Actions(driver);
            builder.doubleClick(element).perform();
        }
        assertThat(driver.findElement(By.cssSelector("div:nth-child(3) > .table th:nth-child(3)")).getText(), is("Min"));
        driver.findElement(By.cssSelector("div:nth-child(3) > .table th:nth-child(4)")).click();
        driver.findElement(By.cssSelector("div:nth-child(3) > .table th:nth-child(4)")).click();
        {
            WebElement element = driver.findElement(By.cssSelector("div:nth-child(3) > .table th:nth-child(4)"));
            Actions builder = new Actions(driver);
            builder.doubleClick(element).perform();
        }
        assertThat(driver.findElement(By.cssSelector("div:nth-child(3) > .table th:nth-child(4)")).getText(), is("Max"));
    }
}
