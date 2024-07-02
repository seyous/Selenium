package org.brandbank.libs;

import com.aventstack.extentreports.Status;
import org.brandbank.base.BaseClass;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

public class LaunchTrayApp extends BaseClass {
    Properties configProperties;
    String env;
    String trayAppLocation;

    public boolean openTrayApp() throws IOException, InterruptedException {
        try {
            configProperties = PropertyManager.getPropertiesData();
            env = configProperties.getProperty("environment");
            if (env.equalsIgnoreCase("prod")) {
                trayAppLocation = System.getProperty("user.dir") + "\\src\\test\\resources\\TrayAppProd\\Brandbank.Messaging.Tray.exe";
            } else {
                trayAppLocation = System.getProperty("user.dir") + "\\src\\test\\resources\\TrayAppStg\\StgBrandbank.Messaging.Tray.exe";
            }

            //Launch TrayApp
            boolean trayAppLaunched = false;
            Runtime.getRuntime().exec(trayAppLocation);
            Thread.sleep(10000);
            String line;
            Process p = Runtime.getRuntime().exec(System.getenv("windir") + "\\system32\\" + "tasklist.exe");
            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((line = input.readLine()) != null) {
                if (line.contains("Brandbank.Messaging.Tr")) {
                    System.out.println(line);
                    extentTest.log(Status.PASS, "Successfully launched TrayApp");
                    System.out.println("Successfully launched TrayApp");
                    trayAppLaunched = true;
                    break;
                }
            }
            input.close();
            if (trayAppLaunched == false) {
                extentTest.log(Status.FAIL, "Failed to launch TrayApp");
                System.out.println("Failed to launch TrayApp");
                return false;
            } else
                return true;
        }
        catch (Exception e){
            System.setProperty("TestStatus", "false");
            extentTest.log(Status.FAIL, e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
}
