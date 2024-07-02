package org.brandbank.tests.salesforce;

import org.brandbank.libs.PropertyManager;
import org.brandbank.pages.LoginPage;
import org.brandbank.pages.salesforce.OpportunityPage;
import org.brandbank.pages.suppliedcontent.SuppliedContentHomePage;
import org.brandbank.pages.suppliedcontent.UploadFilesPage;
import org.brandbank.tests.TestBaseSetup;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class FulfillmentTest extends TestBaseSetup {
    LoginPage loginPage;
    PropertyManager propertyManager;
    OpportunityPage opportunityPage;

    public FulfillmentTest() {
        loginPage = new LoginPage(driver);
        propertyManager = new PropertyManager();
        opportunityPage = new OpportunityPage(driver);
    }

    @Test
    @Parameters({"testSuite","subCode"})
    public void fulfillmentDetails(String testSuite, String subCode) throws Exception {
        try {
            boolean statusCheck = opportunityPage.validateFulfillment(testSuite, subCode);
            Assert.assertEquals(statusCheck, true);
            opportunityPage.logout();
            Thread.sleep(8000);
        } catch (Exception e) {
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
