package xyz.fairportstudios.popularin.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.fragments.AiringFragment;
import xyz.fairportstudios.popularin.fragments.HomeFragment;
import xyz.fairportstudios.popularin.fragments.ProfileFragment;
import xyz.fairportstudios.popularin.fragments.ReviewFragment;
import xyz.fairportstudios.popularin.fragments.SearchFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Binding
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_am_layout);

        // Bottom navigation
        bottomNavigationView.setOnNavigationItemSelectedListener(listener);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_am_container, new HomeFragment())
                .commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener listener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;

            switch (item.getItemId()) {
                case R.id.menu_bn_home:
                    selectedFragment = new HomeFragment();
                    break;
                case R.id.menu_bn_airing:
                    selectedFragment = new AiringFragment();
                    break;
                case R.id.menu_bn_review:
                    selectedFragment = new ReviewFragment();
                    break;
                case R.id.menu_bn_search:
                    selectedFragment = new SearchFragment();
                    break;
                case R.id.menu_bn_profile:
                    selectedFragment = new ProfileFragment();
                    break;
            }

            if (selectedFragment != null) {
                FragmentManager fragmentManager = getSupportFragmentManager();

                // Menghapus semua stack
                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                // Memuat fragment
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_am_container, selectedFragment)
                        .commit();
            }

            return true;
        }
    };
}
