package org.brandbank.tests.boxr;

import com.aventstack.extentreports.Status;
import org.brandbank.libs.BrowserActions;
import org.brandbank.libs.CSVDataManager;
import org.brandbank.libs.PropertyManager;
import org.brandbank.pages.automatedMailer.OutlookPage;
import org.brandbank.pages.boxr.*;
import org.brandbank.tests.TestBaseSetup;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;


public class BoxRCICDTest extends TestBaseSetup {
    SignInPage signInPage;
    WorkflowPage workflowPage;
    DrawPage drawPage;
    CleanPage cleanPage;
    CSVDataManager csvDataManager;
    OutlookPage outlookPage;
    BrowserActions browserActions;
    APIPage apiPage;
    public BoxRCICDTest() {
        signInPage=new SignInPage(driver);
        workflowPage=new WorkflowPage(driver);
        drawPage=new DrawPage(driver);
        cleanPage=new CleanPage(driver);
        csvDataManager=new CSVDataManager();
        outlookPage=new OutlookPage(driver);
        browserActions=new BrowserActions(driver);
        apiPage = new APIPage(driver);
    }

    @Test
    @Parameters({"testSuite"})
    public void performDrawAction(String testSuite) throws Exception{
        String pvid = csvDataManager.getCSVData(testSuite, 1)[0];
        boolean drawAction = workflowPage.selectPvidToDraw(pvid);
        Assert.assertEquals(drawAction, true);
        drawPage.setFlag();
        System.out.println("Product Flag set successfully");
        extentTest.log(Status.PASS,"Product Flag set successfully");
        drawPage.drawForCICD();
        System.out.println("Successfully drew and submitted");
        extentTest.log(Status.PASS,"Successfully drew and submitted");
    }

   @Test
    @Parameters({"testSuite"})
    public void performCleanProductAction(String testSuite) throws Exception{
        String pvid = csvDataManager.getCSVData(testSuite, 1)[0];
        boolean cleanProduct = workflowPage.selectPVIDToClean(pvid);
        Assert.assertEquals(cleanProduct, true);
        boolean dataPresent = cleanPage.dataValidationForCICD();
        Assert.assertEquals(dataPresent,true);
        cleanPage.completeBtn();
        System.out.println("Clean action performed successfully");
       extentTest.log(Status.PASS,"Clean action performed successfully");
    }

    @Test
    @Parameters({"testSuite"})
    public void validateProductCompleted(String testSuite) throws Exception {
        String pvid = csvDataManager.getCSVData(testSuite, 1)[0];
        boolean pvidCheck = workflowPage.validateCompletedStatus(pvid);
        Assert.assertEquals(pvidCheck, true);
    }

    @Test
    @Parameters({"testSuite"})
    public void validateProductExported(String testSuite) throws Exception {
        String pvid = csvDataManager.getCSVData(testSuite, 1)[0];
        boolean pvidCheck = workflowPage.validateExportedStatus(pvid);
        Assert.assertEquals(pvidCheck, true);
    }

    @Test
    public  void signOut() throws InterruptedException {
        signInPage.signOut();
    }

    @AfterMethod
    public void afterMethod(ITestResult result)
    {
        if(!result.isSuccess())
            System.setProperty("TestStatus","false");
    }
}


