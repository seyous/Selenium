package org.brandbank.tests.enrich;

import com.aventstack.extentreports.Status;
import org.brandbank.libs.CSVDataManager;
import org.brandbank.libs.PropertyManager;
import org.brandbank.pages.LoginPage;
import org.brandbank.pages.enrich.AssetActionsPage;
import org.brandbank.pages.enrich.DownloadAssetPage;
import org.brandbank.pages.enrich.EnrichHomePage;
import org.brandbank.pages.enrich.UploadAssetPage;
import org.brandbank.pages.productlibrary.PLHomePage;
import org.brandbank.tests.TestBaseSetup;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;

public class EnrichTest extends TestBaseSetup {
    LoginPage loginPage;
    PropertyManager propertyManager;
    CSVDataManager csvDataManager;
    EnrichHomePage enrichHomePage;
    UploadAssetPage uploadAssetPage;
    AssetActionsPage assetActionsPage;
    DownloadAssetPage downloadAssetPage;
    PLHomePage PLHomePage;

    public EnrichTest() {
        loginPage = new LoginPage(driver);
        propertyManager = new PropertyManager();
        csvDataManager = new CSVDataManager();
        enrichHomePage = new EnrichHomePage(driver);
        uploadAssetPage = new UploadAssetPage(driver);
        assetActionsPage = new AssetActionsPage(driver);
        downloadAssetPage = new DownloadAssetPage(driver);
        PLHomePage = new PLHomePage(driver);
    }

