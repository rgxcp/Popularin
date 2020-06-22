package xyz.fairportstudios.popularin.models;

public class Crew {
    private int mId;
    private String mName;
    private String mJob;
    private String mProfile_path;

    public Crew(
            int id,
            String name,
            String job,
            String profile_path
    ) {
        mId = id;
        mName = name;
        mJob = job;
        mProfile_path = profile_path;
    }

    public int getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public String getJob() {
        return mJob;
    }

    public String getProfile_path() {
        return mProfile_path;
    }
}
