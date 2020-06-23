package xyz.fairportstudios.popularin.models;

public class SelfDetail {
    private String mFull_name;
    private String mUsername;
    private String mEmail;

    public SelfDetail(
            String full_name,
            String username,
            String email
    ) {
        mFull_name = full_name;
        mUsername = username;
        mEmail = email;
    }

    public String getFull_name() {
        return mFull_name;
    }

    public String getUsername() {
        return mUsername;
    }

    public String getEmail() {
        return mEmail;
    }
}
