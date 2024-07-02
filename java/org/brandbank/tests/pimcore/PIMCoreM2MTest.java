package org.brandbank.tests.pimcore;

import com.aventstack.extentreports.Status;
import org.brandbank.libs.CSVDataManager;
import org.brandbank.libs.EANGenerator;
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

public class PIMCoreM2MTest extends TestBaseSetup {
    LoginPage loginPage;
    WhiteboxPage whiteboxPage;
    AppsLoginPage appsLoginPage;
    CSVDataManager csvDataManager;
    FileHandling fileHandling;
    TasklistSearchAndAssignPage tasklistSearchAndAssignPage;
    Properties configProperties;

    public PIMCoreM2MTest(){
        loginPage = new LoginPage(driver);
        whiteboxPage = new WhiteboxPage(driver);
        appsLoginPage = new AppsLoginPage(driver);
        csvDataManager = new CSVDataManager();
        fileHandling = new FileHandling();
        tasklistSearchAndAssignPage = new TasklistSearchAndAssignPage(driver);
    }

    @BeforeTest
    @Parameters({"test"})
    private String getCredentials(String test) {
        configProperties = PropertyManager.getPropertiesData();
        String credentials = configProperties.getProperty("environment")+"."+test+".creds";
        return configProperties.getProperty(credentials);
    }

    @Test
    @Parameters({"testSuite","test","numOfProducts","subCode"})
    public void pimcoreM2MNewImport(String testSuite,String test,int numOfProducts, String subCode) throws Exception {
        try {
            String sourceFilePath = System.getProperty("user.dir") + "\\src\\test\\resources\\testdata\\PIMCore\\source\\Singleproduct_Stg\\Singleproduct_Stg.xml";
            String targetFilePath = System.getProperty("user.dir") + "\\src\\test\\resources\\testdata\\PIMCore\\target\\Singleproduct_Stg\\Singleproduct_Stg.xml";
            String outputZipPath = System.getProperty("user.dir") + "\\src\\test\\resources\\testdata\\PIMCore\\output\\pimcorem2m.zip";
            String targetFolder = System.getProperty("user.dir") + "\\src\\test\\resources\\testdata\\PIMCore\\target\\Singleproduct_Stg\\";

            EANGenerator.generateEAN(testSuite, numOfProducts, subCode);
            String oldEAN = "5721084002012";
            String newEAN = csvDataManager.getCSVData(testSuite, 1)[1];

            fileHandling.modifyFile(sourceFilePath, targetFilePath, oldEAN, newEAN);
            switch (subCode) {
                case "OIMA005":
                    fileHandling.modifyFile(targetFilePath, targetFilePath, "OIMA005", subCode);
                    fileHandling.modifyFile(targetFilePath, targetFilePath, "a074J00000CW0heQAD", "a074J00000CW0heQAD");
                    break;
                case "ACOM063":
                    fileHandling.modifyFile(targetFilePath, targetFilePath, "OIMA005", subCode);
                    fileHandling.modifyFile(targetFilePath, targetFilePath, "a074J00000CW0heQAD", "a0726000008cuW5AAI");
                    break;
            }
            System.out.println("File edited, now zipping");
            extentTest.log(Status.PASS,"File edited, now zipping");
            fileHandling.zipDirectory(outputZipPath, targetFolder);
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
