package xyz.fairportstudios.popularin.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.adapters.PagerAdapter;
import xyz.fairportstudios.popularin.apis.tmdb.get.CreditDetailRequest;
import xyz.fairportstudios.popularin.fragments.CreditBioFragment;
import xyz.fairportstudios.popularin.fragments.CreditFilmAsCastFragment;
import xyz.fairportstudios.popularin.fragments.CreditFilmAsCrewFragment;
import xyz.fairportstudios.popularin.models.CreditDetail;
import xyz.fairportstudios.popularin.models.Film;
import xyz.fairportstudios.popularin.statics.Popularin;

public class CreditDetailActivity extends AppCompatActivity {
    // Variable member
    private LinearLayout mAnchorLayout;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reusable_toolbar_pager);

        // Context
        Context context = CreditDetailActivity.this;

        // Binding
        mAnchorLayout = findViewById(R.id.anchor_rtp_layout);
        mTabLayout = findViewById(R.id.tab_rtp_layout);
        mViewPager = findViewById(R.id.pager_rtp_layout);
        Toolbar toolbar = findViewById(R.id.toolbar_rtp_layout);

        // Extra
        Intent intent = getIntent();
        int creditID = intent.getIntExtra(Popularin.CREDIT_ID, 0);
        int viewPagerIndex = intent.getIntExtra(Popularin.VIEW_PAGER_INDEX, 0);

        // Toolbar
        toolbar.setTitle(R.string.credit);

        // Mendapatkan data
        getCreditDetail(context, creditID, viewPagerIndex);

        // Activity
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void getCreditDetail(final Context context, final int id, final int viewPagerIndex) {
        CreditDetailRequest creditDetailRequest = new CreditDetailRequest(context, id);
        creditDetailRequest.sendRequest(new CreditDetailRequest.Callback() {
            @Override
            public void onSuccess(CreditDetail creditDetail, List<Film> filmAsCastList, List<Film> filmAsCrewList) {
                // Tab pager
                PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
                pagerAdapter.addFragment(new CreditBioFragment(creditDetail), getString(R.string.bio));
                pagerAdapter.addFragment(new CreditFilmAsCastFragment(filmAsCastList), getString(R.string.cast));
                pagerAdapter.addFragment(new CreditFilmAsCrewFragment(filmAsCrewList), getString(R.string.crew));
                mViewPager.setAdapter(pagerAdapter);
                mViewPager.setOffscreenPageLimit(3);
                mViewPager.setCurrentItem(viewPagerIndex);
                mTabLayout.setupWithViewPager(mViewPager);
            }

            @Override
            public void onError(String message) {
                Snackbar.make(mAnchorLayout, message, Snackbar.LENGTH_INDEFINITE).setAction(R.string.retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getCreditDetail(context, id, viewPagerIndex);
                    }
                }).show();
            }
        });
    }
}
