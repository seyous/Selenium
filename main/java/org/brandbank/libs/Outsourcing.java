package org.brandbank.libs;

import com.jcraft.jsch.*;

import java.io.File;

public class Outsourcing {
    static FileHandling fileHandling;
    public static boolean transferFiles(String testSuite, String environment, String taskType, String batchNum, int numberOfImageFiles, String EAN) throws Exception {
        final String REMOTE_HOST = "ftp.brandbank.net";
        final String USERNAME = PropertyManager.getPropertiesData().getProperty(environment+".sftp.user");
        final String PASSWORD = PropertyManager.getPropertiesData().getProperty(environment+".sftp.password");
        //port number for SFTP
        final int REMOTE_PORT = 22;
        final int SESSION_TIMEOUT = 10000;
        final int CHANNEL_TIMEOUT = 5000;
        fileHandling=new FileHandling();
        String sourcePath = "";
        String destPath = "";
        String downloadLocation = System.getProperty("user.dir")+"\\src\\test\\resources\\testdata\\"+testSuite+"\\Outsourcing\\";
        //set environment specific paths and executables
        switch (environment){
            case "dev":
                sourcePath = "/Outsourcing/dev_testoutsrc/test.down/";
                destPath = "/Outsourcing/dev_testoutsrc/test.up";
                break;
            case "stg":
                sourcePath = "/Outsourcing/stg_testoutsrc/test.down/";
                destPath = "/Outsourcing/stg_testoutsrc/test.up";
                break;
            case "prod":
                sourcePath = "/LiveTestOutbound/";
                destPath = "/LiveTestInbound";
                break;
        }
        //set the remote folder location based on the type of task
        if(taskType.trim().toLowerCase().contains("market"))
            sourcePath = sourcePath+"Fully Airbrush v3/";
        else if(taskType.trim().toLowerCase().contains("merch"))
            sourcePath = sourcePath+"Cutout Only/";
        else if(taskType.trim().toLowerCase().contains("moi"))
            sourcePath = sourcePath+"MOI/";
        String dateToday = new DateManager().getCurrentDate("yyyy MM dd");
        sourcePath = sourcePath+dateToday+"/";
        //set the filename based on tasktype, normally batch number but moi uses EAN
        String fileNameToCopy = "*"+batchNum+"*.*";
        if(taskType.trim().toLowerCase().contains("moi"))
            fileNameToCopy = "*"+EAN+"*.*";
        // first we need to check if the sourcePath exists on the remote server, i.e. have any images arrived yet, otherwise get command will fail
        // do while executes the folder check until it exists OR it has tried n (folderRetryCounter) number of times
        Session jschSession = null;
        int folderCheckFlag = 0;
        int folderRetryCounter = 1;
        do {
            try {
                JSch jsch = new JSch();
                jsch.setKnownHosts(System.getProperty("user.dir")+"\\src\\test\\resources\\testdata\\Outsourcing\\known_hosts");
                jschSession = jsch.getSession(USERNAME, REMOTE_HOST, REMOTE_PORT);
                // authenticate using password
                jschSession.setPassword(PASSWORD);
                // 10 seconds session timeout
                jschSession.connect(SESSION_TIMEOUT);
                Channel sftp = jschSession.openChannel("sftp");
                // 5 seconds timeout
                sftp.connect(CHANNEL_TIMEOUT);
                ChannelSftp channelSftp = (ChannelSftp) sftp;
                // check whether the folder is there by trying to change directory to it
                channelSftp.cd(sourcePath);
                channelSftp.exit();
                folderCheckFlag = 1;
            } catch (JSchException | SftpException e) {
                e.printStackTrace();
                System.out.println("Try number: "+folderRetryCounter);
                Thread.sleep(30000);
                folderRetryCounter++;
            } finally {
                if (jschSession != null) {
                    jschSession.disconnect();
                }
            }
        }
        while ((folderCheckFlag == 0) && (folderRetryCounter <21));
        //after the do while we review the folderCheckFlag to see whether to proceed to download or report that there has been an issue and stop
        if (folderCheckFlag == 0) {
            System.out.println("Tried "+folderRetryCounter+" times. The outsourcing folder doesn't exist");
            return false;
        }
        else {
            System.out.println("Outsourcing folder exists, moving onto download step");
        }

        // as the folder exists we can download and check the correct number of images
        // do while executes the download until all images are downloaded OR it has tried n (timeCounter) number of times
        int filesCheckFlag = 0;
        int timeCounter=1;
        do {
            fileHandling.deleteAllFiles(downloadLocation);
            try {
                JSch jsch = new JSch();
                jsch.setKnownHosts(System.getProperty("user.dir")+"\\src\\test\\resources\\testdata\\Outsourcing\\known_hosts");
                jschSession = jsch.getSession(USERNAME, REMOTE_HOST, REMOTE_PORT);
                // authenticate using password
                jschSession.setPassword(PASSWORD);
                // 10 seconds session timeout
                jschSession.connect(SESSION_TIMEOUT);
                Channel sftp = jschSession.openChannel("sftp");
                // 5 seconds timeout
                sftp.connect(CHANNEL_TIMEOUT);
                ChannelSftp channelSftp = (ChannelSftp) sftp;
                // download file from remote server to local
                channelSftp.get(sourcePath+fileNameToCopy, downloadLocation);
                channelSftp.exit();
            } catch (JSchException | SftpException e) {
                e.printStackTrace();
            } finally {
                if (jschSession != null) {
                    jschSession.disconnect();
                }
            }
            //check the number of files in the download directory
            File downloads = new File(downloadLocation);
            int numberOfFiles = downloads.listFiles().length;
            System.out.println("Download attempt: "+timeCounter);
            System.out.println("Expected number of files: "+numberOfImageFiles);
            System.out.println("Number of files downloaded: " + numberOfFiles);
            //if all files are download set the filescheckflag to 1 otherwise sleep before executing again
            if (numberOfFiles >= numberOfImageFiles)
            {
                filesCheckFlag =1;
                System.out.println("All images are downloaded");
                break;
            }
            else {
                System.out.println("Some images are missing, waiting for 30 seconds before trying again");
                Thread.sleep(30000);
                timeCounter++;
            }
        }
        while ((filesCheckFlag == 0) && (timeCounter < 21));
        //after the do while we review the filecheckflag to see whether to proceed to upload or report that there has been an issue and stop
        if (filesCheckFlag == 0) {
            System.out.println("Failed to download all expected files");
            return false;
        }
        else {
            System.out.println("Successfully downloaded all expected files");
        }
        //All files are downloaded so now it's time to upload
        System.out.println("Uploading to destination");
        try {
            JSch jsch = new JSch();
            jsch.setKnownHosts(System.getProperty("user.dir")+"\\src\\test\\resources\\testdata\\Outsourcing\\known_hosts");
            jschSession = jsch.getSession(USERNAME, REMOTE_HOST, REMOTE_PORT);
            // authenticate using password
            jschSession.setPassword(PASSWORD);
            // 10 seconds session timeout
            jschSession.connect(SESSION_TIMEOUT);
            Channel sftp = jschSession.openChannel("sftp");
            // 5 seconds timeout
            sftp.connect(CHANNEL_TIMEOUT);
            ChannelSftp channelSftp = (ChannelSftp) sftp;
            // transfer file from local to remote server
            channelSftp.put(downloadLocation+"*.*", destPath);
            channelSftp.exit();
        } catch (JSchException | SftpException e) {
            e.printStackTrace();
        } finally {
            if (jschSession != null) {
                jschSession.disconnect();
            }
        }
        System.out.println("Outsourcing complete");
        return true;
        //I'm not deleting the files here as it might be useful to have them in case of outsourcing issue and any need to resubmit
    }
}
