package org.brandbank.tests.suppliedcontent;

import org.brandbank.libs.CSVDataManager;
import org.brandbank.libs.PropertyManager;
import org.brandbank.pages.LoginPage;
import org.brandbank.pages.suppliedcontent.SuppliedContentHomePage;
import org.brandbank.pages.suppliedcontent.UploadFilesPage;
import org.brandbank.tests.TestBaseSetup;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class UploadFileTest extends TestBaseSetup {
    LoginPage loginPage;
    PropertyManager propertyManager;
    UploadFilesPage UploadFilesPage;
    SuppliedContentHomePage SuppliedContentHomePage;
    CSVDataManager csvDataManager;

    public UploadFileTest() {
        loginPage = new LoginPage(driver);
        propertyManager = new PropertyManager();
        SuppliedContentHomePage = new SuppliedContentHomePage(driver);
        UploadFilesPage = new UploadFilesPage(driver);
        csvDataManager = new CSVDataManager();
    }

    @Test
    @Parameters({"testSuite", "numOfProducts"})
    public void uploadFiles(String testSuite, int numOfProducts) throws Exception {
        try {
            String csvFilePath = System.getProperty("user.dir") + "/src/test/resources/testdata/" + testSuite + "/ServiceTypes.csv";
            String orderNumber = csvDataManager.getCSVData(testSuite, 1)[3];
            SuppliedContentHomePage.searchOrder(orderNumber);
           boolean fileSuccess = UploadFilesPage.fileUpload(testSuite, numOfProducts, csvFilePath);
            Assert.assertEquals(fileSuccess,true);
            Thread.sleep(8000);
            SuppliedContentHomePage.logout();
        } catch (Exception e) {
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
