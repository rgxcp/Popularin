package xyz.fairportstudios.popularin.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.adapters.FilmGridAdapter;
import xyz.fairportstudios.popularin.apis.tmdb.get.DiscoverFilmRequest;
import xyz.fairportstudios.popularin.models.Film;
import xyz.fairportstudios.popularin.statics.Popularin;

public class FilmListActivity extends AppCompatActivity {
    // Variable untuk fitur load more
    private Boolean isLoadFirstTime = true;
    private Boolean isLoading = true;
    private Integer currentPage = 1;
    private Integer totalPage;

    // Variable member
    private DiscoverFilmRequest discoverFilmRequest;
    private FilmGridAdapter filmGridAdapter;
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

        // Context
        final Context context = FilmListActivity.this;

        // Binding
        progressBar = findViewById(R.id.pbr_rtr_layout);
        recyclerFilm = findViewById(R.id.recycler_rtr_layout);
        anchorLayout = findViewById(R.id.anchor_rtr_layout);
        swipeRefresh = findViewById(R.id.swipe_refresh_rtr_layout);
        textMessage = findViewById(R.id.text_aud_message);
        Toolbar toolbar = findViewById(R.id.toolbar_rtr_layout);

        // Extra
        Intent intent = getIntent();
        genreID = intent.getStringExtra(Popularin.GENRE_ID);
        String genreTitle = intent.getStringExtra(Popularin.GENRE_TITLE);

        // Toolbar
        toolbar.setTitle(genreTitle);

        // Mendapatkan data awal
        filmList = new ArrayList<>();
        discoverFilmRequest = new DiscoverFilmRequest(context, genreID);
        discoverFilm(context, currentPage);

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
                    if (!isLoading && currentPage <= totalPage) {
                        isLoading = true;
                        discoverFilm(context, currentPage);
                        swipeRefresh.setRefreshing(true);
                    }
                }
            }
        });

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (isLoadFirstTime) {
                    discoverFilm(context, currentPage);
                }
                swipeRefresh.setRefreshing(false);
            }
        });
    }

    private void discoverFilm(final Context context, Integer page) {
        // Menghilangkan pesan setiap kali method dijalankan
        textMessage.setVisibility(View.GONE);

        discoverFilmRequest.sendRequest(page, new DiscoverFilmRequest.Callback() {
            @Override
            public void onSuccess(Integer pages, List<Film> films) {
                if (isLoadFirstTime) {
                    int insertIndex = filmList.size();
                    filmList.addAll(insertIndex, films);
                    filmGridAdapter = new FilmGridAdapter(context, filmList);
                    recyclerFilm.setAdapter(filmGridAdapter);
                    recyclerFilm.setLayoutManager(new GridLayoutManager(context, 4));
                    recyclerFilm.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    totalPage = pages;
                    isLoadFirstTime = false;
                } else {
                    int insertIndex = filmList.size();
                    filmList.addAll(insertIndex, films);
                    filmGridAdapter.notifyItemRangeInserted(insertIndex, films.size());
                    recyclerFilm.scrollToPosition(insertIndex);
                    swipeRefresh.setRefreshing(false);
                }
                currentPage++;
                isLoading = false;
            }

            @Override
            public void onError(String message) {
                if (isLoadFirstTime) {
                    progressBar.setVisibility(View.GONE);
                    textMessage.setVisibility(View.VISIBLE);
                    textMessage.setText(message);
                } else {
                    Snackbar.make(anchorLayout, message, Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }
}
