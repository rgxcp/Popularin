package xyz.fairportstudios.popularin.models;

public class AccountDetail {
    private int mTotal_review;
    private int mTotal_favorite;
    private int mTotal_watchlist;
    private int mTotal_follower;
    private int mTotal_following;
    private String mFull_name;
    private String mUsername;
    private String mProfile_picture;

    public AccountDetail(
            int total_review,
            int total_favorite,
            int total_watchlist,
            int total_follower,
            int total_following,
            String full_name,
            String username,
            String profile_picture
    ) {
        mTotal_review = total_review;
        mTotal_favorite = total_favorite;
        mTotal_watchlist = total_watchlist;
        mTotal_follower = total_follower;
        mTotal_following = total_following;
        mFull_name = full_name;
        mUsername = username;
        mProfile_picture = profile_picture;
    }

    public int getTotal_review() {
        return mTotal_review;
    }

    public int getTotal_favorite() {
        return mTotal_favorite;
    }

    public int getTotal_watchlist() {
        return mTotal_watchlist;
    }

    public int getTotal_follower() {
        return mTotal_follower;
    }

    public int getTotal_following() {
        return mTotal_following;
    }

    public String getFull_name() {
        return mFull_name;
    }

    public String getUsername() {
        return mUsername;
    }

    public String getProfile_picture() {
        return mProfile_picture;
    }
}
