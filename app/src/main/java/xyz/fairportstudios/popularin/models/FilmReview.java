package xyz.fairportstudios.popularin.models;

public class FilmReview {
    private Integer review_id;
    private Integer user_id;
    private Double rating;
    private String review_detail;
    private String timestamp;
    private String username;
    private String profile_picture;

    public FilmReview() {
        // Constructor kosong
    }

    public FilmReview(
            Integer review_id,
            Integer user_id,
            Double rating,
            String review_detail,
            String timestamp,
            String username,
            String profile_picture
    ) {
        this.review_id = review_id;
        this.user_id = user_id;
        this.rating = rating;
        this.review_detail = review_detail;
        this.timestamp = timestamp;
        this.username = username;
        this.profile_picture = profile_picture;
    }

    public Integer getReview_id() {
        return review_id;
    }

    public void setReview_id(Integer review_id) {
        this.review_id = review_id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getReview_detail() {
        return review_detail;
    }

    public void setReview_detail(String review_detail) {
        this.review_detail = review_detail;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
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
