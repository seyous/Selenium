package org.brandbank.pages.apps;

import com.aventstack.extentreports.Status;
import org.apache.log4j.net.SyslogAppender;
import org.brandbank.base.BaseClass;
import org.brandbank.libs.BrowserActions;
import org.brandbank.libs.CSVDataManager;
import org.brandbank.libs.DateManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class WhiteboxPage extends BaseClass {
    public static WebDriver driver;
    BrowserActions browserActions;
    CSVDataManager csvDataManager;

    public WhiteboxPage(WebDriver driver) {
        this.driver = driver;
        browserActions = new BrowserActions(this.driver);
        csvDataManager = new CSVDataManager();
        PageFactory.initElements(driver, this);
    }

    By uploadBtn = By.xpath("//button[@type='submit']");
    By chooseFileBtn = By.xpath("//input[@id='whitebox-zipfile']");
    By credentialsTxtBox = By.xpath("//input[@id='whitebox-credential']");
    By whiteboxPage = By.xpath("//h2[normalize-space()='Whitebox Uploader']");
    By historyPanel = By.xpath("//div[@id='whitebox-history-panel']");
    By currentStatus = By.xpath("//h2[normalize-space()='Current Status']");
    By loadingMsg = By.xpath("//div[@id='ajaxProgress']");
    By refreshBtn = By.xpath("//button[@id='get-upload-status']");
    By currentEAN = By.xpath("//div[@id='current-messages-panel']/p[2]");
    By eanStatus = By.xpath("//div[@id='current-status-panel']");

    public boolean whiteboxUpload(String credentials, String EAN, String zipPath) throws Exception {
        try {
            int rowCount = 1;
            boolean eanFoundFlag = false;
            boolean rowEan;
            String status;
            browserActions.waitForElementToBeVisible(whiteboxPage);
            browserActions.waitForElementToDisappear(loadingMsg);
            browserActions.sendKeys(credentialsTxtBox, credentials);
            browserActions.sendKeysToUpload(chooseFileBtn, zipPath);
            browserActions.clickByJavascript(uploadBtn);
            Thread.sleep(15000);
            browserActions.waitForElementToDisappear(loadingMsg);
            browserActions.waitForElementToBeVisible(historyPanel);
            do {
                By row = By.xpath("//div[@class='grid-canvas grid-canvas-top grid-canvas-left']/div[" + rowCount + "]");
                browserActions.waitForElementToBeVisible(row);
                browserActions.click(row);
                Thread.sleep(20000);
                browserActions.waitForElementToBePresent(currentStatus);
                status = eanStatusCheck();
                if (status.equalsIgnoreCase("Success")) {
                    rowEan = browserActions.getElementText(currentEAN).contains(EAN);
                    if (rowEan) {
                        eanFoundFlag = true;
                        System.out.println("EAN Matched : " + rowEan);
                        extentTest.log(Status.INFO, "EAN Matched : " + rowEan);
                        break;
                    } else {
                        rowCount++;
                        System.out.println("Trying Again - EAN not Matched");
                        extentTest.log(Status.INFO, "Trying Again - EAN not Matched");
                        if (rowCount != 3) {
                            browserActions.refreshPage();
                        }
                    }
                } else if (status.equalsIgnoreCase("Fail")) {
                    rowEan = browserActions.getElementText(currentEAN).contains(EAN);
                    if (rowEan) {
                        eanFoundFlag = true;
                        System.out.println("EAN Matched : " + rowEan + " But status is failed");
                        extentTest.log(Status.INFO, "EAN Matched : " + rowEan + "But status is failed");
                        break;
                    } else {
                        rowCount++;
                        System.out.println("Trying Again - EAN not Matched");
                        extentTest.log(Status.INFO, "Trying Again - EAN not Matched");
                        if (rowCount != 3) {
                            browserActions.refreshPage();
                        }
                    }
                } else {
                    rowCount++;
                    System.out.println("Trying Again -Valid status not found");
                    extentTest.log(Status.INFO, "Trying Again -Valid status not found");
                    if (rowCount != 3) {
                        browserActions.refreshPage();
                    }
                }
            }
            while ((!eanFoundFlag) && (rowCount <= 3));
            if (eanFoundFlag & status.equalsIgnoreCase("Success")) {
                System.out.println("EAN found");
                extentTest.log(Status.PASS, "EAN found");
                return true;
            } else {
                System.out.println("EAN not found or status is Failed");
                extentTest.log(Status.FAIL, "EAN not found or status is Failed");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public String eanStatusCheck() throws Exception {
        int timerCount = 1;
        boolean successStatus = false;
        String status;
        do {
            status = browserActions.getElementText(eanStatus);
            if (status.equalsIgnoreCase("Pending")) {
                browserActions.moveToElement(refreshBtn);
                browserActions.click(refreshBtn);
                Thread.sleep(5000);
                timerCount++;
                System.out.println("Status : Pending");
                extentTest.log(Status.INFO, "Status : Pending");
            } else if (status.equalsIgnoreCase("Fail")) {
                System.out.println("Status : Failed");
                extentTest.log(Status.INFO, "Status : Failed");
                break;
            } else if (status.equalsIgnoreCase("Success")) {
                successStatus = true;
                System.out.println("Status : Success");
                extentTest.log(Status.INFO, "Status : Success");
            } else {
                System.out.println("Status Not found");
                extentTest.log(Status.INFO, "Status not found");
                break;
            }
        }
        while ((!successStatus) && (timerCount < 15));
        return status;
    }
}
