package xyz.fairportstudios.popularin.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.apis.popularin.get.UserFavoriteRequest;
import xyz.fairportstudios.popularin.models.Film;

public class UserFavoriteActivity extends AppCompatActivity {
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

        // Bundle
        Bundle bundle = getIntent().getExtras();
        String userID = Objects.requireNonNull(bundle).getString("USER_ID");

        // Toolbar
        toolbar.setTitle("Favorit");

        // List
        List<Film> filmList = new ArrayList<>();

        // GET
        UserFavoriteRequest userFavoriteRequest = new UserFavoriteRequest(this, filmList, recyclerView);
        String requestURL = userFavoriteRequest.getRequestURL(userID, 1);
        userFavoriteRequest.sendRequest(requestURL, new UserFavoriteRequest.JSONCallback() {
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
                Snackbar.make(layout, R.string.get_error, Snackbar.LENGTH_LONG).show();
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
