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

public class MultiApprovalsTest extends TestBaseSetup {
    LoginPage loginPage;
    PropertyManager propertyManager;
    CSVDataManager csvDataManager;
    PLHomePage PLHomePage;
    SearchResultsPage searchResultsPage;
    ProductDetailPage productDetailPage;

    public MultiApprovalsTest(){
        loginPage = new LoginPage(driver);
        propertyManager = new PropertyManager();
        csvDataManager=new CSVDataManager();
        PLHomePage =new PLHomePage(driver);
        searchResultsPage =new SearchResultsPage(driver);
        productDetailPage=new ProductDetailPage(driver);
    }

    @Test()
    @Parameters({"testSuite", "numOfProducts", "approvalType"})
    public void approveMulti(String testSuite, int numOfProducts, String approvalType)throws Exception {
        try {
            for (int i = 1; i <= numOfProducts; i++) {
                String EAN = csvDataManager.getCSVData(testSuite, i)[1];
                String subCode = csvDataManager.getCSVData(testSuite, i)[0];
                PLHomePage.searchEAN(EAN);
                searchResultsPage.selectPLTab("Awaitingapproval");
                boolean actualGtin = searchResultsPage.verifyEAN(EAN);
                Assert.assertEquals(actualGtin, true);
                System.out.println(EAN + " found in search results");
                extentTest.log(Status.INFO,EAN + " found in search results");
                String actualSubCode = searchResultsPage.verifySubscriber(subCode);
                Assert.assertEquals(actualSubCode, subCode);
                searchResultsPage.productToClick(subCode);
                System.out.println("Clicked on product " + EAN + " to open Product Details page");
                extentTest.log(Status.INFO,"Clicked on product " + EAN + " to open Product Details page");
                String actualEAN = productDetailPage.verifyEAN(EAN);
                Assert.assertEquals(actualEAN, EAN);
                String actualSubscriber = productDetailPage.verifySubscriber(subCode);
                Assert.assertEquals(actualSubscriber, subCode);
                productDetailPage.productActions("approve", "Testing", "Does NOT comply with EU1169/2011");
                System.out.println(EAN + " " + approvalType +" approved");
                extentTest.log(Status.INFO,EAN + " " + approvalType +" approved");
                if (approvalType.equals("Regulatory")) {
                    PLHomePage.searchEAN(EAN);
                    searchResultsPage.selectPLTab("Live");
                   boolean actualLiveGtin = searchResultsPage.verifyEAN(EAN);
                    Assert.assertEquals(actualLiveGtin, true);
                    String actualLiveSubCode = searchResultsPage.verifySubscriber(subCode);
                    Assert.assertEquals(actualLiveSubCode, subCode);
                    System.out.println(EAN + " moved to Live status");
                    extentTest.log(Status.PASS,EAN + " moved to Live status");
                } else {
                    PLHomePage.searchEAN(EAN);
                    searchResultsPage.selectPLTab("Awaitingapproval");
                    boolean actualLiveGtin = searchResultsPage.verifyEAN(EAN);
                    Assert.assertEquals(actualLiveGtin, true);
                    String actualLiveSubCode = searchResultsPage.verifySubscriber(subCode);
                    Assert.assertEquals(actualLiveSubCode, subCode);
                    System.out.println(EAN + " awaiting approval");
                    extentTest.log(Status.INFO,EAN + " awaiting approval");
                }
                }
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
