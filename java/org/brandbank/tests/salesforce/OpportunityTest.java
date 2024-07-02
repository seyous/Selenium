package org.brandbank.tests.salesforce;

import com.aventstack.extentreports.Status;
import org.brandbank.libs.EANGenerator;
import org.brandbank.libs.PropertyManager;
import org.brandbank.pages.LoginPage;
import org.brandbank.pages.salesforce.OpportunityPage;
import org.brandbank.pages.salesforce.SalesforceLoginPage;
import org.brandbank.pages.salesforce.SubscriptionPage;
import org.brandbank.tests.TestBaseSetup;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.Properties;

public class OpportunityTest extends TestBaseSetup {
    SalesforceLoginPage salesforceLoginPage;
    LoginPage loginPage;
    SubscriptionPage subscriptionPage;
    PropertyManager propertyManager;
    OpportunityPage opportunityPage;
    Properties configProperties;

    public OpportunityTest() {
        loginPage = new LoginPage(driver);
        salesforceLoginPage = new SalesforceLoginPage(driver);
        subscriptionPage = new SubscriptionPage(driver);
        opportunityPage = new OpportunityPage(driver);
        propertyManager = new PropertyManager();
    }

    @Test
    @Parameters({"testSuite", "numOfProducts", "subCode", "productType"})
    public void createOpportunity(String testSuite, int numOfProducts, String subCode, String productType) throws Exception {
        try {
            EANGenerator.generateEAN(testSuite, numOfProducts, subCode);
            String csvFilePath = System.getProperty("user.dir") + "/src/test/resources/testdata/SuppliedContent/ServiceTypes.csv";
            subscriptionPage.searchSubscription(testSuite, productType);
            subscriptionPage.createOpportunity(testSuite, productType);
            if (productType.equalsIgnoreCase("Virtual Product"))
                opportunityPage.virtual_addOrderLines(testSuite, numOfProducts,csvFilePath);
            else
                opportunityPage.addOrderLines(testSuite, numOfProducts);
            boolean verifyProductCode = opportunityPage.validateServiceTypeInOrderLines(numOfProducts, testSuite, csvFilePath, productType);
            Assert.assertEquals(verifyProductCode, true);
            boolean quoteCreated = opportunityPage.createQuote(testSuite, numOfProducts, "Awaiting PO",productType);
            Assert.assertEquals(quoteCreated, true);
                opportunityPage.createPurchaseOrder();
            opportunityPage.logout();
            Thread.sleep(8000);
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
