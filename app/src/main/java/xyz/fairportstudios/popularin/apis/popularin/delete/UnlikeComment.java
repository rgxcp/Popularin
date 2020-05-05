package xyz.fairportstudios.popularin.apis.popularin.delete;

import xyz.fairportstudios.popularin.apis.popularin.PopularinAPI;

public class UnlikeComment {
    private String id;

    String requestURL = PopularinAPI.REVIEW + "/" + id + "/unlike";
}
