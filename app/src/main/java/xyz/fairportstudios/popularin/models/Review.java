package xyz.fairportstudios.popularin.models;

public class Review {
    private int mId;
    private int mTmdb_id;
    private int mUser_id;
    private int mTotal_like;
    private int mTotal_comment;
    private boolean mIs_liked;
    private double mRating;
    private String mReview_detail;
    private String mTimestamp;
    private String mTitle;
    private String mRelease_date;
    private String mPoster;
    private String mUsername;
    private String mProfile_picture;

    public Review(
            int id,
            int tmdb_id,
            int user_id,
            int total_like,
            int total_comment,
            boolean is_liked,
            double rating,
            String review_detail,
            String timestamp,
            String title,
            String release_date,
            String poster,
            String username,
            String profile_picture
    ) {
        mId = id;
        mTmdb_id = tmdb_id;
        mUser_id = user_id;
        mTotal_like = total_like;
        mTotal_comment = total_comment;
        mIs_liked = is_liked;
        mRating = rating;
        mReview_detail = review_detail;
        mTimestamp = timestamp;
        mTitle = title;
        mRelease_date = release_date;
        mPoster = poster;
        mUsername = username;
        mProfile_picture = profile_picture;
    }

    public int getId() {
        return mId;
    }

    public int getTmdb_id() {
        return mTmdb_id;
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

    public String getTitle() {
        return mTitle;
    }

    public String getRelease_date() {
        return mRelease_date;
    }

    public String getPoster() {
        return mPoster;
    }

    public String getUsername() {
        return mUsername;
    }

    public String getProfile_picture() {
        return mProfile_picture;
    }
}
