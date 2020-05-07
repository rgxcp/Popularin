package xyz.fairportstudios.popularin.models;

public class LatestFavorite {
    private Integer tmdb_id;
    private String poster;

    public LatestFavorite() {
        // Empty constructor
    }

    public LatestFavorite(Integer tmdb_id, String poster) {
        this.tmdb_id = tmdb_id;
        this.poster = poster;
    }

    public Integer getTmdb_id() {
        return tmdb_id;
    }

    public String getPoster() {
        return poster;
    }

    public void setTmdb_id(Integer tmdb_id) {
        this.tmdb_id = tmdb_id;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }
}
