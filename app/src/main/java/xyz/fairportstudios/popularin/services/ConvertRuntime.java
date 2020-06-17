package xyz.fairportstudios.popularin.services;

public class ConvertRuntime {
    public String getRuntimeForHumans(int runtime) {
        int hour = runtime / 60;
        int minute = runtime % 60;
        return hour + " jam " + minute + " menit";
    }
}
