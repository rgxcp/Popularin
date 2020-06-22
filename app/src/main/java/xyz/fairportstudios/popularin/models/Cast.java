package xyz.fairportstudios.popularin.models;

public class Cast {
    private int mId;
    private String mName;
    private String mCharacter;
    private String mProfile_path;

    public Cast(
            int id,
            String name,
            String character,
            String profile_path
    ) {
        mId = id;
        mName = name;
        mCharacter = character;
        mProfile_path = profile_path;
    }

    public int getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public String getCharacter() {
        return mCharacter;
    }

    public String getProfile_path() {
        return mProfile_path;
    }
}
