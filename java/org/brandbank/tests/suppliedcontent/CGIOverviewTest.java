package org.brandbank.tests.suppliedcontent;

import com.aventstack.extentreports.Status;
import com.opencsv.exceptions.CsvException;
import org.brandbank.libs.BrowserActions;
import org.brandbank.libs.CSVDataManager;
import org.brandbank.libs.PropertyManager;
import org.brandbank.pages.LoginPage;
import org.brandbank.pages.salesforce.OpportunityPage;
import org.brandbank.pages.suppliedcontent.CGIOverviewPage;
import org.brandbank.pages.suppliedcontent.SuppliedContentHomePage;
import org.brandbank.tests.TestBaseSetup;
import org.brandbank.tests.libs.RetryTests;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.IOException;
import java.util.Properties;

public class CGIOverviewTest extends TestBaseSetup {
    LoginPage loginPage;
    PropertyManager propertyManager;
    CGIOverviewPage CGIOverviewPage;
    CSVDataManager csvDataManager;
    SuppliedContentHomePage SuppliedContentHomePage;
    BrowserActions browserActions;

    public CGIOverviewTest() {
        loginPage = new LoginPage(driver);
        propertyManager = new PropertyManager();
        CGIOverviewPage = new CGIOverviewPage(driver);
        browserActions = new BrowserActions(this.driver);
        SuppliedContentHomePage = new SuppliedContentHomePage(driver);
        csvDataManager = new CSVDataManager();
    }

    @Test
    @Parameters({"testSuite", "subCode"})
    public void cgiOverview(String testSuite, String subCode) throws Exception {
        try {
            String ean = csvDataManager.getCSVData(testSuite, 1)[1];
            String orderNumber = csvDataManager.getCSVData(testSuite, 1)[3];
            String actualTitle = CGIOverviewPage.loadCGIOverviewApp();
            Assert.assertEquals(actualTitle, "CGI Automation Portal");
            CGIOverviewPage.searchOrder(orderNumber);
            String orderStatus = CGIOverviewPage.addModel(testSuite, ean, subCode);
            Assert.assertEquals(orderStatus, "ERROR: LABELS NOT ASSIGNED");
            System.out.println("Model Added successfully with details");
            extentTest.log(Status.PASS, "Model Added successfully with details");
            CGIOverviewPage.assignLabels(ean);
            CGIOverviewPage.addLabelVariant(ean);
            CGIOverviewPage.addMaterialVariants(testSuite, ean);
            CGIOverviewPage.addShotVariants(ean);
            Thread.sleep(3000);
            browserActions.close();
            browserActions.switchToParticularTab(0);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    @Parameters({"testSuite"})
    public void viewRenderImage(String testSuite) throws Exception {
        try {
            String ean = csvDataManager.getCSVData(testSuite, 1)[1];
            String orderNumber = csvDataManager.getCSVData(testSuite, 1)[3];
            String actualTitle = CGIOverviewPage.loadCGIOverviewApp();
            Assert.assertEquals(actualTitle, "CGI Automation Portal");
            CGIOverviewPage.searchOrder(orderNumber);
            boolean renderingSuccess = CGIOverviewPage.viewRender(ean);
            Assert.assertEquals(renderingSuccess, true);
            browserActions.close();
            browserActions.switchToParticularTab(0);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    @Parameters({"testSuite"})
    public void deleteAndLogoutSCP(String testSuite) throws Exception {
        try {
            String orderNumber = csvDataManager.getCSVData(testSuite, 1)[3];
            String actualTitle = CGIOverviewPage.loadCGIOverviewApp();
            Assert.assertEquals(actualTitle, "CGI Automation Portal");
            CGIOverviewPage.searchOrder(orderNumber);
            boolean deleteSuccess = CGIOverviewPage.deleteModel();
            Assert.assertEquals(deleteSuccess, true);
            browserActions.close();
            browserActions.switchToParticularTab(0);
            SuppliedContentHomePage.logout();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @AfterMethod
    public void afterMethod(ITestResult result) {
        if (!result.isSuccess())
            System.setProperty("TestStatus", "false");
    }
}
