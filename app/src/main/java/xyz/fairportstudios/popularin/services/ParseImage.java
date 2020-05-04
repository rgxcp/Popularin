package xyz.fairportstudios.popularin.services;

import xyz.fairportstudios.popularin.apis.tmdb.TMDbAPI;

public class ParseImage {
    public String getPoster(String path) {
        return TMDbAPI.IMAGE + path;
    }
}
