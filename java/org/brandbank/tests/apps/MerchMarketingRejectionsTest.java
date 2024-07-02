package org.brandbank.tests.apps;

import com.aventstack.extentreports.Status;
import org.brandbank.libs.CSVDataManager;
import org.brandbank.libs.PropertyManager;
import org.brandbank.pages.LoginPage;
import org.brandbank.pages.apps.AppsLoginPage;
import org.brandbank.pages.apps.ImagingRejectionsPage;
import org.brandbank.pages.apps.ImagingTasksPage;
import org.brandbank.pages.apps.TasklistSearchAndAssignPage;
import org.brandbank.tests.TestBaseSetup;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.Properties;

public class MerchMarketingRejectionsTest extends TestBaseSetup {
    LoginPage loginPage;
    AppsLoginPage appsLoginPage;
    CSVDataManager csvDataManager;
    TasklistSearchAndAssignPage tasklistSearchAndAssignPage;
    ImagingTasksPage imagingTasksPage;
    Properties configProperties;
    ImagingRejectionsPage imagingRejectionsPage;
    public MerchMarketingRejectionsTest(){
        loginPage = new LoginPage(driver);
        csvDataManager = new CSVDataManager();
        appsLoginPage = new AppsLoginPage(driver);
        tasklistSearchAndAssignPage = new TasklistSearchAndAssignPage(driver);
        imagingTasksPage = new ImagingTasksPage(driver);
        imagingRejectionsPage = new ImagingRejectionsPage(driver);
    }

    @Test
    @Parameters({"testSuite","taskListName1","taskListName2"})
    public  void merchMarketingRejections(String testSuite,String taskListName1, String taskListName2) throws Exception {
        try {
            String taskListNames[] = {taskListName1,taskListName2};
            for(String taskListName:taskListNames) {
                String batchNumber = csvDataManager.getCSVData(testSuite, 1)[4];
                String EAN = csvDataManager.getCSVData(testSuite, 1)[1];
                tasklistSearchAndAssignPage.selectTasklist(taskListName);
                boolean batchAssigned = tasklistSearchAndAssignPage.searchBatchAndAssign(taskListName, batchNumber,EAN);
                Assert.assertEquals(batchAssigned,true);
                extentTest.log(Status.INFO,"Batch Assigned");
                tasklistSearchAndAssignPage.clickOnAssignedTask(taskListName);
                //verify activity page header
                String actualActivityPageHeader = imagingRejectionsPage.verifyActivityPageHeader();
                Assert.assertEquals(actualActivityPageHeader,taskListName);
                //verify correct batch is present
                imagingRejectionsPage.verifyBatchExist(batchNumber);
                imagingRejectionsPage.approveAndSubmit();
                System.out.println("Batch approved from " + taskListName);
                extentTest.log(Status.PASS,"Batch approved from " + taskListName);
            }
        }
        catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    public void logOut(){
        appsLoginPage.logOut();
    }

    @AfterMethod
    public void afterMethod(ITestResult result)
    {
        if(!result.isSuccess())
            System.setProperty("TestStatus","false");
    }
}
