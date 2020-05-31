package xyz.fairportstudios.popularin.apis.tmdb;

public class TMDbAPI {
    private static final String BASE = "https://api.themoviedb.org/3";
    public static final String IMAGE = "https://image.tmdb.org/t/p/w300";
    public static final String API_KEY = "0cdb7eb7a8102f652a6c74dddd692a2f";
    public static final String AIRING_FILM = BASE + "/movie/now_playing";
    public static final String CREDIT = BASE + "/person/";
    public static final String DISCOVER_FILM = BASE + "/discover/movie";
    public static final String FILM_DETAIL = BASE + "/movie/";
    public static final String SEARCH_FILM = BASE + "/search/movie?api_key=" + API_KEY + "&language=id&query=";
}
