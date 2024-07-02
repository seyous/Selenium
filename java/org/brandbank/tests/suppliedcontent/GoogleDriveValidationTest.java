package org.brandbank.tests.suppliedcontent;

import org.brandbank.libs.CSVDataManager;
import org.brandbank.pages.suppliedcontent.GoogleDriveValidationPage;
import org.brandbank.tests.TestBaseSetup;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class GoogleDriveValidationTest extends TestBaseSetup {

    GoogleDriveValidationPage GDpage;
    CSVDataManager csvDataManager;

    public GoogleDriveValidationTest() {
        csvDataManager = new CSVDataManager();
        GDpage = new GoogleDriveValidationPage(driver);
    }

    @Test
    @Parameters({"testSuite", "subCode"})
    public void googleDriveValidation(String testSuite, String subCode) throws Exception {
        try {
            String ean = csvDataManager.getCSVData(testSuite, 1)[1];
            String orderNumber = csvDataManager.getCSVData(testSuite, 1)[3];
            boolean artworkStatus = GDpage.validateArtworkFolder(orderNumber, ean, subCode);
            Assert.assertEquals(artworkStatus, true);
            boolean dataStatus = GDpage.validateDataFolder(orderNumber, ean, subCode);
            Assert.assertEquals(dataStatus, true);
            boolean imagesStatus = GDpage.validateImagesFolder(orderNumber, ean, subCode);
            Assert.assertEquals(imagesStatus, true);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @AfterMethod
    public void afterMethod(ITestResult result) {
        if (!result.isSuccess())
            System.setProperty("TestStatus", "false");
    }
}
