package xyz.fairportstudios.popularin.models;

public class Film {
    private Integer id;
    private Integer genre_ids;
    private String original_title;
    private String poster_path;
    private String release_date;

    public Film() {
        // Empty constructor
    }

    public Film(Integer id, Integer genre_ids, String original_title, String poster_path, String release_date) {
        this.id = id;
        this.genre_ids = genre_ids;
        this.original_title = original_title;
        this.poster_path = poster_path;
        this.release_date = release_date;
    }

    public Integer getId() {
        return id;
    }

    public Integer getGenre_ids() {
        return genre_ids;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setGenre_ids(Integer genre_ids) {
        this.genre_ids = genre_ids;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }
}
