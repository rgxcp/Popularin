package xyz.fairportstudios.popularin.models;

public class UserReview {
    private Integer id;
    private Integer tmdb_id;
    private Double rating;
    private String review_detail;
    private String timestamp;
    private String title;
    private String release_date;
    private String poster;

    public UserReview() {
        // Constructor kosong
    }

    public UserReview(
            Integer id,
            Integer tmdb_id,
            Double rating,
            String review_detail,
            String timestamp,
            String title,
            String release_date,
            String poster
    ) {
        this.id = id;
        this.tmdb_id = tmdb_id;
        this.rating = rating;
        this.review_detail = review_detail;
        this.timestamp = timestamp;
        this.title = title;
        this.release_date = release_date;
        this.poster = poster;
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
}
