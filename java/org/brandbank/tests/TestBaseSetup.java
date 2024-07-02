package org.brandbank.tests;

import org.brandbank.base.BaseClass;
import org.brandbank.tests.libs.ExtentReportsManager;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.SkipException;
import org.testng.annotations.*;

public class TestBaseSetup extends BaseClass {
    public WebDriver driver;

    @BeforeSuite
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

    @BeforeClass
    @Parameters({"module","username","password"})
    public void beforeClassSetUp(String moduleName, String username, String password) throws Exception {
        if(System.getProperty("TestStatus")!=null && System.getProperty("TestStatus").equals("false"))
            throw new SkipException("Testcase failed hence skipping the test");
        BaseClass.moduleName = moduleName;
        driver = getURL();
        LoginTest logintest = new LoginTest();
        logintest.masterLogin(moduleName,username,password);
    }

    @BeforeMethod
    public void beforeMethodSetUp() {
        if(System.getProperty("TestStatus")!=null && System.getProperty("TestStatus").equals("false"))
            throw new SkipException("Testcase failed hence skipping the test");
    }

    @AfterMethod
    public void afterMethodSetUp() {
        if(System.getProperty("TestStatus")!=null && System.getProperty("TestStatus").equals("false"))
           driver.quit();
    }

    @BeforeTest
    public void beforeTestSetUp(ITestContext context) {
        extentTest = extentReports.createTest(context.getName());
    }
}
