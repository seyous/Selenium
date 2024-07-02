package org.brandbank.tests.dataworkflow;

import com.aventstack.extentreports.Status;
import com.opencsv.exceptions.CsvException;
import org.brandbank.libs.CSVDataManager;
import org.brandbank.libs.PropertyManager;
import org.brandbank.pages.LoginPage;
import org.brandbank.pages.dataworkflow.DataWorkflowHomePage;
import org.brandbank.tests.TestBaseSetup;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.IOException;

public class DataWorkflowTest extends TestBaseSetup {
    LoginPage loginPage;
    PropertyManager propertyManager;
    CSVDataManager csvDataManager;
    DataWorkflowHomePage dataWorkflowHomePage;

    public DataWorkflowTest(){
        loginPage = new LoginPage(driver);
        propertyManager = new PropertyManager();
        dataWorkflowHomePage = new DataWorkflowHomePage(driver);
        csvDataManager = new CSVDataManager();
    }

    @Test
    @Parameters({"testSuite"})
    public void DataWorkflow(String testSuite) throws Exception {
        try{
                dataWorkflowHomePage.searchForBatch(csvDataManager.getCSVData(testSuite, 1)[4]);
                System.out.println("Batch found successfully");
                extentTest.log(Status.PASS,"Batch found successfully");
                dataWorkflowHomePage.logOut();
        }
        catch (Exception e){
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
