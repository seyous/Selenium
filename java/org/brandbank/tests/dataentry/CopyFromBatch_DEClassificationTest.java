package org.brandbank.tests.dataentry;

import com.aventstack.extentreports.Status;
import org.brandbank.libs.CSVDataManager;
import org.brandbank.libs.PropertyManager;
import org.brandbank.pages.LoginPage;
import org.brandbank.pages.apps.AppsLoginPage;
import org.brandbank.pages.apps.TasklistSearchAndAssignPage;
import org.brandbank.pages.dataentry.DEClassificationPage;
import org.brandbank.pages.dataentry.DETasklistSearchAndAssignPage;
import org.brandbank.tests.TestBaseSetup;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class CopyFromBatch_DEClassificationTest extends TestBaseSetup {
    LoginPage loginPage;
    AppsLoginPage appsLoginPage;
    DETasklistSearchAndAssignPage deTasklistSearchAndAssignPage;
    DEClassificationPage deClassificationPage;
    TasklistSearchAndAssignPage tasklistSearchAndAssignPage;
    CSVDataManager csvDataManager;
    public CopyFromBatch_DEClassificationTest(){
        loginPage = new LoginPage(driver);
        csvDataManager = new CSVDataManager();
        appsLoginPage = new AppsLoginPage(driver);
        deTasklistSearchAndAssignPage = new DETasklistSearchAndAssignPage(driver);
        deClassificationPage =new DEClassificationPage(driver);
        tasklistSearchAndAssignPage = new TasklistSearchAndAssignPage(driver);
    }

    @Test
    @Parameters({"testSuite","numOfProducts"})
    public void DataEntryClassification(String testSuite,int numOfProducts) throws Exception{
        try {
                String tasklistName = "Data Entry Classification";
                String copyFromBatchNumber = csvDataManager.getCSVData(testSuite, 1)[4];
                String copyFromPvid = csvDataManager.getCSVData(testSuite, 1)[5];
                String batchNumber = csvDataManager.getCSVData(testSuite, 2)[4];
                String EAN = csvDataManager.getCSVData(testSuite, 2)[1];

                tasklistSearchAndAssignPage.selectTasklist(tasklistName);
                deTasklistSearchAndAssignPage.searchBatchNum(tasklistName,batchNumber,EAN);
                deTasklistSearchAndAssignPage.clickOnAssignToMe();
                tasklistSearchAndAssignPage.clickOnAssignedTask(tasklistName);

                //verify activity page header
                String actualActivityPageHeader = deClassificationPage.verifyActivityPageHeader();
                Assert.assertEquals(actualActivityPageHeader, "Copy data from another Product Version");
                deClassificationPage.selectActivityProduct("Batch");
                deClassificationPage.enterBatchId(copyFromBatchNumber);
                deClassificationPage.selectPVIDRowPopUp(copyFromPvid);
                System.out.println("Batch to copy over found");
                extentTest.log(Status.INFO,"Batch to copy over found");
                deClassificationPage.clickActivityComplete();

                deClassificationPage.selectCheckBoxNoAmendmentsTo();
                deClassificationPage.clickActivityComplete();
                System.out.println("Product Data Copied Successfully");
                extentTest.log(Status.PASS,"Product Data Copied Successfully");
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
