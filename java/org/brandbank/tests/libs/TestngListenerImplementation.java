package org.brandbank.tests.libs;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import org.apache.commons.io.FileUtils;
import org.brandbank.base.BaseClass;
import org.brandbank.tests.TestBaseSetup;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestngListenerImplementation implements ITestListener {

    public void onTestStart(ITestResult iTestResult)
    {

    }

    public void onTestSuccess(ITestResult iTestResult)
    {
        TestBaseSetup.extentTest.pass(MarkupHelper.createLabel("Test case: "+iTestResult.getMethod().getMethodName()+" is Passed", ExtentColor.GREEN));
    }

    public void onTestFailure(ITestResult iTestResult)
    {
        String testSuiteName = iTestResult.getTestContext().getCurrentXmlTest().getSuite().getName();
        TestBaseSetup.extentTest.fail(MarkupHelper.createLabel("Test case: "+iTestResult.getMethod().getMethodName()+" is Failed", ExtentColor.RED));
        TestBaseSetup.extentTest.fail(iTestResult.getThrowable());

        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd_MM_yyyy/");
        String date = simpleDateFormat1.format(new Date());
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("HH_mm_s");
        String time = simpleDateFormat2.format(new Date());
        TakesScreenshot ts = (TakesScreenshot) BaseClass.driver;
        File source = (ts.getScreenshotAs(OutputType.FILE));

        String destination = System.getProperty("user.dir")+"/target/report/"+testSuiteName+"_testng"+"/"+date+iTestResult.getMethod().getMethodName()+"_"+time+".png";
        File fileDestination = new File(destination);
        try {
            FileUtils.copyFile(source, fileDestination);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        TestBaseSetup.extentTest.addScreenCaptureFromPath(destination);
    }

    public void onTestSkipped(ITestResult iTestResult)
    {
        TestBaseSetup.extentTest.skip(MarkupHelper.createLabel("Test case: "+iTestResult.getMethod().getMethodName()+" is Skipped", ExtentColor.AMBER));
    }

    public void onFinish(ITestContext iTestContext)
    {
        TestBaseSetup.extentReports.flush();
    }

    public void onStart(ITestContext iTestContext)
    {

    }

}
