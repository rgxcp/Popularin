package xyz.fairportstudios.popularin.models;

public class RecentReview {
    private int mId;
    private int mTmdb_id;
    private double mRating;
    private String mTitle;
    private String mRelease_date;
    private String mPoster;

    public RecentReview(
            int id,
            int tmdb_id,
            double rating,
            String title,
            String release_date,
            String poster
    ) {
        mId = id;
        mTmdb_id = tmdb_id;
        mRating = rating;
        mTitle = title;
        mRelease_date = release_date;
        mPoster = poster;
    }

    public int getId() {
        return mId;
    }

    public int getTmdb_id() {
        return mTmdb_id;
    }

    public double getRating() {
        return mRating;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getRelease_date() {
        return mRelease_date;
    }

    public String getPoster() {
        return mPoster;
    }
}
