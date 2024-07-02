package org.brandbank.pages.apps;

import com.aventstack.extentreports.Status;
import org.brandbank.base.BaseClass;
import org.brandbank.libs.BrowserActions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class AMMailingsPage extends BaseClass {
    BrowserActions browserActions;
    public static WebDriver driver;
    public AMMailingsPage(WebDriver driver) {
        this.driver = driver;
        browserActions = new BrowserActions(this.driver);
        PageFactory.initElements(driver,this);
    }
    By mailing=By.xpath("//a[normalize-space()='AM - Mailings']");
    By newTemplateAvailable = By.xpath("//div[@id='rightPaneTopContentSection']/descendant::div[text()='Connect - New Template Available' and @class='slick-cell l2 r2']");
    By runInterval=By.xpath("//div[@id='mailing-content']//table//td[text()='Run interval [mins] ']/following-sibling::td/input[@type='text']");
    By sendInterval=By.xpath("//div[@id='mailing-content']//table//td[text()='Send interval [hrs] ']/following-sibling::td/input[@type='text']");
    By saveBtn=By.xpath("//img[@id='saveBtn']");
    By contextBtn=By.xpath("//div[@id='rightPaneTopContentSection']/descendant::div[text()='Connect - New Template Available'][2]/parent::div//button[text()='...']");
    By enableBtn=By.xpath("//body/descendant::span[text()='Enable']");
    By yesBtn=By.xpath("//button[@id='confirmation-yes']");
    By checkNoBtn=By.xpath("//div[@id='rightPaneTopContentSection']/descendant::div[text()='Connect - New Template Available'][1]/parent::div//span[contains(text(),'no')]");
    By checkYesBtn=By.xpath("//div[@id='rightPaneTopContentSection']/descendant::div[text()='Connect - New Template Available'][1]/parent::div//span[contains(text(),'yes')]");
    By noBtn=By.xpath("//button[@id='confirmation-no']");
    By disableBtn=By.xpath("//span[text()='Disable']");

    public void setInterval() throws Exception{
        try {
            browserActions.waitForElementToBeVisible(newTemplateAvailable);
            browserActions.click(newTemplateAvailable);
            System.out.println("clicked on New Template Available");
            extentTest.log(Status.INFO,"Clicked on New Template Available");
            BaseClass.driver.findElement(runInterval).clear();
            browserActions.sendKeys(runInterval, String.valueOf(1));
            BaseClass.driver.findElement(sendInterval).clear();
            browserActions.sendKeys(sendInterval, String.valueOf(0));
            browserActions.click(saveBtn);
            System.out.println("Interval has been set and saved");
            extentTest.log(Status.INFO,"Interval has been set and saved");
            Thread.sleep(5000);
            browserActions.click(contextBtn);
            browserActions.click(enableBtn);
            browserActions.waitForElementToBeVisible(yesBtn);
            browserActions.click(yesBtn);
            System.out.println("Connect - new template available enabled");
            extentTest.log(Status.PASS,"Connect - new template available enabled");
            Thread.sleep(2000);
            browserActions.waitForElementToBeVisible(checkYesBtn);
        }
        catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }
    public void disableMailer() throws Exception {
        try {
            browserActions.waitForElementToBeVisible(newTemplateAvailable);
            browserActions.click(newTemplateAvailable);
            System.out.println("clicked on New Template Available");
            extentTest.log(Status.INFO,"Clicked on New Template Available");
            BaseClass.driver.findElement(runInterval).clear();
            browserActions.sendKeys(runInterval, String.valueOf(10));
            BaseClass.driver.findElement(sendInterval).clear();
            browserActions.sendKeys(sendInterval, String.valueOf(48));
            browserActions.click(saveBtn);
            System.out.println("Connect - new template available disabled");
            extentTest.log(Status.PASS,"Connect - new template available disabled");
            Thread.sleep(2000);
            browserActions.waitForElementToBeVisible(checkNoBtn);
        }
        catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

}
