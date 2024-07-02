package org.brandbank.pages.enrich;

import org.brandbank.libs.BrowserActions;
import org.brandbank.libs.CSVDataManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class DownloadAssetPage {
    BrowserActions browserActions;
    CSVDataManager csvDataManager;
    public static WebDriver driver;
    public DownloadAssetPage(WebDriver driver) {
        this.driver = driver;
        browserActions = new BrowserActions(this.driver);
        csvDataManager=new CSVDataManager();
        PageFactory.initElements(driver, this);
    }
    By primaryDownloadBtn=By.id("btnDownload");
    By cancelDownloadBtn= By.xpath("//button[@class='btn btn-default btn-close']");
    public void primaryAssetDownload(String testSuite) throws Exception {
        try{
        browserActions.waitForElementToBeClickable(primaryDownloadBtn);
        browserActions.click(primaryDownloadBtn);
        Thread.sleep(20000);
        browserActions.waitForElementToBeClickable(cancelDownloadBtn);
        browserActions.click(cancelDownloadBtn);
        Thread.sleep(3000);
        }
        catch(Exception e){
            e.printStackTrace();
            throw e;
        }
    }
}
