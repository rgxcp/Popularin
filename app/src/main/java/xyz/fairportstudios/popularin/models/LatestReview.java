package xyz.fairportstudios.popularin.models;

public class LatestReview {
    private Integer id;
    private Double rating;
    private String poster;

    public LatestReview(Integer id, Double rating, String poster) {
        this.id = id;
        this.rating = rating;
        this.poster = poster;
    }

    public Integer getId() {
        return id;
    }

    public Double getRating() {
        return rating;
    }

    public String getPoster() {
        return poster;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }
}
