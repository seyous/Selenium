package org.brandbank.pages.enrich;

import com.aventstack.extentreports.Status;
import org.brandbank.base.BaseClass;
import org.brandbank.libs.BrowserActions;
import org.brandbank.libs.CSVDataManager;
import org.brandbank.libs.DateManager;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
public class EnrichHomePage extends BaseClass {
    BrowserActions browserActions;
    CSVDataManager csvDataManager;
    public static WebDriver driver;
    public EnrichHomePage(WebDriver driver) {
        this.driver = driver;
        browserActions = new BrowserActions(this.driver);
        csvDataManager=new CSVDataManager();
        PageFactory.initElements(driver,this);
    }
    By enrichBtn=By.xpath("//div[@class='container']//a[text()='Enrich']");
    By uploadBtn=By.xpath("//a[@id='btnActUpload']");
    By searchByTag=By.xpath("//div[@class='tagsBox bottom-margin']//input[@id='token-input-txtTags']");
    By applyFilterBtn=By.xpath("//button[@id='btnFilter']");
    By assetVisiblity=By.xpath("//div[@id='assets']");
    By ticKSelectAssetsBtn =By.xpath("//input[@id='select-asset-checkbox']");
    By selectAllAssets=By.xpath("//span[@class='allPages label fake-link'][contains(text(),'Select all assets')]");
    By advanceSearchBtn=By.xpath("//a[normalize-space()='Advanced search']");
    By selectTag=By.xpath("//select[@class='ddl condition-subject']");
    By searchBoxToEnter=By.xpath("//ul[@class='form-control token-input-list-brandbank']/descendant::input[@type='text']");
    By createSmartFolder=By.xpath("//button[@id='btnCreateSmartFolder']");
    By folderName=By.xpath("//input[@id='txtSmartFolderName']");
    By saveSmartFolderBtn=By.xpath("//button[@id='btnSaveSmartFolder']");
    By clickMoveToFolder= By.xpath("//a[@id='bulkBtnMoveAssets']");
    By newFolderBtn=By.xpath("//a[@id='btnActNewFolder']");
    By typeFolderName=By.xpath("//input[@id='txtName']");
    By okBtn=By.xpath("//button[@id='btnOK']");
    By folderNameInList=By.xpath("//div[@class='modal-body']//select[@id='sFoldersTree']");
    By moveToFolderBtn=By.xpath("//button[normalize-space()='Move']");
    By enrichAssetsBtn=By.xpath("//a[contains(text(),'Enrich Assets')]");
    By homeBtn=By.xpath("//a[@title='Home']//i[@class='icon-home']");
    String iFrame="//iframe[@class='fancybox-iframe']";
    String innerFrame="//iframe[@src='/Assets/AdvancedSearch/?amend=false&smartFolderId=0&createSmartFolderMode=undefined']";

