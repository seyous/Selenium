package org.brandbank.tests.productlibrary;

import com.aventstack.extentreports.Status;
import org.brandbank.libs.CSVDataManager;
import org.brandbank.libs.FileHandling;
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

public class PIMCorePLTest extends TestBaseSetup {
    LoginPage loginPage;
    PropertyManager propertyManager;
    CSVDataManager csvDataManager;
    PLHomePage PLHomePage;
    SearchResultsPage searchResultsPage;
    ProductDetailPage productDetailPage;
    FileHandling fileHandling;
    public PIMCorePLTest(){
        loginPage = new LoginPage(driver);
        propertyManager = new PropertyManager();
        csvDataManager=new CSVDataManager();
        PLHomePage =new PLHomePage(driver);
        searchResultsPage =new SearchResultsPage(driver);
        productDetailPage=new ProductDetailPage(driver);
        fileHandling = new FileHandling();
    }

    @Test
    @Parameters("testSuite")
    public void verifyProductStatus(String testSuite) throws Exception{
        try{
            String EAN = csvDataManager.getCSVData(testSuite, 1)[1];
            String subCode = csvDataManager.getCSVData(testSuite, 1)[0];
            PLHomePage.searchEAN(EAN);
            searchResultsPage.selectPLTab("Live");
            boolean actualLiveGtin = searchResultsPage.verifyEAN(EAN);
            Assert.assertEquals(actualLiveGtin, true);
            String actualLiveSubCode = searchResultsPage.verifySubscriber(subCode);
            Assert.assertEquals(actualLiveSubCode, subCode);
            System.out.println(EAN + " found in Live status");
            extentTest.log(Status.PASS,"All data verified successfully");
        }
        catch (Exception e){
            e.printStackTrace();
            throw  e;
        }
    }

    @Test
    @Parameters("testSuite")
    public void verifyObject(String testSuite) throws Exception{
        try {
            String EAN = csvDataManager.getCSVData(testSuite, 1)[1];
            String subCode = csvDataManager.getCSVData(testSuite, 1)[0];
            PLHomePage.searchEAN(EAN);
            searchResultsPage.selectPLTab("Live");
            boolean actualGtin = searchResultsPage.verifyEAN(EAN);
            Assert.assertEquals(actualGtin, true);
            System.out.println(EAN + " found in search results");
            extentTest.log(Status.INFO,EAN + " found in search results");
            String actualSubCode = searchResultsPage.verifySubscriber(subCode);
            Assert.assertEquals(actualSubCode, subCode);
            searchResultsPage.productToClick(subCode);
            System.out.println("Clicked on product " + EAN + " to open Product Details page");
            extentTest.log(Status.INFO,"Clicked on product " + EAN + " to open Product Details page");
            boolean objectFound = productDetailPage.pimCoreVerifyObject();
            Assert.assertEquals(objectFound,true);
        }
        catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    @Parameters("testSuite")
    public void verifyLabelObject(String testSuite) throws Exception{
        try{
            String EAN = csvDataManager.getCSVData(testSuite, 1)[1];
            String subCode = csvDataManager.getCSVData(testSuite, 1)[0];
            String sourcePath1=System.getProperty("user.dir") + "\\src\\test\\resources\\testdata\\PIMCore\\downloads";

            PLHomePage.searchEAN(EAN);
            searchResultsPage.selectPLTab("Live");
            boolean actualGtin = searchResultsPage.verifyEAN(EAN);
            Assert.assertEquals(actualGtin, true);
            System.out.println(EAN + " found in search results");
            extentTest.log(Status.INFO,EAN + " found in search results");
            String actualSubCode = searchResultsPage.verifySubscriber(subCode);
            Assert.assertEquals(actualSubCode, subCode);
            searchResultsPage.productToClick(subCode);
            System.out.println("Clicked on product " + EAN + " to open Product Details page");
            extentTest.log(Status.INFO,"Clicked on product " + EAN + " to open Product Details page");
            fileHandling.deleteAllFiles(sourcePath1);
            fileHandling.deleteFile(System.getProperty("user.dir") + "\\downloads\\images.zip");
            productDetailPage.pimCoreDownloadLabelObject();
            String sourcePath=System.getProperty("user.dir") + "\\src\\test\\resources\\testdata\\PIMCore\\downloads\\"+EAN+"_LabelData.pdf";
            boolean fileFound = fileHandling.verifyFileExists(sourcePath);
            Assert.assertEquals(fileFound,true);
            System.out.println("Label object downloaded successfully");
            extentTest.log(Status.PASS,"Label object downloaded successfully");
            PLHomePage.logOut();
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
