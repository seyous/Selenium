package org.brandbank.tests;

import com.aventstack.extentreports.Status;
import org.brandbank.base.BaseClass;
import org.brandbank.pages.LoginPage;
import org.brandbank.pages.apps.AppsLoginPage;
import org.brandbank.pages.blackbox.ExtractsPage;
import org.brandbank.pages.boxr.SignInPage;
import org.brandbank.pages.dataworkflow.DataWorkflowHomePage;
import org.brandbank.pages.icm.IcmPage;
import org.brandbank.pages.manualactions.ManualActionsPage;
import org.brandbank.pages.productlibrary.ProductDetailPage;
import org.brandbank.pages.rejections.RejectionsPage;
import org.brandbank.pages.retailerreports.RetailerReportsHomePage;
import org.brandbank.pages.salesforce.OpportunityPage;
import org.brandbank.pages.supplierportal.SPHomePage;
import org.brandbank.tests.libs.ExtentReportsManager;
import org.brandbank.tests.libs.RetryTests;
import org.openqa.selenium.WebDriver;
import org.testng.*;
import org.testng.annotations.*;

import java.lang.reflect.Method;


public class FailoverTestSetup extends BaseClass  {

    public WebDriver driver;
    LoginPage loginPage;
    AppsLoginPage appsLoginPage;
    ProductDetailPage productDetailPage;
    DataWorkflowHomePage dataWorkflowHomePage;
    SPHomePage SPHomePage;
    SignInPage signInPage;
    IcmPage icmPage;
    RejectionsPage rejectionsPage;
    OpportunityPage opportunityPage;
    ExtractsPage extractsPage;
    RetailerReportsHomePage retailerReportsHomePage;
    ManualActionsPage manualActionsPage;

    public FailoverTestSetup() {
        loginPage = new LoginPage(driver);
        appsLoginPage = new AppsLoginPage(driver);
        productDetailPage=new ProductDetailPage(driver);
        dataWorkflowHomePage = new DataWorkflowHomePage(driver);
        SPHomePage =new SPHomePage(driver);
        signInPage=new SignInPage(driver);
        icmPage=new IcmPage(driver);
        opportunityPage = new OpportunityPage(driver);
        rejectionsPage=new RejectionsPage(driver);
        extractsPage=new ExtractsPage(driver);
        retailerReportsHomePage = new RetailerReportsHomePage(driver);
        manualActionsPage = new ManualActionsPage(driver);
    }

    @BeforeSuite(alwaysRun = true)
    public void suiteSetUp(ITestContext context) {
            driver = initProject();
            String testSuiteName = context.getCurrentXmlTest().getSuite().getName();
            extentReports = ExtentReportsManager.setUpExtentReport(testSuiteName);
        }

    @AfterSuite
    public void afterSetUp() {
        driver.quit();
        extentReports.flush();
    }


    @Parameters({"module","username","password"})
    @Test
    public void loginApplication(String moduleName,String username, String password) throws Exception
    {
        try {

            int count = 0;
            if (System.getProperty("TestStatus") != null && System.getProperty("TestStatus").equals("false")){
                do {
                    driver = getURL();
                    driver.navigate().refresh();
                } while (count++ <= 1);

                if (System.getProperty("TestStatus").equals("false"))
                {
                    Logout(RetryTests.FailedModuleName);
                }
            }

            BaseClass.moduleName = moduleName;
            driver = getURL();
            LoginTest logintest = new LoginTest();
            logintest.masterLogin(moduleName,username, password);
            Logout(moduleName);
        }
        catch(AssertionError | Exception e)
        {
            extentTest.log(Status.FAIL, e.getMessage());
            Assert.assertFalse(true);
        }
    }

    private void Logout(String moduleName) throws Exception
    {
        switch (moduleName)
        {
            case "ordering":
                Thread.sleep(2000);
                loginPage.logOut();
                break;
            case "apps":
                Thread.sleep(2000);
                appsLoginPage.logOut();
                break;
            case "blackbox":
                Thread.sleep(2000);
                extractsPage.logOut();
                break;
            case "boxr":
                Thread.sleep(2000);
                signInPage.signOut();
                break;
            case "productlibrary":
                Thread.sleep(2000);
                productDetailPage.logOut();
                break;
            case "dataworkflow":
                Thread.sleep(2000);
                dataWorkflowHomePage.logOut();
                break;
            case "testharness":
                Thread.sleep(2000);
                icmPage.logOut();
                break;
            case "rejections":
                Thread.sleep(2000);
                rejectionsPage.logOut();
                break;
            case "supplierportal":
                SPHomePage.logout();
                break;
            case "manualactions":
                Thread.sleep(2000);
                manualActionsPage.logOut();
                break;
            case "retailerreports":
                Thread.sleep(2000);
                retailerReportsHomePage.logOut();
                break;
            case "salesforce":
                Thread.sleep(2000);
                opportunityPage.logout();
                break;
            case "crm":
                Thread.sleep(2000);
                break;
        }
    }


    @BeforeMethod
    public void beforeMethodSetUp(Method method) {
        System.out.println("Test name: " + method.getName());
    }

    @AfterMethod
    public void afterMethodSetUp(ITestResult result) {
        if(!result.isSuccess())
            System.setProperty("TestStatus","false");
    }

    @BeforeTest
    public void beforeTestSetUp(ITestContext context) {
        extentTest = extentReports.createTest(context.getName());
    }
}