    @Test
    public void addPublicAsset() throws Exception {
        try {
            String visibility = "public";
            enrichHomePage.goToEnrichPage();
            enrichHomePage.uploadBtn();
            uploadAssetPage.uploadAsset(visibility, 3);
            uploadAssetPage.setAssetAttributes(visibility);
            Thread.sleep(3000);
        }
        catch(Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    @Parameters({"testSuite"})
    public void verifyPublicAsset(String testSuite) throws Exception {
        try {
            String visibility = "public";
            String tagName = "MultipleAssets";
            enrichHomePage.goToEnrichPage();
            enrichHomePage.findByTags(tagName);
            boolean verifyImg=enrichHomePage.verifyAsset(testSuite,3,visibility);
            Assert.assertEquals(verifyImg,true);
        }
        catch(Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    @Parameters({"testSuite"})
    public void assetGalleryToView (String testSuite) throws Exception {
        try {
            String visibility = "public";
            String tagName = "MultipleAssets";
            enrichHomePage.goToEnrichPage();
            enrichHomePage.findByTags(tagName);
            enrichHomePage.assetToSelect(testSuite, visibility);
            assetActionsPage.viewAsset();
        }
        catch(Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    @Parameters({"testSuite"})
    public void downloadAsset (String testSuite) throws Exception {
        try {
            String visibility = "public";
            String tagName = "MultipleAssets";
            enrichHomePage.goToEnrichPage();
            enrichHomePage.findByTags(tagName);
            enrichHomePage.assetToSelect(testSuite, visibility);
            assetActionsPage.downloadAssetAction();
            downloadAssetPage.primaryAssetDownload(testSuite);
        }
        catch(Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    @Parameters({"testSuite"})
    public void editAsset (String testSuite) throws Exception {
        try {
            String visibility = "public";
            String tagName = "MultipleAssets";
            enrichHomePage.goToEnrichPage();
            enrichHomePage.findByTags(tagName);
            enrichHomePage.assetToSelect(testSuite, visibility);
            assetActionsPage.editAssetAction(visibility);
            Thread.sleep(2000);
        }
        catch(Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    @Parameters({"testSuite"})
    public void updateAsset (String testSuite) throws Exception {
        try {
            String visibility = "public";
            String tagName = "MultipleAssets";
            enrichHomePage.goToEnrichPage();
            enrichHomePage.findByTags(tagName);
            enrichHomePage.assetToSelect(testSuite, visibility);
            assetActionsPage.UpdateAssetAction(testSuite);
            Thread.sleep(2000);
        }
        catch(Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    @Parameters({"testSuite","EAN"})
    public void rejectLink (String testSuite,String EAN) throws Exception {
        try {
            String visibility = "public";
            String tagName = "MultipleAssets";
            enrichHomePage.goToEnrichPage();
            enrichHomePage.findByTags(tagName);
            enrichHomePage.assetToSelect(testSuite, visibility);
            assetActionsPage.linkProductRejectLink(EAN);
        }
        catch(Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    @Parameters({"testSuite","EAN"})
    public void publishLink(String testSuite,String EAN) throws Exception {
        try {
            String visibility = "public";
            String tagName = "MultipleAssets";
            enrichHomePage.goToEnrichPage();
            enrichHomePage.findByTags(tagName);
            enrichHomePage.assetToSelect(testSuite, visibility);
            assetActionsPage.LinkProductPublishLink(EAN);
        }
        catch(Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    @Parameters({"testSuite"})
    public void deleteAsset(String testSuite) throws Exception {
        try {
            String visibility = "public";
            String tagName = "MultipleAssets";
            enrichHomePage.goToEnrichPage();
            enrichHomePage.findByTags(tagName);
            enrichHomePage.assetToSelect(testSuite, visibility);
            assetActionsPage.deleteAssetAction();
        }
        catch(Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    public void addPrivateAsset() throws Exception {
        try {
            String tagName = "PrivateAssets";
            String visiblity = "private";
            enrichHomePage.goToEnrichPage();
            enrichHomePage.uploadBtn();
            uploadAssetPage.uploadAsset(visiblity, 1);
            uploadAssetPage.setAssetAttributes(visiblity);
        }
        catch(Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    @Parameters({"testSuite"})
    public void verifyPrivateAsset(String testSuite) throws Exception {
        try {
            String visibility = "private";
            String tagName = "PrivateAssets";
            enrichHomePage.goToEnrichPage();
            enrichHomePage.findByTags(tagName);
            boolean verifyImg=enrichHomePage.verifyAsset(testSuite,1, visibility);
            Assert.assertEquals(verifyImg,true);
        }
        catch(Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    public void createSmartFolder() throws Exception {
        try {
            String tagName = "PrivateAssets";
            enrichHomePage.goToEnrichPage();
            enrichHomePage.findByTags(tagName);
            enrichHomePage.movingToSmartFolder();
            enrichHomePage.goToEnrichPage();
            enrichHomePage.findByTags(tagName);
        }
        catch(Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    public void verifySmartFolder() throws Exception{
        try {
            String tagName = "PrivateAssets";
            enrichHomePage.goToEnrichPage();
            enrichHomePage.findByTags(tagName);
            enrichHomePage.verifySmartFolder();
        }
        catch(Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    @Parameters({"testSuite"})
    public void changedToPublicAsset(String testSuite) throws Exception {
        try {
            String tagName = "PrivateAssets";
            String visiblity = "private";
            enrichHomePage.goToEnrichPage();
            enrichHomePage.findByTags(tagName);
            enrichHomePage.assetToSelect(testSuite, visiblity);
            assetActionsPage.editAssetAction(visiblity);
        }
        catch(Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    @Parameters({"testSuite"})
    public void createPublicLink(String testSuite) throws Exception {
        try {
            String tagName = "PrivateAssets";
            String visiblity = "private";
            enrichHomePage.goToEnrichPage();
            enrichHomePage.findByTags(tagName);
            enrichHomePage.assetToSelect(testSuite, visiblity);
            assetActionsPage.createPublicLink();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    public void deleteSmartFolder() throws Exception {
        try {
            String tagName = "PrivateAssets";
            Thread.sleep(2000);
            enrichHomePage.goToEnrichPage();
            enrichHomePage.findByTags(tagName);
            assetActionsPage.deleteSmartFolder();
        }
        catch(Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    public void addInternalAsset() throws Exception  {
        try{
            String tagName="InternalAssets";
            String visiblity ="Internal";
            enrichHomePage.goToEnrichPage();
            enrichHomePage.uploadBtn();
            uploadAssetPage.uploadAsset(visiblity,1);
            uploadAssetPage.setAssetAttributes(visiblity);
            enrichHomePage.goToEnrichPage();
            enrichHomePage.findByTags(tagName);
        }
        catch(Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    @Parameters({"testSuite"})
    public void verifyInternalAsset(String testSuite) throws Exception {
        try {
            String visibility = "Internal";
            String tagName = "InternalAssets";
            enrichHomePage.goToEnrichPage();
            enrichHomePage.findByTags(tagName);
            boolean verifyImg=enrichHomePage.verifyAsset(testSuite,1, visibility);
            Assert.assertEquals(verifyImg,true);
        }
        catch(Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    public void createFolder() throws Exception  {
        try{
            String tagName="InternalAssets";
            enrichHomePage.goToEnrichPage();
            enrichHomePage.findByTags(tagName);
            enrichHomePage.createNewFolder();
        }
        catch(Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    public void addAssetToFolder() throws Exception  {
        try{
            String tagName="InternalAssets";
            enrichHomePage.goToEnrichPage();
            enrichHomePage.findByTags(tagName);
            enrichHomePage.moveToFolder();
        }
        catch(Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    public void verifyAssetAddedToFolder() throws Exception  {
        try{
            String tagName="InternalAssets";
            enrichHomePage.goToEnrichPage();
            enrichHomePage.findByTags(tagName);
            enrichHomePage.verifyMoveToFolder();
        }
        catch(Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    public void deleteAllPublicAssets() throws Exception {
        try {
            String tagName = "MultipleAssets";
            enrichHomePage.goToEnrichPage();
            enrichHomePage.findByTags(tagName);
            assetActionsPage.deleteAllAssets();
        }
        catch(Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @AfterMethod
    public void afterMethod(ITestResult result)
    {
        if(!result.isSuccess())
            System.setProperty("TestStatus","false");
    }
}