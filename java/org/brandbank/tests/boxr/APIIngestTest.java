package org.brandbank.tests.boxr;

import com.aventstack.extentreports.Status;
import org.brandbank.libs.BrowserActions;
import org.brandbank.libs.CSVDataManager;
import org.brandbank.libs.PropertyManager;
import org.brandbank.pages.boxr.*;
import org.brandbank.tests.TestBaseSetup;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.util.Properties;

public class APIIngestTest extends TestBaseSetup {
    CSVDataManager csvDataManager;
    BrowserActions browserActions;
    APIPage apiPage;
    Properties configProperties;
    public APIIngestTest() {
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
            throw e;
        }
    }

    @Test
    @Parameters({"testSuite"})
    public void ingestProduct(String testSuite) throws Exception {
        try {
            String sourceFile = System.getProperty("user.dir") + "/src/test/resources/testdata/BoxR_CICD/v2Request.json";
            String targetFile = System.getProperty("user.dir") + "/src/test/resources/testdata/BoxR_CICD/modifiedFile.json";
            apiPage.submitV2Request(testSuite, sourceFile, targetFile);
            System.out.println("Product ingested successfully");
            extentTest.log(Status.PASS,"Product ingested successfully");
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
