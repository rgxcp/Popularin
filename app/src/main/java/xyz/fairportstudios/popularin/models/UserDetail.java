package xyz.fairportstudios.popularin.models;

public class UserDetail {
    private Boolean is_follower;
    private Boolean is_following;
    private Integer total_review;
    private Integer total_favorite;
    private Integer total_watchlist;
    private Integer total_follower;
    private Integer total_following;
    private String full_name;
    private String username;
    private String profile_picture;

    public UserDetail() {
        // Constructor kosong
    }

    public UserDetail(
            Boolean is_follower,
            Boolean is_following,
            Integer total_review,
            Integer total_favorite,
            Integer total_watchlist,
            Integer total_follower,
            Integer total_following,
            String full_name,
            String username,
            String profile_picture
    ) {
        this.is_follower = is_follower;
        this.is_following = is_following;
        this.total_review = total_review;
        this.total_favorite = total_favorite;
        this.total_watchlist = total_watchlist;
        this.total_follower = total_follower;
        this.total_following = total_following;
        this.full_name = full_name;
        this.username = username;
        this.profile_picture = profile_picture;
    }

    public Boolean getIs_follower() {
        return is_follower;
    }

    public void setIs_follower(Boolean is_follower) {
        this.is_follower = is_follower;
    }

    public Boolean getIs_following() {
        return is_following;
    }

    public void setIs_following(Boolean is_following) {
        this.is_following = is_following;
    }

    public Integer getTotal_review() {
        return total_review;
    }

    public void setTotal_review(Integer total_review) {
        this.total_review = total_review;
    }

    public Integer getTotal_favorite() {
        return total_favorite;
    }

    public void setTotal_favorite(Integer total_favorite) {
        this.total_favorite = total_favorite;
    }

    public Integer getTotal_watchlist() {
        return total_watchlist;
    }

    public void setTotal_watchlist(Integer total_watchlist) {
        this.total_watchlist = total_watchlist;
    }

    public Integer getTotal_follower() {
        return total_follower;
    }

    public void setTotal_follower(Integer total_follower) {
        this.total_follower = total_follower;
    }

    public Integer getTotal_following() {
        return total_following;
    }

    public void setTotal_following(Integer total_following) {
        this.total_following = total_following;
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
