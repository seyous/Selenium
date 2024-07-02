package org.brandbank.tests.apps;

import com.aventstack.extentreports.Status;
import org.brandbank.libs.CSVDataManager;
import org.brandbank.libs.PropertyManager;
import org.brandbank.pages.LoginPage;
import org.brandbank.pages.apps.AppsLoginPage;
import org.brandbank.pages.apps.BookingInPage;
import org.brandbank.pages.apps.SupersedeBatchPage;
import org.brandbank.tests.TestBaseSetup;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class SupersedeBatchTest extends TestBaseSetup {
    LoginPage loginPage;
    AppsLoginPage appsLoginPage;
    BookingInPage bookingInPage;
    CSVDataManager csvDataManager;
    SupersedeBatchPage supersedeBatchPage;
    public SupersedeBatchTest(){
        loginPage = new LoginPage(driver);
        appsLoginPage=new AppsLoginPage(driver);
        bookingInPage=new BookingInPage(driver);
        csvDataManager=new CSVDataManager();
        supersedeBatchPage=new SupersedeBatchPage(driver);

    }

    @Test
    @Parameters({"testSuite","subCode"})
    public void supersedeBatch(String testSuite, String subCode) throws Exception{
        try {
            String batchId = csvDataManager.getCSVData(testSuite, 1)[4];
            boolean BatchFound = bookingInPage.Search_Order_To_BookIn("Batch",batchId,subCode);
            if(BatchFound)
            {
                System.out.println("batch found" );
                extentTest.log(Status.PASS,"Batch Found");
                supersedeBatchPage.supersedeBatch(batchId);
            }
            else
                System.out.println("batch not found" );
            extentTest.log(Status.PASS,"Batch not found");
        }
        catch (AssertionError e) {
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
