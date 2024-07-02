package org.brandbank.pages.boxr;

import com.aventstack.extentreports.Status;
import com.opencsv.CSVReader;
import org.brandbank.base.BaseClass;
import org.brandbank.libs.BrowserActions;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.tracing.opentelemetry.SeleniumSpanExporter;
import org.openqa.selenium.support.PageFactory;

import java.io.FileReader;
import java.util.List;

public class CleanPage extends BaseClass {
    BrowserActions browserActions;
    public static WebDriver driver;
    public CleanPage(WebDriver driver) {
        this.driver = driver;
        browserActions = new BrowserActions(this.driver);
        PageFactory.initElements(driver,this);
    }
    By addresses=By.xpath("//h3[contains(text(),'Addresses')]");
    By brand=By.xpath("//h3[contains(text(),'Brand')]");
    By ingredients =By.xpath("//h3[contains(text(),'Ingredients')]");
    By nutrition =By.xpath("//h3[contains(text(),'nutrition')]");
    By productName=By.xpath("//h3[contains(text(),'Product Name')]");
    By cleanPage=By.xpath("//h2[contains(text(),'clean')]");
    By completeBtn=By.xpath("//div[contains(text(),'Complete')]");
    By loading = By.xpath("//div[@class='message']");
    By enterMktgText=By.xpath("//h3[normalize-space()='Marketing ( 1 )']/following-sibling::*/descendant::textarea");
    By acceptBtn=By.xpath("//div[@class='stardard-modal-window modal-window visible']//button[@class='btn btn-primary btn-sm'][normalize-space()='OK']");


    public boolean validateSegmentContent(String testSuite) throws Exception {
        try {
            String filePath = System.getProperty("user.dir") + "/src/test/resources/testdata/" + testSuite + "/Segment_Box-R_Data.csv";
            CSVReader reader = new CSVReader(new FileReader(filePath));
            String[] cell;
            String SegmentName;
            String SegmentData;
            boolean segmentDataFound = false;
            int numoOfRows = 0;
            int dataNotFoundFlag = 0;
            while ((cell = reader.readNext()) != null) {
                if (numoOfRows != 0) {
                    int i = 0;

                    SegmentName = cell[i];
                    SegmentData = cell[i + 1];
                    By segmentNameXpath = By.xpath("//h3[contains(text(),'" + SegmentName + "')]");
                    By sendMktgText = By.xpath("//h3[normalize-space()='Marketing ( 2 )']/following-sibling::*/descendant::textarea");
                    browserActions.waitForElementToBeVisible(cleanPage);
                    browserActions.scrollToView(segmentNameXpath);
                    if (SegmentName.toLowerCase().trim().contains("marketing")) {
                        List<WebElement> rows = browserActions.listOfElementToBePresent(sendMktgText);
                        for (int count=1; count<rows.size(); ++count){
                            rows.get(count).sendKeys("Test Automation Marketing", Keys.TAB);
                            browserActions.waitForElementToBeVisible(sendMktgText);
                            browserActions.sendKeys(sendMktgText, SegmentData);
                            System.out.println("Marketing segment data entered as :" + SegmentData);
                            extentTest.log(Status.INFO, "Marketing segment data entered as :" + SegmentData);
                            dataNotFoundFlag++;
                        }
                    }
                    By segmentDataStr = By.xpath("//h3[normalize-space()='" + SegmentName + " ( 2 )']/following-sibling::*/descendant::*[contains(text(),'" + SegmentData + "')]");
                    segmentDataFound = browserActions.isDisplayedNew(segmentDataStr);
                    if (segmentDataFound) {
                        System.out.println("segment data found :" + SegmentData);
                        extentTest.log(Status.INFO, "segment data found :" + SegmentData);
                        dataNotFoundFlag++;
                    } else {
                        System.out.println("segment data not found :" + SegmentData);
                        extentTest.log(Status.INFO, "segment data not found :" + SegmentData);
                        dataNotFoundFlag = 0;
                        break;
                    }
                }


                numoOfRows++;
            }

            if (dataNotFoundFlag == 0) {
                return false;
            } else {
                browserActions.click(completeBtn);
                browserActions.click(acceptBtn);
                return true;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public void completeBtn(){
        browserActions.click(completeBtn);
        browserActions.click(acceptBtn);
    }

    public boolean dataValidationForCICD(){
        browserActions.waitForElementToDisappear(loading);
        browserActions.waitForElementToBeVisible(cleanPage);
        int dataPresent = 0;
        for(int i=1;i<=6;i++){
            if(i==5){
                By structuredDataStr = By.xpath("//div[@class='segment'][5]//descendant::div[@class='structured']");
                By structuredData = By.xpath("//div[@class='providerContent']//div[2]//div[2]");
                boolean structuredDataPresent = browserActions.isDisplayedNew(structuredDataStr);
                if(structuredDataPresent){
                    String data = browserActions.getElementText(structuredData);
                    if(data.isEmpty()){
                        System.out.println("Cannot find data in Segment "+i);
                        extentTest.log(Status.INFO,"Cannot find data in Segment "+i);
                        dataPresent = 0;
                        break;
                    }
                    else{
                        System.out.println("Data is present in Segment "+i);
                        extentTest.log(Status.INFO,"Data is present in Segment "+i);
                        dataPresent++;
                    }
                }
            }
            else{
                By textarea = By.xpath("//div[@class='segment']["+i+"]//descendant::div[contains(@class,'providerText active')]//descendant::textarea");
                String data = browserActions.getElementText(textarea);
                if(data.isEmpty()){
                    System.out.println("Cannot find data in Segment "+i);
                    extentTest.log(Status.INFO,"Cannot find data in Segment "+i);
                    dataPresent = 0;
                    break;
                }
                else{
                    System.out.println("Data is present in Segment "+i);
                    extentTest.log(Status.INFO,"Data is present in Segment "+i);
                    dataPresent++;
                }
            }
        }
        if(dataPresent>0)
            return true;
        else
            return false;
    }


    // return ByType of WebElement
    private By toByVal(WebElement we) {
        // By format = "[foundFrom] -> locator: term"
        // see RemoteWebElement toString() implementation
        String[] data = we.toString().split(" -> ")[1].replace("]", "").split(": ");
        String locator = data[0];
        String term = data[1];

        switch (locator) {
            case "xpath":
                return By.xpath(term);
            case "css selector":
                return By.cssSelector(term);
            case "id":
                return By.id(term);
            case "tag name":
                return By.tagName(term);
            case "name":
                return By.name(term);
            case "link text":
                return By.linkText(term);
            case "class name":
                return By.className(term);
        }
        return (By) we;
    }
}
