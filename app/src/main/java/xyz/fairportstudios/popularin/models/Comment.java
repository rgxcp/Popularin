package xyz.fairportstudios.popularin.models;

public class Comment {
    private int mId;
    private int mUser_id;
    private String mComment_detail;
    private String mTimestamp;
    private String mUsername;
    private String mProfile_picture;

    public Comment(
            int id,
            int user_id,
            String comment_detail,
            String timestamp,
            String username,
            String profile_picture
    ) {
        mId = id;
        mUser_id = user_id;
        mComment_detail = comment_detail;
        mTimestamp = timestamp;
        mUsername = username;
        mProfile_picture = profile_picture;
    }

    public int getId() {
        return mId;
    }

    public int getUser_id() {
        return mUser_id;
    }

    public String getComment_detail() {
        return mComment_detail;
    }

    public String getTimestamp() {
        return mTimestamp;
    }

    public String getUsername() {
        return mUsername;
    }

    public String getProfile_picture() {
        return mProfile_picture;
    }
}
