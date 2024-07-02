package org.brandbank.pages.apps;

import com.aventstack.extentreports.Status;
import org.brandbank.base.BaseClass;
import org.brandbank.libs.BrowserActions;
import org.brandbank.libs.CSVDataManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class BoxRPage extends BaseClass {
    public static WebDriver driver;
    BrowserActions browserActions;
    CSVDataManager csvDataManager;
    public BoxRPage(WebDriver driver){
        this.driver = driver;
        browserActions = new BrowserActions(this.driver);
        csvDataManager=new CSVDataManager();
        PageFactory.initElements(driver,this);
    }
    By rightGrid = By.xpath("//div[@id='osq-right-grid']//div[@class='grid-canvas grid-canvas-top grid-canvas-left']");
    By waitingBtn = By.xpath("(//span[@class='slick-column-name'][normalize-space()='Waiting'])[2]");
    By abortOption = By.xpath("//span[normalize-space()='Abort']");


    public boolean abortProductFromBoxR(String testSuite,int numOfProducts) throws Exception {
        try {
            boolean eanFound = false;
            int timeCounter=0;
            browserActions.waitForElementToBeVisible(rightGrid);
            browserActions.click(waitingBtn);
            for (int i = 1; i <= numOfProducts; i++) {
                String EANFromCSV = csvDataManager.getCSVData(testSuite, i)[1];
                By EANInRHS = By.xpath(browserActions.createXpath("//div[@id='osq-right-grid']//div[@class='grid-canvas grid-canvas-top grid-canvas-left']//div[normalize-space()='{0}']", EANFromCSV));
                By contextMenu = By.xpath(browserActions.createXpath("//div[@id='osq-right-grid']//div[@class='grid-canvas grid-canvas-top grid-canvas-left']//div[normalize-space()='{0}']/parent::div//button[@class='contextbutton']", EANFromCSV));
                String sEANInRHS ="//div[@id='osq-right-grid']//div[@class='grid-canvas grid-canvas-top grid-canvas-left']//div[normalize-space()='"+EANFromCSV+"']" ;
                do {
                    Thread.sleep(5000);
                    eanFound = browserActions.isDisplayed(sEANInRHS);
                    if(eanFound==true){
                        extentTest.log(Status.INFO,"EAN " + EANFromCSV + " found in the RHS of Box-R screen");
                        System.out.println("EAN " + EANFromCSV + " found in the RHS of Box-R screen");
                        browserActions.click(EANInRHS);
                        browserActions.click(contextMenu);
                        browserActions.click(abortOption);
                        Thread.sleep(2000);
                        browserActions.waitForElementToDisappear(EANInRHS);
                        extentTest.log(Status.INFO,"EAN " + EANFromCSV + " aborted from Box-R");
                        System.out.println("EAN " + EANFromCSV + " aborted from Box-R");
                        break;
                    }
                    timeCounter++;
                    browserActions.click(waitingBtn);
                    Thread.sleep(2000);
                    browserActions.click(waitingBtn);
                }
                while(eanFound==false && timeCounter<25);
                if(eanFound==false){
                    extentTest.log(Status.FAIL,"Ean not found");
                    System.out.println("Ean not found");
                }
            }
            return eanFound;
        }
        catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }
}
