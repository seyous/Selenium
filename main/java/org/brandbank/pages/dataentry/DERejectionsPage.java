package org.brandbank.pages.dataentry;

import com.aventstack.extentreports.Status;
import org.brandbank.base.BaseClass;
import org.brandbank.libs.BrowserActions;
import org.brandbank.libs.CSVDataManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
public class DERejectionsPage extends BaseClass {
    BrowserActions browserActions;
    CSVDataManager csvDataManager;

    public static WebDriver driver;
    public DERejectionsPage(WebDriver driver){
        this.driver = driver;
        browserActions = new BrowserActions(this.driver);
        csvDataManager=new CSVDataManager();
        PageFactory.initElements(driver,this);
    }
    By activityPageHeader = By.xpath("//span[@id='rightPaneTopContentSectionHeader']");
    By loadingProgressBar = By.xpath("//div[@id='ajaxProgress']");
    By loadingSpinner = By.xpath("//div[@class='loading-spinner-overlay']");
    By productDescTab = By.xpath("//div[normalize-space()='Product Description']");
    By regulatedProductName = By.xpath("//h4[normalize-space()='Regulated Product Name']");
    By variantTxtBox = By.xpath("//input[@id='variant']");
    By rejectSegment = By.xpath("//nav[contains(@class,'react-contextmenu--visible')]/descendant::div[text()='Reject']");
    By rejectionNotePopUp = By.xpath("//div[contains(@class,'ReactModal__Overlay')]//descendant::h3[text()='Rejection note']");
    By rejectionReasonDD = By.xpath("//div[@class='modal-formGroup form-group']//select[@placeholder='select']");
    By rejectionComment = By.xpath("//textarea[@class='form-control']");
    By okBtn = By.xpath("//button[normalize-space()='OK']");
    By rejectBtnBottom = By.xpath("//i[normalize-space()='highlight_off']");
    By assignRejectedSegmentsPopup = By.xpath("//div[contains(@class,'ReactModal__Overlay')]//descendant::h3[text()='Assign Rejected Segments']");
    By myselfRadioBtn = By.xpath("//label[normalize-space()='Myself']/parent::div//i[contains(text(),'radio_button_unchecked')]");
    By rejectBtn = By.xpath("//button[normalize-space()='Reject']");
    By assignEnglishContextMenu = By.xpath("//td[contains(@title,'English')]/parent::tr/descendant::div[@class='context-menu-container']/descendant::i");
    By assignDanishContextMenu = By.xpath("//td[contains(@title,'Danish')]/parent::tr/descendant::div[@class='context-menu-container']/descendant::i");
    By assignNeutralContextMenu = By.xpath("//td[contains(@title,'Neutral')]/parent::tr/descendant::div[@class='context-menu-container']/descendant::i");
    By assignToMe = By.xpath("//span[normalize-space()='Assign to me']");
    By sendToDEComplete = By.xpath("//span[normalize-space()='Send to Data Entry Complete']");
    By englishLabel = By.xpath("//td[contains(@title,'English')]");
    By danishLabel = By.xpath("//td[contains(@title,'Danish')]");
    By neutralLabel = By.xpath("//td[contains(@title,'Neutral')]");
    By contextDD = By.xpath("//ul[@class='context-menu-dropdown']");
    By activityComplete = By.xpath("//i[@id='activity_complete_btn']");


    public String verifyActivityPageHeader() {
        browserActions.waitForElementToDisappear(loadingSpinner);
        browserActions.waitForElementToDisappear(loadingProgressBar);
        return browserActions.getElementText(activityPageHeader);
    }
    public void assignEnglish() throws InterruptedException {
        browserActions.waitForElementToBeVisible(englishLabel);
        browserActions.click(assignEnglishContextMenu);
        browserActions.waitForElementToBeVisible(contextDD);
        browserActions.click(assignToMe);
        System.out.println("Clicked on Assign to me option");
        extentTest.log(Status.INFO,"Clicked on Assign to me option");
        Thread.sleep(8000);
    }
    public void rejectVariant() {
        //Segment Name - Product Description
        browserActions.click(productDescTab);
        browserActions.waitForElementToBeVisible(regulatedProductName);
        //Reject Variant
        browserActions.rightClick(variantTxtBox);
        browserActions.click(rejectSegment);
        browserActions.waitForElementToBePresent(rejectionNotePopUp);
        browserActions.selectFromDropdown(rejectionReasonDD,"Formatting Error");
        browserActions.sendKeys(rejectionComment,"Test Reject");
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

    public void sendToCompleteFromRHS(){
        browserActions.waitForElementToBeVisible(danishLabel);
        browserActions.click(assignDanishContextMenu);
        browserActions.waitForElementToBeVisible(contextDD);
        browserActions.click(sendToDEComplete);
        browserActions.waitForElementToDisappear(loadingSpinner);
        browserActions.waitForElementToBeVisible(neutralLabel);
        browserActions.click(assignNeutralContextMenu);
        browserActions.waitForElementToBeVisible(contextDD);
        browserActions.click(sendToDEComplete);
        browserActions.waitForElementToDisappear(loadingSpinner);
    }
}
