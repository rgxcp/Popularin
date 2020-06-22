package xyz.fairportstudios.popularin.models;

public class ReviewDetail {
    private int mTmdb_id;
    private int mUser_id;
    private int mTotal_like;
    private boolean mIs_liked;
    private double mRating;
    private String mReview_detail;
    private String mReview_date;
    private String mWatch_date;
    private String mTitle;
    private String mRelease_date;
    private String mPoster;
    private String mUsername;
    private String mProfile_picture;

    public ReviewDetail(
            int tmdb_id,
            int user_id,
            int total_like,
            boolean is_liked,
            double rating,
            String review_detail,
            String review_date,
            String watch_date,
            String title,
            String release_date,
            String poster,
            String username,
            String profile_picture
    ) {
        mTmdb_id = tmdb_id;
        mUser_id = user_id;
        mTotal_like = total_like;
        mIs_liked = is_liked;
        mRating = rating;
        mReview_detail = review_detail;
        mReview_date = review_date;
        mWatch_date = watch_date;
        mTitle = title;
        mRelease_date = release_date;
        mPoster = poster;
        mUsername = username;
        mProfile_picture = profile_picture;
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

    public boolean getIs_liked() {
        return mIs_liked;
    }

    public double getRating() {
        return mRating;
    }

    public String getReview_detail() {
        return mReview_detail;
    }

    public String getReview_date() {
        return mReview_date;
    }

    public String getWatch_date() {
        return mWatch_date;
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
