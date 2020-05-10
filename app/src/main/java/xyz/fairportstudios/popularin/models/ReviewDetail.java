package xyz.fairportstudios.popularin.models;

public class ReviewDetail {
    private Boolean likeStatus;
    private Integer filmID;
    private Integer star;
    private Integer like;
    private String filmPoster;
    private String filmTitle;
    private String filmYear;
    private String reviewDate;
    private String reviewText;
    private String userID;
    private String userFirstName;
    private String userProfilePicture;

    public ReviewDetail() {
        // Empty constructor
    }

    public ReviewDetail(Boolean likeStatus, Integer filmID, Integer star, Integer like, String filmPoster, String filmTitle, String filmYear, String reviewDate, String reviewText, String userID, String userFirstName, String userProfilePicture) {
        this.likeStatus = likeStatus;
        this.filmID = filmID;
        this.star = star;
        this.like = like;
        this.filmPoster = filmPoster;
        this.filmTitle = filmTitle;
        this.filmYear = filmYear;
        this.reviewDate = reviewDate;
        this.reviewText = reviewText;
        this.userID = userID;
        this.userFirstName = userFirstName;
        this.userProfilePicture = userProfilePicture;
    }

    public Boolean getLikeStatus() {
        return likeStatus;
    }

    public Integer getFilmID() {
        return filmID;
    }

    public Integer getStar() {
        return star;
    }

    public Integer getLike() {
        return like;
    }

    public String getFilmPoster() {
        return filmPoster;
    }

    public String getFilmTitle() {
        return filmTitle;
    }

    public String getFilmYear() {
        return filmYear;
    }

    public String getReviewDate() {
        return reviewDate;
    }

    public String getReviewText() {
        return reviewText;
    }

    public String getUserID() {
        return userID;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public String getUserProfilePicture() {
        return userProfilePicture;
    }

    public void setLikeStatus(Boolean likeStatus) {
        this.likeStatus = likeStatus;
    }

    public void setFilmID(Integer filmID) {
        this.filmID = filmID;
    }

    public void setStar(Integer star) {
        this.star = star;
    }

    public void setLike(Integer like) {
        this.like = like;
    }

    public void setFilmPoster(String filmPoster) {
        this.filmPoster = filmPoster;
    }

    public void setFilmTitle(String filmTitle) {
        this.filmTitle = filmTitle;
    }

    public void setFilmYear(String filmYear) {
        this.filmYear = filmYear;
    }

    public void setReviewDate(String reviewDate) {
        this.reviewDate = reviewDate;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public void setUserProfilePicture(String userProfilePicture) {
        this.userProfilePicture = userProfilePicture;
    }
}
