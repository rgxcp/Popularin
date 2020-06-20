package xyz.fairportstudios.popularin.models;

public class Genre {
    private int mId;
    private int mBackground;
    private String mTitle;

    public Genre(int id, int background, String title) {
        mId = id;
        mBackground = background;
        mTitle = title;
    }

    public int getId() {
        return mId;
    }

    public int getBackground() {
        return mBackground;
    }

    public String getTitle() {
        return mTitle;
    }
}
