package org.brandbank.tests.virtualimaging;

import com.aventstack.extentreports.Status;
import org.brandbank.libs.LaunchTrayApp;
import org.brandbank.libs.PropertyManager;
import org.brandbank.pages.LoginPage;
import org.brandbank.pages.apps.AppsLoginPage;
import org.brandbank.tests.TestBaseSetup;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class CopyImagesTest extends TestBaseSetup {
    VirtualImagingTest virtualImagingTest;
    LoginPage loginPage;
    AppsLoginPage appsLoginPage;
    LaunchTrayApp trayapp;

    public CopyImagesTest(){
        virtualImagingTest=new VirtualImagingTest();
        loginPage=new LoginPage(driver);
        appsLoginPage=new AppsLoginPage(driver);
        trayapp = new LaunchTrayApp();
    }

    @BeforeClass(dependsOnMethods = {"beforeClassSetUp"})
    @Parameters({"username","password"})
    public void login(String username, String password) throws Exception {
        try {
            boolean trayAppLaunched = trayapp.openTrayApp();
            Assert.assertEquals(trayAppLaunched,true);
            loginPage.login(PropertyManager.getPropertiesData().getProperty(username), PropertyManager.getPropertiesData().getProperty(password));
            String pageTitlePresent = appsLoginPage.verifyTitle();
            Assert.assertEquals(pageTitlePresent,"Apps Brandbank (1.9.1.0)");
            extentTest.log(Status.PASS,"Logged into Apps successfully");
            boolean trayAppConnected = appsLoginPage.connectTrayApp();
            Assert.assertEquals(trayAppConnected,true);
            extentTest.log(Status.PASS,"TrayApp connected successfully");
        }
        catch (AssertionError e){
            System.setProperty("TestStatus", "false");
            extentTest.log(Status.FAIL,"Failed to connect TrayApp");
            throw e;
        }
    }

    @Test
    @Parameters({"testSuite","copyPvid","productsPerBatch","numOfBatches","imagesToExclude"})
    public void copyImages(String testSuite, String copyPvid, int productsPerBatch, int numOfBatches, String imagesToExclude) throws Exception{
        try {
            virtualImagingTest.searchProductandAssign(testSuite, productsPerBatch,numOfBatches);
            for(int i=1;i<=numOfBatches;i++)
            {
                virtualImagingTest.copyImages(testSuite,copyPvid,i,imagesToExclude);
            }
            virtualImagingTest.completeTask(testSuite,numOfBatches);
        }
        catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }
    @Test
    public void logOut(){
        appsLoginPage.logOut();
    }

    @AfterMethod
    public void afterMethod(ITestResult result)
    {
        if(!result.isSuccess())
            System.setProperty("TestStatus","false");
    }
}
