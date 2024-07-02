package org.brandbank.libs;

import com.aventstack.extentreports.Status;
import org.brandbank.base.BaseClass;
import java.io.*;





public class ImageShotConsoleApp extends BaseClass  {

    public boolean imageShotConsole() throws IOException, InterruptedException  {
        try {
            boolean launchImageshot = false;
            for(int count=1; count<=3; ++count ) {
                String imageshotApplication = "cmd /c powershell -ExecutionPolicy RemoteSigned -noprofile -noninteractive" + " " + System.getProperty("user.dir") + "\\src\\test\\resources\\imageshot\\imageshot1.ps1";
                Process imageConsoleProcess = Runtime.getRuntime().exec(imageshotApplication);
                Thread.sleep(40000);
                Process imageTaskProcess = Runtime.getRuntime().exec(System.getenv("windir") + "\\system32\\" + "tasklist.exe");
                BufferedReader reader = new BufferedReader(new InputStreamReader(imageTaskProcess.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.contains("ImageShotBlobPopulatorCon")) {
                        System.out.println(line);
                        extentTest.log(Status.PASS, "Successfully launched imageshotApplication");
                        System.out.println("Successfully launched imageshotApplication");
                        launchImageshot = true;
                        break;
                    }
                } imageConsoleProcess.getOutputStream().close();
                if(imageConsoleProcess.exitValue()==0){
                    Runtime.getRuntime().exec("taskkill /f /im ImageShotBlobPopulatorConsoleApp.exe");
                }
            }

            if (launchImageshot == false) {
                extentTest.log(Status.FAIL, "Failed to launch imageshotApplication");
                System.out.println("Failed to launch imageshotApplication");
            } return launchImageshot;
        }
        catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}

