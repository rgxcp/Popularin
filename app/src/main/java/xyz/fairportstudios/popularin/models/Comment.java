package xyz.fairportstudios.popularin.models;

public class Comment {
    private Integer id;
    private Integer user_id;
    private String comment_date;
    private String comment_text;
    private String first_name;
    private String profile_picture;

    public Comment() {
        // Empty constructor
    }

    public Comment(Integer id, Integer user_id, String comment_date, String comment_text, String first_name, String profile_picture) {
        this.id = id;
        this.user_id = user_id;
        this.comment_date = comment_date;
        this.comment_text = comment_text;
        this.first_name = first_name;
        this.profile_picture = profile_picture;
    }

    public Integer getId() {
        return id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public String getComment_date() {
        return comment_date;
    }

    public String getComment_text() {
        return comment_text;
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

    public void setComment_date(String comment_date) {
        this.comment_date = comment_date;
    }

    public void setComment_text(String comment_text) {
        this.comment_text = comment_text;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }
}
