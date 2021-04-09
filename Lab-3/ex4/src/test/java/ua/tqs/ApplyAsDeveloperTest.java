package ua.tqs;

import ua.tqs.webpages.FreelancerApplyPage;
import ua.tqs.webpages.HomePage;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.net.URL;
import java.util.concurrent.TimeUnit;

public class ApplyAsDeveloperTest {
   WebDriver driver;

   @BeforeEach
   public void setup(){
       driver = new ChromeDriver();
       driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
       driver.manage().window().maximize();
   }

   @Test
   public void applyAsDeveloper() {
       //Create object of HomePage Class
       HomePage home = new HomePage(driver);
       home.clickOnApplyFreelancer();

       //Create object of DeveloperApplyPage
       FreelancerApplyPage applyPage =new FreelancerApplyPage(driver);

       //Check if page is opened
       Assertions.assertTrue(applyPage.isPageOpened());

       //Fill up data
       applyPage.setFreelancer_email("dejan@toptal.com");
       applyPage.setFreelancer_full_name("Dejan Zivanovic Automated Test");
       applyPage.setFreelancer_password("password123");
       applyPage.setFreelancer_password_confirmation("password123");

       //Click on join
       //applyPage.clickOnJoin(); 
   }

    @AfterEach
    public void close(){
          driver.close();
       }
   }