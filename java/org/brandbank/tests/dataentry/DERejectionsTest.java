package org.brandbank.tests.dataentry;

import com.aventstack.extentreports.Status;
import org.brandbank.libs.CSVDataManager;
import org.brandbank.libs.PropertyManager;
import org.brandbank.pages.LoginPage;
import org.brandbank.pages.apps.AppsLoginPage;
import org.brandbank.pages.apps.TasklistSearchAndAssignPage;
import org.brandbank.pages.dataentry.*;
import org.brandbank.tests.TestBaseSetup;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class DERejectionsTest extends TestBaseSetup {
    LoginPage loginPage;
    AppsLoginPage appsLoginPage;
    DETasklistSearchAndAssignPage deTasklistSearchAndAssignPage;
    DEClassificationPage deClassificationPage;
    TasklistSearchAndAssignPage tasklistSearchAndAssignPage;
    CSVDataManager csvDataManager;
    DEProductCapturePage deProductCapturePage;
    DERejectionsPage deRejectionsPage;
    public DERejectionsTest(){
        loginPage = new LoginPage(driver);
        csvDataManager = new CSVDataManager();
        appsLoginPage = new AppsLoginPage(driver);
        deTasklistSearchAndAssignPage = new DETasklistSearchAndAssignPage(driver);
        deClassificationPage =new DEClassificationPage(driver);
        tasklistSearchAndAssignPage = new TasklistSearchAndAssignPage(driver);
        deProductCapturePage = new DEProductCapturePage(driver);
        deRejectionsPage = new DERejectionsPage(driver);
    }

    @Test
    @Parameters({"testSuite","taskListName","fieldToReject"})
    public void deRejections(String testSuite,String taskListName,String fieldToReject) throws Exception {
        try {
            String batchNumber = csvDataManager.getCSVData(testSuite, 1)[4];
            String EAN = csvDataManager.getCSVData(testSuite, 1)[1];
            String fieldToBeRejected = fieldToReject;
            tasklistSearchAndAssignPage.selectTasklist(taskListName);
            deTasklistSearchAndAssignPage.searchBatchNum(taskListName, batchNumber, EAN);
            deRejectionsPage.assignEnglish();
            tasklistSearchAndAssignPage.clickOnAssignedTask(taskListName);
            //verify activity page header
            String actualActivityPageHeader = deRejectionsPage.verifyActivityPageHeader();
            Assert.assertEquals(actualActivityPageHeader, taskListName);
            boolean languageSelected = deProductCapturePage.verifyLanguageInDD(EAN, "English (British)");
            Assert.assertEquals(languageSelected, true);
            System.out.println("English language selected in drop down");
            extentTest.log(Status.INFO,"English language selected in drop down");
            deRejectionsPage.rejectVariant();
            System.out.println(fieldToBeRejected+" rejected");
            extentTest.log(Status.PASS,fieldToBeRejected+" rejected");
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
            boolean languageSelected = deProductCapturePage.verifyLanguageInDD(EAN, "English (British)");
            Assert.assertEquals(languageSelected, true);
            System.out.println("English language selected in drop down");
            extentTest.log(Status.INFO,"English language selected in drop down");
            deRejectionsPage.clickActivityComplete();
            System.out.println("Rejected Segment approved from DE successfully");
            extentTest.log(Status.PASS,"Rejected Segment approved from DE successfully");
        }
        catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    @Parameters({"testSuite","taskListName"})
    public void sendToDEComplete(String testSuite,String taskListName) throws Exception {
        try{
            String batchNumber = csvDataManager.getCSVData(testSuite, 1)[4];
            String EAN = csvDataManager.getCSVData(testSuite, 1)[1];
            tasklistSearchAndAssignPage.selectTasklist(taskListName);
            deTasklistSearchAndAssignPage.searchBatchNum(taskListName, batchNumber, EAN);
            deRejectionsPage.sendToCompleteFromRHS();
            System.out.println("Product send to DE Complete");
            extentTest.log(Status.PASS,"Product send to DE Complete");
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