    public void goToEnrichPage() throws Exception {
        try{
        Thread.sleep(2000);
        browserActions.waitForElementToBeVisible(enrichBtn);
        browserActions.click(enrichBtn);
        browserActions.click(enrichAssetsBtn);
        }
        catch(Exception e){
            e.printStackTrace();
            throw e;
        }

    }
    public void uploadBtn(){
        browserActions.scrollToView(uploadBtn);
        browserActions.click(uploadBtn);
    }
    public void findByTags(String tagName) throws Exception {
        try{
            String extendTag = new DateManager().getCurrentDate("ddMMyyyy");
            browserActions.waitForElementToBeClickable(searchByTag);
            browserActions.sendKeys(searchByTag, tagName + extendTag);
            browserActions.sendKeys(searchByTag, String.valueOf(Keys.ENTER));
            Thread.sleep(2000);
            browserActions.scrollToView(applyFilterBtn);
            browserActions.click(applyFilterBtn);
            Thread.sleep(5000);
            //verify assets are visible
            browserActions.waitForElementToBeVisible(assetVisiblity);
            System.out.println("Assets are Visible");
            extentTest.log(Status.PASS,"Assets are Visible");
            Thread.sleep(3000);
        }
        catch(Exception e){
            e.printStackTrace();
            throw e;
        }
    }
    public boolean verifyAsset(String testSuite, int assets,String assetVisiblity) throws Exception{
        try {
            boolean successFlag=false;
            if(assetVisiblity.equalsIgnoreCase("public")) {
                boolean publicImgFlag;
                for (int i = 1; i <= assets; i++) {
                    String Assets = csvDataManager.getCSVData(testSuite, i)[1];
                    By publicAssets= By.xpath("//div[normalize-space()='"+Assets+"']");
                    publicImgFlag = browserActions.isDisplayedNew(publicAssets);
                    if(publicImgFlag){
                        System.out.println(Assets+ "uploaded successful");
                        extentTest.log(Status.PASS, Assets+ "File uploaded successful");
                        successFlag=true;
                    }
                    else{
                        System.out.println(Assets+ "failed to upload");
                        extentTest.log(Status.FAIL, Assets+ "failed to upload");
                        successFlag=false;
                    }
                }
            } else if (assetVisiblity.equalsIgnoreCase("private")) {
                int j=5;
                boolean privateImgFlag;
                boolean videoFlag;
                for(int i=0;i<assets;i++){
                    String Asset=csvDataManager.getCSVData(testSuite,j)[1];
                    String embedSource= csvDataManager.getCSVData("Enrich",j)[2];
                    By imgVerification=By.xpath("//li[contains(@class,'asset')]//img[@alt='"+Asset+"']");
                    By videoVerification =By.xpath ("(//img[@data-url='"+embedSource+"'])[2]");
                    privateImgFlag =browserActions.isDisplayedNew(imgVerification);
                    videoFlag =browserActions.isDisplayedNew(videoVerification);
                    if(privateImgFlag && videoFlag){
                        System.out.println(Asset+","+embedSource+ "uploaded successful");
                        extentTest.log(Status.PASS, Asset +","+embedSource+ "File uploaded successful");
                        successFlag=true;
                    }
                    else{
                        System.out.println(Asset+","+embedSource+ "failed to upload");
                        extentTest.log(Status.FAIL, Asset+","+embedSource+ "failed to upload");
                        successFlag=false;
                    }

                }
            }else {
                int n=6;
                boolean InternalImgFlag;
                boolean InternalVideoFlag;
                for(int i=0;i<assets;i++){
                    String Asset=csvDataManager.getCSVData(testSuite,n)[1];
                    String embedSource= csvDataManager.getCSVData("Enrich",n)[2];
                    By InternalAsset=By.xpath("//div[normalize-space()='"+Asset+"']");
                    By embedAsset=By.xpath("(//img[@data-url='"+embedSource+"'])[2]");
                    InternalImgFlag=browserActions.isDisplayedNew(InternalAsset);
                    InternalVideoFlag=browserActions.isDisplayedNew(embedAsset);
                    if(InternalImgFlag && InternalVideoFlag){
                        System.out.println(Asset+","+embedSource+ "uploaded successful");
                        extentTest.log(Status.PASS, Asset +","+embedSource+ "File uploaded successful");
                        successFlag=true;
                    }
                    else{
                        System.out.println(Asset+","+embedSource+"failed to upload");
                        extentTest.log(Status.FAIL, Asset+","+embedSource+ "failed to upload");
                        successFlag=false;
                    }

                }
            }
            if(successFlag){
                return true;
            }
            else{
                return false;

            }
        }
        catch(Exception e){
            e.printStackTrace();
            throw e;
        }
    }
    public void assetToSelect(String testSuite,String assetVisiblity)throws Exception {
        try {
            Thread.sleep(2000);
            if (assetVisiblity.equalsIgnoreCase("public")) {
                String getAssetName = csvDataManager.getCSVData(testSuite, 1)[1];
                By clickOnAsset = By.xpath("//div[normalize-space()='" + getAssetName + "']");
                browserActions.waitForElementToBeClickable(clickOnAsset);
                browserActions.click(clickOnAsset);
                System.out.println("Asset has been selected");
                extentTest.log(Status.INFO,"Asset has been selected");
            } else {
                String getAssetName = csvDataManager.getCSVData(testSuite, 5)[1];
                By clickOnAsset = By.xpath("//div[normalize-space()='" + getAssetName + "']");
                browserActions.waitForElementToBeClickable(clickOnAsset);
                browserActions.click(clickOnAsset);
                System.out.println("Asset has been selected");
                extentTest.log(Status.INFO,"Asset has been selected");

            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    public void movingToSmartFolder() throws Exception {
        try {
            String extendTag = new DateManager().getCurrentDate("ddMMyyyy");
            browserActions.scrollToView(advanceSearchBtn);
            browserActions.waitForElementToBeVisible(advanceSearchBtn);
            browserActions.click(advanceSearchBtn);
            browserActions.waitForFrameToBeAvailable(iFrame);
            browserActions.selectFromDropdown(selectTag, "Tag");
            browserActions.waitForElementToBeVisible(searchBoxToEnter);
            browserActions.sendKeysByJavascript(searchBoxToEnter, "PrivateAssets" + extendTag);
            browserActions.sendKeys(searchBoxToEnter, String.valueOf(Keys.ENTER));
            browserActions.waitForElementToBeClickable(createSmartFolder);
            browserActions.click(createSmartFolder);
            browserActions.switchToDefaultContent();
            browserActions.waitForFrameToBeAvailable(innerFrame);
            browserActions.sendKeysByJavascript(folderName, "PrivateAssets" + extendTag);
            browserActions.waitForElementToBeClickable(saveSmartFolderBtn);
            browserActions.click(saveSmartFolderBtn);
            System.out.println("Smart Folder has been created");
            extentTest.log(Status.INFO,"Smart Folder has been created");
            Thread.sleep(3000);
            browserActions.scrollToView(homeBtn);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    public void verifySmartFolder(){
        String extendTag = new DateManager().getCurrentDate("ddMMyyyy");
        By verifySf=By.xpath("//li[@class='folder smart-folder']//a//div[contains(.,'PrivateAssets"+extendTag+"')]");
        browserActions.waitForElementToBeVisible(verifySf);
        System.out.println("Verified SmartFolder with" + extendTag);
        extentTest.log(Status.PASS,"Verified SmartFolder with" + extendTag);
    }
    public void createNewFolder() throws Exception {
        try {
            String extendTag = new DateManager().getCurrentDate("ddMMyyyy");
            browserActions.scrollToView(newFolderBtn);
            browserActions.waitForElementToBeClickable(newFolderBtn);
            browserActions.click(newFolderBtn);
            Thread.sleep(2000);
            browserActions.waitForFrameToBeAvailable(iFrame);
            browserActions.sendKeys(typeFolderName, "InternalAssets" + extendTag);
            browserActions.waitForElementToBeClickable(okBtn);
            browserActions.click(okBtn);
            Thread.sleep(3000);
            browserActions.scrollToView(homeBtn);
            System.out.println("New folder has been created");
            extentTest.log(Status.PASS,"New folder has been created");

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    public void moveToFolder() throws Exception{
        try {
            String extendTag=new DateManager().getCurrentDate("ddMMyyyy");
            Thread.sleep(3000);
            browserActions.waitForElementToBeClickable(ticKSelectAssetsBtn);
            browserActions.click(ticKSelectAssetsBtn);
            browserActions.waitForElementToBeClickable(selectAllAssets);
            browserActions.click(selectAllAssets);
            Thread.sleep(2000);
            browserActions.waitForElementToBeClickable(clickMoveToFolder);
            browserActions.click(clickMoveToFolder);
            browserActions.waitForElementToBeVisible(folderNameInList);
            System.out.println("folder tree visible");
            extentTest.log(Status.INFO,"folder tree visible");
            browserActions.scrollToView(folderNameInList);
            Thread.sleep(2000);
            browserActions.selectFromDropdown(folderNameInList,"┣ InternalAssets"+extendTag);
            Thread.sleep(3000);
            browserActions.waitForElementToBeClickable(moveToFolderBtn);
            browserActions.click(moveToFolderBtn);
            System.out.println("successfully moved to folder");
            extentTest.log(Status.PASS,"successfully moved to folder");

        }
        catch(Exception e){
            e.printStackTrace();
            throw e;
        }
    }
    public void verifyMoveToFolder(){
        String extendTag=new DateManager().getCurrentDate("ddMMyyyy");
        By internalAssetFolder=By.xpath("//li//div[contains(text(),'InternalAssets"+extendTag+"')]");
        browserActions.waitForElementToBeVisible(internalAssetFolder);
        System.out.println("Internal Asset folder has been verified");
        extentTest.log(Status.PASS,"Internal Asset folder has been verified");
    }
}



