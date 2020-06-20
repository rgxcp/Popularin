package xyz.fairportstudios.popularin.models;

public class User {
    private int mId;
    private String mFull_name;
    private String mUsername;
    private String mProfile_picture;

    public User(
            int id,
            String full_name,
            String username,
            String profile_picture
    ) {
        mId = id;
        mFull_name = full_name;
        mUsername = username;
        mProfile_picture = profile_picture;
    }

    public int getId() {
        return mId;
    }

    public String getFull_name() {
        return mFull_name;
    }

    public String getUsername() {
        return mUsername;
    }

    public String getProfile_picture() {
        return mProfile_picture;
    }
}
