package xyz.fairportstudios.popularin.models;

public class SelfDetail {
    private String full_name;
    private String username;
    private String email;

    public SelfDetail() {
        // Constructor kosong
    }

    public SelfDetail(
            String full_name,
            String username,
            String email
    ) {
        this.full_name = full_name;
        this.username = username;
        this.email = email;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
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
