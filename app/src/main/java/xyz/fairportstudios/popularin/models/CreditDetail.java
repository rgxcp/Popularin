package xyz.fairportstudios.popularin.models;

public class CreditDetail {
    String name;
    String known_for_department;
    String birthday;
    String place_of_birth;
    String profile_path;

    public CreditDetail() {
        // Constructor kosong
    }

    public CreditDetail(
            String name,
            String known_for_department,
            String birthday,
            String place_of_birth,
            String profile_path
    ) {
        this.name = name;
        this.known_for_department = known_for_department;
        this.birthday = birthday;
        this.place_of_birth = place_of_birth;
        this.profile_path = profile_path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKnown_for_department() {
        return known_for_department;
    }

    public void setKnown_for_department(String known_for_department) {
        this.known_for_department = known_for_department;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getPlace_of_birth() {
        return place_of_birth;
    }

    public void setPlace_of_birth(String place_of_birth) {
        this.place_of_birth = place_of_birth;
    }

    public String getProfile_path() {
        return profile_path;
    }

    public void setProfile_path(String profile_path) {
        this.profile_path = profile_path;
    }
}
