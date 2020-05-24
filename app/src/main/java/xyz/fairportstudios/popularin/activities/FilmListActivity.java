package xyz.fairportstudios.popularin.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.apis.tmdb.get.DiscoverFilmRequest;
import xyz.fairportstudios.popularin.models.Film;

public class FilmListActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private RelativeLayout layout;
    private TextView emptyResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.global_toolbar_recycler);

        // Binding
        progressBar = findViewById(R.id.pbr_gtr_layout);
        layout = findViewById(R.id.layout_gtr_anchor);
        emptyResult = findViewById(R.id.text_fp_empty);
        RecyclerView recyclerView = findViewById(R.id.recycler_gtr_layout);
        Toolbar toolbar = findViewById(R.id.toolbar_gtr_layout);

        // Extra
        Intent intent = getIntent();
        String genreID = intent.getStringExtra("GENRE_ID");
        String genreTitle = intent.getStringExtra("GENRE_TITLE");

        // Toolbar
        toolbar.setTitle(genreTitle);

        // List
        List<Film> filmList = new ArrayList<>();

        // GET
        DiscoverFilmRequest discoverFilmRequest = new DiscoverFilmRequest(this, filmList, recyclerView);
        String requestURL = discoverFilmRequest.getRequestURL(genreID, 1);
        discoverFilmRequest.sendRequest(requestURL, new DiscoverFilmRequest.APICallback() {
            @Override
            public void onSuccess() {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError() {
                progressBar.setVisibility(View.GONE);
                emptyResult.setVisibility(View.VISIBLE);
                Snackbar.make(layout, R.string.network_error, Snackbar.LENGTH_LONG).show();
            }
        });

        // Activity
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}
