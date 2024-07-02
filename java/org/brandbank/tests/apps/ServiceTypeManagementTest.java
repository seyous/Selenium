package org.brandbank.tests.apps;

import com.aventstack.extentreports.Status;
import org.brandbank.libs.*;
import org.brandbank.pages.apps.AppsLoginPage;
import org.brandbank.pages.apps.ServiceTypeManagementPage;
import org.brandbank.pages.apps.TasklistSearchAndAssignPage;
import org.brandbank.tests.TestBaseSetup;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;

public class ServiceTypeManagementTest extends TestBaseSetup {
    AppsLoginPage appsLoginPage;
    TasklistSearchAndAssignPage tasklistSearchAndAssignPage;
    BrowserActions browserActions;
    ServiceTypeManagementPage serviceTypeManagementPage;

    public ServiceTypeManagementTest() {
        appsLoginPage = new AppsLoginPage(driver);
        tasklistSearchAndAssignPage = new TasklistSearchAndAssignPage(driver);
        serviceTypeManagementPage = new ServiceTypeManagementPage(driver);
        browserActions = new BrowserActions(driver);
    }

    @Test
    @Parameters({"username"})
    public void editServiceType(String username) throws Exception {
        try {
            String filePath;
            if (username.equalsIgnoreCase("user1")) {
                filePath = System.getProperty("user.dir") + "/src/test/resources/testdata/ServiceTypeManagement/UserA.properties";
            } else {
                filePath = System.getProperty("user.dir") + "/src/test/resources/testdata/ServiceTypeManagement/UserB.properties";
            }
            tasklistSearchAndAssignPage.selectTasklist("Service Type Management");
            boolean flag = serviceTypeManagementPage.searchServiceType();
            Assert.assertEquals(flag, true);
            serviceTypeManagementPage.changeStatus("Reactivate");
            boolean editUpdateFlag = serviceTypeManagementPage.editServiceType(filePath);
            Assert.assertEquals(editUpdateFlag, true);
            boolean viewHistoryFlag = serviceTypeManagementPage.viewEditHistory(username);
            Assert.assertEquals(viewHistoryFlag, true);
            serviceTypeManagementPage.changeStatus("Disable");
            appsLoginPage.logOut();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @AfterMethod
    public void afterMethod(ITestResult result) {
        if (!result.isSuccess())
            System.setProperty("TestStatus", "false");
    }
}
