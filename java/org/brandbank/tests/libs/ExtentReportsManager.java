package org.brandbank.tests.libs;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.brandbank.libs.PropertyManager;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ExtentReportsManager {

    static ExtentReports extentReports;
    public static ExtentReports setUpExtentReport(String testSuiteName)
    {
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd_MM_yyyy/");
        String date = simpleDateFormat1.format(new Date());
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("HH_mm_s");
        String time = simpleDateFormat2.format(new Date());
        String path = System.getProperty("user.dir")+"/target/report/"+testSuiteName+"_testng"+"/"+date+time+".html";
        ExtentSparkReporter extentSparkReporter = new ExtentSparkReporter(path);
        extentReports = new ExtentReports();
        extentReports.attachReporter(extentSparkReporter);

        extentSparkReporter.config().setDocumentTitle(testSuiteName+" Extent Report");
        extentSparkReporter.config().setTheme(Theme.STANDARD);
        extentSparkReporter.config().setReportName(testSuiteName+" Extent Report");

        extentReports.setSystemInfo("Environment", PropertyManager.getPropertiesData().getProperty("environment"));
        extentReports.setSystemInfo("User Name",System.getProperty("user.name"));
        return extentReports;
    }
}
