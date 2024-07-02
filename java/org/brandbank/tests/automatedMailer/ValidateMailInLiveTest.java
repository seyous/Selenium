package org.brandbank.tests.automatedMailer;

import com.aventstack.extentreports.Status;
import org.brandbank.libs.BrowserActions;
import org.brandbank.libs.CSVDataManager;
import org.brandbank.libs.PropertyManager;
import org.brandbank.pages.LoginPage;
import org.brandbank.pages.automatedMailer.OutlookPage;
import org.brandbank.pages.productlibrary.PLHomePage;
import org.brandbank.pages.productlibrary.SearchResultsPage;
import org.brandbank.tests.TestBaseSetup;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
    public class ValidateMailInLiveTest extends TestBaseSetup {

        public static WebDriver driver;
        OutlookPage outlookPage;
        LoginPage loginPage;
        PLHomePage PLHomePage;
        CSVDataManager csvDataManager;
        SearchResultsPage searchResultsPage;
        BrowserActions browserActions;
        public ValidateMailInLiveTest() {
            loginPage =new LoginPage(driver);
            searchResultsPage=new SearchResultsPage(driver);
            outlookPage =new OutlookPage(driver);
            PLHomePage =new PLHomePage(driver);
            csvDataManager=new CSVDataManager();
            browserActions=new BrowserActions(driver);
        }

        @Test
        @Parameters({"testSuite"})
        public void searchEmailInProd (String testSuite) throws Exception {
            try {
                String username=PropertyManager.getPropertiesData().getProperty("user2");
                String password=PropertyManager.getPropertiesData().getProperty("pass2");
                boolean validateMail=outlookPage.searchMailAndValidate(username,password);
                Assert.assertEquals(validateMail,true);
                Thread.sleep(3000);
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
                PLHomePage.logOut();
                browserActions.switchToTab(1,0);
                outlookPage.deleteMail("New Connect Template");
                outlookPage.logOut();
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

