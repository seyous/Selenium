package org.brandbank.tests.productmeasurer;

import com.aventstack.extentreports.Status;
import org.brandbank.libs.PropertyManager;
import org.brandbank.pages.LoginPage;
import org.brandbank.pages.productMeasurer.ProductMeasurerPage;
import org.brandbank.tests.TestBaseSetup;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.Properties;

public class ProductMeasurerTest extends TestBaseSetup {
    ProductMeasurerPage productMeasurerPage;
    LoginPage loginPage;
    public ProductMeasurerTest() {
        loginPage = new LoginPage(driver);
        productMeasurerPage =new ProductMeasurerPage(driver);
    }

    @Test
    @Parameters({"testSuite","numOfProducts"})
    public void addMeasures(String testSuite,int numOfProducts) throws Exception{
        try {
            productMeasurerPage.addData(testSuite, numOfProducts);
            productMeasurerPage.logOut();
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
