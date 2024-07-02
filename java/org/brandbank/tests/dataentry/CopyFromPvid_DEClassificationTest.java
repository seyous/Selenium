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

public class CopyFromPvid_DEClassificationTest extends TestBaseSetup {
    LoginPage loginPage;
    AppsLoginPage appsLoginPage;
    DETasklistSearchAndAssignPage deTasklistSearchAndAssignPage;
    DEClassificationPage deClassificationPage;
    TasklistSearchAndAssignPage tasklistSearchAndAssignPage;
    CSVDataManager csvDataManager;
    public CopyFromPvid_DEClassificationTest(){
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
            for (int i = 1; i <= numOfProducts; i++) {
                String tasklistName = "Data Entry Classification";
                String batchNumber = csvDataManager.getCSVData(testSuite, i)[4];
                String EAN = csvDataManager.getCSVData(testSuite, i)[1];
                tasklistSearchAndAssignPage.selectTasklist(tasklistName);
                deTasklistSearchAndAssignPage.searchBatchNum(tasklistName,batchNumber,EAN);
                deTasklistSearchAndAssignPage.clickOnAssignToMe();
                tasklistSearchAndAssignPage.clickOnAssignedTask(tasklistName);

                //verify activity page header
                String actualActivityPageHeader = deClassificationPage.verifyActivityPageHeader();
                Assert.assertEquals(actualActivityPageHeader, "Copy data from another Product Version");
                deClassificationPage.selectActivityProduct("PVID");
                deClassificationPage.enterPVIDValue("12235524");
                deClassificationPage.selectPVIDRowPopUp("12235524");
                extentTest.log(Status.INFO,"PVID to copy over found");
                System.out.println("PVID to copy over found");
                deClassificationPage.clickRecache();
                deClassificationPage.selectTemplate("IT Test Automation - Do Not Use");
                deClassificationPage.clickActivityComplete();

                deClassificationPage.selectCheckBoxNoAmendmentsTo();
                deClassificationPage.clickActivityComplete();
                extentTest.log(Status.PASS,"Product Data Copied Successfully");
                System.out.println("Product Data Copied Successfully");
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
