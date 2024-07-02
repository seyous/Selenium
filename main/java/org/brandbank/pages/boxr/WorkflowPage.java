package org.brandbank.pages.boxr;

import com.aventstack.extentreports.Status;
import org.brandbank.base.BaseClass;
import org.brandbank.libs.BrowserActions;
import org.brandbank.libs.CSVDataManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class WorkflowPage extends BaseClass {
    BrowserActions browserActions;
    public static WebDriver driver;
    CSVDataManager csvDataManager;

    public WorkflowPage(WebDriver driver) {
        this.driver = driver;
        browserActions = new BrowserActions(this.driver);
        PageFactory.initElements(driver,this);
        csvDataManager=new CSVDataManager();
    }
    By textBox=By.xpath("//input[@type='text']");
    By workflowBtn=By.xpath("//li[@title='workflow']");
    By acceptBtn=By.xpath("(//button[@class='btn btn-primary btn-sm'][normalize-space()='OK'])[2]");
    By additionalStatus =By.xpath("//p[normalize-space()='Show additional statuses']");
    By exportedStatus =By.xpath("//p[normalize-space()='Exported']");
    By completedStatus =By.xpath("//p[normalize-space()='Completed']");
    By loading = By.xpath("//div[@class='message']");
    By homePage=By.xpath("//h2[contains(text(),'home')]");

    public String verifyTitle(){
        browserActions.waitForElementToBeVisible(homePage);
        String titlePresent = browserActions.getPageTitle();
        return titlePresent;
    }

    public boolean checkPvid(String pvid) throws Exception {
        try {
            int timeCounter = 0;
            boolean pvidReturns = false;
            By validatePvid = By.xpath("//span[normalize-space()='" + pvid + "']");
            do {
                browserActions.waitForElementToBeVisible(workflowBtn);
                browserActions.click(workflowBtn);
                browserActions.click(textBox);
                browserActions.sendKeys(textBox, pvid);
                Thread.sleep(5000);
                By pvidVisible = By.xpath("//span[normalize-space()='" + pvid + "']");
                pvidReturns = browserActions.isDisplayedNew(pvidVisible);
                if (!pvidReturns) {
                    System.out.println("pvid not found");
                    extentTest.log(Status.INFO,"pvid not found");
                    browserActions.refreshPage();
                    timeCounter++;
                }
                else {
                    browserActions.waitForElementToBeVisible(validatePvid);
                    System.out.println("pvid found");
                    extentTest.log(Status.INFO,"pvid found");
                }
            }
            while ((!pvidReturns) && (timeCounter < 8));
            return pvidReturns;
        }
        catch (Exception e){
            e.printStackTrace();
            throw  e;
        }
    }

    public boolean selectPvidToDraw(String pvid) throws Exception{
        try {
            int timeCounter=0;
            boolean drawAction=false;
            By drawIcon=By.xpath("//span[normalize-space()='"+pvid+"']/following::div[@class='actions']//div[@title='Draw product'][@class='action drawproduct']//img");
            Thread.sleep(3000);
            do {
                browserActions.waitForElementToBeVisible(workflowBtn);
                browserActions.click(workflowBtn);
                browserActions.clearTextBox(textBox);
                browserActions.sendKeys(textBox, pvid);
                Thread.sleep(5000);
                By drawEnabled= By.xpath("//span[normalize-space()='"+pvid+"']/following::div[@class='actions']//div[@title='Draw product'][@class='action drawproduct']//img");
                drawAction = browserActions.isDisplayedNew(drawEnabled);
                if (!drawAction) {
                    System.out.println("Draw action is disabled");
                    extentTest.log(Status.INFO,"Draw action is disabled");
                    browserActions.refreshPage();
                    timeCounter++;
                } else {
                    System.out.println("Draw action is enabled");
                    extentTest.log(Status.INFO,"Draw action is enabled");
                    browserActions.waitForElementToBeVisible(drawIcon);
                    browserActions.click(drawIcon);
                    browserActions.waitForElementToBeVisible(acceptBtn);
                    browserActions.click(acceptBtn);
                }
            }
            while((!drawAction) && (timeCounter<20));
            return drawAction;
        }
        catch (Exception e){
            e.printStackTrace();
            throw  e;
        }
    }
    public boolean selectPVIDToClean(String pvid) throws Exception{
        try {
            Thread.sleep(3000);
            boolean cleanProductIcon = false;
            int timeCounter = 0;
            Thread.sleep(5000);
            By validatePvid = By.xpath("//span[normalize-space()='" + pvid + "']");
            By cleanIcon = By.xpath("//span[normalize-space()='" + pvid + "']/following::div[@class='actions']//div[@title='Clean product'][@class='action cleanproduct']//img");
            By yellowIcon = By.xpath("//span[normalize-space()='" + pvid + "']/following::div[@class='actions']//div[@title='Clean product'][@class='action cleanproduct']//img");
            do {
                browserActions.waitForElementToBeVisible(workflowBtn);
                browserActions.click(workflowBtn);
                browserActions.click(textBox);
                browserActions.sendKeys(textBox,pvid);
                browserActions.waitForElementToBeVisible(validatePvid);
                cleanProductIcon = browserActions.isDisplayedNew(yellowIcon);
                if (!cleanProductIcon) {
                    browserActions.refreshPage();
                    timeCounter++;
                } else {
                    System.out.println("Product Found to perform clean action");
                    extentTest.log(Status.PASS,"Product Found to perform clean action");
                    browserActions.click(cleanIcon);
                    browserActions.click(acceptBtn);
                }
            }
            while ((!cleanProductIcon) && (timeCounter < 10));
            return cleanProductIcon;
        }
        catch (Exception e){
            e.printStackTrace();
            throw  e;
        }
    }
    public boolean validateExportedStatus(String pvid) throws Exception {
        try {
            int timeCounter = 0;
            boolean pvidFound = false;
            By pvidString = By.xpath("//div[@id='Exported']//span[normalize-space()='" + pvid + "']");
            do{
                browserActions.waitForElementToDisappear(loading);
                browserActions.waitForElementToBeVisible(workflowBtn);
                browserActions.click(workflowBtn);
                browserActions.waitForElementToDisappear(loading);
                browserActions.click(additionalStatus);
                Thread.sleep(10000);
                browserActions.click(exportedStatus);
                browserActions.sendKeys(textBox, pvid);
                Thread.sleep(10000);
                pvidFound = browserActions.isDisplayedNew(pvidString);
                if (!pvidFound) {
                    System.out.println("pvid not found,searching again");
                    extentTest.log(Status.INFO,"pvid not found,searching again");
                    browserActions.refreshPage();
                    timeCounter++;
                } else {
                    System.out.println("pvid exported to DE successfully");
                    extentTest.log(Status.PASS,"pvid exported to DE successfully");
                }
            }
            while ((!pvidFound && timeCounter < 5));
            return pvidFound;
        }
         catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public boolean validateCompletedStatus(String pvid) throws Exception {
        try {
            int timeCounter = 0;
            boolean pvidFound = false;
            By pvidString = By.xpath("//div[@id='Completed']//span[normalize-space()='" + pvid + "']");
            do{
                browserActions.waitForElementToDisappear(loading);
                browserActions.waitForElementToBeVisible(workflowBtn);
                browserActions.click(workflowBtn);
                browserActions.waitForElementToDisappear(loading);
                browserActions.click(additionalStatus);
                Thread.sleep(10000);
                browserActions.click(completedStatus);
                browserActions.sendKeys(textBox, pvid);
                Thread.sleep(10000);
                pvidFound = browserActions.isDisplayedNew(pvidString);
                if (!pvidFound) {
                    System.out.println("pvid not found,searching again");
                    extentTest.log(Status.INFO,"pvid not found,searching again");
                    browserActions.refreshPage();
                    timeCounter++;
                } else {
                    System.out.println("pvid found in Completed status");
                    extentTest.log(Status.INFO,"pvid found in Completed status");
                }
            }
            while ((!pvidFound && timeCounter < 5));
            return pvidFound;
        }
        catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
