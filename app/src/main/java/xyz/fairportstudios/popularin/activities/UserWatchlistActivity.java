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
import xyz.fairportstudios.popularin.apis.popularin.get.UserWatchlistRequest;
import xyz.fairportstudios.popularin.models.Film;
import xyz.fairportstudios.popularin.statics.Popularin;

public class UserWatchlistActivity extends AppCompatActivity {
    private List<Film> filmList;
    private ProgressBar progressBar;
    private RecyclerView recyclerWatchlist;
    private RelativeLayout anchorLayout;
    private String userID;
    private TextView textEmptyWatchlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reusable_toolbar_recycler);

        // Binding
        progressBar = findViewById(R.id.pbr_rtr_layout);
        recyclerWatchlist = findViewById(R.id.recycler_rtr_layout);
        anchorLayout = findViewById(R.id.anchor_rtr_layout);
        textEmptyWatchlist = findViewById(R.id.text_rtr_empty_result);
        Toolbar toolbar = findViewById(R.id.toolbar_rtr_layout);

        // Extra
        Intent intent = getIntent();
        userID = intent.getStringExtra(Popularin.USER_ID);

        // Toolbar
        toolbar.setTitle(R.string.watchlist);

        // Mendapatkan data
        filmList = new ArrayList<>();
        getUserWatchlist();

        // Activity
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void getUserWatchlist() {
        UserWatchlistRequest userWatchlistRequest = new UserWatchlistRequest(this, userID, filmList, recyclerWatchlist);
        String requestURL = userWatchlistRequest.getRequestURL(1);
        userWatchlistRequest.sendRequest(requestURL, new UserWatchlistRequest.APICallback() {
            @Override
            public void onSuccess() {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onEmpty() {
                progressBar.setVisibility(View.GONE);
                textEmptyWatchlist.setVisibility(View.VISIBLE);
                textEmptyWatchlist.setText(R.string.empty_user_watchlist);
            }

            @Override
            public void onError() {
                progressBar.setVisibility(View.GONE);
                textEmptyWatchlist.setVisibility(View.VISIBLE);
                textEmptyWatchlist.setText(R.string.not_found);
                Snackbar.make(anchorLayout, R.string.network_error, Snackbar.LENGTH_LONG).show();
            }
        });
    }
}
