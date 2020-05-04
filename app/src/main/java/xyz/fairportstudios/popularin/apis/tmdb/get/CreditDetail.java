package xyz.fairportstudios.popularin.apis.tmdb.get;

import xyz.fairportstudios.popularin.apis.tmdb.TMDbAPI;

public class CreditDetail {
    public String getRequestURL(String id) {
        return TMDbAPI.CREDIT
                + id
                + "?api_key="
                + TMDbAPI.API_KEY
                + "&language=en-US&append_to_response=movie_credits";
    }
}
