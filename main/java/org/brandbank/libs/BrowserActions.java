package org.brandbank.libs;

import org.brandbank.base.BaseClass;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BrowserActions  {
    WebDriver driver;

    public BrowserActions(WebDriver driver) {
        this.driver = driver;
    }

    public void sendKeys(By locator, String data) {
        waitForElementToBeVisible(locator).sendKeys(data);
    }

    public void clearTextBox(By locator) {
        //waitForElementToBeVisible(locator).clear();
        waitForElementToBePresent(locator).clear();
    }
    public void enterKey(By locator) {
        waitForElementToBeVisible(locator).sendKeys(Keys.ENTER);
    }
    public void sendKeysByJavascript(By locator, String data) {
        WebElement element = waitForElementToBePresent(locator);
        JavascriptExecutor javascriptExecutor = (JavascriptExecutor) BaseClass.driver;
        javascriptExecutor.executeScript("arguments[0].value='"+data+"';",element);
    }
    public void sendKeysToUpload(By locator, String filePath) {
        WebElement element =waitForElementToBePresent(locator);
        element.sendKeys(filePath);
    }
    public String getClassName(By locator) {
        String className = waitForElementToBeVisible(locator).getAttribute("className");
        return className;
    }
    public boolean pressEnterUsingRobotClass(){
        try {
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_ENTER);
            return  true;
        }
        catch (AWTException e){
            return false;
        }
    }
    public void click(By locator) {
        waitForElementToBeClickable(locator).click();
    }

    public String getElementText(By locator) {
        String text = waitForElementToBeVisible(locator).getText();
        return text;
    }
    public void clickByJavascript(By locator) {
        WebElement element = waitForElementToBePresent(locator);
        JavascriptExecutor javascriptExecutor = (JavascriptExecutor) BaseClass.driver;
        javascriptExecutor.executeScript("arguments[0].click();",element);
    }

    public void moveToElement(By locator){
        Actions actions = new Actions(BaseClass.driver);
        actions.moveToElement(BaseClass.driver.findElement(locator));
        actions.perform();
    }

    public void moveToElementByOffset(By locator,  int y ) throws InterruptedException {
        WebElement element = BaseClass.driver.findElement(locator);
        int width = element.getSize().getWidth();
        Actions actions = new Actions(BaseClass.driver);
        Thread.sleep(8000);
        actions.moveToElement(element).moveByOffset((width/2)-45, y).click().perform();
    }
    public void mouseDragToSelect(By locator, int x, int y, int a, int b){
        Actions builder=new Actions(BaseClass.driver);
        WebElement element =BaseClass.driver.findElement(locator);
        builder.moveToElement(element).moveByOffset(x,y).clickAndHold().moveByOffset(a,b).release().build().perform();
    }

    public boolean isAlertPresent() {
        try {
            Alert alert = BaseClass.driver.switchTo().alert();
            alert.accept();
            return true;
        }
        catch (NoAlertPresentException e) {
            return false;
        }
    }

    public String createXpath(String xpathExp, Object ...args){
        for(int i=0;i<args.length;i++){
            xpathExp = xpathExp.replace("{"+i+"}", (CharSequence) args[i]);
        }
        return xpathExp;
    }
    public boolean isDisplayed(String locator){
        boolean result = false;
        try {
            result = BaseClass.driver.findElement(By.xpath(locator)).isDisplayed();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }
    public boolean isSelected(By locator){
        return BaseClass.driver.findElement(locator).isSelected();
    }
    public String getPageUrl(){
        return  BaseClass.driver.getCurrentUrl();
    }
    public String getPageTitle() { return  BaseClass.driver.getTitle();}
    public void waitForFrameToBeAvailable(String xpath){
        WebElement element=BaseClass.driver.findElement(By.xpath(xpath));
        BaseClass.wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(element));
    }
    public WebElement waitForElementToBeVisible(By locator) {
        return BaseClass.wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }
    public Boolean waitForElementToBeInvisible(By locator) {
        return BaseClass.wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    public Boolean waitForElementToDisappear(By locator){
        return BaseClass.wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }
    public WebElement waitForElementToBeClickable(By locator) {
        return BaseClass.wait.until(ExpectedConditions.elementToBeClickable(locator));
    }
    public WebElement waitForElementToBePresent(By locator) {
        return BaseClass.wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }
    public void selectFromDropdown(By locator, String data) {
        Select select = new Select(waitForElementToBeVisible(locator));
        select.selectByVisibleText(data);
    }
    public void selectFromDropdownByIndex(By locator, int index) {
        Select select = new Select(waitForElementToBeVisible(locator));
        select.selectByIndex(index);
    }
    public String getFirstSelectedOption(By locator) {
        Select select = new Select(waitForElementToBeVisible(locator));
        WebElement firstSelectedOption = select.getFirstSelectedOption();
        String firstOption = firstSelectedOption.getText();
        return firstOption;
    }
    public int getDropDownListCount(By locator){
        Select select = new Select(waitForElementToBeVisible(locator));
        List<WebElement> elements = select.getOptions();
        return elements.size();
    }
    public void scrollToView(By locator){
        WebElement element = waitForElementToBeVisible(locator);
        JavascriptExecutor js = (JavascriptExecutor) BaseClass.driver;
        js.executeScript("arguments[0].scrollIntoView({ behavior: 'smooth', block: 'end', inline: 'nearest' });",element);
    }
    
    public void escapeKey(){
        Actions action = new Actions(BaseClass.driver);
        action.sendKeys(Keys.ESCAPE).build().perform();
    }
    public void switchToDefaultContent(){
        BaseClass.driver.switchTo().defaultContent();
    }

    public void switchToTab(int firstTab,int secondTab){
        Set<String> windowIDs = BaseClass.driver.getWindowHandles();
        List<String> windowIDsList = new ArrayList(windowIDs);
        String firstTabID = windowIDsList.get(firstTab);
        String secondTabId = windowIDsList.get(secondTab);
        BaseClass.driver.switchTo().window(secondTabId);
    }
    public void switchToParticularTab(int tab){
        Set<String> windowIDs = BaseClass.driver.getWindowHandles();
        List<String> windowIDsList = new ArrayList(windowIDs);
        String tabID = windowIDsList.get(tab);
        BaseClass.driver.switchTo().window(tabID);
    }
    public void dragAndDrop(By source,By target){
        Actions act=new Actions(BaseClass.driver);
        act.dragAndDrop(BaseClass.driver.findElement(source),BaseClass.driver.findElement(target));
        act.perform();
    }
    public void refreshPage(){
        BaseClass.driver.navigate().refresh();
    }

    public void rightClick(By locator){
        Actions action = new Actions(BaseClass.driver);
        action.contextClick(BaseClass.driver.findElement(locator)).perform();
    }

    public void pressTabKey(){
        Actions action = new Actions(BaseClass.driver);
        action.sendKeys(Keys.TAB).build().perform();
    }

    public String getBrowserLocale(){
        JavascriptExecutor executor = (JavascriptExecutor) BaseClass.driver;
        String language = executor.executeScript("return window.navigator.userlanguage || window.navigator.language").toString();
        return language;
    }

    public void doubleClick(By locator){
        Actions action = new Actions(BaseClass.driver);
        action.moveToElement(BaseClass.driver.findElement(locator)).doubleClick().build().perform();
    }

    public void backPage(){
        BaseClass.driver.navigate().back();
    }

    public boolean isDisplayedNew(By locator){
        boolean result = false;
        try {
            result = BaseClass.driver.findElement(locator).isDisplayed();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public boolean isEnabled(By locator){
        return BaseClass.driver.findElement(locator).isEnabled();
    }

    public void close(){
        BaseClass.driver.close();
    }

    public  void tabKeys(By locator,int num) {
        WebElement targetElement =BaseClass.driver.findElement(locator);
        Actions actions = new Actions(BaseClass.driver).sendKeys(targetElement, Keys.TAB);
        for(int i =1 ; i<=num ; i++)
        {
            actions.sendKeys(Keys.TAB);
        }
        actions.sendKeys(Keys.ENTER);
        actions.build().perform();
    }

    public String getCssValue(By locator,String cssValue) {
        String value = waitForElementToBeVisible(locator).getCssValue(cssValue);
        return value;
    }

    public List<WebElement> listOfElementToBePresent(By locator) {
        return BaseClass.driver.findElements(locator);
    }
}
