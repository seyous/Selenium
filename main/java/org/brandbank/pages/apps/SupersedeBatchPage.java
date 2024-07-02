package org.brandbank.pages.apps;

import com.aventstack.extentreports.Status;
import org.brandbank.base.BaseClass;
import org.brandbank.libs.BrowserActions;
import org.brandbank.libs.CSVDataManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class SupersedeBatchPage extends BaseClass {
    BrowserActions browserActions;
    CSVDataManager csvDataManager;

    public static WebDriver driver;
    public SupersedeBatchPage(WebDriver driver) {
        this.driver = driver;
        browserActions = new BrowserActions(this.driver);
        csvDataManager=new CSVDataManager();
        PageFactory.initElements(driver,this);
    }
    By supersedeBatch=By.xpath("//span[normalize-space()='Supersede Batch']");
    By comments=By.xpath("//textarea[@id='supersede-comment']");
    By supersedeProductWindow=By.xpath("//div[@class='fancybox-inner']");
    By confirmSupersede=By.xpath("//input[@id='confirm-supersede']");
    By notes= By.xpath("//span[@id='rightPaneBottomContentSectionHeader']");
    By supersedeReason=By.xpath("//select[@id='supersede-reason']");
    public void supersedeBatch(String batchId) throws Exception{
        try {
            By contextBtn = By.xpath("//span[@class='slick-itemSelected'][normalize-space()='" + batchId + "']//following::div//button[@class='contextbutton']");
            browserActions.waitForElementToBeVisible(notes);
            browserActions.waitForElementToBeVisible(contextBtn);
            browserActions.click(contextBtn);
            browserActions.waitForElementToBeVisible(supersedeBatch);
            browserActions.click(supersedeBatch);
            browserActions.waitForElementToBeVisible(supersedeProductWindow);
            browserActions.selectFromDropdown(supersedeReason, "System Issue");
            browserActions.waitForElementToBeVisible(comments);
            Thread.sleep(2000);
            browserActions.sendKeysToUpload(comments, "Automation Testing");
            browserActions.click(confirmSupersede);
            System.out.println("superseded batch successfully");
            extentTest.log(Status.INFO,"superseded batch successfully");
        }
        catch (Exception e){
            e.printStackTrace();
            throw  e;
        }
    }
}
