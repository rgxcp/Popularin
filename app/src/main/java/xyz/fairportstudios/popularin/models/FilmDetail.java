package xyz.fairportstudios.popularin.models;

public class FilmDetail {
    private Integer runtime;
    private String backdrop_path;
    private String genre;
    private String original_title;
    private String overview;
    private String poster_path;
    private String release_date;

    public FilmDetail() {
        // Empty constructor
    }

    public FilmDetail(Integer runtime, String backdrop_path, String genre, String original_title, String overview, String poster_path, String release_date) {
        this.runtime = runtime;
        this.backdrop_path = backdrop_path;
        this.genre = genre;
        this.original_title = original_title;
        this.overview = overview;
        this.poster_path = poster_path;
        this.release_date = release_date;
    }

    public Integer getRuntime() {
        return runtime;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public String getGenre() {
        return genre;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public String getOverview() {
        return overview;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRuntime(Integer runtime) {
        this.runtime = runtime;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }
}
