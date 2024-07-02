package org.brandbank.tests.apps;

import com.aventstack.extentreports.Status;
import org.brandbank.libs.CSVDataManager;
import org.brandbank.libs.LaunchTrayApp;
import org.brandbank.libs.PropertyManager;
import org.brandbank.pages.LoginPage;
import org.brandbank.pages.apps.*;
import org.brandbank.pages.virtim.AttachImagesPage;
import org.brandbank.pages.virtim.CompleteTaskPage;
import org.brandbank.pages.virtim.CopyImagesPage;
import org.brandbank.pages.virtim.PostProductionPage;
import org.brandbank.tests.TestBaseSetup;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.IOException;

public class PPVEXTAssetStoreTest extends TestBaseSetup {
    LoginPage loginPage;
    AppsLoginPage appsLoginPage;
    ImagingTasksPage imagingTasksPage;
    TasklistSearchAndAssignPage tasklistSearchAndAssignPage;
    CompleteTaskPage completeTaskPage;
    PostProductionPage postProductionPage;
    CSVDataManager csvDataManager;
    BookingInPage bookingInPage;
    SupersedeBatchPage supersedeBatchPage;


    public PPVEXTAssetStoreTest(){
        loginPage = new LoginPage(driver);
        appsLoginPage = new AppsLoginPage(driver);
        imagingTasksPage = new ImagingTasksPage(driver);
        tasklistSearchAndAssignPage = new TasklistSearchAndAssignPage(driver);
        completeTaskPage = new CompleteTaskPage(driver);
        postProductionPage = new PostProductionPage(driver);
        csvDataManager = new CSVDataManager();
        bookingInPage=new BookingInPage(driver);
        supersedeBatchPage=new SupersedeBatchPage(driver);
    }

    @Test
    @Parameters({"testSuite"})
    public  void checkImages(String testSuite) throws Exception {
        try {
            String batchNum = csvDataManager.getCSVData(testSuite, 1)[4];
            String EAN = csvDataManager.getCSVData(testSuite, 1)[1];
            tasklistSearchAndAssignPage.selectTasklist("Post Production Virtual");
            boolean batchAssigned = tasklistSearchAndAssignPage.searchBatchAndAssign("Post Production Virtual", batchNum, EAN);
            Assert.assertEquals(batchAssigned, true);
            extentTest.log(Status.INFO,"Batch Assigned");

            tasklistSearchAndAssignPage.clickOnAssignedTask("Post Production Virtual");
            //checking batch is present on screen
            boolean batchFound = imagingTasksPage.verifyBatchFound("Post Production Virtual",batchNum);
            Assert.assertEquals(batchFound, true);
            extentTest.log(Status.INFO,"Batch Found");
            //Approve from Post Production Virtual
            postProductionPage.checkImagesInPPV(batchNum,EAN);
        }
        catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    @Parameters({"testSuite","subCode"})
    public void supersedeBatch(String testSuite, String subCode) throws Exception{
        try {
            String batchId = csvDataManager.getCSVData(testSuite, 1)[4];
            boolean BatchFound = bookingInPage.Search_Order_To_BookIn("Batch",batchId,subCode);
            if(BatchFound)
            {
                System.out.println("batch found" );
                extentTest.log(Status.PASS,"Batch Found");
                supersedeBatchPage.supersedeBatch(batchId);
            }
            else
                System.out.println("batch not found" );
            extentTest.log(Status.INFO,"Batch Not Found");
        }
        catch (AssertionError e) {
            throw e;
        }
    }

    @AfterMethod
    public void afterMethod(ITestResult result)
    {
        if(!result.isSuccess())
            System.setProperty("TestStatus","false");
    }

}
