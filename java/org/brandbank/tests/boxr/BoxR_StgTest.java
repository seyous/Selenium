package org.brandbank.tests.boxr;

import com.aventstack.extentreports.Status;
import org.brandbank.libs.PropertyManager;
import org.brandbank.pages.boxr.SignInPage;
import org.brandbank.pages.boxr.WorkflowPage;
import org.brandbank.tests.TestBaseSetup;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.IOException;

public class BoxR_StgTest extends TestBaseSetup {
    BoxRTest boxRTest;
    SignInPage signInPage;
    WorkflowPage workflowPage;

    public BoxR_StgTest() {
        boxRTest = new BoxRTest();
        signInPage=new SignInPage(driver);
        workflowPage=new WorkflowPage(driver);
    }

    @Test
    @Parameters({"testSuite","numOfProducts"})
    public void filterPvid(String testSuite, int numOfProducts) throws Exception {
        try {
            boxRTest.filterPVID(testSuite, numOfProducts);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    @Test
    @Parameters({"testSuite","numOfProducts"})
    public void performDrawAction(String testSuite, int numOfProducts) throws Exception {
        try {
            boxRTest.performDrawAction(testSuite, numOfProducts);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    @Test
    @Parameters({"testSuite","numOfProducts"})
    public void cleanProductAction(String testSuite, int numOfProducts) throws Exception {
        try {
            boxRTest.cleanProductAction(testSuite, numOfProducts);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    @Test
    @Parameters({"testSuite","numOfProducts"})
    public void verifyProductExported(String testSuite, int numOfProducts) throws Exception {
        try {
            boxRTest.verifyProductExported(testSuite, numOfProducts);
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






