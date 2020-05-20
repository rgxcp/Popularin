package xyz.fairportstudios.popularin.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.adapters.PagerAdapter;
import xyz.fairportstudios.popularin.fragments.FavoriteFromAllFragment;
import xyz.fairportstudios.popularin.fragments.FavoriteFromFollowingFragment;

public class FavoritedByActivity extends AppCompatActivity {

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
        String filmID = intent.getStringExtra("FILM_ID");

        // Toolbar
        toolbar.setTitle("Favorit");

        // Tab
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(), 0);
        pagerAdapter.addFragment(new FavoriteFromAllFragment(filmID), "SEMUA");
        pagerAdapter.addFragment(new FavoriteFromFollowingFragment(filmID), "MENGIKUTI");
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
