package org.brandbank.tests.ordering;

import com.aventstack.extentreports.Status;
import com.opencsv.exceptions.CsvException;
import org.brandbank.libs.DateManager;
import org.brandbank.libs.EANGenerator;
import org.brandbank.pages.LoginPage;
import org.brandbank.pages.ordering.*;
import org.testng.Assert;
import org.brandbank.libs.CSVDataManager;
import org.brandbank.libs.PropertyManager;
import org.brandbank.tests.TestBaseSetup;
import org.testng.ITestResult;
import org.testng.annotations.*;
import java.io.IOException;

public class PlaceOrderTests extends TestBaseSetup {

    LoginPage loginPage;
    OrderProcessingPage orderProcessingPage;
    SearchResultPage searchResultPage;
    SubscriberDetailsPage subscriberDetailsPage;
    OrderHeaderPage orderHeaderPage;
    OrderConfirmPage orderConfirmPage;
    PurchaseOrderPage purchaseOrderPage;
    PropertyManager propertyManager;
    CSVDataManager csvDataManager;

    public PlaceOrderTests() {
        loginPage = new LoginPage(driver);
        orderProcessingPage = new OrderProcessingPage(driver);
        searchResultPage = new SearchResultPage(driver);
        subscriberDetailsPage =new SubscriberDetailsPage(driver);
        orderHeaderPage = new OrderHeaderPage(driver);
        orderConfirmPage = new OrderConfirmPage(driver);
        purchaseOrderPage = new PurchaseOrderPage(driver);
        csvDataManager = new CSVDataManager();
        propertyManager = new PropertyManager();
    }

    @Test
    @Parameters({"testSuite","numOfProducts","subCode"})
    public void placeOrder(String testSuite, int numOfProducts, String subCode) throws Exception {
        try {
            extentTest.log(Status.INFO,"Creating order");
            EANGenerator.generateEAN(testSuite, numOfProducts, subCode);
            extentTest.log(Status.INFO,"EAN generated");
            orderProcessingPage.clickOnSearchPlaceOrder();
            searchResultPage.enterSubcode(csvDataManager.getCSVData(testSuite, 1)[0]);
            searchResultPage.clickOnSearch();
            searchResultPage.clickOnSubscriberCode();
            subscriberDetailsPage.selectAccountPayable();
            subscriberDetailsPage.clickOnCreateOrder();
            String orderNumber = orderHeaderPage.getOrderNumber();
            CSVDataManager.updateCSV(testSuite, 1, numOfProducts, 3, orderNumber);
            orderHeaderPage.EnterDeliveryDate(new DateManager().getCurrentDate("dd MMM yyyy"));
            orderHeaderPage.selectProjectOption("TESTS");
            orderHeaderPage.clickOnAddOrderLines();
            orderHeaderPage.enterOrderLines(testSuite);
            extentTest.log(Status.INFO,"Order Lines entered");
            orderHeaderPage.clickOnValidateOrderLines();
            subscriberDetailsPage.clickOnCreateOrder();
            orderHeaderPage.clickOnPreviewOrder();
            orderConfirmPage.clickOnorderConfirm();
            extentTest.log(Status.PASS,"Order Created");
        }
        catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    @Parameters({"testSuite"})
    public void signOffOrder(String testSuite) throws Exception {
        try {
            //Getting order quote number
            String actualOrderId = orderProcessingPage.getQuoteOrderNumber(csvDataManager.getCSVData(testSuite, 1)[3]);
            // Validating order quote number
            Assert.assertEquals(actualOrderId, csvDataManager.getCSVData(testSuite, 1)[3]);
            extentTest.log(Status.INFO,"Order found to sign off");
            orderProcessingPage.clickOnQuoteOrderId(csvDataManager.getCSVData(testSuite, 1)[3]);
            purchaseOrderPage.enterPurchaseOrderNumber("Testing");
            purchaseOrderPage.clickOnSignOffOrder();
            extentTest.log(Status.PASS,"Order Signed off successfully");
            loginPage.logOut();
            extentTest.log(Status.INFO,"Logged out of Ordering");
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
