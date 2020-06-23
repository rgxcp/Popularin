package xyz.fairportstudios.popularin.models;

public class FilmMetadata {
    private double mAverage_rating;
    private int mTotal_review;
    private int mTotal_favorite;
    private int mTotal_watchlist;

    public FilmMetadata(
            double average_rating,
            int total_review,
            int total_favorite,
            int total_watchlist
    ) {
        mAverage_rating = average_rating;
        mTotal_review = total_review;
        mTotal_favorite = total_favorite;
        mTotal_watchlist = total_watchlist;
    }

    public double getAverage_rating() {
        return mAverage_rating;
    }

    public int getTotal_review() {
        return mTotal_review;
    }

    public int getTotal_favorite() {
        return mTotal_favorite;
    }

    public int getTotal_watchlist() {
        return mTotal_watchlist;
    }
}
