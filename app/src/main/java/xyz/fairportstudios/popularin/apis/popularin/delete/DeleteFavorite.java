package xyz.fairportstudios.popularin.apis.popularin.delete;

import xyz.fairportstudios.popularin.apis.popularin.PopularinAPI;

public class DeleteFavorite {
    private String id;

    String requestURL = PopularinAPI.FAVORITE + "/" + id;
}
