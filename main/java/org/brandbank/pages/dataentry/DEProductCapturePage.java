package org.brandbank.pages.dataentry;

import com.aventstack.extentreports.Status;
import org.brandbank.base.BaseClass;
import org.brandbank.libs.BrowserActions;
import org.brandbank.libs.CSVDataManager;
import org.brandbank.pages.apps.TasklistSearchAndAssignPage;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class DEProductCapturePage extends BaseClass {
    BrowserActions browserActions;
    CSVDataManager csvDataManager;
    TasklistSearchAndAssignPage tasklistSearchAndAssignPage;

    public static WebDriver driver;
    public DEProductCapturePage(WebDriver driver){
        this.driver = driver;
        browserActions = new BrowserActions(this.driver);
        csvDataManager=new CSVDataManager();
        tasklistSearchAndAssignPage=new TasklistSearchAndAssignPage(driver);
        PageFactory.initElements(driver,this);
    }
    By activityPageHeader = By.xpath("//span[@id='rightPaneTopContentSectionHeader']");
    By selectDropdown = By.xpath("//select[@placeholder='select']");
    By standardisedBrandTab = By.xpath("//span[normalize-space()='Standardised Brand']");
    By brand = By.xpath("(//label[normalize-space()='Brand'])");
    By brandTextBox = By.xpath("//input[@role='combobox']");
    By testingBrand = By.xpath("//div[normalize-space()='testing']");
    By standardisedDimensionsTab = By.xpath("//span[normalize-space()='Standard Dimensions']");
    By shelfHeight = By.xpath("//label[normalize-space()='Shelf Height']");
    By shelfHeightTxtBox = By.xpath("(//input[@id='shelfHeight'])[1]");
    By shelfWidthTxtBox = By.xpath("(//input[@id='shelfWidth'])[1]");
    By shelfDepthTxtBox = By.xpath("(//input[@id='shelfDepth'])[1]");
    By EU169Tab = By.xpath("//span[normalize-space()='EU1169']");
    By EUStatus = By.xpath("(//h4[normalize-space()='EU1169 Status'])");
    By EUStatusDD = By.xpath("(//div[@class='css-qlx21k name-text_select-field__control'])");
    By compliantOption = By.xpath("//div[@id='react-select-2-option-0']");
    By activityComplete = By.xpath("//i[@id='activity_complete_btn']");
    By loadingSpinner = By.xpath("//div[@class='loading-spinner-overlay']");
    By productDescTab = By.xpath("//span[normalize-space()='Product Description']");
    By regulatedProductName = By.xpath("//h4[normalize-space()='Regulated Product Name']");
    By regulatedProductNameTxtArea = By.xpath("//textarea[contains(@class,'rta-text-area')]");
    By functionalNameTxtBox = By.xpath("//input[@id='functionalName']");
    By variantTxtBox = By.xpath("//input[@id='variant']");
    By suppliedDescTxtBox = By.xpath("//input[@id='suppliedDescription']");
    By promotionalDesc = By.xpath("//input[@id='promotionalDescription']");
    By featuresTab = By.xpath("//span[normalize-space()='Features']");
    By featuresHeader = By.xpath("//h4[normalize-space()='Features']");
    By featuresTxtBox = By.xpath("(//input[contains(@type,'text')])[2]");
    By addCircleFeatures = By.xpath("(//i[contains(text(),'add_circle')])[1]");
    By organicCheckbox = By.xpath("//label[normalize-space()='Organic']//input[contains(@type,'checkbox')]");
    By addedMSGDD = By.xpath("(//div[contains(@class,'select-field')])[1]");
    By freeFromOption = By.xpath("//div[normalize-space()='Free From']");
    By ingredientsTab = By.xpath("//span[normalize-space()='Ingredients and Allergy Information - PART1']");
    By ingredientsHeader = By.xpath("//h4[normalize-space()='Taggable Ingredients']");
    By taggableIngredientsTextBox = By.xpath("//div[contains(@class,'tte-single-line-content-editable')]");
    By addCircleTaggableIngredients = By.xpath("//i[normalize-space()='add_circle']");
    By taggableAllergyText = By.xpath("(//div[contains(@class,'tte-content-editable')])[1]");
    By boldCheckbox = By.xpath("//label[normalize-space()='Bold']//input[contains(@type,'checkbox')]");
    By usageTab = By.xpath("//span[normalize-space()='Usage and Storage']");
    By usageHeader = By.xpath("//h4[normalize-space()='Preparation and Usage']");
    By storageTxtArea = By.xpath("(//textarea)[2]");
    By durabilityTextBox = By.xpath("(//input[contains(@type,'text')])[4]");
    By durabilityDD = By.xpath("(//div[contains(@class,'css-1fri1yt nameTextLookup-dropdown__value-container nameTextLookup-dropdown__value-container--has-value')])[2]");
    By weeksOption = By.xpath("//div[normalize-space()='Weeks']");
    By structuredNutritionTab = By.xpath("//span[normalize-space()='Structured Nutrition - PART1']");
    By structuredNutritionHeader = By.xpath("//h4[normalize-space()='Structured Nutrition EU']");
    By per100TxtBox = By.xpath("(//input[contains(@type,'text')])[2]");
    By per100DD = By.xpath("(//select[contains(@placeholder,'select')])[2]");
    By editAmountHeader = By.xpath("(//th[contains(@class,'intake-header-cell cell intake-header')][normalize-space()='Click to edit amount header'])[1]");
    By editAmountHeaderTxtbox = By.xpath("//input[@class='sn-header-component-field form-control']");
    By fatDiv = By.xpath("//td[normalize-space()='Fat']/following-sibling::td[2]");
    By fatTxtBox = By.xpath("//div[@class='sn-cell-editor']//input[@type='text']");
    By fatDD = By.xpath("//select[contains(@class,'sn-cell-component-dropdown form-control')]");
    By ofWhichSaturatesDiv = By.xpath("//td[normalize-space()='of which saturates']/following-sibling::td[2]");
    By ofWhichSaturatesDD = By.xpath("//select[contains(@class,'sn-cell-component-dropdown form-control')]");
    By saltDiv = By.xpath("//td[normalize-space()='Salt'] /following-sibling::td[2]");
    By saltDD = By.xpath("//select[contains(@class,'sn-cell-component-dropdown form-control')]");
    By frontOfPackNutritionTab = By.xpath("//span[normalize-space()='Front of Pack Nutrition - PART1']");
    By frontOfPackNutritionHeader = By.xpath("//h4[normalize-space()='Front of Pack Nutrition']");
    By referenceDD = By.xpath("//select[@id='reference-dropdown']");
    By energyDD1 = By.xpath("(//select[contains(@placeholder,'select')])[3]");
    By energyDD2 = By.xpath("(//select[contains(@placeholder,'select')])[5]");
    By energyDD3 = By.xpath("(//select[contains(@placeholder,'select')])[7]");
    By energyTxtBox = By.xpath("(//select[contains(@placeholder,'select')])[7]//following-sibling::input[1]");
    By fatRatingDD = By.xpath("//label[contains(normalize-space(),'Fat')]/parent::div//descendant::select[@id='rating-dropdown']");
    By fatDD1 = By.xpath("//label[contains(normalize-space(),'Fat')]/parent::div//descendant::div[contains(@class,'quantity')]/descendant::select");
    By fatTxtbox1 = By.xpath("//label[contains(normalize-space(),'Fat')]/parent::div//descendant::div[contains(@class,'quantity')]/descendant::input[1]");
    By fatDD2 = By.xpath("//label[contains(normalize-space(),'Fat')]/parent::div//descendant::div[contains(@class,'percentage')]/descendant::select");
    By fatTxtbox2 = By.xpath("//label[contains(normalize-space(),'Fat')]/parent::div//descendant::div[contains(@class,'percentage')]/descendant::input[1]");
    By saturatesRatingDD = By.xpath("//label[contains(normalize-space(),'Saturates')]/parent::div//descendant::select[@id='rating-dropdown']");
    By saturatesDD1 = By.xpath("//label[contains(normalize-space(),'Saturates')]/parent::div//descendant::div[contains(@class,'quantity')]/descendant::select");
    By saturatesTxtbox2 = By.xpath("//label[contains(normalize-space(),'Saturates')]/parent::div//descendant::div[contains(@class,'percentage')]/descendant::input[1]");
    By saturatesDD2 = By.xpath("//label[contains(normalize-space(),'Saturates')]/parent::div//descendant::div[contains(@class,'percentage')]/descendant::select");
    By sugarsRatingDD = By.xpath("//label[contains(normalize-space(),'Sugars')]/parent::div//descendant::select[@id='rating-dropdown']");
    By sugarsDD1 = By.xpath("//label[contains(normalize-space(),'Sugars')]/parent::div//descendant::div[contains(@class,'quantity')]/descendant::select");
    By sugarsTxtbox1 = By.xpath("//label[contains(normalize-space(),'Sugars')]/parent::div//descendant::div[contains(@class,'quantity')]/descendant::input[1]");
    By sugarsDD2 = By.xpath("//label[contains(normalize-space(),'Sugars')]/parent::div//descendant::div[contains(@class,'percentage')]/descendant::select");
    By sugarsTxtbox2 = By.xpath("//label[contains(normalize-space(),'Sugars')]/parent::div//descendant::div[contains(@class,'percentage')]/descendant::input[1]");
    By saltRatingDD = By.xpath("//label[contains(normalize-space(),'Salt')]/parent::div//descendant::select[@id='rating-dropdown']");
    By saltDD1 = By.xpath("//label[contains(normalize-space(),'Salt')]/parent::div//descendant::div[contains(@class,'quantity')]/descendant::select");
    By productDescHeader = By.xpath("//h4[normalize-space()='Product Description']");
    By productDescTxtBox = By.xpath("//input[@class='form-control']");
    By regulatedProductNameDanishTextArea = By.xpath("//textarea[contains(@class,'rta-text-area')]");
    By functionalNameTxtboxDanish = By.xpath("//input[@id='functionalName']");
    By variantTxtboxDanish = By.xpath("//input[@id='variant']");
    By suppliedDescTxtboxDanish = By.xpath("//input[@id='suppliedDescription']");
    By promotionalDescTxtboxDanish = By.xpath("//input[@id='promotionalDescription']");
    By featuresDanishHeader = By.xpath("//h4[normalize-space()='Karakteristika/Egenskaber']");
    By featuresTxtboxDanish = By.xpath("(//input[contains(@type,'text')])[2]");
    By veganCheckboxDanish = By.xpath("//label[normalize-space()='Velegnet til veganere']//input[contains(@type,'checkbox')]");
    By addedMSGDDDanish = By.xpath("//label[normalize-space()='Added MSG']/parent::div/descendant::div[contains(@class,'select-field')][1]");
    By udenOption = By.xpath("//div[normalize-space()='Uden']");
    By ingredientsHeaderDanish = By.xpath("//h4[normalize-space()='Ingredienser, der kan foretages mærkning af']");
    By ingredientsTxtboxDanish = By.xpath("//div[contains(@class,'tte-single-line-content-editable')]");
    By allergyTextDanish = By.xpath("(//div[@class='tte-content-editable'])[1]");
    By fedCheckbox = By.xpath("//label[normalize-space()='Fed']//input[contains(@type,'checkbox')]");
    By usageHeaderDanish = By.xpath("//h4[normalize-space()='Tilberedning og Anvendelse']");
    By usageTextAreaDanish = By.xpath("(//textarea)[1]");
    By ugerOption = By.xpath("//div[normalize-space()='Uger']");
    By loadingProgressBar = By.xpath("//div[@id='ajaxProgress']");
    By contextMenu=By.xpath("//i[@id='activity-context-button-icon']");
    By unassignProduct=By.xpath("//span[normalize-space()='Unassign Product']");
    By okBtn= By.xpath("//button[normalize-space()='OK']");

    public String verifyActivityPageHeader() {
        browserActions.waitForElementToDisappear(loadingSpinner);
        browserActions.waitForElementToDisappear(loadingProgressBar);
        return browserActions.getElementText(activityPageHeader);
    }

    public void selectLanguageFromDD(String EAN,String desc,String languageName) throws InterruptedException {
        String valueToSelectInDD = EAN+" "+desc+" / "+ languageName +" / Product Capture";
        Thread.sleep(5000);
        //browserActions.selectFromDropdown(selectDropdown,valueToSelectInDD);
        browserActions.click(selectDropdown);
        browserActions.sendKeys(selectDropdown,valueToSelectInDD);
        browserActions.sendKeys(selectDropdown, Keys.ENTER.toString());
    }
    public String eanValidation(){
        String EanValue=browserActions.getElementText(selectDropdown);
        String ean = EanValue.substring(0,Math.min(EanValue.length(),13));
        System.out.println(ean);
        return ean;
    }
    public String validateLanguage(){
        String LanguageValue=browserActions.getElementText(selectDropdown);
        System.out.println(LanguageValue);
        return LanguageValue;
    }
    public boolean enabledDEActivity(){
        By dropdownEnable=By.xpath("//select[@placeholder='select']");
        boolean enableCheck=browserActions.isEnabled(dropdownEnable);
        if(enableCheck){
            return true;
        }
        else{
            return false;
        }

    }
    public void selectLanguageFromDDBoxR(String EAN,String languageName){
        String valueToSelectInDD = EAN+" Test Automation Brand Test Automation Name / "+ languageName +" / Product Capture";
        browserActions.selectFromDropdown(selectDropdown,valueToSelectInDD);
    }

    public int getDDOptionsCount(){
        int options = browserActions.getDropDownListCount(selectDropdown);
        return options;
    }

    public boolean verifyLanguageInDD(String EAN,String language){
        String selectedLanguage = browserActions.getFirstSelectedOption(selectDropdown);
        return selectedLanguage.contains(language) && selectedLanguage.contains(EAN);
    }

    public boolean addDataInNeutral(String testSuite) throws Exception{
        try {
            //Segment Name - Standardised Brand
            browserActions.click(standardisedBrandTab);
            browserActions.waitForElementToBeVisible(brand);
            browserActions.clearTextBox(brandTextBox);
            browserActions.sendKeys(brandTextBox, "testing");
            Thread.sleep(5000);
            browserActions.click(testingBrand);
            System.out.println("Data added to segment Standardised Brand successfully");
            extentTest.log(Status.INFO, "Data added to segment Standardised Brand successfully");
            //Segment Name - Standard Dimensions
            browserActions.click(standardisedDimensionsTab);
            browserActions.waitForElementToBeVisible(shelfHeight);
            browserActions.clearTextBox(shelfHeightTxtBox);
            browserActions.sendKeys(shelfHeightTxtBox, "22");
            browserActions.clearTextBox(shelfWidthTxtBox);
            browserActions.sendKeys(shelfWidthTxtBox, "44");
            browserActions.clearTextBox(shelfDepthTxtBox);
            browserActions.sendKeys(shelfDepthTxtBox, "10");
            System.out.println("Data added to segment Standard Dimensions successfully");
            extentTest.log(Status.INFO, "Data added to segment Standard Dimensions successfully");
            //Segment Name - EU169
            browserActions.click(EU169Tab);
            browserActions.waitForElementToBeVisible(EUStatus);
            browserActions.click(EUStatusDD);
            browserActions.click(compliantOption);
            System.out.println("Data added to segment EU169 successfully");
            extentTest.log(Status.INFO, "Data added to segment EU169 successfully");
            browserActions.click(activityComplete);
            browserActions.waitForElementToDisappear(loadingSpinner);
            //checking weather activity page is displayed and contains our products
            String ean = csvDataManager.getCSVData(testSuite,1)[1];
            String desc= csvDataManager.getCSVData(testSuite,1)[2];
            String language = validateLanguage();
            if (!language.equals(ean+" "+desc+" / "+"Neutral")) {
                System.out.println("Neutral ean moved from product capture activity");
                return true;
            } else {
                return false;
            }
        }
        catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    public boolean addDataInEnglish(String testSuite) throws Exception {
        try {
            //Segment Name - Product Description
            browserActions.click(productDescTab);
            browserActions.waitForElementToBeVisible(regulatedProductName);
            browserActions.clearTextBox(regulatedProductNameTxtArea);
            browserActions.sendKeys(regulatedProductNameTxtArea, "Automation Testing RPN");
            browserActions.clearTextBox(functionalNameTxtBox);
            browserActions.sendKeys(functionalNameTxtBox, "Automation Testing FN");
            browserActions.clearTextBox(variantTxtBox);
            browserActions.sendKeys(variantTxtBox, "Automation Testing V");
            browserActions.clearTextBox(suppliedDescTxtBox);
            browserActions.sendKeys(suppliedDescTxtBox, "Automation Testing SD");
            browserActions.clearTextBox(promotionalDesc);
            browserActions.sendKeys(promotionalDesc, "Automation Testing PD");
            System.out.println("Data added to segment Product Description successfully");
            extentTest.log(Status.INFO, "Data added to segment Product Description successfully");
            //Segment Name - Features
            browserActions.click(featuresTab);
            browserActions.waitForElementToBeVisible(featuresHeader);
            browserActions.clearTextBox(featuresTxtBox);
            browserActions.sendKeys(featuresTxtBox, "Test Feature");
            browserActions.click(addCircleFeatures);
            browserActions.click(organicCheckbox);
            browserActions.click(addedMSGDD);
            browserActions.click(freeFromOption);
            System.out.println("Data added to segment Features successfully");
            extentTest.log(Status.INFO, "Data added to segment Features successfully");
            //Segment Name - Ingredients and Allergy Information - PART1
            browserActions.click(ingredientsTab);
            browserActions.waitForElementToBeVisible(ingredientsHeader);
            browserActions.clearTextBox(taggableIngredientsTextBox);
            browserActions.sendKeys(taggableIngredientsTextBox, "Almonds");
            browserActions.click(addCircleTaggableIngredients);
            browserActions.clearTextBox(taggableAllergyText);
            browserActions.sendKeys(taggableAllergyText, "Contains test");
            browserActions.click(boldCheckbox);
            System.out.println("Data added to segment Ingredients and Allergy Information - PART1 successfully");
            extentTest.log(Status.INFO, "Data added to segment Ingredients and Allergy Information - PART1 successfully");
            //Segment Name - Usage and Storage
            browserActions.click(usageTab);
            browserActions.waitForElementToBeVisible(usageHeader);
            browserActions.clearTextBox(storageTxtArea);
            browserActions.sendKeys(storageTxtArea, "Test Storage");
            browserActions.clearTextBox(durabilityTextBox);
            browserActions.sendKeys(durabilityTextBox, "3");
            browserActions.click(durabilityDD);
            browserActions.click(weeksOption);
            System.out.println("Data added to segment Usage and Storage successfully");
            extentTest.log(Status.INFO, "Data added to segment Usage and Storage successfully");
            //Segment Name - Structured Nutrition - PART1
            browserActions.click(structuredNutritionTab);
            browserActions.waitForElementToBeVisible(structuredNutritionHeader);
            browserActions.clearTextBox(per100TxtBox);
            browserActions.sendKeys(per100TxtBox, "20");
            browserActions.selectFromDropdown(per100DD, "ml");
            browserActions.click(editAmountHeader);
            Thread.sleep(5000);
            browserActions.clearTextBox(editAmountHeaderTxtbox);
            browserActions.sendKeys(editAmountHeaderTxtbox, "100");
            browserActions.click(fatDiv);
            Thread.sleep(2000);
            browserActions.clearTextBox(fatTxtBox);
            browserActions.sendKeys(fatTxtBox, "10");
            browserActions.sendKeys(fatDD, "<");
            browserActions.click(ofWhichSaturatesDiv);
            Thread.sleep(2000);
            browserActions.sendKeys(ofWhichSaturatesDD, "Nil");
            browserActions.click(saltDiv);
            Thread.sleep(2000);
            browserActions.sendKeys(saltDD, "Trace");
            System.out.println("Data added to segment Structured Nutrition - PART1 successfully");
            extentTest.log(Status.INFO, "Data added to segment Structured Nutrition - PART1 successfully");
            //Segment Name - Front of Pack Nutrition - PART1
            browserActions.click(frontOfPackNutritionTab);
            browserActions.waitForElementToBeVisible(frontOfPackNutritionHeader);
            browserActions.selectFromDropdown(referenceDD, "Per Portion");
            browserActions.selectFromDropdown(energyDD1, "Trace");
            browserActions.selectFromDropdown(energyDD2, "Nil");
            browserActions.selectFromDropdown(energyDD3, "<");
            browserActions.clearTextBox(energyTxtBox);
            browserActions.sendKeys(energyTxtBox, "1");
            browserActions.selectFromDropdown(fatRatingDD, "Medium");
            browserActions.selectFromDropdown(fatDD1, "<");
            browserActions.sendKeys(fatTxtbox1, "12");
            browserActions.selectFromDropdown(fatDD2, "<");
            browserActions.clearTextBox(fatTxtbox2);
            browserActions.sendKeys(fatTxtbox2, "30");
            browserActions.selectFromDropdown(saturatesRatingDD, "Low");
            browserActions.selectFromDropdown(saturatesDD1, "Trace");
            browserActions.selectFromDropdown(saturatesDD2, "<");
            browserActions.clearTextBox(saturatesTxtbox2);
            browserActions.sendKeys(saturatesTxtbox2, "2");
            browserActions.selectFromDropdown(sugarsRatingDD, "High");
            browserActions.selectFromDropdown(sugarsDD1, "<");
            browserActions.clearTextBox(sugarsTxtbox1);
            browserActions.sendKeys(sugarsTxtbox1, "30");
            browserActions.selectFromDropdown(sugarsDD2, "<");
            browserActions.clearTextBox(sugarsTxtbox2);
            browserActions.sendKeys(sugarsTxtbox2, "40");
            browserActions.selectFromDropdown(saltRatingDD, "Low");
            browserActions.selectFromDropdown(saltDD1, "Nil");
            System.out.println("Data added to segment Front of Pack Nutrition - PART1 successfully");
            extentTest.log(Status.INFO, "Data added to segment Front of Pack Nutrition - PART1 successfully");
            browserActions.click(activityComplete);
            browserActions.waitForElementToDisappear(loadingSpinner);
            //checking weather activity page is displayed and contains our products
            String ean = csvDataManager.getCSVData(testSuite,1)[1];
            String desc= csvDataManager.getCSVData(testSuite,1)[2];
            String language = validateLanguage();
            if (!language.equals(ean+" "+desc+" / "+"English")) {
                System.out.println("English ean moved from product capture activity");
                return true;
            } else {
                return false;
            }
        }
        catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }



    public boolean addDataInDanish(String testSuite) throws Exception {
            try {
                //Segment Name - Product Description
                browserActions.click(productDescTab);
                browserActions.waitForElementToBeVisible(productDescHeader);
                browserActions.clearTextBox(productDescTxtBox);
                browserActions.sendKeys(productDescTxtBox, "Automation Testing FN Danish");
                browserActions.clearTextBox(regulatedProductNameDanishTextArea);
                browserActions.sendKeys(regulatedProductNameDanishTextArea, "Automation Testing RPN Danish");
                browserActions.clearTextBox(functionalNameTxtboxDanish);
                browserActions.sendKeys(functionalNameTxtboxDanish, "Automation Testing FN Danish");
                browserActions.clearTextBox(variantTxtboxDanish);
                browserActions.sendKeys(variantTxtboxDanish, "Automation Testing V Danish");
                browserActions.clearTextBox(suppliedDescTxtboxDanish);
                browserActions.sendKeys(suppliedDescTxtboxDanish, "Automation Testing SD Danish");
                browserActions.clearTextBox(promotionalDescTxtboxDanish);
                browserActions.sendKeys(promotionalDescTxtboxDanish, "Automation Testing PD Danish");
                System.out.println("Data added to segment Product Description successfully");
                extentTest.log(Status.INFO, "Data added to segment Product Description successfully");
                //Segment Name - Features
                browserActions.click(featuresTab);
                browserActions.waitForElementToBeVisible(featuresDanishHeader);
                browserActions.clearTextBox(featuresTxtboxDanish);
                browserActions.sendKeys(featuresTxtboxDanish, "Test Feature Danish");
                browserActions.click(addCircleFeatures);
                browserActions.click(veganCheckboxDanish);
                browserActions.click(addedMSGDDDanish);
                browserActions.click(udenOption);
                System.out.println("Data added to segment Features successfully");
                extentTest.log(Status.INFO, "Data added to segment Features successfully");
                //Segment Name - Ingredients and Allergy Information - PART1
                browserActions.click(ingredientsTab);
                browserActions.waitForElementToBeVisible(ingredientsHeaderDanish);
                browserActions.clearTextBox(ingredientsTxtboxDanish);
                browserActions.sendKeys(ingredientsTxtboxDanish, "Test Ingredient Danish");
                browserActions.click(addCircleTaggableIngredients);
                browserActions.clearTextBox(allergyTextDanish);
                browserActions.sendKeys(allergyTextDanish, "Allergy Test  Danish");
                browserActions.click(fedCheckbox);
                System.out.println("Data added to segment Ingredients and Allergy Information - PART1 successfully");
                extentTest.log(Status.INFO, "Data added to segment Ingredients and Allergy Information - PART1 successfully");
                //Segment Name - Usage and Storage
                browserActions.click(usageTab);
                browserActions.waitForElementToBePresent(usageHeaderDanish);
                browserActions.clearTextBox(usageTextAreaDanish);
                browserActions.sendKeys(usageTextAreaDanish, "Test Storage Danish");
                browserActions.clearTextBox(durabilityTextBox);
                browserActions.sendKeys(durabilityTextBox, "3");
                browserActions.click(durabilityDD);
                browserActions.click(ugerOption);
                System.out.println("Data added to segment Usage and Storage successfully");
                extentTest.log(Status.INFO, "Data added to segment Usage and Storage successfully");
                browserActions.click(activityComplete);
                browserActions.waitForElementToDisappear(loadingSpinner);
                //checking weather activity page is displayed and contains our products
                boolean successFlag=false;
                By assignedTasklist = By.xpath("//div[@id='myActivity']//descendant::a[text()='Data Entry Product Capture']");
                boolean verifyActivityPackage =browserActions.isDisplayedNew(assignedTasklist);
                if(verifyActivityPackage) {
                    String ean = csvDataManager.getCSVData(testSuite, 1)[1];
                    String desc = csvDataManager.getCSVData(testSuite, 1)[2];
                    String language = validateLanguage();
                    if (!language.equals(ean + " " + desc + " / " + "Danish")) {
                        System.out.println("Danish ean moved from product capture activity");
                        successFlag= true;
                    }
                    else{
                        successFlag= false;
                        System.out.println("Danish ean not moved from product capture activity, found some errors");
                    }
                }
                else{
                    System.out.println("no DE activity product capture activity package is displayed");
                    successFlag=true;
                }
                if(successFlag){
                    return true;
                }
                else{
                    return false;
                }

            } catch (Exception e){
                e.printStackTrace();
                throw e;
            }
        }

    public void unAssignProductFromDE(){
        browserActions.waitForElementToBeVisible(contextMenu);
        browserActions.click(contextMenu);
        browserActions.click(unassignProduct);
        browserActions.waitForElementToBeVisible(okBtn);
        browserActions.click(okBtn);
        System.out.println("unassigned product from DE");
        extentTest.log(Status.PASS,"unassigned product from DE");
    }
}
