package xyz.fairportstudios.popularin.apis.popularin.get;

import xyz.fairportstudios.popularin.apis.popularin.PopularinAPI;

public class UserFollowing {
    private String id;

    String requestURL = PopularinAPI.USER + "/" + id + "/followings";
}