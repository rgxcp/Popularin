package xyz.fairportstudios.popularin.models;

public class FilmSelf {
    private Boolean in_favorite;
    private Boolean in_review;
    private Boolean in_watchlist;
    private Double last_rate;

    public FilmSelf() {
        // Constructor kosong
    }

    public FilmSelf(
            Boolean in_favorite,
            Boolean in_review,
            Boolean in_watchlist,
            Double last_rate
    ) {
        this.in_favorite = in_favorite;
        this.in_review = in_review;
        this.in_watchlist = in_watchlist;
        this.last_rate = last_rate;
    }

    public Boolean getIn_favorite() {
        return in_favorite;
    }

    public void setIn_favorite(Boolean in_favorite) {
        this.in_favorite = in_favorite;
    }

    public Boolean getIn_review() {
        return in_review;
    }

    public void setIn_review(Boolean in_review) {
        this.in_review = in_review;
    }

    public Boolean getIn_watchlist() {
        return in_watchlist;
    }

    public void setIn_watchlist(Boolean in_watchlist) {
        this.in_watchlist = in_watchlist;
    }

    public Double getLast_rate() {
        return last_rate;
    }

    public void setLast_rate(Double last_rate) {
        this.last_rate = last_rate;
    }
}
