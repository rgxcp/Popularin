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
import xyz.fairportstudios.popularin.apis.popularin.get.UserFavoriteRequest;
import xyz.fairportstudios.popularin.models.Film;
import xyz.fairportstudios.popularin.statics.Popularin;

public class UserFavoriteActivity extends AppCompatActivity {
    private List<Film> filmList;
    private ProgressBar progressBar;
    private RecyclerView recyclerFavorite;
    private RelativeLayout anchorLayout;
    private String userID;
    private TextView textEmptyFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reusable_toolbar_recycler);

        // Binding
        progressBar = findViewById(R.id.pbr_rtr_layout);
        recyclerFavorite = findViewById(R.id.recycler_rtr_layout);
        anchorLayout = findViewById(R.id.anchor_rtr_layout);
        textEmptyFavorite = findViewById(R.id.text_rtr_empty_result);
        Toolbar toolbar = findViewById(R.id.toolbar_rtr_layout);

        // Extra
        Intent intent = getIntent();
        userID = intent.getStringExtra(Popularin.USER_ID);

        // Toolbar
        toolbar.setTitle(R.string.favorite);

        // Mendapatkan data
        filmList = new ArrayList<>();
        getUserFavorite();

        // Activity
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void getUserFavorite() {
        UserFavoriteRequest userFavoriteRequest = new UserFavoriteRequest(this, userID, filmList, recyclerFavorite);
        String requestURL = userFavoriteRequest.getRequestURL(1);
        userFavoriteRequest.sendRequest(requestURL, new UserFavoriteRequest.JSONCallback() {
            @Override
            public void onSuccess() {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onEmpty() {
                progressBar.setVisibility(View.GONE);
                textEmptyFavorite.setVisibility(View.VISIBLE);
                textEmptyFavorite.setText(R.string.empty_user_favorite);
            }

            @Override
            public void onError() {
                progressBar.setVisibility(View.GONE);
                textEmptyFavorite.setVisibility(View.VISIBLE);
                textEmptyFavorite.setText(R.string.not_found);
                Snackbar.make(anchorLayout, R.string.network_error, Snackbar.LENGTH_LONG).show();
            }
        });
    }
}
