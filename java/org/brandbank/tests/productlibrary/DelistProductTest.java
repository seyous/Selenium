package org.brandbank.tests.productlibrary;

import com.aventstack.extentreports.Status;
import org.brandbank.libs.CSVDataManager;
import org.brandbank.libs.PropertyManager;
import org.brandbank.pages.LoginPage;
import org.brandbank.pages.productlibrary.PLHomePage;
import org.brandbank.pages.productlibrary.ProductDetailPage;
import org.brandbank.pages.productlibrary.SearchResultsPage;
import org.brandbank.tests.TestBaseSetup;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class DelistProductTest extends TestBaseSetup {
    LoginPage loginPage;
    PropertyManager propertyManager;
    CSVDataManager csvDataManager;
    PLHomePage PLHomePage;
    SearchResultsPage searchResultsPage;
    ProductDetailPage productDetailPage;
    public DelistProductTest(){
        loginPage = new LoginPage(driver);
        propertyManager = new PropertyManager();
        csvDataManager=new CSVDataManager();
        PLHomePage =new PLHomePage(driver);
        searchResultsPage =new SearchResultsPage(driver);
        productDetailPage=new ProductDetailPage(driver);
    }

    @Test
    @Parameters("testSuite")
    public  void delistProduct(String testSuite) throws Exception{
        try {
            String EAN = csvDataManager.getCSVData(testSuite, 1)[1];
            String SubCode = csvDataManager.getCSVData(testSuite, 1)[0];
            PLHomePage.searchEAN(EAN);
            searchResultsPage.selectPLTab("Live");
            boolean actualGtin = searchResultsPage.verifyEAN(EAN);
            Assert.assertEquals(actualGtin, true);
            System.out.println(EAN + " found in search results");
            extentTest.log(Status.INFO,EAN + " found in search results");
            String actualSubCode = searchResultsPage.verifySubscriber(SubCode);
            Assert.assertEquals(actualSubCode, SubCode);
            searchResultsPage.productToClick(SubCode);
            System.out.println("Clicked on product " + EAN + " to open Product Details page");
            extentTest.log(Status.INFO,"Clicked on product " + EAN + " to open Product Details page");
            String actualEAN = productDetailPage.verifyEAN(EAN);
            Assert.assertEquals(actualEAN, EAN);
            String actualSubscriber = productDetailPage.verifySubscriber(SubCode);
            Assert.assertEquals(actualSubscriber, SubCode);
            productDetailPage.productActions("delist","Test",null);
            System.out.println(EAN + " delisted");
            extentTest.log(Status.INFO,EAN + " delisted");
            PLHomePage.searchEAN(EAN);
            searchResultsPage.selectPLTab("Outstandingdelist");
            boolean actualLiveGtin = searchResultsPage.verifyEAN(EAN);
            Assert.assertEquals(actualLiveGtin, true);
            String actualLiveSubCode = searchResultsPage.verifySubscriber(SubCode);
            Assert.assertEquals(actualLiveSubCode, SubCode);
            System.out.println(EAN + " moved to Outstanding delisted status");
            extentTest.log(Status.PASS,EAN + " moved to Outstanding delisted status");
            productDetailPage.logOut();
        }
        catch (Exception e){
            e.printStackTrace();
            throw  e;
        }
    }

    @AfterMethod
    public void afterMethod(ITestResult result)
    {
        if(!result.isSuccess())
            System.setProperty("TestStatus","false");
    }
}
