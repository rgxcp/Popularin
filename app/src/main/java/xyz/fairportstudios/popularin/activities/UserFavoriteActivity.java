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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.adapters.FilmAdapter;
import xyz.fairportstudios.popularin.apis.popularin.get.UserFavoriteRequest;
import xyz.fairportstudios.popularin.models.Film;
import xyz.fairportstudios.popularin.preferences.Auth;
import xyz.fairportstudios.popularin.statics.Popularin;

public class UserFavoriteActivity extends AppCompatActivity {
    // Variable untuk fitur load more
    private Boolean isLoadFirstTime = true;
    private Boolean isLoading = true;
    private Integer currentPage = 1;
    private Integer totalPage;

    // Variable member
    private Boolean isSelf;
    private FilmAdapter filmAdapter;
    private List<Film> filmList;
    private ProgressBar progressBar;
    private RecyclerView recyclerFilm;
    private RelativeLayout anchorLayout;
    private SwipeRefreshLayout swipeRefresh;
    private TextView textMessage;
    private UserFavoriteRequest userFavoriteRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reusable_toolbar_recycler);

        // Context
        final Context context = UserFavoriteActivity.this;

        // Extra
        Intent intent = getIntent();
        int userID = intent.getIntExtra(Popularin.USER_ID, 0);

        // Auth
        isSelf = userID == new Auth(context).getAuthID();

        // Binding
        progressBar = findViewById(R.id.pbr_rtr_layout);
        recyclerFilm = findViewById(R.id.recycler_rtr_layout);
        anchorLayout = findViewById(R.id.anchor_rtr_layout);
        swipeRefresh = findViewById(R.id.swipe_refresh_rtr_layout);
        textMessage = findViewById(R.id.text_aud_message);
        Toolbar toolbar = findViewById(R.id.toolbar_rtr_layout);

        // Toolbar
        toolbar.setTitle(R.string.favorite);

        // Mendapatkan data awal
        filmList = new ArrayList<>();
        userFavoriteRequest = new UserFavoriteRequest(context, userID);
        getUserFavorite(context, currentPage);

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
                        getUserFavorite(context, currentPage);
                        swipeRefresh.setRefreshing(true);
                    }
                }
            }
        });

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (isLoadFirstTime) {
                    getUserFavorite(context, currentPage);
                }
                swipeRefresh.setRefreshing(false);
            }
        });
    }

    private void getUserFavorite(final Context context, Integer page) {
        // Menghilangkan pesan setiap kali method dijalankan
        textMessage.setVisibility(View.GONE);

        userFavoriteRequest.sendRequest(page, new UserFavoriteRequest.Callback() {
            @Override
            public void onSuccess(Integer pages, List<Film> films) {
                if (isLoadFirstTime) {
                    int insertIndex = filmList.size();
                    filmList.addAll(insertIndex, films);
                    filmAdapter = new FilmAdapter(context, filmList);
                    recyclerFilm.setAdapter(filmAdapter);
                    recyclerFilm.setLayoutManager(new LinearLayoutManager(context));
                    recyclerFilm.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    totalPage = pages;
                    isLoadFirstTime = false;
                } else {
                    int insertIndex = filmList.size();
                    filmList.addAll(insertIndex, films);
                    filmAdapter.notifyItemRangeInserted(insertIndex, films.size());
                    recyclerFilm.scrollToPosition(insertIndex);
                    swipeRefresh.setRefreshing(false);
                }
                currentPage++;
                isLoading = false;
            }

            @Override
            public void onNotFound() {
                progressBar.setVisibility(View.GONE);
                textMessage.setVisibility(View.VISIBLE);
                if (!isSelf) {
                    textMessage.setText(R.string.empty_user_favorite);
                } else {
                    textMessage.setText(R.string.empty_account_favorite);
                }
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
