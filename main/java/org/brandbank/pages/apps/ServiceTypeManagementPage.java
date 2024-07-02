package org.brandbank.pages.apps;

import com.aventstack.extentreports.Status;
import org.brandbank.base.BaseClass;
import org.brandbank.libs.BrowserActions;
import org.brandbank.libs.CSVDataManager;
import org.brandbank.libs.DateManager;
import org.brandbank.libs.PropertyManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import java.io.FileInputStream;
import java.util.Properties;

public class ServiceTypeManagementPage extends BaseClass {
    public static WebDriver driver;
    BrowserActions browserActions;
    CSVDataManager csvDataManager;
    DateManager dateManager;
    String time;
    Properties configProperties;

    public ServiceTypeManagementPage(WebDriver driver) {
        this.driver = driver;
        browserActions = new BrowserActions(this.driver);
        csvDataManager = new CSVDataManager();
        dateManager = new DateManager();
        PageFactory.initElements(driver, this);
    }

    By newServiceGroup = By.xpath("//button[contains(text(),'New service group ')]");
    By serviceDescription = By.xpath("//input[@placeholder='description']");
    By subscriberRoutes = By.xpath("//div[contains(text(),'Select subscriber routes ...')]");

    By country = By.xpath("//div[contains(text(),'United Kingdom')]");
    By countryArrow = By.xpath("//div[contains(text(),'United Kingdom')]//ancestor::div[@class='treeview-row row']//descendant::div/span[contains(text(),'keyboard_arrow_right')]");
    By serviceType = By.xpath("//span[contains(text(),'Test Service Type')]");

    By serviceGroupArrow = By.xpath("//div[contains(text(),'Test_Automation')]//ancestor::div[@class='treeview-row row']//descendant::div/span[contains(text(),'keyboard_arrow_right')]");
    By searchBox = By.xpath("//input[@placeholder='search service type']");
    By editButton = By.xpath("//button[contains(text(),'Edit')]");
    By saveButton = By.xpath("//button[contains(text(),'Save Changes')]");
    By reactivateButton = By.xpath("//button[contains(text(),'Reactivate')]");
    By disableButton = By.xpath("//button[contains(text(),'Disable')]");
    By okButton = By.xpath("//button[contains(text(),'OK')]");
    By editServiceType = By.xpath("//button[contains(text(),'Edit ServiceType')]");
    By viewHistoryButton = By.xpath("//button[contains(text(),'View Edit History')]");
    By serviceGroupHeader = By.xpath("//h6[contains(text(),'Test_Automation')]");
    By serviceTypeHeader = By.xpath("//h5[contains(text(),'Test Service Type')]");
    By serviceTier = By.xpath("(//div[@class=' css-nwjfc']/input)[3]");
    By removeTask = By.xpath("(//div[@class='css-1rhbuit-multiValue'])[1]/div[2]");
    By task = By.xpath("(//div[@class=' css-nwjfc']/input)[9]");
    By removeTargetUsage = By.xpath("(//div[@class='css-1rhbuit-multiValue'])[2]/div[2]");
    By targetUsage = By.xpath("(//div[@class=' css-nwjfc']/input)[10]");
    By kgideleteButton = By.xpath("(//button[@class='btn btn-outline-info btn-sm'])[3]");
    By pricedeleteButton = By.xpath("(//button[@class='btn btn-outline-info btn-sm'])[2]");
    By imageActions = By.xpath("(//div[@class=' css-nwjfc']/input)[12]");
    By requiredImage = By.xpath("(//input[@class='form-control numericInput'])[2]");
    By comments = By.xpath("//textarea[@class='form-control input-lg']");
    By prioritySLA = By.xpath("(//div[@class=' css-nwjfc']/input)[13]");
    By updateSavedAlert = By.xpath("//div[contains(text(),'updated')]");
    By priceAddButton = By.xpath("(//button[contains(text(),'Add')])[2]");
    By kgiAddButton = By.xpath("(//button[contains(text(),'Add')])[3]");
    By price = By.xpath("(//input[@class='form-control numericInput'])[1]");
    By priceType = By.xpath("(//div[@class=' css-nwjfc']/input)[4]");
    By revenueCatgeory = By.xpath("(//div[@class=' css-nwjfc']/input)[5]");
    By servicesIncluded = By.xpath("(//div[@class=' css-nwjfc']/input)[6]");
    By kgiType = By.xpath("(//div[@class=' css-nwjfc']/input)[7]");
    By internalServiceGroup = By.xpath("(//div[@class=' css-nwjfc']/input)[8]");
    By historyTableHeader = By.xpath("(//div[@class='card-header'])[1]");
    By backButton = By.xpath("//button[contains(text(),'Back')]");
    By showInactiveToggle = By.xpath("//input[@id='showInactivTypesToggle']");

