package xyz.fairportstudios.popularin.models;

public class FilmReview {
    private Integer id;
    private Integer user_id;
    private Double rating;
    private String review_text;
    private String review_date;
    private String first_name;
    private String profile_picture;

    public FilmReview() {
        // Empty constructor
    }

    public FilmReview(Integer id, Integer user_id, Double rating, String review_text, String review_date, String first_name, String profile_picture) {
        this.id = id;
        this.user_id = user_id;
        this.rating = rating;
        this.review_text = review_text;
        this.review_date = review_date;
        this.first_name = first_name;
        this.profile_picture = profile_picture;
    }

    public Integer getId() {
        return id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public Double getRating() {
        return rating;
    }

    public String getReview_text() {
        return review_text;
    }

    public String getReview_date() {
        return review_date;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getProfile_picture() {
        return profile_picture;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public void setReview_text(String review_text) {
        this.review_text = review_text;
    }

    public void setReview_date(String review_date) {
        this.review_date = review_date;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }
}
