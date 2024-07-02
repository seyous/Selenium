package org.brandbank.tests.supplierportal;

import com.aventstack.extentreports.Status;
import org.brandbank.libs.CSVDataManager;
import org.brandbank.libs.EANGenerator;
import org.brandbank.libs.PropertyManager;
import org.brandbank.pages.LoginPage;
import org.brandbank.pages.supplierportal.ApprovalPage;
import org.brandbank.pages.supplierportal.CertifiedPage;
import org.brandbank.pages.supplierportal.SPHomePage;
import org.brandbank.pages.supplierportal.UncertifiedProductsPage;
import org.brandbank.tests.TestBaseSetup;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;

public class UncertifiedProductTest extends TestBaseSetup{
    LoginPage loginPage;
    PropertyManager propertyManager;
    CSVDataManager csvDataManager;
    SPHomePage SPHomePage;
    ApprovalPage approvalPage;
    UncertifiedProductsPage uncertifiedProductspage;
    CertifiedPage certifiedPage;

    public  UncertifiedProductTest(){
        loginPage = new LoginPage(driver);
        propertyManager = new PropertyManager();
        csvDataManager=new CSVDataManager();
        SPHomePage =new SPHomePage(driver);
        approvalPage=new ApprovalPage(driver);
        uncertifiedProductspage = new UncertifiedProductsPage(driver);
        certifiedPage = new CertifiedPage(driver);
    }

    @Test
    @Parameters({"testSuite","numOfProducts","subCode","category"})
    public void newUncertifiedProduct(String testSuite, int numOfProducts, String subCode, String category) throws Exception{
        try{
            EANGenerator.generateEAN(testSuite, numOfProducts, subCode);
            SPHomePage.selectUncertifiedProducts();
            String ean = (csvDataManager.getCSVData(testSuite, 1)[1]);
            String description = (csvDataManager.getCSVData(testSuite, 1)[2]);
            //Create new Uncertified product page
            uncertifiedProductspage.createUncertifiedProduct(testSuite,ean,subCode,description,category);
            // add neutral data
            uncertifiedProductspage.addDataToUCProduct(testSuite,ean,"Neutral",category);
            //Add English Data
            uncertifiedProductspage.addDataToUCProduct(testSuite,ean,"English",category);
            //Approve Product
            boolean productApproved = uncertifiedProductspage.approveUncertifiedProduct(testSuite,ean);
            Assert.assertEquals(productApproved,true);
            extentTest.log(Status.PASS,"product approved successfully");
            Thread.sleep(2000);

        }
        catch (Exception e){
            throw e;
        }
    }

    @Test
    public void logoutSupplierPortal() throws InterruptedException {
        SPHomePage.logout();
    }

    @AfterMethod
    public void afterMethod(ITestResult result)
    {
        if(!result.isSuccess())
            System.setProperty("TestStatus","false");
    }
}
