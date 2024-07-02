package org.brandbank.pages.apps;

import com.aventstack.extentreports.Status;
import org.brandbank.base.BaseClass;
import org.brandbank.libs.BrowserActions;
import org.brandbank.libs.CSVDataManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class BookingInPage extends BaseClass {
    BrowserActions browserActions;
    CSVDataManager csvDataManager;

    public static WebDriver driver;
    public BookingInPage(WebDriver driver) {
        this.driver = driver;
        browserActions = new BrowserActions(this.driver);
        csvDataManager=new CSVDataManager();
        PageFactory.initElements(driver,this);
    }
    By orderOption = By.xpath("//select[@id='app-header-searchProvider']");
    By orderNumSearchBox = By.id("app-header-searchTerm");
    By searchBtn = By.id("app-header-searchBtn");
    By profileBtn = By.xpath("//img[@src='../Content/images/workspace-icons/light/1.png']");
    By newBatch = By.xpath("//span[normalize-space()='New Batch']");
    By selectBatchType=By.id("batchTypeSelector");
    By btnAccept=By.id("btnAccept");
    By getProducts=By.id("btnGetProducts");
    By okBtn = By.xpath("//input[@value='OK']");
    By acceptBtn = By.xpath("//img[@id='btnAccept' or @src='/Content/images/submit.png']");
    By dataNoCopyOver = By.xpath("//input[@id='rbDataCopiedWithNoCopyOver']");
    By dataCopyOverNoAmends = By.xpath("//input[@id='rbDataCopiedWithNoAmends']");
    By dataCopyAndAmend = By.xpath("//input[@id='rbDataCopiedWithAmends']");
    By mkgNoCopyOver = By.xpath("//input[@id='rbMarketingImagesWithNoCopyOver']");
    By mkgCopyOverNoAmends = By.xpath("//input[@id='rbMarketingImagesWithNoAmends']");
    By mkgCopyAndAmend = By.xpath("//input[@id='rbMarketingImagesWithAmends']");
    By merchNoCopyOver = By.xpath("//input[@id='rbMarchandisingImagesWithNoCopyOver']");
    By merchCopyOverNoAmends = By.xpath("//input[@id='rbMarchandisingImagesWithNoAmends']");
    By merchCopyAndAmend = By.xpath("//input[@id='rbMarchandisingImagesWithAmends']");
    By batchId = By.xpath("//div[@id='rightPaneTopContentSection']//h1[@class='col-md-12']");
    By clickOnHeader=By.xpath("//div[@id='rightPaneTopContentSection']//span[@class='slick-group-title']");
    By batchDetails=By.xpath("//li[@title='Batch Details']");
    By notes= By.xpath("//span[@id='rightPaneBottomContentSectionHeader']");
    By msgBar=By.xpath("//span[@id='notificationBar-Message']");
    public boolean Search_Order_To_BookIn(String searchProvider,String orderId,String subCode) throws Exception {
        try {
            By OrderNumber = By.xpath("(//div[@id='rightPaneTopContentSection']//span[text()='" + orderId + "']/parent::*/following-sibling::*[text()='" + subCode + "'])");
            browserActions.waitForElementToBeVisible(profileBtn);
            int orderFound = 0;
            if(!(browserActions.isDisplayedNew(OrderNumber))){
                browserActions.click(orderOption);
                extentTest.log(Status.INFO, "Searching order to book in");
                browserActions.selectFromDropdown(orderOption, searchProvider);
                browserActions.clearTextBox(orderNumSearchBox);
                browserActions.sendKeys(orderNumSearchBox, orderId);
                browserActions.click(searchBtn);
                if(browserActions.isDisplayedNew(OrderNumber))
                    orderFound = 1;
            }
            else
                orderFound = 1;
            if(orderFound ==1)
                return true;
            else
                return false;
        }
        catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    public void OpenNewBatchScreen(String orderId) throws Exception{
        try {
            By menuOrderId = By.xpath("//button[@data-orderid='" + orderId + "']//parent::div");
            browserActions.click(menuOrderId);
            browserActions.waitForElementToBeVisible(newBatch);
            browserActions.click(newBatch);
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }
    public String getBatchNum() throws Exception{
        try {
            browserActions.waitForElementToBeVisible(batchId);
            String text = browserActions.getElementText(batchId);
            String actual = text.replaceAll("[^0-9]", "");
            return actual;
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    public boolean Bookin_NewBatch(String testSuite, String productType, int numOfBatches, int productsPerBatch, String subCode) throws Exception {
        try {
            int csvRowNumber = 1;
            int SearchOrderTimer = 0;
            String OrderNumber ;
            for (int i = 1; i <= numOfBatches; i++) {
                do {
                    OrderNumber= csvDataManager.getCSVData(testSuite, i)[3];
                    boolean OrderFound = Search_Order_To_BookIn("Order", OrderNumber,subCode);
                    if(OrderFound){
                        System.out.println("Order found" );
                        extentTest.log(Status.PASS,"Order found");
                        break;
                    }
                    else {
                        SearchOrderTimer++;
                        Thread.sleep(40000);
                    }
                }while (SearchOrderTimer<10);
                if(SearchOrderTimer>=10)
                {
                    System.out.println("Order not found. Waited for 7.5 mins" );
                    extentTest.log(Status.FAIL,"Order not found. Waited for 7.5 mins");
                    return false;
                }
                OpenNewBatchScreen(OrderNumber);
                if (productType.toLowerCase().contains("physical"))
                    Bookin_PhysicalProduct(testSuite, productsPerBatch, csvRowNumber);
                else {
                    if (testSuite.toLowerCase().contains("dataentry"))
                        Bookin_SuppliedContent(testSuite, productsPerBatch, csvRowNumber, "No Copy Over", "Copy Over No Amends", "Copy Over No Amends");
                    else
                        Bookin_SuppliedContent(testSuite, productsPerBatch, csvRowNumber, "No Copy Over", "No Copy Over", "No Copy Over");
                }
                if (productsPerBatch > 1)
                    csvRowNumber = csvRowNumber + productsPerBatch;
                else
                    csvRowNumber++;
            }
            return true;
        }
        catch (Exception e){
        e.printStackTrace();
        throw e;
    }
    }
    public void Bookin_PhysicalProduct(String testSuite, int numOfProducts,int csvRowNumber) throws Exception{
        try {
            int j=csvRowNumber,p=csvRowNumber,m=csvRowNumber;
                browserActions.selectFromDropdown(selectBatchType, "Physical Product");
                extentTest.log(Status.INFO,"Physical Product selected from dropdown");
                browserActions.click(btnAccept);
                Thread.sleep(5000);
                browserActions.waitForElementToBeVisible(getProducts);
                browserActions.click(getProducts);
                for (int n = 1; n <= numOfProducts; n++) {
                    String var_EAN = csvDataManager.getCSVData(testSuite, j)[1];
                    j++;
                    By productCode = By.xpath("//div[@id=\"productSelectionGrid\"]//div[(text()='" + var_EAN + "')]");

                    browserActions.click(productCode);
                }
                browserActions.click(okBtn);
                browserActions.click(acceptBtn);
                Thread.sleep(5000);
                browserActions.waitForElementToDisappear(msgBar);
                String batchId = getBatchNum();
                browserActions.waitForElementToDisappear(msgBar);
                browserActions.waitForElementToBeVisible(orderOption);
                browserActions.click(orderOption);
                browserActions.selectFromDropdown(orderOption, "Batch");
                browserActions.clearTextBox(orderNumSearchBox);
                browserActions.sendKeys(orderNumSearchBox,batchId);
                browserActions.click(searchBtn);
                browserActions.waitForElementToBeVisible(notes);
                browserActions.waitForElementToBeVisible(batchDetails);
                browserActions.click(batchDetails);
                for(int x = 1; x<= numOfProducts; x++) {
                    String ean=csvDataManager.getCSVData(testSuite,m)[1];
                    m++;
                    By pvid =By.xpath("//div[normalize-space()='"+ean+"']/parent::div//div[@class='slick-cell l1 r1']");
                    browserActions.waitForElementToBeVisible(pvid);
                    String text = browserActions.getElementText(pvid);
                    String actual = text.replaceAll("[^0-9]", "");
                    CSVDataManager.updateCSV(testSuite,p, 1, 5, actual);
                    CSVDataManager.updateCSV(testSuite, p, 1, 4, batchId);
                    p++;
                }
        }
        catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    public void Bookin_SuppliedContent(String testSuite,int numOfProducts,int csvRowNumber,String dataCopyOrAmend, String mkgCopyOrAmend, String merchCopyOrAmend) throws Exception{
        try {
            int j=csvRowNumber,p=csvRowNumber,m=csvRowNumber;

                browserActions.selectFromDropdown(selectBatchType, "Supplied Content");
                extentTest.log(Status.INFO,"Supplied Content selected from dropdown");
                browserActions.click(acceptBtn);
                Thread.sleep(5000);
                browserActions.waitForElementToBeVisible(getProducts);
                browserActions.click(getProducts);
                for (int n = 1; n <= numOfProducts; n++) {
                    String var_EAN_ProductSelectionGrid = csvDataManager.getCSVData(testSuite, j)[1];
                    j++;
                    By productCode = By.xpath("//div[@id=\"productSelectionGrid\"]//div[(text()='" + var_EAN_ProductSelectionGrid + "')]");
                    browserActions.click(productCode);
                }
                browserActions.click(okBtn);

                if(dataCopyOrAmend.toLowerCase().contains("no copy"))
                    browserActions.click(dataNoCopyOver);
                else if (dataCopyOrAmend.toLowerCase().contains("no amend"))
                    browserActions.click(dataCopyOverNoAmends);
                else
                    browserActions.click(dataCopyAndAmend);

                if(mkgCopyOrAmend.toLowerCase().contains("no copy"))
                    browserActions.click(mkgNoCopyOver);
                else if (mkgCopyOrAmend.toLowerCase().contains("no amend"))
                    browserActions.click(mkgCopyOverNoAmends);
                else
                    browserActions.click(mkgCopyAndAmend);

                if(merchCopyOrAmend.toLowerCase().contains("no copy"))
                    browserActions.click(merchNoCopyOver);
                else if (merchCopyOrAmend.toLowerCase().contains("no amend"))
                    browserActions.click(merchCopyOverNoAmends);
                else
                    browserActions.click(merchCopyAndAmend);

                browserActions.click(acceptBtn);
                Thread.sleep(6000);
                browserActions.click(acceptBtn);
                Thread.sleep(10000);
                browserActions.waitForElementToDisappear(msgBar);
                browserActions.waitForElementToBeVisible(clickOnHeader);
                browserActions.moveToElement(clickOnHeader);
                browserActions.click(clickOnHeader);
                String batchId = getBatchNum();
                browserActions.waitForElementToDisappear(msgBar);
                browserActions.waitForElementToBeVisible(orderOption);
                browserActions.click(orderOption);
                browserActions.selectFromDropdown(orderOption, "Batch");
                browserActions.clearTextBox(orderNumSearchBox);
                browserActions.sendKeys(orderNumSearchBox,batchId);
                browserActions.click(searchBtn);
                Thread.sleep(10000);
                browserActions.waitForElementToBeVisible(notes);
                browserActions.waitForElementToBeVisible(batchDetails);
                browserActions.click(batchDetails);
                for(int x = 1; x<= numOfProducts; x++) {

                    String ean=csvDataManager.getCSVData(testSuite,m)[1];
                    m++;
                    By pvid =By.xpath("//div[normalize-space()='"+ean+"']/parent::div//div[@class='slick-cell l1 r1']");
                    browserActions.waitForElementToBeVisible(pvid);
                    String pvidFromScreen = browserActions.getElementText(pvid);
                    String pvidToWriteToCsv = pvidFromScreen.replaceAll("[^0-9]", "");
                    CSVDataManager.updateCSV(testSuite,p, 1, 5, pvidToWriteToCsv);
                    CSVDataManager.updateCSV(testSuite, p, 1, 4, batchId);
                    p++;
                }
        }
        catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

}
