package org.brandbank.pages.crm;

import org.apache.commons.lang3.RandomStringUtils;
import org.brandbank.libs.BrowserActions;
import org.brandbank.libs.CSVDataManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class CRMHomePage {

    BrowserActions browserActions;
    CSVDataManager csvDataManager;

    public static WebDriver driver;
    public CRMHomePage(WebDriver driver) {
        this.driver = driver;
        browserActions = new BrowserActions(this.driver);
        csvDataManager=new CSVDataManager();
        PageFactory.initElements(driver,this);
    }

    By title = By.xpath("//span[contains(text(),'Nielsen Brandbank CRM')]");
    By search = By.xpath("//button[@id='btnSearch']");
    By searchField = By.xpath("//input[@id='txtSearch']");
    By subscriberCode = By.xpath("//table[@id='cntMain_grdEnabled']/tbody/tr/td[3]");
    By commissionTestAccount = By.xpath("//table[@id='cntMain_grdEnabled']/tbody/tr/td[1]/a[contains(text(),'Commission Test Account')]");
    By plusIcon = By.xpath("//a[@id='cntMain_ctl01_lnkAddCall']");
    By date = By.xpath("//table[@id='tblWidgetCallAbstract']/tbody/tr[1]/td[2]");
    By subject = By.xpath("//table[@id='tblWidgetCallAbstract']/tbody/tr[1]/td[7]");
    By callDetails = By.xpath("//div[@class='widget']//ul/li/a");
    By subjectTextBox = By.xpath("//div[@class='widget']//table//input[@id='cntMain_ucAddCall_txtSubject']");
    By description = By.xpath("//div[@class='widget']//table//textarea[@id='cntMain_ucAddCall_txtCallContent']");
    By submit =By.xpath("//div[@class='widget']//table//input[@name='ctl00$cntMain$ucAddCall$btnAdd']");
    String iframe="//iframe[@id='sb-player']";

    public String verifyTitle(){
        String title = browserActions.getPageUrl();
        title = title.substring(0,title.indexOf("."));
        return title;
    }

    public String getCRMTitle() {
       String titleText = browserActions.getElementText(title);
       return titleText.trim();
    }

    public void clickSearch() {
        browserActions.click(search);
    }

    public void enterSubCode(String subCode){
        browserActions.sendKeys(searchField, subCode);
    }

    public String getSubscriberCode() {
        return browserActions.getElementText(subscriberCode);
    }

    public void selectCommissionTestAccount() {
        browserActions.click(commissionTestAccount);
    }

    public void selectPlusIcon()  {
        browserActions.click(plusIcon);
    }

    public String getCRMDate() {
        return browserActions.getElementText(date);
    }

    public String getCRMSubject() {
        return browserActions.getElementText(subject);
    }

    public String getCallDetailsText() {
        browserActions.waitForFrameToBeAvailable(iframe);
        return browserActions.getElementText(callDetails);
    }

    public void enterSubject(String text) {
        browserActions.sendKeys(subjectTextBox,text);
    }

    public void enterDescription(String text) {
        browserActions.sendKeys(description,text);
    }

    public void clickOnSubmit() {
        browserActions.click(submit);

    }

   public String generateRandomString() {
       return RandomStringUtils.randomAlphabetic(15);
   }

}
