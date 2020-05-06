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
import xyz.fairportstudios.popularin.apis.popularin.get.UserReviewRequest;
import xyz.fairportstudios.popularin.models.UserReview;

public class UserReviewActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private TextView emptyReview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_review);

        // Binding
        progressBar = findViewById(R.id.progress_bar_aur_layout);
        emptyReview = findViewById(R.id.text_aur_empty);
        RecyclerView recyclerView = findViewById(R.id.recycler_view_aur_layout);
        Toolbar toolbar = findViewById(R.id.toolbar_aur_layout);

        // Set-up list
        List<UserReview> userReviewList = new ArrayList<>();

        // Bundle
        Bundle bundle = getIntent().getExtras();
        String userID = Objects.requireNonNull(bundle).getString("USER_ID");

        // Mendapatkan data
        UserReviewRequest userReviewRequest = new UserReviewRequest(
                userID,
                this,
                userReviewList,
                recyclerView
        );

        userReviewRequest.sendRequest(new UserReviewRequest.JSONCallback() {
            @Override
            public void onSuccess() {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onEmptyReview() {
                progressBar.setVisibility(View.GONE);
                emptyReview.setVisibility(View.VISIBLE);
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
