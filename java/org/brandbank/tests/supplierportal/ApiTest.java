package org.brandbank.tests.supplierportal;

import com.aventstack.extentreports.Status;
import org.apache.commons.io.IOUtils;
import org.brandbank.libs.CSVDataManager;
import org.brandbank.libs.FileHandling;
import org.brandbank.libs.PropertyManager;
import org.brandbank.pages.LoginPage;
import org.brandbank.pages.blackbox.ExtractsPage;
import org.brandbank.pages.supplierportal.ApiRequestPage;
import org.brandbank.tests.TestBaseSetup;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.*;
import java.util.Properties;

public class ApiTest extends TestBaseSetup {
    ApiRequestPage apiRequestPage;
    CSVDataManager csvDataManager;
    LoginPage loginPage;
    ExtractsPage extractsPage;
    FileHandling fileHandling;
    Properties configProperties;
    PropertyManager propertyManager;

    public ApiTest() {
        apiRequestPage = new ApiRequestPage();
        csvDataManager = new CSVDataManager();
        loginPage=new LoginPage(driver);
        extractsPage=new ExtractsPage(driver);
        fileHandling=new FileHandling();
        propertyManager = new PropertyManager();

    }
    @BeforeTest
    @Parameters({"test"})
    private String getFunctionKey() {
        configProperties = PropertyManager.getPropertiesData();
        String functionKey= configProperties.getProperty("environment")+".evolve.functionKey";
        return configProperties.getProperty(functionKey);
    }

