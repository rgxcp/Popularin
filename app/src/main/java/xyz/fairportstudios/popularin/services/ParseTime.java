package xyz.fairportstudios.popularin.services;

public class ParseTime {
    public String getHourMinute(Integer runtime) {
        int hour = runtime / 60;
        int minute = runtime % 60;
        return hour + " jam " + minute + " menit";
    }
}
