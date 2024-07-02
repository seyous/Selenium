package org.brandbank.tests.automatedMailer;

import com.aventstack.extentreports.Status;
import org.brandbank.libs.PropertyManager;
import org.brandbank.pages.LoginPage;
import org.brandbank.pages.apps.AppsLoginPage;
import org.brandbank.pages.automatedMailer.SearchEmailInStgPage;
import org.brandbank.tests.TestBaseSetup;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class ValidateMailInStgTest extends TestBaseSetup {
    SearchEmailInStgPage searchEmailInStgPage;
    LoginPage loginPage;
    AppsLoginPage appsLoginPage;
    public ValidateMailInStgTest(){
        searchEmailInStgPage =new SearchEmailInStgPage(driver);
        appsLoginPage=new AppsLoginPage(driver);
        loginPage=new LoginPage(driver);
    }

    @Test
    public void searchMail() {
        try {
            searchEmailInStgPage.resultFileCheck();
            String destinationPath = "\\\\mgt-stg-vm1\\Incoming$";
            boolean mailFound = searchEmailInStgPage.searchMail(destinationPath,5,"svc.autotestb","New Connect Template");
            Assert.assertEquals(mailFound,true);
            extentTest.log(Status.PASS,"Mail Found");
        }
        catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    public void validateMail() throws Exception{
        try {
            boolean urlexists = searchEmailInStgPage.verifyStgUrlInMail();
            Assert.assertEquals(urlexists, true);
            extentTest.log(Status.PASS,"stg url in mail exists");
        }
        catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    @Test
    public void logOutOfApps(){
        appsLoginPage.logOut();
    }
    @AfterMethod
    public void afterMethod(ITestResult result)
    {
        if(!result.isSuccess())
            System.setProperty("TestStatus","false");
    }
}
