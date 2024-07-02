package org.brandbank.tests.rejections;

import com.aventstack.extentreports.Status;
import org.brandbank.libs.CSVDataManager;
import org.brandbank.libs.PropertyManager;
import org.brandbank.pages.LoginPage;
import org.brandbank.pages.rejections.RejectionsPage;
import org.brandbank.tests.TestBaseSetup;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class RejectionsTest extends TestBaseSetup {
    LoginPage loginPage;
    PropertyManager propertyManager;
    CSVDataManager csvDataManager;
    RejectionsPage rejectionsPage;
    public RejectionsTest() {
        loginPage = new LoginPage(driver);
        propertyManager = new PropertyManager();
        csvDataManager = new CSVDataManager();
        rejectionsPage=new RejectionsPage(driver);
    }

    @Test
    @Parameters({"testSuite","action"})
    public void rejectionsTest(String testSuite,String action) throws Exception{
        try {
            String EAN = csvDataManager.getCSVData(testSuite, 1)[1];
            String SubCode = csvDataManager.getCSVData(testSuite, 1)[0];
            rejectionsPage.searchProduct(EAN);
            String actualEAN = rejectionsPage.verifyEAN(EAN);
            Assert.assertEquals(actualEAN, EAN);
            String actualSubCode = rejectionsPage.verifySubscriber(SubCode);
            Assert.assertEquals(actualSubCode, SubCode);
            System.out.println("EAN found");
            extentTest.log(Status.PASS,"EAN found");
            rejectionsPage.productAction(action);
            Thread.sleep(3000);
            rejectionsPage.logOut();
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
