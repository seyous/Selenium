package org.brandbank.libs;


import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;


public class FileHandling {
    Workbook wb;
    Sheet sh;
    FileInputStream fis;
    String excelFilePath;
    Map<String, Integer> columns = new HashMap<>();
    Cell cell;

    public boolean verifyFileExists(String filePath) {
        File f = new File(filePath);
        boolean verifyFile = f.exists();
        return verifyFile;
    }

    public void deleteFile(String path) {
        File f = new File(path);
        if (f.delete()) {
            System.out.println("Deleted the file: " + f.getName());
        } else {
            System.out.println("Failed to delete the file");
        }
    }

    public void deleteAllFiles(String filePath) {
        File f = new File(filePath);
        String[] entries = f.list();
        for (String s : entries) {
            File currentFile = new File(f.getPath(), s);
            currentFile.delete();
        }
        System.out.println("Deleted all files");
    }

    public boolean verifyFileContent(String content,String filePath) throws IOException {
        BufferedReader buffer = new BufferedReader(new FileReader(filePath));
        String str;
        while ((str = buffer.readLine()) != null) {
            if(str.contains(content)){
                return true;
            }
        }
        System.out.println("Content "+content+" not found");
        return false;
    }

    public String readFileContent(String filePath) throws IOException {
        String filecontent = Files.readString(Path.of(filePath));
        return filecontent;
    }

    public void unZipFile(String zipFilePath, String destDir) throws Exception{
        File dir = new File(destDir);
        //creating an output directory if it doesn't exist already
        if (!dir.exists()) dir.mkdirs();
        FileInputStream FiS;
        //buffer to read and write in the file
        byte[] buffer = new byte[1024];
        try {
            FiS = new FileInputStream(zipFilePath);
            ZipInputStream ZiS = new ZipInputStream(FiS);
            ZipEntry ZE = ZiS.getNextEntry();
            while (ZE != null) {
                String fileName = ZE.getName();
                File newFile = new File(destDir + File.separator + fileName);
                System.out.println(" Unzipping to " + newFile.getAbsolutePath());
                // create directories for sub directories in zip
                new File(newFile.getParent()).mkdirs();
                FileOutputStream FoS = new FileOutputStream(newFile);
                int len;
                while ((len = ZiS.read(buffer)) > 0) {
                    FoS.write(buffer, 0, len);
                }
                FoS.close();
                // close this ZipEntry
                ZiS.closeEntry();
                ZE = ZiS.getNextEntry();
            }
            // close last ZipEntry
            ZiS.closeEntry();
            ZiS.close();
            FiS.close();
        } catch (Exception e) {
            throw e;
        }
    }

    public void setExcelFile(String ExcelPath, String SheetName) throws Exception {
        try {
            ZipSecureFile.setMinInflateRatio(0);
            File f = new File(ExcelPath);
            fis = new FileInputStream(ExcelPath);
            wb = WorkbookFactory.create(fis);
            sh = wb.getSheet(SheetName);
            this.excelFilePath = ExcelPath;
            //adding all the column header names to the map columns
            sh.getRow(0).forEach(cell -> {
                columns.put(cell.getStringCellValue(), cell.getColumnIndex());
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public String getCellData(int rownum, int colnum) {
        try {
            cell = sh.getRow(rownum).getCell(colnum);
            String CellData = null;
            switch (cell.getCellType()) {
                case STRING:
                    CellData = cell.getStringCellValue();
                    break;
                case NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        CellData = String.valueOf(cell.getDateCellValue());
                    } else {
                        CellData = String.valueOf((long) cell.getNumericCellValue());
                    }
                    break;
                case BOOLEAN:
                    CellData = Boolean.toString(cell.getBooleanCellValue());
                    break;
                case BLANK:
                    CellData = "";
                    break;
            }
            return CellData;
        } catch (Exception e) {
            return "";
        }
    }

    public String getCellData(String columnName, int rownum){
        return getCellData(rownum,columns.get(columnName));
    }

    public void modifyFile(String sourcefilePath, String targetFilePath, String oldString, String newString){
        File fileToBeModified = new File(sourcefilePath);
        File outputFile = new File(targetFilePath);
        String oldContent = "";
        BufferedReader reader = null;
        FileWriter writer = null;
        try
        {
            reader = new BufferedReader(new FileReader(fileToBeModified));
            //Reading all the lines of input text file into oldContent
            String line = reader.readLine();
            while (line != null)
            {
                oldContent = oldContent + line + System.lineSeparator();
                line = reader.readLine();
            }
            //Replacing oldString with newString in the oldContent
            String newContent = oldContent.replaceAll(oldString, newString);
            //Rewriting the input text file with newContent
            writer = new FileWriter(outputFile);
            writer.write(newContent);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                //Closing the resources
                reader.close();
                writer.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public void zipDirectory(String zipFileName, String inputDir) throws IOException {
        String sourceFile = inputDir;
        FileOutputStream fos = new FileOutputStream(zipFileName);
        ZipOutputStream zipOut = new ZipOutputStream(fos);
        File fileToZip = new File(sourceFile);
        zipFile(fileToZip, fileToZip.getName(), zipOut);
        zipOut.close();
        fos.close();
    }

    public void zipFile(File fileToZip, String fileName, ZipOutputStream zipOut) throws IOException {
        if (fileToZip.isHidden()) {
            return;
        }
        if (fileToZip.isDirectory()) {
            if (fileName.endsWith("/")) {
                zipOut.putNextEntry(new ZipEntry(fileName));
                zipOut.closeEntry();
            } else {
                zipOut.putNextEntry(new ZipEntry(fileName + "/"));
                zipOut.closeEntry();
            }
            File[] children = fileToZip.listFiles();
            for (File childFile : children) {
                zipFile(childFile, fileName + "/" + childFile.getName(), zipOut);
            }
            return;
        }
        FileInputStream fis = new FileInputStream(fileToZip);
        ZipEntry zipEntry = new ZipEntry(fileName);
        zipOut.putNextEntry(zipEntry);
        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zipOut.write(bytes, 0, length);
        }
        fis.close();
    }

    public void editDelistXml() throws Exception{
        try {
            String tempString1 = null;
            String tempString2 = null;
            String newString = null;
            String stringFromDelist = null;
            int fromIndex = 0;
            int toIndex = 0;
            Path logFile = Path.of(System.getProperty("user.dir") + "/downloads/DataImportProcessor Log.xml");
            tempString1 = Files.readString(logFile);
            fromIndex = tempString1.indexOf("<productCodes>");
            toIndex = tempString1.indexOf("/productCodes>") + "/productCodes>".length();
            newString = tempString1.substring(fromIndex,toIndex);
            newString = newString.replace("productCodes", "ProductCodes");
            newString = newString.replace("code", "Code");
            newString = newString.replace("scheme", "Scheme");

            Path delistFile = Path.of(System.getProperty("user.dir") + "\\src\\test\\resources\\testdata\\PIMCore\\source\\Delistproduct_Stg\\Delistproduct_Stg.xml");
            tempString2 = Files.readString(delistFile);
            fromIndex = tempString2.indexOf("<ProductCodes>");
            toIndex = tempString2.indexOf("/ProductCodes>") + "/ProductCodes>".length();
            stringFromDelist = tempString2.substring(fromIndex,toIndex);
            tempString2 = tempString2.replace(stringFromDelist, newString);
            Files.writeString(delistFile, tempString2);
            System.out.println("File edited");
        }
        catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }
}









