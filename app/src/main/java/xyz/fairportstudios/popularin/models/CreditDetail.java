package xyz.fairportstudios.popularin.models;

public class CreditDetail {
    String mName;
    String mKnown_for_department;
    String mBirthday;
    String mPlace_of_birth;
    String mProfile_path;

    public CreditDetail(
            String name,
            String known_for_department,
            String birthday,
            String place_of_birth,
            String profile_path
    ) {
        mName = name;
        mKnown_for_department = known_for_department;
        mBirthday = birthday;
        mPlace_of_birth = place_of_birth;
        mProfile_path = profile_path;
    }

    public String getName() {
        return mName;
    }

    public String getKnown_for_department() {
        return mKnown_for_department;
    }

    public String getBirthday() {
        return mBirthday;
    }

    public String getPlace_of_birth() {
        return mPlace_of_birth;
    }

    public String getProfile_path() {
        return mProfile_path;
    }
}
