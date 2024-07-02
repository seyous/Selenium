package org.brandbank.tests.dataentry;

import com.aventstack.extentreports.Status;
import org.brandbank.libs.CSVDataManager;
import org.brandbank.libs.PropertyManager;
import org.brandbank.pages.LoginPage;
import org.brandbank.pages.apps.AppsLoginPage;
import org.brandbank.pages.apps.TasklistSearchAndAssignPage;
import org.brandbank.pages.dataentry.DEClassificationPage;
import org.brandbank.pages.dataentry.DEProductCapturePage;
import org.brandbank.pages.dataentry.DETasklistSearchAndAssignPage;
import org.brandbank.tests.TestBaseSetup;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class DEProductCaptureTest extends TestBaseSetup {
    LoginPage loginPage;
    AppsLoginPage appsLoginPage;
    DETasklistSearchAndAssignPage deTasklistSearchAndAssignPage;
    DEClassificationPage deClassificationPage;
    TasklistSearchAndAssignPage tasklistSearchAndAssignPage;
    CSVDataManager csvDataManager;
    DEProductCapturePage deProductCapturePage;
    public DEProductCaptureTest(){
        loginPage = new LoginPage(driver);
        csvDataManager = new CSVDataManager();
        appsLoginPage = new AppsLoginPage(driver);
        deTasklistSearchAndAssignPage = new DETasklistSearchAndAssignPage(driver);
        deClassificationPage =new DEClassificationPage(driver);
        tasklistSearchAndAssignPage = new TasklistSearchAndAssignPage(driver);
        deProductCapturePage = new DEProductCapturePage(driver);
    }

    @Test
    @Parameters({"testSuite"})
    public void DEClassification(String testSuite) throws Exception{
        try {
                String tasklistName = "Data Entry Classification";
                String batchNumber = csvDataManager.getCSVData(testSuite, 1)[4];
                String EAN = csvDataManager.getCSVData(testSuite, 1)[1];
                tasklistSearchAndAssignPage.selectTasklist(tasklistName);
                deTasklistSearchAndAssignPage.searchBatchNum(tasklistName,batchNumber,EAN);
                deTasklistSearchAndAssignPage.clickOnAssignToMe();
                tasklistSearchAndAssignPage.clickOnAssignedTask(tasklistName);
                //verify activity page header
                String actualActivityPageHeader = deClassificationPage.verifyActivityPageHeader();
                Assert.assertEquals(actualActivityPageHeader, "Copy data from another Product Version");
                Thread.sleep(10000);
                deClassificationPage.selectTemplate("IT Test Automation - Do Not Use");
                System.out.println("Template selected");
                extentTest.log(Status.INFO,"Template selected");
                deClassificationPage.selectCategories();
                System.out.println("Categories selected");
                extentTest.log(Status.INFO,"Categories selected");
                deClassificationPage.selectEnglishAndMakePrimary();
                System.out.println("English Language to Capture selected and made as Primary");
                extentTest.log(Status.INFO,"English Language to Capture selected and made as Primary");
                deClassificationPage.addParts();
                System.out.println("Parts Added");
                extentTest.log(Status.INFO,"Parts Added");
                deClassificationPage.clickActivityComplete();
                Thread.sleep(5000);
                System.out.println("DE Classification Task completed successfully");
                extentTest.log(Status.PASS,"DE Classification Task completed successfully");
        }
        catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    @Parameters({"testSuite"})
    public void attachObject(String testSuite) throws Exception{
        try{
            String batchNumber = csvDataManager.getCSVData(testSuite, 1)[4];
            String EAN = csvDataManager.getCSVData(testSuite, 1)[1];
            String objectPath = System.getProperty("user.dir") + "/src/test/resources/testdata/DataEntry/DEAttachObject.docx";
            tasklistSearchAndAssignPage.selectTasklist("Data Entry");
            deTasklistSearchAndAssignPage.searchBatchNum("Data Entry",batchNumber,EAN);
            deTasklistSearchAndAssignPage.attachObject(objectPath);
            System.out.println("Object attached successfully");
            extentTest.log(Status.PASS,"Object attached successfully");
        }
        catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    @Parameters({"testSuite"})
    public void captureNeutralLanguage(String testSuite) throws Exception{
        try {
            String batchNumber = csvDataManager.getCSVData(testSuite, 1)[4];
            String EAN = csvDataManager.getCSVData(testSuite, 1)[1];
            String desc = csvDataManager.getCSVData(testSuite, 1)[2];
            tasklistSearchAndAssignPage.selectTasklist("Data Entry");
            deTasklistSearchAndAssignPage.searchBatchNum("Data Entry",batchNumber,EAN);
            deTasklistSearchAndAssignPage.clickOnAssignToMe();
            tasklistSearchAndAssignPage.clickOnAssignedTask("Data Entry Product Capture");
            //verify activity page header
            //String actualActivityPageHeader = deProductCapturePage.verifyActivityPageHeader();
           // Assert.assertEquals(actualActivityPageHeader, "Data Entry Product Capture");
            deProductCapturePage.verifyLanguageInDD(EAN,"Neutral");
            System.out.println("Neutral language selected from drop down");
            extentTest.log(Status.INFO,"Neutral language selected from drop down");
            boolean verifyProductComplete=deProductCapturePage.addDataInNeutral(testSuite);
            Assert.assertEquals(verifyProductComplete,true);
            System.out.println("Data added to Neutral Language successfully");
            extentTest.log(Status.PASS,"Data added to Neutral Language successfully");
        }
        catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    @Parameters({"testSuite"})
    public void captureEnglishLanguage(String testSuite) throws Exception {
        try{
            String EAN = csvDataManager.getCSVData(testSuite, 1)[1];
            String desc = csvDataManager.getCSVData(testSuite, 1)[2];
            tasklistSearchAndAssignPage.clickOnAssignedTask("Data Entry Product Capture");
            //verify activity page header
            //String actualActivityPageHeader = deProductCapturePage.verifyActivityPageHeader();
            //Assert.assertEquals(actualActivityPageHeader, "Data Entry Product Capture");
            deProductCapturePage.selectLanguageFromDD(EAN,desc,"English (British)");
            System.out.println("English language selected from drop down");
            extentTest.log(Status.INFO,"English language selected from drop down");
            boolean verifyProductComplete=deProductCapturePage.addDataInEnglish(testSuite);
            Assert.assertEquals(verifyProductComplete,true);
            System.out.println("Data added to English Language successfully");
            extentTest.log(Status.PASS,"Data added to English Language successfully");
        }
        catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    @Parameters({"testSuite"})
    public void captureDanishLanguage(String testSuite) throws Exception{
        try {
            String EAN = csvDataManager.getCSVData(testSuite, 1)[1];
            String desc = csvDataManager.getCSVData(testSuite, 1)[2];
            tasklistSearchAndAssignPage.clickOnAssignedTask("Data Entry Product Capture");
            //verify activity page header
           // String actualActivityPageHeader = deProductCapturePage.verifyActivityPageHeader();
            //Assert.assertEquals(actualActivityPageHeader, "Data Entry Product Capture");
            boolean enabledDropdown = deProductCapturePage.enabledDEActivity();
            if(enabledDropdown) {
                deProductCapturePage.selectLanguageFromDD(EAN, desc, "Danish");
                System.out.println("Danish language selected in drop down");
                extentTest.log(Status.INFO, "Danish language selected in drop down");
            }
            else {
                String eanValidation=deProductCapturePage.eanValidation();
                Assert.assertEquals(eanValidation,EAN);
                boolean languageSelected = deProductCapturePage.verifyLanguageInDD(EAN, "Danish");
                Assert.assertEquals(languageSelected, true);
                System.out.println("Danish language selected in drop down");
                extentTest.log(Status.INFO, "Danish language selected in drop down");
            }
            boolean verifyProductComplete = deProductCapturePage.addDataInDanish(testSuite);
            Assert.assertEquals(verifyProductComplete, true);
            System.out.println("Data added to Danish Language successfully");
            extentTest.log(Status.PASS, "Data added to Danish Language successfully");
        }
        catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    public void logOut(){
        appsLoginPage.logOut();
    }

    @AfterMethod
    public void afterMethod(ITestResult result)
    {
        if(!result.isSuccess())
            System.setProperty("TestStatus","false");
    }
}
