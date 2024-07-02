package org.brandbank.pages.dataentry;

import com.aventstack.extentreports.Status;
import org.brandbank.base.BaseClass;
import org.brandbank.libs.BrowserActions;
import org.brandbank.libs.CSVDataManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class DEQAQCCompletePage extends BaseClass {
    BrowserActions browserActions;
    CSVDataManager csvDataManager;

    public static WebDriver driver;
    public DEQAQCCompletePage(WebDriver driver){
        this.driver = driver;
        browserActions = new BrowserActions(this.driver);
        csvDataManager=new CSVDataManager();
        PageFactory.initElements(driver,this);
    }
    By activityPageHeader = By.xpath("//span[@id='rightPaneTopContentSectionHeader']");
    By loadingProgressBar = By.xpath("//div[@id='ajaxProgress']");
    By loadingSpinner = By.xpath("//div[@class='loading-spinner-overlay']");
    By standardDimensionsTab = By.xpath("//div[normalize-space()='Standard Dimensions']");
    By shelfHeight = By.xpath("//label[normalize-space()='Shelf Height']");
    By shelfHeightTxtBox = By.xpath("(//input[@id='shelfHeight'])[1]");
    By shelfWidthTxtBox = By.xpath("(//input[@id='shelfWidth'])[1]");
    By shelfDepthTxtBox = By.xpath("(//input[@id='shelfDepth'])[1]");
    By rejectSegment = By.xpath("(//div[@role='menuitem'])[1]");
    By rejectionNotePopUp = By.xpath("//div[contains(@class,'ReactModal__Overlay')]//descendant::h3[text()='Rejection note']");
    By rejectionReasonDD = By.xpath("//div[@class='modal-formGroup form-group']//select[@placeholder='select']");
    By rejectionComment = By.xpath("//textarea[@class='form-control']");
    By okBtn = By.xpath("//button[normalize-space()='OK']");
    By rejectBtnBottom = By.xpath("//i[normalize-space()='highlight_off']");
    By assignRejectedSegmentsPopup = By.xpath("//div[contains(@class,'ReactModal__Overlay')]//descendant::h3[text()='Assign Rejected Segments']");
    By myselfRadioBtn = By.xpath("//label[normalize-space()='Myself']/parent::div//i[contains(text(),'radio_button_unchecked')]");
    By rejectBtn = By.xpath("//button[normalize-space()='Reject']");
    By assignNeutralContextMenu = By.xpath("//td[contains(@title,'Neutral')]/parent::tr/descendant::div[@class='context-menu-container']/descendant::i");
    By assignToMe = By.xpath("//span[normalize-space()='Assign to me']");
    By neutralLabel = By.xpath("//td[contains(@title,'Neutral')]");
    By activityComplete = By.xpath("//i[@id='activity_complete_btn']");
    By contextDD = By.xpath("//ul[@class='context-menu-dropdown']");
    public String verifyActivityPageHeader() {
        browserActions.waitForElementToDisappear(loadingSpinner);
        browserActions.waitForElementToDisappear(loadingProgressBar);
        return browserActions.getElementText(activityPageHeader);
    }
    public void assignNeutral() throws InterruptedException {
        browserActions.waitForElementToBeVisible(neutralLabel);
        browserActions.click(assignNeutralContextMenu);
        browserActions.waitForElementToBeVisible(contextDD);
        browserActions.click(assignToMe);
        System.out.println("Clicked on Assign to me option");
        extentTest.log(Status.INFO,"Clicked on Assign to me option");
        Thread.sleep(8000);
    }
    public void rejectDimensionHeight() {
        //Segment Name - Standard Dimensions
        browserActions.click(standardDimensionsTab);
        browserActions.waitForElementToBeVisible(shelfHeight);
        //Reject Shelf Height
        browserActions.rightClick(shelfHeightTxtBox);
        browserActions.click(rejectSegment);
        browserActions.waitForElementToBePresent(rejectionNotePopUp);
        browserActions.selectFromDropdown(rejectionReasonDD,"Missing Data");
        browserActions.sendKeys(rejectionComment,"testing");
        browserActions.click(okBtn);
        browserActions.click(rejectBtnBottom);
        browserActions.waitForElementToBePresent(assignRejectedSegmentsPopup);
        browserActions.click(myselfRadioBtn);
        browserActions.click(rejectBtn);
        browserActions.waitForElementToDisappear(loadingSpinner);
    }

    public void rejectDimensionWidth() {
        //Segment Name - Standard Dimensions
        browserActions.click(standardDimensionsTab);
        browserActions.waitForElementToBeVisible(shelfHeight);
        //Reject Shelf Width
        browserActions.rightClick(shelfWidthTxtBox);
        browserActions.click(rejectSegment);
        browserActions.waitForElementToBePresent(rejectionNotePopUp);
        browserActions.selectFromDropdown(rejectionReasonDD,"Formatting Error");
        browserActions.sendKeys(rejectionComment,"testing");
        browserActions.click(okBtn);
        browserActions.click(rejectBtnBottom);
        browserActions.waitForElementToBePresent(assignRejectedSegmentsPopup);
        browserActions.click(myselfRadioBtn);
        browserActions.click(rejectBtn);
        browserActions.waitForElementToDisappear(loadingSpinner);
    }

    public void rejectDimensionDepth() {
        //Segment Name - Standard Dimensions
        browserActions.click(standardDimensionsTab);
        browserActions.waitForElementToBeVisible(shelfHeight);
        //Reject Shelf Width
        browserActions.rightClick(shelfDepthTxtBox);
        browserActions.click(rejectSegment);
        browserActions.waitForElementToBePresent(rejectionNotePopUp);
        browserActions.selectFromDropdown(rejectionReasonDD,"Other");
        browserActions.sendKeys(rejectionComment,"testing");
        browserActions.click(okBtn);
        browserActions.click(rejectBtnBottom);
        browserActions.waitForElementToBePresent(assignRejectedSegmentsPopup);
        browserActions.click(myselfRadioBtn);
        browserActions.click(rejectBtn);
        browserActions.waitForElementToDisappear(loadingSpinner);
    }
    public void clickActivityComplete() {
        browserActions.waitForElementToDisappear(loadingSpinner);
        browserActions.click(activityComplete);
        browserActions.waitForElementToDisappear(loadingSpinner);
    }

}
