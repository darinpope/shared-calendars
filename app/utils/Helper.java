package utils;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Minutes;
import play.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Helper {
    public static String getTempDir() {
        String tempDir = System.getProperty("java.io.tmpdir");
        if (!StringUtils.endsWith(tempDir, "/")) {
            tempDir = tempDir + "/";
        }
        return tempDir;
    }

    public static Date getDateFromString(String value) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date date = null;
        try {
            date = sdf.parse(value);
        } catch (Exception e) {
            Logger.error(e.getMessage(), e);
        }
        return date;
    }

    public static String getStringFromDate(Date value) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        String dateValue = null;
        try {
            dateValue = sdf.format(value);
        } catch (Exception e) {
            Logger.error(e.getMessage(), e);
        }
        return dateValue;
    }

    public static boolean isAllDayEvent(Date startDate,Date endDate) {
        if(endDate == null || startDate == null) {
            return false;
        }
        int diff = Minutes.minutesBetween(new DateTime(startDate), new DateTime(endDate)).getMinutes();
        if(diff == 1440) {
            return true;
        }
        return false;
    }
}
