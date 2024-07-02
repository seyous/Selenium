package org.brandbank.pages.automatedMailer;

import com.aventstack.extentreports.Status;
import org.brandbank.base.BaseClass;
import org.brandbank.libs.BrowserActions;
import org.brandbank.libs.DateManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class OutlookPage extends BaseClass {
    BrowserActions browserActions;
    DateManager dateManager;
    public static WebDriver driver;

    public OutlookPage(WebDriver driver) {
        this.driver = driver;
        browserActions = new BrowserActions(this.driver);
        dateManager=new DateManager();
        PageFactory.initElements(driver,this);
    }
    By signInBtn=By.xpath("//nav[@aria-label='Quick links']//a[@class='internal sign-in-link'][normalize-space()='Sign in']");
    By emailToEnter=By.xpath("//input[@type='email']");
    By nextBtn=By.xpath("//input[@id='idSIButton9']");
    By passwordToEnter = By.xpath("//input[@id='passwordInput']");
    By submitBtn=By.xpath("//span[@id='submitButton']");
    By filterBtn=By.xpath("//button[@aria-label='Filter']");
    By unreadFilter =By.xpath("//span[normalize-space()='Unread']");
    By productLibraryLink=By.xpath("//a[text()='Go to Product Library ']");
    By emailAddress = By.id("txtEmail");
    By password1 = By.id("txtPassword");
    By continueToSite = By.id("loginButton");
    By menuBtn= By.xpath("//img[@alt='AS']/parent::div");
    By logOut=By.xpath("//a[@id='mectrl_body_signOut']");
    By homeBtn=By.xpath("//span[contains(text(),'Home')]");
    By template =By.xpath("//span[contains(text(),'New Connect Template')]");
    By other=By.xpath("//span[text()='Other']");
    By mailUnreadRegular = By.xpath("//button[@aria-label='Unread']");



    public void getUrl(String url){
        JavascriptExecutor jse = (JavascriptExecutor) BaseClass.driver;
        jse.executeScript("window.open()");
        browserActions.switchToTab(0,1);
        BaseClass.driver.get(url);
    }
    public void loginToOutlook(String email,String pass){
        boolean signInCheck;
        By signIn= By.xpath("//nav[@aria-label='Quick links']//a[@class='internal sign-in-link'][normalize-space()='Sign in']");
        signInCheck = browserActions.isDisplayedNew(signIn);
        if(signInCheck) {
            browserActions.waitForElementToBeVisible(signInBtn);
            browserActions.click(signInBtn);
        }
        browserActions.sendKeys(emailToEnter, email);
        browserActions.click(nextBtn);
        browserActions.waitForElementToBeVisible(passwordToEnter);
        browserActions.sendKeys(passwordToEnter, pass);
        browserActions.click(submitBtn);
        browserActions.click(nextBtn);
        System.out.println("successfully logged in outlook");
        extentTest.log(Status.INFO,"successfully logged in outlook");
    }
    public String verifyTitle(){
        browserActions.waitForElementToBeVisible(homeBtn);
        String titlePresent = browserActions.getPageTitle();
        return titlePresent;
    }
    public boolean searchMailAndValidate(String username, String password) throws Exception {
        try {
            boolean emailFoundFlag = false;
            int timeCounter = 0;
            String todaydate = dateManager.getCurrentDate("dd/MM/YYYY");
            String date = dateManager.getCurrentDate("dd/MM");
            String newConnectTemplateStr = "(//span[contains(text(),'New Connect Template')])[1]";
            By dateDivTag = By.xpath("//div[@id='ReadingPaneContainerId']//div[contains(text(),'" + todaydate + "')]");
            By titleExpectedStr = By.xpath("//h3[normalize-space()='Dear Svc Autotestb,']");

            boolean newTemplate;
            boolean todaysDate;
            boolean titleAsExpected;
            browserActions.waitForElementToBeVisible(filterBtn);

            do {
                browserActions.click(filterBtn);
                browserActions.click(unreadFilter);
                newTemplate = browserActions.isDisplayed(newConnectTemplateStr);
                if (!newTemplate) {
                    browserActions.click(mailUnreadRegular);
                    timeCounter++;
                    System.out.println("Mail not found trying again");
                    extentTest.log(Status.INFO,"Mail not found trying again");
                } else {
                    browserActions.waitForElementToBeVisible(template);
                    Thread.sleep(3000);
                    browserActions.click(template);
                    todaysDate = browserActions.isDisplayedNew(dateDivTag);
                    titleAsExpected = browserActions.isDisplayedNew(titleExpectedStr);
                    if (todaysDate && titleAsExpected) {
                        System.out.println("email content is valid ");
                        extentTest.log(Status.INFO,"email content is valid");
                        browserActions.click(productLibraryLink);
                        Thread.sleep(5000);
                        browserActions.switchToTab(0, 1);
                        browserActions.sendKeys(emailAddress, username);
                        browserActions.sendKeys(password1, password);
                        browserActions.click(continueToSite);
                        emailFoundFlag = true;
                        break;
                    } else {
                        System.out.println("email content is invalid trying again");
                        browserActions.click(mailUnreadRegular);
                        extentTest.log(Status.INFO,"email content is invalid trying again");
                    }
                }
            }
            while ((!emailFoundFlag) && (timeCounter < 60));
            if (emailFoundFlag) {
                System.out.println("New Connect Template email found");
                extentTest.log(Status.PASS,"New Connect Template email found");
                return true;
            } else {
                System.out.println("New Connect Template email not found");
                extentTest.log(Status.FAIL,"New Connect Template email not found");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public String getEmailCode() {
        By codeToEnter=By.xpath("//span[contains(text(),'Your code')]");
        String text = browserActions.getElementText(codeToEnter);
        String actual = text.replaceAll("[^0-9]","");
        System.out.println(actual);
        extentTest.log(Status.INFO,"EmailCode" + actual);
        return actual;

    }
    public boolean searchMailAndCopyCode() throws Exception {
        try {
            By unreadRegular=By.xpath("//i[@data-icon-name='MailUnreadRegular']");
            By emailTextToFind = By.xpath("//span[contains(text(),'Azure.Brandbank.Dev')]");
            By verifyMailName= By.xpath("//span[contains(text(),'Thanks for verifying your svc.autotest')]");
            By emailTextStr = By.xpath("//span[contains(text(),'Azure.Brandbank.Dev')]");
            int timeCounter = 0;
            boolean title;
            boolean verify;
            boolean emailFoundFlag=false;
            browserActions.click(other);
            do {
                browserActions.click(filterBtn);
                browserActions.click(unreadFilter);
                title = browserActions.isDisplayedNew(emailTextStr);
                if (!title) {
                    browserActions.click(unreadRegular);
                    timeCounter++;
                    System.out.println("Mail not found trying again");
                    emailFoundFlag=false;
                } else {
                    browserActions.waitForElementToBeVisible(emailTextToFind);
                    browserActions.click(emailTextToFind);
                    verify = browserActions.isDisplayedNew(verifyMailName);
                    if (verify) {
                        System.out.println("Mail content is  valid");
                        emailFoundFlag = true;
                    } else {
                        System.out.println("Mail content is invalid");
                        emailFoundFlag = false;
                    }
                }
            }
            while ((!emailFoundFlag) && (timeCounter < 3));
            if(emailFoundFlag){
                return true;
            }
            else {
                return false;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    public void deleteMail(String mailToDelete) throws Exception{
        try{
        By deleteMailBtn=By.xpath("//span[contains(normalize-space(),'"+mailToDelete+"')]/following::div[@class='QpoLy']");
        Thread.sleep(5000);
        browserActions.moveToElement(deleteMailBtn);
        browserActions.click(deleteMailBtn);
        System.out.println("Mail deleted");
        extentTest.log(Status.INFO,"Mail deleted");
        }
        catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    public void logOut() throws Exception {
        try {
            browserActions.switchToTab(1, 0);
            Thread.sleep(2000);
            browserActions.waitForElementToBeVisible(menuBtn);
            browserActions.click(menuBtn);
            browserActions.click(logOut);
            System.out.println("logged out of Outlook");
            extentTest.log(Status.PASS, "logged out of Outlook");
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

}
