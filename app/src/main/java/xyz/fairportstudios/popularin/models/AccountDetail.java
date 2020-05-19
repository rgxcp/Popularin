package xyz.fairportstudios.popularin.models;

public class AccountDetail {
    private Integer total_following;
    private Integer total_follower;
    private Integer total_favorite;
    private Integer total_review;
    private Integer total_watchlist;
    private String joined_since;
    private String full_name;
    private String username;
    private String profile_picture;

    public AccountDetail() {
        // Empty constructor
    }

    public AccountDetail(
            Integer total_following,
            Integer total_follower,
            Integer total_favorite,
            Integer total_review,
            Integer total_watchlist,
            String joined_since,
            String full_name,
            String username,
            String profile_picture
    ) {
        this.total_following = total_following;
        this.total_follower = total_follower;
        this.total_favorite = total_favorite;
        this.total_review = total_review;
        this.total_watchlist = total_watchlist;
        this.joined_since = joined_since;
        this.full_name = full_name;
        this.username = username;
        this.profile_picture = profile_picture;
    }

    public Integer getTotal_following() {
        return total_following;
    }

    public void setTotal_following(Integer total_following) {
        this.total_following = total_following;
    }

    public Integer getTotal_follower() {
        return total_follower;
    }

    public void setTotal_follower(Integer total_follower) {
        this.total_follower = total_follower;
    }

    public Integer getTotal_favorite() {
        return total_favorite;
    }

    public void setTotal_favorite(Integer total_favorite) {
        this.total_favorite = total_favorite;
    }

    public Integer getTotal_review() {
        return total_review;
    }

    public void setTotal_review(Integer total_review) {
        this.total_review = total_review;
    }

    public Integer getTotal_watchlist() {
        return total_watchlist;
    }

    public void setTotal_watchlist(Integer total_watchlist) {
        this.total_watchlist = total_watchlist;
    }

    public String getJoined_since() {
        return joined_since;
    }

    public void setJoined_since(String joined_since) {
        this.joined_since = joined_since;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfile_picture() {
        return profile_picture;
    }

    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }
}
