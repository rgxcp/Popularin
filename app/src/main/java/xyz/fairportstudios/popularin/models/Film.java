package xyz.fairportstudios.popularin.models;

public class Film {
    private Integer id;
    private Integer genre_id;
    private String original_title;
    private String release_date;
    private String poster_path;

    public Film() {
        // Constructor kosong
    }

    public Film(
            Integer id,
            Integer genre_id,
            String original_title,
            String release_date,
            String poster_path
    ) {
        this.id = id;
        this.genre_id = genre_id;
        this.original_title = original_title;
        this.release_date = release_date;
        this.poster_path = poster_path;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGenre_id() {
        return genre_id;
    }

    public void setGenre_id(Integer genre_id) {
        this.genre_id = genre_id;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }
}
