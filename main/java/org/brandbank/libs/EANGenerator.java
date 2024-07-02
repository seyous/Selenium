package org.brandbank.libs;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EANGenerator {
    public static void generateEAN(String testSuite, int numberOfProducts, String subcode) throws IOException {
        String[] prods = new String[numberOfProducts];
        for(int i=1;i<=numberOfProducts;i++)
        {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("ddHHmmss");
            LocalDateTime now = LocalDateTime.now();
            String grtTen = null;
            if(i<10)
                grtTen = "0"+Integer.toString(i);
            else
                grtTen = Integer.toString(i);
            String ean = "57"+(dtf.format(now))+grtTen;
            int checkDigit = calcCD(ean);
            ean = ean + checkDigit;
            prods[i-1] = (subcode+","+ean+","+testSuite+" Automation Test Product "+grtTen+",,,");
        }
        CSVDataManager.writeProds(testSuite,prods);
    }

    public static int calcCD(String ean)
    {
        int checkDigit = 0;
        int first=Integer.parseInt(ean.substring(0,1));
        int second=Integer.parseInt(ean.substring(1,2)) * 3;
        int third=Integer.parseInt(ean.substring(2,3));
        int fourth=Integer.parseInt(ean.substring(3,4)) * 3;
        int fifth=Integer.parseInt(ean.substring(4,5));
        int sixth=Integer.parseInt(ean.substring(5,6)) * 3;
        int seventh=Integer.parseInt(ean.substring(6,7));
        int eighth=Integer.parseInt(ean.substring(7,8)) * 3;
        int ninth=Integer.parseInt(ean.substring(8,9));
        int tenth=Integer.parseInt(ean.substring(9,10)) * 3;
        int eleventh=Integer.parseInt(ean.substring(10,11));
        int twelth=Integer.parseInt(ean.substring(11,12)) * 3;
        int sum = first + second + third + fourth + fifth + sixth + seventh + eighth + ninth + tenth + eleventh + twelth;
        int tempMod = sum%=10;
        int tempCheckDigit = (sum-tempMod+10)-sum;
        if (tempCheckDigit > 9)
            checkDigit = 0;
        else
            checkDigit = tempCheckDigit;
        return checkDigit;
    }
}
