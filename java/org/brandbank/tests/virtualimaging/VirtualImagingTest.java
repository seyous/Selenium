package org.brandbank.tests.virtualimaging;

import com.aventstack.extentreports.Status;
import org.brandbank.libs.CSVDataManager;
import org.brandbank.libs.LaunchTrayApp;
import org.brandbank.libs.PropertyManager;
import org.brandbank.pages.apps.AppsLoginPage;
import org.brandbank.pages.apps.BoxRPage;
import org.brandbank.pages.apps.ImagingTasksPage;
import org.brandbank.pages.apps.TasklistSearchAndAssignPage;
import org.brandbank.pages.LoginPage;
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

public class VirtualImagingTest extends TestBaseSetup {
    LoginPage loginPage;
    AppsLoginPage appsLoginPage;
    ImagingTasksPage imagingTasksPage;
    TasklistSearchAndAssignPage tasklistSearchAndAssignPage;
    AttachImagesPage attachImagesPage;
    CopyImagesPage copyImagesPage;
    CompleteTaskPage completeTaskPage;
    PostProductionPage postProductionPage;
    BoxRPage boxrpage;
    CSVDataManager csvDataManager;
    LaunchTrayApp trayapp;

    public VirtualImagingTest(){
        loginPage = new LoginPage(driver);
        appsLoginPage = new AppsLoginPage(driver);
        imagingTasksPage = new ImagingTasksPage(driver);
        tasklistSearchAndAssignPage = new TasklistSearchAndAssignPage(driver);
        attachImagesPage = new AttachImagesPage(driver);
        copyImagesPage = new CopyImagesPage(driver);
        completeTaskPage = new CompleteTaskPage(driver);
        postProductionPage = new PostProductionPage(driver);
        boxrpage = new BoxRPage(driver);
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
    @Parameters({"testSuite","productsPerBatch","numOfBatches"})
    public void searchProductandAssign(String testSuite, int productsPerBatch,int numOfBatches) throws Exception{
        try {
            int csvRow = 1;
            for(int j=1;j<=numOfBatches;j++) {
                String batchNum = csvDataManager.getCSVData(testSuite, csvRow)[4];
                tasklistSearchAndAssignPage.selectTasklist("Virtual Imaging");
                for (int i = 1; i <= productsPerBatch; i++) {
                    String EAN = csvDataManager.getCSVData(testSuite, csvRow)[1];
                    boolean batchAssigned = tasklistSearchAndAssignPage.searchBatchAndAssign("Virtual Imaging", batchNum, EAN);
                    csvRow++;
                    Assert.assertEquals(batchAssigned, true);
                    extentTest.log(Status.PASS,"Batch Assigned");
                }
            }
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
            String imagesPath = System.getProperty("user.dir") + "\\src\\test\\resources\\testdata\\VIRTIM\\Images";
            String csvFilePath = System.getProperty("user.dir") + "/src/test/resources/testdata/VIRTIM/AttachToProduct.csv";

            tasklistSearchAndAssignPage.clickOnAssignedTask("Virtual Imaging");
            Thread.sleep(3000);
            //checking batch is present on screen
            boolean batchFound = imagingTasksPage.verifyBatchFound("Virtual Imaging",batchNum);
            Assert.assertEquals(batchFound, true);
            //Attach images
            boolean verifyImages = attachImagesPage.attachToProduct(imagesPath,EAN,csvFilePath);
            Assert.assertEquals(verifyImages, true);
            //Generate Composite
            attachImagesPage.generateComposite();
        }
        catch (Exception e){
            throw e;
        }
    }

    @Test
    @Parameters({"testSuite","copyPvid","eanRow","imagesToExclude"})
    public void copyImages(String testSuite,String copyPvid, int eanRow, String imagesToExclude) throws Exception {
        try {
            String batchNum = csvDataManager.getCSVData(testSuite, eanRow)[4];
            tasklistSearchAndAssignPage.clickOnAssignedTask("Virtual Imaging");
            Thread.sleep(3000);
            //checking batch is present on screen
            boolean batchFound = imagingTasksPage.verifyBatchFound("Virtual Imaging", batchNum);
            Assert.assertEquals(batchFound, true);
            //Copy Images
            copyImagesPage.copyImagesToProduct(testSuite, copyPvid, eanRow, imagesToExclude);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    @Parameters({"testSuite","numOfBatches"})
    public void completeTask(String testSuite,int numOfBatches) throws Exception{

         for(int i=1;i<=numOfBatches;i++) {
            String batchNum = csvDataManager.getCSVData(testSuite, i)[4];
            tasklistSearchAndAssignPage.clickOnAssignedTask("Virtual Imaging");
            //checking batch is present on screen
            boolean batchFound = imagingTasksPage.verifyBatchFound("Virtual Imaging", batchNum);
            Assert.assertEquals(batchFound, true);
            //Approve and Complete
            completeTaskPage.approveAndCompleteTask(batchNum);
        }
    }

    @Test
    @Parameters({"testSuite","productsPerBatch"})
    public  void PPV(String testSuite,int productsPerBatch) throws Exception {
        try {
            String batchNum = csvDataManager.getCSVData(testSuite, 1)[4];
            for(int i=1;i<=productsPerBatch;i++) {
                String EAN = csvDataManager.getCSVData(testSuite, i)[1];
                tasklistSearchAndAssignPage.selectTasklist("Post Production Virtual");
                boolean batchAssigned = tasklistSearchAndAssignPage.searchBatchAndAssign("Post Production Virtual", batchNum, EAN);
                Assert.assertEquals(batchAssigned, true);
            }
            tasklistSearchAndAssignPage.clickOnAssignedTask("Post Production Virtual");
            //checking batch is present on screen
            boolean batchFound = imagingTasksPage.verifyBatchFound("Post Production Virtual",batchNum);
            Assert.assertEquals(batchFound, true);
            //Approve from Post Production Virtual
            postProductionPage.approvePPV(batchNum);
        }
        catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    @Parameters({"testSuite","productsPerBatch"})
    public  void PPVM(String testSuite,int productsPerBatch) throws Exception {
        try {
            String batchNum = csvDataManager.getCSVData(testSuite, 1)[4];
            for(int i=1;i<=productsPerBatch;i++) {
                String EAN = csvDataManager.getCSVData(testSuite, i)[1];
                tasklistSearchAndAssignPage.selectTasklist("Post Production Virtual Merch");
                boolean batchAssigned = tasklistSearchAndAssignPage.searchBatchAndAssign("Post Production Virtual Merch", batchNum, EAN);
                Assert.assertEquals(batchAssigned, true);
            }
            tasklistSearchAndAssignPage.clickOnAssignedTask("Post Production Virtual Merch");
            //checking batch is present on screen
            boolean batchFound = imagingTasksPage.verifyBatchFound("Post Production Virtual Merch",batchNum);
            Assert.assertEquals(batchFound, true);
            //Approve from Post Production Virtual Merch
            postProductionPage.approvePPVM(batchNum);
        }
        catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    @Parameters({"testSuite","productsPerBatch"})
    public void abortFromBoxR(String testSuite,int productsPerBatch) throws Exception{
        try {
            tasklistSearchAndAssignPage.selectTasklist("BOX-R");
            //Abort from BOX-R
            boolean eanFound = boxrpage.abortProductFromBoxR(testSuite, productsPerBatch);
            Assert.assertEquals(eanFound,true);
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
