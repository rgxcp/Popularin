package xyz.fairportstudios.popularin.apis.popularin.get;

import xyz.fairportstudios.popularin.apis.popularin.PopularinAPI;

public class LikeFromFollowing {
    private String id;

    String requestURL = PopularinAPI.REVIEW + "/" + id + "/likes/from/following";
}
