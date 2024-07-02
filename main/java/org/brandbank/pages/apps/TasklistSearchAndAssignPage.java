package org.brandbank.pages.apps;

import com.aventstack.extentreports.Status;
import org.brandbank.base.BaseClass;
import org.brandbank.libs.BrowserActions;
import org.brandbank.libs.CSVDataManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class TasklistSearchAndAssignPage extends BaseClass {
    public static WebDriver driver;
    BrowserActions browserActions;
    CSVDataManager csvDataManager;

    public TasklistSearchAndAssignPage(WebDriver driver){
        this.driver = driver;
        browserActions = new BrowserActions(this.driver);
        csvDataManager=new CSVDataManager();
        PageFactory.initElements(driver,this);
    }
    By batchNumberFilter = By.xpath("//input[@id='batch-search']");
    By contextDD = By.xpath("//ul[@class='contextMenu']");
    By assignToMe = By.xpath("//span[normalize-space()='Assign to Me']");
    By clickAwayStep = By.linkText("Post-Outsource PS");
    By rightPane = By.xpath("//div[@id='rightPaneTopContentSection']");

    public void selectTasklist(String tasklistName) throws InterruptedException{
        By tasklistName_ATag = By.xpath("//ul[@id='container-workspace-layout-list']//a[normalize-space()='"+tasklistName+"']");
        Thread.sleep(8000);
        browserActions.moveToElement(tasklistName_ATag);
        Thread.sleep(2000);
        browserActions.click(tasklistName_ATag);
        extentTest.log(Status.INFO,"Clicked on the Tasklist "+ tasklistName +" to open it ");
        System.out.println("Clicked on the Tasklist "+ tasklistName +" to open it ");
        Thread.sleep(5000);
        browserActions.waitForElementToBeVisible(rightPane);
    }

    public boolean searchBatchAndAssign(String tasklistName,String batchNumber,String EAN) throws Exception{
        try {
            boolean batchFoundFlag;
            int timeCounter = 0;
            String batchNumRow = browserActions.createXpath("//div[normalize-space()='{0}']", batchNumber);
            By batchContextBtn = By.xpath(browserActions.createXpath("//div[normalize-space()='{0}']/parent::div/descendant::i[contains(@class,'context-button')]",batchNumber));
            do {
                browserActions.clearTextBox(batchNumberFilter);
                browserActions.sendKeys(batchNumberFilter, batchNumber);
                browserActions.enterKey(batchNumberFilter);
                Thread.sleep(5000);
                batchFoundFlag = browserActions.isDisplayed(String.valueOf(batchNumRow));
                if (batchFoundFlag) {
                    extentTest.log(Status.PASS,"Batch " + batchNumber + " found in the grid");
                    System.out.println("Batch " + batchNumber + " found in the grid");
                    browserActions.click(batchContextBtn);
                    browserActions.click(By.xpath(batchNumRow));
                    Thread.sleep(5000);
                    extentTest.log(Status.INFO,"Clicked on batch number to select the row");
                    System.out.println("Clicked on batch number to select the row");
                } else {
                    timeCounter++;
                    extentTest.log(Status.INFO,"Batch not found, trying again");
                    System.out.println("Batch not found, trying again");
                    browserActions.click(clickAwayStep);
                    selectTasklist(tasklistName);
                }
            }
            while ((batchFoundFlag == false) && (timeCounter < 30));
            if (batchFoundFlag == false) {
                extentTest.log(Status.FAIL,"batch " + batchNumber + " not found in the grid");
                System.out.println("batch " + batchNumber + " not found in the grid");
                return false;
            }
            else {
                boolean EANAssigned = assignBatch(EAN,batchNumber);
                return EANAssigned;
            }
        }
        catch (Exception e){
            return false;
        }
    }
    public boolean assignBatch(String EAN,String batchNumber) throws InterruptedException {
        try {
            int EANFoundFlag = 0;
            int timer = 0;
            By batchNumRow = By.xpath(browserActions.createXpath("//div[normalize-space()='{0}']",batchNumber));
            By EANInRHS = By.xpath("//div[normalize-space()='"+EAN+"']");
            By EANInRHSDivTag = By.xpath(browserActions.createXpath("//div[normalize-space()='{0}']",EAN));
            By EANStatusInRHS = By.xpath("//div[normalize-space()='"+EAN+"']/parent::div/descendant::span[contains(text(),'Pending')]");
            By contextBtn = By.xpath(browserActions.createXpath("//div[normalize-space()='{0}']/parent::div/descendant::i[@class='context-button-i material-icons']",EAN));
            By batchContextBtn = By.xpath(browserActions.createXpath("//div[normalize-space()='{0}']/parent::div/descendant::i[contains(@class,'context-button')]",batchNumber));
            boolean EANIsDisplayed = browserActions.isDisplayedNew(EANInRHS);
           if(!(EANIsDisplayed)){
                browserActions.click(batchContextBtn);
                browserActions.click(batchNumRow);
                Thread.sleep(5000);
            }
           if(EANIsDisplayed) {
                do {
                    boolean EANStatusIsDisplayed = browserActions.isDisplayedNew(EANStatusInRHS);
                    if(EANStatusIsDisplayed){
                        extentTest.log(Status.INFO,"EAN found in the right splitter screen and is in pending status");
                        System.out.println("EAN found in the right splitter screen and is in pending status");
                        EANFoundFlag=1;
                        browserActions.click(EANInRHSDivTag);
                        Thread.sleep(5000);
                        browserActions.moveToElement(contextBtn);
                        browserActions.click(contextBtn);
                        browserActions.waitForElementToBeVisible(contextDD);
                        browserActions.moveToElement(assignToMe);
                        browserActions.click(assignToMe);
                        extentTest.log(Status.INFO,"Clicked on Assign to me option");
                        System.out.println("Clicked on Assign to me option");
                        Thread.sleep(8000);
                    }
                    else{
                        Thread.sleep(10000);
                        timer++;
                    }
                }
                while ((EANFoundFlag==0) && (timer<30));
                if(EANFoundFlag==0){
                    extentTest.log(Status.FAIL,"Failed to find the EAN "+EAN+" in RHS. Wait time was 5 mins");
                    System.out.println("Failed to find the EAN "+EAN+" in RHS. Wait time was 5 mins");
                    return false;
                }
            }
            else{
                extentTest.log(Status.FAIL,"Product not found in the right splitter screen");
                System.out.println("Product not found in the right splitter screen");
                return false;
            }
            return true;
        }
        catch (Exception e){
            throw e;
        }
    }

    public void clickOnAssignedTask(String tasklistName) throws InterruptedException {
        By assignedTasklist = By.xpath(browserActions.createXpath("//div[@id='myActivity']//descendant::a[text()='{0}']", tasklistName));
        browserActions.waitForElementToBeVisible(assignedTasklist);
        browserActions.moveToElement(assignedTasklist);
        Thread.sleep(1000);
        browserActions.click(assignedTasklist);
        extentTest.log(Status.INFO,"Batch successfully assigned and opened under assigned section");
        System.out.println("Batch successfully assigned and opened under assigned section");
    }
}
