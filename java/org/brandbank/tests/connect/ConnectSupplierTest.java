package org.brandbank.tests.connect;

import com.aventstack.extentreports.Status;
import org.brandbank.libs.CSVDataManager;
import org.brandbank.libs.PropertyManager;
import org.brandbank.pages.LoginPage;
import org.brandbank.pages.productlibrary.SelectTemplatePage;
import org.brandbank.pages.productlibrary.UploadAssetPage;
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

public class ConnectSupplierTest extends TestBaseSetup {
    LoginPage loginPage;
    PropertyManager propertyManager;
    CSVDataManager csvDataManager;
    PLHomePage PLHomePage;
    SearchResultsPage searchResultsPage;
    ProductDetailPage productDetailPage;
    SelectTemplatePage selectTemplatePage;
    UploadAssetPage uploadAssetPage;

    public ConnectSupplierTest() {
        loginPage = new LoginPage(driver);
        propertyManager = new PropertyManager();
        csvDataManager = new CSVDataManager();
        PLHomePage = new PLHomePage(driver);
        searchResultsPage = new SearchResultsPage(driver);
        productDetailPage = new ProductDetailPage(driver);
        selectTemplatePage = new SelectTemplatePage(driver);
        uploadAssetPage = new UploadAssetPage(driver);
    }

    @Test
    @Parameters({"testSuite"})
    public void publishContent(String testSuite) throws Exception{
        try {
            String EAN = csvDataManager.getCSVData(testSuite, 1)[1];
            String subCode = csvDataManager.getCSVData(testSuite, 1)[0];
            PLHomePage.searchEAN(EAN);
            searchResultsPage.selectPLTab("Awaitingpublish");
            boolean actualEan = searchResultsPage.verifyAwaitingEan(EAN,"awaitingPublishResults");
            Assert.assertEquals(actualEan, true);
            System.out.println(EAN + " found in search results");
            String actualsubCode = searchResultsPage.verifyAwaitingSubscriber(subCode,"awaitingPublishResults");
            Assert.assertEquals(actualsubCode, subCode);
            searchResultsPage.productAwaitingToClick(subCode,"awaitingPublishResults");
            productDetailPage.selectTemplate();
            boolean completeStatus=productDetailPage.validateCompleteStatus();
            Assert.assertEquals(completeStatus,true);
            productDetailPage.templateStatusBtn();
            selectTemplatePage.publish();
            PLHomePage.searchEAN(EAN);
            searchResultsPage.selectPLTab("Live");
            boolean eanFound = searchResultsPage.verifyEAN(EAN);
            Assert.assertEquals(eanFound,true);
            System.out.println(EAN + " found in search results");
            extentTest.log(Status.PASS,EAN + " found in search results");
            String actualSubCode1 = searchResultsPage.verifySubscriber(subCode);
            Assert.assertEquals(actualSubCode1, subCode);
            searchResultsPage.productToClick(subCode);
            productDetailPage.selectTemplate();
            Thread.sleep(2000);
            boolean published=productDetailPage.validatePublishedAndExpiryStatus();
            Assert.assertEquals(published,true);
            extentTest.log(Status.PASS,"Template has been published");
        }
        catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    @Test
    @Parameters({"testSuite"})
    public void expiringTemplate(String testSuite) throws Exception{
        try {
            String EAN = csvDataManager.getCSVData(testSuite, 1)[1];
            String subCode = csvDataManager.getCSVData(testSuite, 1)[0];
            Thread.sleep(10000);
            PLHomePage.searchEAN(EAN);
            searchResultsPage.selectPLTab("ExpiringTemplates");
            boolean actualEan = searchResultsPage.verifyAwaitingEan(EAN,"dueToExpireResults");
            Assert.assertEquals(actualEan, true);
            System.out.println(EAN + " found in search results");
            extentTest.log(Status.INFO,EAN + " found in search results");
            String actualsubCode = searchResultsPage.verifyAwaitingSubscriber(subCode,"dueToExpireResults");
            Assert.assertEquals(actualsubCode, subCode);
            System.out.println(subCode + " found in search results");
            extentTest.log(Status.INFO,subCode + " found in search results");
            System.out.println("Ean and Subcode has been verified");
            extentTest.log(Status.PASS,"Ean and Subcode has been verified");
        }
        catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    @Test
    @Parameters({"testSuite"})
    public void unPublishContent(String testSuite) throws Exception{
        try {
            String EAN = csvDataManager.getCSVData(testSuite, 1)[1];
            String subCode = csvDataManager.getCSVData(testSuite, 1)[0];
            PLHomePage.searchEAN(EAN);
            searchResultsPage.selectPLTab("Live");
            Thread.sleep(3000);
            boolean actualGtin = searchResultsPage.verifyEAN(EAN);
            Assert.assertEquals(actualGtin, true);
            System.out.println(EAN + " found in search results");
            extentTest.log(Status.INFO,EAN + " found in search results");
            String actualSubCode = searchResultsPage.verifySubscriber(subCode);
            Assert.assertEquals(actualSubCode,subCode);
            searchResultsPage.productToClick(subCode);
            productDetailPage.selectTemplate();
            productDetailPage.templateStatusBtn();
            selectTemplatePage.unPublish();
        }
        catch (Exception e) {
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
