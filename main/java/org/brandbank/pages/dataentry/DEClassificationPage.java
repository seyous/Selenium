package org.brandbank.pages.dataentry;

import com.aventstack.extentreports.Status;
import org.brandbank.libs.BrowserActions;
import org.brandbank.libs.CSVDataManager;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.Color;
import org.openqa.selenium.support.PageFactory;

import static org.brandbank.base.BaseClass.extentTest;

public class DEClassificationPage {
    BrowserActions browserActions;
    CSVDataManager csvDataManager;

    public static WebDriver driver;
    public DEClassificationPage(WebDriver driver) {
        this.driver = driver;
        browserActions = new BrowserActions(this.driver);
        csvDataManager=new CSVDataManager();
        PageFactory.initElements(driver,this);
    }
    By activityPageHeader = By.xpath("//div[@class='panelToggleDiv']/h4");
    By segmentHeader = By.xpath("//div[@class='copy-segment-editor']/h4[starts-with(text(),'Select Segments to Copy Data For')]");
    By copyDataFromAnotherProductVersion = By.xpath("//button[@class='linkButton collapsed']");
    By searchBox = By.xpath("//input[@class='form-control input-sm search_el']");
    By selectAndClose = By.xpath("//button[text()='Select and Close']");
    By reCache = By.xpath("//span[@title='Recache']");
    By selectTemplate = By.xpath("//div[@class='template-editor']/select");
    By activityComplete = By.xpath(" //i[@class='completion-bar-button material-icons ninety-to-hundred']");
    By copySegment = By.xpath("//table[@class='Segments']//th[contains(text(),'Copy Segment')]//i");
    By amendments = By.xpath("//div[contains(text(),'No amendments')]/parent::th/descendant::i");
    By loadingSpinner = By.xpath("//div[@class='loading-spinner-overlay']");
    By confirm = By.xpath("//body[@class='ReactModal__Body--open']/descendant::div[@class='Message']//span[text()='By completing this task you are confirming that all data in the Unmatched Data section will be deleted.']/parent::*/following-sibling::*[text()='Confirm']");
    By selectCategoryDD = By.xpath("(//select[@placeholder='select'])[3]");
    By selectSuCategory1DD = By.xpath("(//select[@placeholder='select'])[4]");
    By selectSuCategory2DD = By.xpath("(//select[@placeholder='select'])[5]");
    By englishLanguageCheckbox = By.xpath("(//i[contains(text(),'check_box_outline_blank')])[3]");
    By partsTextBox = By.xpath("//div[@class='hovering-button-parent form-group']//input[@type='text']");
    By englishRadioBtn = By.xpath("(//i[contains(text(),'radio_button_unchecked')])[6]");
    By dutchLanguageCheckbox =By.xpath("(//i[contains(text(),'check_box_outline_blank')])[13]");
    By showAllRadioBtn = By.xpath("(//i[contains(text(),'radio_button_unchecked')])[6]");
    By danishLanguageCheckbox =By.xpath("(//i[contains(text(),'check_box')])[2]");
    By ingredientsAndAllergyInformationButton =By.xpath("//div[contains(text(),'Ingredients and Allergy Information')]");
    By validationFailureArrow = By.xpath("//i[@class='material-icons vp-keyboard-arrow']");
    By falsePositiveButton = By.xpath("(//i[@class='material-icons clickable vp-button'])[2]");
    By headerPanel =By.xpath("//div[@class ='vp-panel-heading panel-heading']");


