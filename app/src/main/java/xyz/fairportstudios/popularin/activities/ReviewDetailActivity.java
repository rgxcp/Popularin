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
import xyz.fairportstudios.popularin.services.Popularin;

public class ReviewDetailActivity extends AppCompatActivity {
    private Context context;
    private String reviewID;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reusable_toolbar_pager);

        // Binding
        context = ReviewDetailActivity.this;
        toolbar = findViewById(R.id.toolbar_rtp_layout);
        TabLayout tabLayout = findViewById(R.id.tab_rtp_layout);
        ViewPager viewPager = findViewById(R.id.pager_rtp_layout);

        // Extra
        Intent intent = getIntent();
        reviewID = intent.getStringExtra(Popularin.REVIEW_ID);
        final boolean isSelf = intent.getBooleanExtra(Popularin.IS_SELF, false);

        // Toolbar
        toolbar.setTitle(R.string.review);
        if (isSelf) {
            addToolbarMenu();
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

    private void addToolbarMenu() {
        toolbar.inflateMenu(R.menu.review_detail);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_rd_edit:
                        editReview();
                        return true;
                    case R.id.menu_rd_delete:
                        deleteReview();
                        return true;
                    default:
                        return true;
                }
            }
        });
    }

    private void editReview() {
        Intent gotoEditReview = new Intent(context, EditReviewActivity.class);
        gotoEditReview.putExtra(Popularin.REVIEW_ID, reviewID);
        startActivity(gotoEditReview);
    }

    private void deleteReview() {
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
    }
}
