package org.brandbank.pages.apps;

import com.aventstack.extentreports.Status;
import org.brandbank.base.BaseClass;
import org.brandbank.libs.BrowserActions;
import org.brandbank.libs.CSVDataManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class ImagingRejectionsPage extends BaseClass {
    public static WebDriver driver;
    BrowserActions browserActions;
    CSVDataManager csvDataManager;
    public ImagingRejectionsPage(WebDriver driver){
        this.driver = driver;
        browserActions = new BrowserActions(this.driver);
        csvDataManager=new CSVDataManager();
        PageFactory.initElements(driver,this);
    }
    By activityPageHeader = By.xpath("//span[@id='rightPaneTopContentSectionHeader']");
    By loadingProgressBar = By.xpath("//div[@id=''ajaxProgress']");
    By loadingSpinner = By.xpath("//div[@class='loading-spinner-overlay']");
    By batchStart = By.xpath("//img[@id='batch-start']");
    By batchApprove = By.xpath("//img[@id='batch-approve']");
    By batchSubmit = By.xpath("//img[@id='batch-submit']");


    public String verifyActivityPageHeader() {
        browserActions.waitForElementToDisappear(loadingSpinner);
        browserActions.waitForElementToDisappear(loadingProgressBar);
        return browserActions.getElementText(activityPageHeader);
    }

    public void verifyBatchExist(String batchNum){
        By batchRow = By.xpath("//div[text()='"+batchNum+"']");
        browserActions.waitForElementToBeVisible(batchRow);
        System.out.println("Batch found");
        extentTest.log(Status.INFO,"Batch found");
        browserActions.click(batchRow);
    }

    public void approveAndSubmit() throws InterruptedException {
        browserActions.waitForElementToBeVisible(batchStart);
        browserActions.moveToElement(batchStart);
        Thread.sleep(2000);
        browserActions.click(batchStart);
        Thread.sleep(2000);
        browserActions.moveToElement(batchApprove);
        Thread.sleep(2000);
        browserActions.click(batchApprove);
        Thread.sleep(2000);
        browserActions.moveToElement(batchSubmit);
        Thread.sleep(2000);
        browserActions.click(batchSubmit);
        Thread.sleep(2000);
        extentTest.log(Status.PASS,"Batch Approved and Submitted successfully");
    }
}
