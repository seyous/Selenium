package org.brandbank.tests.apps;

import com.aventstack.extentreports.Status;
import org.brandbank.libs.CSVDataManager;
import org.brandbank.libs.Outsourcing;
import org.brandbank.libs.PropertyManager;
import org.brandbank.pages.LoginPage;
import org.brandbank.pages.apps.AppsLoginPage;
import org.brandbank.pages.apps.ImagingTasksPage;
import org.brandbank.pages.apps.TasklistSearchAndAssignPage;
import org.brandbank.tests.TestBaseSetup;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.util.Properties;

public class ImagingWorkflowTest extends TestBaseSetup {
    LoginPage loginPage;
    AppsLoginPage appsLoginPage;
    CSVDataManager csvDataManager;
    TasklistSearchAndAssignPage tasklistSearchAndAssignPage;
    ImagingTasksPage imagingTasksPage;
    Outsourcing outsourcing;
    Properties configProperties;

    public ImagingWorkflowTest(){
        loginPage = new LoginPage(driver);
        csvDataManager = new CSVDataManager();
        appsLoginPage = new AppsLoginPage(driver);
        tasklistSearchAndAssignPage = new TasklistSearchAndAssignPage(driver);
        imagingTasksPage = new ImagingTasksPage(driver);
        outsourcing = new Outsourcing();
    }

    @Test
    @Parameters({"testSuite","numOfBatches"})
    public  void exitStudioQA(String testSuite,int numOfBatches) throws Exception {
        try {
            for (int i = 1; i <= numOfBatches; i++) {
                String taskListName = "Exit Studio QA";
                String batchNumber = csvDataManager.getCSVData(testSuite, i)[4];
                String EAN = csvDataManager.getCSVData(testSuite, i)[1];
                tasklistSearchAndAssignPage.selectTasklist(taskListName);
                boolean batchAssigned = tasklistSearchAndAssignPage.searchBatchAndAssign(taskListName, batchNumber,EAN);
                Assert.assertEquals(batchAssigned,true);
                extentTest.log(Status.INFO,"Batch Assigned");
                tasklistSearchAndAssignPage.clickOnAssignedTask(taskListName);
                //checking batch is present on screen
                boolean batchFound = imagingTasksPage.verifyBatchFound(taskListName, batchNumber);
                Assert.assertEquals(batchFound, true);
                extentTest.log(Status.INFO,"Batch Found");
                //checking for images on screen
                boolean imagesPresent = imagingTasksPage.checkImagesOnScreen(taskListName);
                Assert.assertEquals(imagesPresent, true);
                extentTest.log(Status.INFO,"Image Present");
                //Approve from ESQA
                imagingTasksPage.approveMarketingTask(batchNumber);
                System.out.println("ESQA Task for Batch "+batchNumber+" Completed successfully");
                extentTest.log(Status.PASS,"ESQA Task for Batch "+batchNumber+" Completed successfully");
            }
        }
        catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    @Parameters({"testSuite","numOfBatches"})
    public void marketingOutsourcing(String testSuite,int numOfBatches) throws Exception{
        try{
            for (int i = 1; i <= numOfBatches; i++) {
                configProperties = PropertyManager.getPropertiesData();
                String env = configProperties.getProperty("environment");
                String taskName = "Marketing";
                String EAN = csvDataManager.getCSVData(testSuite, i)[1];
                String batchNum = csvDataManager.getCSVData(testSuite, i)[4];

                boolean filesTransferred = outsourcing.transferFiles(testSuite, env, taskName, batchNum, 1, EAN);
                Assert.assertEquals(filesTransferred, true);
                extentTest.log(Status.PASS,"marketing Outsourcing - Files Transferred");
            }
        }
        catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    @Parameters({"testSuite","numOfBatches"})
    public void merchOutsourcing(String testSuite,int numOfBatches) throws Exception{
        try{
            for (int i = 1; i <= numOfBatches; i++) {
                configProperties = PropertyManager.getPropertiesData();
                String env = configProperties.getProperty("environment");
                String taskName = "Merchandising";
                String EAN = csvDataManager.getCSVData(testSuite, i)[1];
                String batchNum = csvDataManager.getCSVData(testSuite, i)[4];

                boolean filesTransferred = outsourcing.transferFiles(testSuite, env, taskName, batchNum, 6, EAN);
                Assert.assertEquals(filesTransferred, true);
                extentTest.log(Status.PASS,"Merch Outsourcing - Files Transferred");
            }
        }
        catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    @Parameters({"testSuite","numOfBatches"})
    public void merchQC1(String testSuite,int numOfBatches) throws Exception{
        try{
            for (int i = 1; i <= numOfBatches; i++) {
                String taskListName = "Merch QC1.Image & Dimensions";
                String batchNum = csvDataManager.getCSVData(testSuite, i)[4];
                String EAN = csvDataManager.getCSVData(testSuite, i)[1];
                tasklistSearchAndAssignPage.selectTasklist(taskListName);
                boolean batchAssigned = tasklistSearchAndAssignPage.searchBatchAndAssign(taskListName, batchNum,EAN);
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
                //Approve from Merch QC1
                imagingTasksPage.approveMerchQC1(batchNum);
                System.out.println("Merch QC1 Task Completed successfully");
                extentTest.log(Status.PASS,"Merch QC1 Task Completed successfully");
            }
        }
        catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    @Parameters({"testSuite","numOfBatches"})
    public  void postOutsourceQA(String testSuite,int numOfBatches) throws Exception {
        try {
            for (int i = 1; i <= numOfBatches; i++) {
                String taskListName = "Post Outsource QA";
                String batchNum = csvDataManager.getCSVData(testSuite, i)[4];
                String EAN = csvDataManager.getCSVData(testSuite, i)[1];
                tasklistSearchAndAssignPage.selectTasklist(taskListName);
                boolean batchAssigned = tasklistSearchAndAssignPage.searchBatchAndAssign(taskListName, batchNum,EAN);
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
                //Approve from Post Outsource QA
                imagingTasksPage.approvePostOutsourceQATask(batchNum);
                System.out.println("Post Outsource QA Task Completed successfully");
                extentTest.log(Status.PASS,"Post Outsource QA Task Completed successfully");
            }
        }
        catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    @Parameters({"testSuite","numOfBatches"})
    public  void postProductionEssential(String testSuite,int numOfBatches) throws Exception {
        try {
            for (int i = 1; i <= numOfBatches; i++) {
                String taskListName = "Post Production Essential";
                String batchNum = csvDataManager.getCSVData(testSuite, i)[4];
                String EAN = csvDataManager.getCSVData(testSuite, i)[1];
                tasklistSearchAndAssignPage.selectTasklist(taskListName);
                boolean batchAssigned = tasklistSearchAndAssignPage.searchBatchAndAssign(taskListName, batchNum,EAN);
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
                //Approve from Post Production Essential
                imagingTasksPage.approveMarketingTask(batchNum);
                System.out.println("Post Production Essential Task Completed successfully");
                extentTest.log(Status.PASS,"Post Production Essential Task Completed successfully");
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
