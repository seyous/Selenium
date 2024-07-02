package org.brandbank.libs;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import java.io.*;
import java.util.List;
public class CSVDataManager {
    public String[] getCSVData(String testSuite, int rowNumber) throws IOException, CsvException {
        String csvFilePath = System.getProperty("user.dir")+"/src/test/resources/testdata/"+testSuite+"/"+testSuite+"_Products.csv";
        CSVReader csvReader = new CSVReader(new FileReader(csvFilePath));
        List<String[]> lines = csvReader.readAll();
        csvReader.close();
        return lines.get(rowNumber);
    }

    public void writeCSVFile(String fileName, int rowNumber, String orderNumber) throws IOException, CsvException {
        String csvFilePath = System.getProperty("user.dir")+"/src/test/resources/testdata/"+fileName;
        CSVReader csvReader = new CSVReader(new FileReader(csvFilePath));
        List<String[]> lines = csvReader.readAll();
        lines.get(rowNumber)[2] = orderNumber;
        csvReader.close();
        CSVWriter csvWriter = new CSVWriter(new FileWriter(csvFilePath));
        csvWriter.writeAll(lines);
        csvWriter.flush();
        csvWriter.close();
    }

    public static void writeProds (String testSuite, String[]x) throws IOException {
        String csvFilePath = System.getProperty("user.dir")+"/src/test/resources/testdata/"+testSuite+"/"+testSuite+"_Products.csv";
        BufferedWriter outputWriter = null;
        outputWriter = new BufferedWriter(new FileWriter(csvFilePath));
        outputWriter.write("Subcode,EAN,Description,OrderNumber,BatchNumber,Pvid");
        outputWriter.newLine();
        for (int i = 0; i < x.length; i++) {
            outputWriter.write(x[i]);
            outputWriter.newLine();
        }
        outputWriter.flush();
        outputWriter.close();
    }
    public static void updateCSV (String testSuite,int startRow,int NumOfRows,int column,String value) throws IOException,CsvException {
        String csvFilePath =  System.getProperty("user.dir")+"/src/test/resources/testdata/"+testSuite+"/"+testSuite+"_Products.csv";
        CSVReader csvReader = new CSVReader(new FileReader(csvFilePath));
        List<String[]> lines = csvReader.readAll();
        for (int i = startRow; i <startRow+NumOfRows; i++) {
            lines.get(i)[column] = value;
        }
        csvReader.close();
        CSVWriter csvWriter = new CSVWriter(new FileWriter(csvFilePath));
        csvWriter.writeAll(lines);
        csvWriter.flush();
        csvWriter.close();
    }
    public void writeToFile(String fileName, String content) throws IOException, CsvException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
        writer.write(content);
        writer.close();


    }
}
