package org.brandbank.tests;

import com.aventstack.extentreports.Status;
import org.brandbank.libs.PropertyManager;
import org.brandbank.pages.LoginPage;
import org.brandbank.pages.apps.AppsLoginPage;
import org.brandbank.pages.automatedMailer.OutlookPage;
import org.brandbank.pages.blackbox.ExtractsPage;
import org.brandbank.pages.boxr.APIPage;
import org.brandbank.pages.boxr.SignInPage;
import org.brandbank.pages.boxr.WorkflowPage;
import org.brandbank.pages.crm.CRMHomePage;
import org.brandbank.pages.dataworkflow.DataWorkflowHomePage;
import org.brandbank.pages.icm.IcmPage;
import org.brandbank.pages.manualactions.ManualActionsPage;
import org.brandbank.pages.ordering.*;
import org.brandbank.pages.powerbi.PowerBIHomepage;
import org.brandbank.pages.productlibrary.PLHomePage;
import org.brandbank.pages.rejections.RejectionsPage;
import org.brandbank.pages.retailerreports.RetailerReportsHomePage;
import org.brandbank.pages.salesforce.SalesforceLoginPage;
import org.brandbank.pages.suppliedcontent.GoogleDriveValidationPage;
import org.brandbank.pages.suppliedcontent.SuppliedContentHomePage;
import org.brandbank.pages.supplierportal.*;
import org.brandbank.tests.boxr.BoxRTest;
import org.brandbank.tests.libs.RetryTests;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.Properties;


public class LoginTest extends TestBaseSetup {
    LoginPage loginPage;
    OrderProcessingPage orderProcessingPage;
    AppsLoginPage appsLoginPage;
    SearchResultPage searchResultPage;
    SubscriberDetailsPage subscriberDetailsPage;
    OrderHeaderPage orderHeaderPage;
    OrderConfirmPage orderConfirmPage;
    PurchaseOrderPage purchaseOrderPage;
    OutlookPage outlookPage;
    ExtractsPage extractsPage;
    SignInPage signInPage;
    WorkflowPage workflowPage;
    PLHomePage PLHomePage;
    CRMHomePage crmHomePage;
    DataWorkflowHomePage dataWorkflowHomePage;
    IcmPage icmPage;
    RejectionsPage rejectionsPage;
    ManualActionsPage manualActionsPage;
    PowerBIHomepage powerBIHomepage;
    RetailerReportsHomePage retailerReportsHomePage;
    SalesforceLoginPage salesforceLoginPage;
    Properties configProperties;
    SPHomePage SPHomePage;
    BoxRTest boxRTest;
    SuppliedContentHomePage SuppliedContentHomePage;
    APIPage apiPage;
    GoogleDriveValidationPage GDpage;


    public LoginTest() {
        loginPage = new LoginPage(driver);
        orderProcessingPage = new OrderProcessingPage(driver);
        searchResultPage = new SearchResultPage(driver);
        subscriberDetailsPage = new SubscriberDetailsPage(driver);
        orderHeaderPage = new OrderHeaderPage(driver);
        orderConfirmPage = new OrderConfirmPage(driver);
        purchaseOrderPage = new PurchaseOrderPage(driver);
        appsLoginPage = new AppsLoginPage(driver);
        signInPage = new SignInPage(driver);
        workflowPage = new WorkflowPage(driver);
        PLHomePage = new PLHomePage(driver);
        crmHomePage = new CRMHomePage(driver);
        dataWorkflowHomePage = new DataWorkflowHomePage(driver);
        icmPage = new IcmPage(driver);
        rejectionsPage = new RejectionsPage(driver);
        manualActionsPage = new ManualActionsPage(driver);
        powerBIHomepage = new PowerBIHomepage(driver);
        retailerReportsHomePage = new RetailerReportsHomePage(driver);
        salesforceLoginPage = new SalesforceLoginPage(driver);
        SPHomePage = new SPHomePage(driver);
        extractsPage = new ExtractsPage(driver);
        outlookPage = new OutlookPage(driver);
        signInPage = new SignInPage(driver);
        boxRTest = new BoxRTest();
        SuppliedContentHomePage = new SuppliedContentHomePage(driver);
        apiPage = new APIPage(driver);
        GDpage = new GoogleDriveValidationPage(driver);

    }

