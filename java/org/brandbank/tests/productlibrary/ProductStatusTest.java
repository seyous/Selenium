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

public class ProductStatusTest extends TestBaseSetup {
    LoginPage loginPage;
    PropertyManager propertyManager;
    CSVDataManager csvDataManager;
    PLHomePage PLHomePage;
    SearchResultsPage searchResultsPage;
    ProductDetailPage productDetailPage;
    public ProductStatusTest(){
        loginPage = new LoginPage(driver);
        propertyManager = new PropertyManager();
        csvDataManager=new CSVDataManager();
        PLHomePage =new PLHomePage(driver);
        searchResultsPage =new SearchResultsPage(driver);
        productDetailPage=new ProductDetailPage(driver);
    }

    @Test()
    @Parameters({"testSuite","PLTab","verifyEanPresentYesOrNo","eanRow"})
    public void verifyProductStatusInPLTab(String testSuite, String PLTab, String verifyEanPresentYesOrNo,int eanRow)throws Exception {
        try {
                if (verifyEanPresentYesOrNo.trim() == "")
                    verifyEanPresentYesOrNo = "Yes";
                String EAN = csvDataManager.getCSVData(testSuite, eanRow)[1];
                String subCode = csvDataManager.getCSVData(testSuite, 1)[0];
                PLHomePage.searchEAN(EAN);
                searchResultsPage.selectPLTab(PLTab);
                switch (PLTab) {
                    case "Live":
                        if (verifyEanPresentYesOrNo.trim().toLowerCase().contains("no")) {
                            boolean productDelisted = searchResultsPage.verifyDelisted();
                            Assert.assertEquals(productDelisted, true);
                            System.out.println("Product " + EAN + " not found as expected");
                            extentTest.log(Status.INFO,"Product " + EAN + " not found as expected");
                        } else {
                            boolean actualLiveGtin = searchResultsPage.verifyEAN(EAN);
                            Assert.assertEquals(actualLiveGtin, true);
                            String actualLiveSubCode = searchResultsPage.verifySubscriber(subCode);
                            Assert.assertEquals(actualLiveSubCode, subCode);
                            System.out.println(EAN + " moved to Live status");
                            extentTest.log(Status.INFO,EAN + " moved to Live status");
                        }
                        break;
                    case "Outstandingrejected":
                        boolean GtinStatus = searchResultsPage.verifyEAN(EAN);
                        Assert.assertEquals(GtinStatus, true);
                        String actualSubCode = searchResultsPage.verifySubscriber(subCode);
                        Assert.assertEquals(actualSubCode, subCode);
                        System.out.println(EAN + " found in outstanding rejection status");
                        extentTest.log(Status.INFO,EAN + " found in outstanding rejection status");
                        break;

                    default:
                        System.out.println("no status found");
                        extentTest.log(Status.FAIL,"no status found");
                        break;
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
