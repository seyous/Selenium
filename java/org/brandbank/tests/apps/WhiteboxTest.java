package org.brandbank.tests.apps;

import com.aventstack.extentreports.Status;
import org.brandbank.libs.*;
import org.brandbank.pages.LoginPage;
import org.brandbank.pages.apps.AppsLoginPage;
import org.brandbank.pages.apps.TasklistSearchAndAssignPage;
import org.brandbank.pages.apps.WhiteboxPage;
import org.brandbank.tests.TestBaseSetup;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.util.Properties;

public class WhiteboxTest extends TestBaseSetup {
    LoginPage loginPage;
    WhiteboxPage whiteboxPage;
    AppsLoginPage appsLoginPage;
    CSVDataManager csvDataManager;
    FileHandling fileHandling;
    TasklistSearchAndAssignPage tasklistSearchAndAssignPage;
    Properties configProperties;
    BrowserActions browserActions;

    public WhiteboxTest() {
        loginPage = new LoginPage(driver);
        whiteboxPage = new WhiteboxPage(driver);
        appsLoginPage = new AppsLoginPage(driver);
        csvDataManager = new CSVDataManager();
        fileHandling = new FileHandling();
        tasklistSearchAndAssignPage = new TasklistSearchAndAssignPage(driver);
        browserActions=new BrowserActions(driver);
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
    public void whiteboxUpload(String testSuite,String test,int numOfProducts, String subCode) throws Exception{
        try {
            String sourceFilePath = System.getProperty("user.dir") + "\\src\\test\\resources\\testdata\\Whitebox\\source\\IntegrationM2MTestProduct.xml";
            String targetFilePath = System.getProperty("user.dir") + "\\src\\test\\resources\\testdata\\Whitebox\\target\\IntegrationM2MTestProduct.xml";
            String outputZipPath = System.getProperty("user.dir") + "\\src\\test\\resources\\testdata\\Whitebox\\output\\m2m.zip";
            String targetFolder = System.getProperty("user.dir") + "\\src\\test\\resources\\testdata\\Whitebox\\target\\";
            String evolveSourceFilePath = System.getProperty("user.dir") + "\\src\\test\\resources\\testdata\\Whitebox\\evolveSource\\EvolveIntegrationM2MTestProduct.xml";

            if(testSuite.equals("InferredAttributes")){
                //use predefined ean
                System.out.println("using pre-defined ean");
            }
            else {
                EANGenerator.generateEAN(testSuite, numOfProducts, subCode);

            }
            String oldEAN = "5704014348015";


            for (int i = 1; i <= numOfProducts; i++){
                String newEAN = csvDataManager.getCSVData(testSuite, i)[1];
                switch (subCode) {
                    case "OCOM900":
                        fileHandling.modifyFile(sourceFilePath, targetFilePath, oldEAN, newEAN);
                        fileHandling.modifyFile(targetFilePath, targetFilePath, "OCOM899", subCode);
                        fileHandling.modifyFile(targetFilePath, targetFilePath, "99836", "101594");
                        break;
                    case "OCOM898":
                        fileHandling.modifyFile(sourceFilePath, targetFilePath, oldEAN, newEAN);
                        fileHandling.modifyFile(targetFilePath, targetFilePath, "OCOM899", subCode);
                        fileHandling.modifyFile(targetFilePath, targetFilePath, "99836", "99835");
                        break;
                    case "AEVO027":
                        fileHandling.modifyFile(evolveSourceFilePath, targetFilePath, oldEAN, newEAN);
                        fileHandling.modifyFile(targetFilePath, targetFilePath, "AEVO027", subCode);
                        fileHandling.modifyFile(targetFilePath, targetFilePath, "99836", "106544");
                        break;
                    default:
                        fileHandling.modifyFile(sourceFilePath, targetFilePath, oldEAN, newEAN);
                        fileHandling.modifyFile(targetFilePath, targetFilePath, "OCOM899", "OCOM899");
                        fileHandling.modifyFile(targetFilePath, targetFilePath, "99836", "99836");
                        break;
                }
                System.out.println("File edited, now zipping");
                extentTest.log(Status.INFO,"File edited, now zipping");
                fileHandling.zipDirectory(outputZipPath, targetFolder);
                System.out.println("Successfully created zipfile");
                extentTest.log(Status.PASS,"Successfully created zipfile");

                String credentials = getCredentials(test);

                tasklistSearchAndAssignPage.selectTasklist("Whitebox Package");
                boolean uploadStatus = whiteboxPage.whiteboxUpload(credentials, newEAN, outputZipPath);
                Assert.assertEquals(uploadStatus, true);
                System.out.println("Whitebox upload was run successfully");
                extentTest.log(Status.PASS,"Whitebox upload was run successfully");
                browserActions.refreshPage();
                Thread.sleep(20000);
            }
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
