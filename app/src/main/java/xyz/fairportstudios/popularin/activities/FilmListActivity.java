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
import xyz.fairportstudios.popularin.utilities.DiscoverFilm;
import xyz.fairportstudios.popularin.utilities.TMDBRequestURL;

public class FilmListActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film_list);

        Bundle mBundle = getIntent().getExtras();
        String mGenreID = Objects.requireNonNull(mBundle).getString("GENRE_ID");
        String mGenreTitle = Objects.requireNonNull(mBundle).getString("GENRE_TITLE");

        Toolbar mToolbar = findViewById(R.id.tbr_afl);
        mToolbar.setTitle(mGenreTitle);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        List<FilmList> mFilmList = new ArrayList<>();
        RecyclerView mRecyclerView = findViewById(R.id.rcv_afl_film);

        TMDBRequestURL mTMDBRequestURL = new TMDBRequestURL();
        String mRequestURL = mTMDBRequestURL.getDiscoverFilmURL(mGenreID);

        DiscoverFilm mDiscoverFilm = new DiscoverFilm(mFilmList, mRecyclerView);
        mDiscoverFilm.parseJSON(mRequestURL, FilmListActivity.this);
    }
}
