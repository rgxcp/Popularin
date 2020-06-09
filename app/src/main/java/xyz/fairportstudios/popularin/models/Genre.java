package xyz.fairportstudios.popularin.models;

public class Genre {
    private Integer id;
    private Integer background;
    private String title;

    public Genre(
            Integer id,
            Integer background,
            String title
    ) {
        this.id = id;
        this.background = background;
        this.title = title;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBackground() {
        return background;
    }

    public void setBackground(Integer background) {
        this.background = background;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
