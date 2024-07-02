package org.brandbank.tests.apps;

import com.aventstack.extentreports.Status;
import org.brandbank.libs.CSVDataManager;
import org.brandbank.libs.PropertyManager;
import org.brandbank.pages.LoginPage;
import org.brandbank.pages.apps.AppsLoginPage;
import org.brandbank.pages.apps.BookingInPage;
import org.brandbank.pages.apps.BoxRPage;
import org.brandbank.pages.apps.TasklistSearchAndAssignPage;
import org.brandbank.tests.TestBaseSetup;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class BoxRTest extends TestBaseSetup {
    LoginPage loginPage;
    AppsLoginPage appsLoginPage;
    BookingInPage bookingInPage;
    CSVDataManager csvDataManager;
    TasklistSearchAndAssignPage tasklistSearchAndAssignPage;
    BoxRPage boxrpage;
    public BoxRTest(){
        loginPage = new LoginPage(driver);
        bookingInPage = new BookingInPage(driver);
        csvDataManager = new CSVDataManager();
        appsLoginPage = new AppsLoginPage(driver);
        tasklistSearchAndAssignPage = new TasklistSearchAndAssignPage(driver);
        boxrpage = new BoxRPage(driver);
    }

    @Test
    @Parameters({"testSuite","numOfProducts"})
    public void abortFromBoxR(String testSuite,int numOfProducts) throws Exception{
        try {
            tasklistSearchAndAssignPage.selectTasklist("BOX-R");
            //Abort from BOX-R
            boolean eanFound=boxrpage.abortProductFromBoxR(testSuite, numOfProducts);
            Assert.assertEquals(eanFound,true);
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
