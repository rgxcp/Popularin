package xyz.fairportstudios.popularin.services;

import java.util.HashMap;
import java.util.Map;

public class ParseDate {
    public String getDate(String releaseDate) {
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

        String date = releaseDate.substring(8,10);
        String month = releaseDate.substring(5,7);
        String year = releaseDate.substring(0,4);

        return date + " " + months.get(month) + " " + year;
    }

    public String getDay(String releaseDate) {
        return releaseDate.substring(8,10);
    }

    public String getMonth(String releaseDate) {
        return releaseDate.substring(5,7);
    }

    public String getYear(String releaseDate) {
        return releaseDate.substring(0,4);
    }
}
