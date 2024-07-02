package org.brandbank.tests.blackbox;

import com.aventstack.extentreports.Status;
import org.brandbank.libs.CSVDataManager;
import org.brandbank.libs.FileHandling;
import org.brandbank.libs.PropertyManager;
import org.brandbank.pages.LoginPage;
import org.brandbank.pages.blackbox.ExtractsPage;
import org.brandbank.tests.TestBaseSetup;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class DownloadXMLTest extends TestBaseSetup {
    LoginPage loginPage;
    CSVDataManager csvDataManager;
    FileHandling fileHandling;
    ExtractsPage extractsPage;
    public DownloadXMLTest(){
        loginPage = new LoginPage(driver);
        csvDataManager = new CSVDataManager();
        fileHandling = new FileHandling();
        extractsPage = new ExtractsPage(driver);
    }

    @Test
    public void downloadXML() throws Exception {
        try {
            String downloadedFile = System.getProperty("user.dir") + "/downloads/DataImportProcessor Log.xml";
            fileHandling.deleteFile(downloadedFile);
            extractsPage.searchExtract("PimCore M2M Dev");
            boolean successRowFound = extractsPage.downloadXml("PimCore M2M Dev");
            Assert.assertEquals(successRowFound,true);
            extentTest.log(Status.PASS,"Xml Downloaded successfully");
            extractsPage.logOut();
        }
        catch (Exception e) {
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
