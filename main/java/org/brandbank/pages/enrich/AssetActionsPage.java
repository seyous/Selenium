package org.brandbank.pages.enrich;

import com.aventstack.extentreports.Status;
import org.brandbank.base.BaseClass;
import org.brandbank.libs.BrowserActions;
import org.brandbank.libs.CSVDataManager;
import org.brandbank.libs.DateManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class AssetActionsPage extends BaseClass {
    BrowserActions browserActions;
    CSVDataManager csvDataManager;
    public static WebDriver driver;
    public AssetActionsPage(WebDriver driver) {
        this.driver = driver;
        browserActions = new BrowserActions(this.driver);
        csvDataManager=new CSVDataManager();
        PageFactory.initElements(driver, this);
    }
    By downloadAsset=By.xpath("//i[@class='icon-download-alt']");
    By editAsset=By.xpath("//a[@id='btn-edit-asset']");
    By changeAssetStyle = By.xpath("//select[@class='form-control input-sm']");
    By doneBtn=By.xpath("//button[@id='btnOK']");
    By uploadRevisionAsset=By.xpath("//a[@id='btn-update-asset']");
    By addFileBtn=By.xpath("//parent::div[@class='col-xs-2']//input[@id='uploadFileInput']");
    By uploadBtn=By.xpath("//button[@id='btnUpload']");
    By auditLog=By.xpath("//li[@rel='tabAudit']//a[text()='Audit Log']");
    By revisionBtn=By.xpath("//li[@rel='tabRevisions']");
    By clickRevertBtn=By.xpath("//a[contains(text(),'Revert')]");
    By clickAddLink=By.xpath("//i[@id='btn-add-link']");
    By subCodeToLookup=By.xpath("//input[@id='subCodeLookup']");

    By EANTxtBox = By.xpath("//textarea[@id='EAN']");
    By triggerSearch=By.xpath("//button[@id='triggerSearchPopup']");
    By checkCreateLink=By.xpath("//tbody/tr[1]/td[@class='checkbox-cell']/span[1]");
    By clickCreateLinks=By.xpath("//button[@id='btnDone']");
    By clickRejectBtn=By.xpath("//i[@original-title='Reject Link']");
    By comments=By.xpath("//div[@class='modal-comment']//textarea");
    By acceptBtn =By.xpath("//button[@id='btnOk']");
    By unlinkProduct=By.xpath("//i[@class='icon-trash icon-1_5x']");
    By publishLink=By.xpath("//tbody/tr[1]/td[@class='publishing checkbox-cell']/span[1]");
    By publishedStatus=By.xpath("//span[contains(.,'Published')]");
    By deleteAsset=By.xpath("//a[@id='btn-delete-asset']");
    By rejectedStatus=By.xpath("//span[contains(text(),'Rejected')]");
    By changeRadioBtn=By.xpath("//input[@id='is_publice' and @type='radio']");
    By changeAssetType=By.xpath("//select[@id='ddlAssetTypes']");
    By clickPublicLink=By.xpath("//a[@id='btn-publiclink-asset']");
    By publicLinkDate=By.xpath("//input[@id='calEndDate']");
    By generatePublicLink=By.xpath("//button[normalize-space()='Generate public link']");
    By closePopUpBtn=By.xpath("//button[@class='close']");
    By ticKSelectAssetsBtn =By.xpath("//input[@id='select-asset-checkbox']");
    By selectAllAssets=By.xpath("//span[@class='allPages label fake-link'][contains(text(),'Select all assets')]");
    By yesToConfirm=By.xpath("//button[normalize-space()='Yes']");
    By deleteAllAssets=By.xpath("//a[@id='bulkBtnDeleteAssets']");
    By deleteBtn=By.xpath("//li[@class='folder smart-folder']//i[@title='Delete']");
    By reasonToEnter=By.xpath("//textarea[@id='changeReason']");
    By revertUploadBtn=By.xpath("//button[@id='btnUpload']");
    By assetPreviewBtn=By.xpath("//a[@id='btn-preview-asset']//i[@class='icon-eye-open']");
    String iFrameToEnter1 ="//iframe[@class='fancybox-iframe']";
    String iFrameToEnter2 ="//iframe[contains(@src,'/Assets/popup_SelectableProducts')]";
    By assetPreview = By.xpath("//img[contains(@alt,'Asset Preview')]");

    public void viewAsset() throws Exception {
        try {
            Thread.sleep(2000);
            browserActions.waitForElementToBeClickable(assetPreviewBtn);
            browserActions.click(assetPreviewBtn);
            Thread.sleep(6000);
            browserActions.switchToTab(0,1);
            browserActions.waitForElementToBeVisible(assetPreview);
            System.out.println("Asset previewed successfully");
            extentTest.log(Status.PASS,"Asset previewed successfully");
            browserActions.switchToTab(1,0);
        }
        catch(Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    public void downloadAssetAction() {
        browserActions.scrollToView(downloadAsset);
        browserActions.waitForElementToBeClickable(downloadAsset);
        browserActions.click(downloadAsset);
        System.out.println("Asset has been downloaded");
        extentTest.log(Status.INFO,"Asset has been downloaded");
    }

    public void editAssetAction(String assetVisiblity) throws Exception {
        try {
            browserActions.waitForElementToBeClickable(editAsset);
            browserActions.click(editAsset);
            browserActions.waitForFrameToBeAvailable(iFrameToEnter1);
            System.out.println("Swicthed to Frame");
            Thread.sleep(5000);
            if (assetVisiblity.equalsIgnoreCase("public")) {
                browserActions.selectFromDropdown(changeAssetStyle, "Lifestyle");
                browserActions.click(doneBtn);
                System.out.println("AssetStyle has changed to Lifestyle");
                extentTest.log(Status.PASS,"AssetStyle has changed to Lifestyle");
            } else {
                browserActions.waitForElementToBeClickable(changeRadioBtn);
                browserActions.clickByJavascript(changeRadioBtn);
                browserActions.selectFromDropdown(changeAssetStyle, "Lifestyle");
                browserActions.selectFromDropdown(changeAssetType, "Image");
                browserActions.click(doneBtn);
                System.out.println("Asset actions has edited to public");
                extentTest.log(Status.PASS,"Asset actions has edited to public");
            }
        }
        catch(Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    public void createPublicLink() throws Exception {
        try {
            String extendTag=new DateManager().getCurrentDate("MM/dd/yyyy");
            Thread.sleep(3000);
            browserActions.click(clickPublicLink);
            browserActions.waitForFrameToBeAvailable(iFrameToEnter1);
            Thread.sleep(2000);
            browserActions.waitForElementToBeClickable(publicLinkDate);
            browserActions.sendKeysByJavascript(publicLinkDate,extendTag);
            browserActions.click(generatePublicLink);
            Thread.sleep(2000);
            browserActions.waitForElementToBeClickable(acceptBtn);
            browserActions.click(acceptBtn);
            System.out.println("Public link has been generated");
            extentTest.log(Status.PASS,"Public link has been generated");
            Thread.sleep(3000);
            browserActions.waitForElementToBeClickable(closePopUpBtn);
            browserActions.click(closePopUpBtn);
    }
        catch(Exception e){
        e.printStackTrace();
        throw e;
    }
}
    public void UpdateAssetAction(String testSuite) throws Exception {
        try{
         Thread.sleep(3000);
        browserActions.waitForElementToBeClickable(uploadRevisionAsset);
        browserActions.click(uploadRevisionAsset);
        browserActions.waitForFrameToBeAvailable(iFrameToEnter1);
        String assetToUploadPath = System.getProperty("user.dir") + "\\src\\test\\resources\\testdata\\Enrich\\Assets\\";
        String getAssetName=assetToUploadPath+csvDataManager.getCSVData(testSuite,4)[0];
        System.out.println(getAssetName);
        extentTest.log(Status.INFO,"AssetName " +getAssetName);
        Thread.sleep(4000);
        browserActions.sendKeysToUpload(addFileBtn,getAssetName);
        browserActions.sendKeysByJavascript(reasonToEnter,"Automation Testing");
        browserActions.waitForElementToBeClickable(uploadBtn);
        browserActions.click(uploadBtn);
        System.out.println("revision asset has been uploaded successfully");
        extentTest.log(Status.INFO,"revision asset has been uploaded successfully");
        Thread.sleep(3000);
        browserActions.waitForElementToBeVisible(auditLog);
        browserActions.click(auditLog);
        String asset=csvDataManager.getCSVData(testSuite,4)[0];
        By verifyUploadAuditLog=By.xpath("//ancestor::ul[@id='auditLogList']//li[@data-t='UploadRevision']/div//assetversion[contains(.,'"+asset+"')]");
        browserActions.waitForElementToBeVisible(verifyUploadAuditLog);
        System.out.println("Verified upload revision" + asset);
        extentTest.log(Status.INFO,"Verified upload revision" + asset);
        Thread.sleep(2000);
        browserActions.waitForElementToBeClickable(revisionBtn);
        browserActions.click(revisionBtn);
        browserActions.click(clickRevertBtn);
        Thread.sleep(2000);
        browserActions.waitForFrameToBeAvailable(iFrameToEnter1);
        browserActions.waitForElementToBeClickable(revertUploadBtn);
        browserActions.click(revertUploadBtn);
        Thread.sleep(3000);
        browserActions.waitForElementToBeClickable(revisionBtn);
        browserActions.click(revisionBtn);
        String activeAsset=csvDataManager.getCSVData(testSuite,1)[0];
        By verifyRevisionAsset=By.xpath("//ancestor::ul[@class='revisions @*list*@ scrollable-box dam']/li[@class='revision current']//span[contains(text(),'"+activeAsset+"')]/preceding::strong[contains(.,'Active')]");
        browserActions.waitForElementToBeVisible(verifyRevisionAsset);
        System.out.println("Revision Asset" + verifyRevisionAsset);
        extentTest.log(Status.PASS,"Revision Asset" + verifyRevisionAsset);

        }
        catch(Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    public void linkProductRejectLink(String EAN) throws Exception {
        try{
        Thread.sleep(3000);
        browserActions.waitForElementToBeClickable(clickAddLink);
        browserActions.click(clickAddLink);
        Thread.sleep(6000);
        browserActions.waitForFrameToBeAvailable(iFrameToEnter1);
        Thread.sleep(5000);
        browserActions.sendKeys(subCodeToLookup,"OCOM898");
        browserActions.sendKeys(EANTxtBox,EAN);
        browserActions.click(triggerSearch);
        Thread.sleep(5000);
        browserActions.switchToDefaultContent();
        Thread.sleep(8000);
        browserActions.waitForFrameToBeAvailable(iFrameToEnter2);
        browserActions.clickByJavascript(checkCreateLink);
        browserActions.click(clickCreateLinks);
        Thread.sleep(5000);
        browserActions.waitForElementToBeClickable(clickRejectBtn);
        browserActions.click(clickRejectBtn);
        browserActions.sendKeysByJavascript(comments,"Automation Testing");
        System.out.println("comments passed");
        extentTest.log(Status.INFO,"comments passed");
        Thread.sleep(5000);
        browserActions.clickByJavascript(acceptBtn);
        browserActions.waitForElementToBeVisible(rejectedStatus);
        //check to verify that the link has been Rejected
        System.out.println("Rejected");
        extentTest.log(Status.PASS,"Rejected");
        browserActions.click(unlinkProduct);
        browserActions.clickByJavascript(acceptBtn);
        }
        catch(Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    public void LinkProductPublishLink(String EAN) throws Exception {
        try {
            browserActions.waitForElementToBeClickable(clickAddLink);
            browserActions.click(clickAddLink);
            Thread.sleep(6000);
            browserActions.waitForFrameToBeAvailable(iFrameToEnter1);
            Thread.sleep(5000);
            browserActions.sendKeys(subCodeToLookup,"OCOM898");
            browserActions.sendKeys(EANTxtBox,EAN);
            browserActions.click(triggerSearch);
            Thread.sleep(5000);
            BaseClass.driver.switchTo().defaultContent();
            browserActions.waitForFrameToBeAvailable(iFrameToEnter2);
            Thread.sleep(5000);
            //browserActions.waitForElementToBeClickable(publishLink);
            browserActions.clickByJavascript(publishLink);
            browserActions.click(clickCreateLinks);
            Thread.sleep(5000);
            browserActions.click(acceptBtn);
            browserActions.waitForElementToBeVisible(publishedStatus);
            System.out.println("Published");
            extentTest.log(Status.PASS,"Published");
            Thread.sleep(3000);
            browserActions.click(unlinkProduct);
            browserActions.click(acceptBtn);
            System.out.println("product has been unlinked");
            extentTest.log(Status.PASS,"product has been unlinked");
        }
        catch(Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    public void deleteAssetAction() throws Exception {
        try{
        Thread.sleep(2000);
        browserActions.waitForElementToBeClickable(deleteAsset);
        browserActions.click(deleteAsset);
        Thread.sleep(3000);
        browserActions.waitForElementToBeClickable(acceptBtn);
        browserActions.click(acceptBtn);
        Thread.sleep(5000);
        String assetName=csvDataManager.getCSVData("Enrich",0)[0];
        if(assetName=="Asset1"){
            System.out.println("Asset1 has not deleted");
            extentTest.log(Status.PASS,"Asset1 has not deleted");
        }
        }
        catch(Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    public void deleteSmartFolder() throws Exception {
        try {
            Thread.sleep(5000);
            browserActions.waitForElementToBeClickable(deleteBtn);
            browserActions.click(deleteBtn);
            System.out.println("SmartFolder is successfully deleted");
            extentTest.log(Status.PASS,"SmartFolder is successfully deleted");
            browserActions.waitForElementToBeClickable(acceptBtn);
            browserActions.click(acceptBtn);
        }
        catch(Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    public void deleteAllAssets() throws Exception{
        try {
        browserActions.waitForElementToBeClickable(ticKSelectAssetsBtn);
        browserActions.click(ticKSelectAssetsBtn);
        browserActions.waitForElementToBeClickable(selectAllAssets);
        browserActions.click(selectAllAssets);
        Thread.sleep(3000);
        browserActions.waitForElementToBeClickable(deleteAllAssets);
        browserActions.click(deleteAllAssets);
        Thread.sleep(3000);
        browserActions.waitForElementToBeClickable(yesToConfirm);
        browserActions.click(yesToConfirm);
        browserActions.waitForElementToBeClickable(acceptBtn);
        browserActions.click(acceptBtn);
        System.out.println("All Assets are successfully deleted");
        extentTest.log(Status.PASS,"All Assets are successfully deleted");

        }
        catch(Exception e){
            e.printStackTrace();
            throw e;
        }
    }
}
