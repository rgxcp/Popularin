package xyz.fairportstudios.popularin.models;

public class RecentFavorite {
    private int mTmdb_id;
    private String mTitle;
    private String mRelease_date;
    private String mPoster;

    public RecentFavorite(
            int tmdb_id,
            String title,
            String release_date,
            String poster
    ) {
        mTmdb_id = tmdb_id;
        mTitle = title;
        mRelease_date = release_date;
        mPoster = poster;
    }

    public int getTmdb_id() {
        return mTmdb_id;
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
