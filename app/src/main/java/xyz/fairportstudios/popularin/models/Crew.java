package xyz.fairportstudios.popularin.models;

public class Crew {
    private Integer id;
    private String job;
    private String name;
    private String profile_path;

    public Crew() {
        // Constructor kosong
    }

    public Crew(
            Integer id,
            String job,
            String name,
            String profile_path
    ) {
        this.id = id;
        this.job = job;
        this.name = name;
        this.profile_path = profile_path;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfile_path() {
        return profile_path;
    }

    public void setProfile_path(String profile_path) {
        this.profile_path = profile_path;
    }
}
