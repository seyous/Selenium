package org.brandbank.libs;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateManager {

    public String getCurrentDate(String format) {
        SimpleDateFormat simpleDateFormat = null;
        if (format.equals("dd MMM yyyy"))
            simpleDateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
        else if (format.equals("MM/dd/yyyy"))
            simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        else if (format.equals("M/d/yyyy"))
            simpleDateFormat = new SimpleDateFormat("M/d/yyyy");
        else if (format.equals("M/dd/yyyy"))
            simpleDateFormat = new SimpleDateFormat("M/dd/yyyy");
        else if (format.equals("dd-MM-YYYY"))
            simpleDateFormat = new SimpleDateFormat("dd-MM-YYYY");
        else if (format.equals("MMM dd yyyy HH"))
            simpleDateFormat = new SimpleDateFormat("MMM dd yyyy HH", Locale.ENGLISH);
        else if (format.equals("yyyy MM dd"))
            simpleDateFormat = new SimpleDateFormat("yyyy MM dd");
        else if (format.equals("YYYY-MM-dd"))
            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        else if (format.equals("d"))
            simpleDateFormat = new SimpleDateFormat("d");
        else if (format.equals("dd/MM"))
            simpleDateFormat = new SimpleDateFormat("dd/MM");
        else if (format.equals("MMddHHmmss"))
            simpleDateFormat = new SimpleDateFormat("MMddHHmmss");
        else if (format.equals("MM"))
            simpleDateFormat = new SimpleDateFormat("MM");
        else if (format.equals("YYYY"))
            simpleDateFormat = new SimpleDateFormat("YYYY");
        else
            simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        return simpleDateFormat.format(new Date());

    }

    public String getFutureDate(int days) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, days);
        return dateFormat.format(cal.getTime());
    }

    public String getMonthEndDate() {
        SimpleDateFormat df = new SimpleDateFormat("dd");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return df.format(calendar.getTime());
    }

    public String getCurrentUtcTime(String format) throws ParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format)
                .withZone(ZoneId.of("UTC"));
        Instant instant = Instant.now();
        String time = formatter.format(instant);
        System.out.println(time);
        return time;
    }
}

