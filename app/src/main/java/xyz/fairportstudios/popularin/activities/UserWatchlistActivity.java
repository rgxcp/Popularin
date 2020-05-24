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

public class UserWatchlistActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private RelativeLayout layout;
    private TextView emptyResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reusable_toolbar_recycler);

        // Binding
        progressBar = findViewById(R.id.pbr_rtr_layout);
        layout = findViewById(R.id.anchor_rtr_layout);
        emptyResult = findViewById(R.id.text_rtr_empty_result);
        RecyclerView recyclerView = findViewById(R.id.recycler_rtr_layout);
        Toolbar toolbar = findViewById(R.id.toolbar_rtr_layout);

        // Extra
        Intent intent = getIntent();
        String userID = intent.getStringExtra("USER_ID");

        // Toolbar
        toolbar.setTitle("Watchlist");

        // List
        List<Film> filmList = new ArrayList<>();

        // GET
        UserWatchlistRequest userWatchlistRequest = new UserWatchlistRequest(this, filmList, recyclerView);
        String requestURL = userWatchlistRequest.getRequestURL(userID, 1);
        userWatchlistRequest.sendRequest(requestURL, new UserWatchlistRequest.APICallback() {
            @Override
            public void onSuccess() {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onEmpty() {
                progressBar.setVisibility(View.GONE);
                emptyResult.setVisibility(View.VISIBLE);
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
