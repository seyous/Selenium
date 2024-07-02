package org.brandbank.pages.blackbox;

import com.aventstack.extentreports.Status;
import org.brandbank.base.BaseClass;
import org.brandbank.libs.BrowserActions;
import org.brandbank.libs.DateManager;
import org.brandbank.libs.FileHandling;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class ExtractsPage extends BaseClass {
    BrowserActions browserActions;
    WebDriver driver;
    FileHandling fileHandling;
    public ExtractsPage(WebDriver driver) {
        this.driver = driver;
        browserActions = new BrowserActions(this.driver);
        PageFactory.initElements(driver,this);
        fileHandling = new FileHandling();
    }
    By extractTab = By.xpath("//span[normalize-space()='Extracts']");
    By extractFilter = By.xpath("//input[@id='extractFilter']");
    By runExtractBtn = By.xpath("//img[@title='Run Extract']");
    By xmlName = By.linkText("DataImportProcessor Log.xml");
    By jsonName=By.linkText("products.json");
    By loginPage = By.xpath("//span[@id='newLoginBlock']");
    By logOutBtn = By.xpath("//a[text()='Log Out']");

    public String verifyTitle(){
        String titlePresent = browserActions.getPageTitle();
        return titlePresent;
    }
    public void searchExtract(String extractName) throws InterruptedException {
        browserActions.waitForElementToBeVisible(extractTab);
        browserActions.sendKeys(extractFilter, Keys.CONTROL + "a");
        browserActions.sendKeys(extractFilter, String.valueOf(Keys.DELETE));
        browserActions.sendKeys(extractFilter, extractName);
        browserActions.sendKeys(extractFilter, String.valueOf(Keys.ENTER));
        Thread.sleep(5000);
    }

    public boolean downloadXml(String extractName){
            By extract = By.xpath("//a[normalize-space()='"+extractName+"']");
            String currentDate=new DateManager().getCurrentDate("M/d/yyyy");
            String successRow = browserActions.createXpath("//div[@id='scrollViewer']/table//td[contains(text(),'{0}')][1]/following-sibling::td[normalize-space()='Completed Successfully']",currentDate);

            browserActions.waitForElementToBePresent(extract);
            browserActions.click(extract);
            browserActions.waitForElementToBePresent(extract);
            browserActions.waitForElementToBeVisible(runExtractBtn);
            boolean successRowPresent = browserActions.isDisplayed(successRow);
            if(!successRowPresent){
                System.out.println("Extract run not found");
                extentTest.log(Status.FAIL,"Extract run not found");
                return false;
            }
            System.out.println("Extract run found");
            extentTest.log(Status.PASS,"Extract run found");
            browserActions.click(By.xpath(successRow));
            browserActions.waitForElementToBeVisible(xmlName);
            browserActions.click(xmlName);
            return true;
        }
    public boolean downloadJson(String extractName){
        By extract = By.xpath("//a[normalize-space()='"+extractName+"']");
        String currentDate=new DateManager().getCurrentDate("M/d/yyyy");
        String successRow = browserActions.createXpath("//div[@id='scrollViewer']/table//td[contains(text(),'{0}')][1]/following-sibling::td[normalize-space()='Completed Successfully']",currentDate);
        By extractToClick=By.linkText(extractName);

        browserActions.waitForElementToBePresent(extract);
        browserActions.click(extract);
        browserActions.waitForElementToBePresent(extract);
        browserActions.waitForElementToBeVisible(runExtractBtn);
        browserActions.click(runExtractBtn);
        System.out.println("Extract run");
        extentTest.log(Status.INFO,"Extract run");
        browserActions.waitForElementToBeVisible(jsonName);
        System.out.println("Product.json visible");
        extentTest.log(Status.INFO,"Product.json visible");
        browserActions.click(extractToClick);
        boolean successRowPresent = browserActions.isDisplayed(successRow);
        if(!successRowPresent) {
            System.out.println("Extract run not found");
            extentTest.log(Status.FAIL,"Extract run not found");
            return false;
        }
            browserActions.click(By.xpath(successRow));
            browserActions.waitForElementToBeVisible(jsonName);
            browserActions.click(jsonName);
            System.out.println("JSON file downloaded");
            extentTest.log(Status.PASS,"JSON file downloaded");
        return true;
    }

    public void logOut(){
        browserActions.click(logOutBtn);
        browserActions.waitForElementToBeVisible(loginPage);
        System.out.println("Logged out of Blackbox");
        extentTest.log(Status.INFO,"Logged out of Blackbox");
    }
}
