package org.brandbank.pages.apps;

import com.aventstack.extentreports.Status;
import org.brandbank.base.BaseClass;
import org.brandbank.libs.BrowserActions;
import org.brandbank.libs.CSVDataManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class CheckTasklistPage extends BaseClass {
    public static WebDriver driver;
    BrowserActions browserActions;
    CSVDataManager csvDataManager;
    TasklistSearchAndAssignPage tasklistSearchAndAssignPage;

    public CheckTasklistPage(WebDriver driver){
        this.driver = driver;
        browserActions = new BrowserActions(this.driver);
        csvDataManager=new CSVDataManager();
        PageFactory.initElements(driver,this);
        tasklistSearchAndAssignPage = new TasklistSearchAndAssignPage(driver);
    }
    By batchNumberFilter = By.xpath("//input[@id='batch-search']");
    By clickAwayStep = By.linkText("Post-Outsource PS");

    public boolean searchBatch(String tasklistName,String batchNumber) throws Exception{
        try {
            boolean batchFoundFlag;
            int timeCounter = 0;
            String batchNumRow = browserActions.createXpath("//div[normalize-space()='{0}']", batchNumber);
            do {
                browserActions.clearTextBox(batchNumberFilter);
                browserActions.sendKeys(batchNumberFilter, batchNumber);
                browserActions.enterKey(batchNumberFilter);
                Thread.sleep(5000);
                batchFoundFlag = browserActions.isDisplayed(String.valueOf(batchNumRow));
                if (batchFoundFlag) {
                    System.out.println("Batch " + batchNumber + " Found in the grid");
                    extentTest.log(Status.INFO,"batch " + batchNumber + " Found in the grid");
                    return true;
                }
                else {
                    timeCounter++;
                    System.out.println("Batch not found, trying again");
                    browserActions.click(clickAwayStep);
                    tasklistSearchAndAssignPage.selectTasklist(tasklistName);
                }
            }
            while ((!batchFoundFlag) && (timeCounter < 30));
            if (!batchFoundFlag) {
                System.out.println("batch " + batchNumber + " not found in the grid");
                extentTest.log(Status.FAIL,"batch " + batchNumber + " not found in the grid");
                return false;
            }
            return true;
        }
        catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }
}
