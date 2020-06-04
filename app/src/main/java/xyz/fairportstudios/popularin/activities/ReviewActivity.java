package xyz.fairportstudios.popularin.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.adapters.PagerAdapter;
import xyz.fairportstudios.popularin.apis.popularin.delete.DeleteReviewRequest;
import xyz.fairportstudios.popularin.fragments.ReviewCommentFragment;
import xyz.fairportstudios.popularin.fragments.ReviewDetailFragment;
import xyz.fairportstudios.popularin.statics.Popularin;

public class ReviewActivity extends AppCompatActivity {
    private Context context;
    private String reviewID;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reusable_toolbar_pager);

        // Binding
        context = ReviewActivity.this;
        toolbar = findViewById(R.id.toolbar_rtp_layout);
        TabLayout tabLayout = findViewById(R.id.tab_rtp_layout);
        ViewPager viewPager = findViewById(R.id.pager_rtp_layout);

        // Extra
        Intent intent = getIntent();
        reviewID = intent.getStringExtra(Popularin.REVIEW_ID);
        boolean isSelf = intent.getBooleanExtra(Popularin.IS_SELF, false);

        // Toolbar
        toolbar.setTitle(R.string.review);
        if (isSelf) {
            addToolbarMenu();
        }

        // Tab Pager
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        pagerAdapter.addFragment(new ReviewDetailFragment(reviewID), getString(R.string.detail));
        pagerAdapter.addFragment(new ReviewCommentFragment(reviewID), getString(R.string.comment));
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
        Intent intent = new Intent(context, EditReviewActivity.class);
        intent.putExtra(Popularin.REVIEW_ID, reviewID);
        startActivity(intent);
    }

    private void deleteReview() {
        DeleteReviewRequest deleteReviewRequest = new DeleteReviewRequest(context, reviewID);
        deleteReviewRequest.sendRequest(new DeleteReviewRequest.APICallback() {
            @Override
            public void onSuccess() {
                onBackPressed();
                Toast.makeText(context, R.string.review_deleted, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError() {
                Toast.makeText(context, R.string.failed_delete_review, Toast.LENGTH_LONG).show();
            }
        });
    }
}
