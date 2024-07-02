package org.brandbank.tests.boxr;

import com.aventstack.extentreports.Status;
import org.brandbank.libs.BrowserActions;
import org.brandbank.libs.CSVDataManager;
import org.brandbank.libs.PropertyManager;
import org.brandbank.pages.boxr.APIPage;
import org.brandbank.tests.TestBaseSetup;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.Properties;

public class APIExportTest extends TestBaseSetup {
    CSVDataManager csvDataManager;
    BrowserActions browserActions;
    APIPage apiPage;
    Properties configProperties;
    public APIExportTest() {
        csvDataManager=new CSVDataManager();
        browserActions=new BrowserActions(driver);
        apiPage = new APIPage(driver);
    }

    @BeforeTest
    private String getAccessKey() {
        configProperties = PropertyManager.getPropertiesData();
        String accessKey = configProperties.getProperty("environment")+".boxr.accesskey";
        return configProperties.getProperty(accessKey);
    }
    @BeforeTest
    private String getAccountId() {
        configProperties = PropertyManager.getPropertiesData();
        String accountId = configProperties.getProperty("environment")+".boxr.accountid";
        return configProperties.getProperty(accountId);
    }

    @Test
    public void authenticate(){
        try {
            String accessKey = getAccessKey();
            String accountId = getAccountId();
            apiPage.getToken(accessKey,accountId);
            System.out.println("Authentication done successfully");
            extentTest.log(Status.PASS,"Authentication done successfully");
        }
        catch (AssertionError e){
            System.setProperty("TestStatus", "false");
            extentTest.log(Status.FAIL,"Authentication not done successfully");
            throw e;
        }
    }

    @Test
    @Parameters({"testSuite"})
    public void exportProduct(String testSuite) throws Exception {
        try {
            String pvid = csvDataManager.getCSVData(testSuite, 1)[0];
            apiPage.exportRequest(pvid);
            System.out.println("Product exported successfully");
            extentTest.log(Status.PASS,"Product exported successfully");
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
