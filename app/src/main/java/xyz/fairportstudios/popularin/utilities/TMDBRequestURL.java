package xyz.fairportstudios.popularin.utilities;

public class TMDBRequestURL {
    private static String API_KEY = "0cdb7eb7a8102f652a6c74dddd692a2f";

    public String getDiscoverFilmURL(String genreID) {
        return "https://api.themoviedb.org/3/discover/movie?api_key=" + API_KEY + "&sort_by=popularity.desc&page=1&release_date.gte=2000-01-01&with_genres=" + genreID + "&with_original_language=id";
    }

    public String getSearchURL(String query) {
        return "https://api.themoviedb.org/3/search/movie?api_key=" + API_KEY + "&query=" + query;
    }
}
