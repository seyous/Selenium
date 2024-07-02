package org.brandbank.tests.suppliedcontent;

import org.brandbank.libs.CSVDataManager;
import org.brandbank.libs.PropertyManager;
import org.brandbank.pages.LoginPage;
import org.brandbank.pages.suppliedcontent.ApproveProductPage;
import org.brandbank.pages.suppliedcontent.SuppliedContentHomePage;
import org.brandbank.tests.TestBaseSetup;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class ApproveDataTest extends TestBaseSetup {
    LoginPage loginPage;
    PropertyManager propertyManager;
    ApproveProductPage ApproveProductPage;
    SuppliedContentHomePage SuppliedContentHomePage;
    CSVDataManager csvDataManager;

    public ApproveDataTest() {
        loginPage = new LoginPage(driver);
        propertyManager = new PropertyManager();
        SuppliedContentHomePage = new SuppliedContentHomePage(driver);
        ApproveProductPage = new ApproveProductPage(driver);
        csvDataManager = new CSVDataManager();
    }

    @Test
    @Parameters({"testSuite", "numOfProducts", "approvalType"})
    public void approveUploadedFiles(String testSuite, int numOfProducts, String approvalType) throws Exception {
        try {
            System.out.println(approvalType);
            String orderNumber = csvDataManager.getCSVData(testSuite, 1)[3];
            SuppliedContentHomePage.searchOrder(orderNumber);
          boolean fileApproval = ApproveProductPage.approveProductData(testSuite, numOfProducts, approvalType);
            Assert.assertEquals(fileApproval,true);
            Thread.sleep(5000);
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
