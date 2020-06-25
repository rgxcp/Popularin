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
    // Variable untuk fitur load
    private boolean mIsLoading = false;

    // Variable member
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reusable_toolbar_pager);

        // Context
        Context context = ReviewActivity.this;

        // Binding
        mToolbar = findViewById(R.id.toolbar_rtp_layout);
        TabLayout tabLayout = findViewById(R.id.tab_rtp_layout);
        ViewPager viewPager = findViewById(R.id.pager_rtp_layout);

        // Extra
        Intent intent = getIntent();
        int reviewID = intent.getIntExtra(Popularin.REVIEW_ID, 0);
        int viewPagerIndex = intent.getIntExtra(Popularin.VIEW_PAGER_INDEX, 0);
        boolean isSelf = intent.getBooleanExtra(Popularin.IS_SELF, false);

        // Toolbar
        mToolbar.setTitle(R.string.review);
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
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void addToolbarMenu(final Context context, final int id) {
        mToolbar.inflateMenu(R.menu.review_detail);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_rd_edit:
                        editReview(context, id);
                        return true;
                    case R.id.menu_rd_delete:
                        if (!mIsLoading) {
                            mIsLoading = true;
                            deleteReview(context, id);
                        }
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    private void editReview(Context context, int id) {
        Intent intent = new Intent(context, EditReviewActivity.class);
        intent.putExtra(Popularin.REVIEW_ID, id);
        startActivity(intent);
    }

    private void deleteReview(final Context context, int id) {
        DeleteReviewRequest deleteReviewRequest = new DeleteReviewRequest(context, id);
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

        // Memberhentikan loading
        mIsLoading = false;
    }
}
