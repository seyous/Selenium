package org.brandbank.tests.retailerreports;

import com.aventstack.extentreports.Status;
import org.brandbank.libs.PropertyManager;
import org.brandbank.pages.LoginPage;
import org.brandbank.pages.retailerreports.RetailerReportsHomePage;
import org.brandbank.tests.TestBaseSetup;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class RetailerReportsTest extends TestBaseSetup {
    LoginPage loginPage;
    PropertyManager propertyManager;
    RetailerReportsHomePage retailerReportsHomePage;

    public RetailerReportsTest(){
        loginPage = new LoginPage(driver);
        propertyManager = new PropertyManager();
        retailerReportsHomePage = new RetailerReportsHomePage(driver);
    }

    @Test
    public void RetailerReports() {
        Assert.assertEquals(retailerReportsHomePage.checkMainTable(),"Retailer Name");
        retailerReportsHomePage.clickOnAllCategories();
        Assert.assertEquals(retailerReportsHomePage.checkCategoriesTable(),"Matched Type");
        retailerReportsHomePage.clickOnMatchedType();
        Assert.assertEquals(retailerReportsHomePage.checkMatchedTable(),"Require Action");
        retailerReportsHomePage.clickOnRetailerSupplier();
        Assert.assertEquals(retailerReportsHomePage.checkUnknownTable(),"Retailer UID");
        System.out.println("Retailer Reports test ran successfully");
        extentTest.log(Status.PASS,"Retailer Reports test ran successfully");
        //retailerReportsHomePage.logOut();
    }

    @AfterMethod
    public void afterMethod(ITestResult result)
    {
        if(!result.isSuccess())
            System.setProperty("TestStatus","false");
    }
}
