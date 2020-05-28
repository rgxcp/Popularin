package xyz.fairportstudios.popularin.models;

public class ReviewDetail {
    private Integer tmdb_id;
    private Integer user_id;
    private Integer total_like;
    private Boolean is_liked;
    private Double rating;
    private String review_detail;
    private String review_date;
    private String watch_date;
    private String title;
    private String release_date;
    private String poster;
    private String full_name;
    private String profile_picture;

    public ReviewDetail() {
        // Constructor kosong
    }

    public ReviewDetail(
            Integer tmdb_id,
            Integer user_id,
            Integer total_like,
            Boolean is_liked,
            Double rating,
            String review_detail,
            String review_date,
            String watch_date,
            String title,
            String release_date,
            String poster,
            String full_name,
            String profile_picture
    ) {
        this.tmdb_id = tmdb_id;
        this.user_id = user_id;
        this.total_like = total_like;
        this.is_liked = is_liked;
        this.rating = rating;
        this.review_detail = review_detail;
        this.review_date = review_date;
        this.watch_date = watch_date;
        this.title = title;
        this.release_date = release_date;
        this.poster = poster;
        this.full_name = full_name;
        this.profile_picture = profile_picture;
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

    public String getReview_date() {
        return review_date;
    }

    public void setReview_date(String review_date) {
        this.review_date = review_date;
    }

    public String getWatch_date() {
        return watch_date;
    }

    public void setWatch_date(String watch_date) {
        this.watch_date = watch_date;
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

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getProfile_picture() {
        return profile_picture;
    }

    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }
}
