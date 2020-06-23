package xyz.fairportstudios.popularin.models;

public class FilmDetail {
    private int mGenre_id;
    private int mRuntime;
    private String mOriginal_title;
    private String mRelease_date;
    private String mOverview;
    private String mPoster_path;
    private String mVideo_key;

    public FilmDetail(
            int genre_id,
            int runtime,
            String original_title,
            String release_date,
            String overview,
            String poster_path,
            String video_key
    ) {
        mGenre_id = genre_id;
        mRuntime = runtime;
        mOriginal_title = original_title;
        mRelease_date = release_date;
        mOverview = overview;
        mPoster_path = poster_path;
        mVideo_key = video_key;
    }

    public int getGenre_id() {
        return mGenre_id;
    }

    public int getRuntime() {
        return mRuntime;
    }

    public String getOriginal_title() {
        return mOriginal_title;
    }

    public String getRelease_date() {
        return mRelease_date;
    }

    public String getOverview() {
        return mOverview;
    }

    public String getPoster_path() {
        return mPoster_path;
    }

    public String getVideo_key() {
        return mVideo_key;
    }
}
