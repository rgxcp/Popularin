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
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reusable_toolbar_pager);

        // Context
        Context context = ReviewActivity.this;

        // Binding
        toolbar = findViewById(R.id.toolbar_rtp_layout);
        TabLayout tabLayout = findViewById(R.id.tab_rtp_layout);
        ViewPager viewPager = findViewById(R.id.pager_rtp_layout);

        // Extra
        Intent intent = getIntent();
        Integer reviewID = intent.getIntExtra(Popularin.REVIEW_ID, 0);
        int viewPagerIndex = intent.getIntExtra(Popularin.VIEW_PAGER_INDEX, 0);
        boolean isSelf = intent.getBooleanExtra(Popularin.IS_SELF, false);

        // Toolbar
        toolbar.setTitle(R.string.review);
        if (isSelf) {
            addToolbarMenu(context, reviewID);
        }

        // Tab pager
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        pagerAdapter.addFragment(new ReviewDetailFragment(reviewID), getString(R.string.detail));
        pagerAdapter.addFragment(new ReviewCommentFragment(reviewID), getString(R.string.comment));
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(viewPagerIndex);
        tabLayout.setupWithViewPager(viewPager);

        // Activity
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void addToolbarMenu(final Context context, final Integer reviewID) {
        toolbar.inflateMenu(R.menu.review_detail);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_rd_edit:
                        editReview(context, reviewID);
                        return true;
                    case R.id.menu_rd_delete:
                        deleteReview(context, reviewID);
                        return true;
                    default:
                        return true;
                }
            }
        });
    }

    private void editReview(Context context, Integer reviewID) {
        Intent intent = new Intent(context, EditReviewActivity.class);
        intent.putExtra(Popularin.REVIEW_ID, reviewID);
        startActivity(intent);
    }

    private void deleteReview(final Context context, Integer reviewID) {
        DeleteReviewRequest deleteReviewRequest = new DeleteReviewRequest(context, reviewID);
        deleteReviewRequest.sendRequest(new DeleteReviewRequest.Callback() {
            @Override
            public void onSuccess() {
                onBackPressed();
                Toast.makeText(context, R.string.review_deleted, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String message) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
