package xyz.fairportstudios.popularin.statics;

public class TMDbAPI {
    private static final String BASE_URL = "https://api.themoviedb.org/3";
    public static final String BASE_SMALL_IMAGE_URL = "https://image.tmdb.org/t/p/w154";
    public static final String BASE_LARGE_IMAGE_URL = "https://image.tmdb.org/t/p/w780";
    public static final String AIRING = BASE_URL + "/movie/now_playing";
    public static final String CREDIT = BASE_URL + "/person/";
    public static final String DISCOVER = BASE_URL + "/discover/movie";
    public static final String FILM = BASE_URL + "/movie/";
    public static final String SEARCH_FILM = BASE_URL + "/search/movie";
}