    public void selectActivityProduct(String radioButtonName) {
        By pvidOption = By.xpath("//div[@class='search_field_btn']/label[text()='"+radioButtonName+"']/parent::div//i");
        browserActions.click(copyDataFromAnotherProductVersion);
        browserActions.click(pvidOption);
    }
    public void enterPVIDValue(String value) {
        //browserActions.sendKeys(searchBox, Keys.CONTROL + "a");
        //browserActions.sendKeys(searchBox, String.valueOf(Keys.DELETE));
        browserActions.clearTextBox(searchBox);
        browserActions.sendKeys(searchBox, value);
        browserActions.enterKey(searchBox);
        browserActions.waitForElementToDisappear(loadingSpinner);
    }
    public void enterBatchId(String id) {
        browserActions.clearTextBox(searchBox);
        browserActions.sendKeys(searchBox, id);
        browserActions.enterKey(searchBox);
        browserActions.waitForElementToDisappear(loadingSpinner);
    }
    public String verifyActivityPageHeader() {
        return browserActions.getElementText(activityPageHeader);
    }
    public String verifySegmentsHeader() {
        String value = browserActions.getElementText(segmentHeader);
        String[] sub = value.split("\n");
        return sub[0];
    }
    public void selectPVIDRowPopUp(String value) {
        By PVIDRow = By.xpath("(//table[@class='table table-striped table-hover']//td[text()='"+value+"'])[2]");
        browserActions.waitForElementToBeVisible(PVIDRow);
        browserActions.click(PVIDRow);
        browserActions.click(selectAndClose);
        browserActions.waitForElementToDisappear(loadingSpinner);
    }
    public void clickRecache() {
        browserActions.click(reCache);
    }

    public void selectTemplate(String value) {
        browserActions.selectFromDropdown(selectTemplate,value);
    }
    public void clickActivityComplete() {
        By unmatchedDataMsg = By.xpath("//body[@class='ReactModal__Body--open']/descendant::div[@class='Message']//span[text()='By completing this task you are confirming that all data in the Unmatched Data section will be deleted.']");
        browserActions.waitForElementToDisappear(loadingSpinner);
        browserActions.click(activityComplete);
        boolean unmatchedDataExists = browserActions.isDisplayedNew(unmatchedDataMsg);
        if(unmatchedDataExists){
            browserActions.click(confirm);
        }
       // browserActions.waitForElementToDisappear(loadingSpinner);
    }
    public void selectCheckBoxCopySegment() {
        browserActions.click(copySegment);
    }
    public void selectCheckBoxNoAmendmentsTo() {
        browserActions.click(amendments);
    }

    public void selectCategories() {
        browserActions.selectFromDropdown(selectCategoryDD,"Baby");
        browserActions.selectFromDropdown(selectSuCategory1DD,"Baby Foods");
        browserActions.selectFromDropdown(selectSuCategory2DD,"Other");
    }
    public void unselectDanish(){
        browserActions.click(danishLanguageCheckbox);
    }
    public void selectDutchlanguage(){
        browserActions.click(showAllRadioBtn);
        browserActions.click(dutchLanguageCheckbox);
    }


    public void selectEnglishAndMakePrimary(){
        browserActions.click(englishLanguageCheckbox);
        browserActions.click(englishRadioBtn);
    }
    public void addParts() throws InterruptedException {
        browserActions.sendKeys(partsTextBox,"PART1");
        browserActions.sendKeys(partsTextBox, String.valueOf(Keys.ENTER));
        Thread.sleep(5000);
        browserActions.sendKeys(partsTextBox,"PART2");
        browserActions.sendKeys(partsTextBox, String.valueOf(Keys.ENTER));
        Thread.sleep(5000);
    }

    public void setFalsePositive() throws Exception {
        Thread.sleep(5000);
        browserActions.waitForElementToBeVisible(ingredientsAndAllergyInformationButton);
        browserActions.click(ingredientsAndAllergyInformationButton);
        browserActions.waitForElementToBeVisible(validationFailureArrow);
        browserActions.click(validationFailureArrow);
        browserActions.waitForElementToBeVisible(falsePositiveButton);
        browserActions.click(falsePositiveButton);
        String color = browserActions.getCssValue(headerPanel,"color");
        String hex = Color.fromString(color).asHex();
        if(hex.equalsIgnoreCase("#3c763d")){
            System.out.println("False Positive has been set successfully");
            extentTest.log(Status.PASS,"False Positive has been set successfully");
        }
        else {
            System.out.println("False Positive has Not been set successfully");
            extentTest.log(Status.FAIL,"False Positive has Not been set successfully");
        }
    }
}
