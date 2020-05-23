package xyz.fairportstudios.popularin.models;

public class Comment {
    private Integer id;
    private Integer user_id;
    private String comment_detail;
    private String timestamp;
    private String username;
    private String profile_picture;

    public Comment() {
        // Constructor kosong
    }

    public Comment(
            Integer id,
            Integer user_id,
            String comment_detail,
            String timestamp,
            String username,
            String profile_picture
    ) {
        this.id = id;
        this.user_id = user_id;
        this.comment_detail = comment_detail;
        this.timestamp = timestamp;
        this.username = username;
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

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfile_picture() {
        return profile_picture;
    }

    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }
}
