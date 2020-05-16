package xyz.fairportstudios.popularin.models;

public class ReviewDetail {
    private Integer film_id;
    private Integer user_id;
    private Boolean liked;
    private Double rating;
    private Integer likes;
    private String poster;
    private String title;
    private String release_date;
    private String review_date;
    private String watch_date;
    private String review_text;
    private String first_name;
    private String profile_picture;

    public ReviewDetail() {
        // Empty constructor
    }

    public ReviewDetail(Integer film_id, Integer user_id, Boolean liked, Double rating, Integer likes, String poster, String title, String release_date, String review_date, String watch_date, String review_text, String first_name, String profile_picture) {
        this.film_id = film_id;
        this.user_id = user_id;
        this.liked = liked;
        this.rating = rating;
        this.likes = likes;
        this.poster = poster;
        this.title = title;
        this.release_date = release_date;
        this.review_date = review_date;
        this.watch_date = watch_date;
        this.review_text = review_text;
        this.first_name = first_name;
        this.profile_picture = profile_picture;
    }

    public Integer getFilm_id() {
        return film_id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public Boolean getLiked() {
        return liked;
    }

    public Double getRating() {
        return rating;
    }

    public Integer getLikes() {
        return likes;
    }

    public String getPoster() {
        return poster;
    }

    public String getTitle() {
        return title;
    }

    public String getRelease_date() {
        return release_date;
    }

    public String getReview_date() {
        return review_date;
    }

    public String getWatch_date() {
        return watch_date;
    }

    public String getReview_text() {
        return review_text;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getProfile_picture() {
        return profile_picture;
    }

    public void setFilm_id(Integer film_id) {
        this.film_id = film_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public void setLiked(Boolean liked) {
        this.liked = liked;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public void setReview_date(String review_date) {
        this.review_date = review_date;
    }

    public void setWatch_date(String watch_date) {
        this.watch_date = watch_date;
    }

    public void setReview_text(String review_text) {
        this.review_text = review_text;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }
}
