package xyz.fairportstudios.popularin.models;

public class Cast {
    private Integer id;
    private Integer gender;
    private String character;
    private String name;
    private String profile_path;

    public Cast() {
        // Empty constructor
    }

    public Cast(Integer id, Integer gender, String character, String name, String profile_path) {
        this.id = id;
        this.gender = gender;
        this.character = character;
        this.name = name;
        this.profile_path = profile_path;
    }

    public Integer getId() {
        return id;
    }

    public Integer getGender() {
        return gender;
    }

    public String getCharacter() {
        return character;
    }

    public String getName() {
        return name;
    }

    public String getProfile_path() {
        return profile_path;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProfile_path(String profile_path) {
        this.profile_path = profile_path;
    }
}
