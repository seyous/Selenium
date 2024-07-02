package org.brandbank.pages.boxr;

import com.aventstack.extentreports.Status;
import org.brandbank.base.BaseClass;
import org.brandbank.libs.BrowserActions;
import org.checkerframework.checker.index.qual.PolyUpperBound;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;


public class DrawPage extends BaseClass{
    BrowserActions browserActions;
    public static WebDriver driver;
    public DrawPage(WebDriver driver) {
        this.driver = driver;
        browserActions = new BrowserActions(this.driver);
        PageFactory.initElements(driver,this);
    }
    By nutrition=By.xpath("//p[normalize-space()='Nutrition']");
    By ingredients=By.xpath("//p[normalize-space()='Ingredients']");
    By brand=By.xpath("//p[normalize-space()='Brand']");
    By addresses=By.xpath("//p[normalize-space()='Addresses']");
    By productName=By.xpath("//p[normalize-space()='Product Name']");
    By cookingInstructions=By.xpath("//p[normalize-space()='Cooking Instructions']");
    By marketing=By.xpath("//p[normalize-space()='Marketing']");
    By canvas=By.xpath("//canvas[@id='canvas']");
    By drawHeader=By.xpath("//h2[normalize-space()='draw']");
    By drawBtn=By.xpath("//button[@title='Draw']");
    By resetBtn=By.xpath("//div[contains(text(),'Reset')]");
    By moveBtn=By.xpath("//button[@title='Move']");
    By submit=By.xpath("//button[@title='Submit']");
    By loading = By.xpath("//div[@class='message']");
    By flagBtn = By.xpath("//button[@title='Flags']");
    By flagsHeader = By.xpath("//h3[normalize-space()='Product Flags']");
    By testFlagCheckbox = By.xpath("//label[normalize-space()='Test']");
    By saveBtn = By.xpath("//button[normalize-space()='Save']");
    
    public void setFlag() throws Exception{
        try {
            browserActions.waitForElementToBeVisible(drawHeader);
            Thread.sleep(3000);
            browserActions.click(flagBtn);
            browserActions.waitForElementToBeVisible(flagsHeader);
            browserActions.click(testFlagCheckbox);
            browserActions.click(saveBtn);
            Thread.sleep(5000); // wait for flag set notification to disappear
        }
        catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public void drawRequiredSegments() throws Exception {
        try {
            Thread.sleep(1000);
            browserActions.click(addresses);
            browserActions.click(drawBtn);
            browserActions.mouseDragToSelect(canvas,-660,-320,250,140);
            browserActions.click(moveBtn);
            browserActions.click(marketing);
            browserActions.click(drawBtn);
            browserActions.mouseDragToSelect(canvas,-470,-390,130,65);
            browserActions.click(moveBtn);
            browserActions.click(cookingInstructions);
            browserActions.click(drawBtn);
            browserActions.mouseDragToSelect(canvas,-680,-70,270,160);
            browserActions.click(moveBtn);
            browserActions.click(nutrition);
            browserActions.click(drawBtn);
            browserActions.mouseDragToSelect(canvas,-680,200,270,160);
            browserActions.click(moveBtn);
            browserActions.click(brand);
            browserActions.click(drawBtn);
            browserActions.mouseDragToSelect(canvas,-280,-340,270,160);
            browserActions.click(moveBtn);
            browserActions.click(ingredients);
            browserActions.click(drawBtn);
            browserActions.mouseDragToSelect(canvas,-280,-70,270,160);
            browserActions.click(moveBtn);
            browserActions.click(productName);
            browserActions.click(drawBtn);
            browserActions.mouseDragToSelect(canvas,-280,200,270,160);
            browserActions.click(moveBtn);
        }
        catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public void getDimension() throws InterruptedException {
        browserActions.waitForElementToDisappear(loading);
        browserActions.waitForElementToBeVisible(drawHeader);
        browserActions.waitForElementToBeVisible(canvas);
        browserActions.click(resetBtn);
        browserActions.click(drawBtn);
        browserActions.click(canvas);
        Thread.sleep(2000);
        Dimension d1 =BaseClass.driver.manage().window().getSize();
        System.out.println("Window size is "+ d1.getWidth()+" "+ d1.getHeight());
        extentTest.log(Status.INFO,"Window size is "+ d1.getWidth()+" "+ d1.getHeight());
        Dimension d2 =BaseClass.driver.findElement(canvas).getSize();
        System.out.println("Canvas size is "+ d2.getWidth()+" "+ d2.getHeight());
        extentTest.log(Status.INFO,"Canvas size is "+ d2.getWidth()+" "+ d2.getHeight());
    }

    public void submitSegment(){
          browserActions.scrollToView(submit);
          browserActions.click(submit);
    }

    public void drawForCICD() throws InterruptedException {
        browserActions.waitForElementToDisappear(loading);
        browserActions.waitForElementToBeVisible(drawHeader);
        browserActions.waitForElementToBeVisible(canvas);
        browserActions.click(resetBtn);
        browserActions.click(drawBtn);
        browserActions.click(canvas);
        Thread.sleep(3000);
        Dimension d1 =BaseClass.driver.manage().window().getSize();
        System.out.println("Window size is "+ d1.getWidth()+" "+ d1.getHeight());
        extentTest.log(Status.INFO,"Window size is "+ d1.getWidth()+" "+ d1.getHeight());
        Dimension d2 =BaseClass.driver.findElement(canvas).getSize();
        System.out.println("Canvas size is "+ d2.getWidth()+" "+ d2.getHeight());
        extentTest.log(Status.INFO,"Canvas size is "+ d2.getWidth()+" "+ d2.getHeight());
        browserActions.click(addresses);
        browserActions.click(drawBtn);
        browserActions.mouseDragToSelect(canvas,-680,-420,270,160);
        browserActions.click(moveBtn);
        browserActions.click(cookingInstructions);
        browserActions.click(drawBtn);
        browserActions.mouseDragToSelect(canvas,-680,-150,270,160);
        browserActions.click(moveBtn);
        browserActions.click(nutrition);
        browserActions.click(drawBtn);
        browserActions.mouseDragToSelect(canvas,-680,100,270,160);
        browserActions.click(moveBtn);
        browserActions.click(brand);
        browserActions.click(drawBtn);
        browserActions.mouseDragToSelect(canvas,-280,-420,270,160);
        browserActions.click(moveBtn);
        browserActions.click(ingredients);
        browserActions.click(drawBtn);
        browserActions.mouseDragToSelect(canvas,-280,-150,270,160);
        browserActions.click(moveBtn);
        browserActions.click(productName);
        browserActions.click(drawBtn);
        browserActions.mouseDragToSelect(canvas,-280,100,270,160);
        browserActions.click(moveBtn);
        browserActions.scrollToView(submit);
        browserActions.click(submit);
    }
}
