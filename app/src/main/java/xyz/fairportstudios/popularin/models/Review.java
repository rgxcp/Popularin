package xyz.fairportstudios.popularin.models;

public class Review {
    private Integer id;
    private Integer tmdb_id;
    private Integer user_id;
    private Double rating;
    private String title;
    private String poster;
    private String release_date;
    private String first_name;
    private String profile_picture;
    private String review_detail;
    private String timestamp;

    public Review() {
        // Empty constructor
    }

    public Review(
            Integer id,
            Integer tmdb_id,
            Integer user_id,
            Double rating,
            String title,
            String poster,
            String release_date,
            String first_name,
            String profile_picture,
            String review_detail,
            String timestamp
    ) {
        this.id = id;
        this.tmdb_id = tmdb_id;
        this.user_id = user_id;
        this.rating = rating;
        this.title = title;
        this.poster = poster;
        this.release_date = release_date;
        this.first_name = first_name;
        this.profile_picture = profile_picture;
        this.review_detail = review_detail;
        this.timestamp = timestamp;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTmdb_id() {
        return tmdb_id;
    }

    public void setTmdb_id(Integer tmdb_id) {
        this.tmdb_id = tmdb_id;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getProfile_picture() {
        return profile_picture;
    }

    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
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
}
