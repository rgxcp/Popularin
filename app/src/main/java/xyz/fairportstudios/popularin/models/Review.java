package xyz.fairportstudios.popularin.models;

public class Review {
    private Double rating;
    private Integer id;
    private Integer tmdb_id;
    private Integer user_id;
    private String first_name;
    private String poster;
    private String profile_picture;
    private String release_date;
    private String review_text;
    private String title;

    public Review(Double rating, Integer id, Integer tmdb_id, Integer user_id, String first_name, String poster, String profile_picture, String release_date, String review_text, String title) {
        this.rating = rating;
        this.id = id;
        this.tmdb_id = tmdb_id;
        this.user_id = user_id;
        this.first_name = first_name;
        this.poster = poster;
        this.profile_picture = profile_picture;
        this.release_date = release_date;
        this.review_text = review_text;
        this.title = title;
    }

    public Double getRating() {
        return rating;
    }

    public Integer getId() {
        return id;
    }

    public Integer getTmdb_id() {
        return tmdb_id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getPoster() {
        return poster;
    }

    public String getProfile_picture() {
        return profile_picture;
    }

    public String getRelease_date() {
        return release_date;
    }

    public String getReview_text() {
        return review_text;
    }

    public String getTitle() {
        return title;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setTmdb_id(Integer tmdb_id) {
        this.tmdb_id = tmdb_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public void setReview_text(String review_text) {
        this.review_text = review_text;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
