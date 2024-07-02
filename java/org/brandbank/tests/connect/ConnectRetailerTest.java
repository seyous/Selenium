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

import java.util.Properties;

public class ConnectRetailerTest extends TestBaseSetup {
    LoginPage loginPage;
    CSVDataManager csvDataManager;
    PLHomePage PLHomePage;
    SearchResultsPage searchResultsPage;
    ProductDetailPage productDetailPage;
    SelectTemplatePage selectTemplatePage;
    UploadAssetPage uploadAssetPage;
    Properties configProperties;

    public ConnectRetailerTest() {
        loginPage = new LoginPage(driver);
        csvDataManager = new CSVDataManager();
        PLHomePage = new PLHomePage(driver);
        searchResultsPage = new SearchResultsPage(driver);
        productDetailPage = new ProductDetailPage(driver);
        selectTemplatePage=new SelectTemplatePage(driver);
        uploadAssetPage=new UploadAssetPage(driver);
    }

    @Test
    @Parameters({"testSuite"})
    public void selectTemplate(String testSuite) throws Exception {
        try {
            String EAN = csvDataManager.getCSVData(testSuite, 1)[1];
            PLHomePage.searchEAN(EAN);
            Thread.sleep(3000);
            searchResultsPage.selectOptionFromDropdown("Select_Template", testSuite);
            configProperties = PropertyManager.getPropertiesData();
            String env = configProperties.getProperty("environment");
            selectTemplatePage.selectTemplate(env);
            Thread.sleep(10000);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
        @Test
        @Parameters({"testSuite"})
        public void awaitingContentCheck(String testSuite) throws Exception{
            try{
                String EAN = csvDataManager.getCSVData(testSuite, 1)[1];
                String subCode = csvDataManager.getCSVData(testSuite, 1)[0];
                PLHomePage.searchEAN(EAN);
                searchResultsPage.selectPLTab("Awaitingcontent");
                boolean actualGtin = searchResultsPage.verifyAwaitingEan(EAN,"awaitingContentResults");
                Assert.assertEquals(actualGtin, true);
                System.out.println(EAN + " found in search results");
                extentTest.log(Status.PASS,EAN + " found in search results");
                String actualSubCode = searchResultsPage.verifyAwaitingSubscriber(subCode,"awaitingContentResults");
                Assert.assertEquals(actualSubCode, subCode);
                System.out.println(subCode + " found in search results");
                extentTest.log(Status.PASS,subCode + " found in search results");
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    @Parameters({"testSuite","uploadAsset"})
    public void uploadAssetToTemplate(String testSuite,String uploadAsset) throws Exception{
        try{
            if(uploadAsset.trim().toLowerCase().equals("yes")) {
                String EAN = csvDataManager.getCSVData(testSuite, 1)[1];
                String subCode = csvDataManager.getCSVData(testSuite, 1)[0];
                PLHomePage.searchEAN(EAN);
                searchResultsPage.selectPLTab("Awaitingcontent");
                Thread.sleep(3000);
                searchResultsPage.productAwaitingToClick(subCode, "awaitingContentResults");
                productDetailPage.selectTemplate();
                productDetailPage.templateStatusBtn();
                selectTemplatePage.templateToUploadAsset();
                uploadAssetPage.uploadAsset(1);
                uploadAssetPage.setAssetAttributes();
                selectTemplatePage.attachImagesToPlaceholder();
            }
            else{
                System.out.println("This method is not executed for Automated Mailer test suite");
                extentTest.log(Status.INFO,"This method is not executed for Automated Mailer test suite");
            }
            productDetailPage.logOut();
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
