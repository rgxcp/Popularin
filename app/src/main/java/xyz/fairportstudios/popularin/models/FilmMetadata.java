package xyz.fairportstudios.popularin.models;

public class FilmMetadata {
    private Double average_rating;
    private Integer total_review;
    private Integer total_favorite;
    private Integer total_watchlist;

    public FilmMetadata() {
        // Constructor kosong
    }

    public FilmMetadata(
            Double average_rating,
            Integer total_review,
            Integer total_favorite,
            Integer total_watchlist
    ) {
        this.average_rating = average_rating;
        this.total_review = total_review;
        this.total_favorite = total_favorite;
        this.total_watchlist = total_watchlist;
    }

    public Double getAverage_rating() {
        return average_rating;
    }

    public void setAverage_rating(Double average_rating) {
        this.average_rating = average_rating;
    }

    public Integer getTotal_review() {
        return total_review;
    }

    public void setTotal_review(Integer total_review) {
        this.total_review = total_review;
    }

    public Integer getTotal_favorite() {
        return total_favorite;
    }

    public void setTotal_favorite(Integer total_favorite) {
        this.total_favorite = total_favorite;
    }

    public Integer getTotal_watchlist() {
        return total_watchlist;
    }

    public void setTotal_watchlist(Integer total_watchlist) {
        this.total_watchlist = total_watchlist;
    }
}
