package xyz.fairportstudios.popularin.models;

public class Crew {
    private Integer id;
    private Integer gender;
    private String job;
    private String name;
    private String profile_path;

    public Crew() {
        // Empty constructor
    }

    public Crew(Integer id, Integer gender, String job, String name, String profile_path) {
        this.id = id;
        this.gender = gender;
        this.job = job;
        this.name = name;
        this.profile_path = profile_path;
    }

    public Integer getId() {
        return id;
    }

    public Integer getGender() {
        return gender;
    }

    public String getJob() {
        return job;
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

    public void setJob(String job) {
        this.job = job;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProfile_path(String profile_path) {
        this.profile_path = profile_path;
    }
}
