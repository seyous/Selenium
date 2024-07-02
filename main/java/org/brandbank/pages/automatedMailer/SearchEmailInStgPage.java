package org.brandbank.pages.automatedMailer;

import com.aventstack.extentreports.Status;
import org.brandbank.base.BaseClass;
import org.brandbank.libs.BrowserActions;
import org.brandbank.libs.FileHandling;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Base64;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public class SearchEmailInStgPage extends BaseClass {
    BrowserActions browserActions;
    public static WebDriver driver;
    FileHandling fileHandling;
    String resultfilepath = System.getProperty("user.dir") + "/src/test/resources/testdata/AutomatedMailerStg/NewConnectMail.mht";

    public SearchEmailInStgPage(WebDriver driver) {
        this.driver = driver;
        browserActions = new BrowserActions(this.driver);
        fileHandling=new FileHandling();
        PageFactory.initElements(driver, this);
    }

    public boolean searchMail(String folder, int minutesRecency, String searchTerm1, String searchTerm2){
        try {
            long recency = System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(minutesRecency);

            File file = new File(folder);
            File[] fileList = file.listFiles(pathname -> pathname.lastModified() >= recency);
            int fileCount = fileList.length;
            if(fileCount ==0){
                Thread.sleep(60000);
                fileList = file.listFiles(pathname -> pathname.lastModified() >= recency);
                fileCount = fileList.length;
            }
            System.out.println(fileCount + " files found within the recency timeframe");
            extentTest.log(Status.INFO,fileCount + " files found within the recency timeframe");
            int emailCheckFlag = 0;
            int fileCheckCounter = 0;
            do {
                try (Stream<String> lines = Files.lines(Paths.get(fileList[fileCheckCounter].toURI()))) {
                    boolean term1found = lines.anyMatch(line -> line.contains(searchTerm1));
                    if (term1found) {
                        System.out.println(fileList[fileCheckCounter] + " contains " + searchTerm1);
                        try (Stream<String> thislines = Files.lines(Paths.get(fileList[fileCheckCounter].toURI()))) {
                            boolean term2found = thislines.anyMatch(line -> line.contains(searchTerm2));
                            if (term2found) {
                                System.out.println(fileList[fileCheckCounter] + " also contains " + searchTerm2);
                                emailCheckFlag = 1;
                                Path source = Paths.get(fileList[fileCheckCounter].toURI());
                                Path target = Paths.get(resultfilepath);
                                try {
                                    Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
                                } catch (IOException e) {
                                    System.out.println("Error copying email file");
                                    extentTest.log(Status.FAIL,"Error copying email file");
                                    e.printStackTrace();
                                }
                            } else {
                                System.out.println(fileList[fileCheckCounter] + " but does not contain " + searchTerm2);
                                fileCheckCounter++;
                            }
                        }
                    } else {
                        System.out.println(fileList[fileCheckCounter] + " does not contain " + searchTerm1);
                        fileCheckCounter++;
                    }
                }
            }
            while ((emailCheckFlag == 0) && (fileCheckCounter < fileCount));
            if (emailCheckFlag == 0) {
                System.out.println("Could not find the email");
                extentTest.log(Status.FAIL,"Could not find the email");
                return false;
            }
            else
            {
                System.out.println("The email has been found and copied. Proceeding to next steps");
                extentTest.log(Status.PASS,"The email has been found and copied. Proceeding to next steps\"");
            }
        }
        catch(Exception e){
            System.out.println("Error checking the email folder");
            extentTest.log(Status.FAIL,"Error checking the email folder");
            return false;
        }
        return true;
    }

    public void resultFileCheck(){
        boolean fileExists=fileHandling.verifyFileExists(resultfilepath);
        if(fileExists) {
            fileHandling.deleteFile(resultfilepath);
            System.out.println("File has been deleted");
            extentTest.log(Status.INFO,"File has been deleted");
        }
    }

    public boolean verifyStgUrlInMail() throws Exception {
       BufferedReader br = new BufferedReader(new FileReader(resultfilepath));
        String oldContent = "";
        String Search = "<HtmlView>";
        String line = br.readLine();
            while (line != null)
            {
                oldContent = oldContent + line + System.lineSeparator();
                line = br.readLine();
            }
               int index=oldContent.lastIndexOf(Search);
            String content=oldContent.substring(index+10,index+560);
            System.out.println(content);
           byte[] decoded=Base64.getMimeDecoder().decode(content);
           String decode=new String(decoded);
           System.out.println(decode);
         boolean url= decode.contains("https://redirect.stgbrandbank.com/mailer");
         if(!url){
             System.out.println("not found");
             extentTest.log(Status.FAIL,"url not found");
         }
         else{
             System.out.println("found the url");
             extentTest.log(Status.PASS,"found the url");
         }
         return url;
    }
}










