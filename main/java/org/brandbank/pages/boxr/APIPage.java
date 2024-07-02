package org.brandbank.pages.boxr;

import com.aventstack.extentreports.Status;
import org.brandbank.base.BaseClass;
import org.brandbank.libs.BrowserActions;
import org.brandbank.libs.CSVDataManager;
import org.brandbank.libs.DateManager;
import org.brandbank.libs.FileHandling;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import java.io.BufferedReader;
import java.io.FileReader;

public class APIPage extends BaseClass {
    BrowserActions browserActions;
    public static WebDriver driver;
    public static String externalUID;
    FileHandling fileHandling;
    public APIPage(WebDriver driver) {
        this.driver = driver;
        browserActions = new BrowserActions(this.driver);
        PageFactory.initElements(driver,this);
        fileHandling=new FileHandling();
    }
    By authenticationGetBtn = By.xpath("//button[contains(@aria-label,'getSecurityToken')]");
    By tryBtn = By.xpath("//button[normalize-space()='Try it out']");
    By accessKeyTxtBox = By.xpath("//input[@placeholder='accessKey']");
    By accountIdTxtBox = By.xpath("//input[@placeholder='accountId']");
    By executeAuthenticationBtn = By.xpath("//div[contains(@id,'getSecurityToken')]/descendant::div[@class='execute-wrapper']/button");
    By status = By.xpath("//div[contains(@id,'getSecurityToken')]/descendant::div[@class='responses-wrapper']/descendant::tr[@class='response']/td[normalize-space()='200']");
    By v2SubmitBtn = By.xpath("//div[contains(@id,'v2_submit')]");
    By productsExportBtn = By.xpath("//div[contains(@id,'products_export')]");
    By bodyTextArea = By.xpath("//textarea[@class='body-param__text']");
    By executeRequest = By.xpath("//div[contains(@id,'v2_submit')]/descendant::div[@class='execute-wrapper']/button");
    By executeExport = By.xpath("//div[contains(@id,'products_export')]/descendant::div[@class='execute-wrapper']/button");
    By tokenSpanTag = By.xpath("//pre[@class='microlight']//code[@class='language-json']//span[5]");
    By authorizeBtn = By.xpath("//button[@class='btn authorize unlocked']");
    By tokenTxtBox = By.xpath("//input[@aria-label='auth-bearer-value']");
    By submitTokenBtn = By.xpath("//button[@type='submit']");
    By closeBtn = By.xpath("//button[normalize-space()='Close']");
    By ingestResponseStatus = By.xpath("//div[contains(@id,'v2_submit')]/descendant::div[@class='responses-wrapper']/descendant::tr[@class='response']/td[normalize-space()='200']");
    By externalUIDTxtBox = By.xpath("//input[@placeholder='externalUId']");
    By exportResponseStatus = By.xpath("//div[contains(@id,'products_export')]/descendant::div[@class='responses-wrapper']/descendant::tr[@class='response']/td[normalize-space()='200']");

    public String verifyTitle(){
        String title = browserActions.getPageTitle();
        return title;
    }

    public void getToken(String accessKey, String accountId){
        browserActions.click(authenticationGetBtn);
        browserActions.click(tryBtn);
        browserActions.sendKeys(accessKeyTxtBox,accessKey);
        browserActions.sendKeys(accountIdTxtBox,accountId);
        browserActions.moveToElement(executeAuthenticationBtn);
        browserActions.click(executeAuthenticationBtn);
        browserActions.waitForElementToBeVisible(status);
        String token = browserActions.getElementText(tokenSpanTag);
        String newToken = token.replaceAll("\"","");
        System.out.println(newToken);
        browserActions.click(authorizeBtn);
        browserActions.sendKeys(tokenTxtBox,newToken);
        browserActions.click(submitTokenBtn);
        browserActions.click(closeBtn);
    }

    public void submitV2Request(String testSuite,String sourceFilePath,String targetFilePath) throws Exception {
        try {
            browserActions.click(v2SubmitBtn);
            browserActions.click(tryBtn);
            String date = new DateManager().getCurrentDate("MMddHHmmss");
            externalUID = date;
            System.out.println(externalUID);
            extentTest.log(Status.INFO,"ExternalUID " + externalUID);
            CSVDataManager.updateCSV(testSuite, 1, 1, 0, externalUID);
            fileHandling.modifyFile(sourceFilePath, targetFilePath, "999999", externalUID);
            BufferedReader br = new BufferedReader(new FileReader(targetFilePath));
            String oldContent = "";
            String line = br.readLine();
            while (line != null) {
                oldContent = oldContent + line + System.lineSeparator();
                line = br.readLine();
            }
            browserActions.clearTextBox(bodyTextArea);
            browserActions.sendKeysToUpload(bodyTextArea, oldContent);
            browserActions.click(executeRequest);
            browserActions.waitForElementToBeVisible(ingestResponseStatus);
        }
        catch (Exception e){
            e.printStackTrace();
            throw  e;
        }
    }

    public void exportRequest(String externalUID){
            browserActions.click(productsExportBtn);
            browserActions.click(tryBtn);
            browserActions.clearTextBox(externalUIDTxtBox);
            browserActions.sendKeys(externalUIDTxtBox,externalUID);
            browserActions.click(executeExport);
            browserActions.waitForElementToBeVisible(exportResponseStatus);
    }
}
