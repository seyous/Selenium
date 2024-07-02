package org.brandbank.tests.supplierportal;

import com.aventstack.extentreports.Status;
import org.brandbank.libs.CSVDataManager;
import org.brandbank.libs.PropertyManager;
import org.brandbank.pages.LoginPage;
import org.brandbank.pages.supplierportal.ApprovalPage;
import org.brandbank.pages.supplierportal.CertifiedPage;
import org.brandbank.pages.supplierportal.SPHomePage;
import org.brandbank.tests.TestBaseSetup;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class CertifiedApprovalTest extends TestBaseSetup {
    LoginPage loginPage;
    PropertyManager propertyManager;
    CSVDataManager csvDataManager;
    SPHomePage SPHomePage;
    CertifiedPage certifiedPage;
    ApprovalPage approvalPage;

    public CertifiedApprovalTest(){
        loginPage = new LoginPage(driver);
        propertyManager = new PropertyManager();
        csvDataManager=new CSVDataManager();
        SPHomePage =new SPHomePage(driver);
        certifiedPage=new CertifiedPage(driver);
        approvalPage=new ApprovalPage(driver);

    }

    @Test
    @Parameters({"testSuite","eanRow"})
    public void validateData(String testSuite,int eanRow) throws Exception{
         try{
             SPHomePage.selectCertified();
             boolean verifyEan=certifiedPage.validateGtinAndSubCode(testSuite,eanRow,"Awaiting Brandbank Captured Data Review");
             Assert.assertEquals(verifyEan,true);
             String csvFilePath = System.getProperty("user.dir") + "/src/test/resources/testdata/Evolve/DataValidation_SupplierPortal.csv";
             boolean verifyData=approvalPage.verifyData(csvFilePath);
             Assert.assertEquals(verifyData,true);
             extentTest.log(Status.PASS,"data validated successfully");
         }
         catch (Exception e){
             e.printStackTrace();
             throw e;
         }

    }
    @Test
    @Parameters({"testSuite","eanRow"})
    public void approveProduct(String testSuite,int eanRow) throws Exception {
        try {
            SPHomePage.selectCertified();
            boolean verifyEan=certifiedPage.validateGtinAndSubCode(testSuite,eanRow,"Awaiting Brandbank Captured Data Review");
            Assert.assertEquals(verifyEan,true);
            approvalPage.approve();
            System.out.println("product approved");
            extentTest.log(Status.INFO,"product approved");
        } catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }
    @Test
    @Parameters({"testSuite","eanRow"})
    public void finalApprove(String testSuite,int eanRow) throws Exception {
        try {
            SPHomePage.selectCertified();
            String statusReturn=certifiedPage.statusReturn(testSuite,eanRow);
            certifiedPage.validateGtinAndSubCode(testSuite,eanRow,statusReturn);
            approvalPage.finalApprove();
            System.out.println("product approval completed");
            extentTest.log(Status.INFO,"product approval completed");
        } catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    @Parameters({"testSuite","eanRow"})
    public void liveStatusCheck(String testSuite,int eanRow) throws Exception {
        try {
            SPHomePage.selectCertified();
            String statusReturn=certifiedPage.statusReturn(testSuite,eanRow);
            certifiedPage.validateGtinAndSubCode(testSuite,eanRow, statusReturn);
        } catch (Exception e){
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
