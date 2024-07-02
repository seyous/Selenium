package org.brandbank.pages.apps;

import com.aventstack.extentreports.Status;
import org.brandbank.base.BaseClass;
import org.brandbank.libs.BrowserActions;
import org.brandbank.libs.CSVDataManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class ImagingTasksPage extends BaseClass {
    public static WebDriver driver;
    BrowserActions browserActions;
    CSVDataManager csvDataManager;
    public ImagingTasksPage(WebDriver driver){
        this.driver = driver;
        browserActions = new BrowserActions(this.driver);
        csvDataManager=new CSVDataManager();
        PageFactory.initElements(driver,this);
    }

    By numOfBatchesAssignedMkt = By.xpath("//span[@id='esqa-currentBatch']//span[5]");
    By numOfProductsAssignedMerch = By.xpath("//span[@id='merch-qa-productindicator']//span[6]");
    By numOfBatchesAssignedVirtim = By.xpath("//span[@id='vi-currentBatch']//span[5]");
    By nextBatchArrow = By.xpath("//img[@id='esqa-next-batch']");
    By nextProductArrowMerch = By.xpath("//img[@id='merch-qa-next-product']");
    By nextVirtimBatchArrow = By.xpath("//img[@id='vi-next-batch']");
    By approveTask = By.xpath("//img[@id='esqa-approve']");
    By approveMerch = By.xpath("//img[@id='product-approve']");
    By completeTask = By.xpath("//img[@id='esqa-submit']");
    By loadingMsg = By.xpath("//div[@id='ajaxProgress']");
    By merchImageGrid = By.xpath("//table[@class='merch-qa-imagegrid']");
    By requiresProcessingOption = By.xpath("//span[contains(text(),'Requires Processing')]");
    By T1_Image =By.xpath("//div[@title='T1 - ID Shot' and @class='underImageShotType']/parent::*/descendant::img[contains(@src,'https://assetapi')]");

    By clickToViewLink = By.xpath("//span[@id='esqa-specialInstructions-link']//a");

    public boolean verifyBatchFound(String stepName,String batchNum) throws InterruptedException {
        try {
            boolean currentBatchPresent = false;
            if(stepName.contains("Virtual Imaging")){
                By currentBatchVirtim = By.xpath("//span[@id='vi-currentBatch']//span[contains(text(),'" + batchNum + "')]") ;
                int correctVirtimBatchFlag = 0;
                String numOfVirtimBatches = browserActions.getElementText(numOfBatchesAssignedVirtim);
                numOfVirtimBatches = numOfVirtimBatches.split("/")[1].substring(0, 1);
                int numberOfVirtimBatch = Integer.parseInt(numOfVirtimBatches);
                do {
                    currentBatchPresent = browserActions.isDisplayedNew(currentBatchVirtim);
                    if (currentBatchPresent) {
                        extentTest.log(Status.INFO,"Correct batch " + batchNum + " opened in the Imaging screen");
                        System.out.println("Correct batch " + batchNum + " opened in the Imaging screen");
                        correctVirtimBatchFlag = 1;
                        return currentBatchPresent;
                    } else {
                        browserActions.click(nextVirtimBatchArrow);
                        Thread.sleep(5000);
                       // browserActions.waitForElementToDisappear(loadingMsg);
                        numberOfVirtimBatch--;
                    }
                } while ((correctVirtimBatchFlag == 0) && (numberOfVirtimBatch >= 1));
                if (correctVirtimBatchFlag == 0) {
                    extentTest.log(Status.FAIL,"Batch " + batchNum + " not found under assigned screen");
                    System.out.println("Batch " + batchNum + " not found under assigned screen");
                    return currentBatchPresent;
                }
            }
            else if (!(stepName.contains("Merch QC1"))) {
                By currentBatchMkt = By.xpath("//span[@id='esqa-currentBatch']//span[contains(text(),'" + batchNum + "')]");
                int correctBatchFlag = 0;
                String numOfBatches = browserActions.getElementText(numOfBatchesAssignedMkt);
                numOfBatches = numOfBatches.split("/")[1].substring(0, 1);
                int numberOfBatch = Integer.parseInt(numOfBatches);
                do {
                    currentBatchPresent = browserActions.isDisplayedNew(currentBatchMkt);
                    if (currentBatchPresent) {
                        extentTest.log(Status.INFO,"Correct batch " + batchNum + " opened in the Imaging screen");
                        System.out.println("Correct batch " + batchNum + " opened in the Imaging screen");
                        correctBatchFlag = 1;
                        return currentBatchPresent;
                    } else {
                        browserActions.click(nextBatchArrow);
                        //browserActions.waitForElementToDisappear(loadingMsg);
                        numberOfBatch--;
                    }
                } while ((correctBatchFlag == 0) && (numberOfBatch >= 1));
                if (correctBatchFlag == 0) {
                    extentTest.log(Status.FAIL,"Batch " + batchNum + " not found under assigned screen");
                    System.out.println("Batch " + batchNum + " not found under assigned screen");
                    return currentBatchPresent;
                }
            }
            else {
                By currentBatchMerch = By.xpath("//span[@id='merch-qa-productindicator']//span[contains(text(),'" + batchNum + "')]");
                int merchCorrectBatchFlag = 0;
                String numOfProductsStr = browserActions.getElementText(numOfProductsAssignedMerch);
                numOfProductsStr = numOfProductsStr.split("/")[1].substring(0, 1);
                int numOfProduct = Integer.parseInt(numOfProductsStr);
                do {
                    currentBatchPresent = browserActions.isDisplayedNew(currentBatchMerch);
                    if (currentBatchPresent) {
                        extentTest.log(Status.INFO,"Correct batch " + batchNum + " opened in the Imaging screen");
                        System.out.println("Correct batch " + batchNum + " opened in the Imaging screen");
                        merchCorrectBatchFlag = 1;
                        return currentBatchPresent;
                    } else {
                        browserActions.click(nextProductArrowMerch);
                       // browserActions.waitForElementToDisappear(loadingMsg);
                        numOfProduct--;
                    }
                } while ((merchCorrectBatchFlag == 0) && (numOfProduct >= 1));
                if (merchCorrectBatchFlag == 0) {
                    extentTest.log(Status.FAIL,"Batch " + batchNum + " not found under assigned screen");
                    System.out.println("Batch " + batchNum + " not found under assigned screen");
                    return currentBatchPresent;
                }
            }
            return currentBatchPresent;
        }
        catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    public boolean checkImagesOnScreen(String screenName) throws InterruptedException {
        try {
            int imagesNotPresent = 0;
            switch (screenName) {
                case "Exit Studio QA":
                    Thread.sleep(30000);
                    boolean T1ImageExists = browserActions.isDisplayedNew(T1_Image);
                    if (T1ImageExists) {
                        extentTest.log(Status.PASS,"Marketing Image present in Exit Studio Screen");
                        System.out.println("Marketing Image present in Exit Studio Screen");
                    }
                    else {
                        extentTest.log(Status.FAIL,"Marketing Image is not present in Exit Studio Screen");
                        System.out.println("Marketing Image is not present in Exit Studio Screen");
                        imagesNotPresent++;
                    }
                    break;
                case "Merch QC1.Image & Dimensions":
                    String[] merchImages = {"Planogram Top", "Planogram Front", "Planogram Right", "Planogram Back", "Planogram Left", "Planogram Bottom"};
                    browserActions.waitForElementToBeVisible(merchImageGrid);
                    Thread.sleep(30000);
                    for (String image : merchImages) {
                        By merchImageTitle =By.xpath("//img[@title='" + image + "']");
                        By merchImage = By.xpath("//img[contains(@src,'https://assetapi') and @title= '" + image + "']");
                        boolean imagePresent = browserActions.isDisplayedNew(merchImageTitle);
                        if (imagePresent) {
                            if (browserActions.isDisplayedNew(merchImage)) {
                                extentTest.log(Status.INFO,"Merch Image " + image + " succesfully verified in the image grid");
                                System.out.println("Merch Image " + image + " succesfully verified in the image grid");
                            }
                            else {
                                extentTest.log(Status.FAIL,"Merch Image " + image + " does not have an image in the placeholder");
                                System.out.println("Merch Image " + image + " does not have an image in the placeholder");
                                imagesNotPresent++;
                            }
                        } else {
                            extentTest.log(Status.FAIL,"Merch Image " + image + " not present in the image grid");
                            System.out.println("Merch Image " + image + " not present in the image grid");
                        }
                    }
                    break;
                default:
                    Thread.sleep(30000);
                    break;
            }
            if (imagesNotPresent > 0) {
                extentTest.log(Status.FAIL,"There are " + imagesNotPresent + " images missing from the " + screenName + " screen ");
                System.out.println("There are " + imagesNotPresent + " images missing from the " + screenName + " screen ");
                return false;
            } else
                return true;
        }
        catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }
    public void approveMarketingTask(String batchNum) throws InterruptedException {
        By currentBatchMkt = By.xpath("//span[@id='esqa-currentBatch']//span[contains(text(),'"+batchNum+"')]");
        By specialInstructions = By.xpath("//span[@id='esqa-specialInstructions-link']");

        boolean specialInstructionsDisplayed = browserActions.isDisplayedNew(specialInstructions);
        if(specialInstructionsDisplayed){
            browserActions.click(clickToViewLink);
            extentTest.log(Status.INFO,"Clicked to view Special Instructions");
            System.out.println("Clicked to view Special Instructions");
        }
        browserActions.click(approveTask);
        Thread.sleep(2000);
        extentTest.log(Status.INFO,"Approved the task");
        System.out.println("Approved the task");
        browserActions.click(completeTask);
       // browserActions.waitForElementToDisappear(loadingMsg);
        browserActions.waitForElementToDisappear(currentBatchMkt);
    }
    public void approveMerchQC1(String batchNum){
        By currentBatchMerch = By.xpath("//span[@id='merch-qa-productindicator']//span[contains(text(),'"+batchNum+"')]");

        browserActions.click(approveMerch);
       // browserActions.waitForElementToDisappear(loadingMsg);
        browserActions.waitForElementToDisappear(currentBatchMerch);
    }

    public void approvePostOutsourceQATask(String batchNum) throws InterruptedException {
        By currentBatchMkt = By.xpath("//span[@id='esqa-currentBatch']//span[contains(text(),'"+batchNum+"')]");
        By specialInstructions = By.xpath("//span[@id='esqa-specialInstructions-link']");

        boolean specialInstructionsDisplayed = browserActions.isDisplayedNew(specialInstructions);
        if(specialInstructionsDisplayed){
            browserActions.click(clickToViewLink);
            extentTest.log(Status.INFO,"Clicked to view Special Instructions");
            System.out.println("Clicked to view Special Instructions");
        }
        browserActions.click(approveTask);
        Thread.sleep(1000);
        browserActions.click(requiresProcessingOption);
        extentTest.log(Status.INFO,"Approved the task");
        System.out.println("Approved the task");
        browserActions.click(completeTask);
        // browserActions.waitForElementToDisappear(loadingMsg);
        browserActions.waitForElementToDisappear(currentBatchMkt);
    }
}
