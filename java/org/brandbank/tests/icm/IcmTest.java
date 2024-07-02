package org.brandbank.tests.icm;

import com.aventstack.extentreports.Status;
import org.brandbank.libs.CSVDataManager;
import org.brandbank.libs.PropertyManager;
import org.brandbank.pages.LoginPage;
import org.brandbank.pages.icm.IcmPage;
import org.brandbank.tests.TestBaseSetup;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class IcmTest extends TestBaseSetup {
    IcmPage icmPage;
    CSVDataManager csvDataManager;
    LoginPage loginPage;
    public IcmTest() {
        icmPage=new IcmPage(driver);
        csvDataManager=new CSVDataManager();
        loginPage = new LoginPage(driver);
    }

    @Test
    @Parameters({"testSuite","numOfProducts","copyPvid","tokenId","SkipOutsourcingYesorNo"})
    public void ICMCopyImages(String testSuite,int numOfProducts,String copyPvid,String tokenId,String SkipOutsourcingYesorNo) throws Exception{
        try {
            for(int i=1;i<=numOfProducts;i++) {
                String pvid = csvDataManager.getCSVData(testSuite, i)[5];
                icmPage.icmTaskToComplete(pvid,copyPvid,tokenId,SkipOutsourcingYesorNo);
            }
            icmPage.logOut();
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
