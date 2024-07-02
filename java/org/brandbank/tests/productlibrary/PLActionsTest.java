package org.brandbank.tests.productlibrary;

import com.aventstack.extentreports.Status;
import org.brandbank.libs.CSVDataManager;
import org.brandbank.libs.DateManager;
import org.brandbank.libs.FileHandling;
import org.brandbank.libs.PropertyManager;
import org.brandbank.pages.LoginPage;
import org.brandbank.pages.productlibrary.*;
import org.brandbank.tests.TestBaseSetup;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class PLActionsTest extends TestBaseSetup {
    LoginPage loginPage;
    PropertyManager propertyManager;
    CSVDataManager csvDataManager;
    PLHomePage PLHomePage;
    SearchResultsPage searchResultsPage;
    ProductDetailPage productDetailPage;
    FileHandling fileHandling;
    ImageSearchPage imageSearchPage;
    ProjectsPage projectsPage;
    ImpersonatePage impersonatePage;
    public PLActionsTest() {
        loginPage = new LoginPage(driver);
        propertyManager = new PropertyManager();
        csvDataManager=new CSVDataManager();
        fileHandling=new FileHandling();
        PLHomePage =new PLHomePage(driver);
        searchResultsPage =new SearchResultsPage(driver);
        productDetailPage=new ProductDetailPage(driver);
        imageSearchPage=new ImageSearchPage(driver);
        projectsPage = new ProjectsPage(driver);
        impersonatePage = new ImpersonatePage(driver);
    }

    @Test
    @Parameters({"testSuite"})
    public void extractProductData(String testSuite) throws Exception{
        try{
            String extendTag = new DateManager().getCurrentDate("dd-MM-YYYY");
            String sheetName="Brandbank Product Library Data";
            String downloadFilePath=System.getProperty("user.dir") + "\\downloads\\Product Library Extract " + extendTag + ".xlsx";
            String EAN = csvDataManager.getCSVData(testSuite, 1)[1];
            if(fileHandling.verifyFileExists(downloadFilePath))
                fileHandling.deleteFile(downloadFilePath);
            PLHomePage.searchEAN(EAN);
            //homePage.verifyCookie();
            System.out.println("Selecting Extract_Data option from dropdown");
            extentTest.log(Status.INFO,"Selecting Extract_Data option from dropdown");
            searchResultsPage.selectOptionFromDropdown("Extract_Data",testSuite);
            boolean fileFound = fileHandling.verifyFileExists(downloadFilePath);
            Assert.assertEquals(fileFound,true);
            System.out.println("Product Data file downloaded successfully");
            extentTest.log(Status.PASS,"Product Data file downloaded successfully");
            fileHandling.setExcelFile(downloadFilePath,sheetName);
            Assert.assertEquals(String.valueOf(fileHandling.getCellData("EAN",2)),EAN);
            System.out.println("EAN is present in excel sheet");
            extentTest.log(Status.INFO,"EAN is present in excel sheet");
        }
        catch (Exception e){
            e.printStackTrace();
            throw  e;
        }
    }

    @Test
    @Parameters({"testSuite"})
    public void extractDataAsPDF(String testSuite) throws Exception {
        try {
            Thread.sleep(5000);
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
            String actualEAN = productDetailPage.verifyEAN(EAN);
            Assert.assertEquals(actualEAN, EAN);
            String actualSubscriber = productDetailPage.verifySubscriber(subCode);
            Assert.assertEquals(actualSubscriber, subCode);
            boolean verifyDownloaded = productDetailPage.downloadPDF();
            Assert.assertEquals(verifyDownloaded,true);
            Thread.sleep(3000);
        }
        catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    @Parameters({"testSuite"})
    public void extractProductImages(String testSuite) throws Exception {
        try {
            String EAN = csvDataManager.getCSVData(testSuite, 1)[1];
            PLHomePage.searchEAN(EAN);
            Thread.sleep(5000);
            searchResultsPage.selectOptionFromDropdown("Request_Images", testSuite);
            boolean imageZipDownloaded = imageSearchPage.imageRequest();
            Assert.assertEquals(imageZipDownloaded,true);
            System.out.println("Image zip downloaded successfully");
            extentTest.log(Status.PASS,"Image zip downloaded successfully");
            String sourcePath=System.getProperty("user.dir") + "\\src\\test\\resources\\testdata\\PL_Extract\\Downloads\\"+EAN+"_T1.tif";
            boolean fileFound = fileHandling.verifyFileExists(sourcePath);
            Assert.assertEquals(fileFound,true);
            System.out.println("Images downloaded successfully");
            extentTest.log(Status.PASS,"Images downloaded successfully");
            String sourcePath1=System.getProperty("user.dir") + "\\src\\test\\resources\\testdata\\PL_Extract\\Downloads";
            fileHandling.deleteAllFiles(sourcePath1);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    @Parameters({"testSuite"})
    public void merchExtract(String testSuite) throws Exception {
        try {
            String sheetName="PRODUCT";
            String ExcelPath=System.getProperty("user.dir") + "\\src\\test\\resources\\testdata\\PL_Extract\\Downloads\\Merchandising Dimensions.xlsx";
            String sourcePath=System.getProperty("user.dir") + "\\src\\test\\resources\\testdata\\PL_Extract\\Downloads";
            String zipFilePath=System.getProperty("user.dir") + "\\downloads\\brandbank-merchandising-1.zip";
            if(fileHandling.verifyFileExists(zipFilePath))
                fileHandling.deleteFile(zipFilePath);
            String ean = csvDataManager.getCSVData(testSuite, 1)[1];
            Thread.sleep(5000);
            PLHomePage.searchEAN(ean);
            Thread.sleep(3000);
            searchResultsPage.selectOptionFromDropdown("Merch_Extract", testSuite);
            Thread.sleep(5000);
            boolean results=imageSearchPage.verifyTShot();
            Assert.assertEquals(results,true);
            boolean zipDownloaded = imageSearchPage.unZipDownload();
            Assert.assertEquals(zipDownloaded,true);
            System.out.println("Merch extract zip downloaded successfully");
            extentTest.log(Status.PASS,"Merch extract zip downloaded successfully");
            boolean fileFound = imageSearchPage.verifyImageDownload(testSuite);
            Assert.assertEquals(fileFound,true);
            System.out.println("Image found:" + ean + ".1 ");
            extentTest.log(Status.INFO,"Image found:" + ean + ".1 ");
            fileHandling.setExcelFile(ExcelPath,sheetName);
            Assert.assertEquals(String.valueOf(fileHandling.getCellData("UPC",1)),ean);
            System.out.println("ean found in downloaded excel");
            extentTest.log(Status.INFO,"ean found in downloaded excel");
            fileHandling.deleteAllFiles(sourcePath);
            System.out.println("File has been deleted");
            extentTest.log(Status.INFO,"File has been deleted");
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    @Parameters({"testSuite"})
    public void labelImageViewer(String testSuite) throws Exception {
        try {
            String EAN = csvDataManager.getCSVData(testSuite, 1)[1];
            String subCode = csvDataManager.getCSVData(testSuite, 1)[0];
            Thread.sleep(5000);
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
            String actualEAN = productDetailPage.verifyEAN(EAN);
            Assert.assertEquals(actualEAN, EAN);
            String actualSubscriber = productDetailPage.verifySubscriber(subCode);
            Assert.assertEquals(actualSubscriber, subCode);
            productDetailPage.viewLabelImage();
        }
        catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    @Parameters({"testSuite"})
    public void verifyDataAndObject(String testSuite) throws Exception{
        try {
            String EAN = csvDataManager.getCSVData(testSuite, 1)[1];
            String subCode = csvDataManager.getCSVData(testSuite, 1)[0];
            String fieldValue = "A Test product for M2M Testing - Update";
            Thread.sleep(5000);
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
            String actualEAN = productDetailPage.verifyEAN(EAN);
            Assert.assertEquals(actualEAN, EAN);
            String actualSubscriber = productDetailPage.verifySubscriber(subCode);
            Assert.assertEquals(actualSubscriber, subCode);
            boolean objectFound = productDetailPage.verifyDataAndObject(fieldValue);
            Assert.assertEquals(objectFound,true);
        }
        catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    public void projectListTest(){
        PLHomePage.navigateToProjectsTab();
        projectsPage.validateProjectPage();
        projectsPage.validateProject();
    }

    @Test
    public void impersonateAndMeXTest() throws InterruptedException{
        PLHomePage.verifyCookie();
        PLHomePage.navigateToImpersonatePage();
        String pageTitle = impersonatePage.verifyPageTitle();
        Assert.assertEquals(pageTitle,"User Impersonation");
        System.out.println("Impersonation page open successfully");
        extentTest.log(Status.INFO,"Impersonation page open successfully");
        impersonatePage.impersonateUser("svc.autotestd@brandbank.com");
        PLHomePage.advancedSearch();
        searchResultsPage.verifySubscriberName();
        PLHomePage.stopImpersonation();
        productDetailPage.logOut();
    }

    @AfterMethod
    public void afterMethod(ITestResult result)
    {
        if(!result.isSuccess())
            System.setProperty("TestStatus","false");
    }
}


