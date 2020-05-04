package xyz.fairportstudios.popularin.apis.popularin.get;

import xyz.fairportstudios.popularin.apis.popularin.PopularinAPI;

public class ReviewLiked {
    private String id;

    String requestURL = PopularinAPI.FILM + "/" + id + "/reviews/liked";
}
