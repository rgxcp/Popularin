package xyz.fairportstudios.popularin.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.models.FilmList;
import xyz.fairportstudios.popularin.apis.tmdb.DiscoverFilm;

public class FilmListActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film_list);

        RecyclerView recyclerView = findViewById(R.id.rcv_afl_film);
        Toolbar toolbar = findViewById(R.id.tbr_afl);

        Bundle bundle = getIntent().getExtras();
        String genreID = Objects.requireNonNull(bundle).getString("GENRE_ID");
        String genreTitle = Objects.requireNonNull(bundle).getString("GENRE_TITLE");

        toolbar.setTitle(genreTitle);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        List<FilmList> filmLists = new ArrayList<>();
        DiscoverFilm discoverFilm = new DiscoverFilm(FilmListActivity.this, filmLists, recyclerView);
        String requestURL = discoverFilm.getRequestURL(genreID, 1);
        discoverFilm.parseJSON(requestURL);
    }
}
