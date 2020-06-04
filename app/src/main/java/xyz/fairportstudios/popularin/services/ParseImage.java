package xyz.fairportstudios.popularin.services;

import xyz.fairportstudios.popularin.statics.TMDbAPI;

public class ParseImage {
    public String getImage(String path) {
        return TMDbAPI.IMAGE + path;
    }
}
