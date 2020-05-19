package xyz.fairportstudios.popularin.models;

public class SelfDetail {
    private String first_name;
    private String last_name;
    private String username;
    private String email;

    public SelfDetail() {
        // Empty constructor
    }

    public SelfDetail(
            String first_name,
            String last_name,
            String username,
            String email
    ) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.username = username;
        this.email = email;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
