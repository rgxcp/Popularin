package xyz.fairportstudios.popularin.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.fragments.AccountFragment;
import xyz.fairportstudios.popularin.fragments.AiringFragment;
import xyz.fairportstudios.popularin.fragments.EmptyAccountFragment;
import xyz.fairportstudios.popularin.fragments.GenreFragment;
import xyz.fairportstudios.popularin.fragments.ReviewFragment;
import xyz.fairportstudios.popularin.fragments.SearchFragment;
import xyz.fairportstudios.popularin.fragments.TimelineFragment;
import xyz.fairportstudios.popularin.preferences.Auth;
import xyz.fairportstudios.popularin.statics.Popularin;

public class MainActivity extends AppCompatActivity {
    // Variable untuk fitur double tap to exit
    private static final int TIME_INTERVAL = 2000;
    private static long TIME_BACK_PRESSED;

    // Variable member
    private Boolean isAuth;
    private Fragment selectedFragment;
    private final Context context = MainActivity.this;
    private final Fragment accountFragment = new AccountFragment();
    private final Fragment airingFragment = new AiringFragment();
    private final Fragment emptyAccountFragment = new EmptyAccountFragment();
    private final Fragment genreFragment = new GenreFragment();
    private final Fragment reviewFragment = new ReviewFragment();
    private final Fragment searchFragment = new SearchFragment();
    private final Fragment timelineFragment = new TimelineFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Auth
        isAuth = new Auth(context).isAuth();

        // Bottom navigation
        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation_am_layout);
        bottomNavigation.setOnNavigationItemSelectedListener(listener);

        // Menampilkan fragment otomatis sesuai kondisi
        if (isAuth) {
            selectedFragment = timelineFragment;
        } else {
            selectedFragment = genreFragment;
        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_am_container, selectedFragment)
                .commit();
    }

    @Override
    public void onBackPressed() {
        // Mengecek apakah masih ada back stack dalam activity
        Fragment mainBackStack = getSupportFragmentManager().findFragmentByTag(Popularin.MAIN_BACK_STACK);

        if (mainBackStack != null) {
            super.onBackPressed();
        } else {
            if (TIME_INTERVAL + TIME_BACK_PRESSED > System.currentTimeMillis()) {
                super.onBackPressed();
            } else {
                Toast.makeText(context, R.string.press_once_more_to_exit, Toast.LENGTH_SHORT).show();
            }
            TIME_BACK_PRESSED = System.currentTimeMillis();
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener listener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_bn_home:
                    if (isAuth) {
                        selectedFragment = timelineFragment;
                    } else {
                        selectedFragment = genreFragment;
                    }
                    break;
                case R.id.menu_bn_airing:
                    selectedFragment = airingFragment;
                    break;
                case R.id.menu_bn_review:
                    selectedFragment = reviewFragment;
                    break;
                case R.id.menu_bn_search:
                    selectedFragment = searchFragment;
                    break;
                case R.id.menu_bn_account:
                    if (isAuth) {
                        selectedFragment = accountFragment;
                    } else {
                        selectedFragment = emptyAccountFragment;
                    }
                    break;
            }

            if (selectedFragment != null) {
                // Menghapus semua back stack terlebih dahulu
                getSupportFragmentManager()
                        .popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                // Menampilkan fragment sesuai pilihan
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_am_container, selectedFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();
            }

            return true;
        }
    };
}
