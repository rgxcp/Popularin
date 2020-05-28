package xyz.fairportstudios.popularin.models;

public class RecentFavorite {
    private Integer tmdb_id;
    private String title;
    private String release_date;
    private String poster;

    public RecentFavorite() {
        // Constructor kosong
    }

    public RecentFavorite(
            Integer tmdb_id,
            String title,
            String release_date,
            String poster
    ) {
        this.tmdb_id = tmdb_id;
        this.title = title;
        this.release_date = release_date;
        this.poster = poster;
    }

    public Integer getTmdb_id() {
        return tmdb_id;
    }

    public void setTmdb_id(Integer tmdb_id) {
        this.tmdb_id = tmdb_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }
}
