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

public class CertifiedRejectionTest extends TestBaseSetup {
    LoginPage loginPage;
    PropertyManager propertyManager;
    CSVDataManager csvDataManager;
    SPHomePage SPHomePage;
    CertifiedPage certifiedPage;
    ApprovalPage approvalPage;

    public CertifiedRejectionTest(){
        loginPage = new LoginPage(driver);
        propertyManager = new PropertyManager();
        csvDataManager=new CSVDataManager();
        SPHomePage =new SPHomePage(driver);
        certifiedPage=new CertifiedPage(driver);
        approvalPage=new ApprovalPage(driver);

    }

    @Test
    @Parameters({"testSuite","eanRow"})
    public void rejectProduct(String testSuite,int eanRow) throws Exception{
        try {
            SPHomePage.selectCertified();
            boolean verifyEan=certifiedPage.validateGtinAndSubCode(testSuite,eanRow,"Awaiting Brandbank Captured Data Review");
            Assert.assertEquals(verifyEan,true);
            approvalPage.Reject();
        }
        catch (Exception e){
            e.printStackTrace();
            throw e;
        }

    }
    @Test
    @Parameters({"testSuite","eanRow"})
    public void rejectionStatusCheck(String testSuite,int eanRow) throws Exception{
        try {
            Thread.sleep(5000);
            SPHomePage.selectCertified();
            boolean verifyEan=certifiedPage.validateGtinAndSubCode(testSuite,eanRow,"Brandbank Captured Data Rejected");
            Assert.assertEquals(verifyEan,true);
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
