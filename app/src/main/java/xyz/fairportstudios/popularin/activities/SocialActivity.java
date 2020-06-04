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
import xyz.fairportstudios.popularin.fragments.FollowerFragment;
import xyz.fairportstudios.popularin.fragments.FollowingFragment;
import xyz.fairportstudios.popularin.fragments.MutualFragment;
import xyz.fairportstudios.popularin.preferences.Auth;
import xyz.fairportstudios.popularin.statics.Popularin;

public class SocialActivity extends AppCompatActivity {

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
        String userID = intent.getStringExtra(Popularin.USER_ID);
        boolean isSelf = intent.getBooleanExtra(Popularin.IS_SELF, false);

        // Auth
        boolean isAuth = new Auth(this).isAuth();

        // Toolbar
        toolbar.setTitle(R.string.social);

        // Tab Pager
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        pagerAdapter.addFragment(new FollowerFragment(userID), getString(R.string.follower));
        pagerAdapter.addFragment(new FollowingFragment(userID), getString(R.string.following));
        if (isAuth && !isSelf) {
            pagerAdapter.addFragment(new MutualFragment(userID), getString(R.string.mutual));
        }
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
