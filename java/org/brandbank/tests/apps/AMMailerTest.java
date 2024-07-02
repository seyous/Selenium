package org.brandbank.tests.apps;

import com.aventstack.extentreports.Status;
import org.brandbank.libs.PropertyManager;
import org.brandbank.pages.LoginPage;
import org.brandbank.pages.apps.AppsLoginPage;
import org.brandbank.pages.apps.TasklistSearchAndAssignPage;
import org.brandbank.pages.apps.AMMailingsPage;
import org.brandbank.tests.TestBaseSetup;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class AMMailerTest extends TestBaseSetup {
    LoginPage loginPage;
    AppsLoginPage appsLoginPage;
    TasklistSearchAndAssignPage tasklistSearchAndAssignPage;
    AMMailingsPage amMailingsPage;
    public AMMailerTest(){
        loginPage = new LoginPage(driver);
        appsLoginPage=new AppsLoginPage(driver);
        tasklistSearchAndAssignPage =new TasklistSearchAndAssignPage(driver);
        amMailingsPage =new AMMailingsPage(driver);
    }

    @Test
    public void setIntervalApps() throws Exception{
        try {
            appsLoginPage.validateLogin();
            tasklistSearchAndAssignPage.selectTasklist("AM - Mailings");
            amMailingsPage.setInterval();
            appsLoginPage.logOut();
            extentTest.log(Status.PASS,"Logged out App successfully");
        }
        catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    @Test
    public void disableMailerApps() throws Exception{
        try {
            appsLoginPage.validateLogin();
            tasklistSearchAndAssignPage.selectTasklist("AM - Mailings");
            amMailingsPage.disableMailer();
        }
        catch (Exception e) {
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
