package org.brandbank.pages.dataworkflow;

import com.aventstack.extentreports.Status;
import org.brandbank.base.BaseClass;
import org.brandbank.libs.BrowserActions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class DataWorkflowHomePage extends BaseClass {
    BrowserActions browserActions;
    public static WebDriver driver;

    public DataWorkflowHomePage(WebDriver driver){
        this.driver = driver;
        browserActions = new BrowserActions(this.driver);
        PageFactory.initElements(driver,this);
    }

    By search = By.xpath("//a[normalize-space()='Search']");
    By searchField = By.xpath("//input[@id='search-term']");
    By searchButton = By.xpath("//input[@id='search-button']");
    String iframe="//iframe[@class='fancybox-iframe']";
    By closeButton = By.xpath("//a[@title='Close']");
    By logOutLink = By.xpath("//a[normalize-space()='Log out']");
    By loginPage = By.xpath("//span[@id='newLoginBlock']");

    public String verifyTitle(){
        String title = browserActions.getPageTitle();
        return title;
    }
    public void searchForBatch(String batchId) {
        try {
            browserActions.click(search);
            browserActions.waitForFrameToBeAvailable(iframe);
            browserActions.sendKeys(searchField, batchId);
            browserActions.click(searchButton);
            By batchResults = By.xpath(browserActions.createXpath("//a[@id='{0}']", batchId));
            browserActions.waitForElementToBeVisible(batchResults);
            browserActions.switchToDefaultContent();
            browserActions.click(closeButton);
            }
        catch (Exception e){
            System.out.println("Batch not found");
            extentTest.log(Status.FAIL,"Batch not found");
            e.printStackTrace();
            throw e;
        }
    }

    public void logOut(){
        browserActions.waitForElementToBeVisible(logOutLink);
        browserActions.click(logOutLink);
        browserActions.waitForElementToBeVisible(loginPage);
        System.out.println("Logged out of Data Workflow");
        extentTest.log(Status.INFO,"Logged out of Data Workflow");
    }
}
