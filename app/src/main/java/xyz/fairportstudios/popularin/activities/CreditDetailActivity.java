package xyz.fairportstudios.popularin.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.adapters.PagerAdapter;
import xyz.fairportstudios.popularin.fragments.CreditBioFragment;
import xyz.fairportstudios.popularin.fragments.CreditFilmAsCastFragment;
import xyz.fairportstudios.popularin.fragments.CreditFilmAsCrewFragment;
import xyz.fairportstudios.popularin.statics.Popularin;

public class CreditDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reusable_toolbar_pager);

        // Binding
        TabLayout tabLayout = findViewById(R.id.tab_rtp_layout);
        Toolbar toolbar = findViewById(R.id.toolbar_rtp_layout);
        ViewPager viewPager = findViewById(R.id.pager_rtp_layout);

        // Extra
        Intent intent = getIntent();
        int creditID = intent.getIntExtra(Popularin.CREDIT_ID, 0);
        int viewPagerIndex = intent.getIntExtra(Popularin.VIEW_PAGER_INDEX, 0);

        // Toolbar
        toolbar.setTitle(R.string.credit);

        // Tab pager
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        pagerAdapter.addFragment(new CreditBioFragment(creditID), getString(R.string.bio));
        pagerAdapter.addFragment(new CreditFilmAsCastFragment(creditID), getString(R.string.cast));
        pagerAdapter.addFragment(new CreditFilmAsCrewFragment(creditID), getString(R.string.crew));
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(3);
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
}
