package xyz.fairportstudios.popularin.models;

public class FilmReview {
    private int mId;
    private int mUser_id;
    private int mTotal_like;
    private int mTotal_comment;
    private boolean mIs_liked;
    private double mRating;
    private String mReview_detail;
    private String mTimestamp;
    private String mUsername;
    private String mProfile_picture;

    public FilmReview(
            int id,
            int user_id,
            int total_like,
            int total_comment,
            boolean is_liked,
            double rating,
            String review_detail,
            String timestamp,
            String username,
            String profile_picture
    ) {
        mId = id;
        mUser_id = user_id;
        mTotal_like = total_like;
        mTotal_comment = total_comment;
        mIs_liked = is_liked;
        mRating = rating;
        mReview_detail = review_detail;
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

    public int getTotal_like() {
        return mTotal_like;
    }

    public void setTotal_like(int total_like) {
        mTotal_like = total_like;
    }

    public int getTotal_comment() {
        return mTotal_comment;
    }

    public boolean getIs_liked() {
        return mIs_liked;
    }

    public void setIs_liked(boolean is_liked) {
        mIs_liked = is_liked;
    }

    public double getRating() {
        return mRating;
    }

    public String getReview_detail() {
        return mReview_detail;
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
