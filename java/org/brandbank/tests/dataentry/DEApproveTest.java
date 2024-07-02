package org.brandbank.tests.dataentry;

import com.aventstack.extentreports.Status;
import org.brandbank.libs.BrowserActions;
import org.brandbank.libs.CSVDataManager;
import org.brandbank.pages.LoginPage;
import org.brandbank.pages.apps.AppsLoginPage;
import org.brandbank.pages.apps.TasklistSearchAndAssignPage;
import org.brandbank.pages.dataentry.DEClassificationPage;
import org.brandbank.pages.dataentry.DEProductCapturePage;
import org.brandbank.pages.dataentry.DEQAQCCompletePage;
import org.brandbank.pages.dataentry.DETasklistSearchAndAssignPage;
import org.brandbank.tests.TestBaseSetup;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class DEApproveTest extends TestBaseSetup {
    LoginPage loginPage;
    AppsLoginPage appsLoginPage;
    DETasklistSearchAndAssignPage deTasklistSearchAndAssignPage;
    DEClassificationPage deClassificationPage;
    TasklistSearchAndAssignPage tasklistSearchAndAssignPage;
    CSVDataManager csvDataManager;
    DEQAQCCompletePage DEQAQCCompletePage;
    DEProductCapturePage deProductCapturePage;

    public DEApproveTest(){
        loginPage = new LoginPage(driver);
        csvDataManager = new CSVDataManager();
        appsLoginPage = new AppsLoginPage(driver);
        deTasklistSearchAndAssignPage = new DETasklistSearchAndAssignPage(driver);
        deClassificationPage =new DEClassificationPage(driver);
        tasklistSearchAndAssignPage = new TasklistSearchAndAssignPage(driver);
        DEQAQCCompletePage = new DEQAQCCompletePage(driver);
        deProductCapturePage = new DEProductCapturePage(driver);

    }

    @Test
    @Parameters({"testSuite","taskListName","batchIdRow","EANRow"})
    public void approveTask(String testSuite,String taskListName,int batchIdRow,int EANRow) throws Exception {
        try {
            String batchNumber = csvDataManager.getCSVData(testSuite, batchIdRow)[4];
            String gtin = csvDataManager.getCSVData(testSuite, EANRow)[1];
            String desc = csvDataManager.getCSVData(testSuite, EANRow)[2];
            tasklistSearchAndAssignPage.selectTasklist(taskListName);
            deTasklistSearchAndAssignPage.searchBatchNum(taskListName, batchNumber, gtin);
            deTasklistSearchAndAssignPage.clickOnAssignToMe();
            tasklistSearchAndAssignPage.clickOnAssignedTask(taskListName);
            //verify activity page header
            //String actualActivityPageHeader = deProductCapturePage.verifyActivityPageHeader();
            //Assert.assertEquals(actualActivityPageHeader, taDEProductCapturePage.skListName);
            String eanValidation = deProductCapturePage.eanValidation();
            String verifyLanguage=deProductCapturePage.validateLanguage();
            int ddOptions = deProductCapturePage.getDDOptionsCount();
            if (ddOptions > 2 && verifyLanguage.contains("Neutral")) {
                if(!eanValidation.equals(gtin)) {
                    deProductCapturePage.selectLanguageFromDD(gtin, desc, "Neutral");
                    System.out.println("Neutral language selected in drop down");
                    extentTest.log(Status.INFO, "Neutral language selected in drop down");
                }
                else{
                    boolean languageSelected = deProductCapturePage.verifyLanguageInDD(gtin, "Neutral");
                    Assert.assertEquals(languageSelected, true);
                    System.out.println("Neutral language selected in drop down");
                    extentTest.log(Status.INFO, "Neutral language selected in drop down");
                }
                DEQAQCCompletePage.clickActivityComplete();
                System.out.println("Neutral tasks completed successfully");
                extentTest.log(Status.PASS, "Neutral language tasks completed successfully");
            }

                if(eanValidation.equals(gtin)) {
                    boolean languageSelected = deProductCapturePage.verifyLanguageInDD(gtin, "Danish");
                    Assert.assertEquals(languageSelected, true);
                    System.out.println("Danish language selected in drop down");
                    extentTest.log(Status.INFO, "Danish language selected in drop down");
                    DEQAQCCompletePage.clickActivityComplete();
                    System.out.println("Danish tasks completed successfully");
                    extentTest.log(Status.PASS, "Danish tasks completed successfully");
                }
                if(eanValidation.equals(gtin)){
                    boolean languageSelected = deProductCapturePage.verifyLanguageInDD(gtin, "English");
                    Assert.assertEquals(languageSelected, true);
                    System.out.println("English language selected in drop down");
                    extentTest.log(Status.INFO, "English language selected in drop down");
                    DEQAQCCompletePage.clickActivityComplete();
                    System.out.println("English tasks completed successfully");
                    extentTest.log(Status.PASS, "English tasks completed successfully");
                    System.out.println("Segments approved successfully");
                    extentTest.log(Status.PASS, "Segments approved successfully");
                } else  {
                    boolean enabledDropdown = deProductCapturePage.enabledDEActivity();
                    Assert.assertEquals(enabledDropdown,true);
                    System.out.println("dropdown enabled DE QA/QC activity");
                    extentTest.log(Status.PASS, "dropdown enabled DE QA/QC activity");
                    deProductCapturePage.selectLanguageFromDD(gtin, desc, "Danish");
                    boolean languageSelected = deProductCapturePage.verifyLanguageInDD(gtin, "Danish");
                    Assert.assertEquals(languageSelected, true);
                    System.out.println("Danish language selected in drop down");
                    extentTest.log(Status.INFO, "Danish language selected in drop down");
                    DEQAQCCompletePage.clickActivityComplete();
                    System.out.println("Danish tasks completed successfully");
                    extentTest.log(Status.PASS, "Danish tasks completed successfully");
                    deProductCapturePage.selectLanguageFromDD(gtin, desc, "English (British)");
                    languageSelected = deProductCapturePage.verifyLanguageInDD(gtin, "English");
                    Assert.assertEquals(languageSelected, true);
                    System.out.println("English language selected in drop down");
                    extentTest.log(Status.INFO, "English language selected in drop down");
                    DEQAQCCompletePage.clickActivityComplete();
                    System.out.println("English tasks completed successfully");
                    extentTest.log(Status.PASS, "English tasks completed successfully");
                    System.out.println("Segments approved successfully");
                    extentTest.log(Status.PASS, "Segments approved successfully");
                }


        }
        catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    public void logOut(){
            appsLoginPage.logOut();
    }

    @AfterMethod
    public void afterMethod(ITestResult result)
    {
        if(!result.isSuccess())
            System.setProperty("TestStatus","false");
    }
}
