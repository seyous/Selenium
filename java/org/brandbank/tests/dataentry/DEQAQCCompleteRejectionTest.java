package org.brandbank.tests.dataentry;

import com.aventstack.extentreports.Status;
import org.brandbank.libs.CSVDataManager;
import org.brandbank.libs.PropertyManager;
import org.brandbank.pages.LoginPage;
import org.brandbank.pages.apps.AppsLoginPage;
import org.brandbank.pages.apps.TasklistSearchAndAssignPage;
import org.brandbank.pages.dataentry.DEClassificationPage;
import org.brandbank.pages.dataentry.DEProductCapturePage;
import org.brandbank.pages.dataentry.DEQAQCCompletePage;
import org.brandbank.pages.dataentry.DETasklistSearchAndAssignPage;
import org.brandbank.tests.TestBaseSetup;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class DEQAQCCompleteRejectionTest extends TestBaseSetup {
    LoginPage loginPage;
    AppsLoginPage appsLoginPage;
    DETasklistSearchAndAssignPage deTasklistSearchAndAssignPage;
    DEClassificationPage deClassificationPage;
    TasklistSearchAndAssignPage tasklistSearchAndAssignPage;
    CSVDataManager csvDataManager;
    DEQAQCCompletePage DEQAQCCompletePage;
    DEProductCapturePage deProductCapturePage;
    public DEQAQCCompleteRejectionTest(){
        loginPage = new LoginPage(driver);
        csvDataManager = new CSVDataManager();
        appsLoginPage = new AppsLoginPage(driver);
        deTasklistSearchAndAssignPage = new DETasklistSearchAndAssignPage(driver);
        deClassificationPage =new DEClassificationPage(driver);
        tasklistSearchAndAssignPage = new TasklistSearchAndAssignPage(driver);
        DEQAQCCompletePage = new DEQAQCCompletePage(driver);
        deProductCapturePage = new DEProductCapturePage(driver);
    }

    @Test
    @Parameters({"testSuite","taskListName","fieldToReject"})
    public void deQAQCCompleteRejection(String testSuite,String taskListName,String fieldToReject) throws Exception {
        try {
            String batchNumber = csvDataManager.getCSVData(testSuite, 1)[4];
            String EAN = csvDataManager.getCSVData(testSuite, 1)[1];
            String fieldToBeRejected = fieldToReject;
            tasklistSearchAndAssignPage.selectTasklist(taskListName);
            deTasklistSearchAndAssignPage.searchBatchNum(taskListName, batchNumber, EAN);
            DEQAQCCompletePage.assignNeutral();
            tasklistSearchAndAssignPage.clickOnAssignedTask(taskListName);
            //verify activity page header
            String actualActivityPageHeader = DEQAQCCompletePage.verifyActivityPageHeader();
            Assert.assertEquals(actualActivityPageHeader, taskListName);
            boolean languageSelected = deProductCapturePage.verifyLanguageInDD(EAN, "Neutral");
            Assert.assertEquals(languageSelected, true);
            System.out.println("Neutral language selected in drop down");
            extentTest.log(Status.INFO,"Neutral language selected in drop down");
            switch (fieldToBeRejected) {
                case "Shelf Height" :
                    DEQAQCCompletePage.rejectDimensionHeight();
                    System.out.println("Shelf Height rejected");
                    extentTest.log(Status.PASS,"Shelf Height rejected");
                    break;
                case "Shelf Width" :
                    DEQAQCCompletePage.rejectDimensionWidth();
                    System.out.println("Shelf Width rejected");
                    extentTest.log(Status.PASS,"Shelf Width rejected");
                    break;
                case "Shelf Depth" :
                    DEQAQCCompletePage.rejectDimensionDepth();
                    System.out.println("Shelf Depth rejected");
                    extentTest.log(Status.PASS,"Shelf Depth rejected");
                    break;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    @Parameters({"testSuite"})
    public void deApproveRejected(String testSuite) throws Exception{
        try{
            String EAN = csvDataManager.getCSVData(testSuite, 1)[1];
            tasklistSearchAndAssignPage.clickOnAssignedTask("Data Entry Product Capture");
            //verify activity page header
            //String actualActivityPageHeader = deProductCapturePage.verifyActivityPageHeader();
            //Assert.assertEquals(actualActivityPageHeader, "Data Entry Product Capture");
            boolean languageSelected = deProductCapturePage.verifyLanguageInDD(EAN, "Neutral");
            Assert.assertEquals(languageSelected, true);
            System.out.println("Neutral language selected in drop down");
            extentTest.log(Status.INFO,"Neutral language selected in drop down");
            DEQAQCCompletePage.clickActivityComplete();
            System.out.println("Rejected Segment approved from DE successfully");
            extentTest.log(Status.PASS,"Rejected Segment approved from DE successfully");
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
