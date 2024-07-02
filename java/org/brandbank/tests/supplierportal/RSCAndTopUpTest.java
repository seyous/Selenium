package org.brandbank.tests.supplierportal;

import org.brandbank.libs.CSVDataManager;
import org.brandbank.libs.PropertyManager;
import org.brandbank.pages.LoginPage;
import org.brandbank.pages.supplierportal.*;
import org.brandbank.tests.TestBaseSetup;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class RSCAndTopUpTest extends TestBaseSetup {
    LoginPage loginPage;
    PropertyManager propertyManager;
    CSVDataManager csvDataManager;
    SPHomePage SPHomePage;
    CertifiedPage certifiedPage;
    ApprovalPage approvalPage;
    RetailerSpecificContent retailerSpecificContent;
   UncertifiedProductsPage uncertifiedProductsPage;

    public RSCAndTopUpTest(){
        loginPage = new LoginPage(driver);
        propertyManager = new PropertyManager();
        csvDataManager=new CSVDataManager();
        SPHomePage =new SPHomePage(driver);
        certifiedPage=new CertifiedPage(driver);
        approvalPage=new ApprovalPage(driver);
        retailerSpecificContent=new RetailerSpecificContent(driver);
        uncertifiedProductsPage=new UncertifiedProductsPage(driver);
    }

    @Test
    @Parameters({"testSuite","eanRow"})
    public void approveRSCProduct(String testSuite, int eanRow) throws Exception{
        try {
            SPHomePage.selectCertified();
            certifiedPage.validateGtinAndSubCode(testSuite,eanRow,"Awaiting Brandbank Captured Data Review");
            approvalPage.approve();
        }
        catch (Exception e){
            e.printStackTrace();
            throw e;
        }

    }
    @Test
    @Parameters({"testSuite","eanRow"})
    public void addRSCAndTopUpContent(String testSuite, int eanRow) throws Exception{
        try {
            String ean=csvDataManager.getCSVData(testSuite,eanRow)[1];
            SPHomePage.selectCertified();
            String statusReturn = certifiedPage.statusReturn(testSuite, eanRow);
            boolean verifyEan = certifiedPage.validateGtinAndSubCode(testSuite, eanRow, statusReturn);
            Assert.assertEquals(verifyEan, true);
            retailerSpecificContent.addRetailer();
            retailerSpecificContent.addDataToRSCProduct();
            uncertifiedProductsPage.addDataToUCProduct(testSuite, ean, "English", "");
            approvalPage.finalApprove();
            SPHomePage.selectCertified();
            String statusReturnRSC = certifiedPage.statusReturn(testSuite, eanRow);
            certifiedPage.validateGtinAndSubCode(testSuite, eanRow, statusReturnRSC);
        }
        catch (Exception e){
            e.printStackTrace();
            throw e;
        }

    }
    @Test
    public void logOut() throws InterruptedException {
        SPHomePage.logout();

    }
    @AfterMethod
    public void afterMethod(ITestResult result)
    {
        if(!result.isSuccess())
            System.setProperty("TestStatus","false");
    }

}
