package xyz.fairportstudios.popularin.models;

public class FilmMetadata {
    private Double average_rating;
    private Integer favorites;
    private Integer reviews;
    private Integer watchlists;
    private Integer rate_05;
    private Integer rate_10;
    private Integer rate_15;
    private Integer rate_20;
    private Integer rate_25;
    private Integer rate_30;
    private Integer rate_35;
    private Integer rate_40;
    private Integer rate_45;
    private Integer rate_50;

    public FilmMetadata() {
        // Empty constructor
    }

    public FilmMetadata(Double average_rating, Integer favorites, Integer reviews, Integer watchlists, Integer rate_05, Integer rate_10, Integer rate_15, Integer rate_20, Integer rate_25, Integer rate_30, Integer rate_35, Integer rate_40, Integer rate_45, Integer rate_50) {
        this.average_rating = average_rating;
        this.favorites = favorites;
        this.reviews = reviews;
        this.watchlists = watchlists;
        this.rate_05 = rate_05;
        this.rate_10 = rate_10;
        this.rate_15 = rate_15;
        this.rate_20 = rate_20;
        this.rate_25 = rate_25;
        this.rate_30 = rate_30;
        this.rate_35 = rate_35;
        this.rate_40 = rate_40;
        this.rate_45 = rate_45;
        this.rate_50 = rate_50;
    }

    public Double getAverage_rating() {
        return average_rating;
    }

    public Integer getFavorites() {
        return favorites;
    }

    public Integer getReviews() {
        return reviews;
    }

    public Integer getWatchlists() {
        return watchlists;
    }

    public Integer getRate_05() {
        return rate_05;
    }

    public Integer getRate_10() {
        return rate_10;
    }

    public Integer getRate_15() {
        return rate_15;
    }

    public Integer getRate_20() {
        return rate_20;
    }

    public Integer getRate_25() {
        return rate_25;
    }

    public Integer getRate_30() {
        return rate_30;
    }

    public Integer getRate_35() {
        return rate_35;
    }

    public Integer getRate_40() {
        return rate_40;
    }

    public Integer getRate_45() {
        return rate_45;
    }

    public Integer getRate_50() {
        return rate_50;
    }

    public void setAverage_rating(Double average_rating) {
        this.average_rating = average_rating;
    }

    public void setFavorites(Integer favorites) {
        this.favorites = favorites;
    }

    public void setReviews(Integer reviews) {
        this.reviews = reviews;
    }

    public void setWatchlists(Integer watchlists) {
        this.watchlists = watchlists;
    }

    public void setRate_05(Integer rate_05) {
        this.rate_05 = rate_05;
    }

    public void setRate_10(Integer rate_10) {
        this.rate_10 = rate_10;
    }

    public void setRate_15(Integer rate_15) {
        this.rate_15 = rate_15;
    }

    public void setRate_20(Integer rate_20) {
        this.rate_20 = rate_20;
    }

    public void setRate_25(Integer rate_25) {
        this.rate_25 = rate_25;
    }

    public void setRate_30(Integer rate_30) {
        this.rate_30 = rate_30;
    }

    public void setRate_35(Integer rate_35) {
        this.rate_35 = rate_35;
    }

    public void setRate_40(Integer rate_40) {
        this.rate_40 = rate_40;
    }

    public void setRate_45(Integer rate_45) {
        this.rate_45 = rate_45;
    }

    public void setRate_50(Integer rate_50) {
        this.rate_50 = rate_50;
    }
}
