package xyz.fairportstudios.popularin.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.apis.tmdb.get.DiscoverFilmRequest;
import xyz.fairportstudios.popularin.models.Film;
import xyz.fairportstudios.popularin.statics.Popularin;

public class FilmListActivity extends AppCompatActivity {
    // Untuk fitur discover
    private Integer currentPage = 1;

    // Member variable
    private List<Film> filmList;
    private ProgressBar progressBar;
    private RecyclerView recyclerFilm;
    private RelativeLayout anchorLayout;
    private String genreID;
    private TextView textNetworkError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reusable_toolbar_recycler);

        // Binding
        progressBar = findViewById(R.id.pbr_rtr_layout);
        recyclerFilm = findViewById(R.id.recycler_rtr_layout);
        anchorLayout = findViewById(R.id.anchor_rtr_layout);
        textNetworkError = findViewById(R.id.text_rtr_empty_result);
        Toolbar toolbar = findViewById(R.id.toolbar_rtr_layout);

        // Extra
        Intent intent = getIntent();
        genreID = intent.getStringExtra(Popularin.GENRE_ID);
        String genreTitle = intent.getStringExtra(Popularin.GENRE_TITLE);

        // Toolbar
        toolbar.setTitle(genreTitle);

        // Mendapatkan data
        filmList = new ArrayList<>();
        discoverFilm(currentPage);

        // Activity
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        recyclerFilm.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    discoverFilm(currentPage);
                }
            }
        });
    }

    private void discoverFilm(Integer page) {
        DiscoverFilmRequest discoverFilmRequest = new DiscoverFilmRequest(this, genreID, filmList, recyclerFilm);
        String requestURL = discoverFilmRequest.getRequestURL(page);
        discoverFilmRequest.sendRequest(requestURL, new DiscoverFilmRequest.APICallback() {
            @Override
            public void onSuccess() {
                progressBar.setVisibility(View.GONE);
                currentPage++;
            }

            @Override
            public void onError() {
                progressBar.setVisibility(View.GONE);
                textNetworkError.setVisibility(View.VISIBLE);
                textNetworkError.setText(R.string.not_found);
                Snackbar.make(anchorLayout, R.string.network_error, Snackbar.LENGTH_LONG).show();
            }
        });
    }
}
