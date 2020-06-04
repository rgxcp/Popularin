package xyz.fairportstudios.popularin.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.statics.Popularin;

public class SearchFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        // Binding
        final CardView cardFilm = view.findViewById(R.id.card_fs_film);
        final CardView cardUser = view.findViewById(R.id.card_fs_user);

        // Activity
        cardFilm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoSearchFilm();
            }
        });

        cardUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoSearchUser();
            }
        });

        return view;
    }

    private void gotoSearchFilm() {
        if (getFragmentManager() != null) {
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_am_container, new SearchFilmFragment(), Popularin.MAIN_BACK_STACK)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .addToBackStack(null)
                    .commit();
        }
    }

    private void gotoSearchUser() {
        if (getFragmentManager() != null) {
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_am_container, new SearchUserFragment(), Popularin.MAIN_BACK_STACK)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .addToBackStack(null)
                    .commit();
        }
    }
}
