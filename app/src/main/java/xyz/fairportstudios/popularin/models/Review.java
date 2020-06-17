package xyz.fairportstudios.popularin.models;

public class Review {
    private Integer id;
    private Integer tmdb_id;
    private Integer user_id;
    private Integer total_like;
    private Integer total_comment;
    private Boolean is_liked;
    private Double rating;
    private String review_detail;
    private String timestamp;
    private String title;
    private String release_date;
    private String poster;
    private String username;
    private String profile_picture;

    public Review() {
        // Constructor kosong
    }

    public Review(
            Integer id,
            Integer tmdb_id,
            Integer user_id,
            Integer total_like,
            Integer total_comment,
            Boolean is_liked,
            Double rating,
            String review_detail,
            String timestamp,
            String title,
            String release_date,
            String poster,
            String username,
            String profile_picture
    ) {
        this.id = id;
        this.tmdb_id = tmdb_id;
        this.user_id = user_id;
        this.total_like = total_like;
        this.total_comment = total_comment;
        this.is_liked = is_liked;
        this.rating = rating;
        this.review_detail = review_detail;
        this.timestamp = timestamp;
        this.title = title;
        this.release_date = release_date;
        this.poster = poster;
        this.username = username;
        this.profile_picture = profile_picture;
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

    public Integer getTotal_like() {
        return total_like;
    }

    public void setTotal_like(Integer total_like) {
        this.total_like = total_like;
    }

    public Integer getTotal_comment() {
        return total_comment;
    }

    public void setTotal_comment(Integer total_comment) {
        this.total_comment = total_comment;
    }

    public Boolean getIs_liked() {
        return is_liked;
    }

    public void setIs_liked(Boolean is_liked) {
        this.is_liked = is_liked;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
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
