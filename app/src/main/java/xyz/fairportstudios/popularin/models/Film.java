package xyz.fairportstudios.popularin.models;

public class Film {
    private int mId;
    private int mGenre_id;
    private String mOriginal_title;
    private String mRelease_date;
    private String mPoster_path;

    public Film(
            int id,
            int genre_id,
            String original_title,
            String release_date,
            String poster_path
    ) {
        mId = id;
        mGenre_id = genre_id;
        mOriginal_title = original_title;
        mRelease_date = release_date;
        mPoster_path = poster_path;
    }

    public int getId() {
        return mId;
    }

    public int getGenre_id() {
        return mGenre_id;
    }

    public String getOriginal_title() {
        return mOriginal_title;
    }

    public String getRelease_date() {
        return mRelease_date;
    }

    public String getPoster_path() {
        return mPoster_path;
    }
}
