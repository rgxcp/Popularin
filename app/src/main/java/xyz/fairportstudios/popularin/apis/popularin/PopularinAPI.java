package xyz.fairportstudios.popularin.apis.popularin;

public class PopularinAPI {
    // Safe
    private static final String BASE = "https://popularin.fairportstudios.xyz/api";
    public static final String COMMENT = PopularinAPI.BASE + "/comment";
    public static final String FILM = PopularinAPI.BASE + "/film";
    public static final String REVIEW = PopularinAPI.BASE + "/review";
    public static final String TIMELINE = PopularinAPI.BASE + "/review/timeline?page=";

    // Unsafe
    public static final String FAVORITE = "https://popularin.fairportstudios.xyz/api/favorite";
    public static final String REVIEWS = "https://popularin.fairportstudios.xyz/api/reviews";
    public static final String SEARCH_USER = "https://popularin.fairportstudios.xyz/api/user/search?query=";
    public static final String SIGN_IN = "https://popularin.fairportstudios.xyz/api/user/signin";
    public static final String SIGN_UP = "https://popularin.fairportstudios.xyz/api/user/signup";
    public static final String SIGN_OUT = "https://popularin.fairportstudios.xyz/api/user/signout";
    public static final String USER = "https://popularin.fairportstudios.xyz/api/user";
    public static final String USER_SELF = "https://popularin.fairportstudios.xyz/api/user/self";
    public static final String WATCHLIST = "https://popularin.fairportstudios.xyz/api/watchlist";
}
