package xyz.fairportstudios.popularin.models;

public class User {
    private Integer id;
    private String full_name;
    private String username;
    private String profile_picture;

    public User(Integer id, String full_name, String username, String profile_picture) {
        this.id = id;
        this.full_name = full_name;
        this.username = username;
        this.profile_picture = profile_picture;
    }

    public Integer getId() {
        return id;
    }

    public String getFull_name() {
        return full_name;
    }

    public String getUsername() {
        return username;
    }

    public String getProfile_picture() {
        return profile_picture;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }
}
