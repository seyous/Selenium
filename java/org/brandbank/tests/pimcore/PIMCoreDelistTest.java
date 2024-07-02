package org.brandbank.tests.pimcore;

import com.aventstack.extentreports.Status;
import org.brandbank.libs.CSVDataManager;
import org.brandbank.libs.FileHandling;
import org.brandbank.libs.PropertyManager;
import org.brandbank.pages.LoginPage;
import org.brandbank.pages.apps.AppsLoginPage;
import org.brandbank.pages.apps.TasklistSearchAndAssignPage;
import org.brandbank.pages.apps.WhiteboxPage;
import org.brandbank.tests.TestBaseSetup;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.util.Properties;

public class PIMCoreDelistTest extends TestBaseSetup {
    LoginPage loginPage;
    AppsLoginPage appsLoginPage;
    WhiteboxPage whiteboxPage;
    TasklistSearchAndAssignPage tasklistSearchAndAssignPage;
    CSVDataManager csvDataManager;
    FileHandling fileHandling;
    Properties configProperties;
    public PIMCoreDelistTest(){
        loginPage = new LoginPage(driver);
        csvDataManager = new CSVDataManager();
        fileHandling = new FileHandling();
        tasklistSearchAndAssignPage = new TasklistSearchAndAssignPage(driver);
        whiteboxPage = new WhiteboxPage(driver);
        appsLoginPage = new AppsLoginPage(driver);
    }

    @BeforeTest
    @Parameters({"test"})
    private String getCredentials(String test) {
        configProperties = PropertyManager.getPropertiesData();
        String credentials = configProperties.getProperty("environment")+"."+test+".creds";
        return configProperties.getProperty(credentials);
    }

    @Test
    public void editDelistXml() throws Exception {
        try {
            String outputZipPath = System.getProperty("user.dir") + "\\src\\test\\resources\\testdata\\PIMCore\\output\\pimcorem2m.zip";
            String sourceFolder = System.getProperty("user.dir") + "\\src\\test\\resources\\testdata\\PIMCore\\source\\Delistproduct_Stg\\";
            String downloadedFile = System.getProperty("user.dir") + "/downloads/DataImportProcessor Log.xml";

            boolean xmlDownloaded = fileHandling.verifyFileExists(downloadedFile);
            Assert.assertEquals(xmlDownloaded, true);
            System.out.println("Successfully downloaded DataImportProcessor Log.xml file from PIMCore extract");
            extentTest.log(Status.INFO,"Successfully downloaded DataImportProcessor Log.xml file from PIMCore extract");

            boolean contentVerified = fileHandling.verifyFileContent("Live",downloadedFile);
            Assert.assertEquals(contentVerified, true);
            System.out.println("Product is in Live status in PL");
            extentTest.log(Status.INFO,"Product is in Live status in PL");

            fileHandling.editDelistXml();
            System.out.println("File edited, now zipping");
            extentTest.log(Status.PASS,"File edited, now zipping");
            fileHandling.zipDirectory(outputZipPath, sourceFolder);
            System.out.println("Successfully created zipfile");
            extentTest.log(Status.PASS,"Successfully created zipfile");
        }
        catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    @Parameters({"testSuite","test"})
    public void pimcoreM2MDelistImport(String testSuite,String test) throws Exception {
        try {
            String outputZipPath = System.getProperty("user.dir") + "\\src\\test\\resources\\testdata\\PIMCore\\output\\pimcorem2m.zip";
            String sourceFolder = System.getProperty("user.dir") + "\\src\\test\\resources\\testdata\\PIMCore\\source\\Delistproduct_Stg\\";
            String downloadedFile = System.getProperty("user.dir") + "\\downloads\\DataImportProcessor Log.xml";
            String newEAN = csvDataManager.getCSVData(testSuite, 1)[1];

            System.out.println("Now zipping");
            extentTest.log(Status.INFO,"Now zipping");
            fileHandling.zipDirectory(outputZipPath, sourceFolder);
            System.out.println("Successfully created zipfile");
            extentTest.log(Status.PASS,"Successfully created zipfile");

            String credentials = getCredentials(test);

            tasklistSearchAndAssignPage.selectTasklist("Whitebox Package");
            boolean uploadStatus = whiteboxPage.whiteboxUpload(credentials, newEAN, outputZipPath);
            Assert.assertEquals(uploadStatus, true);
            System.out.println("Whitebox upload was run successfully");
            extentTest.log(Status.PASS,"Whitebox upload was run successfully");
            appsLoginPage.logOut();
        }
        catch (Exception e){
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
