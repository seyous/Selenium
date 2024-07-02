package org.brandbank.pages.apps;

import com.aventstack.extentreports.Status;
import org.brandbank.base.BaseClass;
import org.brandbank.libs.BrowserActions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class AppsLoginPage extends BaseClass{
    BrowserActions browserActions;
    WebDriver driver;

    public AppsLoginPage(WebDriver driver) {
        this.driver = driver;
        browserActions = new BrowserActions(this.driver);
        PageFactory.initElements(driver,this);
    }
    By myWorkSpace = By.xpath("//img[@src='../Content/images/workspace-icons/light/1.png']");
    By loadingMsg = By.xpath("//div[@id='ajaxProgress']");
    By username = By.xpath("//a[@id='app-header-username']");
    By logOutBtn = By.xpath("//a[contains(text(),'Log out')]");
    By loginPage = By.xpath("//span[@id='newLoginBlock']");
    By trayAppConnectImage = By.xpath("//img[@id='serverIndicator']");

    public void validateLogin() throws InterruptedException{
        Thread.sleep(2000);
        browserActions.isAlertPresent();
        Thread.sleep(5000);
        browserActions.pressEnterUsingRobotClass();
        browserActions.waitForElementToDisappear(loadingMsg);
        browserActions.waitForElementToBeVisible(myWorkSpace);
        extentTest.log(Status.INFO,"Workspace visible and loaded");
        System.out.println("Workspace visible and loaded");
    }

    public boolean connectTrayApp() throws InterruptedException{
        Thread.sleep(2000);
        browserActions.isAlertPresent();
        Thread.sleep(5000);
        browserActions.pressEnterUsingRobotClass();
        browserActions.waitForElementToDisappear(loadingMsg);
        browserActions.waitForElementToBeVisible(myWorkSpace);
        extentTest.log(Status.INFO,"Workspace visible and loaded");
        System.out.println("Workspace visible and loaded");
        String className = browserActions.getClassName(trayAppConnectImage);
        if(className.equals("disconnected")){
            extentTest.log(Status.PASS,"Clicking on image to connect TrayApp");
            System.out.println("Clicking on image to connect TrayApp");
            browserActions.click(trayAppConnectImage);
            Thread.sleep(5000);
            className = browserActions.getClassName(trayAppConnectImage);
            if(className.equals("connected")) {
                extentTest.log(Status.PASS,"Successfully connected to TrayApp");
                System.out.println("Successfully connected to TrayApp");
                return true;
            }
            else{
                extentTest.log(Status.FAIL,"TrayApp not connected");
                System.out.println("TrayApp not connected");
                return false;
            }
        }
        else{
            extentTest.log(Status.PASS,"TrayApp already connected");
            System.out.println("TrayApp already connected");
            return true;
        }
    }

    public String verifyTitle(){
        String titlePresent = browserActions.getPageTitle();
        return titlePresent;
    }

    public void logOut(){
        browserActions.click(username);
        browserActions.click(logOutBtn);
        browserActions.waitForElementToDisappear(loadingMsg);
        browserActions.waitForElementToBeVisible(loginPage);
        System.out.println("Logged out of Apps");
        extentTest.log(Status.INFO,"Logged out of Apps");
    }
}
