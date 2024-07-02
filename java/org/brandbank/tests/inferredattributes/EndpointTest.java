package org.brandbank.tests.inferredattributes;

import com.aventstack.extentreports.Status;
import org.brandbank.libs.PropertyManager;
import org.brandbank.pages.inferredattributes.ApiPage;
import org.brandbank.tests.TestBaseSetup;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.Properties;

public class EndpointTest extends TestBaseSetup {
      ApiPage apiPage;
      Properties configProperties;

      public EndpointTest() {
            apiPage = new ApiPage();
      }

      @Test
      @Parameters({"testSuite"})
      public void validateLicenses(String testSuite) throws Exception{
            try {
                  boolean bearerToken=apiPage.auth0("Licenses","resource","client_id", "grant_type", "client_credentials", "client_secret");
                  Assert.assertEquals(bearerToken,true);
                  extentTest.log(Status.PASS, "token returned");
                  String env;
                  configProperties = PropertyManager.getPropertiesData();
                  env = configProperties.getProperty("environment");
                  switch (env) {
                        case "stg":
                              String stgUrl = "https://license-comp-stg-eun-func.azurewebsites.net/api/GetActiveLicenseInfoByServiceCode";
                              boolean stgServiceItemLicensed =apiPage.validateLicenses(testSuite, stgUrl);
                              Assert.assertEquals(stgServiceItemLicensed,true);
                              extentTest.log(Status.PASS, "service items licensed");
                              break;

                        case "prod":
                              String prodUrl = "https://license-comp-prod-eun-func.azurewebsites.net/api/GetActiveLicenseInfoByServiceCode";
                              boolean prodServiceItemLicensed=apiPage.validateLicenses(testSuite, prodUrl);
                              Assert.assertEquals(prodServiceItemLicensed, true);
                              extentTest.log(Status.PASS, "service items licensed");
                              break;
                        default:
                              System.out.println("no env notified");
                              break;
                  }
            }

            catch (Exception e) {
                  e.printStackTrace();
                  throw e;
            }
     }
      @Test
      @Parameters({"testSuite"})
      public void requestHFSSAttributes(String testSuite) throws Exception{
            try {
                  boolean bearerToken=apiPage.auth0("HFSS","resource","client_id", "grant_type", "client_credentials", "client_secret");
                  Assert.assertEquals(bearerToken,true);
                  extentTest.log(Status.PASS, "token returned");
                  String env;
                  boolean pvidReturned = apiPage.getHFSSAttributes(testSuite);
                  Assert.assertEquals(pvidReturned, true);
                  extentTest.log(Status.PASS, "pvid returned and completed");
            }
            catch (Exception e) {
                  e.printStackTrace();
                  throw e;
            }

      }
      @Test
      @Parameters({"testSuite"})
      public void publishEndpoint(String testSuite) throws Exception{
            try {
                  boolean bearerToken=apiPage.auth0("publishEvent","resource","client_id", "grant_type", "client_credentials", "client_secret");
                  Assert.assertEquals(bearerToken,true);
                  extentTest.log(Status.PASS, "token returned");
                  String env;
                  configProperties = PropertyManager.getPropertiesData();
                  env = configProperties.getProperty("environment");
                  boolean statusCode;
                  switch (env) {
                        case "stg":
                              String stgUrl = "https://wsconnect-comp-stg-eun-func.azurewebsites.net/api/PublishEvent";
                              statusCode=apiPage.addAttributesAndPublish(testSuite,stgUrl,"publishEvent");
                              Assert.assertEquals(statusCode,true);
                              extentTest.log(Status.PASS, "status as expected");
                              break;

                        case "prod":
                              String prodUrl = "https://wsconnect-comp-prod-eun-func.azurewebsites.net/api/PublishEvent";
                              statusCode=apiPage.addAttributesAndPublish(testSuite,prodUrl,"publishEvent");
                              Assert.assertEquals(statusCode,true);
                              extentTest.log(Status.PASS, "status as expected");
                              break;
                        default:
                              System.out.println("no env notified");
                              extentTest.log(Status.FAIL, "no env notified");
                              break;
                  }
            }
            catch (Exception e) {
                  e.printStackTrace();
                  throw e;
            }

      }
      @Test
      @Parameters({"testSuite"})
      public void foodMaestroAttributes(String testSuite) throws Exception{
            try {
                  boolean bearerToken=apiPage.auth0("FMAndWSAndV3Test","resource","client_id", "grant_type", "client_credentials", "client_secret");
                  Assert.assertEquals(bearerToken,true);
                  extentTest.log(Status.PASS, "token returned");
                  String env;
                  configProperties = PropertyManager.getPropertiesData();
                  env = configProperties.getProperty("environment");
                  boolean statusCode;
                  switch (env) {
                        case "stg":
                              String stgUrl = "https://eattributes-comp-stg-eun-func.azurewebsites.net/api/AddAttributesByPvid";
                              statusCode=apiPage.addAttributesAndPublish(testSuite,stgUrl,"foodMaestro");
                              Assert.assertEquals(statusCode,true);
                              extentTest.log(Status.PASS, "status as expected");
                              break;

                        case "prod":
                              String prodUrl = "https://eattributes-comp-prod-eun-func.azurewebsites.net/api/AddAttributesByPvid";
                              statusCode=apiPage.addAttributesAndPublish(testSuite,prodUrl,"foodMaestro");
                              Assert.assertEquals(statusCode,true);
                              extentTest.log(Status.PASS, "status as expected");
                              break;
                        default:
                              System.out.println("no env notified");
                              extentTest.log(Status.FAIL, "no env notified");
                              break;
                  }
            }
            catch (Exception e) {
                  e.printStackTrace();
                  throw e;
            }

      }
      @Test
      @Parameters({"testSuite"})
      public void whyteSpyderAttributes(String testSuite) throws Exception{
            try {
                  boolean bearerToken=apiPage.auth0("FMAndWSAndV3Test","resource","client_id", "grant_type", "client_credentials", "client_secret");
                  Assert.assertEquals(bearerToken,true);
                  extentTest.log(Status.PASS, "token returned");
                  String env;
                  configProperties = PropertyManager.getPropertiesData();
                  env = configProperties.getProperty("environment");
                  boolean statusCode;
                  switch (env) {
                        case "stg":
                              String stgUrl = "https://eattributes-comp-stg-eun-func.azurewebsites.net/api/AddAttributesByProductID";
                              statusCode=apiPage.addAttributesAndPublish(testSuite,stgUrl,"whytespyder");
                              Assert.assertEquals(statusCode,true);
                              extentTest.log(Status.PASS, "status as expected");
                              break;

                        case "prod":
                              String prodUrl = "https://eattributes-comp-prod-eun-func.azurewebsites.net/api/AddAttributesByProductID";
                              statusCode=apiPage.addAttributesAndPublish(testSuite,prodUrl,"whytespyder");
                              Assert.assertEquals(statusCode,true);
                              extentTest.log(Status.PASS, "status as expected");
                              break;
                        default:
                              System.out.println("no env notified");
                              extentTest.log(Status.FAIL, "no env notified");
                              break;
                  }
            }
            catch (Exception e) {
                  e.printStackTrace();
                  throw e;
            }

      }
      @Test
      @Parameters({"testSuite"})
      public void getV3ProductsContent(String testSuite) throws Exception{
            try {
                  boolean bearerToken=apiPage.auth0("FMAndWSAndV3Test","resource","client_id", "grant_type", "client_credentials", "client_secret");
                  Assert.assertEquals(bearerToken,true);
                  extentTest.log(Status.PASS, "token returned");
                  String env;
                  configProperties = PropertyManager.getPropertiesData();
                  env = configProperties.getProperty("environment");
                  boolean statusCode;
                  switch (env) {
                        case "stg":
                              String stgUrl = "https://eattributes-comp-stg-eun-func.azurewebsites.net/api/GetProductsV3";
                              statusCode=apiPage.addAttributesAndPublish(testSuite,stgUrl,"GetProductsV3");
                              Assert.assertEquals(statusCode,true);
                              extentTest.log(Status.PASS, "status as expected");
                              break;

                        case "prod":
                              String prodUrl = "https://eattributes-comp-prod-eun-func.azurewebsites.net/api/GetProductsV3";
                              statusCode=apiPage.addAttributesAndPublish(testSuite,prodUrl,"GetProductsV3");
                              Assert.assertEquals(statusCode,true);
                              extentTest.log(Status.PASS, "status as expected");
                              break;
                        default:
                              System.out.println("no env notified");
                              extentTest.log(Status.FAIL, "no env notified");
                              break;
                  }
            }
            catch (Exception e) {
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

