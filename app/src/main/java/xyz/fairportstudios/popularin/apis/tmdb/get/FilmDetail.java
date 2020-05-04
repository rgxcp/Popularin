package xyz.fairportstudios.popularin.apis.tmdb.get;

import xyz.fairportstudios.popularin.apis.tmdb.TMDbAPI;

public class FilmDetail {
    public String getRequestURL(String id) {
        return TMDbAPI.FILM_DETAIL
                + id
                + "?api_key="
                + TMDbAPI.API_KEY
                + "&language=id&append_to_response=credits%2Cimages";
    }
}
