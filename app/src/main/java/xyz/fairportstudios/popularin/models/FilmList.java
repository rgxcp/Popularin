package xyz.fairportstudios.popularin.models;

public class FilmList {
    private Integer id;
    private String original_title, poster_path;

    public FilmList(Integer id, String original_title, String poster_path) {
        this.id = id;
        this.original_title = original_title;
        this.poster_path = poster_path;
    }

    public int getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }
}
