package org.brandbank.pages.dataentry;

import com.aventstack.extentreports.Status;
import com.opencsv.CSVReader;
import org.brandbank.base.BaseClass;
import org.brandbank.libs.BrowserActions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import java.io.FileReader;

public class DEProductMeasurerValidationPage extends BaseClass {
    BrowserActions browserActions;
    public static WebDriver driver;
    public DEProductMeasurerValidationPage(WebDriver driver) {
        this.driver = driver;
        browserActions = new BrowserActions(this.driver);
        PageFactory.initElements(driver,this);
    }
    By standardDimensionsTab = By.xpath("//span[normalize-space()='Standard Dimensions']");

    public void standardDimensionsValidation() throws Exception {
        try {
            String filePath = System.getProperty("user.dir") + "/src/test/resources/testdata/ProductMeasurer/Dimensions_DE.csv";
            CSVReader reader = new CSVReader(new FileReader(filePath));
            String[] cell;
            String value;
            int numoOfRows = 0;
            while ((cell = reader.readNext()) != null) {
                if (numoOfRows != 0) {
                    for (int i = 0; i < 1; i++) {
                        value = cell[i];
                        By dimensions=By.xpath("(//input[@value='"+value+"'])[1]");
                        browserActions.waitForElementToBeVisible(standardDimensionsTab);
                        browserActions.click(standardDimensionsTab);
                        browserActions.waitForElementToBeVisible(dimensions);
                        System.out.println(value+": is present");
                        extentTest.log(Status.INFO,value+": is present");
                    }
                }
                numoOfRows++;
            }

        }
        catch (Exception e) {
            e.printStackTrace();
            throw e;

        }
    }

}
