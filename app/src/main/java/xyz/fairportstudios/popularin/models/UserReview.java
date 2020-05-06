package xyz.fairportstudios.popularin.models;

public class UserReview {
    private Double rating;
    private Integer id;
    private Integer tmdb_id;
    private String poster;
    private String release_date;
    private String review_date;
    private String review_text;
    private String title;

    public UserReview(Double rating, Integer id, Integer tmdb_id, String poster, String release_date, String review_date, String review_text, String title) {
        this.rating = rating;
        this.id = id;
        this.tmdb_id = tmdb_id;
        this.poster = poster;
        this.release_date = release_date;
        this.review_date = review_date;
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

    public String getPoster() {
        return poster;
    }

    public String getRelease_date() {
        return release_date;
    }

    public String getReview_date() {
        return review_date;
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

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public void setReview_date(String review_date) {
        this.review_date = review_date;
    }

    public void setReview_text(String review_text) {
        this.review_text = review_text;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
