package org.brandbank.tests.apps;

import com.aventstack.extentreports.Status;
import org.brandbank.libs.CSVDataManager;
import org.brandbank.libs.LaunchTrayApp;
import org.brandbank.libs.PropertyManager;
import org.brandbank.pages.LoginPage;
import org.brandbank.pages.apps.AppsLoginPage;
import org.brandbank.pages.apps.BoxRPage;
import org.brandbank.pages.apps.ImagingTasksPage;
import org.brandbank.pages.apps.TasklistSearchAndAssignPage;
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

public class EXTAssetStoreTest extends TestBaseSetup {
    LoginPage loginPage;
    AppsLoginPage appsLoginPage;
    ImagingTasksPage imagingTasksPage;
    TasklistSearchAndAssignPage tasklistSearchAndAssignPage;
    AttachImagesPage attachImagesPage;
    CompleteTaskPage completeTaskPage;
    CSVDataManager csvDataManager;
    LaunchTrayApp trayapp;

    public EXTAssetStoreTest(){
        loginPage = new LoginPage(driver);
        appsLoginPage = new AppsLoginPage(driver);
        imagingTasksPage = new ImagingTasksPage(driver);
        tasklistSearchAndAssignPage = new TasklistSearchAndAssignPage(driver);
        attachImagesPage = new AttachImagesPage(driver);
        completeTaskPage = new CompleteTaskPage(driver);
        csvDataManager = new CSVDataManager();
        trayapp = new LaunchTrayApp();
    }

    @BeforeClass(dependsOnMethods = {"beforeClassSetUp"})
    public void setUp() throws InterruptedException, IOException {
        try{
            System.out.println("Starting to launch TrayApp");
            boolean trayAppLaunched = trayapp.openTrayApp();
            Assert.assertEquals(trayAppLaunched,true);
            boolean trayAppConnected = appsLoginPage.connectTrayApp();
            Assert.assertEquals(trayAppConnected,true);
            extentTest.log(Status.PASS,"TrayApp connected successfully");
        }
        catch (AssertionError e){
            System.setProperty("TestStatus", "false");
            extentTest.log(Status.FAIL,"Failed to connect TrayApp");
            throw e;
        }
    }

    @Test
    @Parameters({"testSuite"})
    public void searchBatchAndAssign(String testSuite) throws Exception{
        try {
            String batchNum = csvDataManager.getCSVData(testSuite, 1)[4];
            tasklistSearchAndAssignPage.selectTasklist("Virtual Imaging");
            String EAN = csvDataManager.getCSVData(testSuite, 1)[1];
            boolean batchAssigned = tasklistSearchAndAssignPage.searchBatchAndAssign("Virtual Imaging", batchNum, EAN);
            Assert.assertEquals(batchAssigned, true);
            extentTest.log(Status.PASS,"Batch Assigned");
        }
        catch (Exception e){
            throw e;
        }
    }

    @Test
    @Parameters({"testSuite"})
    public void attachImages(String testSuite) throws Exception{
        try {
            String batchNum = csvDataManager.getCSVData(testSuite, 1)[4];
            String EAN = csvDataManager.getCSVData(testSuite, 1)[1];
            String imagesPath = System.getProperty("user.dir") + "\\src\\test\\resources\\testdata\\EXTAssetStore\\Images";
            String csvFilePath = System.getProperty("user.dir") + "/src/test/resources/testdata/EXTAssetStore/AttachToProduct.csv";

            tasklistSearchAndAssignPage.clickOnAssignedTask("Virtual Imaging");
            Thread.sleep(3000);
            //checking batch is present on screen
            boolean batchFound = imagingTasksPage.verifyBatchFound("Virtual Imaging",batchNum);
            Assert.assertEquals(batchFound, true);
            extentTest.log(Status.PASS,"Batch Found");
            //Attach images
            boolean attachImages = attachImagesPage.attachToProduct(imagesPath,EAN,csvFilePath);
            Assert.assertEquals(attachImages, true);
            extentTest.log(Status.PASS,"Image Attached");
            //Generate Composite
            attachImagesPage.verifyImages(EAN);
        }
        catch (Exception e){
            throw e;
        }
    }

    @Test
    @Parameters({"testSuite"})
    public void completeTask(String testSuite) throws Exception {
            String batchNum = csvDataManager.getCSVData(testSuite, 1)[4];
            tasklistSearchAndAssignPage.clickOnAssignedTask("Virtual Imaging");
            //checking batch is present on screen
            boolean batchFound = imagingTasksPage.verifyBatchFound("Virtual Imaging", batchNum);
            Assert.assertEquals(batchFound, true);
            extentTest.log(Status.PASS,"Batch Found");
            //Approve and Complete
            completeTaskPage.approveAndCompleteTask(batchNum);
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
