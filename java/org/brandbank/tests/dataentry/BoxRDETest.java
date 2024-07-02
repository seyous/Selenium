package org.brandbank.tests.dataentry;

import com.aventstack.extentreports.Status;
import org.brandbank.libs.CSVDataManager;
import org.brandbank.pages.LoginPage;
import org.brandbank.pages.apps.AppsLoginPage;
import org.brandbank.pages.apps.BookingInPage;
import org.brandbank.pages.apps.SupersedeBatchPage;
import org.brandbank.pages.apps.TasklistSearchAndAssignPage;
import org.brandbank.pages.dataentry.*;
import org.brandbank.tests.TestBaseSetup;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class BoxRDETest extends TestBaseSetup {
    LoginPage loginPage;
    AppsLoginPage appsLoginPage;
    DEProductCapturePage deProductCapturePage;
    DEProductMeasurerValidationPage dEProductMeasurerValidationPage;
    CSVDataManager csvDataManager;
    TasklistSearchAndAssignPage tasklistSearchAndAssignPage;
    DETasklistSearchAndAssignPage deTasklistSearchAndAssignPage;
    DEClassificationPage deClassificationPage;
    BoxRDataValidationPage boxRDataValidationPage;
    BookingInPage bookingInPage;
    SupersedeBatchPage supersedeBatchPage;
    public BoxRDETest() {
        loginPage = new LoginPage(driver);
        appsLoginPage = new AppsLoginPage(driver);
        deProductCapturePage = new DEProductCapturePage(driver);
        dEProductMeasurerValidationPage = new DEProductMeasurerValidationPage(driver);
        csvDataManager = new CSVDataManager();
        tasklistSearchAndAssignPage = new TasklistSearchAndAssignPage(driver);
        bookingInPage = new BookingInPage(driver);
        deTasklistSearchAndAssignPage = new DETasklistSearchAndAssignPage(driver);
        deClassificationPage = new DEClassificationPage(driver);
        boxRDataValidationPage = new BoxRDataValidationPage(driver);
        supersedeBatchPage = new SupersedeBatchPage(driver);
    }

    @Test
    @Parameters({"testSuite"})
    public void dataEntryClassification(String testSuite) throws Exception {
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
            deClassificationPage.unselectDanish();
            deClassificationPage.selectDutchlanguage();
            deClassificationPage.clickActivityComplete();
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
    public void validateSegmentsInDE(String testSuite) throws Exception {
        try{
            String batchNumber = csvDataManager.getCSVData(testSuite, 1)[4];
            String EAN = csvDataManager.getCSVData(testSuite, 1)[1];
            String desc = csvDataManager.getCSVData(testSuite, 1)[2];
            tasklistSearchAndAssignPage.selectTasklist("Data Entry");
            deTasklistSearchAndAssignPage.searchBatchNum("Data Entry", batchNumber, EAN);
            deTasklistSearchAndAssignPage.clickOnAssignToMe();
            tasklistSearchAndAssignPage.clickOnAssignedTask("Data Entry Product Capture");
            //verify activity page header
            String actualActivityPageHeader = deProductCapturePage.verifyActivityPageHeader();
            Assert.assertEquals(actualActivityPageHeader, "Data Entry Product Capture");
            tasklistSearchAndAssignPage.clickOnAssignedTask("Data Entry Product Capture");
            deProductCapturePage.selectLanguageFromDDBoxR(EAN,"English (British)");
            System.out.println("English language selected from drop down");
            extentTest.log(Status.INFO,"English language selected from drop down");
            boxRDataValidationPage.dataValidationInDE(testSuite);
            Thread.sleep(3000);
            deProductCapturePage.selectLanguageFromDDBoxR(EAN,"Dutch");
            System.out.println("Dutch language selected from drop down");
            extentTest.log(Status.INFO,"Dutch language selected from drop down");
            boolean result=boxRDataValidationPage.dataValidationInDE(testSuite);
            Assert.assertEquals(result, true);
            deProductCapturePage.unAssignProductFromDE();
            appsLoginPage.logOut();
            extentTest.log(Status.INFO,"Logged out of Apps");
        }
        catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }
}
