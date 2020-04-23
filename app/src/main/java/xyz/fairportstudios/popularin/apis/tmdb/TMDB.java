package xyz.fairportstudios.popularin.apis.tmdb;

public class TMDB {
    static final String API_KEY = "0cdb7eb7a8102f652a6c74dddd692a2f";
    static final String BASE_REQUEST = "https://api.themoviedb.org/3";
    static final String BASE_IMAGE = "https://image.tmdb.org/t/p/w500";
    public static final String BASE_VIDEO = "https://www.youtube.com/watch?v=";

    String getYear(String release_date) {
        return release_date.substring(0,4);
    }
}
