package xyz.fairportstudios.popularin.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.adapters.PagerAdapter;
import xyz.fairportstudios.popularin.apis.popularin.delete.DeleteReviewRequest;
import xyz.fairportstudios.popularin.fragments.ReviewCommentFragment;
import xyz.fairportstudios.popularin.fragments.ReviewDetailFragment;

public class ReviewDetailActivity extends AppCompatActivity {
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.global_toolbar_pager);

        // Binding
        context = ReviewDetailActivity.this;
        TabLayout tabLayout = findViewById(R.id.tab_gtp_layout);
        Toolbar toolbar = findViewById(R.id.toolbar_gtp_layout);
        ViewPager viewPager = findViewById(R.id.pager_gtp_layout);

        // Extra
        Intent intent = getIntent();
        final String reviewID = intent.getStringExtra("REVIEW_ID");
        boolean isSelf = intent.getBooleanExtra("IS_SELF", false);

        // Toolbar
        toolbar.setTitle("Ulasan");
        if (isSelf) {
            toolbar.inflateMenu(R.menu.review_detail);
            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.menu_rd_edit:
                            Intent gotoEditReview = new Intent(context, EditReviewActivity.class);
                            gotoEditReview.putExtra("REVIEW_ID", reviewID);
                            startActivity(gotoEditReview);
                            return true;
                        case R.id.menu_rd_delete:
                            DeleteReviewRequest deleteReviewRequest = new DeleteReviewRequest(context, reviewID);
                            deleteReviewRequest.sendRequest(new DeleteReviewRequest.APICallback() {
                                @Override
                                public void onSuccess() {
                                    Toast.makeText(context, R.string.review_removed, Toast.LENGTH_SHORT).show();
                                    onBackPressed();
                                }

                                @Override
                                public void onError() {
                                    Toast.makeText(context, R.string.failed_remove_review, Toast.LENGTH_LONG).show();
                                }
                            });
                            return true;
                        default:
                            return true;
                    }
                }
            });
        }

        // Tab
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(), 0);
        pagerAdapter.addFragment(new ReviewDetailFragment(reviewID), "DETAIL");
        pagerAdapter.addFragment(new ReviewCommentFragment(reviewID), "KOMEN");
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        // Activity
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}
