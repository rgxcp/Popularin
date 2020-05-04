package xyz.fairportstudios.popularin.apis.popularin.post;

import xyz.fairportstudios.popularin.apis.popularin.PopularinAPI;

public class FollowUser {
    private String id;

    String requestURL = PopularinAPI.USER + "/" + id + "/follow";
}
