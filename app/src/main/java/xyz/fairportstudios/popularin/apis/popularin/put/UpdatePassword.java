package xyz.fairportstudios.popularin.apis.popularin.put;

import xyz.fairportstudios.popularin.apis.popularin.PopularinAPI;

public class UpdatePassword {
    private String id;

    String requestURL = PopularinAPI.USER + "/" + id + "/password";
}
