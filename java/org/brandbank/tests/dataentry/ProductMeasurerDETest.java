package org.brandbank.tests.dataentry;

import com.aventstack.extentreports.Status;
import org.brandbank.libs.CSVDataManager;
import org.brandbank.libs.PropertyManager;
import org.brandbank.pages.LoginPage;
import org.brandbank.pages.apps.*;
import org.brandbank.pages.dataentry.*;
import org.brandbank.tests.TestBaseSetup;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class ProductMeasurerDETest extends TestBaseSetup {
    LoginPage loginPage;
    AppsLoginPage appsLoginPage;
    DEProductCapturePage deProductCapturePage;
    DEProductMeasurerValidationPage dEProductMeasurerValidationPage;
    CSVDataManager csvDataManager;
    TasklistSearchAndAssignPage tasklistSearchAndAssignPage;
    DETasklistSearchAndAssignPage deTasklistSearchAndAssignPage;
    DEClassificationPage deClassificationPage;
    BoxRDataValidationPage boxRDataValidationPage;
    BookingInPage bookingInPage;
    SupersedeBatchPage supersedeBatchPage;
    BoxRPage boxrpage;
    public ProductMeasurerDETest() {
        loginPage = new LoginPage(driver);
        appsLoginPage = new AppsLoginPage(driver);
        deProductCapturePage=new DEProductCapturePage(driver);
        dEProductMeasurerValidationPage =new DEProductMeasurerValidationPage(driver);
        csvDataManager=new CSVDataManager();
        tasklistSearchAndAssignPage =new TasklistSearchAndAssignPage(driver);
        bookingInPage=new BookingInPage(driver);
        deTasklistSearchAndAssignPage=new DETasklistSearchAndAssignPage(driver);
        deClassificationPage =new DEClassificationPage(driver);
        boxRDataValidationPage=new BoxRDataValidationPage(driver);
        supersedeBatchPage=new SupersedeBatchPage(driver);
        boxrpage=new BoxRPage(driver);

    }

    @Test
    @Parameters({"testSuite","numOfProducts"})
    public void abortFromBoxR(String testSuite,int numOfProducts) throws Exception{
        try {
            tasklistSearchAndAssignPage.selectTasklist("BOX-R");
            //Abort from BOX-R
            boolean eanFound=boxrpage.abortProductFromBoxR(testSuite, numOfProducts);
            Assert.assertEquals(eanFound,true);
            extentTest.log(Status.PASS,eanFound +" Aborted from Box-R");
        }
        catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }
    @Test
    @Parameters({"testSuite"})
    public void dataEntryClassification(String testSuite) throws Exception {
        try {
            String tasklistName = "Data Entry Classification";
            String batchNumber = csvDataManager.getCSVData(testSuite, 1)[4];
            String EAN = csvDataManager.getCSVData(testSuite, 1)[1];
            tasklistSearchAndAssignPage.selectTasklist(tasklistName);
            deTasklistSearchAndAssignPage.searchBatchNum(tasklistName,batchNumber,EAN);
            deTasklistSearchAndAssignPage.clickOnAssignToMe();
            tasklistSearchAndAssignPage.clickOnAssignedTask(tasklistName);
            //verify activity page header
            String actualActivityPageHeader = deClassificationPage.verifyActivityPageHeader();
            Assert.assertEquals(actualActivityPageHeader, "Copy data from another Product Version");
            Thread.sleep(10000);
            deClassificationPage.selectTemplate("IT Test Automation - Do Not Use");
            System.out.println("Template selected");
            extentTest.log(Status.INFO,"Template selected");
            deClassificationPage.selectCategories();
            System.out.println("Categories selected");
            extentTest.log(Status.INFO,"Categories selected");
            deClassificationPage.selectEnglishAndMakePrimary();
            System.out.println("English Language to Capture selected and made as Primary");
            extentTest.log(Status.INFO,"English Language to Capture selected and made as Primary");
            deClassificationPage.unselectDanish();
            deClassificationPage.clickActivityComplete();
            System.out.println("DE Classification Task completed successfully");
            extentTest.log(Status.PASS,"DE Classification Task completed successfully");
        }
        catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }
    @Test
    @Parameters({"testSuite"})
    public void validateMeasuresInDE(String testSuite) throws Exception{
        try {
            String batchNumber = csvDataManager.getCSVData(testSuite, 1)[4];
            String EAN = csvDataManager.getCSVData(testSuite, 1)[1];
            String desc = csvDataManager.getCSVData(testSuite, 1)[2];
            tasklistSearchAndAssignPage.selectTasklist("Data Entry");
            deTasklistSearchAndAssignPage.searchBatchNum("Data Entry", batchNumber, EAN);
            deTasklistSearchAndAssignPage.clickOnAssignToMe();
            tasklistSearchAndAssignPage.clickOnAssignedTask("Data Entry Product Capture");
            //verify activity page header
            String actualActivityPageHeader = deProductCapturePage.verifyActivityPageHeader();
            Assert.assertEquals(actualActivityPageHeader, "Data Entry Product Capture");
            deProductCapturePage.selectLanguageFromDD(EAN, desc,"Neutral");
            System.out.println("Neutral language selected from drop down");
            extentTest.log(Status.INFO,"Neutral language selected from drop down");
            dEProductMeasurerValidationPage.standardDimensionsValidation();
            deProductCapturePage.unAssignProductFromDE();
        }
        catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }
    @Test
    @Parameters({"testSuite","subCode"})
    public void supersedeBatch(String testSuite,String subCode) throws Exception{
        try {
            String batchId = csvDataManager.getCSVData(testSuite, 1)[4];
            boolean BatchFound = bookingInPage.Search_Order_To_BookIn("Batch",batchId,subCode);
            if(BatchFound)
            {
                System.out.println("batch found");
                extentTest.log(Status.PASS,"batch found");
                supersedeBatchPage.supersedeBatch(batchId);
            }
            else {
                System.out.println("batch not found");
                extentTest.log(Status.FAIL, "batch not found");
            }
        }
        catch (AssertionError e) {
            System.setProperty("TestStatus", "false");
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
