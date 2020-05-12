package xyz.fairportstudios.popularin.models;

public class LatestFavorite {
    private Integer tmdb_id;
    private String poster;
    private String release_date;
    private String title;

    public LatestFavorite() {
        // Empty constructor
    }

    public LatestFavorite(Integer tmdb_id, String poster, String release_date, String title) {
        this.tmdb_id = tmdb_id;
        this.poster = poster;
        this.release_date = release_date;
        this.title = title;
    }

    public Integer getTmdb_id() {
        return tmdb_id;
    }

    public String getPoster() {
        return poster;
    }

    public String getRelease_date() {
        return release_date;
    }

    public String getTitle() {
        return title;
    }

    public void setTmdb_id(Integer tmdb_id) {
        this.tmdb_id = tmdb_id;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
