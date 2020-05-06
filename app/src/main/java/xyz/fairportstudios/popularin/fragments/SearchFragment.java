package xyz.fairportstudios.popularin.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.adapters.PagerAdapter;

public class SearchFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        // Binding
        TabLayout tabLayout = view.findViewById(R.id.tab_fs_layout);
        ViewPager viewPager = view.findViewById(R.id.pager_fs_layout);

        // Tabbed
        PagerAdapter pagerAdapter = new PagerAdapter(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), 0);
        pagerAdapter.addFragment(new SearchFilmFragment(), "FILM");
        pagerAdapter.addFragment(new SearchUserFragment(), "PENGGUNA");
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }
}
