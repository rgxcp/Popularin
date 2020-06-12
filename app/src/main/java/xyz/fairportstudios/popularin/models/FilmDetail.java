package xyz.fairportstudios.popularin.models;

public class FilmDetail {
    private Integer genre_id;
    private Integer runtime;
    private String original_title;
    private String release_date;
    private String overview;
    private String poster_path;
    private String video_key;

    public FilmDetail() {
        // Constructor kosong
    }

    public FilmDetail(
            Integer genre_id,
            Integer runtime,
            String original_title,
            String release_date,
            String overview,
            String poster_path,
            String video_key
    ) {
        this.genre_id = genre_id;
        this.runtime = runtime;
        this.original_title = original_title;
        this.release_date = release_date;
        this.overview = overview;
        this.poster_path = poster_path;
        this.video_key = video_key;
    }

    public Integer getGenre_id() {
        return genre_id;
    }

    public void setGenre_id(Integer genre_id) {
        this.genre_id = genre_id;
    }

    public Integer getRuntime() {
        return runtime;
    }

    public void setRuntime(Integer runtime) {
        this.runtime = runtime;
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

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getVideo_key() {
        return video_key;
    }

    public void setVideo_key(String video_key) {
        this.video_key = video_key;
    }
}
