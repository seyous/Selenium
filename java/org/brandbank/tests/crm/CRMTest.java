package org.brandbank.tests.crm;

import com.aventstack.extentreports.Status;
import org.brandbank.libs.CSVDataManager;
import org.brandbank.libs.DateManager;
import org.brandbank.pages.crm.CRMHomePage;
import org.brandbank.pages.LoginPage;
import org.brandbank.pages.ordering.SearchResultPage;
import org.brandbank.tests.TestBaseSetup;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CRMTest extends TestBaseSetup {
    LoginPage loginPage;
    CRMHomePage crmHomePage;
    CSVDataManager csvDataManager;
    SearchResultPage searchResultPage;

    public CRMTest() {
        loginPage = new LoginPage(driver);
        csvDataManager = new CSVDataManager();
        crmHomePage =new CRMHomePage(driver);
        searchResultPage = new SearchResultPage(driver);
    }

    @Test
    @Parameters({"subCode"})
    public void CRMFlow(String subCode) throws Exception {
        try {
            String actualTitle = crmHomePage.getCRMTitle();
            Assert.assertEquals(actualTitle, "Nielsen Brandbank CRM");
            crmHomePage.clickSearch();
            crmHomePage.enterSubCode(subCode);
            crmHomePage.clickSearch();
            String actualSubscriberCode = crmHomePage.getSubscriberCode();
            Assert.assertEquals(actualSubscriberCode, subCode);
            crmHomePage.selectCommissionTestAccount();
            crmHomePage.selectPlusIcon();
            String actualCallDetails = crmHomePage.getCallDetailsText();
            Assert.assertEquals(actualCallDetails, "Call Details");
            String subjectToEnter = crmHomePage.generateRandomString();
            crmHomePage.enterSubject("Test Subject: " + subjectToEnter);
            crmHomePage.enterDescription("Test Description");
            crmHomePage.clickOnSubmit();
            String actualCRMSubject = crmHomePage.getCRMSubject();
            Assert.assertEquals(actualCRMSubject, "Test Subject: " + subjectToEnter);
            String crmCallDate = crmHomePage.getCRMDate();
            String cdate = new DateManager().getCurrentDate("dd/MM/yyyy");
            //get all chrome options
            String allOptions = System.getProperty("allOptions");
            if(allOptions.contains("headless")){
                // extract only the date from the crmCallDate so that we drop the time part - note we should also remove the substring from the assert below
                String dateOnly = crmCallDate.substring(0,crmCallDate.indexOf(" "));
                // set the format of the incoming date that we're going to change.  Use M and d only so that it doesn't matter if for example, it's 01 or 1 for the day or month
                DateTimeFormatter oldpattern = DateTimeFormatter.ofPattern("M/d/yyyy");
                // set the format of the date that we want it to be
                DateTimeFormatter newpattern = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                // now we convert our CRM date string to a date
                LocalDate datetime = LocalDate.parse(dateOnly, oldpattern);
                // now we convert that date to our new format
                String formattedDate = datetime.format(newpattern);
                //Minor tweak here removing the old substring as mentioned above
                Assert.assertEquals(formattedDate,cdate);
            }
            else {
                Assert.assertEquals(crmCallDate.substring(0, 10), cdate);
            }
            System.out.println("CRM Test Completed Successfully");
            extentTest.log(Status.PASS, "CRM Test Completed Successfully");
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
