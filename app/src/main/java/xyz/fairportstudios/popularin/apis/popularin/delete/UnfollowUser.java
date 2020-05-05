package xyz.fairportstudios.popularin.apis.popularin.delete;

import xyz.fairportstudios.popularin.apis.popularin.PopularinAPI;

public class UnfollowUser {
    private String id;

    String requestURL = PopularinAPI.USER + "/" + id + "/unfollow";
}
