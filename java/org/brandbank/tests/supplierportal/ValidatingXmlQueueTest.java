package org.brandbank.tests.supplierportal;

import com.aventstack.extentreports.Status;
import org.brandbank.libs.CSVDataManager;
import org.brandbank.libs.FileHandling;
import org.brandbank.libs.PropertyManager;
import org.brandbank.pages.LoginPage;
import org.brandbank.pages.blackbox.ExtractsPage;
import org.brandbank.pages.supplierportal.ApiRequestPage;
import org.brandbank.tests.TestBaseSetup;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.Properties;

public class ValidatingXmlQueueTest extends TestBaseSetup {
    ApiRequestPage apiRequestPage;
    CSVDataManager csvDataManager;
    ExtractsPage extractsPage;
    Properties configProperties;

    public  ValidatingXmlQueueTest() {
        apiRequestPage = new ApiRequestPage();
        csvDataManager = new CSVDataManager();
        extractsPage=new ExtractsPage(driver);

    }
    @BeforeTest
    @Parameters({"test"})
    private String getFunctionKey() {
        configProperties = PropertyManager.getPropertiesData();
        String functionKey= configProperties.getProperty("environment")+".evolve.functionKey";
        return configProperties.getProperty(functionKey);
    }
    @Test
    @Parameters({"testSuite"})
    public void validatingXmlQueue(String testSuite) throws Exception {
        try {
            String env;
            configProperties = PropertyManager.getPropertiesData();
            env = configProperties.getProperty("environment");
            switch(env){
                case "stg":
                    String stgUrl = "https://api.stgbrandbank.com/svc/feed/extractdata.asmx?op=GetUnsentProductData";
                    boolean pvidQueueCheck = apiRequestPage.clearingTheXmlQueue(testSuite, stgUrl);
                    Assert.assertEquals(pvidQueueCheck, true);
                    extentTest.log(Status.PASS,"no pvids are in queue");
                    break;

                case "prod":
                    String prodUrl = "https://api.brandbank.com/svc/feed/extractdata.asmx?op=GetUnsentProductData";
                    boolean pvidQueueCheckProd = apiRequestPage.clearingTheXmlQueue(testSuite,prodUrl);
                    Assert.assertEquals(pvidQueueCheckProd, true);
                    extentTest.log(Status.PASS,"no pvids are in queue");
                    break;
                default:
                    System.out.println("no env notified");
                    break;
            }
            extractsPage.logOut();
        } catch (Exception e) {
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
