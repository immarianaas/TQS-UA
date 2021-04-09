package ua.tqs.webpages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class FreelancerApplyPage {
   private WebDriver driver;

   @FindBy(tagName = "h1")
   WebElement heading;

   @FindBy(css=".has-default_value")
   WebElement freelancer_type;

   @FindBy(id="talent_create_applicant_email")
   WebElement freelancer_email;

   @FindBy(id = "talent_create_applicant_password")
   WebElement freelancer_password;

   @FindBy(id = "talent_create_applicant_password_confirmation")
   WebElement freelancer_password_confirmation;

   @FindBy(id = "talent_create_applicant_full_name")
   WebElement freelancer_full_name;

   @FindBy(id ="save_new_talent_create_applicant")
   WebElement join_toptal_button;


   //Constructor
   public FreelancerApplyPage(WebDriver driver){
       this.driver=driver;

       //Initialise Elements
       PageFactory.initElements(driver, this);
   }

   public void setFreelancer_email(String email){
        freelancer_email.clear();
        freelancer_email.sendKeys(email);
   }

   public void setFreelancer_password(String password){
        freelancer_password.clear();
        freelancer_password.sendKeys(password);
   }

public void  setFreelancer_password_confirmation(String password_confirmation){
       freelancer_password_confirmation.clear();
       freelancer_password_confirmation.sendKeys(password_confirmation);
   }

   public void setFreelancer_full_name (String fullname){
        freelancer_full_name.clear();
        freelancer_full_name.sendKeys(fullname);
   }

   public void clickOnJoin(){
       join_toptal_button.click();
   }
   public boolean isPageOpened(){
       //Assertion
       return heading.getText().toString().contains("the World's Top Talent Network");
   }
}