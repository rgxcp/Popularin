package xyz.fairportstudios.popularin.models;

public class RecentReview {
    private Integer id;
    private Integer tmdb_id;
    private Double rating;
    private String title;
    private String release_date;
    private String poster;

    public RecentReview() {
        // Empty constructor
    }

    public RecentReview(
            Integer id,
            Integer tmdb_id,
            Double rating,
            String title,
            String release_date,
            String poster
    ) {
        this.id = id;
        this.tmdb_id = tmdb_id;
        this.rating = rating;
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
