package xyz.fairportstudios.popularin.services;

import xyz.fairportstudios.popularin.apis.tmdb.TMDBBaseRequest;

public class ParseImage {
    public String getPoster(String path) {
        return TMDBBaseRequest.IMAGE + path;
    }
}
