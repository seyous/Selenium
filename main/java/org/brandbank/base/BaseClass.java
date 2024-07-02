package org.brandbank.base;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import org.brandbank.libs.ManageBrowsers;
import org.brandbank.libs.PropertyManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Properties;


public class BaseClass {

    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss");
        System.setProperty("current.date.time", simpleDateFormat.format(new Date()));
    }

    Properties configProperties;
    String browserName;

    public static WebDriver driver;
    public static WebDriverWait wait;
    public static String moduleName;
    public static ExtentReports extentReports;
    public static ExtentTest extentTest;

    public WebDriver initProject() {
        configProperties = PropertyManager.getPropertiesData();
        browserName = configProperties.getProperty("browser");
        driver = new ManageBrowsers().launchBrowser(browserName, driver);
        return driver;
    }
    public WebDriver getURL(){
        driver.get(getModuleURL());
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(150));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
        wait = new WebDriverWait(driver, Duration.ofSeconds(Integer.parseInt(configProperties.getProperty("explicitwaittime"))), Duration.ofSeconds(8));
        return driver;
    }
    private String getModuleURL() {
        configProperties = PropertyManager.getPropertiesData();
        String key = configProperties.getProperty("environment")+"."+moduleName+".url";
        return configProperties.getProperty(key);
    }
}