    @Test
    @Parameters({"testSuite","numOfProducts"})
    public void getUnsentPostRequestXml(String testSuite,int numOfProducts) throws Exception {
        try {
            String env;
            configProperties = PropertyManager.getPropertiesData();
            env = configProperties.getProperty("environment");
            switch(env){
                case "stg":
                       String stgUrl = "https://api.stgbrandbank.com/svc/feed/extractdata.asmx?op=GetUnsentProductData";
                       boolean pvidCheck = apiRequestPage.getUnsentPostRequest(testSuite, stgUrl,numOfProducts);
                       Assert.assertEquals(pvidCheck, true);
                       extentTest.log(Status.PASS,"pvid found");
                       break;

                case "prod":
                        String prodUrl = "https://api.brandbank.com/svc/feed/extractdata.asmx?op=GetUnsentProductData";
                        boolean pvidFound = apiRequestPage.getUnsentPostRequest(testSuite,prodUrl,numOfProducts);
                        Assert.assertEquals(pvidFound, true);
                        extentTest.log(Status.PASS,"pvid found");
                        break;
                default:
                        System.out.println("no env notified");
                        break;
            }
            AcknowledgeMessagePostRequestXml(testSuite);

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

    }

    public void AcknowledgeMessagePostRequestXml(String testSuite) throws Exception {
        try {
            String env;
            configProperties = PropertyManager.getPropertiesData();
            env = configProperties.getProperty("environment");
            switch(env){
                case "stg":
                    String stgUrl = "https://api.stgbrandbank.com/svc/feed/extractdata.asmx?op=AcknowledgeMessage";
                    int statusCode = apiRequestPage.AcknowledgeMsgPostRequest(stgUrl,testSuite);
                    Assert.assertEquals(statusCode, 200);
                    extentTest.log(Status.PASS,"200 status in response");
                    break;

                case "prod":
                    String prodUrl = "https://api.brandbank.com/svc/feed/extractdata.asmx?op=AcknowledgeMessage";
                    int status = apiRequestPage.AcknowledgeMsgPostRequest(prodUrl,testSuite);
                    Assert.assertEquals(status,200);
                    extentTest.log(Status.PASS,"200 status in response");
                    break;
                default:
                    System.out.println("no env notified");
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

    }
    @Test
    @Parameters({"testSuite"})
    public void deleteProductsQueueRequestJson(String testSuite) throws Exception {
        try {
            String env;
            configProperties = PropertyManager.getPropertiesData();
            env = configProperties.getProperty("environment");
            switch(env){
                case "stg":
                    String functionKeyStg =getFunctionKey();
                    String stgUrl = "https://connectapi.stgbrandbank.com/api/reset";
                    int statusCode = apiRequestPage.resetQueueRequest(testSuite, functionKeyStg,stgUrl);
                    Assert.assertEquals(statusCode, 200);
                    extentTest.log(Status.PASS,"200 status in response");
                    break;
                case "prod":
                    String functionKeyProd =getFunctionKey();
                    String prodUrl = "https://connectapi.brandbank.com/api/reset";
                    int status = apiRequestPage.resetQueueRequest(testSuite,functionKeyProd,prodUrl);
                    Assert.assertEquals(status,200);
                    extentTest.log(Status.PASS,"200 status in response");
                    break;
                default:
                    System.out.println("no env notified");
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

    }


    @Test
    @Parameters({"jsonExtractName"})
    public void runExtractAndDownloadJson(String jsonExtractName) throws Exception {
        try {
            String downloadedFile = System.getProperty("user.dir") + "/downloads/products.json";
            fileHandling.deleteFile(downloadedFile);
            extractsPage.searchExtract(jsonExtractName);
            boolean successRowFound = extractsPage.downloadJson(jsonExtractName);
            Assert.assertEquals(successRowFound, true);
            extractsPage.logOut();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    @Test
    @Parameters({"testSuite","numOfProducts"})
    public void validatePVIDFromJson(String testSuite,int numOfProducts) throws Exception {
        try {
            for(int i=1;i<=numOfProducts;i++) {
                String downloadedFile = System.getProperty("user.dir") + "\\downloads\\products.json";
                String pvid = csvDataManager.getCSVData(testSuite, i)[5];
                Thread.sleep(5000);

                boolean jsonDownloaded = fileHandling.verifyFileExists(downloadedFile);
                Assert.assertEquals(jsonDownloaded, true);
                System.out.println("Successfully downloaded product.json file from GetUnsent_JSON extract");
                extentTest.log(Status.PASS,"Successfully downloaded product.json file from GetUnsent_JSON extract");

                FileInputStream fis = new FileInputStream(downloadedFile);
                String data = IOUtils.toString(fis, "UTF_16LE");
                String targetFile = System.getProperty("user.dir") + "\\downloads\\evolveModified.json";
                csvDataManager.writeToFile(targetFile, data);


                boolean pvidVerified = fileHandling.verifyFileContent(pvid, targetFile);
                Assert.assertEquals(pvidVerified, true);
                extentTest.log(Status.PASS,"pvid found and verified");
                System.out.println(pvid + ":pvid found in products.json file");
            }
        }
        catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }
    @Test
    @Parameters({"testSuite","numOfProducts"})
    public void getUnsentGetRequestJson(String testSuite, int numOfProducts) throws Exception {
        try {
            String env;
            configProperties = PropertyManager.getPropertiesData();
            env = configProperties.getProperty("environment");
            switch(env){
                case "stg":
                      String stgUrl="https://connectapi.stgbrandbank.com/api/";
                      boolean pvidCheck = apiRequestPage.getUnSentGetRequest(testSuite,stgUrl,numOfProducts);
                      Assert.assertEquals(pvidCheck,true);
                      extentTest.log(Status.PASS,"pvid found");
                      break;

                case "prod":
                       String prodUrl ="https://connectapi.brandbank.com/api/";
                       boolean pvidFound = apiRequestPage.getUnSentGetRequest(testSuite, prodUrl,numOfProducts);
                       Assert.assertEquals(pvidFound, true);
                      extentTest.log(Status.PASS,"pvid found");
                       break;
                default:
                       System.out.println("no env notified");
                       extentTest.log(Status.FAIL,"pvid found and verified");
                       break;
            }
        } catch (Exception e) {
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
