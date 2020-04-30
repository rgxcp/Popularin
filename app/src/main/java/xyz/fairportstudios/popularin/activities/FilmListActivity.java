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
import xyz.fairportstudios.popularin.apis.tmdb.DiscoverRequest;
import xyz.fairportstudios.popularin.models.Film;

public class FilmListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film_list);

        // Binding
        Toolbar toolbar = findViewById(R.id.toolbar_afl_layout);
        RecyclerView recyclerView = findViewById(R.id.recycler_view_afl_layout);

        // Set-up list
        List<Film> filmList = new ArrayList<>();

        // Bundle
        Bundle bundle = getIntent().getExtras();
        String genreID = Objects.requireNonNull(bundle).getString("GENRE_ID");
        String genreTitle = Objects.requireNonNull(bundle).getString("GENRE_TITLE");

        // Toolbar
        toolbar.setTitle(genreTitle);

        // Mendapatkan data
        DiscoverRequest discoverRequest = new DiscoverRequest(this, filmList, recyclerView);
        String requestURL = discoverRequest.getRequestURL(genreID, "1");
        discoverRequest.sendRequest(requestURL);

        // Activity
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}
