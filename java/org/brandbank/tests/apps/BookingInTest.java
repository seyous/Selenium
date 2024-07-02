package org.brandbank.tests.apps;

import com.aventstack.extentreports.Status;
import org.brandbank.libs.CSVDataManager;
import org.brandbank.libs.PropertyManager;
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

public class BookingInTest extends TestBaseSetup {
    LoginPage loginPage;
    AppsLoginPage appsLoginPage;
    BookingInPage bookingInPage;
    CSVDataManager csvDataManager;

    public BookingInTest() {
        loginPage = new LoginPage(driver);
        bookingInPage = new BookingInPage(driver);
        csvDataManager = new CSVDataManager();
        appsLoginPage = new AppsLoginPage(driver);
    }

    @Test
    @Parameters({"testSuite","productType","numOfBatches","productsPerBatch","subCode"})
    public void BookingInTest(String testSuite, String productType, int numOfBatches, int productsPerBatch, String subCode) throws Exception {
        try {
            boolean BookedInBatch = bookingInPage.Bookin_NewBatch(testSuite,productType,numOfBatches, productsPerBatch,subCode);
            Assert.assertEquals(BookedInBatch,true);
            extentTest.log(Status.PASS,"Batch created successfully");
            appsLoginPage.logOut();
            extentTest.log(Status.INFO,"Logged out of Booking In");
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
