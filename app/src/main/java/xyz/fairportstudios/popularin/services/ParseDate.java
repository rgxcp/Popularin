package xyz.fairportstudios.popularin.services;

import java.util.HashMap;
import java.util.Map;

public class ParseDate {
    public String getDateForHumans(String date) {
        Map<String, String> months = new HashMap<>();
        months.put("01", "Januari");
        months.put("02", "Februari");
        months.put("03", "Maret");
        months.put("04", "April");
        months.put("05", "Mei");
        months.put("06", "Juni");
        months.put("07", "Juli");
        months.put("08", "Agustus");
        months.put("09", "September");
        months.put("10", "Oktober");
        months.put("11", "November");
        months.put("12", "Desember");

        try {
            String day = date.substring(8, 10);
            String month = date.substring(5, 7);
            String year = date.substring(0, 4);
            return day + " " + months.get(month) + " " + year;
        } catch (StringIndexOutOfBoundsException exception) {
            return "Tanpa Tahun";
        }
    }

    public String getDay(String date) {
        try {
            return date.substring(8, 10);
        } catch (StringIndexOutOfBoundsException exception) {
            return "01";
        }
    }

    public String getMonth(String date) {
        try {
            return date.substring(5, 7);
        } catch (StringIndexOutOfBoundsException exception) {
            return "01";
        }
    }

    public String getYear(String date) {
        try {
            return date.substring(0, 4);
        } catch (StringIndexOutOfBoundsException exception) {
            return "2020";
        }
    }
}