    public boolean searchServiceType() throws Exception {
        try {
            configProperties = PropertyManager.getPropertiesData();
            String env = configProperties.getProperty("environment");
            browserActions.waitForElementToBeVisible(searchBox);
            browserActions.click(searchBox);
            if (env.equalsIgnoreCase("stg")) {
                browserActions.sendKeys(searchBox, "4138");
            } else {
                browserActions.sendKeys(searchBox, "4308");
            }
            browserActions.click(showInactiveToggle);
            browserActions.waitForElementToBeVisible(countryArrow);
            browserActions.clickByJavascript(countryArrow);
            browserActions.click(countryArrow);
            browserActions.waitForElementToBeVisible(serviceGroupArrow);
            browserActions.click(serviceGroupArrow);
            browserActions.waitForElementToBeVisible(serviceType);
            browserActions.click(serviceType);
            boolean flag = browserActions.isDisplayedNew(serviceTypeHeader) && browserActions.isDisplayedNew(serviceGroupHeader);
            if (flag) {
                System.out.println("Service Type Found");
                extentTest.log(Status.PASS, "Service Type Found");
            } else {
                System.out.println("Service Type Not Found");
                extentTest.log(Status.FAIL, "Service Type Not Found");
            }
            return flag;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public void changeStatus(String status) throws Exception {
        try {
            if (status.equalsIgnoreCase("Disable")) {
                browserActions.waitForElementToBeVisible(disableButton);
                browserActions.click(disableButton);
                browserActions.waitForElementToBeVisible(okButton);
                browserActions.click(okButton);
                Thread.sleep(5000);
                System.out.println(status + " Successfully");
                extentTest.log(Status.PASS, status + " Successfully");
            } else if (status.equalsIgnoreCase("Reactivate")) {
                if (browserActions.isDisplayedNew(reactivateButton)) {
                    browserActions.waitForElementToBeVisible(reactivateButton);
                    browserActions.click(reactivateButton);
                    browserActions.waitForElementToBeVisible(okButton);
                    browserActions.click(okButton);
                    Thread.sleep(5000);
                    System.out.println(status + " Successfully");
                    extentTest.log(Status.PASS, status + " Successfully");
                } else {
                    System.out.println("Service type is already in Activated status");
                    extentTest.log(Status.PASS, "Service type is already in Activated status");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public boolean editServiceType(String filePath) throws Exception {
        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream(filePath));
            browserActions.waitForElementToBeVisible(editServiceType);
            browserActions.click(editServiceType);
            browserActions.waitForElementToBeVisible(serviceTier);
            browserActions.click(serviceTier);
            Thread.sleep(2000);
            browserActions.sendKeys(serviceTier, properties.getProperty("ServiceTier"));
            browserActions.enterKey(serviceTier);
            browserActions.click(pricedeleteButton);
            Thread.sleep(2000);
            browserActions.click(price);
            Thread.sleep(2000);
            browserActions.clearTextBox(price);
            browserActions.sendKeys(price, properties.getProperty("Prices"));
            browserActions.click(priceType);
            Thread.sleep(2000);
            browserActions.sendKeys(priceType, properties.getProperty("PricesType"));
            browserActions.enterKey(priceType);
            browserActions.click(priceAddButton);
            Thread.sleep(5000);
            browserActions.click(revenueCatgeory);
            browserActions.sendKeys(revenueCatgeory, properties.getProperty("RevenueCategory"));
            browserActions.enterKey(revenueCatgeory);
            browserActions.click(servicesIncluded);
            browserActions.sendKeys(servicesIncluded, properties.getProperty("ServicesIncluded"));
            browserActions.enterKey(servicesIncluded);
            Thread.sleep(5000);
            browserActions.click(kgideleteButton);
            browserActions.click(kgiType);
            Thread.sleep(2000);
            browserActions.sendKeys(kgiType, properties.getProperty("KPIGroup"));
            browserActions.enterKey(kgiType);
            browserActions.click(internalServiceGroup);
            Thread.sleep(2000);
            browserActions.sendKeys(internalServiceGroup, properties.getProperty("InternalServiceGroup"));
            browserActions.enterKey(internalServiceGroup);
            Thread.sleep(5000);
            browserActions.click(kgiAddButton);
            Thread.sleep(2000);
            browserActions.click(removeTask);
            browserActions.click(task);
            Thread.sleep(2000);
            browserActions.sendKeys(task, properties.getProperty("Tasks"));
            browserActions.enterKey(task);
            browserActions.click(removeTargetUsage);
            browserActions.click(targetUsage);
            Thread.sleep(2000);
            browserActions.sendKeys(targetUsage, properties.getProperty("TargetUsages"));
            browserActions.enterKey(targetUsage);
            browserActions.click(imageActions);
            Thread.sleep(2000);
            browserActions.sendKeys(imageActions, properties.getProperty("ImageActions"));
            browserActions.enterKey(imageActions);
            browserActions.moveToElement(requiredImage);
            browserActions.click(requiredImage);
            browserActions.clearTextBox(requiredImage);
            Thread.sleep(2000);
            browserActions.sendKeys(requiredImage, properties.getProperty("RequiredImages"));
            browserActions.click(comments);
            browserActions.clearTextBox(comments);
            Thread.sleep(2000);
            browserActions.sendKeys(comments, properties.getProperty("Comments"));
            browserActions.click(prioritySLA);
            Thread.sleep(2000);
            browserActions.sendKeys(prioritySLA, properties.getProperty("PrioritySLA"));
            browserActions.enterKey(prioritySLA);
            browserActions.waitForElementToBeVisible(saveButton);
            browserActions.click(saveButton);
            time = dateManager.getCurrentUtcTime("hh");
            boolean flag = browserActions.isDisplayedNew(updateSavedAlert);
            if (flag) {
                System.out.println("Service Type edited successfully");
                extentTest.log(Status.PASS, "Service Type edited successfully");
            } else {
                System.out.println("Service Type edit failed");
                extentTest.log(Status.FAIL, "Service Type edit failed");
            }
            return flag;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public boolean viewEditHistory(String username) throws Exception {
        try {
            boolean flag;
            String date = dateManager.getCurrentDate("dd/MM/yyyy");
            browserActions.waitForElementToBeVisible(viewHistoryButton);
            browserActions.click(viewHistoryButton);
            Thread.sleep(10000);
            String tableHeader = browserActions.getElementText(historyTableHeader);
            if (username.equalsIgnoreCase("user1")) {
                flag = tableHeader.contains("Svc Autotesta") && tableHeader.contains(date) && tableHeader.contains(time);
            } else {
                flag = tableHeader.contains("Svc Autotestb") && tableHeader.contains(date) && tableHeader.contains(time);
            }
            if (flag) {
                System.out.println("View History table updated successfully for the edit done by " + username);
                extentTest.log(Status.PASS, "Service Type updated successfully for the edit by " + username);
                browserActions.waitForElementToBeVisible(backButton);
                browserActions.click(backButton);
            } else {
                System.out.println("View History table not updated successfully for the edit by " + username);
                extentTest.log(Status.FAIL, "View History table not updated successfully for the edit by " + username);
            }
            return flag;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
