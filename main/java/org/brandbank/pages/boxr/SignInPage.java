package org.brandbank.pages.boxr;
import com.aventstack.extentreports.Status;
import org.brandbank.base.BaseClass;
import org.brandbank.libs.BrowserActions;
import org.brandbank.pages.automatedMailer.OutlookPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class SignInPage extends BaseClass {
        BrowserActions browserActions;
        public static WebDriver driver;
        OutlookPage outlookPage;
        public SignInPage(WebDriver driver) {
            this.driver = driver;
            browserActions = new BrowserActions(this.driver);
            PageFactory.initElements(driver,this);
            outlookPage=new OutlookPage(driver);
        }
        By signInBtn=By.xpath("//h2[normalize-space()='Sign-In']");
        By email=By.xpath("//input[@id='email']");
        By password=By.xpath("//input[@id='password']");
        By nextBtn=By.xpath("//button[@id='next']");
        By homeBtn=By.xpath("//h2[normalize-space()='home']");
        By verificationBtn=By.xpath("(//button[normalize-space()='Send verification code'])[1]");
        By enterVerificationCode=By.xpath("//input[@id='ReadOnlyEmail_ver_input']");
        By verificationCode=By.xpath("//input[@id='ReadOnlyEmail_ver_input']");
        By continueBtn=By.xpath("//button[@id='continue'][@type='submit']");
        By verifyCode=By.xpath("(//button[normalize-space()='Verify code'])[1]");
        By homePage=By.xpath("//h2[normalize-space()='home']");
        By logOutBtn = By.xpath("//div[@class='info']");
        By signOutBtn = By.xpath("//div[@class='button signout']");
        public void signIn(String userName,String pwd){
            browserActions.waitForElementToBeVisible(signInBtn);
            browserActions.click(signInBtn);
            browserActions.sendKeys(email,userName);
            browserActions.sendKeys(password, pwd);
            browserActions.click(nextBtn);
        }
        public void sendCodeToVerify (){
            browserActions.waitForElementToBeVisible(verificationBtn);
            browserActions.click(verificationBtn);

        }
        public void verifyUserDetails() throws Exception {
            try {
                String code = outlookPage.getEmailCode();
                System.out.println(code);
                extentTest.log(Status.INFO,code);
                Thread.sleep(3000);
                outlookPage.deleteMail("Azure.Brandbank.Dev");
                System.out.println("Mail deleted");
                extentTest.log(Status.INFO,"Mail deleted");
                browserActions.switchToTab(1, 0);
                browserActions.waitForElementToBeVisible(verificationCode);
                browserActions.sendKeys(verificationCode, code);
                browserActions.click(verifyCode);
                Thread.sleep(3000);
                browserActions.click(continueBtn);
                browserActions.waitForElementToBeVisible(homePage);
                System.out.println("successfully entered into box-r home page");
                extentTest.log(Status.PASS,"successfully entered into box-r home page");
            }
            catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                }
        }

        public String verifyTitle(){
            browserActions.waitForElementToBeVisible(homeBtn);
            String title = browserActions.getPageTitle();
            return title;
        }

        public void signOut() throws InterruptedException {
            browserActions.click(logOutBtn);
            browserActions.waitForElementToBeVisible(signOutBtn);
            browserActions.click(signOutBtn);
            browserActions.waitForElementToBeVisible(signInBtn);
            Thread.sleep(5000);
            System.out.println("Signed out from Box R");
            extentTest.log(Status.PASS,"Signed out from Box R");
        }
}
