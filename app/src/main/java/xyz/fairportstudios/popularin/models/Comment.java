package xyz.fairportstudios.popularin.models;

public class Comment {
    private Integer id;
    private Integer user_id;
    private String comment_detail;
    private String comment_text;
    private String timestamp;
    private String first_name;
    private String profile_picture;

    public Comment() {
        // Empty constructor
    }

    public Comment(
            Integer id,
            Integer user_id,
            String comment_detail,
            String comment_text,
            String timestamp,
            String first_name,
            String profile_picture
    ) {
        this.id = id;
        this.user_id = user_id;
        this.comment_detail = comment_detail;
        this.comment_text = comment_text;
        this.timestamp = timestamp;
        this.first_name = first_name;
        this.profile_picture = profile_picture;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public String getComment_detail() {
        return comment_detail;
    }

    public void setComment_detail(String comment_detail) {
        this.comment_detail = comment_detail;
    }

    public String getComment_text() {
        return comment_text;
    }

    public void setComment_text(String comment_text) {
        this.comment_text = comment_text;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getProfile_picture() {
        return profile_picture;
    }

    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }
}
