package xyz.fairportstudios.popularin.apis.popularin.get;

import xyz.fairportstudios.popularin.apis.popularin.PopularinAPI;

public class FavoriteFromFollowing {
    private String id;

    String requestURL = PopularinAPI.FILM + "/" + id + "/favorites/from/following";
}
