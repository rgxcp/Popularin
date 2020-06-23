package xyz.fairportstudios.popularin.models;

public class FilmSelf {
    private boolean mIn_review;
    private boolean mIn_favorite;
    private boolean mIn_watchlist;
    private double mLast_rate;

    public FilmSelf(
            boolean in_review,
            boolean in_favorite,
            boolean in_watchlist,
            double last_rate
    ) {
        mIn_review = in_review;
        mIn_favorite = in_favorite;
        mIn_watchlist = in_watchlist;
        mLast_rate = last_rate;
    }

    public boolean getIn_review() {
        return mIn_review;
    }

    public boolean getIn_favorite() {
        return mIn_favorite;
    }

    public boolean getIn_watchlist() {
        return mIn_watchlist;
    }

    public double getLast_rate() {
        return mLast_rate;
    }
}
