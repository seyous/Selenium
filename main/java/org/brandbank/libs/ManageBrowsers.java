package org.brandbank.libs;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.HashMap;
import java.util.Map;

public class ManageBrowsers {

    public WebDriver launchBrowser(String browserName, WebDriver driver) {
        switch(browserName) {
            case "chrome":
                System.setProperty("webdriver.http.factory", "jdk-http-client");
                ChromeOptions option = new ChromeOptions();
                Map<String, Object> prefs = new HashMap<String, Object>();
                prefs.put("plugins.plugins_disabled", new String[] {
                        "Chrome PDF Viewer"
                });
                prefs.put("plugins.always_open_pdf_externally", true);
                prefs.put("download.default_directory", System.getProperty("user.dir")+"\\downloads");
                prefs.put("safebrowsing.enabled", true);
                option.setExperimentalOption("prefs", prefs);
                option.addArguments("--remote-allow-origin=*");
                option.addArguments("--disable-site-isolation-trials");
                option.addArguments("--lang=en-GB");
                //option.addArguments("--lang=en-US");
                //option.addArguments("--headless");
                option.addArguments("--window-size=1920,1080");
                option.addArguments("--disable-gpu");
                option.addArguments("enable-features=NetworkServiceInProcess"); //to avoid timeout errors
                DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
                desiredCapabilities.setCapability(ChromeOptions.CAPABILITY,option);
                option.merge(desiredCapabilities);
                driver = new ChromeDriver(option);
                //get all chrome options which are set above
                String allOptions = String.valueOf(option);
                System.setProperty("allOptions",allOptions);
                break;
            case "firefox":
                driver = new FirefoxDriver();
                break;
            case "ie":
                driver = new InternetExplorerDriver();
                break;
            default:
                driver = new ChromeDriver();
                break;
        }
        driver.manage().window().maximize();
        return driver;
    }
}
