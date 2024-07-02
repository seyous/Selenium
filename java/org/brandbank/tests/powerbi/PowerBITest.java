package org.brandbank.tests.powerbi;

import com.aventstack.extentreports.Status;
import org.brandbank.libs.PropertyManager;
import org.brandbank.pages.LoginPage;
import org.brandbank.pages.powerbi.PowerBIHomepage;
import org.brandbank.tests.TestBaseSetup;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class PowerBITest extends TestBaseSetup {
    LoginPage loginPage;
    PropertyManager propertyManager;
    PowerBIHomepage powerBIHomepage;

    public PowerBITest() {
        loginPage = new LoginPage(driver);
        propertyManager = new PropertyManager();
        powerBIHomepage = new PowerBIHomepage(driver);
    }

    @Test
    @Parameters({"report"})
    public void powerBI(String report) {
        powerBIHomepage.searchReport(report);
        String searchResult = powerBIHomepage.verifyReport(report);
        Assert.assertEquals(searchResult,report);
        powerBIHomepage.openReport(report);
        powerBIHomepage.runReport();
        boolean results = powerBIHomepage.validateResults();
        Assert.assertEquals(results,true);
        System.out.println("validateResults Complete");
        extentTest.log(Status.PASS,"validateResults Complete");
    }
}
