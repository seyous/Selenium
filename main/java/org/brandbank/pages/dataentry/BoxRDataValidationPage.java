package org.brandbank.pages.dataentry;

import com.aventstack.extentreports.Status;
import com.opencsv.CSVReader;
import org.brandbank.base.BaseClass;
import org.brandbank.libs.BrowserActions;
import org.brandbank.libs.CSVDataManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import java.io.FileReader;

public class BoxRDataValidationPage extends BaseClass {
    BrowserActions browserActions;
    CSVDataManager csvDataManager;

    DEProductCapturePage deProductCapturePage;

    public static WebDriver driver;

    public BoxRDataValidationPage(WebDriver driver) {
        this.driver = driver;
        browserActions = new BrowserActions(this.driver);
        csvDataManager = new CSVDataManager();
        deProductCapturePage = new DEProductCapturePage(driver);
        PageFactory.initElements(driver, this);
    }

    public boolean dataValidationInDE(String testSuite) throws Exception {
        try {
            //String filePath = System.getProperty("user.dir") + "/src/test/resources/testdata/"+testSuite+"/Segment_DE_Data.csv";
            String EAN = csvDataManager.getCSVData(testSuite, 1)[1];
            String filePath = System.getProperty("user.dir") + "/src/test/resources/testdata/" + testSuite + "";
            if (deProductCapturePage.verifyLanguageInDD(EAN,"English (British)")){
                filePath = filePath + "/" + "Segment_DE_Data.csv";
            } else if (deProductCapturePage.verifyLanguageInDD(EAN,"Dutch")) {
                filePath = filePath + "/" + "Segement_DE_Data_Dutch.csv";

            } else {
                System.out.println("Language not present");
                extentTest.log(Status.FAIL, "Language not present");
            }
            CSVReader reader = new CSVReader(new FileReader(filePath));
            String[] cell;
            String segmentName;
            String segmentData;
            String fieldType;
            int numoOfRows = 0;
            int dataNotFoundFlag = 0;
            while ((cell = reader.readNext()) != null) {
                if (numoOfRows != 0) {
                    for (int i = 0; i < 1; i++) {
                        segmentName = cell[i];
                        segmentData = cell[i + 1];
                        fieldType = cell[i + 2];
                        By segmentXpath = By.xpath("//span[normalize-space()='" + segmentName + "']");
                        By segmentDataStr =By.xpath("//*[normalize-space()='" + segmentData + "']") ;
                        By productDescAndBrand = By.xpath("//*[@value='"+segmentData+"']");
                        if (fieldType.toLowerCase().trim().contains("text")) {
                            browserActions.waitForElementToBeVisible(segmentXpath);
                            browserActions.click(segmentXpath);
                            if (browserActions.isDisplayedNew(segmentDataStr)) {
                                System.out.println(" " + segmentData + " found as a text field in data entry product capture");
                                extentTest.log(Status.PASS, " " + segmentData + " found as a text field in data entry product capture");
                            }
                            else {
                                System.out.println(" " + segmentData + " Not found under " + segmentName + " in data entry product capture");
                                extentTest.log(Status.FAIL," " + segmentData + " Not found under " + segmentName + " in data entry product capture");
                                dataNotFoundFlag++;
                            }
                        } else if (fieldType.toLowerCase().trim().contains("input")) {
                            browserActions.waitForElementToBeVisible(segmentXpath);
                            browserActions.click(segmentXpath);
                            browserActions.scrollToView(segmentXpath);
                            if (browserActions.isDisplayedNew(productDescAndBrand)) {
                                System.out.println(" " + segmentData + " found as a text field in data entry product capture");
                                extentTest.log(Status.PASS," " + segmentData + " found as a text field in data entry product capture");
                            }
                            else {
                                System.out.println(" " + segmentData + " Not found under " + segmentName + " in data entry product capture");
                                extentTest.log(Status.FAIL," " + segmentData + " Not found under " + segmentName + " in data entry product capture");
                                dataNotFoundFlag++;

                            }
                        } else {
                            System.out.println(" " + segmentData + " Not found under any of the field");
                            extentTest.log(Status.FAIL," " + segmentData + " Not found under any of the field");
                            dataNotFoundFlag++;

                        }
                    }
                }
                numoOfRows++;
            }
            if (dataNotFoundFlag==0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}















