package org.brandbank.tests.manualactions;

import com.aventstack.extentreports.Status;
import org.brandbank.libs.CSVDataManager;
import org.brandbank.libs.PropertyManager;
import org.brandbank.pages.LoginPage;
import org.brandbank.pages.manualactions.ManualActionsPage;
import org.brandbank.tests.TestBaseSetup;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class ManualActionsTest extends TestBaseSetup {
    LoginPage loginPage;
    PropertyManager propertyManager;
    CSVDataManager csvDataManager;
    ManualActionsPage manualActionsPage;
    public ManualActionsTest(){
        loginPage = new LoginPage(driver);
        propertyManager = new PropertyManager();
        csvDataManager = new CSVDataManager();
        manualActionsPage = new ManualActionsPage(driver);
    }

    @Test
    @Parameters({"testSuite"})
    public void manualActions(String testSuite) throws Exception{
        try{
            String EAN = csvDataManager.getCSVData(testSuite, 1)[1];
            String subCode = csvDataManager.getCSVData(testSuite, 1)[0];
            manualActionsPage.completeDelist(subCode,EAN);
            System.out.println("EAN : "+EAN+" delisted successfully");
            extentTest.log(Status.PASS,"EAN : "+EAN+" delisted successfully");
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
