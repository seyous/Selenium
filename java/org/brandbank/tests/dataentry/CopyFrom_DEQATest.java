package org.brandbank.tests.dataentry;

import com.aventstack.extentreports.Status;
import org.brandbank.libs.CSVDataManager;
import org.brandbank.libs.PropertyManager;
import org.brandbank.pages.apps.TasklistSearchAndAssignPage;
import org.brandbank.pages.dataentry.DEClassificationPage;
import org.brandbank.pages.dataentry.DETasklistSearchAndAssignPage;
import org.brandbank.pages.apps.AppsLoginPage;
import org.brandbank.pages.apps.BookingInPage;
import org.brandbank.pages.LoginPage;
import org.brandbank.tests.TestBaseSetup;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class CopyFrom_DEQATest extends TestBaseSetup  {
    LoginPage loginPage;
    AppsLoginPage appsLoginPage;
    BookingInPage bookingInPage;
    CSVDataManager csvDataManager;
    DETasklistSearchAndAssignPage deTasklistSearchAndAssignPage;
    DEClassificationPage deClassificationPage;
    TasklistSearchAndAssignPage tasklistSearchAndAssignPage;

    public CopyFrom_DEQATest(){
        loginPage = new LoginPage(driver);
        bookingInPage = new BookingInPage(driver);
        csvDataManager = new CSVDataManager();
        appsLoginPage = new AppsLoginPage(driver);
        deTasklistSearchAndAssignPage = new DETasklistSearchAndAssignPage(driver);
        deClassificationPage =new DEClassificationPage(driver);
        tasklistSearchAndAssignPage = new TasklistSearchAndAssignPage(driver);
    }

    @Test
    @Parameters({"testSuite","numOfProducts","subCode"})
    public void DataEntryQA(String testSuite,int numOfProducts,String subCode) throws Exception {
        try {
            for (int i = 1; i <= numOfProducts; i++) {
                String tasklistName = "Data Entry QA";
                String batchNumber = csvDataManager.getCSVData(testSuite, i)[4];
                String EAN = csvDataManager.getCSVData(testSuite, i)[1];
                tasklistSearchAndAssignPage.selectTasklist(tasklistName);
                deTasklistSearchAndAssignPage.searchBatchNum(tasklistName, batchNumber, EAN);
                deTasklistSearchAndAssignPage.clickOnAssignToMe();
                tasklistSearchAndAssignPage.clickOnAssignedTask(tasklistName);

               deClassificationPage.clickActivityComplete();
                if(subCode.equals("OCOM898")) {
                    deClassificationPage.setFalsePositive();
                }
                deClassificationPage.clickActivityComplete();
                //Thread.sleep(10000);
                extentTest.log(Status.PASS,"QA task completed successfully for EAN " + EAN);
                System.out.println("QA task completed successfully for EAN " + EAN);
            }
            appsLoginPage.logOut();
        }
        catch (Exception e){
            e.printStackTrace();
            throw  e;
        }
    }

    @AfterMethod
    public void afterMethod(ITestResult result)
    {
        if(!result.isSuccess())
            System.setProperty("TestStatus","false");
    }
}
