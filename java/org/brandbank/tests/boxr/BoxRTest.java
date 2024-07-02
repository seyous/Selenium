package org.brandbank.tests.boxr;

import com.aventstack.extentreports.Status;
import org.brandbank.libs.BrowserActions;
import org.brandbank.libs.CSVDataManager;
import org.brandbank.libs.PropertyManager;
import org.brandbank.pages.automatedMailer.OutlookPage;
import org.brandbank.pages.boxr.CleanPage;
import org.brandbank.pages.boxr.DrawPage;
import org.brandbank.pages.boxr.SignInPage;
import org.brandbank.pages.boxr.WorkflowPage;
import org.brandbank.tests.TestBaseSetup;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.IOException;


public class BoxRTest extends TestBaseSetup {
    SignInPage signInPage;
    WorkflowPage workflowPage;
    DrawPage drawPage;
    CleanPage cleanPage;
    CSVDataManager csvDataManager;
    OutlookPage outlookPage;
    BrowserActions browserActions;
    public BoxRTest() {
        signInPage=new SignInPage(driver);
        workflowPage=new WorkflowPage(driver);
        drawPage=new DrawPage(driver);
        cleanPage=new CleanPage(driver);
        csvDataManager=new CSVDataManager();
        outlookPage=new OutlookPage(driver);
        browserActions=new BrowserActions(driver);
    }
    public void loginToOutlook() throws Exception{
        try {
            String outlookUser = PropertyManager.getPropertiesData().getProperty("outlookUser1");
            String outlookPwd = PropertyManager.getPropertiesData().getProperty("outlookPass1");
            outlookPage.getUrl("https://imap-mail.outlook.com/mail/");
            outlookPage.loginToOutlook(outlookUser, outlookPwd);
            String actualTitle = outlookPage.verifyTitle();
            Assert.assertEquals(actualTitle, "Email - autotestb, svc - Outlook");
            boolean mailVerification=outlookPage.searchMailAndCopyCode();
            Assert.assertEquals(mailVerification,true);
            extentTest.log(Status.PASS,"Logged into Outlook successfully");
        }
        catch (AssertionError e){
            System.setProperty("TestStatus", "false");
            extentTest.log(Status.FAIL,"Failed to log into Outlook");
            throw e;
        }
    }

    public void enterMFA() throws Exception{
        try {
            signInPage.verifyUserDetails();
        }
        catch (AssertionError e){
                System.setProperty("TestStatus", "false");
                throw e;
            }
    }
    @Test
    @Parameters({"testSuite","numOfProducts"})
    public void filterPVID(String testSuite,int numberOfProducts) throws Exception {
        try {
            for (int i = 1; i <= numberOfProducts; i++) {
                String pvid = csvDataManager.getCSVData(testSuite, i)[5];
                boolean pvidReturns = workflowPage.checkPvid(pvid);
                Assert.assertEquals(pvidReturns, true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    @Test
    @Parameters({"testSuite","numOfProducts"})
    public void performDrawAction(String testSuite,int numOfProducts) throws Exception{
        try {
            for (int i = 1; i <= numOfProducts; i++) {
                String pvid = csvDataManager.getCSVData(testSuite, i)[5];
                boolean drawAction = workflowPage.selectPvidToDraw(pvid);
                Assert.assertEquals(drawAction, true);
                drawPage.getDimension();
                drawPage.drawRequiredSegments();
                By languageBtn = By.xpath("//*[@class='languages']//following::select[2]");
                if (browserActions.isDisplayedNew(languageBtn)) {
                    Thread.sleep(1000);
                    browserActions.selectFromDropdownByIndex(languageBtn, 1);
                    drawPage.drawRequiredSegments();
                    drawPage.submitSegment();
                    extentTest.log(Status.PASS, "Draw action is completed");
                } else {
                    extentTest.log(Status.FAIL, "Language not displayed");
                    System.out.println("Language not displayed");
                }
            }
        }
        catch(Exception e){
                e.printStackTrace();
                throw e;
            }
        }
    @Test
    @Parameters({"testSuite","numOfProducts"})
    public void cleanProductAction(String testSuite,int numOfProducts) throws Exception{
        try {
            for (int i = 1; i <= numOfProducts; i++) {
                String pvid = csvDataManager.getCSVData(testSuite, i)[5];
                boolean cleanProduct = workflowPage.selectPVIDToClean(pvid);
                Assert.assertEquals(cleanProduct, true);
                boolean dataFound = cleanPage.validateSegmentContent(testSuite);

                Assert.assertEquals(dataFound, true);
            }
        }
        catch(Exception e){
            e.printStackTrace();
            throw e;
        }
    }
    @Test
    @Parameters({"testSuite","numOfProducts"})
    public void verifyProductExported(String testSuite, int numOfProducts) throws Exception {
        try {
            for (int i = 1; i <= numOfProducts; i++) {
                String pvid = csvDataManager.getCSVData(testSuite, i)[5];
                boolean pvidCheck = workflowPage.validateExportedStatus(pvid);
                Assert.assertEquals(pvidCheck, true);
            }
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


