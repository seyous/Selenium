package org.brandbank.tests.apps;

import com.aventstack.extentreports.Status;
import org.brandbank.libs.CSVDataManager;
import org.brandbank.libs.PropertyManager;
import org.brandbank.pages.LoginPage;
import org.brandbank.pages.apps.AppsLoginPage;
import org.brandbank.pages.apps.ImagingTasksPage;
import org.brandbank.pages.apps.TasklistSearchAndAssignPage;
import org.brandbank.tests.TestBaseSetup;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class PostProdQAEssentialTest extends TestBaseSetup {
    LoginPage loginPage;
    AppsLoginPage appsLoginPage;
    CSVDataManager csvDataManager;
    TasklistSearchAndAssignPage tasklistSearchAndAssignPage;
    ImagingTasksPage imagingTasksPage;
    public PostProdQAEssentialTest(){
        loginPage = new LoginPage(driver);
        csvDataManager = new CSVDataManager();
        appsLoginPage = new AppsLoginPage(driver);
        tasklistSearchAndAssignPage = new TasklistSearchAndAssignPage(driver);
        imagingTasksPage = new ImagingTasksPage(driver);
    }

    @Test
    @Parameters({"testSuite","numOfBatches"})
    public  void postProductionQAEssential(String testSuite,int numOfBatches) throws Exception {
        try {
            for (int i = 1; i <= numOfBatches; i++) {
                String taskListName = "Post Production QA Essential";
                String batchNum = csvDataManager.getCSVData(testSuite, i)[4];
                String EAN = csvDataManager.getCSVData(testSuite, i)[1];
                tasklistSearchAndAssignPage.selectTasklist(taskListName);
                boolean batchAssigned = tasklistSearchAndAssignPage.searchBatchAndAssign(taskListName, batchNum, EAN);
                Assert.assertEquals(batchAssigned,true);
                extentTest.log(Status.INFO,"Batch Assigned");
                tasklistSearchAndAssignPage.clickOnAssignedTask(taskListName);
                //checking batch is present on screen
                boolean batchFound = imagingTasksPage.verifyBatchFound(taskListName,batchNum);
                Assert.assertEquals(batchFound, true);
                extentTest.log(Status.INFO,"Batch Found");
                //checking for images on screen
                boolean imagesPresent = imagingTasksPage.checkImagesOnScreen(taskListName);
                Assert.assertEquals(imagesPresent, true);
                extentTest.log(Status.INFO,"Image Present");
                //Approve from Post Production QA Essential
                imagingTasksPage.approveMarketingTask(batchNum);
                System.out.println("Post Production QA Essential Task Completed successfully");
                extentTest.log(Status.PASS,"Post Production QA Essential Task Completed successfully");
            }
            appsLoginPage.logOut();
        }
        catch (Exception e){
            e.printStackTrace();
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
