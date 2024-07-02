package org.brandbank.tests.apps;

import com.aventstack.extentreports.Status;
import org.brandbank.libs.CSVDataManager;
import org.brandbank.libs.PropertyManager;
import org.brandbank.pages.LoginPage;
import org.brandbank.pages.apps.AppsLoginPage;
import org.brandbank.pages.apps.BookingInPage;
import org.brandbank.pages.apps.CheckTasklistPage;
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

public class CheckTasklistTest extends TestBaseSetup  {
    LoginPage loginPage;
    AppsLoginPage appsLoginPage;
    BookingInPage bookingInPage;
    CSVDataManager csvDataManager;
    DETasklistSearchAndAssignPage deTasklistSearchAndAssignPage;
    DEClassificationPage deClassificationPage;
    TasklistSearchAndAssignPage tasklistSearchAndAssignPage;
    CheckTasklistPage checkTasklistPage;

    public CheckTasklistTest(){
        loginPage = new LoginPage(driver);
        bookingInPage = new BookingInPage(driver);
        csvDataManager = new CSVDataManager();
        appsLoginPage = new AppsLoginPage(driver);
        deTasklistSearchAndAssignPage = new DETasklistSearchAndAssignPage(driver);
        deClassificationPage =new DEClassificationPage(driver);
        tasklistSearchAndAssignPage = new TasklistSearchAndAssignPage(driver);
        checkTasklistPage = new CheckTasklistPage(driver);
    }

    @Test
    @Parameters({"testSuite","numOfProducts","taskListName"})
    public void checkTaskList(String testSuite,int numOfProducts,String taskListName) throws Exception {
        try {
            for (int i = 1; i <= numOfProducts; i++) {
                String batchNumber = csvDataManager.getCSVData(testSuite, i)[4];
                String EAN = csvDataManager.getCSVData(testSuite, i)[1];
                tasklistSearchAndAssignPage.selectTasklist(taskListName);
                boolean EANFound = checkTasklistPage.searchBatch(taskListName,batchNumber);
                Assert.assertEquals(EANFound,true);
                System.out.println("EAN " + EAN + " found in " +taskListName );
                extentTest.log(Status.INFO,"EAN " + EAN + " found in " +taskListName );
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
