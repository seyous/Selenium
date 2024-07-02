package org.brandbank.tests.imageshotconsole;

import com.aventstack.extentreports.Status;
import org.brandbank.base.BaseClass;
import org.brandbank.libs.ImageShotConsoleApp;
import org.brandbank.tests.libs.ExtentReportsManager;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.*;

import java.io.IOException;

public class ImageShotConsoleTest extends BaseClass {

    ImageShotConsoleApp imageShotConsoleApp;


    public ImageShotConsoleTest() {
        imageShotConsoleApp = new ImageShotConsoleApp();
    }

    @Test
    public void launchImageShot() throws IOException, InterruptedException {
        System.out.println("Starting to launch ImageConsole App to resync images ");
        boolean launchImageshot = imageShotConsoleApp.imageShotConsole();
        Assert.assertEquals(launchImageshot, true);
        extentTest.log(Status.PASS, "Imageshot Console launched successfully");
    }

    @BeforeSuite
    public void suiteSetUp(ITestContext context) {
        String testSuiteName = context.getCurrentXmlTest().getSuite().getName();
        extentReports = ExtentReportsManager.setUpExtentReport(testSuiteName);
    }

    @AfterSuite
    public void afterSetUp() {
        extentReports.flush();
    }

    @BeforeTest
    public void beforeTestSetUp(ITestContext context) {
        extentTest = extentReports.createTest(context.getName());
    }
}