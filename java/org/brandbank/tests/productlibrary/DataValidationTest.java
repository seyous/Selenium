package org.brandbank.tests.productlibrary;

import com.aventstack.extentreports.Status;
import org.brandbank.libs.BrowserActions;
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

public class DataValidationTest extends TestBaseSetup {
    LoginPage loginPage;
    PropertyManager propertyManager;
    CSVDataManager csvDataManager;
    PLHomePage PLHomePage;
    SearchResultsPage searchResultsPage;
    ProductDetailPage productDetailPage;
    DataValidationPage dataValidationPage;
    BrowserActions browserActions;

    public DataValidationTest() {
        loginPage = new LoginPage(driver);
        propertyManager = new PropertyManager();
        csvDataManager = new CSVDataManager();
        PLHomePage = new PLHomePage(driver);
        searchResultsPage = new SearchResultsPage(driver);
        productDetailPage = new ProductDetailPage(driver);
        dataValidationPage = new DataValidationPage(driver);
        browserActions = new BrowserActions(driver);
    }

    @Test()
    @Parameters({"testSuite", "numOfProducts","tabToSelect","category"})
    public void validateDataInPL(String testSuite, int numOfProducts,String tabToSelect,String productCategory)throws Exception {
        try{
            //String csvFilePath = System.getProperty("user.dir") + "/src/test/resources/testdata/" + testSuite + "/DataValidation_PL.csv";
            String locale = browserActions.getBrowserLocale();
            String csvFilePath = System.getProperty("user.dir") + "/src/test/resources/testdata/" + testSuite + "";
            if (locale.trim().toLowerCase().contains("en-gb")) {
                csvFilePath = csvFilePath + "/" + "DataValidation_PL.csv";
            } else if (locale.trim().toLowerCase().contains("en-us")) {
                csvFilePath = csvFilePath + "/" + "DataValidationUS_PL.csv";

            } else {
                System.out.println("Language not correct");
                extentTest.log(Status.FAIL, "Language not correct");
            }
            if((testSuite.trim().toLowerCase().contains("uncertified")) && (productCategory.trim().toLowerCase().equals("food")))
                csvFilePath = System.getProperty("user.dir") + "/src/test/resources/testdata/" + testSuite + "/DataValidation_PL_Food.csv";
            for (int i = 1; i <= numOfProducts; i++) {
                String EAN = csvDataManager.getCSVData(testSuite, i)[1];
                String subCode = csvDataManager.getCSVData(testSuite, i)[0];
                PLHomePage.searchEAN(EAN);
                searchResultsPage.selectPLTab(tabToSelect);
                boolean actualLiveGtin = searchResultsPage.verifyEAN(EAN);
                Assert.assertEquals(actualLiveGtin, true);
                String actualLiveSubCode = searchResultsPage.verifySubscriber(subCode);
                Assert.assertEquals(actualLiveSubCode, subCode);
                searchResultsPage.productToClick(subCode);
                System.out.println("Clicked on product " + EAN + " to open Product Details page");
                extentTest.log(Status.INFO,"Clicked on product " + EAN + " to open Product Details page");
                String actualEAN = productDetailPage.verifyEAN(EAN);
                Assert.assertEquals(actualEAN, EAN);
                String actualSubscriber = productDetailPage.verifySubscriber(subCode);
                Assert.assertEquals(actualSubscriber, subCode);
                System.out.println("Verifying data entered in Data Entry is present in PL");
                extentTest.log(Status.INFO,"Verifying data entered in Data Entry is present in PL");
                boolean dataVerified = dataValidationPage.verifyData(csvFilePath);
                Assert.assertEquals(dataVerified, true);
                System.out.println("All data verified successfully");
                extentTest.log(Status.PASS,"All data verified successfully");
                //added this for UC product. Will not affect other packs
                String PVID = csvDataManager.getCSVData(testSuite, i)[5];
                if(PVID.trim().equals(""))
                    productDetailPage.writePvidToCsv(testSuite,i);
            }
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
