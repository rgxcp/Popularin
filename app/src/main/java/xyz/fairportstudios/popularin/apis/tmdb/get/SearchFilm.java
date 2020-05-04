package xyz.fairportstudios.popularin.apis.tmdb.get;

import xyz.fairportstudios.popularin.apis.tmdb.TMDbAPI;

public class SearchFilm {
    public String getRequestURL(String query, String page) {
        return TMDbAPI.SEARCH_FILM
                + "?api_key="
                + TMDbAPI.API_KEY
                + "&language=id&query="
                + query
                + "&page="
                + page;
    }
}
