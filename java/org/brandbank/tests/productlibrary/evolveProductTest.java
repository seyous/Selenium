package org.brandbank.tests.productlibrary;

import com.aventstack.extentreports.Status;
import org.brandbank.libs.CSVDataManager;
import org.brandbank.libs.PropertyManager;
import org.brandbank.pages.LoginPage;
import org.brandbank.pages.productlibrary.DataValidationPage;
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

public class evolveProductTest extends TestBaseSetup {

    LoginPage loginPage;
    PropertyManager propertyManager;
    CSVDataManager csvDataManager;
    PLHomePage PLHomePage;
    SearchResultsPage searchResultsPage;
    ProductDetailPage productDetailPage;
    DataValidationPage dataValidationPage;
    public evolveProductTest(){
        loginPage = new LoginPage(driver);
        propertyManager = new PropertyManager();
        csvDataManager=new CSVDataManager();
        PLHomePage =new PLHomePage(driver);
        searchResultsPage =new SearchResultsPage(driver);
        productDetailPage=new ProductDetailPage(driver);
        dataValidationPage=new DataValidationPage(driver);
    }

    @Test
    @Parameters({"testSuite", "numOfProducts","eanRow"})
    public void checkApproveRejectProductStatus(String testSuite,int numOfProducts,int eanRow)throws Exception {
        try {
                String EAN = csvDataManager.getCSVData(testSuite, eanRow)[1];
                String subCode = csvDataManager.getCSVData(testSuite, 1)[0];
                PLHomePage.searchEAN(EAN);
                searchResultsPage.selectPLTab("Awaitingapproval");
                boolean actualGtin = searchResultsPage.verifyEAN(EAN);
                Assert.assertEquals(actualGtin, true);
                System.out.println(EAN + " found in search results");
                extentTest.log(Status.INFO,EAN + " found in search results");
                String actualSubCode = searchResultsPage.verifySubscriber(subCode);
                Assert.assertEquals(actualSubCode, subCode);
                System.out.println(subCode + " found in search results");
                extentTest.log(Status.INFO,subCode + " found in search results");
                searchResultsPage.productToClick(subCode);
                System.out.println("Clicked on product " + EAN + " to open Product Details page");
                extentTest.log(Status.INFO,"Clicked on product " + EAN + " to open Product Details page");
                String actualEAN = productDetailPage.verifyEAN(EAN);
                Assert.assertEquals(actualEAN, EAN);
                productDetailPage.writePvidToCsv(testSuite,eanRow);
                String actualSubscriber = productDetailPage.verifySubscriber(subCode);
                Assert.assertEquals(actualSubscriber, subCode);
                productDetailPage.checkApprovalRejectBtnDisabled();

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

        @Test
        public void logOut(){
            productDetailPage.logOut();
        }

    @AfterMethod
    public void afterMethod(ITestResult result)
    {
        if(!result.isSuccess())
            System.setProperty("TestStatus","false");
    }


}