    @Test
    public void masterLogin(String moduleName, String username, String password) throws Exception {
        String env;
        configProperties = PropertyManager.getPropertiesData();
        env = configProperties.getProperty("environment");
        try {
            if (moduleName.equals("boxr")) {
                signInPage.signIn(PropertyManager.getPropertiesData().getProperty(username), PropertyManager.getPropertiesData().getProperty(password));

//                if (env.equals("prod")) {
//                    signInPage.sendCodeToVerify();
//                    boxRTest.loginToOutlook();
//                    boxRTest.enterMFA();
//                }
            } else if (moduleName.equals("outlook")) {
                outlookPage.loginToOutlook(PropertyManager.getPropertiesData().getProperty(username), PropertyManager.getPropertiesData().getProperty(password));
            } else if (moduleName.equals("supplierportal")) {
                SPHomePage.login();
                loginPage.login(PropertyManager.getPropertiesData().getProperty(username), PropertyManager.getPropertiesData().getProperty(password));
            } else if (moduleName.equals("salesforce") && env.equals("stg")) {
                salesforceLoginPage.clickOnLoginInWithBrandbankSts();
                loginPage.login(PropertyManager.getPropertiesData().getProperty(username), PropertyManager.getPropertiesData().getProperty(password));
            } else if (moduleName.equals("googleDrive")) {
            } else {
                if (!moduleName.equals("boxrapi")) {
                    loginPage.login(PropertyManager.getPropertiesData().getProperty(username), PropertyManager.getPropertiesData().getProperty(password));
                }
            }
            String actualTitle;
            switch (moduleName) {
                case "ordering":
                    actualTitle = orderProcessingPage.verifyTitle();
                    Assert.assertEquals(actualTitle, "Order Processing - Welcome");
                    break;
                case "apps":
                    actualTitle = appsLoginPage.verifyTitle();
                    Assert.assertEquals(actualTitle, "Apps Brandbank (1.9.1.0)");
                    appsLoginPage.validateLogin();
                    break;
                case "outlook":
                    actualTitle = outlookPage.verifyTitle();
                    Assert.assertEquals(actualTitle, "Email - autotestb, svc - Outlook");
                    break;
                case "blackbox":
                    actualTitle = extractsPage.verifyTitle();
                    Assert.assertEquals(actualTitle, "Black Box - Home Page");
                    break;
                case "boxr":
                    actualTitle = workflowPage.verifyTitle();
                    Assert.assertEquals(actualTitle, "Box-R 2.0 Alpha");
                    break;
                case "boxrapi":
                    actualTitle = apiPage.verifyTitle();
                    Assert.assertEquals(actualTitle, "Swagger UI");
                    break;
                case "productlibrary":
                    actualTitle = PLHomePage.verifyTitle();
                    Assert.assertEquals(actualTitle, "Brandbank - Product Library");
                    PLHomePage.verifyCookie();
                    break;
                case "crm":
                    actualTitle = crmHomePage.verifyTitle();
                    Assert.assertEquals(actualTitle, "https://crm");
                    break;
                case "dataworkflow":
                    actualTitle = dataWorkflowHomePage.verifyTitle();
                    Assert.assertEquals(actualTitle, "Data Workflow");
                    break;
                case "testharness":
                    actualTitle = icmPage.verifyTitle();
                    Assert.assertEquals(actualTitle, "Home Page — Productversion Test Harness");
                    break;
                case "rejections":
                    actualTitle = rejectionsPage.verifyTitle();
                    Assert.assertEquals(actualTitle, "Brandbank Rejections");
                    break;
                case "supplierportal":
                    actualTitle = PLHomePage.verifyTitle();
                    Assert.assertEquals(actualTitle, "Brandbank | Your global product content partner");
                    break;
                case "manualactions":
                    actualTitle = manualActionsPage.verifyTitle();
                    Assert.assertEquals(actualTitle, "Brandbank Manual Actions");
                    break;
                case "powerbi":
                    actualTitle = powerBIHomepage.verifyTitle();
                    Assert.assertEquals(actualTitle, "Home - Power BI Report Server");
                    break;
                case "retailerreports":
                    actualTitle = retailerReportsHomePage.verifyTitle();
                    Assert.assertEquals(actualTitle, "Nielsen Brandbank Retailer Reports");
                    break;
                case "salesforce":
                    actualTitle = salesforceLoginPage.verifyTitle();
                    Assert.assertEquals(actualTitle, "Home | Salesforce");
                    break;
                case "suppliedcontent":
                    actualTitle = SuppliedContentHomePage.verifyTitle();
                    Assert.assertEquals(actualTitle, "Subscriber Overview");
                    break;
            }
            System.setProperty("TestStatus", "true");
            extentTest.log(Status.PASS, "Logged into " + moduleName + " successfully");
        } catch (AssertionError | Exception e) {
            RetryTests.FailedModuleName = moduleName;
            System.setProperty("TestStatus", "false");
            extentTest.log(Status.FAIL, "Failed log in to " + moduleName);
            throw e;
        }
    }
}
