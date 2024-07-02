package org.brandbank.pages.dataentry;

import com.aventstack.extentreports.Status;
import org.brandbank.base.BaseClass;
import org.brandbank.libs.BrowserActions;
import org.brandbank.libs.CSVDataManager;
import org.brandbank.libs.DateManager;
import org.brandbank.pages.apps.TasklistSearchAndAssignPage;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class DETasklistSearchAndAssignPage extends BaseClass {
    BrowserActions browserActions;
    CSVDataManager csvDataManager;
    TasklistSearchAndAssignPage tasklistSearchAndAssignPage;

    public static WebDriver driver;
    public DETasklistSearchAndAssignPage(WebDriver driver) {
        this.driver = driver;
        browserActions = new BrowserActions(this.driver);
        csvDataManager=new CSVDataManager();
        tasklistSearchAndAssignPage = new TasklistSearchAndAssignPage(driver);
        PageFactory.initElements(driver,this);
    }
    By productSearch = By.xpath("//input[@class='search-input form-control input-sm']");
    By assignToMe = By.xpath("//ul[@class='context-menu-dropdown']//li//span[text()='Assign to me']");
    By clickAwayStep = By.linkText("Post-Outsource PS");
    By bottomContentSection = By.xpath("//span[@id='rightPaneBottomContentSectionHeader']");
    By deAttachmentsTab = By.xpath("//li[@title='Data Entry Attachments']//img[@class='contextsheet-image']");
    By addAttachmentBtn = By.xpath("//button[normalize-space()='Add Attachment']");
    By dropzone = By.xpath("//input[@type='file']");
    By attachmentTypeDD = By.xpath("//select[@class='form-control']");
    By uploadFileBtn = By.xpath("//button[normalize-space()='Upload File']");
    By loadingText = By.xpath("//div[@class='loading-text']");
    By attachedObjectInList = By.xpath("//td[@title='Patient Information Leaflet']");
    By testProductsOnlyFilter = By.xpath("//div[@class='testProduct-filter-bar']/descendant::div[contains(@class,'components__control')]");
    By testOnlyProductsMenuOption = By.xpath("//div[@class='testProduct-filter-bar']/descendant::*[normalize-space()='Test only products']");

    public boolean searchBatchNum(String tasklistName,String batchNum,String EAN) throws InterruptedException {
        boolean batchFoundFlag;
        int timeCounter = 0;
        String batchRowStr = browserActions.createXpath("//div[normalize-space()='{0}']//preceding::div[normalize-space()='{1}']",batchNum,EAN);
        By batchRow = By.xpath("//div[normalize-space()='"+batchNum+"']//preceding::div[normalize-space()='"+EAN+"']");
        browserActions.click(testProductsOnlyFilter);
        browserActions.waitForElementToBeVisible(testOnlyProductsMenuOption);
        browserActions.click(testOnlyProductsMenuOption);

        do {
            browserActions.sendKeys(productSearch, Keys.CONTROL + "a");
            browserActions.sendKeys(productSearch, String.valueOf(Keys.DELETE));
            browserActions.sendKeys(productSearch, batchNum);
            Thread.sleep(5000);
            if(tasklistName == "Data Entry Awaiting Approval"){
                Thread.sleep(30000);
            }
            batchFoundFlag = browserActions.isDisplayed(String.valueOf(batchRowStr));
            if(batchFoundFlag){
                browserActions.click(batchRow);
                extentTest.log(Status.INFO,"Row with batch "+batchNum+" and EAN "+ EAN+" found");
                System.out.println("Row with batch "+batchNum+" and EAN "+ EAN+" found");
                browserActions.click(batchRow);
            }
            else{
                timeCounter++;
                extentTest.log(Status.INFO,"Product not found, trying again");
                System.out.println("Product not found, trying again");
                browserActions.click(clickAwayStep);
                tasklistSearchAndAssignPage.selectTasklist(tasklistName);
            }
        }
        while ((!batchFoundFlag) && (timeCounter<30));
        if(!batchFoundFlag) {
            System.out.println("Batch " + batchNum + " with EAN" + EAN + " not found in the grid");
            extentTest.log(Status.FAIL,"Batch " + batchNum + " with EAN" + EAN + " not found in the grid");
            return false;

        }
        else{
            System.out.println("Batch " + batchNum + " with EAN" + EAN + " found in the grid");
            extentTest.log(Status.PASS,"Batch " + batchNum + " with EAN" + EAN + " found in the grid");
            return true;
        }
    }

    public void clickOnAssignToMe() throws  InterruptedException {
        By contextButton = By.xpath("(//div[@class='context-menu-container']//i[@class='material-icons'])[1]");

        browserActions.waitForElementToBeVisible(contextButton);
        browserActions.click(contextButton);
        browserActions.click(assignToMe);
        extentTest.log(Status.INFO,"Clicked on Assign to me option");
        System.out.println("Clicked on Assign to me option");
        //Thread.sleep(8000);
        Thread.sleep(2000);
    }

    public void attachObject(String objectPath) throws InterruptedException {
        try {
        String todaysDate = new DateManager().getCurrentDate("dd/MM/yyyy");
        By date =By.xpath(browserActions.createXpath("//div[normalize-space()='{0}']",todaysDate));
        browserActions.waitForElementToBeVisible(bottomContentSection);
        browserActions.click(deAttachmentsTab);
        Thread.sleep(6000);
        browserActions.waitForElementToBeVisible(addAttachmentBtn);
        browserActions.click(addAttachmentBtn);
        Thread.sleep(6000);
        browserActions.waitForElementToBeVisible(dropzone);
        browserActions.sendKeysToUpload(dropzone,objectPath);
        Thread.sleep(4000);
        browserActions.waitForElementToBeVisible(attachmentTypeDD);
        browserActions.selectFromDropdown(attachmentTypeDD,"Patient Information Leaflet");
        browserActions.click(uploadFileBtn);
        browserActions.waitForElementToDisappear(loadingText);
        browserActions.waitForElementToBePresent(attachedObjectInList);
        browserActions.waitForElementToBePresent(date);
        System.out.println("file uploaded successfully");
    }
        catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}