package xyz.fairportstudios.popularin.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.apis.popularin.get.UserFavoriteRequest;
import xyz.fairportstudios.popularin.models.Film;

public class FavoriteActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private TextView emptyFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        // Binding
        progressBar = findViewById(R.id.progress_bar_af_layout);
        emptyFavorite = findViewById(R.id.text_af_empty);
        RecyclerView recyclerView = findViewById(R.id.recycler_view_af_layout);
        Toolbar toolbar = findViewById(R.id.toolbar_af_layout);

        // Set-up list
        List<Film> filmList = new ArrayList<>();

        // Bundle
        Bundle bundle = getIntent().getExtras();
        String userID = Objects.requireNonNull(bundle).getString("USER_ID");

        // Mendapatkan data
        UserFavoriteRequest userFavoriteRequest = new UserFavoriteRequest(userID,
                this,
                filmList,
                recyclerView
        );

        userFavoriteRequest.sendRequest(new UserFavoriteRequest.JSONCallback() {
            @Override
            public void onSuccess() {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onEmptyFavorite() {
                progressBar.setVisibility(View.GONE);
                emptyFavorite.setVisibility(View.VISIBLE);
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
